<%@page import="modelo.Alimento"%>
<%@page import="java.util.ArrayList"%>
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
	<%@include file="/includes/protec.inc.jsp"%>
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
			ArrayList<Alimento> alimentos = new ArrayList<Alimento>();
			int idTerreno = 0;
			//si no se pasa un terreno se cargan todos los alimentos
			//si se llama desde terrenos.jsp se puedan incluir alimentos al Terreno pasado
			if (request.getAttribute("alimentos") != null){
				alimentos = (ArrayList<Alimento>)request.getAttribute("alimentos");
				if (request.getAttribute("idTerreno") != null){
					idTerreno = (int)request.getAttribute("idTerreno");
				}

				if (alimentos != null){
					for (Alimento a : alimentos) {
					%>
					<tr>
						<td><%=a.getNombre()%></td>
						<td><%=a.getMedida()%></td>
						<td><%=a.getPrecio()%></td>
						<td>
							<% if (idTerreno != 0) { %>
								<a href="TerrenosController?opcion=6&idTerreno=<%=idTerreno%>&idAlimento=<%=a.getId()%>"><img src="img/add.png" width="16px" alt="Agregar"></a>
							<% } else { %>
								<a href="AlimentosController?opcion=2&id=<%=a.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
								<a href="AlimentosController?opcion=5&id=<%=a.getId()%>"><img src="img/edit.png" alt="Editar" width="16px"></a>
							<% } %>
						</td>
					</tr>
					<%
					}
				}
				%>
			</table>
			<% if (idTerreno == 0) { %>
				<div class="centeredContainer">
					<button class="button" onclick="document.location='AlimentosController?opcion=5&id=0'">Nuevo Alimento</button>
				</div>
			<% } 
			}%>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>