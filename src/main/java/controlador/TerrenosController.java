package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TerrenoDAO;
import modelo.Alimento;
import modelo.Terreno;
import modelo.Usuario;

/**
 * Servlet para procesar las peticiones de Terrenos
 * @author Iker Aparicio
 */
@WebServlet("/TerrenosController")
public class TerrenosController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 /**
     * Constructor vac?o
     */
    public TerrenosController() {
    }

    /**
     * Recoge la opcion indicada y la procesa
     */
	private void procesarTerrenos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
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
			case "8":
				cargarTerrenos(request, response);
				break;
			case "9":
				cargarSelectAlimentos(request, response);
				break;
			default:
				System.out.println("Opci?n no valida.");
		}
	}

	/**
	 * Da de alta el terreno segun los parametros pasados y vuelve a la pagina de alta con un mensaje de resultado.
	 */
	private void altaTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Terreno incluido.";
		try {
			String nombre = request.getParameter("nombre");
			int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
			double metros = Double.parseDouble(request.getParameter("metros"));
			String ciudad = request.getParameter("ciudad");
			String direccion = request.getParameter("direccion");

			Terreno  terreno = new Terreno(nombre, metros, ciudad, direccion, idUsuario);
			int id = terreno.insertar();
			terreno.buscarID(id);
			request.setAttribute("terreno",terreno);
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir el Terreno.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al introducir el Terreno.";
		}

		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina el terreno que tiene el id pasado y vuelve a la pagina del listado de terrenos con un mensaje de resultado.
	 */
	private void eliminarTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Terreno eliminado.";
		Terreno terreno = new Terreno();
		ArrayList<Terreno> terrenos = new ArrayList<Terreno>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			terreno = new Terreno();
			terreno.eliminar(id);
		} catch (Exception e) {
			msg = "ERROR al eliminar el terreno.";
		} finally {
			terrenos = terreno.obtenerTerrenos(0);
		}
		
		request.setAttribute("terrenos",terrenos);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terrenos.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Modifica el terreno con los parametros pasados y vuelve a la pagina del listado de terrenos con un mensaje de resultado.
	 */
	private void actualizarTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Terreno actualizado.";
		Terreno terreno = new Terreno();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String nombre = request.getParameter("nombre");
			int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
			double metros = Double.parseDouble(request.getParameter("metros"));
			String ciudad = request.getParameter("ciudad");
			String direccion = request.getParameter("direccion");
			
			terreno = new Terreno(id,nombre, metros, ciudad, direccion, idUsuario);
			terreno.actualizar();
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar el terreno.";
		} catch (SQLException e) {
			msg = "ERROR en la BBDD al modificar el terreno.";
		}
		
		request.setAttribute("terreno",terreno);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}

	/**
	 * Carga el terreno de id pasado y llama a la pagina de terreno 
	 */
	private void verDetalleTerreno(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			if (id != 0) {
				Terreno terreno = new Terreno();
				terreno.buscarID(id);
				request.setAttribute("terreno",terreno);
			}
		} catch (NumberFormatException e) {
			msg = "ERROR al cargar el terreno.";
		}

		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}

	/**
	 * Elimina el alimento indicado del terreno indicado y vuelve a la pagina del terreno con un mensaje de resultado.
	 */
	private void quitarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento quitado del terreno.";
		Terreno terreno = new Terreno();
		try {
			int idTerreno = Integer.parseInt(request.getParameter("idTerreno"));
			int idAlimento = Integer.parseInt(request.getParameter("idAlimento"));
			
			terreno.buscarID(idTerreno);
			terreno.quitarAlimento(idAlimento);
			terreno.buscarID(idTerreno);
		} 
		catch (Exception e) {
			msg = "ERROR al quitar el alimento del terreno.";
		}
		
		request.setAttribute("terreno",terreno);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}

	/**
	 * Agrega el alimento indicado al terreno indicado y vuelve a la pagina del terreno con un mensaje de resultado.
	 */
	private void agregarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento agregado al terreno.";
		Terreno terreno = new Terreno();
		try {
			int idTerreno = Integer.parseInt(request.getParameter("idTerreno"));
			int idAlimento = Integer.parseInt(request.getParameter("idAlimento"));
			
			terreno.buscarID(idTerreno);
			terreno.agregarAlimento(idAlimento);
			terreno.buscarID(idTerreno);
		} catch (Exception e) {
			msg = "ERROR al agregar el alimento al terreno.";
		}
		
		request.setAttribute("terreno",terreno);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("terreno.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Carga el terreno de id indicado y llama a la pagina de alimentos con los que esten disponibles para ese terreno
	 */
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
	 * Carga el listado de terrenos y llama a la pagina de listado de terrenos
	 * - si es productor lista solo sus terrenos, si no todos
	 */
	private void cargarTerrenos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		int idUsuario = 0;
		if (usuario.obtenerPermisosRol() == 5) {
			idUsuario = usuario.getId();
		}
		
		TerrenoDAO tDAO = new TerrenoDAO();
		ArrayList<Terreno> terrenos = tDAO.listTerrenos(idUsuario);	
		
		request.setAttribute("terrenos",terrenos);
		RequestDispatcher req = request.getRequestDispatcher("terrenos.jsp");
		req.forward(request, response);
	}
	
	/**
	 * Tratamiento de llamada AJAX. Carga un select con los alimentos que tiene el terreno con id pasada
	 */
	private void cargarSelectAlimentos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
        	int id = Integer.parseInt(request.getParameter("id"));
        	Terreno terreno = new Terreno();
        	terreno.buscarID(id);
        	ArrayList<Alimento> alimentos = terreno.obtenerAlimentos();
        	StringBuilder result = new StringBuilder();
        	result.append("<select id=\"producto\" name=\"producto\" required>");
        	if (alimentos != null) {
	        	for(Alimento alimento : alimentos) {
	        		result.append("<option value=\""+alimento.getId()+"\">"+alimento.getNombre()+ 
	        				" (" + alimento.getPrecio() + " " + alimento.getMedida() + ")</option>");
	        	}
        	}
        	else {
        		result.append("<option value=0>---sin alimentos---</option>");
        	}
        	result.append("</select>");
        	
            out.println(result.toString());
        }
	}
	
	/**
	 * todas las peticiones GET se gestionan a traves de procesarTerrenos()
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarTerrenos(request, response);
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * todas las peticiones POST se gestionan a traves de procesarTerrenos()
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarTerrenos(request, response);
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		}
	}
}
