<%@page import="modelo.Cesta"%>
<%@page import="dao.CestaDAO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Confeccionar Cestas</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Confeccionar Cestas</h1>

		<table>
			<tr>
				<th>ID</th>
				<th>CESTA</th>
				<th>FECHA</th>
				<th>CONSUMIDOR</th>
				<th>FECHA COMPRA</th>
				<th>PRECIO</th>
				<th></th>
			</tr>
			<%
			if (request.getAttribute("cestas") != null){
				ArrayList<Cesta> cestas = (ArrayList<Cesta>)request.getAttribute("cestas");
				for (Cesta c : cestas) {
				%>
				<tr>
					<td><%=c.getId()%></td>
					<td><%=c.getNombre()%></td>
					<td><%=c.getFechaCreacion()%></td>
					<td><%=c.getUsuarioNombreCompleto()%></td>
					<td><%=c.getFechaCompra()%></td>
					<td><%=c.getPrecio()%></td>
					<td>
						<% if (c.getUsuarioNombreCompleto() == "") {%>
							<a href="CestasController?opcion=1&id=<%=c.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
							<a href="CestasController?opcion=5&id=<%=c.getId()%>"><img src="img/edit.png" alt="Editar" width="16px"></a>
						<% } %>
					</td>
				</tr>
				<%
				}
			}
			%>
		</table>
		
		<div class="centeredContainer">
			<button class="button" onclick="document.location='CestasController?opcion=5&id=0'">Nueva Cesta</button>
		</div>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>