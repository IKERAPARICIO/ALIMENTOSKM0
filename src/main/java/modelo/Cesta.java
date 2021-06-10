package modelo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.AlimentoDAO;
import dao.CestaDAO;

public class Cesta implements Producto {
	
	private int id;
	private String nombre;
	private Date fechaCreacion;
	private Date fechaCompra;
	
	Usuario consumidor = new Usuario();
	ArrayList<Porcion> listaPorciones = new ArrayList<Porcion>();
	
	public Cesta() {
		
	}

	public Cesta(String nombre) {
		this.nombre = nombre;
	}

	public Cesta(int id, String nombre, Date fechaCreacion, Date fechaCompra, int idUsuario) {
		this.id = id;
		this.nombre = nombre;
		this.fechaCreacion = fechaCreacion;
		this.fechaCompra = fechaCompra;
		
		Usuario u = new Consumidor();
		u.buscarID(idUsuario);
		this.consumidor = u;
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
	
	public int getUsuarioId() {
		if(consumidor == null)
			return 0;
		else
			return consumidor.getId();
	}
	
	public String getUsuarioName() {
		if(consumidor == null)
			return "";
		else
			return consumidor.getNombre();
	}

	public void setUsuario(Usuario usuario) {
		this.consumidor = usuario;
	}

	@Override
	public double getPrecio() {
		double precio = 0;
		
		if (listaPorciones.isEmpty()) {
			listaPorciones = this.obtenerPorciones();
		}
		if (listaPorciones != null) {
			for (Porcion p : listaPorciones) {
				precio = precio + p.getPrecio();
			}
		}
		return precio;
	}

	public void insertar() {
		try {
			CestaDAO.getInstance().insert(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminar(int id) {
		try {
			CestaDAO.getInstance().delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizar() {
		try {
			CestaDAO.getInstance().update(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void buscarID(int idCesta) {
		Cesta c = null;
		try {
			c = CestaDAO.getInstance().finID(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (c != null) {
			this.id = c.getId();
			this.nombre = c.getNombre();
			this.fechaCreacion = c.getFechaCreacion();
			this.fechaCompra = c.getFechaCompra();
			this.consumidor = c.getUsuario();
		}
	}
	
	public ArrayList<Porcion> obtenerPorciones() {
		ArrayList<Porcion> porciones = null;
		try {
			porciones = CestaDAO.getInstance().getPorciones(this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return porciones;
	}
}
