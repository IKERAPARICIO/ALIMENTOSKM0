<%
//String msgJsp = null;
HttpSession sesion = request.getSession();
String srol = (String)sesion.getAttribute("srol");
if (srol==null){ // si el usuario no existe, vamos a login
	response.sendRedirect("index.jsp");
}
else{
	switch(srol){
	     case "GESTOR":
	      %><%@include file="/includes/nav_gestor.inc.jsp"%><%
	     	break; 
	     case "PRODUCTOR":
	     	%><%@include file="/includes/nav_productor.inc.jsp"%><%
	     	break; 
	     case "CONSUMIDOR":
	     	%><%@include file="/includes/nav_consumidor.inc.jsp"%><%
	     	break;
	     default :
	    	%><%@include file="/includes/nav_invitado.inc.jsp"%><%
	     	break; 
	 }
}
 %>