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
import modelo.Paquete;
import modelo.Porcion;

/**
 * Servlet implementation class GestionLibros
 */
@WebServlet("/PaquetesController")
public class PaquetesController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    //Constructor vacio
    public PaquetesController() {
    }

    /**
     * Dependiendo la opcion indicada, da de alta un paquete, lo elimina o actualiza
     */
	private void procesarPaquetes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		switch (request.getParameter("opcion")) {
			case "1":
				aprobarPaquete(request, response);
				break;
			case "2":
				rechazarPaquete(request, response);
				break;
			case "3":
				anularPaquete(request, response);
				break;
			case "4":
				porcionesPaquete(request, response);
				break;
			case "5":
				crearPorciones(request, response);
				break;
			case "6":
				eliminarPorcion(request, response);
				break;
			case "7":
				verCesta(request, response);
				break;
			default:
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Aprueba el paquete que tiene el id pasado total o parcialmente y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void aprobarPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Paquete aprobado.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			int cantidad = Integer.parseInt(request.getParameter("cant"));
			
			Paquete paquete = new Paquete();
			paquete.aprobar(id,cantidad);
		
		} catch (Exception e) {
			msg = "ERROR al aprobar el paquete.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Aprueba parcialmente el paquete que tiene el id pasado y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void rechazarPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Paquete rechazado.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			paquete.rechazar(id);
		
		} catch (Exception e) {
			msg = "ERROR al rechazar el paquete.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}
	
	private void anularPaquete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "Paquete anulado.";
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			paquete.anular(id);
		
		} catch (Exception e) {
			msg = "ERROR al anular el paquete.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacen.jsp");
		vista.forward(request, response);
	}
	
	private void porcionesPaquete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Paquete paquete = new Paquete();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			paquete.buscarID(id);
		
		} catch (Exception e) {
			msg = "ERROR al cargar el paquete.";
		}
		
		request.setAttribute("mensaje",msg);
		request.setAttribute("paquete",paquete);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}

	//ya se ha validado via javascript
	private void crearPorciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Paquete paquete = new Paquete();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Double cantNew = Double.parseDouble(request.getParameter("cantNew"));
			int numNew = Integer.parseInt(request.getParameter("numNew"));
			Double cantDisp = Double.parseDouble(request.getParameter("cantDisp"));
			
			paquete.buscarID(id);
			
			//se valida si hay cantidad suficiente
			/*Double cantNecesaria = cantNew * numNew;
			if (cantNecesaria <= 0 || cantNecesaria > cantDisp) {
				msg = "Parametros de Cantidad y Número Porciones no válidos.";
			}*/
				
			for (int i = 0; i < numNew; i++) {
				Porcion p = new Porcion(cantNew, paquete);
				p.insertar();
				//paquete.paquete.getCantidadDisponible() - 
			}
			//carga los datos actualizados
			paquete.buscarID(id);
			
			msg = "Porción creada.";

		}
		catch (Exception e) {
			msg = "ERROR al crear las porciones el paquete.";
		}
		
		request.setAttribute("paquete",paquete);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}
	
	private void eliminarPorcion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "Porción eliminada.";
		Paquete paquete = new Paquete();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Porcion porcion = new Porcion();
			porcion.buscarID(id);
			int paqueteId = porcion.getPaquete().getId();
			porcion.eliminar();
			//carga los datos actualizados
			//paquete.buscarID(paqueteId);
		
		} catch (Exception e) {
			msg = "ERROR al eliminar la porción.";
		}
		
		request.setAttribute("paquete",paquete);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}
	
	private void verCesta(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 *  TODO: quitar el metodo
	 */
	/*private void historicoPreciosPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Histórico de precios del Paquete";
		Map<String,String> historico = new HashMap<>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			historico = paquete.getHistoricoPrecios(id);
		
		} catch (Exception e) {
			msg = "ERROR al buscar el historico de precios del paquete.";
		}
		
		request.setAttribute("mensaje",msg);
		request.setAttribute("historico",historico);
		RequestDispatcher vista = request.getRequestDispatcher("paquetes.jsp");
		vista.forward(request, response);
	}*/
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarPaquetes(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarPaquetes(request, response);
	}
}
