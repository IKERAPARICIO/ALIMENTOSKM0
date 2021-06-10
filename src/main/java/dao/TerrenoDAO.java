package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Terreno;
import singleton.DBConnection;

public class TerrenoDAO {
	private Connection con = null;
	
	public static TerrenoDAO instance = null;

	public TerrenoDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static TerrenoDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new TerrenoDAO();
		return instance;
	}
	
	public int insert(Terreno t) throws SQLException {
		int idTerreno = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO terreno (nombre, idUsuario, metros, ciudad, direccion) VALUES (?,?,?,?,?)");
			ps.setString(1, t.getNombre());
			ps.setInt(2, t.getProductorId());
			ps.setDouble(3, t.getMetros());
			ps.setString(4, t.getCiudad());
			ps.setString(5, t.getDireccion());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al introducir el terreno!");
		}
		return idTerreno;
	}
	
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM terreno WHERE idTerreno = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	public void update(Terreno t) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE terreno SET nombre = ?, idUsuario = ?, metros = ?,"
													+ " ciudad = ?, direccion = ? WHERE idTerreno = ?");
		ps.setString(1, t.getNombre());
		ps.setInt(2, t.getProductorId());
		ps.setDouble(3, t.getMetros());
		ps.setString(4, t.getCiudad());
		ps.setString(5, t.getDireccion());
		ps.setInt(6, t.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	public ArrayList<Terreno> listTerrenos() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from terreno ORDER BY nombre");
		ResultSet rs = ps.executeQuery();
		ArrayList<Terreno> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Terreno(rs.getInt("idTerreno"), rs.getString("nombre"), rs.getDouble("metros"),
					 rs.getString("ciudad"), rs.getString("direccion"), rs.getInt("idUsuario")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	public Terreno finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM terreno WHERE idTerreno = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Terreno result = null;
		if (rs.next()) {
			result = new Terreno(rs.getInt("idTerreno"), rs.getString("nombre"), rs.getDouble("metros"),
					 rs.getString("ciudad"), rs.getString("direccion"), rs.getInt("idUsuario"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
}
