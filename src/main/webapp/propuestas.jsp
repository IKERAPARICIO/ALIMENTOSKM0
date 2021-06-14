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
		if (cantAceptada === null) {
			return;
		}
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
		document.location = 'PaquetesController?opcion=8&fEstado=' + fEstado;
	}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Propuestas</h1>
		<%
		String estado = "";
		if (request.getAttribute("fEstado") != null) {
			estado = (String)request.getAttribute("fEstado");
		}
		
		PaqueteDAO pDAO = new PaqueteDAO();
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
				<th>PROPUESTO</th>
				<th>ACEPTADO</th>
				<th>MEDIDA</th>
				<th>TERRENO</th>
				<th>FECHA</th>
				<th>ESTADO</th>
				<th></th>
			</tr>
			<%
			if (request.getAttribute("propuestas") != null) {
				ArrayList<Paquete> propuestas = (ArrayList<Paquete>)request.getAttribute("propuestas");
				for (Paquete p : propuestas) {
				%>
				<tr>
					<td><%=p.getNombreCompletoProductor()%></td>
					<td><%=p.getNombreAlimento()%></td>
					<td><%=p.getCantidadPropuesta().toString()%></td>
					<td><%=p.getCantidadAceptada().toString()%></td>
					<td><%=p.getMedidaAlimento()%></td>
					<td><%=p.getNombreTerreno()%></td>
					<td><%=p.getFechaPropuesta().toString()%></td>
					<td><%=p.getEstado().toString()%></td>
					<td>
						<% if (nivelAcceso > 8 && p.estaSinGestionar()){ %>
							<img src="img/approval.png" width="16px" alt="Aprobar" onclick="approveQuantity(<%=p.getId()%>,<%=p.getCantidadPropuesta()%>)">
							<img src="img/edit.png" width="16px" alt="Parcialmente" onclick="askForQuantity(<%=p.getId()%>,<%=p.getCantidadPropuesta()%>)">
							<a href="PaquetesController?opcion=2&id=<%=p.getId()%>"><img src="img/disapproval.png" width="16px" alt="Rechazar"></a>
						<% }
						else if (nivelAcceso == 5){ 
							if (p.estaSinGestionar()){%>
								<a href="PaquetesController?opcion=12&id=<%=p.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
								<a href="PaquetesController?opcion=14&id=<%=p.getId()%>"><img src="img/edit.png" width="16px" alt="Editar"></a>
							<% } else if (p.estaAceptado()){%>
								<a href="PaquetesController?opcion=10&id=<%=p.getId()%>"><img src="img/invoice.png" alt="Justificante" width="24px"></a>
						<% } 
						}%>
					</td>
				</tr>
				<%
				}
			}
			%>
		</table>
		<% if (nivelAcceso == 5){ %>
			<div class="centeredContainer">
				<button class="button" onclick="document.location='PaquetesController?opcion=14&id=0'">Nueva Propuesta</button>
			</div>
		<% } %>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>