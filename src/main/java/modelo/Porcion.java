package modelo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.PorcionDAO;

/**
 * Clase para trabajar con porciones
 * @author Iker Aparicio
 */
public class Porcion implements Producto {

	private int id;
	private double cantidad;
	
	private Paquete paquete;
	private Cesta cesta;
	
	//****************** Constructores ******************
	public Porcion() {
		
	}
	
	public Porcion(double cantidad, Paquete paquete) {
		this.cantidad = cantidad;
		this.paquete = paquete;
	}
	
	public Porcion(int id, double cantidad, int idPaquete) {
		this.id = id;
		this.cantidad = cantidad;

		Paquete paquete = new Paquete();
		paquete.buscarID(idPaquete);
		this.paquete = paquete;
	}
	
	public Porcion(int id, double cantidad, int idPaquete, int idCesta) {
		this.id = id;
		this.cantidad = cantidad;

		Paquete paquete = new Paquete();
		paquete.buscarID(idPaquete);
		this.paquete = paquete;
		
		Cesta cesta = new Cesta();
		cesta.buscarID(idCesta);
		this.cesta = cesta;
	}

	//****************** Getters y Setters ******************
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Paquete getPaquete() {
		return paquete;
	}

	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}
	
	public int getPaqueteId() {
		return paquete.getId();
	}
	
	public int getCestaId() {
		return cesta.getId();
	}

	//****************** Override de Interfaz Producto ******************
	@Override
	public double getPrecio() {
		double precio = 0;
		
		precio = this.paquete.getAlimento().getPrecio() * cantidad;
		return Math.round(precio * 100.0) / 100.0;
	}
	
	@Override
	public double getPrecio(Date fecha) {
		double precio = 0;
		
		precio = this.paquete.getAlimento().getPrecio(fecha) * cantidad;
		return Math.round(precio * 100.0) / 100.0;
	}
	
	//****************** Métodos DAO ******************
	public int insertar() {
		int idPorcion = 0;
		try {
			idPorcion = PorcionDAO.getInstance().insert(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idPorcion;
	}
	
	public void eliminar() {
		try {
			PorcionDAO.getInstance().delete(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizar() {
		try {
			PorcionDAO.getInstance().update(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Porcion> obtenerPorcions() {
		ArrayList<Porcion> lista = null;
		try {
			lista = PorcionDAO.getInstance().listPorcions();
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
		Porcion p = null;
		try {
			p = PorcionDAO.getInstance().finID(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (p != null) {
			this.id = p.getId();
			this.cantidad = p.getCantidad();
			this.paquete = p.getPaquete();
		}
	}
	
	/** 
	 * @return nombre del Alimento al que pertenece el Paquete de la Porcion
	 */
	public String getNombreAlimento() {
		return this.paquete.getAlimento().getNombre();
	}
	
	/** 
	 * @return nombre completo del Productor al que pertenece el Terreno del Paquete de la Porcio
	 */
	public String getNombreProductor() {
		return this.paquete.getTerreno().getProductor().getNombre();
	}
	
	/** 
	 * @return fecha de aceptación del Paquete de la Porcion
	 */
	public Date getFechaAceptacionPaquete() {
		return this.paquete.getFechaAceptacion();
	}
	
	/**
	 * @return nombre del Terreno al que pertenece el Paquete de la Porcion
	 */
	public String getNombreTerreno() {
		return this.paquete.getTerreno().getNombre();
	}
	
	/**
	 * Mira si la Porcion esá incluida en alguna cesta
	 * @return true si lo está, false en caso contrario
	 */
	public boolean incluidaEnCesta() {
		if (cesta.getId() == 0)
			return false;
		else
			return true;
	}
}
