<%@page import="modelo.Paquete"%>
<%@page import="modelo.Estado"%>
<%@page import="dao.PaqueteDAO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Almacen</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script>
	function doReload(fEstado){
		document.location = 'almacen.jsp?fEstado=' + fEstado;
	}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Almacén</h1>
		<%
		String estado = "";
		if (request.getAttribute("fEstado") != null) {
			estado = (String)request.getAttribute("fEstado");
		}
		
		PaqueteDAO pDAO = new PaqueteDAO();
		ArrayList<String> estados = pDAO.getAlmacenStates();
		%>
		<label for="stateFilter">Estado:</label>
		<select name="stateFilter" onchange="doReload(this.value);">
			<option value="">TODOS</option>
			<% for(String est : estados){ %>
				<option value="<%=est%>" <%if(est.equals(estado)){%> selected <%} %>><%=est%></option>
		  	<% } %>
		</select>
		<p>
		<table>
			<tr>
				<th>PRODUCTO</th>
				<th>PRODUCTOR</th>
				<th>DISPONIBLE</th>
				<th>TERRENO</th>
				<th>PRECIO</th>
				<th>ESTADO</th>
				<th></th>
			</tr>
			<%	
			if (request.getAttribute("almacen") != null) {
				ArrayList<Paquete> almacen = (ArrayList<Paquete>)request.getAttribute("almacen");
				for (Paquete p : almacen) {
				%>
				<tr>
					<td><%=p.getNombreAlimento()%></td>
					<td><%=p.getNombreProductor()%></td>
					<td><%=p.getCantidadDisponible().toString()%></td>
					<td><%=p.getNombreTerreno()%></td>
					<td><%=p.getPrecio()%></td>
					<td><%=p.getEstado().toString()%></td>
					<td>
						<a href="PaquetesController?opcion=3&id=<%=p.getId()%>"><img src="img/delete.png" width="16px" alt="Anular"></a>
						<% if (p.estaGestionado()){ %>
							<a href="PaquetesController?opcion=4&id=<%=p.getId()%>"><img src="img/multiply.png" width="16px" alt="Porciones"></a>
						<% } %>
					</td>
				</tr>
				<%
				}
			}
			%>
		</table>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>