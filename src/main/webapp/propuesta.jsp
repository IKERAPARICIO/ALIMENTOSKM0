<%@page import="modelo.Paquete"%>
<%@page import="modelo.Terreno"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Propuesta</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script type="text/javascript">
	// Uso de AJAX para cargar el listado de alimentos por terreno cada vez que se elige uno
    // Variable que almacena la referencia del objeto XMLHttpRequest
    var xhr;

    function cargarAlimentos() {
        var idTerreno = document.getElementById("terreno").value;
    	// Para Explorer 6 y anteriores
        if (window.ActiveXObject)
        	xhr = new ActiveXObject("Microsoft.XMLHttp");
        // Resto de navegadores
        else if ((window.XMLHttpRequest) || (typeof XMLHttpRequest) != undefined)
            xhr = new XMLHttpRequest();
        else {
            alert("Su navegador no soporta AJAX");
            return;
        }
        // Si ya tenemos el objeto enviamos la peticion asincrona
        enviarPeticion(idTerreno);
    }

     function enviarPeticion(idTerreno) {
    	 var url = "TerrenosController";  
    	 myRand = parseInt(Math.random()*999999999999999);
    	 modurl = url+"?rand="+myRand+"&opcion=9&id="+idTerreno;

         // preparar la peticion
         xhr.open("GET", modurl, true);
         // Informar de la funcion que procesara el resultado
         xhr.onreadystatechange = procesarRespuesta;
         // Enviar la peticion
         xhr.send();
     }

     function procesarRespuesta() {
         // Condicionamos a que solo se ejecute al recibir la respuesta completa
         if (xhr.readyState == 4) {
             document.getElementById("productosSelect").innerHTML = xhr.responseText;
         }
     }
 </script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<%
		int id = 0; 
		int option = 11;
		Double cantidadPropuesta = 0.0;
		Date fechaPropuesta = null;
		String sEstado = "";
		String nombreTerreno = "";
		String nombreAlimento = "";
		
		//carga los terrenos del usuario, solo para crearlos nuevos
		ArrayList<Terreno> terrenos = new ArrayList<Terreno>();
		if(request.getAttribute("terrenos") != null){
			terrenos = (ArrayList<Terreno>)request.getAttribute("terrenos");
		}
		
		//actualizar la propuesta
		if(request.getAttribute("propuesta") != null){
			Paquete paquete = (Paquete)request.getAttribute("propuesta");
			option = 13;

			id = paquete.getId();
			cantidadPropuesta = paquete.getCantidadPropuesta();
			fechaPropuesta = paquete.getFechaPorpuesta();
			sEstado = paquete.getEstado().toString();
			nombreTerreno = paquete.getNombreTerreno();
			nombreAlimento = paquete.getNombreAlimento();
		}
	%>

	<section>
		<h1>Propuesta</h1>
		<form name="propuesta" action="PaquetesController" method="post">
			<label for="terreno">Terreno:</label>
			<% if (option == 11){ %>
				<select id="terreno" name="terreno" onchange="cargarAlimentos()">
					<option value="0">---seleccione un Terreno---</option>
					<% for(Terreno t : terrenos){ %>
						<option value="<%=t.getId()%>"><%=t.getNombre()%></option>	
			  		<% } %>
			  	</select><br>
			  <%} else{ %>
			  	<input type="text" name="terreno" value="<%=nombreTerreno%>" id="terreno" DISABLED><br>
			<% } %>
			<label for="producto">Producto:</label>
			<% if (option == 11){ %>
				<!-- Uso de AJAX para cargar el producto en funcion del Terreno seleccionado -->
				<div id="productosSelect">
				</div><br>
			<% } else{ %>
				<input type="text" name="producto" value="<%=nombreAlimento%>" id="producto" DISABLED><br>
			<% } %>
			<label for="cantidadPropuesta">Cantidad Propuesta:</label><input type="text" name="cantidadPropuesta" value="<%=cantidadPropuesta%>" id="cantidadPropuesta"><br>
	
			<input type="hidden" name="opcion" value="<%=option%>">
			<input type="hidden" name="id" value="<%=id%>">
			<div class="centeredContainer">
				<input type="submit" name="guardar" value="Guardar" onclick="return validarDatos();">
			</div>
		</form>

		<%@include file="/includes/msg.inc.jsp"%>
		</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>