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
<script>
function validarDatos(){
	result = true;
	metros = parseFloat(document.getElementById("metros").value);
	if (metros <= 0){
		alert("Cantidad incorrecta, debe ser mayor que 0.");
		result = false;
	}

	return result;
}

//al cargar la pagina deshabilita las opciones de edicion segun el nivel de acceso del usuario 
window.onload=function() {
	permisoEdicion = document.getElementById("permisoEdicion").value;
	if (permisoEdicion == "false"){
	  	document.getElementById("nombre").disabled = true;
	  	document.getElementById("idUsuario").disabled=true;
	  	document.getElementById("metros").disabled=true;
	  	document.getElementById("ciudad").disabled=true;
	  	document.getElementById("direccion").disabled=true;
	  	document.getElementById("nuevo").style.display = "none";
	  	document.getElementById("guardar").style.display = "none";
	}
}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<%
	boolean permisoEdicion = (nivelAcceso > 8) ? true : false;
	
	int option = 1;
	String nombre = "";
	String productor = "";
	int idProductor = 0;
	Double metros = 0.0;
	String ciudad = "";
	String direccion = "";
	
	//si se ha pasado un terreno, carga los valores para poder actualizarlo
	int id = 0;
	Terreno terreno = new Terreno();
	if(request.getAttribute("terreno") != null){
		terreno = (Terreno)request.getAttribute("terreno");
		option = 3;
		id = terreno.getId();
		nombre = terreno.getNombre();
		productor = terreno.getNombreCompletoProductor();
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
			<label for="nombre">Nombre:</label><input type="text" name="nombre" value="<%=nombre%>" id="nombre" required><br>
			<label for="idUsuario">Productor:</label>
			<select name="idUsuario" id="idUsuario" required>
				<option value="">--sin productor asignado--</option>
				<% for(Usuario u : productores){ %><option value="<%=u.getId()%>" <%if(idProductor == u.getId()){%> selected <%} %>><%=u.getNombreCompleto()%></option>
			  	<% } %>
			</select><br>
			<label for="metros">Metros:</label><input type="number" step="0.05" name="metros" value="<%=metros%>" id="metros" required><br>
			<label for="ciudad">Ciudad:</label><input type="text" name="ciudad" value="<%=ciudad%>" id="ciudad" required><br>
			<label for="direccion">Direcci?n:</label><input type="text" name="direccion" value="<%=direccion%>" id="direccion" required><br>
	
			<input type="hidden" name="opcion" value="<%=option%>">
			<input type="hidden" name="id" value="<%=id%>">
			<input type="hidden" name="idUsuario" value="<%=idProductor%>">
			<input type="hidden" id="permisoEdicion" value="<%=permisoEdicion%>">
			<div class="centeredContainer">
				<input class="button" type="submit" name="guardar" id="guardar" value="Guardar" onclick="return validarDatos();">
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
				//muestar los alimentos del terreno
				for (Alimento a : alimentos) { %>
					<tr>
						<td><%=a.getNombre()%></td>
						<td><%=a.getMedida()%></td>
						<td>
							<% if(permisoEdicion){ %>
								<a href="TerrenosController?opcion=5&idTerreno=<%=id%>&idAlimento=<%=a.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
							<% } %>
						</td>
					</tr>
				<% } %>
				</table><%
			} else { %>
				<p>No hay alimentos incluidos. </p>
			<%}%>
			<div class="centeredContainer">
				<button class="button" id="nuevo" onclick="document.location='TerrenosController?opcion=7&id=<%=id%>'">Nuevo Alimento</button>
			</div>
		<%}%>
		
		<%@include file="/includes/msg.inc.jsp"%>
		</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>