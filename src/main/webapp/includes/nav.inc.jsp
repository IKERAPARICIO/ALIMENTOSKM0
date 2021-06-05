<%
switch(userrol){
     case "gestor":
      %><%@include file="/includes/nav_gestor.inc.jsp"%><%
     	break; 
     case "produc":
     	%><%@include file="/includes/nav_productor.inc.jsp"%><%
     	break; 
     case "consum":
     	%><%@include file="/includes/nav_consumidor.inc.jsp"%><%
     	break;
     default :
    	%><%@include file="/includes/nav_invitado.inc.jsp"%><%
     	break; 
 }
 %>