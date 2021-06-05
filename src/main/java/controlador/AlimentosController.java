package controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Alimento;

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
     */
	private void procesarAlimentos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
			default:
				System.out.println("Opcion no valida.");
		}
	}

	/**
	 * Da de alta el alimento segun los parametros pasados y vuelve a la pagina de alta con un mensaje de resultado.
	 */
	private void altaAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento incluido.";
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
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina el alimento que tiene el id pasado y vuelve a la pagina del listado de alimento con un mensaje de resultado.
	 */
	private void eliminarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento eliminado.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Alimento alimento = new Alimento();
			alimento.eliminar(id);
		
		} catch (Exception e) {
			msg = "ERROR al eliminar el alimento.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Modifica el precio del alimento que tiene el id pasado y vuelve a la pagina del listado de alimentos con un mensaje de resultado.
	 */
	private void actualizarAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Alimento actualizado.";
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
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar el alimento.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}

	/**
	 *  TODO: quitar el metodo
	 */
	/*private void historicoPreciosAlimento(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Histórico de precios del Alimento";
		Map<String,String> historico = new HashMap<>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Alimento alimento = new Alimento();
			historico = alimento.getHistoricoPrecios(id);
		
		} catch (Exception e) {
			msg = "ERROR al buscar el historico de precios del alimento.";
		}
		
		request.setAttribute("mensaje",msg);
		request.setAttribute("historico",historico);
		RequestDispatcher vista = request.getRequestDispatcher("alimentos.jsp");
		vista.forward(request, response);
	}*/
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarAlimentos(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarAlimentos(request, response);
	}
}
