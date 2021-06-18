package modelo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.PaqueteDAO;

/**
 * Clase para trabajar con Paquetes
 * @author Iker Aparicio
 */
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
	
	//****************** Constructores ******************
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
	
	public Paquete(int idTerreno, int idAlimento, Double cantidadPropuesta) {
		this.cantidadPropuesta = cantidadPropuesta;
		Terreno t = new Terreno();
		t.buscarID(idTerreno);
		this.terreno = t;
		Alimento a = new Alimento();
		a.buscarID(idAlimento);
		this.alimento = a;
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
		else if (Estado.FINALIZADO.toString().equals(estado))
			this.estado = Estado.FINALIZADO;
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

	//****************** Getters y Setters ******************
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

	public Date getFechaPropuesta() {
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
	
	//****************** Override de Interfaz Producto ******************
	@Override
	public double getPrecio() {
		double precio = 0;
		
		precio = this.getAlimento().getPrecio(this.fechaAceptacion) * this.cantidadAceptada; 
		return Math.round(precio * 100.0) / 100.0;
	}
	
	@Override
	public double getPrecio(Date fecha) {
		double precio = 0;
		
		precio = this.getAlimento().getPrecio(fecha) * this.cantidadAceptada; 
		return Math.round(precio * 100.0) / 100.0;
	}
	
	//****************** Métodos DAO ******************
	public void insertar() throws SQLException {
		PaqueteDAO.getInstance().insert(this);
	}
	
	public void eliminar(int id) throws SQLException {
		PaqueteDAO.getInstance().delete(id);
	}
	
	public void actualizar() throws SQLException {
		PaqueteDAO.getInstance().update(this);
	}
	
	/**
	 * Carga el objeto que tiene el id pasado
	 * @param id
	 */
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
	
	/**
	 * @param estado: nombre del estado
	 * @return Lista de propuestas filtradas por su estado
	 */
	public ArrayList<Paquete> obtenerPropuestas(String estado) {
		ArrayList<Paquete> lista = null;
		try {
			lista = PaqueteDAO.getInstance().listPropuestas(estado);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	/**
	 * @return Lista de porciones incluidas en el Paquete
	 */
	public ArrayList<Porcion> obtenerPorciones() {
		ArrayList<Porcion> porciones = null;
		try {
			porciones = PaqueteDAO.getInstance().getPorciones(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return porciones;
	}
	
	/**
	 * @return Productor del terreno al que pertenece el Paquete
	 */
	public Productor getProductor() {
		return this.terreno.getProductor();
	}
	
	/**
	 * @return nombre completo del Productor del terreno al que pertenece el Paquete
	 */
	public String getNombreCompletoProductor() {
		return this.terreno.getProductor().getNombreCompleto();
	}
	
	/**
	 * @return nombre del Alimento al que pertenece el Paquete
	 */
	public String getNombreAlimento() {
		return this.alimento.getNombre();
	}
	
	/**
	 * @return nombre del Terreno al que pertenece el Paquete
	 */
	public String getNombreTerreno() {
		return this.terreno.getNombre();
	}
	
	/**
	 * @return medida del Alimento al que pertenece el Paquete
	 */
	public String getMedidaAlimento() {
		return this.alimento.getMedida();
	}

	/**
	 * Aprueba la cantidad el Paquete indicado
	 * @param id: id del Paquete
	 * @param cantidad: cantidad a aprobar
	 * @throws SQLException
	 */
	public void aprobar(int id, Double cantidad) throws SQLException {
		PaqueteDAO.getInstance().approve(id, cantidad);
	}
	
	/**
	 * Rechaza el Paquete indicado
	 * @param id: id del Paquete
	 * @throws SQLException
	 */
	public void rechazar(int id) throws SQLException {
		PaqueteDAO.getInstance().disApprove(id);
	}
	
	/**
	 * Finaliza el Paquete indicado y deja su cantidad disponible a 0
	 * @param id: id del Paquete
	 * @throws SQLException
	 */
	public void finalizar(int id) throws SQLException {
		PaqueteDAO.getInstance().archive(id);
	}
	
	/**
	 * @return true si todavía no se ha getionado el Paquete, false en caso contrario
	 */
	public boolean estaSinGestionar() {
		if (this.estado == Estado.PROPUESTO)
			return true;
		else
			return false;
	}
	
	/**
	 * @return true si se ha aceptado el Paquete, false en caso contrario
	 */
	public boolean estaAceptado() {
		if (this.estado == Estado.ACEPTADO)
			return true;
		else
			return false;
	}
	
	/**
	 * @return true si se ha finalizado el Paquete, false en caso contrario
	 */
	public boolean estaFinalizado() {
		if (this.estado == Estado.FINALIZADO)
			return true;
		else
			return false;
	}
	
	/**
	 * @return valor del estado definido como Inicial (PROPUESTO)
	 */
	public String getEstadoInicial() {
		return Estado.PROPUESTO.toString();
	}
	
	/**
	 * @return valor del estado por defecto en el filtro (PROPUESTO)
	 */
	public String getValorDefectoFIltro() {
		return Estado.PROPUESTO.toString();
	}
}
