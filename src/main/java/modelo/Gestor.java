package modelo;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.TerrenoDAO;

public class Gestor extends Usuario {
	
	private Rol rol = Rol.GESTOR;
	
	public Gestor() {
		super();
	}
}
