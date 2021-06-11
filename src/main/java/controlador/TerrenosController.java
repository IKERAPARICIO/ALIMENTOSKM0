package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Alimento;
import modelo.Cesta;
import modelo.Terreno;

/**
 * Servlet implementation class GestionLibros
 */
@WebServlet("/TerrenosController")
public class TerrenosController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    //Constructor vacio
    public TerrenosController() {
    }

    /**
     * Dependiendo la opcion indicada, da de alta un terreno, lo elimina o actualiza
     */
	private void procesarTerrenos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		switch (request.getParameter("opcion")) {
			case "1":
				altaTerreno(request, response);
				break;
			case "2":
				eliminarTerreno(request, response);
				break;
			case "3":
				actualizarTerreno(request, response);
				break;
			case "4":
				verDetalleTerreno(request, response);
				break;	
			case "5":
				quitarAlimento(request, response);
				break;
			case "6":
				agregarAlimento(request, response);
				break;
			case "7":
				mostrarAlimentos(request, response);
				break;
			default:
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Da de alta el terreno segun los parametros pasados y vuelve a la pagina de alta con un mensaje de resultado.
	 */
	private void altaTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Terreno incluido.";
		int id = 0;
		try {
			String nombre = request.getParameter("nombre");
			int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
			double metros = Double.parseDouble(request.getParameter("metros"));
			String ciudad = request.getParameter("ciudad");
			String direccion = request.getParameter("direccion");

			Terreno terreno = new Terreno(nombre, metros, ciudad, direccion, idUsuario);
			id = terreno.insertar();
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir el Terreno.";
		}
		
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina el terreno que tiene el id pasado y vuelve a la pagina del listado de terreno con un mensaje de resultado.
	 */
	private void eliminarTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Terreno eliminado.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Terreno terreno = new Terreno();
			terreno.eliminar(id);
		} catch (Exception e) {
			msg = "ERROR al eliminar el terreno.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terrenos.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Modifica el precio del terreno que tiene el id pasado y vuelve a la pagina del listado de terrenos con un mensaje de resultado.
	 */
	private void actualizarTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Terreno actualizado.";
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			String nombre = request.getParameter("nombre");
			int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
			double metros = Double.parseDouble(request.getParameter("metros"));
			String ciudad = request.getParameter("ciudad");
			String direccion = request.getParameter("direccion");
			
			Terreno terreno = new Terreno(id,nombre, metros, ciudad, direccion, idUsuario);
			terreno.actualizar();
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar el terreno.";
		}
		
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}

	private void verDetalleTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = null;
		int idTerreno = 0;
		try {
			idTerreno = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar el terreno.";
		}
		
		request.setAttribute("id",idTerreno);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}

	/**
	 * Elimina el alimento del terreno indicado y vuelve a la pagina del listado de terrenos con un mensaje de resultado.
	 */
	private void quitarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento quitado del terreno.";
		int idTerreno = 0;
		try {
			idTerreno = Integer.parseInt(request.getParameter("idTerreno"));
			int idAlimento = Integer.parseInt(request.getParameter("idAlimento"));
			
			Terreno terreno = new Terreno();
			terreno.buscarID(idTerreno);
			terreno.quitarAlimento(idAlimento);
		} 
		catch (Exception e) {
			msg = "ERROR al quitar el alimento del terreno.";
		}
		
		request.setAttribute("id",idTerreno);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}

	private void agregarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento agregado al terreno.";
		int idTerreno = 0;
		try {
			idTerreno = Integer.parseInt(request.getParameter("idTerreno"));
			int idAlimento = Integer.parseInt(request.getParameter("idAlimento"));
			
			Terreno terreno = new Terreno();
			terreno.buscarID(idTerreno);
			terreno.agregarAlimento(idAlimento);
		} catch (Exception e) {
			msg = "ERROR al agregar el alimento al terreno.";
		}
		
		request.setAttribute("id",idTerreno);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}
	
	private void mostrarAlimentos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = null;
		ArrayList<Alimento> alimentos = new ArrayList<Alimento>();
		int idTerreno = 0;
		try {
			idTerreno = Integer.parseInt(request.getParameter("id"));
			
			Terreno terreno = new Terreno();
			terreno.buscarID(idTerreno);
			alimentos = terreno.obtenerAlimentosDisponibles();
			
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar los alimentos disponibles.";
		}
		
		request.setAttribute("idTerreno",idTerreno);
		request.setAttribute("alimentos",alimentos);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}
	
	
	/**
	 *  TODO: quitar el metodo
	 */
	/*private void historicoPreciosTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Histórico de precios del Terreno";
		Map<String,String> historico = new HashMap<>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Terreno terreno = new Terreno();
			historico = terreno.getHistoricoPrecios(id);
		
		} catch (Exception e) {
			msg = "ERROR al buscar el historico de precios del terreno.";
		}
		
		request.setAttribute("mensaje",msg);
		request.setAttribute("historico",historico);
		RequestDispatcher vista = request.getRequestDispatcher("terrenos.jsp");
		vista.forward(request, response);
	}*/
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarTerrenos(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarTerrenos(request, response);
	}
}
