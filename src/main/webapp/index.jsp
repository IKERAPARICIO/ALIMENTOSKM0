<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

<link href="css/estilos.css" rel="stylesheet" type="text/css" />
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body class="loginBack">
<%@include file="/includes/header.inc.jsp"%>
<%@include file="/includes/msg.inc.jsp"%>
<div class="wrapper-login">
    <form class="form-signin" action="LoginController" method="post">       
      	<h2 class="form-signin-heading">Datos de Acceso</h2>
      	<input type="text" class="form-control" name="nick" placeholder="Usuario" required="" autofocus="" />
      	<input type="password" class="form-control" name="pass" placeholder="Password" required=""/>      
    
      	<button class="button buttonLogin" type="submit">Login</button>
      	<br>
      	<button class="button buttonLogin" onclick="document.location='alimentos.jsp'">Acceso como Invitado</button>
      	<br>
      	<a href="registro.jsp"><em>Registro Consumidor</em></a>
		<input type="hidden" name="opcion" value="1">
    </form>
 </div>

<%@include file="/includes/footer.inc.jsp"%>
</body>
</html>