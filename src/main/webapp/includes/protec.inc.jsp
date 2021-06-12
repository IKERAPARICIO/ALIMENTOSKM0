<%
//definicion de permisos por pagina jsp dependiendo del nivel de acceso
String nombrePagina = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1,request.getRequestURI().lastIndexOf("."));

int nivelPagina = 0;
switch(nombrePagina){
	case "cestas":
	case "usuarios":
		nivelPagina = 1;
		break;
	case "usuario":
		nivelPagina = 3;
		break;
	case "terreno":
	case "terrenos":
		nivelPagina = 5;
		break;
	case "alimento":
	case "alimentos":
	case "almacenadoPorciones":
	case "cesta":
	case "cestaPorciones":
	case "confeccionarCestas":
	case "propuestas":
		nivelPagina = 9;
		break;
	default :
		nivelPagina = 1;
	break;
}

if (nivelPagina > nivelAcceso){
	msgJsp = "No tiene acceso para ver la página indicada!";
	//response.sendRedirect("index.jsp");
	response.sendRedirect("index.jsp");
}
%>