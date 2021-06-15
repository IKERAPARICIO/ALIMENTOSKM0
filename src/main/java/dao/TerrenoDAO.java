package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelo.Alimento;
import modelo.Porcion;
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

		PreparedStatement ps = con
				.prepareStatement("INSERT INTO terreno (nombre, idUsuario, metros, ciudad, direccion) VALUES (?,?,?,?,?)", 
						Statement.RETURN_GENERATED_KEYS );
		ps.setString(1, t.getNombre());
		ps.setInt(2, t.getProductorId());
		ps.setDouble(3, t.getMetros());
		ps.setString(4, t.getCiudad());
		ps.setString(5, t.getDireccion());
		
		int rowAffected = ps.executeUpdate();
		if(rowAffected == 1)
		{
			//obtiene el id del nuevo alimento insertado
			ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
            	idTerreno = rs.getInt(1);
		}
		ps.close();

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
	
	public ArrayList<Terreno> listTerrenos(int idUsuario) throws SQLException {
		String st = "SELECT * from terreno";
		if (idUsuario != 0) {
			st += " WHERE idUsuario = " + idUsuario;
		}
		st += " ORDER BY nombre";
			
		PreparedStatement ps = con.prepareStatement(st);
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
	
	public void removeAlimento(int idTerreno, int idAlimento) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM terreno_alimento WHERE idTerreno = ? AND idAlimento = ?");
		ps.setInt(1, idTerreno);
		ps.setInt(2, idAlimento);
		ps.executeUpdate();
		ps.close();
	}
	
	public void addAlimento(int idTerreno, int idAlimento) throws SQLException {
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO terreno_alimento (idTerreno, idAlimento) VALUES (?,?)");
			ps.setInt(1, idTerreno);
			ps.setInt(2, idAlimento);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al agregar el alimento al terreno!");
		}
	}
	
	public ArrayList<Alimento> getAlimentos(int idTerreno) throws SQLException{	
		PreparedStatement ps = con.prepareStatement("SELECT * FROM terreno_alimento, alimento "
				+ "WHERE terreno_alimento.idAlimento = alimento.idAlimento AND idTerreno = ? ORDER BY alimento.nombre");		
		ps.setInt(1, idTerreno);
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
	
	public ArrayList<Alimento> getAlimentosDisponibles(int idTerreno) throws SQLException{	
		PreparedStatement ps = con.prepareStatement("SELECT * FROM alimento WHERE idAlimento NOT IN (SELECT idAlimento "
				+ "FROM terreno_alimento WHERE idTerreno = ?) ORDER BY nombre");
		ps.setInt(1, idTerreno);
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
	
}
