package modelo;

import java.sql.Date;

/**
 * Interfaz Producto
 * @author Iker Aparicio
 */
public interface Producto {
	/**
	 * @return Devuelve el precio actual del producto redondeado a 2 decimales
	 */
	public double getPrecio();
	
	/**
	 * @param fecha
	 * @return Devuelve el precio del producto en la fecha pasada redondeado a 2 decimales
	 */
	public double getPrecio(Date fecha);
}
