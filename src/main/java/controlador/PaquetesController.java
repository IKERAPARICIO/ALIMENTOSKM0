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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import dao.PaqueteDAO;
import dao.TerrenoDAO;
import modelo.GeneradorPdf;
import modelo.Paquete;
import modelo.Porcion;
import modelo.Terreno;
import modelo.Usuario;

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
     * @throws SQLException 
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
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Aprueba el paquete que tiene el id pasado total o parcialmente y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void aprobarPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Paquete aprobado.";
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			int cantidad = Integer.parseInt(request.getParameter("cant"));
			
			Paquete paquete = new Paquete();
			paquete.aprobar(id,cantidad);
			propuestas = paquete.obtenerPropuestas("");
		
		} catch (Exception e) {
			msg = "ERROR al aprobar el paquete.";
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Aprueba parcialmente el paquete que tiene el id pasado y vuelve a la pagina del listado de paquetes con un mensaje de resultado.
	 */
	private void rechazarPaquete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Paquete rechazado.";
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			paquete.rechazar(id);
			propuestas = paquete.obtenerPropuestas("");
		
		} catch (Exception e) {
			msg = "ERROR al rechazar el paquete.";
		}
		
		request.setAttribute("propuestas",propuestas);
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
	
	private void cargarPropuestas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		String fEstado = "";
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");

		try {
			if (request.getParameter("fEstado") != null) {
				fEstado = request.getParameter("fEstado");
			}
			
			PaqueteDAO pDAO = new PaqueteDAO();
			//si es productor lista sus propuestas, si es gestor todas
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
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher req = request.getRequestDispatcher("propuestas.jsp");
		req.forward(request, response);
	}
	
	private void cargarAlmacen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String fEstado = "";
		if (request.getParameter("fEstado") != null) {
			fEstado = request.getParameter("fEstado");
		}
		
		PaqueteDAO pDAO = new PaqueteDAO();
		ArrayList<Paquete> almacen = pDAO.listAlmacen(fEstado);	
		
		request.setAttribute("almacen",almacen);
		request.setAttribute("fEstado",fEstado);
		RequestDispatcher req = request.getRequestDispatcher("almacen.jsp");
		req.forward(request, response);
	}
	
	private void descargarJustificante(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		Document document = new Document(); 
		String filePath = "C:\\tmp\\";
		String fileNameEnd = "reciboProductor.pdf";
	    try 
	    { 
	    	int idPaquete = Integer.parseInt(request.getParameter("id"));	
	    	String fileName = filePath+idPaquete+"_"+fileNameEnd;
	    	//PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Test.pdf"));
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
	    
		
		String fEstado = "";
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
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}

	private void altaPropuesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		String msg = "Propuesta incluida.";
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>(); 
		try {
			int idTerreno = Integer.parseInt(request.getParameter("terreno"));
			int idAlimento = Integer.parseInt(request.getParameter("producto"));
			double cantidadPropuesta = Double.parseDouble(request.getParameter("cantidadPropuesta"));
			
			Paquete paquete = new Paquete(idTerreno, idAlimento, cantidadPropuesta);
			paquete.insertar();
			
			PaqueteDAO pDAO = new PaqueteDAO();
			propuestas = pDAO.listMyPropuestas("",usuario.getId());
		
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir la Propuesta.";
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}

	private void eliminarPropuesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Propuesta eliminada.";
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		
		ArrayList<Paquete> propuestas = new ArrayList<Paquete>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Paquete paquete = new Paquete();
			paquete.eliminar(id);
			
			PaqueteDAO pDAO = new PaqueteDAO();
			propuestas = pDAO.listMyPropuestas("",usuario.getId());
		} catch (Exception e) {
			msg = "ERROR al eliminar la propuesta.";
		}
		
		request.setAttribute("propuestas",propuestas);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuestas.jsp");
		vista.forward(request, response);
	}

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
		}

		request.setAttribute("propuesta",paquete);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("propuesta.jsp");
		vista.forward(request, response);
	}

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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarPaquetes(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarPaquetes(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
