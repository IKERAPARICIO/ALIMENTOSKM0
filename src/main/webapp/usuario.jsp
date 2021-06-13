<%@page import="modelo.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page import="modelo.Productor"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Perfil de Usuario</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script>
window.onload = function() {
	showHideAttributes()
};
	
function showHideAttributes(){
	var sRol = document.getElementById("sRol");
	var attrProductor = document.getElementById("attrProductor");
	attrProductor.style.display = sRol.value == "PRODUCTOR" ? "block" : "none";
}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<%
		int id = 0; 
		int option = 1;
		String nombre = "";
		String apellidos = "";
		String nick = "";
		String pass = "";
		String sRol = "";
		String mail = "";
		String ciudad = "";
		String telefono = "";
		String dni = "";
		String direccion = "";
		
		UsuarioDAO uDAO = new UsuarioDAO();
		ArrayList<String> roles = uDAO.getRols();

		//actualizar el usuario
		if (request.getAttribute("id") != null){
			id = (int)request.getAttribute("id");
			option = 3;
			Usuario usuario = new Usuario();
			String userRol = usuario.obtenerRol(id);
			
			if (userRol.equals("PRODUCTOR")){
				Productor productor = new Productor();
				productor.buscarID(id);
				nombre = productor.getNombre();
				apellidos = productor.getApellidos();
				nick = productor.getNick();
				pass = productor.getPassword();
				sRol = productor.getRolName();
				mail = productor.getMail();
				ciudad = productor.getCiudad();
				telefono = productor.getTelefono();
				dni = productor.getDni();
				direccion = productor.getDireccion();
			}
			else{
				usuario.buscarID(id);
				nombre = usuario.getNombre();
				apellidos = usuario.getApellidos();
				nick = usuario.getNick();
				pass = usuario.getPassword();
				sRol = usuario.getRolName();
				mail = usuario.getMail();
				ciudad = usuario.getCiudad();
				telefono = usuario.getTelefono();
			}
		}
	%>

	<section>
		<h1>Usuario</h1>
		<form name="usuario" action="UsuariosController" method="post">
			<label for="nombre">Nombre:</label><input type="text" name="nombre" value="<%=nombre%>" id="nombre"><br>
			<label for="apellidos">apellidos:</label><input type="text" name="apellidos" value="<%=apellidos%>" id="apellidos"><br>
			<label for="nick">nick:</label><input type="text" name="nick" value="<%=nick%>" id="nick"><br>
			<label for="password">pass:</label><input type="text" name="password" value="<%=pass%>" id="password"><br>
			<% if(nivelAcceso > 8){ %>
				<label for="rol">Rol:</label>
				<select id="sRol" name="sRol" onload="showHideAttributes()" onchange="showHideAttributes(this)">
					<% for(String rol : roles){ %>
						<option value="<%=rol%>" <%if(rol.equals(sRol)){%> selected <%} %>><%=rol%></option>
				  	<% } %>
				</select><br>
			<% } %>
			<label for="mail">mail:</label><input type="text" name="mail" value="<%=mail%>" id="mail"><br>
			<label for="ciudad">ciudad:</label><input type="text" name="ciudad" value="<%=ciudad%>" id="ciudad"><br>
			<label for="telefono">telefono:</label><input type="text" name="telefono" value="<%=telefono%>" id="telefono"><br>
			
			<!-- solo si es produtor -->
			<div id="attrProductor" style="display: none">
				<label for="dni">DNI:</label><input type="text" name="dni" value="<%=dni%>" id="dni"><br>
				<label for="direccion">Direccion:</label><input type="text" name="direccion" value="<%=direccion%>" id="direccion"><br>
			</div>
			
			<input type="hidden" name="opcion" value="<%=option%>">
			<input type="hidden" name="id" value="<%=id%>">
			<div class="centeredContainer">
				<input type="submit" name="guardar" value="Guardar" onclick="return validarDatos();">
			</div>
		</form>

		<%@include file="/includes/msg.inc.jsp"%>
		</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>