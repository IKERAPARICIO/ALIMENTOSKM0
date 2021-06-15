<%@page import="modelo.Terreno"%>
<%@page import="dao.TerrenoDAO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Lista Terrenos</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Lista Terrenos</h1>
		<%
			if (request.getAttribute("terrenos") != null){
			%>
			<table>
				<tr>
					<th>TERRENO</th>
					<th>PRODUCTOR</th>
					<th>CIUDAD</th>
					<th>METROS</th>
					<th></th>
				</tr>
			<%
				//muestra los terrenos pasados
				ArrayList<Terreno> terrenos = (ArrayList<Terreno>)request.getAttribute("terrenos");
				for (Terreno t : terrenos) {
				%>
				<tr>
					<td><%=t.getNombre()%></td>
					<td><%=t.getNombreCompletoProductor()%></td>
					<td><%=t.getCiudad()%></td>
					<td><%=t.getMetros()%></td>
					<td>
						<% if(nivelAcceso > 8){ %>
							<a href="TerrenosController?opcion=2&id=<%=t.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
							<a href="TerrenosController?opcion=4&id=<%=t.getId()%>"><img src="img/edit.png" width="16px" alt="Editar"></a>
						<% } else {%>
							<a href="TerrenosController?opcion=4&id=<%=t.getId()%>"><img src="img/view.png" width="16px" alt="Ver Detalle"></a>
						<% } %>
					</td>
				</tr>
				<%
				}
			%>
			</table>
		<% } else{ %>
			<p>No hay terrenos a mostrar. </p>
		<%	} if(nivelAcceso > 8){ %>
			<div class="centeredContainer">
				<button class="button" onclick="document.location='TerrenosController?opcion=4&id=0'">Nuevo Terreno</button>
			</div>
		<% } %>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>