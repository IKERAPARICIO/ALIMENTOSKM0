package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.Alimento;
import singleton.DBConnection;

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
	 */
	public int insert(Alimento a) throws SQLException {
		int idAlimento = 0;
		try {
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
		} catch (Exception e) {
			System.out.println("Error al introducir el alimento!");
		}
		return idAlimento;
	}
	
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM alimento WHERE idAlimento = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	public void update(Alimento a) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE alimento SET nombre = ?, medida = ? WHERE idAlimento = ?");
		ps.setString(1, a.getNombre());
		ps.setString(2, a.getMedida());
		ps.setInt(3, a.getId());

		ps.executeUpdate();
		ps.close();
	}
	
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
	 * actualiza el precio del alimento. Si tiene precio en el dia actual lo actualiza
	 */
	public void setCurrentPrice(int id, double precio) throws SQLException {
		if (hasPriceToday(id)) {
			updateTodayPrice(id, precio);
		}
		else {
			newTodayPrice(id, precio);
		}
	}
	
	private void newTodayPrice(int id, double precio) throws SQLException {
		try {			
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO precio (idAlimento, precioMedida, fecha) VALUES (?,?,?)");
			ps.setInt(1, id);
			ps.setDouble(2, precio);
			ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
			// Since Java 8
			//ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
			ps.executeUpdate();
			ps.close();
		 } catch (Exception e) {
			 System.out.println("Error al actualizar el precio del alimento!");
		 }
	}
	
	private void updateTodayPrice(int id, double precio) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE precio SET precioMedida = ? WHERE idAlimento = ? AND fecha = ?");
		ps.setDouble(1, precio);
		ps.setInt(2, id);
		ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));

		ps.executeUpdate();
		ps.close();
	}
	
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
