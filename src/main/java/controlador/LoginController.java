package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.Consumidor;
import modelo.Usuario;

/**
 * Servlet para procesar las peticiones de Login
 * @author Iker Aparicio
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * Constructor vacío
     */
    public LoginController() {
    }

    /**
     * Recoge la opcion indicada y la procesa
     */
    private void procesarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
		switch (request.getParameter("opcion")) {
			case "1":
				checkLogin(request, response);
				break;
			case "2":
				registroUsuario(request, response);
				break;
			case "3":
				accesoInvitado(request, response);
				break;
			case "4":
				cargarPaginaInicio(request, response);
				break;
			default:
				System.out.println("Opción no valida.");
		}
	}
    
    /**
     * Comprueba que el nick y password pasados sean validos:
     * - Si es incorrecto vuelve a la pagina login con un mensaje
     * - Si es correcto carga la pagina inicial segun el rol del usuario validado y guarda en las variables de sesion el usurio y su nivel de acceso
     */
	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String nick = request.getParameter("nick");
		String pass = request.getParameter("pass");
		Usuario usuario = new Usuario();
		int idUsuario = usuario.idUsuarioValido(nick, pass);

		if(idUsuario != 0)
		{ 
			usuario.buscarID(idUsuario);
			int nivelAcceso = usuario.obtenerPermisosRol();
			HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", usuario);
            sesion.setAttribute("nivelAcceso", nivelAcceso);
            
            String vista = "index.jsp";
            if (nivelAcceso > 8)
            	vista = "UsuariosController?opcion=5";
            else if (nivelAcceso > 4)
            	vista = "TerrenosController?opcion=8";
            else if (nivelAcceso > 2)
            	vista = "CestasController?opcion=9";
           
			RequestDispatcher req = request.getRequestDispatcher(vista);
			req.forward(request, response);
		}
		else
		{
			request.setAttribute("mensaje","Datos de usuario no válidos.");
			RequestDispatcher req = request.getRequestDispatcher("index.jsp");
			req.include(request, response);
		}
	}
	
	/**
	 * Carga la pagina principal de los usuarios invitados y especifica su nivel de acceso
	 */
	private void accesoInvitado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		HttpSession sesion = request.getSession();
		sesion.setAttribute("nivelAcceso", 1);
        
		RequestDispatcher req = request.getRequestDispatcher("CestasController?opcion=9");
		req.forward(request, response);
	}

	/**
	 * Da de alta un Consumidor con los datos pasados:
	 * - Si faltan datos por completar lo indica en la misma pagina
	 * - Si es correcto le da la bienvenida y reenvia al Login
	 */
	private void registroUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		String nick = request.getParameter("nick");
		String pass = request.getParameter("pass");
		String mail = request.getParameter("mail");
		String ciudad = request.getParameter("ciudad");
		String telefono = request.getParameter("telefono");
		
		if(nombre.isEmpty() || apellidos.isEmpty() || nick.isEmpty() || 
				pass.isEmpty() || mail.isEmpty() || ciudad.isEmpty() || telefono.isEmpty())
		{
			request.setAttribute("mensaje","Datos de usuario incompletos.");
			RequestDispatcher req = request.getRequestDispatcher("registro.jsp");
			req.forward(request, response);
		}
		else
		{
			//alta de Consumidor
			String sRol = "CONSUMIDOR";
			String msg = "";
			try {
				Consumidor consumidor = new Consumidor(nick,pass,nombre,apellidos,mail,ciudad,telefono,sRol);
				consumidor.setPassword(pass);
				consumidor.insertar();
				msg = "Bienvenid@ " + nombre + "! Acceda con su datos de usuario.";
			} catch (SQLIntegrityConstraintViolationException sqlError) {
				msg = "No se ha podido crear el usuario, el nick usado ya existe.";
			} catch (Exception e) {
				msg = "ERROR al crear el usuario.";
			}
			
			request.setAttribute("mensaje",msg);
			RequestDispatcher req = request.getRequestDispatcher("index.jsp");
			req.forward(request, response);
		}
	}

	/**
	 * Carga la pagina inicial de login y en caso de indicarse algun error lo muestra 
	 */
	private void cargarPaginaInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		HttpSession sesion = request.getSession();
		sesion.invalidate();
		String msg = "";
		if (request.getParameter("error") != null) {
			int error = Integer.parseInt(request.getParameter("error"));
			if (error == 1) {
				msg = "Sesión cerrada, vuelva a validarse.";
			}
			else if (error == 2) {
				msg = "No tiene permisos para ver la página indicada.";
			}
		}
		else {
			msg = "Ha finalizado la sesión.";
		}
		request.setAttribute("mensaje",msg);
		RequestDispatcher req = request.getRequestDispatcher("index.jsp");
		req.forward(request, response);
	}
	
	/**
	 * todas las peticiones GET se gestionan a traves de procesarLogin()
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarLogin(request, response);
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * todas las peticiones POST se gestionan a traves de procesarLogin()
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarLogin(request, response);
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		}
	}
}
