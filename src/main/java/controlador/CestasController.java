package controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Cesta;

/**
 * Servlet implementation class CestasController
 */
@WebServlet("/CestasController")
public class CestasController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//Constructor vacio
    public CestasController() {
    }

    /**
     * Dependiendo la opcion indicada, da de alta un cesta, lo elimina o actualiza
     */
	private void procesarCestas(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		switch (request.getParameter("opcion")) {
			case "1":
				eliminarCesta(request, response);
				break;
			case "2":
				quitarPorcion(request, response);
				break;
			case "3":
				altaCesta(request, response);
				break;
			case "4":
				actualizarCesta(request, response);
				break;
			case "5":
				verDetalleCesta(request, response);
				break;
			case "6":
				agregarPorcion(request, response);
				break;
			/*
			case "4":
				porcionesCesta(request, response);
				break;
			case "5":
				crearPorciones(request, response);
				break;
			case "6":
				eliminarPorcion(request, response);
				break;;*/
			default:
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Elimina la cesta que tiene el id pasado y vuelve a la pagina del listado de cestas con un mensaje de resultado.
	 */
	private void eliminarCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Cesta eliminada.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Cesta cesta = new Cesta();
			cesta.eliminar(id);
		
		} catch (Exception e) {
			msg = "ERROR al eliminar la cesta.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("confeccionarCestas.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina la porcion de la cesta indicada y vuelve a la pagina del listado de cestas con un mensaje de resultado.
	 */
	private void quitarPorcion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Porción quitada de la cesta.";
		Cesta cesta = new Cesta();
		int idCesta = 0;
		try {
			idCesta = Integer.parseInt(request.getParameter("idCesta"));
			int idPorcion = Integer.parseInt(request.getParameter("idPorcion"));
			
			cesta.buscarID(idCesta);
			cesta.quitarPorcion(idPorcion);
			//actualiza la cesta
			//cesta.buscarID(idCesta);
		
		} catch (Exception e) {
			msg = "ERROR al quitar la porción de la cesta.";
		}
		
		//request.setAttribute("cesta",cesta);
		request.setAttribute("id",idCesta);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void altaCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Cesta creada.";
		int id = 0;
		try {
			String nombre = request.getParameter("nombre");
						
			Cesta cesta = new Cesta(nombre);
			id = cesta.insertar();
		} catch (NumberFormatException e) {
			msg = "ERROR al crear la cesta.";
		}
		
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void actualizarCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Cesta actualizada.";
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			String nombre = request.getParameter("nombre");

			Cesta cesta = new Cesta(id, nombre);
			cesta.actualizar();
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar la cesta.";
		}
		
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void verDetalleCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = null;
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar la cesta.";
		}
		
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void agregarPorcion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Porción agregada a la cesta.";
		int idCesta = 0;
		try {
			idCesta = Integer.parseInt(request.getParameter("idCesta"));
			int idPorcion = Integer.parseInt(request.getParameter("idPorcion"));
			
			Cesta cesta = new Cesta();
			cesta.buscarID(idCesta);
			cesta.agregarPorcion(idPorcion);
		} catch (Exception e) {
			msg = "ERROR al agregar la porción a la cesta.";
		}
		
		request.setAttribute("id",idCesta);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
/*	
	private void porcionesCesta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Cesta cesta = new Cesta();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			cesta.buscarID(id);
		
		} catch (Exception e) {
			msg = "ERROR al cargar el cesta.";
		}
		
		request.setAttribute("mensaje",msg);
		request.setAttribute("cesta",cesta);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}

	//ya se ha validado via javascript
	private void crearPorciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Cesta cesta = new Cesta();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Double cantNew = Double.parseDouble(request.getParameter("cantNew"));
			int numNew = Integer.parseInt(request.getParameter("numNew"));
			Double cantDisp = Double.parseDouble(request.getParameter("cantDisp"));
			
			cesta.buscarID(id);
				
			for (int i = 0; i < numNew; i++) {
				Porcion p = new Porcion(cantNew, cesta);
				c.insertar();
				//cesta.cesta.getCantidadDisponible() - 
			}
			//carga los datos actualizados
			cesta.buscarID(id);
			
			msg = "Porción creada.";

		}
		catch (Exception e) {
			msg = "ERROR al crear las porciones el cesta.";
		}
		
		request.setAttribute("cesta",cesta);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}
	
	private void eliminarPorcion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "Porción eliminada.";
		Cesta cesta = new Cesta();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Porcion porcion = new Porcion();
			porcion.buscarID(id);
			int cestaId = porcion.getCesta().getId();
			porcion.eliminar();
			//carga los datos actualizados
			//cesta.buscarID(cestaId);
		
		} catch (Exception e) {
			msg = "ERROR al eliminar la porción.";
		}
		
		request.setAttribute("cesta",cesta);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}
	
	private void verCesta(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	*/
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarCestas(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarCestas(request, response);
	}    
}
