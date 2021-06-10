package modelo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.AlimentoDAO;
import dao.PaqueteDAO;
import dao.PorcionDAO;

public class Paquete implements Producto {
	
	private int id;
	private Double cantidadPropuesta;
	private Double cantidadAceptada;
	private Double cantidadDisponible;
	private Date fechaPropuesta;
	private Date fechaAceptacion;
	private Estado estado;
	
	private Alimento alimento;
	private Terreno terreno;
	
	public Paquete() {
		
	}
	
	public Paquete(int id) {
		this.id = id;
	}
	
	public Paquete(Double cantidadPropuesta, Alimento alimento, Terreno terreno) {
		this.cantidadPropuesta = cantidadPropuesta;
		this.alimento = alimento;
		this.terreno = terreno;
	}
	
	public Paquete(int id, int idTerreno, int idAlimento, Double cantidadPropuesta, Double cantidadAceptada, 
			Double cantidadDisponible, Date fechaPropuesta, Date fechaAceptacion, String estado) {
		this.id = id;
		this.cantidadAceptada = cantidadAceptada;
		this.cantidadPropuesta = cantidadPropuesta;
		this.cantidadDisponible = cantidadDisponible;
		this.fechaPropuesta = fechaPropuesta;
		this.fechaAceptacion = fechaAceptacion;
		if (Estado.ACEPTADO.toString().equals(estado))
			this.estado = Estado.ACEPTADO;
		else if (Estado.PROPUESTO.toString().equals(estado))
			this.estado = Estado.PROPUESTO;
		else
			this.estado = Estado.RECHAZADO;
		
		Terreno t = new Terreno();
		t.buscarID(idTerreno);
		this.terreno = t;
		Alimento a = new Alimento();
		a.buscarID(idAlimento);
		this.alimento = a;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getCantidadPropuesta() {
		return cantidadPropuesta;
	}

	public void setCantidadPropuesta(Double cantidadPropuesta) {
		this.cantidadPropuesta = cantidadPropuesta;
	}

	public Double getCantidadAceptada() {
		return cantidadAceptada;
	}

	public void setCantidadAceptada(Double cantidadAceptada) {
		this.cantidadAceptada = cantidadAceptada;
	}

	public Double getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(Double cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public Date getFechaPorpuesta() {
		return fechaPropuesta;
	}

	public void setFechaPorpuesta(Date fechaPorpuesta) {
		this.fechaPropuesta = fechaPorpuesta;
	}

	public Date getFechaAceptacion() {
		return fechaAceptacion;
	}

	public void setFechaAceptacion(Date fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Alimento getAlimento() {
		return alimento;
	}

	public void setAlimento(Alimento alimento) {
		this.alimento = alimento;
	}

	public Terreno getTerreno() {
		return terreno;
	}

	public void setTerreno(Terreno terreno) {
		this.terreno = terreno;
	}

	@Override
	public double getPrecio() {
		double precio = 0;
		
		precio = this.getAlimento().getPrecio() * this.cantidadAceptada; 
		
		return precio;
	}
	
	
	public void buscarID(int id) {
		Paquete p = null;
		try {
			p = PaqueteDAO.getInstance().finID(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (p != null) {
			this.id = p.getId();
			this.cantidadPropuesta = p.cantidadPropuesta;
			this.cantidadAceptada = p.cantidadAceptada;
			this.cantidadDisponible = p.cantidadDisponible;
			this.fechaPropuesta = p.fechaPropuesta;
			this.fechaAceptacion = p.fechaAceptacion;
			this.estado = p.estado;
			this.alimento = p.getAlimento();
			this.terreno = p.getTerreno();
		}
	}
	
	public void aprobar(int id, int cantidad) {
		try {
			PaqueteDAO.getInstance().approve(id, cantidad);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void rechazar(int id) {
		try {
			PaqueteDAO.getInstance().disApprove(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void anular(int id) {
		try {
			PaqueteDAO.getInstance().cancel(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean estaSinGestionar() {
		if (this.estado == Estado.PROPUESTO)
			return true;
		else
			return false;
	}
	
	public boolean estaGestionado() {
		if (this.estado == Estado.ACEPTADO)
			return true;
		else
			return false;
	}

	public ArrayList<Porcion> obtenerPorciones() {
		ArrayList<Porcion> porciones = null;
		try {
			porciones = PaqueteDAO.getInstance().getPorciones(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return porciones;
	}
}
