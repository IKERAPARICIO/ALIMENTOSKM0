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
<title>Listado de Propuestas</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script>
	function askForQuantity(idPaquete,cantidadPropuesta){
		var cantAceptada = prompt("Cantidad a aceptar: ", "");
		cantAceptada = parseInt(cantAceptada);
		if (cantAceptada < 1 || cantAceptada > cantidadPropuesta)
	   	{
	   		alert("Valor incorrecto!!");
	   	}
		else{
			approveQuantity(idPaquete,cantAceptada);
		}
	}

	function approveQuantity(idPaquete,cantAceptada){
		document.location.href="PaquetesController?opcion=1&id="+idPaquete+"&cant="+cantAceptada;
	}

	function doReload(fEstado){
		document.location = 'propuestas.jsp?fEstado=' + fEstado;
	}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Propuestas</h1>
		<%
		PaqueteDAO pDAO = new PaqueteDAO();
		String estado = "";
		if (request.getParameter("fEstado") != null) {
			estado = request.getParameter("fEstado");
		}
		
		ArrayList<String> estados = pDAO.getPropuestasStates();
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
				<th>PRODUCTOR</th>
				<th>PRODUCTO</th>
				<th>CANTIDAD</th>
				<th>TERRENO</th>
				<th>FECHA</th>
				<th>ESTADO</th>
				<th></th>
			</tr>
			<%			
			if (pDAO.listPropuestas(estado) != null){
				for (Paquete p : pDAO.listPropuestas(estado)) {
				%>
				<tr>
					<td><%=p.getTerreno().getProductor().getNombre()%></td>
					<td><%=p.getAlimento().getNombre()%></td>
					<td><%=p.getCantidadPropuesta().toString()%></td>
					<td><%=p.getTerreno().getNombre()%></td>
					<td><%=p.getFechaPorpuesta().toString()%></td>
					<td><%=p.getEstado().toString()%></td>
					<td>
						<% if (p.estaSinGestionar()){ %>
							<img src="img/approval.png" width="16px" alt="Aprobar" onclick="approveQuantity(<%=p.getId()%>,<%=p.getCantidadPropuesta()%>)">
							<img src="img/edit.png" width="16px" alt="Parcialmente" onclick="askForQuantity(<%=p.getId()%>,<%=p.getCantidadPropuesta()%>)">
							<a href="PaquetesController?opcion=2&id=<%=p.getId()%>"><img src="img/disapproval.png" width="16px" alt="Rechazar"></a>
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