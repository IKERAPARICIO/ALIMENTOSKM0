package controlador;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import modelo.Consumidor;
import modelo.Usuario;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
    }

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
				System.out.println("Opcion no valida.");
		}
	}
    
	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String nick = request.getParameter("nick");
		String pass = request.getParameter("pass");
		//if(username.isEmpty() || password.isEmpty() )
		Usuario usuario = new Usuario();
		int idUsuario = usuario.idUsuarioValido(nick, pass);

		if(idUsuario != 0)
		{ 
			usuario.buscarID(idUsuario);
			int nivelAcceso = usuario.obtenerPermisosRol();
			//session.setAttribute("session","TRUE");  
			HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", usuario);
            sesion.setAttribute("nivelAcceso", nivelAcceso);
            
            String vista = "index.jsp";
            if (nivelAcceso > 8)
            	vista = "usuarios.jsp";
            else if (nivelAcceso > 4)
            	vista = "terrenos.jsp";
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
	
	private void accesoInvitado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		HttpSession sesion = request.getSession();
		sesion.setAttribute("nivelAcceso", 1);
        
		RequestDispatcher req = request.getRequestDispatcher("CestasController?opcion=9");
		req.forward(request, response);
	}

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
			Consumidor consumidor = new Consumidor(nick,pass,nombre,apellidos,mail,ciudad,telefono,sRol);
			consumidor.setPassword(pass);
			consumidor.insertar();
			
			request.setAttribute("mensaje","Bienvenido! Acceda con su datos de usuario.");
			RequestDispatcher req = request.getRequestDispatcher("index.jsp");
			req.forward(request, response);
		}
	}

	private void cargarPaginaInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//HttpSession sesion = request.getSession();
		//sesion.invalidate();
		
		request.setAttribute("mensaje","No tiene permisos para ver la página indicada.");
		RequestDispatcher req = request.getRequestDispatcher("index.jsp");
		req.forward(request, response);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			procesarLogin(request, response);
		} catch (IOException | ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
