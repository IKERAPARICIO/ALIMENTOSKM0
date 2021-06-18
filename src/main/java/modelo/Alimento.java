package modelo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.AlimentoDAO;

/**
 * Clase para trabajar con alimentos
 * @author Iker Aparicio
 */
public class Alimento implements Producto {

	private int id;
	private String nombre;
	private String medida;
	
	//****************** Constructores ******************
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

	//****************** Getters y Setters ******************
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

	//****************** Override de Interfaz Producto ******************
	@Override
	public double getPrecio() {
		double precio = 0;
		try {
			precio = AlimentoDAO.getInstance().getCurrentPrice(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Math.round(precio * 100.0) / 100.0;
	}
	
	@Override
	public double getPrecio(Date fecha) {
		double precio = 0;
		try {
			precio = AlimentoDAO.getInstance().getDatePrice(this.id,fecha);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Math.round(precio * 100.0) / 100.0;
	}
	
	//****************** Métodos DAO ******************
	public int insertar() throws SQLException {
		int idAlimento = 0;
		idAlimento = AlimentoDAO.getInstance().insert(this);

		return idAlimento;
	}
	
	public void eliminar(int id) throws SQLException {
		AlimentoDAO.getInstance().delete(id);
	}
	
	public void actualizar() throws SQLException {
		AlimentoDAO.getInstance().update(this);
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
	
	/**
	 * Carga el objeto que tiene el id pasado
	 * @param id
	 */
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
	
	/**
	 * Actualiza el precio del Alimento
	 * @param precio: nuevo precio
	 */
	public void setPrecio(double precio) {
		try {
			AlimentoDAO.getInstance().setCurrentPrice(this.id,precio);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Devuelve el histórico de precios del Alimento
	 * @param id: id del Alimento
	 * @return Histórico de precios
	 */
	public Map getHistoricoPrecios(int id) {
		Map<String,String> historico = new HashMap<>();
		try {
			historico = AlimentoDAO.getInstance().getPriceHistory(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return historico;
	}
	
	/**
	 * Mira si el Alimento actual esta incluido en algún Terreno
	 * @return true si lo está y false si no
	 */
	public boolean estaEnTerrenos(){
		boolean encontrado = false;
		try {
			encontrado = AlimentoDAO.getInstance().hasTerrenos(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return encontrado;
	}
}
