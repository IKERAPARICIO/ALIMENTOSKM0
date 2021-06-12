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
	
	public int insert(Cesta c) throws SQLException {
		int idCesta = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO cesta (nombre, idUsuario, fechaCreacion) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS );
			ps.setString(1, c.getNombre());
			ps.setInt(2, c.getUsuarioId());
			ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
			
			int rowAffected = ps.executeUpdate();
			if(rowAffected == 1)
			{
				//obtiene el id del nuevo alimento insertado
				ResultSet rs = ps.getGeneratedKeys();
                if(rs.next())
                	idCesta = rs.getInt(1);
			}
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al introducir el cesta!");
		}
		return idCesta;
	}
	
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM cesta WHERE idCesta = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	public void update(Cesta c) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE cesta SET nombre = ?, idUsuario = ?, fechaCompra = ? "
													+ "WHERE idCesta = ?");
		ps.setString(1, c.getNombre());
		ps.setInt(2, c.getUsuarioId());
		ps.setDate(3, c.getFechaCompra());
		ps.setInt(4, c.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	public ArrayList<Cesta> listCestas() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from cesta ORDER BY fechaCreacion");
		ResultSet rs = ps.executeQuery();
		ArrayList<Cesta> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
				result.add(new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion"),
						rs.getDate("fechaCompra"), rs.getInt("idUsuario")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	public ArrayList<Cesta> listCestasDisponibles() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from cesta WHERE idUsuario = 0 ORDER BY fechaCreacion");
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
	
	public ArrayList<Cesta> listMyCestas(int idUsuario) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from cesta WHERE idUsuario = ? ORDER BY fechaCreacion");
		ps.setInt(1, idUsuario);
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
	
	public Cesta finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM cesta WHERE idCesta = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Cesta result = null;
		if (rs.next()) {
			result = new Cesta(rs.getInt("idCesta"), rs.getString("nombre"), rs.getDate("fechaCreacion"),
					 rs.getDate("fechaCompra"), rs.getInt("idUsuario"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
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
	
	public void removePorcion(int idCesta, int idPorcion) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM cesta_porcion WHERE idCesta = ? AND idPorcion = ?");
		ps.setInt(1, idCesta);
		ps.setInt(2, idPorcion);
		ps.executeUpdate();
		ps.close();
	}
	
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
