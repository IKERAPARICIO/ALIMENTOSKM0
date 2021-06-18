package controlador;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import dao.PaqueteDAO;
import dao.TerrenoDAO;
import modelo.Estado;
import modelo.GeneradorPdf;
import modelo.Paquete;
import modelo.Porcion;
import modelo.Terreno;
import modelo.Usuario;

/**
 * Servlet para procesar las peticiones de Paquetes
 * @author Iker Aparicio
 */
@WebServlet("/PaquetesController")
public class PaquetesController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Constructor vacío
     */
    public PaquetesController() {
    }

   /**
    * Recoge la opcion indicada y la procesa
    */
	private void procesarPaquetes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		switch (request.getParameter("opcion")) {
			case "1":
				aprobarPaquete(request, response);
				break;
			case "2":
				rechazarPaquete(request, response);
				break;
			case "3":
				finalizarPaquete(request, response);
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
			case "8":
				cargarPropuestas(request, response);
				break;
			case "9":
				cargarAlmacen(request, response);
				break;
			case "10":
				descargarJustificante(request, response);
				break;
			case "11":
				altaPropuesta(request, response);
				break;
			case "12":
				eliminarPropuesta(request, response);
				break;
			case "13":
				actualizarPropuesta(request, response);
				break;
			case "14":
				verDetallePropuesta(request, response);
				break;
			default:
				System.out.println("Opción no valida.");
		}
	}

	/**
	 * Aprueba el paquete que tiene el id pasado total o parcialmente y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void aprobarPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Propuesta aprobada.";
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		Paquete paquete = new Paquete();
		String fEstado = Estado.ACEPTADO.toString();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Double cantidad = Double.parseDouble(request.getParameter("cant"));
			paquete.aprobar(id,cantidad);
		} catch (Exception e) {
			msg = "ERROR al aprobar la propuesta.";
		} finally {
			propuestas = paquete.obtenerPropuestas(fEstado);
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Rechaza el paquete que tiene el id pasado y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void rechazarPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Propuesta rechazada.";
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		Paquete paquete = new Paquete();
		String fEstado = Estado.RECHAZADO.toString();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			paquete.rechazar(id);
		} catch (Exception e) {
			msg = "ERROR al rechazar la propuesta.";
		} finally {
			propuestas = paquete.obtenerPropuestas(fEstado);
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Finaliza el paquete que tiene el id pasado y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void finalizarPaquete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "Paquete finalizado, se ha actualizado su cantidad a 0.";
		ArrayList<Paquete> almacen = new ArrayList<Paquete>();
		boolean disponible = true;
		String sDisponible = "si";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			paquete.finalizar(id);
			
			PaqueteDAO pDAO = new PaqueteDAO();
			almacen = pDAO.listAlmacen(disponible);	
		
		} catch (Exception e) {
			msg = "ERROR al finalizar el paquete.";
		}
		
		request.setAttribute("sDisponible",sDisponible);
		request.setAttribute("almacen",almacen);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacen.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Carga el paquete que tiene el id pasado y llama a la pagina de sus porciones
	 */
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

	/**
	 * Crea las porciones con los parametros pasados en el paquete que tiene el id pasado y llama a la pagina de sus porciones
	 * Previamente se ha hecho la validacion Javascript del numero de porciones y cantidades validas
	 */
	private void crearPorciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Paquete paquete = new Paquete();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Double cantNew = Double.parseDouble(request.getParameter("cantNew"));
			int numNew = Integer.parseInt(request.getParameter("numNew"));
			Double cantDisp = Double.parseDouble(request.getParameter("cantDisp"));
			
			paquete.buscarID(id);
			for (int i = 0; i < numNew; i++) {
				Porcion p = new Porcion(cantNew, paquete);
				p.insertar();
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
	
	/**
	 * Elimina la porcion indicada y llama a la pagina de las porciones del paquete en el que se encontraba
	 */
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
			paquete.buscarID(paqueteId);
		
		} catch (Exception e) {
			msg = "ERROR al eliminar la porción.";
		}
		
		request.setAttribute("paquete",paquete);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("almacenadoPorciones.jsp");
		vista.forward(request, response);
	}
		
	/**
	 * Carga el listado de propuestas segun el estado pasado y llama a la pagina de propuestas
	 * Si es productor lista sus propuestas, si es gestor todas
	 */
	private void cargarPropuestas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		//por defecto carga las PROPUESTAS
		String fEstado = this.getEstadoDefectoFiltros();
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");

		try {
			if (request.getParameter("fEstado") != null) {
				fEstado = request.getParameter("fEstado");
			}
			PaqueteDAO pDAO = new PaqueteDAO();
			if (usuario.obtenerPermisosRol() < 8) {
				propuestas = pDAO.listMyPropuestas(fEstado,usuario.getId());
			}
			else {
				propuestas = pDAO.listPropuestas(fEstado);
			}
		} catch (Exception e) {
			msg = "ERROR al cargar las propuestas.";
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher req = request.getRequestDispatcher("propuestas.jsp");
		req.forward(request, response);
	}
	
	/**
	 * Carga el listado de paquetes segun el parametro de cantidad disponible pasado y llama a la pagina de almacen
	 */
	private void cargarAlmacen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//por defecto carga los paquetes con alguna cantidad disponible
		String sDisponible = "si";
		if (request.getParameter("sDisponible") != null) {
			sDisponible = request.getParameter("sDisponible");
		}
		
		Boolean disponible = ("si".equals(sDisponible)) ? true : false;
		PaqueteDAO pDAO = new PaqueteDAO();
		ArrayList<Paquete> almacen = pDAO.listAlmacen(disponible);	
		
		request.setAttribute("almacen",almacen);
		request.setAttribute("sDisponible",sDisponible);
		RequestDispatcher req = request.getRequestDispatcher("almacen.jsp");
		req.forward(request, response);
	}
	
	/**
	 * Genera un PDF para el Productor como recibo de la propuesta aceptada indicada y vuelve a mostrar sus propuestas
	 */
	private void descargarJustificante(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Document document = new Document(); 
		String filePath = "C:\\tmp\\";
		String fileNameEnd = "reciboProductor.pdf";
	    try 
	    { 
	    	int idPaquete = Integer.parseInt(request.getParameter("id"));	
	    	String fileName = filePath+idPaquete+"_"+fileNameEnd;
	    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    	document.open(); 
	    	GeneradorPdf pdf = new GeneradorPdf();
	    	pdf.crearReciboPaquete(document,idPaquete);
	    	document.close(); 
	    	writer.close(); 
	    	msg = "Se ha generado el recibo en " + fileName + ". Preséntelo como justificante.";
	    } catch (NumberFormatException e) {
			msg = "ERROR al cargar la propuesta.";
		} catch (DocumentException e) 
	    { 
	    	e.printStackTrace(); 
	    	msg = "Error al generar el documento pdf.";
	    } catch (FileNotFoundException e) 
	    { 
	    	e.printStackTrace(); 
	    	msg = "Error al generar el fichero pdf, asegurese que la carpeta "+filePath+" exista.";
	    }
	    
	    //carga su listado de aceptados para mostrarlo el la pagina
		String fEstado = Estado.ACEPTADO.toString();
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		try {			
			PaqueteDAO pDAO = new PaqueteDAO();
			propuestas = pDAO.listMyPropuestas(fEstado,usuario.getId());
		} catch (Exception e) {
			msg += "ERROR al cargar las propuestas.";
		}

		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}

	/**
	 * Da de alta la propuesta segun los parametros pasados y vuelve a la pagina de las propuestas del usuario con un mensaje de resultado.
	 */
	private void altaPropuesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		String msg = "Propuesta incluida.";
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>(); 
		String fEstado = this.getEstadoDefectoFiltros();
		try {
			int idTerreno = Integer.parseInt(request.getParameter("terreno"));
			int idAlimento = Integer.parseInt(request.getParameter("producto"));
			double cantidadPropuesta = Double.parseDouble(request.getParameter("cantidadPropuesta"));
			
			Paquete paquete = new Paquete(idTerreno, idAlimento, cantidadPropuesta);
			paquete.insertar();
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir la Propuesta.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al introducir la Propuesta.";
		} finally {
			PaqueteDAO pDAO = new PaqueteDAO();
			propuestas = pDAO.listMyPropuestas(fEstado,usuario.getId());
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}

	/**
	 * Elimina la propuesta de id pasado y vuelve a la pagina de las propuestas del usuario con un mensaje de resultado.
	 */
	private void eliminarPropuesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Propuesta eliminada.";
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		String fEstado = this.getEstadoDefectoFiltros();
		
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			paquete.eliminar(id);
			
			PaqueteDAO pDAO = new PaqueteDAO();
			propuestas = pDAO.listMyPropuestas(fEstado,usuario.getId());
		} catch (Exception e) {
			msg = "ERROR al eliminar la propuesta.";
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}

	/**
	 * Actualiza la propuesta segun los parametros pasados y vuelve a la pagina de la propuesta
	 */
	private void actualizarPropuesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Propuesta actualizada.";	
		Paquete paquete = new Paquete();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			double cantidadPropuesta = Double.parseDouble(request.getParameter("cantidadPropuesta"));
						
			paquete.setId(id);
			paquete.setCantidadPropuesta(cantidadPropuesta);
			paquete.actualizar();
			paquete.buscarID(id);	
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar la propuesta.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al modificar la propuesta.";
		}

		request.setAttribute("propuesta",paquete);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuesta.jsp");
		vista.forward(request, response);
	}

	/**
	 * Carga la propuesta indicada y su terreno y llama a la pagina de la propuesta 
	 */
	private void verDetallePropuesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		String msg = null;
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		
		ArrayList<Terreno> terrenos = new ArrayList<Terreno>();
		try {
			TerrenoDAO tDAO = new TerrenoDAO();
			terrenos = tDAO.listTerrenos(usuario.getId());

			int id = Integer.parseInt(request.getParameter("id"));
			if (id != 0) {
				Paquete paquete = new Paquete();
				paquete.buscarID(id);
				request.setAttribute("propuesta",paquete);
			}
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar el alimento.";
		}

		request.setAttribute("terrenos",terrenos);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuesta.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Busca y devuelve el valor que estado de los paquetes por defecto
	 * @return
	 */
	private String getEstadoDefectoFiltros() {
		Paquete paquete = new Paquete();
		return paquete.getValorDefectoFIltro();
	}
	
	/**
	 * todas las peticiones GET se gestionan a traves de procesarPaquetes()
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarPaquetes(request, response);
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * todas las peticiones POST se gestionan a traves de procesarPaquetes()
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarPaquetes(request, response);
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		}
	}
}
