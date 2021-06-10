package controlador;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			default:
				System.out.println("Opcion no valida.");
		}
	}
    
	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String nick = request.getParameter("nick");
		String pass = request.getParameter("pass");
		//if(username.isEmpty() || password.isEmpty() )
		Usuario u = new Usuario();
		boolean status = u.esUsuarioValido(nick, pass);

		if(status)
		{ 
			//session.setAttribute("session","TRUE");  
			
			RequestDispatcher req = request.getRequestDispatcher("welcome.jsp");
			req.forward(request, response);
		}
		else
		{
			request.setAttribute("mensaje","Datos de usuario no válidos.");
			
			RequestDispatcher req = request.getRequestDispatcher("index.jsp");
			req.include(request, response);
		}
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
			Consumidor consumidor = new Consumidor(nick,nombre,apellidos,mail,ciudad,telefono);
			consumidor.setPassword(pass);
			consumidor.insertar();
			
			request.setAttribute("mensaje","Bienvenido! Acceda con su datos de usuario.");
			RequestDispatcher req = request.getRequestDispatcher("index.jsp");
			req.forward(request, response);
		}
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
