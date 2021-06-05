package modelo;

import java.sql.Date;

public class Paquete implements Producto {
	
	private int id;
	private int cantidadPropuesta;
	private int cantidadAceptada;
	private int cantidadDisponible;
	private Date fechaPorpuesta;
	private Date fechaAceptacion;
	private Estado estado;
	
	private Alimento alimento;
	private Terreno terreno;
	
	public Paquete() {
		
	}
	
	public Paquete(int cantidadPropuesta, Alimento alimento, Terreno terreno) {
		this.cantidadPropuesta = cantidadPropuesta;
		this.alimento = alimento;
		this.terreno = terreno;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCantidadPropuesta() {
		return cantidadPropuesta;
	}

	public void setCantidadPropuesta(int cantidadPropuesta) {
		this.cantidadPropuesta = cantidadPropuesta;
	}

	public int getCantidadAceptada() {
		return cantidadAceptada;
	}

	public void setCantidadAceptada(int cantidadAceptada) {
		this.cantidadAceptada = cantidadAceptada;
	}

	public int getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public Date getFechaPorpuesta() {
		return fechaPorpuesta;
	}

	public void setFechaPorpuesta(Date fechaPorpuesta) {
		this.fechaPorpuesta = fechaPorpuesta;
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
		return precio;
	}
	
	
}
