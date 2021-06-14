package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Estado;
import modelo.Paquete;
import modelo.Porcion;
import singleton.DBConnection;

public class PaqueteDAO {
	private Connection con = null;
	
	public static PaqueteDAO instance = null;

	public PaqueteDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static PaqueteDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new PaqueteDAO();
		return instance;
	}
	
	public int insert(Paquete p) throws SQLException {
		int idPaquete = 0;
		
		PreparedStatement ps = con
				.prepareStatement("INSERT INTO paquete (idTerreno, idAlimento, cantidadPropuesta, cantidadAceptada,"
						+ "cantidadDisponible, fechaPropuesta, estado) VALUES (?,?,?,?,?,?,?)");
		ps.setInt(1, p.getTerreno().getId());
		ps.setInt(2, p.getAlimento().getId());
		ps.setDouble(3, p.getCantidadPropuesta());
		ps.setDouble(4, 0);
		ps.setDouble(5, 0);
		ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
		ps.setString(7, p.getEstadoInicial());
		
		ps.executeUpdate();
		ps.close();

		return idPaquete;
	}
	
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM paquete WHERE idPaquete = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	public void update(Paquete p) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET cantidadPropuesta = ? WHERE idPaquete = ?");
		ps.setDouble(1, p.getCantidadPropuesta());
		ps.setInt(2, p.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	public ArrayList<Paquete> listMyPropuestas(String estado, int idProductor) throws SQLException {
		String selectSt = "SELECT * from paquete LEFT JOIN terreno ON paquete.idTerreno = terreno.idTerreno";
		String orderBy = " ORDER BY fechaPropuesta DESC";
		
		String whereSt = "";
		if (estado != "")
			whereSt = " WHERE estado = \"" + estado + "\"";
		if (idProductor != 0) {
			if (estado != "")
				whereSt += " AND";
			else
				whereSt += " WHERE";
			whereSt += " idUsuario = " + idProductor + "";
		}
		
		return listPaquetes(selectSt, whereSt, orderBy);
	}
	
	public ArrayList<Paquete> listPropuestas(String estado) throws SQLException {
		String selectSt = "SELECT * from paquete";
		String orderBy = " ORDER BY fechaPropuesta DESC";
		
		String whereSt = "";
		if (estado != "")
			whereSt = " WHERE estado = \"" + estado + "\"";
		
		return listPaquetes(selectSt, whereSt, orderBy);
	}
	
	public ArrayList<Paquete> listAlmacen(String estado) throws SQLException {
		String selectSt = "SELECT * from paquete";
		String orderBy = " ORDER BY idAlimento,cantidadDisponible";
		
		//define el WHERE con los estados
		String whereSt = "";
		if(estado != "")
			whereSt = " WHERE estado = \"" + estado + "\"";
		else {
			ArrayList<String> estados = this.getAlmacenStates();
			for(String est : estados){
				if (whereSt == "")
					whereSt += " WHERE estado IN (\"" + est + "\"";
				else
					whereSt += ",\"" + est + "\"";
			}
			whereSt += ")";
		}
		
		return listPaquetes(selectSt, whereSt, orderBy);
	}
	/**
	 * 
	 * @param fEstado: filtro para mostrar los estados GESTIONADO, PROPUESTO o vacío para TODOS
	 */
	public ArrayList<Paquete> listPaquetes(String selectSt, String whereSt, String orderBy) throws SQLException {
		//construye el select
		String sqlSt = selectSt.concat(whereSt).concat(orderBy);
		PreparedStatement ps = con.prepareStatement(sqlSt);
		ResultSet rs = ps.executeQuery();
		ArrayList<Paquete> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Paquete(rs.getInt("idPaquete"), rs.getInt("idTerreno"), rs.getInt("idAlimento"),
					rs.getDouble("cantidadPropuesta"), rs.getDouble("cantidadAceptada"), rs.getDouble("cantidadDisponible"),
					rs.getDate("fechaPropuesta"), rs.getDate("fechaAceptacion"), rs.getString("estado")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	public Paquete finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM paquete WHERE idPaquete = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Paquete result = null;
		if (rs.next()) {
			result = new Paquete(rs.getInt("idPaquete"), rs.getInt("idTerreno"), rs.getInt("idAlimento"),
					rs.getDouble("cantidadPropuesta"), rs.getDouble("cantidadAceptada"), rs.getDouble("cantidadDisponible"),
					rs.getDate("fechaPropuesta"), rs.getDate("fechaAceptacion"), rs.getString("estado"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	public void approve(int id, int cantidad) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET cantidadAceptada = ?, cantidadDisponible = ?,"
				+ "fechaAceptacion = ?, estado = ? WHERE idPaquete = ?");
		ps.setDouble(1, cantidad);
		ps.setDouble(2, cantidad);
		ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		ps.setString(4, Estado.ACEPTADO.toString());
		ps.setInt(5, id);
		
		ps.executeUpdate();
		ps.close();
	}
	
	public void disApprove(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET cantidadAceptada = ?, cantidadDisponible = ?,"
				+ "fechaAceptacion = ?, estado = ? WHERE idPaquete = ?");
		ps.setDouble(1, 0);
		ps.setDouble(2, 0);
		ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		ps.setString(4, Estado.RECHAZADO.toString());
		ps.setInt(5, id);
		
		ps.executeUpdate();
		ps.close();
	}
	
	public void cancel(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET estado = ? WHERE idPaquete = ?");
		ps.setString(1, Estado.ANULADO.toString());
		ps.setInt(2, id);
		
		ps.executeUpdate();
		ps.close();
	}
	
	public ArrayList<Porcion> getPorciones(int id) throws SQLException{	
		PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion LEFT JOIN cesta_porcion "
				+ "ON porcion.idPorcion = cesta_porcion.idPorcion WHERE idPaquete = ?");		
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		ArrayList<Porcion> result = null;
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Porcion(rs.getInt("idPorcion"), rs.getDouble("cantidad"), rs.getInt("idPaquete"), rs.getInt("idCesta")));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	public void updateCantidadDisponible(int id, Double cantidadNueva) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET cantidadDisponible = ? WHERE idPaquete = ?");
		ps.setDouble(1, cantidadNueva);
		ps.setInt(2, id);
		
		ps.executeUpdate();
		ps.close();
	}
	
	public ArrayList<Porcion> listPorcionesDisponibles() throws SQLException{	
		PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion WHERE idPorcion NOT IN (SELECT idPorcion FROM cesta_porcion)");		;
		ResultSet rs = ps.executeQuery();
		ArrayList<Porcion> result = null;
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
				result.add(new Porcion(rs.getInt("idPorcion"), rs.getDouble("cantidad"), rs.getInt("idPaquete")));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	public ArrayList<String> getPropuestasStates() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Estado.PROPUESTO.toString());
		lista.add(Estado.ACEPTADO.toString());
		lista.add(Estado.RECHAZADO.toString());
		lista.add(Estado.ANULADO.toString());
		return lista;
	}
	
	public ArrayList<String> getAlmacenStates() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Estado.ACEPTADO.toString());
		lista.add(Estado.ANULADO.toString());
		return lista;
	}
	
}
