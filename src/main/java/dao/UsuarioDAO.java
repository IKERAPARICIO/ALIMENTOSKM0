package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Usuario;
import singleton.DBConnection;

public class UsuarioDAO {
	private Connection con = null;
	
	public static UsuarioDAO instance = null;

	public UsuarioDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static UsuarioDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new UsuarioDAO();
		return instance;
	}
	
	public boolean validUser(String nick, String pass) throws SQLException {
		boolean valid = false;
		
		PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE nick = ? AND pass = ?");
		ps.setString(1, nick);
		ps.setString(2, pass);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			valid = true;
		}
		rs.close();
		ps.close();
		
		return valid;
	}
	
	public int insert(Usuario u, String tipo, String dni, String direccion) throws SQLException {
		int idUsuario = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO usuario (nick, pass, nombre, apellidos, mail, ciudad, telefono,"
							+ "tipo, dni, direccion) VALUES (?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, u.getNick());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getNombre());
			ps.setString(4, u.getApellidos());
			ps.setString(5, u.getMail());
			ps.setString(6, u.getCiudad());
			ps.setString(7, u.getTelefono());
			ps.setString(8, tipo);
			ps.setString(9, dni);
			ps.setString(10, direccion);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al introducir el usuario!");
		}
		return idUsuario;
	}
	
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM usuario WHERE idUsuario = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/*public void update(Usuario u) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE usuario SET nick = ?, pass = ?, nombre = ?, apellidos = ?,"
													+ " mail = ?, ciudad = ?, telefono = ?, tipo = ?, dni = ?,"
													+ " direccion = ? WHERE idUsuario = ?");
		ps.setString(1, u.getNick());
		ps.setString(2, u.getPassword());
		ps.setString(3, u.getNombre());
		ps.setString(4, u.getApellidos());
		ps.setString(5, u.getMail());
		ps.setString(6, u.getCiudad());
		ps.setString(7, u.getTelefono());
		ps.setString(8, tipo);
		ps.setString(9, dni);
		ps.setString(10, direccion);
		ps.setInt(11, u.getId());

		ps.executeUpdate();
		ps.close();
	}*/
	
	public ArrayList<Usuario> listUsuarios() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from usuario ORDER BY nombre");
		ResultSet rs = ps.executeQuery();
		ArrayList<Usuario> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Usuario(rs.getString("nick"), rs.getString("nombre"), rs.getString("apellidos"),
					 rs.getString("mail"), rs.getString("ciudad"), rs.getString("telefono")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	public Usuario finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE idUsuario = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Usuario result = null;
		if (rs.next()) {
			result = new Usuario(rs.getString("nick"), rs.getString("nombre"), rs.getString("apellidos"),
					 rs.getString("mail"), rs.getString("ciudad"), rs.getString("telefono"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
}