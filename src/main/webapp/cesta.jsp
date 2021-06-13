<%@page import="modelo.Cesta"%>
<%@page import="modelo.Porcion"%>
<%@page import="dao.CestaDAO"%>
<%@page import="java.sql.Date"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Detalle Cesta</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<%
		int option = 3;
		int id = 0;
		String nombre = "";
		Date fechaCreacion = null;
		String nombreUsuario = "";
		Date fechaCompra = null;
		double precio = 0;
		
		Cesta cesta = new Cesta();
		//actualizar la cesta
		if(request.getAttribute("cesta") != null){
			cesta = (Cesta)request.getAttribute("cesta");
			option = 4;
			
			id = cesta.getId();
			nombre = cesta.getNombre();
			fechaCreacion = cesta.getFechaCreacion();
			nombreUsuario = cesta.getUsuarioNombre();
			fechaCompra = cesta.getFechaCompra();
			precio = cesta.getPrecio();
		}
	%>

	<section>
		<h1>Detalle Cesta</h1>
		<form name="cesta" action="CestasController" method="post">
			<label for="nombre">Nombre:</label><input type="text" name="nombre" value="<%=nombre%>" id="nombre"><br>
			<% if(id != 0){ %>
				<label for="fechaCreacion">Fecha Creacion:</label><input type="text" name="fechaCreacion" value="<%=fechaCreacion%>" id="fechaCreacion" DISABLED><br>
				<label for="nombreUsuario">Usuario:</label><input type="text" name="nombreUsuario" value="<%=nombreUsuario%>" id="nombreUsuario" DISABLED><br>
				<label for="fechaCompra">Fecha Compra:</label><input type="text" name="fechaCompra" value="<%=fechaCompra%>" id="fechaCompra" DISABLED><br>
				<label for="precio">Precio:</label><input type="text" name="precio" value="<%=precio%>" id="nombre" DISABLED><br>
			<% } %>
			<input type="hidden" name="opcion" value="<%=option%>">
			<input type="hidden" name="id" value="<%=id%>">
			<div class="centeredContainer">
				<input type="submit" name="guardar" value="Guardar" onclick="return validarDatos();">
			</div>
		</form>
		
		<%
		if(id != 0){
			ArrayList<Porcion> porciones = cesta.obtenerPorciones();
			%> <h2>Porciones</h2> <%
			if (porciones != null){ %>
				<table>
					<tr>
						<th>ALIMENTO</th>
						<th>CANTIDAD</th>
						<th>PRECIO</th>
						<th>PRODUCTOR</th>
						<th>FECHA</th>
						<th></th>
					</tr>
				<%
				for (Porcion p : porciones) { %>
					<tr>
						<td><%=p.getNombreAlimento()%></td>
						<td><%=p.getCantidad()%></td>
						<td><%=p.getPrecio()%></td>
						<td><%=p.getNombreProductor()%></td>
						<td><%=p.getFechaAceptacionPaquete()%></td>
						<td>
							<% if ("".equals(nombreUsuario)){ %>
								<a href="CestasController?opcion=2&idCesta=<%=id%>&idPorcion=<%=p.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
							<% } %>
						</td>
					</tr>
				<% } %>
				</table><%
			} %>
			<div class="centeredContainer">
				<button class="button" onclick="document.location='CestasController?opcion=11&id=<%=id%>'">Nueva Porción</button>
			</div>
		<%}
		%>
		
		<%@include file="/includes/msg.inc.jsp"%>
		</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>