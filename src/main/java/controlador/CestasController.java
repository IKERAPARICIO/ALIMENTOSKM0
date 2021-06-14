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

import dao.CestaDAO;
import dao.PaqueteDAO;
import dao.TerrenoDAO;
import modelo.Cesta;
import modelo.GeneradorPdf;
import modelo.Paquete;
import modelo.Porcion;
import modelo.Terreno;
import modelo.Usuario;

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
     * @throws SQLException 
     */
	private void procesarCestas(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
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
			case "7":
				comprarCesta(request, response);
				break;
			case "8":
				verMisCestas(request, response);
				break;
			case "9":
				verCestasDisponibles(request, response);
				break;
			case "10":
				cargarCestas(request, response);
				break;
			case "11":
				verPorcionesDisponibles(request, response);
				break;
			case "12":
				descargarJustificante(request, response);
				break;
			default:
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Elimina la cesta que tiene el id pasado y vuelve a la pagina del listado de cestas con un mensaje de resultado.
	 */
	private void eliminarCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Cesta eliminada.";
		ArrayList<Cesta> cestas = new ArrayList<Cesta>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Cesta cesta = new Cesta();
			cesta.eliminar(id);
			cestas = cesta.obtenerCestas();
		} catch (Exception e) {
			msg = "ERROR al eliminar la cesta.";
		}

		request.setAttribute("cestas",cestas);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("confeccionarCestas.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina la porcion de la cesta indicada y vuelve a la pagina del listado de cestas con un mensaje de resultado.
	 */
	private void quitarPorcion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Porci�n quitada de la cesta.";
		Cesta cesta = new Cesta();
		try {
			int idCesta = Integer.parseInt(request.getParameter("idCesta"));
			int idPorcion = Integer.parseInt(request.getParameter("idPorcion"));
			
			cesta.buscarID(idCesta);
			cesta.quitarPorcion(idPorcion);
		} catch (Exception e) {
			msg = "ERROR al quitar la porci�n de la cesta.";
		}
		
		request.setAttribute("cesta",cesta);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void altaCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Cesta creada.";
		try {
			String nombre = request.getParameter("nombre");	
			Cesta cesta = new Cesta(nombre);
			int id = cesta.insertar();
			//carga el resto de datos
			cesta.buscarID(id);
			
			request.setAttribute("cesta",cesta);
		} catch (NumberFormatException e) {
			msg = "ERROR al crear la cesta.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al insertar la cesta.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void actualizarCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Cesta actualizada.";
		Cesta cesta = new Cesta();
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			String nombre = request.getParameter("nombre");
			String sPreparada = request.getParameter("preparada");
			boolean preparada = "0".equals(sPreparada) ? false : true;

			cesta = new Cesta(id, nombre, preparada);
			cesta.actualizar();
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar la cesta.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al modificar la cesta.";
		} finally {
			//carga el resto de datos
			cesta.buscarID(id);
			request.setAttribute("cesta",cesta);
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void verDetalleCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			if (id != 0) {
				Cesta cesta = new Cesta();
				cesta.buscarID(id);
				request.setAttribute("cesta",cesta);
			}
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar la cesta.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void agregarPorcion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Porci�n agregada a la cesta.";
		Cesta cesta = new Cesta();
		try {
			int idCesta = Integer.parseInt(request.getParameter("idCesta"));
			int idPorcion = Integer.parseInt(request.getParameter("idPorcion"));
			
			cesta.buscarID(idCesta);
			cesta.agregarPorcion(idPorcion);
		} catch (Exception e) {
			msg = "ERROR al agregar la porci�n a la cesta.";
		}
		
		request.setAttribute("cesta",cesta);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cesta.jsp");
		vista.forward(request, response);
	}
	
	private void comprarCesta(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		String msg = null;
		int id = 0;
		ArrayList<Cesta> listaCestas = new ArrayList<Cesta>();
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		try {
			id = Integer.parseInt(request.getParameter("id"));
			
			Cesta cesta = new Cesta();
			cesta.buscarID(id);
			cesta.comprar(usuario.getId());

			CestaDAO cDAO = new CestaDAO();
			listaCestas = cDAO.listMyCestas(usuario.getId());	
		} catch (NumberFormatException e) {
			msg = "ERROR al comprar la cesta.";
		}
		
		request.setAttribute("listaCestas",listaCestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("miscestas",true);
		RequestDispatcher vista = request.getRequestDispatcher("cestas.jsp");
		vista.forward(request, response);
	}
	
	private void verMisCestas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
		ArrayList<Cesta> listaCestas = new ArrayList<Cesta>();
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		try {
			CestaDAO cDAO = new CestaDAO();
			//si es productor lista las cestas en las que hay porciones de sus terrenos, si es consumidor sus cestas compradas
			if (usuario.obtenerPermisosRol() < 4) {
				listaCestas = cDAO.listMyCestas(usuario.getId());
			}
			else {
				listaCestas = cDAO.listCestasMyTerrenos(usuario.getId());
			}
		} catch (Exception e) {
			msg = "ERROR al cargar las cestas.";
		}
		
		request.setAttribute("listaCestas",listaCestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("miscestas",true);
		RequestDispatcher vista = request.getRequestDispatcher("cestas.jsp");
		vista.forward(request, response);
	}
	
	private void verCestasDisponibles(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		String msg = null;
		ArrayList<Cesta> listaCestas = new ArrayList<Cesta>();
		try {
			CestaDAO cDAO = new CestaDAO();
			listaCestas = cDAO.listCestasDisponibles();
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar las cestas.";
		}
		
		request.setAttribute("listaCestas",listaCestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("miscestas",false);
		RequestDispatcher vista = request.getRequestDispatcher("cestas.jsp");
		vista.forward(request, response);
	}
	
	private void cargarCestas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		CestaDAO cest = new CestaDAO();
		ArrayList<Cesta> cestas = cest.listCestas();
		
		request.setAttribute("cestas",cestas);
		RequestDispatcher req = request.getRequestDispatcher("confeccionarCestas.jsp");
		req.forward(request, response);
	}
	
	
	private void verPorcionesDisponibles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		String msg = null;
		ArrayList<Porcion> porciones = new ArrayList<Porcion>();
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			PaqueteDAO pDAO = new PaqueteDAO();
			porciones = pDAO.listPorcionesDisponibles();
		} catch (Exception e) {
			msg = "ERROR al cargar las porciones disponibles.";
		}
		
		request.setAttribute("porciones",porciones);
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("cestaPorciones.jsp");
		vista.forward(request, response);
	}
	
	private void descargarJustificante(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String msg = null;
		Document document = new Document(); 
		String filePath = "C:\\tmp\\";
		String fileNameEnd = "reciboConsumidor.pdf";
	    try 
	    { 
	    	int idCesta = Integer.parseInt(request.getParameter("id"));	
	    	String fileName = filePath+idCesta+"_"+fileNameEnd;
	    	//PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Test.pdf"));
	    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    	document.open(); 
	    	GeneradorPdf pdf = new GeneradorPdf();
	    	pdf.crearReciboCesta(document,idCesta);
	    	document.close(); 
	    	writer.close(); 
	    	msg = "Se ha generado el recibo en " + fileName + ". Pres�ntelo como justificante.";
	    } catch (NumberFormatException e) {
			msg = "ERROR al cargar la cesta.";
		} catch (DocumentException e) 
	    { 
	    	e.printStackTrace(); 
	    	msg = "Error al generar el documento pdf.";
	    } catch (FileNotFoundException e) 
	    { 
	    	e.printStackTrace(); 
	    	msg = "Error al generar el fichero pdf, asegurese que la carpeta "+filePath+" exista.";
	    }
	    
		
		ArrayList<Cesta> listaCestas = new ArrayList<Cesta>();
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		try {
			CestaDAO cDAO = new CestaDAO();
			listaCestas = cDAO.listMyCestas(usuario.getId());	
		} catch (NumberFormatException e) {
			msg = "ERROR al comprar la cesta.";
		}
		
		request.setAttribute("listaCestas",listaCestas);
		request.setAttribute("mensaje",msg);
		request.setAttribute("miscestas",true);
		RequestDispatcher vista = request.getRequestDispatcher("cestas.jsp");
		vista.forward(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarCestas(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarCestas(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}
