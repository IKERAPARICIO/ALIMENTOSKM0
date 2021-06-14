<%@page import="modelo.Paquete"%>
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
<title>Almacenado - Porciones</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script>
function isPositiveInteger(x){
	result = false;
	if(isNumber(x)){
		if (Number.isInteger(x) && x > 0) {
			result = true;
		}
	}
	return result;
}

function isPositiveNumber(x){
	if(isNumber(x) && x > 0){
		return true;
	}
	else{
		return false;
	}
}

function isPositiveFloat(x){
	result = false;
	if(isNumber(x)){
		if (!Number.isInteger(x) && x > 0) {
			result = true;
		}
	}
	return result;
}

function isNumber(x){
	if(typeof x == 'number' && !isNaN(x)){
		return true;
	}
	else{
		return false;
	}
}

function validateForm() {
	result = true;
	cantNew = parseFloat(document.getElementById("cantNew").value);
	numNew  = parseInt(document.getElementById("numNew").value);
	cantDisp = parseFloat(document.getElementById("cantDisp").value);

	introCant = document.getElementById("cantNew").value;
	introNum = document.getElementById("numNew").value;

	//validar formatos
	if (numNew != introNum) {
		document.getElementById("numNew").value = numNew;
		alert("Número incorrecto, debe ser un entero:  " + introNum);
		result = false;
	}
	else if (!isPositiveInteger(numNew) || numNew != introNum) {
		alert("Número incorrecto, debe ser un entero positivo:  " + introNum);
		result = false;
    }
	else if (cantNew != introCant) {
		alert("Número incorrecto, debe ser numérico:  " + introCant);
		result = false;
	}
	else if (!isPositiveNumber(cantNew)){
		alert("Cantidad incorrecta, debe ser positivo:  " + cantNew);
		result = false;
	}
	else{
		//validar total disponible
		cantNecesaria = cantNew * numNew;
		if (cantDisp < cantNecesaria){
			alert(cantNecesaria + " es mayor a lo disponible " + cantDisp);
			result = false;
		}
	}

	return result;
}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Paquete</h1>
		<%
		Paquete paquete = (Paquete)request.getAttribute("paquete");
		if (paquete.getId() != 0){
			ArrayList<Porcion> porciones = paquete.obtenerPorciones();
			%>
			<form>
				<label for="alimento" class="XXL">Producto:</label><input type="text" name="alimento" value="<%=paquete.getAlimento().getNombre()%>" id="alimento" DISABLED><br>
				<label for="productor" class="XXL">Productor:</label><input type="text" name="productor" value="<%=paquete.getTerreno().getProductor().getNombre()%>" id="productor" DISABLED><br>
				<label for="cantIni" class="XXL">Cantidad Inicial:</label><input type="text" name="cantIni" value="<%=paquete.getCantidadAceptada()%>" id="cantIni" DISABLED><br>
				<label for="cantDisp" class="XXL">Cantidad Disponible:</label><input type="text" name="cantDisp" value="<%=paquete.getCantidadDisponible()%>" id="cantDisp" DISABLED><br>
			</form>	
			<h1>Porciones</h1>
			<table>
				<tr>
					<th>CANTIDAD</th>
					<th>INCLUIDA EN CESTA</th>
					<th></th>
				</tr>
				<%	
				if (porciones != null){
					for (Porcion p : porciones) {
					%>
					<tr>
						<td><%=p.getCantidad()%></td>
						<td><%=p.incluidaEnCesta()%></td>
						<td>
							<% if (p.incluidaEnCesta()){ %>
								<a href="CestasController?opcion=5&id=<%=p.getCestaId()%>"><img src="img/basket.png" width="16px" alt="Ver Cesta"></a>
							<% } else{ %>
								<a href="PaquetesController?opcion=6&id=<%=p.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
							<% } %>
						</td>
					</tr>
					<%
					}
				}
				%>
			</table>
			<div class="centeredContainer">
				<form name="paquete" action="PaquetesController" method="post">
					Cantidad:<input type="text" name="cantNew" value="0" id="cantNew" required>
					Número Porciones:<input type="text" name="numNew" value="0" id="numNew" required>
					
					<input type="hidden" name="opcion" value="5">
					<input type="hidden" name="id" value="<%=paquete.getId()%>">
					<input type="hidden" name="cantDisp" value="<%=paquete.getCantidadDisponible()%>">
					
					<input type="submit" name="crear" value="Crear Porciones" onclick="return validateForm()">
				</form>
			</div>
		<% } else {
			msgJsp = "No existe el paquete.";
		} %>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>