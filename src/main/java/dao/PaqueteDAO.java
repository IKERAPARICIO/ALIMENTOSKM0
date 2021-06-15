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

/**
 * Clase para acceso a datos de Paquetes
 * @author Iker Aparicio
 */
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
	
	/**
	 * Inserta el paquete pasado y devuelve el id que le corresponde
	 * @param p: Paquete
	 * @return id del paquete insertado
	 * @throws SQLException
	 */
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
	
	/**
	 * Elimina el paquete con id pasado
	 * @param id: id del paquete a eliminar
	 * @throws SQLException
	 */
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM paquete WHERE idPaquete = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Actualiza el paquete de id pasado con el resto de atributos
	 * @param p: paquete a actualizar
	 * @throws SQLException
	 */
	public void update(Paquete p) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET cantidadPropuesta = ? WHERE idPaquete = ?");
		ps.setDouble(1, p.getCantidadPropuesta());
		ps.setInt(2, p.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * 
	 * @param estado: filtro para estado de la propuesta
	 * @param idProductor: id del productor
	 * @return Listado de propuestas del productor pasado en el estado indicado
	 * @throws SQLException
	 */
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
	
	/**
	 * 
	 * @param estado: filtro para estado de la propuesta
	 * @return Listado de todas las propuestas en el estado indicado
	 * @throws SQLException
	 */
	public ArrayList<Paquete> listPropuestas(String estado) throws SQLException {
		String selectSt = "SELECT * from paquete";
		String orderBy = " ORDER BY fechaPropuesta DESC";
		
		String whereSt = "";
		if (estado != "")
			whereSt = " WHERE estado = \"" + estado + "\"";
		
		return listPaquetes(selectSt, whereSt, orderBy);
	}
	
	/**
	 * 
	 * @param disponible: true si cantidad disponible tiene que ser > 0, false para ver todos
	 * @return Listado de paquetes segun la disponibilidad indicada
	 * @throws SQLException
	 */
	public ArrayList<Paquete> listAlmacen(Boolean disponible) throws SQLException {
		String selectSt = "SELECT * from paquete";
		String orderBy = " ORDER BY fechaPropuesta DESC,cantidadDisponible DESC";
		
		String whereSt = "";
		if(disponible)
			whereSt = " WHERE cantidadDisponible > 0";
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
	 * @param selectSt: sentencia select
	 * @param whereSt: sentencia where
	 * @param orderBy: sentencia order
	 * @return Listado de paquetes segun las sentencias select, where y order indicadas
	 * @throws SQLException
	 */
	private ArrayList<Paquete> listPaquetes(String selectSt, String whereSt, String orderBy) throws SQLException {
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
	
	/**
	 * @param id: id del paquete
	 * @return Paquete con id pasado
	 * @throws SQLException
	 */
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
	
	/**
	 * Aprueba el paquete pasado con la cantidad indicada y pone la fecha actual como fecha de aceptacion
	 * @param id: id del paquete
	 * @param cantidad: cantidad aceptada
	 * @throws SQLException
	 */
	public void approve(int id, Double cantidad) throws SQLException {
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
	
	/**
	 * Rechaza el paquete pasado con la cantidad indicada y pone la fecha actual como fecha de aceptacion
	 * @param id: id del paquete
	 * @throws SQLException
	 */
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
	
	/**
	 * Archiva el paquete pasado y deja la cantidad disponible a 0
	 * @param id: id del paquete
	 * @throws SQLException
	 */
	public void archive(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET estado = ?, cantidadDisponible = ? WHERE idPaquete = ?");
		ps.setString(1, Estado.FINALIZADO.toString());
		ps.setInt(2, 0);
		ps.setInt(3, id);
		
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * @param id: id del paquete
	 * @return Listado de porciones del paquete indicado
	 * @throws SQLException
	 */
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
	
	/**
	 * Actualiza la cantidad disponible indicada del paquete indicado
	 * @param id: id del paquete
	 * @param cantidadNueva: cantidad a actualizar
	 * @throws SQLException
	 */
	public void updateCantidadDisponible(int id, Double cantidadNueva) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE paquete SET cantidadDisponible = ? WHERE idPaquete = ?");
		ps.setDouble(1, cantidadNueva);
		ps.setInt(2, id);
		
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * @return Listado de porciones disponibles, todavía no se han incluido en ninguna cesta
	 * @throws SQLException
	 */
	public ArrayList<Porcion> listPorcionesDisponibles() throws SQLException{	
		PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion WHERE idPorcion NOT IN (SELECT idPorcion FROM cesta_porcion)");
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
	
	/**
	 * @return Listado de strings de posibles estados para las propuestas
	 */
	public ArrayList<String> getPropuestasStates() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Estado.PROPUESTO.toString());
		lista.add(Estado.ACEPTADO.toString());
		lista.add(Estado.RECHAZADO.toString());
		lista.add(Estado.FINALIZADO.toString());
		return lista;
	}
	
	/**
	 * @return Listado de strings de posibles estados para los paquetes almacenados
	 */
	public ArrayList<String> getAlmacenStates() {
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Estado.ACEPTADO.toString());
		lista.add(Estado.FINALIZADO.toString());
		return lista;
	}
}
