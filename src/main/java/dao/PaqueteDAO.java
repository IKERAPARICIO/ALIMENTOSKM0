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
	
	/*public int insert(Paquete p) throws SQLException {
		int idPaquete = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO paquete (idTerreno, idAlimento, cantidadPropuesta, cantidadAceptada,"
							+ "cantidadDisponible, fechaPropuesta, fechaAceptacion, estado) VALUES (?,?,?,?,?,?,?,?)");
			ps.setString(1, p.get
			ps.setInt(2, t.getProductorId());
			ps.setDouble(3, t.getMetros());
			ps.setString(4, t.getCiudad());
			ps.setString(5, t.getDireccion());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al introducir el paquete!");
		}
		return idPaquete;
	}*/
	
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM paquete WHERE idPaquete = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/*public void update(Paquete t) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET nombre = ?, idUsuario = ?, metros = ?,"
													+ " ciudad = ?, direccion = ? WHERE idPaquete = ?");
		ps.setString(1, t.getNombre());
		ps.setInt(2, t.getProductorId());
		ps.setDouble(3, t.getMetros());
		ps.setString(4, t.getCiudad());
		ps.setString(5, t.getDireccion());
		ps.setInt(6, t.getId());

		ps.executeUpdate();
		ps.close();
	}*/
	
	
	public ArrayList<Paquete> listPropuestas(String estado) throws SQLException {
		String orderBy = " ORDER BY fechaPropuesta";
		
		String whereSt = "";
		if(estado != "")
			whereSt = " WHERE estado = \"" + estado + "\"";
		
		return listPaquetes(whereSt, orderBy);
	}
	
	public ArrayList<Paquete> listAlmacen(String estado) throws SQLException {
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
		
		return listPaquetes(whereSt, orderBy);
	}
	/**
	 * 
	 * @param fEstado: filtro para mostrar los estados GESTIONADO, PROPUESTO o vac�o para TODOS
	 */
	public ArrayList<Paquete> listPaquetes(String whereSt, String orderBy) throws SQLException {
		String sqlSt = "SELECT * from paquete";
		//incluye el WHERE
		sqlSt += whereSt;
		//incluye el ORDER BY
		sqlSt += orderBy;
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
	
	public ArrayList<String> getPropuestasStates() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Estado.PROPUESTO.toString());
		lista.add(Estado.ACEPTADO.toString());
		lista.add(Estado.RECHAZADO.toString());
		lista.add(Estado.VENDIDO.toString());
		lista.add(Estado.ANULADO.toString());
		return lista;
	}
	
	public ArrayList<String> getAlmacenStates() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Estado.ACEPTADO.toString());
		lista.add(Estado.VENDIDO.toString());
		lista.add(Estado.ANULADO.toString());
		return lista;
	}
	
}