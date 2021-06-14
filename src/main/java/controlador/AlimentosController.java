package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AlimentoDAO;
import dao.TerrenoDAO;
import modelo.Alimento;
import modelo.Terreno;

/**
 * Servlet implementation class GestionLibros
 */
@WebServlet("/AlimentosController")
public class AlimentosController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    //Constructor vacio
    public AlimentosController() {
    }

    /**
     * Dependiendo la opcion indicada, da de alta un alimento, lo elimina o actualiza
     * @throws SQLException 
     */
	private void procesarAlimentos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		switch (request.getParameter("opcion")) {
			case "1":
				altaAlimento(request, response);
				break;
			case "2":
				eliminarAlimento(request, response);
				break;
			case "3":
				actualizarAlimento(request, response);
				break;
			case "4":
				cargarAlimentos(request, response);
				break;
			case "5":
				verDetalleAlimento(request, response);
				break;	
			default:
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Da de alta el alimento segun los parametros pasados y vuelve a la pagina de alta con un mensaje de resultado.
	 */
	private void altaAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento incluido.";
		ArrayList<Alimento> alimentos = new ArrayList<Alimento>(); 
		Alimento alimento = new Alimento();
		try {
			String nombre = request.getParameter("nombre");
			String medida = request.getParameter("medida");
			double precio = Double.parseDouble(request.getParameter("precio"));
			
			alimento = new Alimento(nombre, medida);
			int idAlimento = alimento.insertar();
			alimento.setId(idAlimento);
			alimento.setPrecio(precio);
			
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir el Alimento.";
		} catch (SQLException e) {
			msg = "ERROR al introducir el Alimento, compruebe que no sea un nombre ya existente.";
		}
		finally {
			alimentos = alimento.obtenerAlimentos();
		}
		
		request.setAttribute("alimentos",alimentos);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina el alimento que tiene el id pasado y vuelve a la pagina del listado de alimento con un mensaje de resultado.
	 */
	private void eliminarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento eliminado.";
		ArrayList<Alimento> alimentos = new ArrayList<Alimento>(); 
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Alimento alimento = new Alimento();
			alimento.buscarID(id);
			if (alimento.estaEnTerrenos()) {
				msg = "ERROR al eliminar el alimento, puede que tenga referencias de Terrenos.";
			}
			else {
				alimento.eliminar(id);
			}
			alimentos = alimento.obtenerAlimentos();
		} catch (Exception e) {
			msg = "ERROR al eliminar el alimento.";
		}
		
		request.setAttribute("alimentos",alimentos);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Modifica el precio del alimento que tiene el id pasado y vuelve a la pagina del listado de alimentos con un mensaje de resultado.
	 */
	private void actualizarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento actualizado.";
		ArrayList<Alimento> alimentos = new ArrayList<Alimento>(); 
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String nombre = request.getParameter("nombre");
			String medida = request.getParameter("medida");
			double precio = Double.parseDouble(request.getParameter("precio"));
			
			Alimento alimento = new Alimento(id,nombre,medida);
			alimento.actualizar();
			//actualiza el precio si es que ha cambiado
			if(alimento.getPrecio() != precio) {
				alimento.setPrecio(precio);
			}
			alimentos = alimento.obtenerAlimentos();
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar el alimento.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al modificar el alimento.";
		}
		
		request.setAttribute("alimentos",alimentos);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}

	private void cargarAlimentos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		AlimentoDAO aDAO = new AlimentoDAO();
		ArrayList<Alimento> alimentos = aDAO.listAlimentos();	
		
		request.setAttribute("alimentos",alimentos);
		RequestDispatcher req = request.getRequestDispatcher("alimentos.jsp");
		req.forward(request, response);
	}
	
	private void verDetalleAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			if (id != 0) {
				Alimento alimento = new Alimento();
				alimento.buscarID(id);
				request.setAttribute("alimento",alimento);
			}
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar el alimento.";
		}

		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimento.jsp");
		vista.forward(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarAlimentos(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarAlimentos(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
