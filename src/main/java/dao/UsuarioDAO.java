package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Estado;
import modelo.Productor;
import modelo.Rol;
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
	
	public int insert(Usuario u, String dni, String direccion) throws SQLException {
		int idUsuario = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO usuario (nick, pass, nombre, apellidos, mail, ciudad, telefono,"
							+ "rol, dni, direccion) VALUES (?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, u.getNick());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getNombre());
			ps.setString(4, u.getApellidos());
			ps.setString(5, u.getMail());
			ps.setString(6, u.getCiudad());
			ps.setString(7, u.getTelefono());
			ps.setString(8, u.getRolName());
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
	
	public void update(Usuario u, String dni, String direccion) throws SQLException {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE usuario SET nick = ?, pass = ?, nombre = ?, apellidos = ?,"
													+ " mail = ?, ciudad = ?, telefono = ?, rol = ?, dni = ?,"
													+ " direccion = ? WHERE idUsuario = ?");
			ps.setString(1, u.getNick());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getNombre());
			ps.setString(4, u.getApellidos());
			ps.setString(5, u.getMail());
			ps.setString(6, u.getCiudad());
			ps.setString(7, u.getTelefono());
			ps.setString(8, u.getRolName());
			ps.setString(9, dni);
			ps.setString(10, direccion);
			ps.setInt(11, u.getId());
	
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			System.out.println("Error al actualizar el usuario!");
		}
	}
	
	public ArrayList<Usuario> listUsuarios(String rol) throws SQLException {
		String st = "SELECT * from usuario ";
		if(rol != "") {
			st += " WHERE tipo = \"" + rol + "\"";
		}
		st += " ORDER BY nombre";
		
		PreparedStatement ps = con.prepareStatement(st);
		ResultSet rs = ps.executeQuery();
		ArrayList<Usuario> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			
			result.add(new Usuario(rs.getInt("idUsuario"), rs.getString("nick"), rs.getString("pass"),
					rs.getString("nombre"), rs.getString("apellidos"), rs.getString("mail"), rs.getString("ciudad"),
					rs.getString("telefono"), rs.getString("rol")));
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
			result = new Usuario(rs.getInt("idUsuario"), rs.getString("nick"), rs.getString("pass"),
					rs.getString("nombre"), rs.getString("apellidos"), rs.getString("mail"), rs.getString("ciudad"),
					rs.getString("telefono"), rs.getString("rol"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	public Productor finIDProductor(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE idUsuario = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Productor result = null;
		if (rs.next()) {
			result = new Productor(rs.getInt("idUsuario"), rs.getString("nick"), rs.getString("pass"),
					rs.getString("nombre"), rs.getString("apellidos"), rs.getString("mail"), rs.getString("ciudad"),
					rs.getString("telefono"), rs.getString("rol"), rs.getString("dni"), rs.getString("direccion"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	public String getRol(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT rol FROM usuario WHERE idUsuario = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		String result = "";
		if (rs.next()) {
			result = rs.getString("rol");
		}
		rs.close();
		ps.close();
		return result;
	}
	
	public ArrayList<String> getRols() {
		ArrayList<String> lista = new ArrayList<String>();
		for (Rol rol : Rol.values()) { 
		    lista.add(rol.toString());
		}
		return lista;
	}
	
}
