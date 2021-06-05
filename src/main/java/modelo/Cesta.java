package modelo;

import java.sql.Date;

public class Cesta implements Producto {
	
	private int id;
	private String nombre;
	private Date fechaCreacion;
	private Date fechaCompra;
	
	Usuario consumidor = new Usuario();
	
	public Cesta() {
		
	}

	public Cesta(String nombre) {
		this.nombre = nombre;
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

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public Usuario getUsuario() {
		return consumidor;
	}

	public void setUsuario(Usuario usuario) {
		this.consumidor = usuario;
	}

	@Override
	public double getPrecio() {
		double precio = 0;
		return precio;
	}

	
}
