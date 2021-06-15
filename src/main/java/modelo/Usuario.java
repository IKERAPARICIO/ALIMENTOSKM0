package modelo;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.UsuarioDAO;

/**
 * Clase para trabajar con usuarios
 * @author Iker Aparicio
 */
public class Usuario {
		
	private int id;
	private String nick;
	private String password;
	private String nombre = "";
	private String apellidos = "";
	private String mail;
	private String ciudad;
	private String telefono;
	private Rol rol;
	
	//****************** Constructores ******************
	public Usuario() {
	}

	public Usuario(String nick, String password, String nombre, String apellidos, String mail, 
			String ciudad, String telefono, String sRol) {
		this.nick = nick;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.mail = mail;
		this.ciudad = ciudad;
		this.telefono = telefono;
		this.rol = this.getRolFromString(sRol);
	}
	
	public Usuario(int id, String nick, String password, String nombre, String apellidos, String mail, 
			String ciudad, String telefono, String sRol) {
		this.id = id;
		this.nick = nick;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.mail = mail;
		this.ciudad = ciudad;
		this.telefono = telefono;
		this.rol = this.getRolFromString(sRol);
	}

	//****************** Getters y Setters ******************
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	public String getRolName() {
		return rol.toString();
	}

	//****************** Métodos DAO ******************
	public void insertar() throws SQLException {
		UsuarioDAO.getInstance().insert(this,"","");
	}
	
	public void eliminar(int id) throws SQLException {
		UsuarioDAO.getInstance().delete(id);
	}
	
	public void actualizar() throws SQLException {
		UsuarioDAO.getInstance().update(this,"","");
	}
	
	/**
	 * Carga el objeto que tiene el id pasado
	 * @param id
	 */
	public void buscarID(int id) {
		Usuario u = null;
		try {
			u = UsuarioDAO.getInstance().finID(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (u != null) {
			this.id = u.getId();
			this.nick = u.getNick();
			this.password = u.getPassword();
			this.nombre = u.getNombre();
			this.apellidos = u.getApellidos();
			this.mail = u.getMail();
			this.ciudad = u.getCiudad();
			this.telefono = u.getTelefono();
			this.rol = u.getRol();
		}
	}
	
	/**
	 * @return nombre completo (nombre y apellidos) del Usuario
	 */
	public String getNombreCompleto () {
		return this.getNombre() + " " + this.getApellidos();
	}
	
	/**
	 * Comprueba si el password y usuario son correctos
	 * @param nick: nick a comprobar
	 * @param pass: password a comprobar
	 * @return id del usuario si es correcto, 0 si no existe en la BBDD
	 */
	public int idUsuarioValido(String nick, String pass) {
		int id = 0;
		try {
			id = UsuarioDAO.getInstance().idValidUser(nick, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * @param sRol: rol de usuario
	 * @return lista de usuarios con el rol indicado, todos los usuarios si el rol esta vacio y null si no encuentra usuarios
	 */
	public ArrayList<Usuario> obtenerUsuarios(String sRol) {
		ArrayList<Usuario> lista = null;
		try {
			lista = UsuarioDAO.getInstance().listUsuarios(sRol);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	/**
	 * @param sRol: valor pasado
	 * @return Rol que tiene el valor pasado
	 */
	public Rol getRolFromString(String sRol) {
		if (sRol.equals(Rol.CONSUMIDOR.toString())) {
			return Rol.CONSUMIDOR;
		}
		else if (sRol.equals(Rol.PRODUCTOR.toString())) {
			return Rol.PRODUCTOR;
		}
		else if (sRol.equals(Rol.GESTOR.toString())) {
			return Rol.GESTOR;
		}
		else {
			return null;
		}
	}
	
	/**
	 * @param id: id de Usuario
	 * @return valor del rol del Usuario pasado
	 */
	public String obtenerRol(int id) {
		String sRol = "";
		try {
			sRol = UsuarioDAO.getInstance().getRol(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sRol;
	}
	
	/**
	 * Para establecer los permisos en las paginas jsp se establece un sistema numérico de prioridades
	 * A más valor más privilegios: 1 para Invitado, 3 Consumidor, 5 Productor y 9 Administrador
	 * @return devuelve un entero con el valor del priviegio del usuario
	 */
	public int obtenerPermisosRol() {
		if (this.rol.equals(Rol.CONSUMIDOR)) {
			return 3;
		}
		else if (this.rol.equals(Rol.PRODUCTOR)) {
			return 5;
		}
		else if (this.rol.equals(Rol.GESTOR)) {
			return 9;
		}
		else {
			return 1;
		}
	}
}
