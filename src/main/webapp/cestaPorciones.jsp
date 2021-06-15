<%@page import="dao.PaqueteDAO"%>
<%@page import="modelo.Porcion"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Porciones Disponibles</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Porciones Disponibles</h1>
		<% if (request.getAttribute("porciones") != null){ %>
			<table>
				<tr>
					<tr>
						<th>ALIMENTO</th>
						<th>CANTIDAD</th>
						<th>PRECIO/ALIMENTO</th>
						<th>TERRENO</th>
						<th>PRODUCTOR</th>
						<th>PRECIO TOTAL</th>
						<th></th>
					</tr>
				</tr>
			<%  ArrayList<Porcion> porciones = (ArrayList<Porcion>)request.getAttribute("porciones");
				int id = (int)request.getAttribute("id");
				for (Porcion p : porciones) {
				%>
				<tr>
					<td><%=p.getNombreAlimento()%></td>
					<td><%=p.getCantidad()%></td>
					<td><%=p.getPrecioAlimento()%></td>
					<td><%=p.getNombreTerreno()%></td>
					<td><%=p.getNombreProductor()%></td>
					<td><%=p.getPrecio()%></td>
					<td>
						<a href="CestasController?opcion=6&idCesta=<%=id%>&idPorcion=<%=p.getId()%>"><img src="img/add.png" width="16px" alt="Incluir"></a>
					</td>
				</tr>
				<% } %>
			</table>
		<% }else {%>
			<p>No hay porciones a mostrar. </p>
		<% } %>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>