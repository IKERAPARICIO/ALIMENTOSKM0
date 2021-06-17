<%
//definicion de permisos por pagina jsp dependiendo del nivel de acceso
String nombrePagina = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1,request.getRequestURI().lastIndexOf("."));

int nivelPagina = 0;
switch(nombrePagina){
	case "cestas":
	case "usuarios":
	case "terreno":
	case "terrenos":
		nivelPagina = 1;
		break;
	case "usuario":
		nivelPagina = 3;
		break;
	case "propuestas":
	case "propuesta":
		nivelPagina = 5;
		break;
	case "alimento":
	case "alimentos":
	case "almacen":
	case "almacenadoPorciones":
	case "cesta":
	case "cestaPorciones":
	case "confeccionarCestas":
		nivelPagina = 9;
		break;
	default :
		nivelPagina = 1;
	break;
}
//si no existe la sesion redirecciona al login con un mensaje de error
if (nivelAcceso == -1){
	response.sendRedirect("LoginController?opcion=4&error=1");
}
//si no tiene acceso a la pagina redirecciona al login con un mensaje de error
else if (nivelPagina > nivelAcceso){
	response.sendRedirect("LoginController?opcion=4&error=2");
}
%>