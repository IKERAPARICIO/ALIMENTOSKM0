package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Productor;
import modelo.Rol;
import modelo.Usuario;
import singleton.DBConnection;

/**
 * Clase para acceso a datos de Usuarios
 * @author Iker Aparicio
 */
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
	
	/**
	 * Comprueba que el password indicado corresponde al nick indicado
	 * @param nick: nick de usuario a validar
	 * @param pass: password de usuario a validar
	 * @return idValidUser: id del usuario validado, si no lo encuentra devuelve 0
	 * @throws SQLException
	 */
	public int idValidUser(String nick, String pass) throws SQLException {
		int id = 0;
		PreparedStatement ps = con.prepareStatement("SELECT idUsuario FROM usuario WHERE nick = ? AND pass = ?");
		ps.setString(1, nick);
		ps.setString(2, pass);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			id = rs.getInt("idUsuario");
		}
		rs.close();
		ps.close();
		
		return id;
	}
	
	/**
	 * Inserta el usuario pasado y devuelve el id que le corresponde
	 * @param u: Usuario
	 * @param dni: dni para el cado de productores
	 * @param direccion: direccion para el cado de productores
	 * @return id del usuario insertado
	 * @throws SQLException
	 */
	public int insert(Usuario u, String dni, String direccion) throws SQLException {
		int idUsuario = 0;

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

		return idUsuario;
	}
	
	/**
	 * Elimina el usuario con id pasado
	 * @param id: id del usuario a eliminar
	 * @throws SQLException
	 */
	public void delete(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM usuario WHERE idUsuario = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
	
	/**
	 * Actualiza el usuairo de id pasado con el resto de atributos
	 * @param u: usuairo a actualizar
	 * @param dni: dni nuevo de usuario
	 * @param direccion: direccion nueva de usuario
	 * @throws SQLException
	 */
	public void update(Usuario u, String dni, String direccion) throws SQLException {
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
	}
		
	/**
	 * 
	 * @param rol: rol de usuario
	 * @return Listado de usuarios segun el rol pasado ordenados por nombre. Si no pasa el rol se devuelven todos
	 * @throws SQLException
	 */
	public ArrayList<Usuario> listUsuarios(String rol) throws SQLException {
		String st = "SELECT * from usuario ";
		if(rol != "" && rol != null) {
			st += " WHERE rol = \"" + rol + "\"";
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
	
	/**
	 * @param id: id de usuario
	 * @return Usuario con id pasado
	 * @throws SQLException
	 */
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
	
	/**
	 * @param id: id del productor
	 * @return Devuelve el Productor de id pasado. Si no lo encuentra devuelve null
	 * @throws SQLException
	 */
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
	
	/**
	 * @param id: id del usuario
	 * @return Devuelve el rol del usuario con id pasado. Si no lo encuentra devuelve null
	 * @throws SQLException
	 */
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
	
	/**
	 * @return Listado de strings con los posibles valores del Rol
	 */
	public ArrayList<String> getRols() {
		ArrayList<String> lista = new ArrayList<String>();
		for (Rol rol : Rol.values()) { 
		    lista.add(rol.toString());
		}
		return lista;
	}
}
