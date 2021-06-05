package modelo;

public class Productor extends Usuario {
	
	private String dni;
	private String direccion;
	
	public Productor() {
		super();
	}

	public Productor(String dni, String direccion) {
		super();
		this.dni = dni;
		this.direccion = direccion;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	
}
