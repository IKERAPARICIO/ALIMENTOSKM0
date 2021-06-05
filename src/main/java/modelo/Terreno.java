package modelo;

public class Terreno {

	private int id;
	private String nombre;
	private double metros;
	private String ciudad;
	private String direccion;
	
	Usuario productor = new Usuario();
	
	public Terreno() {
		
	}

	public Terreno(String nombre, double metros, String ciudad, String direccion) {
		super();
		this.nombre = nombre;
		this.metros = metros;
		this.ciudad = ciudad;
		this.direccion = direccion;
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

	public double getMetros() {
		return metros;
	}

	public void setMetros(double metros) {
		this.metros = metros;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Usuario getProductor() {
		return productor;
	}

	public void setProductor(Usuario productor) {
		this.productor = productor;
	}
	
	
}
