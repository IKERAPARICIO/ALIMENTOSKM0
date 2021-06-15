<%@page import="modelo.Alimento"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script>
function validarDatos(){
	result = true;
	precio = parseFloat(document.getElementById("precio").value);

	if (precio <= 0){
		alert("Precio incorrecto, debe ser mayor que 0.");
		result = false;
	}
	return result;
}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<%
		int id = 0;
		int option = 1;
		String nombre = "";
		String medida = "";
		double precio = 0;

		//si se ha pasado un alimento, carga los valores para poder actualizarlo
		if(request.getAttribute("alimento") != null){
			Alimento alimento = (Alimento)request.getAttribute("alimento");
			option = 3;

			id = alimento.getId();
			nombre = alimento.getNombre();
			medida = alimento.getMedida();
			precio = alimento.getPrecio();
		}
	%>

	<section>
		<h1>Alimento</h1>
		<form name="alimento" action="AlimentosController" method="post">
			<label for="nombre">Nombre:</label><input type="text" name="nombre" value="<%=nombre%>" id="nombre" required><br>
			<label for="medida">Medida:</label>
			<select id="medida" name="medida" required>
				<option value="KG" <%= ("KG".equals(medida)) ? "selected" : "" %>>KG</option>
			  	<option value="UNIDAD" <%= ("UNIDAD".equals(medida)) ? "selected" : "" %>>UNIDAD</option>
			</select><br>
			<label for="medida">Precio:</label><input type="number" step="0.05" name="precio" value="<%=precio%>"  id="precio"><br>
			
			<input type="hidden" name="opcion" value="<%=option%>">
			<input type="hidden" name="id" value="<%=id%>">
			<div class="centeredContainer">
				<input type="submit" class="button" name="guardar" value="Guardar" onclick="return validarDatos();">
			</div>
		</form>
		<%
		//muestra el historico del precio del alimento
		if(id != 0){
			Alimento alimento = new Alimento();
			Map<String,String> historico = alimento.getHistoricoPrecios(id);
			if(historico != null){ %>
			<h2>Histórico de Precios</h2>
			<table>
				<tr>
					<th>FECHA</th>
					<th>PRECIO</th>
				</tr>
				<%
				for (Map.Entry<String, String> entry : historico.entrySet()) {
				    %>
				    <tr>
						<td><%=entry.getKey()%></td>
						<td><%=entry.getValue()%></td>
					</tr>
				    <%
				}
			%></table><%
			}
		}
		%>
		<%@include file="/includes/msg.inc.jsp"%>
		</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>