package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.Alimento;
import singleton.DBConnection;

/**
 * Clase para acceso a datos de Alimentos
 * @author Family
 */
public class AlimentoDAO {
	private Connection con = null;
	
	public static AlimentoDAO instance = null;

	public AlimentoDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static AlimentoDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new AlimentoDAO();
		return instance;
	}
	
	/**
	 * Inserta el alimento pasado y devuelve el id que le corresponde
	 * @param a: Alimento
	 * @return id del alimento insertado
	 * @throws SQLException
	 */
	public int insert(Alimento a) throws SQLException {
		int idAlimento = 0;

		PreparedStatement ps = con
				.prepareStatement("INSERT INTO alimento (nombre, medida) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS );
		ps.setString(1, a.getNombre());
		ps.setString(2, a.getMedida());
		int rowAffected = ps.executeUpdate();
		if(rowAffected == 1)
		{
			//obtiene el id del nuevo alimento insertado
			ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
            	idAlimento = rs.getInt(1);
		}
		ps.close();

		return idAlimento;
	}
	
	/**
	 * Elimina el alimento con id pasado
	 * @param id: id del alimento a eliminar
	 * @throws SQLException
	 */
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM alimento WHERE idAlimento = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Actualiza el alimento de id pasado con el resto de atributos
	 * @param a: alimento a actualizar
	 * @throws SQLException
	 */
	public void update(Alimento a) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE alimento SET nombre = ?, medida = ? WHERE idAlimento = ?");
		ps.setString(1, a.getNombre());
		ps.setString(2, a.getMedida());
		ps.setInt(3, a.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * @return Listado de alimentos completo ordenados por nombre
	 * @throws SQLException
	 */
	public ArrayList<Alimento> listAlimentos() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from alimento ORDER BY nombre");
		ResultSet rs = ps.executeQuery();
		ArrayList<Alimento> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Alimento(rs.getInt("idAlimento"), rs.getString("nombre"), rs.getString("medida")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * @param id: id de alimento
	 * @return Alimento con id pasado
	 * @throws SQLException
	 */
	public Alimento finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM alimento WHERE idAlimento = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Alimento result = null;
		if (rs.next()) {
			result = new Alimento(rs.getInt("idAlimento"), rs.getString("nombre"), rs.getString("medida"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Obtiene el precio actual del alimento indicado
	 * @param id: id del alimento
	 * @return precio actual
	 * @throws SQLException
	 */
	public double getCurrentPrice(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT precioMedida FROM precio WHERE idAlimento = ? ORDER BY fecha DESC limit 1");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		double result = 0;
		if (rs.next()) {
			result = rs.getDouble("precioMedida");
		}
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Obtiene el precio del alimento indicado en la fecha indicada
	 * @param id: id del alimento
	 * @param fecha: fecha a buscar el precio
	 * @return precio a fecha pasada
	 * @throws SQLException
	 */
	public double getDatePrice(int id, Date fecha) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT precioMedida FROM precio WHERE idAlimento = ? "
				+ "AND fecha <= ? ORDER BY fecha DESC limit 1");
		ps.setInt(1, id);
		ps.setDate(2, fecha);
		ResultSet rs = ps.executeQuery();
		double result = 0;
		if (rs.next()) {
			result = rs.getDouble("precioMedida");
		}
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Comprueba que el alimento tenga un precio en la fecha actual
	 * @param id: id del alimento
	 * @return true si tiene precio y false si no
	 * @throws SQLException
	 */
	public boolean hasPriceToday(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT precioMedida FROM precio WHERE idAlimento = ? AND fecha = ?");
		ps.setInt(1, id);
		ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
		ResultSet rs = ps.executeQuery();
		boolean result = false;
		if (rs.next()) {
			result = true;
		}
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * Pone precio al alimento indicado con fecha de hoy. Si tiene precio en el dia actual lo actualiza, si no lo inserta como nuevo.
	 * @param id: id del alimento
	 * @param precio: precio para el alimento
	 * @throws SQLException
	 */
	public void setCurrentPrice(int id, double precio) throws SQLException {
		if (hasPriceToday(id)) {
			updateTodayPrice(id, precio);
		}
		else {
			newTodayPrice(id, precio);
		}
	}
	
	/**
	 * Inserta el precio al alimento indicado con fecha de hoy
	 * @param id: id del alimento
	 * @param precio: precio para el alimento
	 * @throws SQLException
	 */
	private void newTodayPrice(int id, double precio) throws SQLException {
		try {			
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO precio (idAlimento, precioMedida, fecha) VALUES (?,?,?)");
			ps.setInt(1, id);
			ps.setDouble(2, precio);
			ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
			ps.executeUpdate();
			ps.close();
		 } catch (Exception e) {
			 System.out.println("Error al actualizar el precio del alimento!");
		 }
	}
	
	/**
	 * Actualiza el precio al alimento indicado con fecha de hoy
	 * @param id: id del alimento
	 * @param precio: precio para el alimento
	 * @throws SQLException
	 */
	private void updateTodayPrice(int id, double precio) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE precio SET precioMedida = ? WHERE idAlimento = ? AND fecha = ?");
		ps.setDouble(1, precio);
		ps.setInt(2, id);
		ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));

		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Comprueba si el alimento pasado esta incluido en algún terreno
	 * @param id: id del terreno
	 * @return true si está en algún terreno y false si no lo está
	 * @throws SQLException
	 */
	public boolean hasTerrenos(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM terreno_alimento WHERE idAlimento = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		boolean result = false;
		if (rs.next()) {
			result = true;
		}
		rs.close();
		ps.close();
		return result;
	}
	
	/**
	 * @param id: id del alimento
	 * @return Historico de precios en un Map con fecha y precio
	 * @throws SQLException
	 */
	public Map getPriceHistory(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM precio WHERE idAlimento = ? ORDER BY fecha DESC");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Map<String,String> result = null;
		while (rs.next()) {
			if (result == null)
				result = new HashMap<>();
			result.put( rs.getString("fecha"), rs.getString("precioMedida"));
		}
		rs.close();
		ps.close();
		return result;
	}
}
