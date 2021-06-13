package controlador;

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

import dao.UsuarioDAO;
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
     * @throws SQLException 
     */
	private void procesarUsuarios(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
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
			case "4":
				cargarPerfil(request, response);
				break;
			case "5":
				cargarUsuarios(request, response);
				break;
			case "6":
				cargarPaginaUsuario(request, response);
				break;
			default:
				System.out.println("Opcion no valida.");
		}
	}
	
	/**
	 * Da de alta el usuario segun los parametros pasados y vuelve a la pagina de alta con un mensaje de resultado.
	 * @throws SQLException 
	 */
	private void altaUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Usuario incluido.";
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
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
				usuarios = usuario.obtenerUsuarios("");
			}
			else {
				Usuario usuario = new Usuario(nick, password, nombre, apellidos, mail, ciudad, telefono, sRol);
				usuario.insertar();
				usuarios = usuario.obtenerUsuarios("");
			}
		} catch (NumberFormatException e) {
			msg = "ERROR al introducir el usuario.";
		}
		
		request.setAttribute("usuarios",usuarios);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("usuarios.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Elimina el usuario que tiene el id pasado y vuelve a la pagina del listado de usuario con un mensaje de resultado.
	 * @throws SQLException 
	 */
	private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Usuario eliminado.";
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Usuario usuario = new Usuario();
			usuario.eliminar(id);
			usuarios = usuario.obtenerUsuarios("");
		} catch (Exception e) {
			msg = "ERROR al eliminar el usuario.";
		}
		
		request.setAttribute("usuarios",usuarios);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("usuarios.jsp");
		vista.forward(request, response);
	}
	
	/**
	 * Modifica el precio del usuario que tiene el id pasado y vuelve a la pagina del listado de usuarios con un mensaje de resultado.
	 */
	private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String msg = "Usuario actualizado.";
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			String nick = request.getParameter("nick");
			String password = request.getParameter("password");
			String nombre = request.getParameter("nombre");
			String apellidos = request.getParameter("apellidos");
			String mail = request.getParameter("mail");
			String ciudad = request.getParameter("ciudad");
			String telefono = request.getParameter("telefono");
			String sRol = request.getParameter("sRol");
			
			//si un Productor acutaliza su perfil no le pasa el sRol del formulario
			//mira si es Productor actualizando su perfil o el gestor actualizando un Productor
			HttpSession sesion = request.getSession();
			Usuario usuarioSes = (Usuario)sesion.getAttribute("usuario");
			if (usuarioSes.getRolName().equals(Rol.PRODUCTOR.toString())) {
				sRol = usuarioSes.getRolName();
			}
				
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
		
		request.setAttribute("id",id);
		request.setAttribute("mensaje",msg);
		RequestDispatcher vista = request.getRequestDispatcher("usuario.jsp");
		vista.forward(request, response);
	}
	
	private void cargarPerfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario)sesion.getAttribute("usuario");
		
		request.setAttribute("id",usuario.getId());
		RequestDispatcher req = request.getRequestDispatcher("usuario.jsp");
		req.forward(request, response);
	}
	
	private void cargarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String rol = request.getParameter("rol");
		
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		UsuarioDAO uDAO = new UsuarioDAO();
		usuarios = uDAO.listUsuarios(rol);
		
		request.setAttribute("usuarios",usuarios);
		RequestDispatcher req = request.getRequestDispatcher("usuarios.jsp");
		req.forward(request, response);
	}
	
	private void cargarPaginaUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("id") != null) {
			request.setAttribute("id",Integer.parseInt(request.getParameter("id")));
		}
		
		RequestDispatcher req = request.getRequestDispatcher("usuario.jsp");
		req.forward(request, response);
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
			procesarUsuarios(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarUsuarios(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
