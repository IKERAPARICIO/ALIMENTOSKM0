package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelo.Cesta;
import modelo.Porcion;
import singleton.DBConnection;

/**
 * Clase para acceso a datos de Cestas
 * @author Iker Aparicio
 */
public class CestaDAO {
	private Connection con = null;
	
	public static CestaDAO instance = null;

	public CestaDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static CestaDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new CestaDAO();
		return instance;
	}
	
	/**
	 * Inserta la cesta pasada y devuelve el id que le corresponde
	 * @param c: Cesta
	 * @return id de la cesta insertada
	 * @throws SQLException
	 */
	public int insert(Cesta c) throws SQLException {
		int idCesta = 0;
		PreparedStatement ps = con
				.prepareStatement("INSERT INTO cesta (nombre, idUsuario, fechaCreacion, preparada) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS );
		ps.setString(1, c.getNombre());
		ps.setInt(2, c.getUsuarioId());
		ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		ps.setBoolean(4, false);
		
		int rowAffected = ps.executeUpdate();
		if(rowAffected == 1)
		{
			//obtiene el id del nuevo alimento insertado
			ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
            	idCesta = rs.getInt(1);
		}
		ps.close();

		return idCesta;
	}
	
	/**
	 * Elimina la cesta con id pasado
	 * @param id: id de la cesta a eliminar
	 * @throws SQLException
	 */
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM cesta WHERE idCesta = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Actualiza la cesta de id pasado con el resto de atributos
	 * @param c: cesta a actualizar
	 * @throws SQLException
	 */
	public void update(Cesta c) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE cesta SET nombre = ?, idUsuario = ?, fechaCompra = ?, "
													+ "preparada = ? WHERE idCesta = ?");
		ps.setString(1, c.getNombre());
		ps.setInt(2, c.getUsuarioId());
		ps.setDate(3, c.getFechaCompra());
		ps.setBoolean(4, c.isPreparada());
		ps.setInt(5, c.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Devuelve el listado de cestas completo ordenadas por fecha de creacion
	 * @return Listado de cestas
	 * @throws SQLException
	 */
	public ArrayList<Cesta> listCestas() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from cesta ORDER BY fechaCreacion DESC");
		ResultSet rs = ps.executeQuery();
		ArrayList<Cesta> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
				result.add(new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion"),
						rs.getDate("fechaCompra"), rs.getBoolean("preparada"), rs.getInt("idUsuario")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Devuelve el Listado de cestas disponibles (preparada a TRUE) ordenadas por fecha de creacion
	 * @return Listado de cestas
	 * @throws SQLException
	 */
	public ArrayList<Cesta> listCestasDisponibles() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from cesta WHERE idUsuario = 0 "
													+ "AND preparada = ? ORDER BY fechaCreacion");
		ps.setBoolean(1, true);
		ResultSet rs = ps.executeQuery();
		ArrayList<Cesta> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
				result.add(new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion")));
		}
		
		rs.close();
		ps.close();
		return result;
	}

	/**
	 * Devuelve el listado de cestas compradas por el usuario pasado ordenadas por fecha de creacion
	 * @param idUsuario: id del usuario consumidor
	 * @return Listado de cestas
	 * @throws SQLException
	 */
	public ArrayList<Cesta> listMyCestas(int idUsuario) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from cesta WHERE idUsuario = ? ORDER BY fechaCreacion DESC");
		ps.setInt(1, idUsuario);
		ResultSet rs = ps.executeQuery();
		ArrayList<Cesta> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion"), rs.getDate("fechaCompra")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Devuelve el listado de cestas en las que se ha incluido algun producto del usuario pasado ordenadas por fecha de creacion
	 * @param idUsuario: id del usuario productor
	 * @return Listado de cestas
	 * @throws SQLException
	 */
	public ArrayList<Cesta> listCestasMyTerrenos(int idUsuario) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT DISTINCT cesta.* FROM CESTA "
								+ "LEFT JOIN cesta_porcion ON cesta.idCesta = cesta_porcion.idCesta "
								+ "LEFT JOIN porcion ON cesta_porcion.idPorcion = porcion.idPorcion "
								+ "LEFT JOIN paquete ON porcion.idPaquete = paquete.idPaquete "
								+ "LEFT JOIN terreno ON paquete.idTerreno = terreno.idTerreno "
								+ "WHERE terreno.idUsuario = ? AND cesta.preparada = ? ORDER BY fechaCreacion DESC");
		ps.setInt(1, idUsuario);
		ps.setBoolean(2, true);
		ResultSet rs = ps.executeQuery();
		ArrayList<Cesta> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion"),
					rs.getDate("fechaCompra"), rs.getBoolean("preparada"), rs.getInt("idUsuario")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Devuelve la Cesta con id pasado
	 * @param id: id de la cesta
	 * @return Cesta con id pasado
	 * @throws SQLException
	 */
	public Cesta finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM cesta WHERE idCesta = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Cesta result = null;
		if (rs.next()) {
			result = new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion"),
					 rs.getDate("fechaCompra"), rs.getBoolean("preparada"), rs.getInt("idUsuario"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Devuelve el listado de porciones incluidas en la cesta pasada
	 * @param id: id de la cesta
	 * @return Listado de porciones
	 * @throws SQLException
	 */
	public ArrayList<Porcion> getPorciones(int id) throws SQLException{	
		PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion LEFT JOIN cesta_porcion "
				+ "ON porcion.idPorcion = cesta_porcion.idPorcion WHERE idCesta = ?");		
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
	 * Elimina la porcion indicada de la cesta indicada
	 * @param idCesta: id de la cesta
	 * @param idPorcion: id de la porcion
	 * @throws SQLException
	 */
	public void removePorcion(int idCesta, int idPorcion) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM cesta_porcion WHERE idCesta = ? AND idPorcion = ?");
		ps.setInt(1, idCesta);
		ps.setInt(2, idPorcion);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Incluye la porcion indicada en la cesta indicada
	 * @param idCesta: id de la cesta
	 * @param idPorcion: id de la porcion
	 * @throws SQLException
	 */
	public void addPorcion(int idCesta, int idPorcion) throws SQLException {
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO cesta_porcion (idCesta, idPorcion) VALUES (?,?)");
			ps.setInt(1, idCesta);
			ps.setInt(2, idPorcion);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al agregar la porción a la cesta!");
		}
	}
	
	/**
	 * Incluye el usuario indicado como comprador en la cesta indicada y pone la fecha actual como fecha de compra
	 * @param idCesta: id de la cesta
	 * @param idUsuario: id del usuario
	 * @throws SQLException
	 */
	public void addUser(int idCesta, int idUsuario) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE cesta SET idUsuario = ?, fechaCompra = ? "
													+ "WHERE idCesta = ?");
		ps.setInt(1, idUsuario);
		ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
		ps.setInt(3, idCesta);
		ps.executeUpdate();
		ps.close();
	}
}
