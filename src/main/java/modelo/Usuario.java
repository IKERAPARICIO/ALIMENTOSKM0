package modelo;

import java.sql.SQLException;

import dao.AlimentoDAO;
import dao.TerrenoDAO;
import dao.UsuarioDAO;

public class Usuario {
	
	private int id;
	private String nick;
	private String password;
	private String nombre = "";
	private String apellidos = "";
	private String mail;
	private String ciudad;
	private String telefono;
	
	public Usuario() {
		
	}

	public Usuario(String nick, String nombre, String apellidos, String mail, String ciudad,
			String telefono) {
		this.nick = nick;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.mail = mail;
		this.ciudad = ciudad;
		this.telefono = telefono;
	}

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

	//polimorfismo, se trata en las clases hijo
	public void insertar() {
	}
	
	
	public boolean esUsuarioValido(String nick, String pass) {
		try {
			return UsuarioDAO.getInstance().validUser(nick, pass);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

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
			this.nombre = u.getNombre();
			this.apellidos = u.getApellidos();
			this.mail = u.getMail();
			this.ciudad = u.getCiudad();
			this.telefono = u.getTelefono();
		}
	}
	
}
