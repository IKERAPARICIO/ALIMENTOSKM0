package controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Rol;
import modelo.Usuario;
import modelo.Productor;

/**
 * Servlet implementation class UsuariosServlet
 */
@WebServlet("/UsuariosController")
public class UsuariosController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    //constructor vacio
    public UsuariosController() {
    }
    
    /**
     * Dependiendo la opcion indicada, da de alta un usuario, lo elimina o actualiza
     */
	private void procesarUsuarios(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		switch (request.getParameter("opcion")) {
			case "1":
				altaUsuario(request, response);
				break;
			case "2":
				eliminarUsuario(request, response);
				break;
			case "3":
				actualizarUsuario(request, response);
				break;
			default:
				System.out.println("Opcion no valida.");
		}
	}
	
	/**
	 * Da de alta el usuario segun los parametros pasados y vuelve a la pagina de alta con un mensaje de resultado.
	 */
	private void altaUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Usuario incluido.";
		try {		
			String nick = request.getParameter("nick");
			String password = request.getParameter("password");
			String nombre = request.getParameter("nombre");
			String apellidos = request.getParameter("apellidos");
			String mail = request.getParameter("mail");
			String ciudad = request.getParameter("ciudad");
			String telefono = request.getParameter("telefono");
			String sRol = request.getParameter("sRol");
			
			if (sRol.equals(Rol.PRODUCTOR.toString())){
				String dni = request.getParameter("dni");
				String direccion = request.getParameter("direccion");
				Productor usuario = new Productor(nick, password, nombre, apellidos, mail, ciudad, telefono, sRol, dni, direccion);
				usuario.insertar();
			}
			else {
				Usuario usuario = new Usuario(nick, password, nombre, apellidos, mail, ciudad, telefono, sRol);
				usuario.insertar();
			}
			
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir el usuario.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("usuarios.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina el usuario que tiene el id pasado y vuelve a la pagina del listado de usuario con un mensaje de resultado.
	 */
	private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Usuario eliminado.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Usuario usuario = new Usuario();
			usuario.eliminar(id);
		} catch (Exception e) {
			msg = "ERROR al eliminar el usuario.";
		}
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("usuarios.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Modifica el precio del usuario que tiene el id pasado y vuelve a la pagina del listado de usuarios con un mensaje de resultado.
	 */
	private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Usuario actualizado.";
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String nick = request.getParameter("nick");
			String password = request.getParameter("password");
			String nombre = request.getParameter("nombre");
			String apellidos = request.getParameter("apellidos");
			String mail = request.getParameter("mail");
			String ciudad = request.getParameter("ciudad");
			String telefono = request.getParameter("telefono");
			String sRol = request.getParameter("sRol");
			
			if (sRol.equals(Rol.PRODUCTOR.toString())){
				String dni = request.getParameter("dni");
				String direccion = request.getParameter("direccion");
				Productor usuario = new Productor(id, nick, password, nombre, apellidos, mail, ciudad, telefono, sRol, dni, direccion);
				usuario.actualizar();
			}
			else {
				Usuario usuario = new Usuario(id, nick, password, nombre, apellidos, mail, ciudad, telefono, sRol);
				usuario.actualizar();
			}
		} catch (NumberFormatException e) {
			msg = "ERROR al modificar el usuario.";
		} 		
		
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("usuarios.jsp");
		vista.forward(request, response);
	}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	procesarUsuarios(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		procesarUsuarios(request, response);
	}
}
