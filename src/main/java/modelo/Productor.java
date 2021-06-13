package modelo;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.TerrenoDAO;
import dao.UsuarioDAO;

public class Productor extends Usuario {
	
	private String dni;
	private String direccion;
	private Rol rol = Rol.PRODUCTOR;
	
	public Productor() {
		super();
	}

	public Productor(String nick, String password, String nombre, String apellidos, String mail, 
			String ciudad, String telefono, String sRol, String dni, String direccion) {
		super(nick, password, nombre, apellidos, mail, ciudad, telefono, sRol);
		this.dni = dni;
		this.direccion = direccion;
	}
	
	public Productor(int id, String nick, String password, String nombre, String apellidos, String mail, 
			String ciudad, String telefono, String sRol, String dni, String direccion) {
		super(id, nick, password, nombre, apellidos, mail, ciudad, telefono, sRol);
		this.dni = dni;
		this.direccion = direccion;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	//sobrescribe los metodos de la clase padre
	public void insertar() {
		super.insertar();
		try {
			UsuarioDAO.getInstance().insert(this,this.getDni(),this.getDireccion());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizar() {
		try {
			UsuarioDAO.getInstance().update(this,this.getDni(),this.getDireccion());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public void buscarID(int id) {
		Productor u = null;
		try {
			u = UsuarioDAO.getInstance().finIDProductor(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (u != null) {
			super.setId(u.getId());
			super.setNick(u.getNick());
			super.setNombre(u.getNombre());
			super.setPassword(u.getPassword());
			super.setApellidos(u.getApellidos());
			super.setMail(u.getMail());
			super.setCiudad(u.getCiudad());
			super.setTelefono(u.getTelefono());
			super.setRol(u.getRol());
			this.dni = u.getDni();
			this.direccion = u.getDireccion();
		}
	}
}
