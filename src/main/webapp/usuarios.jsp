<%@page import="modelo.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Listado de Usuarios</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
<div id="contenedor">
	<%@include file="/includes/header.inc.jsp"%>
	<%@include file="/includes/nav.inc.jsp"%>
	<section>
		<h1>Usuarios</h1>

		<table>
			<tr>
				<th>NOMBRE</th>
				<th>TIPO</th>
				<th>CIUDAD</th>
				<th>TELEFONO</th>
				<th>EMAIL</th>
				<th></th>
			</tr>
			<%
			String rol = "";
			String getRol = request.getParameter("rol");
			if (getRol != null){
				rol = getRol;
			}
			
			UsuarioDAO uDAO = new UsuarioDAO();
			ArrayList<Usuario> usuarios = uDAO.listUsuarios(rol);
			if (usuarios != null){
				for (Usuario u : usuarios) {
				%>
				<tr>
					<td><%=u.getNombreCompleto()%></td>
					<td><%=u.getRolName()%></td>
					<td><%=u.getCiudad()%></td>
					<td><%=u.getTelefono()%></td>
					<td><%=u.getMail()%></td>
					<td>
						<% if(isGestor){ %>
							<a href="UsuariosController?opcion=2&id=<%=u.getId()%>"><img src="img/delete.png" width="16px" alt="Eliminar"></a>
							<a href="usuario.jsp?id=<%=u.getId()%>"><img src="img/edit.png" alt="Editar" width="16px"></a>
						<% } %>
					</td>
				</tr>
				<%
				}
			}
			%>
		</table>
		<% if(isGestor){ %>
			<div class="centeredContainer">
				<button class="button" onclick="document.location='usuario.jsp'">Nuevo Usuario</button>
			</div>
		<% } %>
		<%@include file="/includes/msg.inc.jsp"%>
	</section>
	<%@include file="/includes/footer.inc.jsp"%>
</div>
</body>
</html>