package modelo;

import java.sql.SQLException;

import dao.UsuarioDAO;

public class Consumidor extends Usuario {
	
	private Rol rol = Rol.CONSUMIDOR;
	
	public Consumidor() {
		super();
	}
	
	public Consumidor(String nick, String password, String nombre, String apellidos, String mail, String ciudad,
			String telefono, String sRol) {
		super(nick,password,nombre,apellidos,mail,ciudad,telefono,sRol);
	}
	
	public Consumidor(int id, String nick, String password, String nombre, String apellidos, String mail, String ciudad,
			String telefono, String sRol) {
		super(id,nick,password,nombre,apellidos,mail,ciudad,telefono,sRol);
	}
	
	public void insertar() {
		String dni = "";
		String direccion = "";
		try {
			UsuarioDAO.getInstance().insert(this,dni,direccion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
