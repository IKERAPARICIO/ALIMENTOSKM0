package modelo;

import java.sql.SQLException;

import dao.UsuarioDAO;

/**
 * Clase para trabajar con Consumidor
 * @author Iker Aparicio
 */
public class Consumidor extends Usuario {
	
	private Rol rol = Rol.CONSUMIDOR;
	
	//****************** Constructores ******************
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
	
	//****************** Métodos DAO ******************
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
