<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Registro</title>
</head>
<body class="loginBack">
<%@include file="/includes/header.inc.jsp"%>
<%@include file="/includes/msg.inc.jsp"%>
<div class="wrapper-login">
	<form class="form-signin" action="LoginController" method="post">
		<h2 class="form-signin-heading">Nuevo Consumidor</h2>
		
		<label for="nombre">Nombre:</label>
		<input class="form-control-register" type="text" name="nombre">
		<label for="nombre">Apellidos:</label>
		<input class="form-control-register" type="text" name="apellidos">
		<label for="nombre">Mail:</label>
		<input class="form-control-register" type="text" name="mail">
		<label for="nombre">Ciudad:</label>
		<input class="form-control-register" type="text" name="ciudad">
		<label for="nombre">Teléfono:</label>
		<input class="form-control-register" type="text" name="telefono">
		<label for="nombre">Nick:</label>
		<input class="form-control-register" type="text" name="nick">
		<label for="nombre">Password:</label>
		<input class="form-control-register" type="password" name="pass">
		<input type="hidden" name="opcion" value="2">
		<input type="submit" class="button" value="Registrar" />
		<br>
      	<a href="index.jsp"><em>Login</em></a>
	</form>
</div>
<%@include file="/includes/footer.inc.jsp"%>
</body>
</html>