<%@page import="modelo.Cesta"%>
<%@page import="modelo.Porcion"%>
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
<title>Listado de Cestas</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<script>
function confirmMsg(){
	return confirm('Con esta acción comprará la cesta indicada. Deberá mostrar el documento que lo acredita desde sus cestas a la hora de recogerla.')
}
</script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/protec.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<%
		ArrayList<Cesta> listaCestas = (ArrayList<Cesta>)request.getAttribute("listaCestas");
		Boolean miscestas = false;
		if (request.getAttribute("miscestas") != null) {
			miscestas = (boolean)request.getAttribute("miscestas");
		}
		if (miscestas){
			%><h1>Mis Cestas</h1><%
		}
		else{
			%><h1>Cestas Disponibles</h1><%
		}
		if (listaCestas != null){
			int cId = 0;
			Double cPrecio = 0.0;
			for (Cesta cesta : listaCestas) {
				cId = cesta.getId();
				cPrecio = cesta.getPrecio();
				%>
				<div id="accordion">
				  <div class="card">
				    <div class="card-header" id="heading<%=cId%>">
				      <h5 class="mb-0" >
				        <button class="btn btn-link collapsed" data-toggle="collapse" data-target="#collapse<%=cId%>" aria-expanded="false" aria-controls="collapse<%=cId%>">
				          	<%=cesta.getNombre()%><%if (miscestas) {%> (<%=cesta.getFechaCompra()%>)<%}%>
				          	
				        </button>
				        <div class="precio-icono">
				        <% if(nivelAcceso > 1 && !miscestas){ %>
					        	<a href="CestasController?opcion=7&id=<%=cesta.getId()%>" onclick="return confirmMsg()"><img src="img/basket.png" alt="Comprar" width="16px"></a>
					    <% } else if (miscestas){ %>
					        	<a href="CestasController?opcion=10&id=<%=cesta.getId()%>"><img src="img/invoice.png" alt="Justificante" width="24px"></a>
					    <% } %>
					    </div>
						<div class="precio">
				        	<%=cPrecio%>&nbsp;<img src="img/euro.png" alt="Comprar" width="16px">
				        </div>
				      </h5>
				    </div>

				    <div id="collapse<%=cId%>" class="collapse" aria-labelledby="heading<%=cId%>" data-parent="#accordion">
				      	<div class="card-body">
				      	<%  ArrayList<Porcion> porciones = cesta.obtenerPorciones();
							if (porciones != null){
					      	%>
				      		<table>
								<tr>
									<tr>
										<th>ALIMENTO</th>
										<th>CANTIDAD</th>
										<th>PRECIO/ALIMENTO</th>
										<th>TERRENO</th>
										<th>PRODUCTOR</th>
										<th>PRECIO TOTAL</th>
									</tr>
								</tr>
								<%for (Porcion p : porciones) {
									%>
									<tr>
										<td><%=p.getPaquete().getAlimento().getNombre()%></td>
										<td><%=p.getCantidad()%></td>
										<td><%=p.getPaquete().getAlimento().getPrecio()%></td>
										<td><%=p.getPaquete().getTerreno().getNombre()%></td>
										<td><%=p.getPaquete().getTerreno().getProductor().getNombre()%></td>
										<td><%=p.getPrecio()%></td>
									</tr>
									<%
									}
								}
								%>
							</table>
				      	</div>
				    </div>
				  </div>
				</div>
			<%
			}
		}
		%>	
		
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>