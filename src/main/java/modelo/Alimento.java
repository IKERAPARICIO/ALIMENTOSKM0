package modelo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.AlimentoDAO;

public class Alimento implements Producto {

	private int id;
	private String nombre;
	private String medida;
	
	public Alimento() {
		
	}
	
	public Alimento(int id, String nombre, String medida) {
		this.id = id;
		this.nombre = nombre;
		this.medida = medida;
	}
	
	public Alimento(String nombre, String medida) {
		this.nombre = nombre;
		this.medida = medida;
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

	public String getMedida() {
		return medida;
	}

	public void setMedida(String medida) {
		this.medida = medida;
	}

	@Override
	//devuelve el precio actual del alimento
	public double getPrecio() {
		double precio = 0;
		try {
			precio = AlimentoDAO.getInstance().getCurrentPrice(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return precio;
	}
	
	public void setPrecio(double precio) {
		try {
			AlimentoDAO.getInstance().setCurrentPrice(this.id,precio);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Map getHistoricoPrecios(int id) {
		Map<String,String> historico = new HashMap<>();
		try {
			historico = AlimentoDAO.getInstance().getPriceHistory(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return historico;
	}
	
	public int insertar() {
		int idAlimento = 0;
		try {
			idAlimento = AlimentoDAO.getInstance().insert(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idAlimento;
	}
	
	public void eliminar(int id) {
		try {
			AlimentoDAO.getInstance().delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizar() {
		try {
			AlimentoDAO.getInstance().update(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Alimento> obtenerAlimentos() {
		ArrayList<Alimento> lista = null;
		try {
			lista = AlimentoDAO.getInstance().listAlimentos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public void buscarID(int id) {
		Alimento a = null;
		try {
			a = AlimentoDAO.getInstance().finID(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (a != null) {
			this.id = a.getId();
			this.nombre = a.getNombre();
			this.medida = a.getMedida();
		}
	}

}
