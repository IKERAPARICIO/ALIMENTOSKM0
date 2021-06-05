<%@page import="modelo.Alimento"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Listado de Alimentos</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Alimentos</h1>

		<table>
			<tr>
				<th>ALIMENTO</th>
				<th>MEDIDA</th>
				<th>PRECIO ACTUAL</th>
				<th></th>
			</tr>
			<%
			Alimento alimento = new Alimento();
			if (alimento.obtenerAlimentos() != null){
				for (Alimento a : alimento.obtenerAlimentos()) {
				%>
				<tr>
					<td><%=a.getNombre()%></td>
					<td><%=a.getMedida()%></td>
					<td><%=a.getPrecio()%></td>
					<td>
						<a href="AlimentosController?opcion=2&id=<%=a.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
						<a href="alimento.jsp?id=<%=a.getId()%>"><img src="img/edit.png" alt="Editar" width="16px"></a>
					</td>
				</tr>
				<%
				}
			}
			%>
		</table>
		
		<div class="centeredContainer">
			<button class="button" onclick="document.location='alimento.jsp'">Nuevo Alimento</button>
		</div>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>