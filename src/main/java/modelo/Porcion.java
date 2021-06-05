package modelo;

public class Porcion implements Producto {

	private int id;
	private double cantidad;
	
	private Paquete paquete;
	
	public Porcion() {
		
	}
	
	public Porcion(double cantidad, Paquete paquete) {
		this.cantidad = cantidad;
		this.paquete = paquete;
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

	@Override
	public double getPrecio() {
		double precio = 0;
		return precio;
	}
	
}
