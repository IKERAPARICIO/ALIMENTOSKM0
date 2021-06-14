package modelo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.PorcionDAO;

public class Porcion implements Producto {

	private int id;
	private double cantidad;
	
	private Paquete paquete;
	private Cesta cesta;
	
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

	//
	public String getNombreAlimento() {
		return this.paquete.getAlimento().getNombre();
	}
	
	public String getNombreProductor() {
		return this.paquete.getTerreno().getProductor().getNombre();
	}
	
	public Date getFechaAceptacionPaquete() {
		return this.paquete.getFechaAceptacion();
	}
	
	public Double getPrecioAlimento() {
		return this.paquete.getAlimento().getPrecio();
	}
	
	public Double getPrecioAlimento(Date fecha) {
		return this.paquete.getAlimento().getPrecio(fecha);
	}
	
	public String getNombreTerreno() {
		return this.paquete.getTerreno().getNombre();
	}
	
	@Override
	public double getPrecio() {
		double precio = 0;
		
		precio = this.paquete.getAlimento().getPrecio() * cantidad;
		return precio;
	}
	
	@Override
	public double getPrecio(Date fecha) {
		double precio = 0;
		
		precio = this.paquete.getAlimento().getPrecio(fecha) * cantidad;
		return precio;
	}
	
	public boolean incluidaEnCesta() {
		if (cesta.getId() == 0)
			return false;
		else
			return true;
	}
	
	//Acceso a DAO
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
	
}
