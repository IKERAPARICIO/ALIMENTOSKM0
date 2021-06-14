package modelo;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CestaDAO;
import dao.PaqueteDAO;
import dao.TerrenoDAO;

public class Terreno {

	private int id;
	private String nombre;
	private double metros;
	private String ciudad;
	private String direccion;
	
	Productor productor = new Productor();
	
	public Terreno() {
		
	}

	public Terreno(String nombre, double metros, String ciudad, String direccion) {
		this.nombre = nombre;
		this.metros = metros;
		this.ciudad = ciudad;
		this.direccion = direccion;
	}
	
	public Terreno(String nombre, double metros, String ciudad, String direccion, int idUsuario) {
		this.nombre = nombre;
		this.metros = metros;
		this.ciudad = ciudad;
		this.direccion = direccion;
		
		Productor productor = new Productor();
		productor.buscarID(idUsuario);
		this.productor = productor;
	}
	
	public Terreno(int id, String nombre, double metros, String ciudad, String direccion, int idUsuario) {
		this.id = id;
		this.nombre = nombre;
		this.metros = metros;
		this.ciudad = ciudad;
		this.direccion = direccion;
		
		Productor productor = new Productor();
		productor.buscarID(idUsuario);
		this.productor = productor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getMetros() {
		return metros;
	}

	public void setMetros(double metros) {
		this.metros = metros;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Productor getProductor() {
		return productor;
	}

	public void setProductor(Productor productor) {
		this.productor = productor;
	}
	
	public int getProductorId() {
		return productor.getId();
	}
	
	//
	public String getNombreProductor() {
		return this.productor.getNombre();
	}
	
	//Acceso a DAO
	public int insertar() {
		int idTerreno = 0;
		try {
			idTerreno = TerrenoDAO.getInstance().insert(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idTerreno;
	}
	
	public void eliminar(int id) {
		try {
			TerrenoDAO.getInstance().delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizar() {
		try {
			TerrenoDAO.getInstance().update(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Terreno> obtenerTerrenos(int idUsuario) {
		ArrayList<Terreno> lista = null;
		try {
			lista = TerrenoDAO.getInstance().listTerrenos(idUsuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public void buscarID(int id) {
		Terreno t = null;
		try {
			t = TerrenoDAO.getInstance().finID(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (t != null) {
			this.id = t.getId();
			this.nombre = t.getNombre();
			this.metros = t.getMetros();
			this.ciudad = t.getCiudad();
			this.direccion = t.getDireccion();
			this.productor = t.getProductor();
		}
	}
	
	public void quitarAlimento(int idAlimento) {
		try {
			TerrenoDAO.getInstance().removeAlimento(this.id, idAlimento);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void agregarAlimento(int idAlimento) {
		try {
			TerrenoDAO.getInstance().addAlimento(this.id, idAlimento);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Alimento> obtenerAlimentos() {
		ArrayList<Alimento> alimentos = null;
		try {
			alimentos = TerrenoDAO.getInstance().getAlimentos(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alimentos;
	}
	
	public ArrayList<Alimento> obtenerAlimentosDisponibles() {
		ArrayList<Alimento> alimentos = null;
		try {
			alimentos = TerrenoDAO.getInstance().getAlimentosDisponibles(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alimentos;
	}
}
