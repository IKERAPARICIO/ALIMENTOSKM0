package modelo;

import java.sql.SQLException;

import dao.UsuarioDAO;

public class Consumidor extends Usuario {
	
	public Consumidor() {
		super();
	}
	
	public Consumidor(String nick, String nombre, String apellidos, String mail, String ciudad,
			String telefono) {
		super(nick,nombre,apellidos,mail,ciudad,telefono);
	}
	
	public void insertar() {
		String tipo = "Consumidor";
		String dni = "";
		String direccion = "";
		try {
			UsuarioDAO.getInstance().insert(this,tipo,dni,direccion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
