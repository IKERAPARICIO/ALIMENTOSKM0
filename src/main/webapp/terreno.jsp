<%@page import="modelo.Terreno"%>
<%@page import="dao.TerrenoDAO"%>
<%@page import="dao.UsuarioDAO"%>
<%@page import="modelo.Alimento"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Rol"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Detalle Terreno</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<%
		int option = 1;
		String nombre = "";
		String productor = "";
		int idProductor = 0;
		Double metros = 0.0;
		String ciudad = "";
		String direccion = "";
		
		int id = 0;
		Terreno terreno = new Terreno();
		//actualizar el terreno
		if(request.getAttribute("terreno") != null){
			terreno = (Terreno)request.getAttribute("terreno");
			option = 3;
			id = terreno.getId();
			nombre = terreno.getNombre();
			productor = terreno.getNombreProductor();
			idProductor = terreno.getProductorId();
			metros = terreno.getMetros();
			ciudad = terreno.getCiudad();
			direccion = terreno.getDireccion();
		}
		
		//carga los productores
		UsuarioDAO uDAO = new UsuarioDAO();
		ArrayList<Usuario> productores = uDAO.listUsuarios(Rol.PRODUCTOR.toString());
		int tam = productores.size();
	%>
	<section>
		<h1>Detalle Terreno</h1>
		<form name="terreno" action="TerrenosController" method="post">
			<label for="nombre">Nombre:</label><input type="text" name="nombre" value="<%=nombre%>" id="nombre"><br>
			<label for="idUsuario">Productor:</label>
			<select name=idUsuario>
				<option value="">--sin productor asignado--</option>
				<% for(Usuario u : productores){ %>
					<option value="<%=u.getId()%>" <%if(idProductor == u.getId()){%> selected <%} %>><%=u.getNombreCompleto()%></option>
			  	<% } %>
			</select><br>
			<label for="metros">Metros:</label><input type="text" name="metros" value="<%=metros%>" id="metros"><br>
			<label for="ciudad">Ciudad:</label><input type="text" name="ciudad" value="<%=ciudad%>" id="ciudad"><br>
			<label for="direccion">Dirección:</label><input type="text" name="direccion" value="<%=direccion%>" id="direccion"><br>
	
			<input type="hidden" name="opcion" value="<%=option%>">
			<input type="hidden" name="id" value="<%=id%>">
			<input type="hidden" name="idUsuario" value="<%=idProductor%>">
			<div class="centeredContainer">
				<input type="submit" name="guardar" value="Guardar" onclick="return validarDatos();">
			</div>
		</form>
		
		<%
		if(id != 0){
			ArrayList<Alimento> alimentos = terreno.obtenerAlimentos();
			%> <h2>Alimentos</h2> <%
			if (alimentos != null){ %>
				<table>
					<tr>
						<th>ALIMENTO</th>
						<th>MEDIDA</th>
						<th></th>
					</tr>
				<%
				for (Alimento a : alimentos) { %>
					<tr>
						<td><%=a.getNombre()%></td>
						<td><%=a.getMedida()%></td>
						<td>
							<a href="TerrenosController?opcion=5&idTerreno=<%=id%>&idAlimento=<%=a.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
						</td>
					</tr>
				<% } %>
				</table><%
			} %>
			<div class="centeredContainer">
				<button class="button" onclick="document.location='TerrenosController?opcion=7&id=<%=id%>'">Nuevo Alimento</button>
			</div>
		<%}
		%>
		
		<%@include file="/includes/msg.inc.jsp"%>
		</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>