package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.Porcion;
import singleton.DBConnection;

public class PorcionDAO {
	private Connection con = null;
	
	public static PorcionDAO instance = null;

	public PorcionDAO() throws SQLException {
		con = DBConnection.getConnection();
	}
	
	public static PorcionDAO getInstance() throws SQLException {
		if (instance == null)
			instance = new PorcionDAO();
		return instance;
	}
	
	public int insert(Porcion p) throws SQLException {
		int idPorcion = 0;
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO porcion (idPaquete, cantidad) VALUES (?,?)");
			ps.setInt(1, p.getPaqueteId());
			ps.setDouble(2, p.getCantidad());
			ps.executeUpdate();
			ps.close();
			
			Double newQuantity = p.getPaquete().getCantidadDisponible() - p.getCantidad();
			PaqueteDAO.getInstance().updateCantidadDisponible(p.getPaquete().getId(), newQuantity);
			p.getPaquete().setCantidadDisponible(newQuantity);
		} catch (Exception e) {
			System.out.println("Error al introducir la porcion!");
		}
		return idPorcion;
	}
	
	public void delete(Porcion p) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM porcion WHERE idPorcion = ?");
		ps.setInt(1, p.getId());
		ps.executeUpdate();
		ps.close();
		
		//restar lo eliminado
		Double newQuantity = p.getPaquete().getCantidadDisponible() + p.getCantidad();
		PaqueteDAO.getInstance().updateCantidadDisponible(p.getPaquete().getId(), newQuantity);
		p.getPaquete().setCantidadDisponible(newQuantity);
	}
	
	public void update(Porcion p) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE porcion SET idPaquete = ?, cantidad = ? WHERE idPorcion = ?");
		ps.setInt(1, p.getPaqueteId());
		ps.setDouble(2, p.getCantidad());
		ps.setInt(3, p.getId());

		ps.executeUpdate();
		ps.close();
	}
	
	public ArrayList<Porcion> listPorcions() throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion LEFT JOIN cesta_porcion "
				+ "ON porcion.idPorcion = cesta_porcion.idPorcion ORDER BY nombre");
		//PreparedStatement ps = con.prepareStatement("SELECT * from porcion ORDER BY nombre");
		ResultSet rs = ps.executeQuery();
		ArrayList<Porcion> result = null;
		
		while (rs.next()) {
			if (result == null)
				result = new ArrayList<>();
			result.add(new Porcion(rs.getInt("idPorcion"), rs.getDouble("cantidad"), rs.getInt("idPaquete"), rs.getInt("idCesta")));
		}
		
		rs.close();
		ps.close();
		return result;
	}
	
	public Porcion finID(int id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion LEFT JOIN cesta_porcion "
				+ "ON porcion.idPorcion = cesta_porcion.idPorcion WHERE porcion.idPorcion = ?");
		//PreparedStatement ps = con.prepareStatement("SELECT * FROM porcion WHERE idPorcion = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Porcion result = null;
		if (rs.next()) {
			result = new Porcion(rs.getInt("idPorcion"), rs.getDouble("cantidad"),  rs.getInt("idPaquete"), rs.getInt("idCesta"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
}
