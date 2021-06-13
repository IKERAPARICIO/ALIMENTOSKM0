package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Rol;
import singleton.DBConnection;

public class CommonDAO {
	private Connection con = null;
	
	public static UsuarioDAO instance = null;

	public CommonDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static UsuarioDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new UsuarioDAO();
		return instance;
	}
	
	public ArrayList<String> getRols() {
		ArrayList<String> lista = new ArrayList<String>();
		for (Rol rol : Rol.values()) { 
		    lista.add(rol.toString());
		}
		return lista;
	}

}
