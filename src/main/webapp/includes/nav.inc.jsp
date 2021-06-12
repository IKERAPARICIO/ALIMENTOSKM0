<%
switch(nivelAcceso){
     case 9:
      	%><%@include file="/includes/nav_gestor.inc.jsp"%><%
     	break; 
     case 5:
     	%><%@include file="/includes/nav_productor.inc.jsp"%><%
     	break; 
     case 3:
     	%><%@include file="/includes/nav_consumidor.inc.jsp"%><%
     	break;
     default :
    	%><%@include file="/includes/nav_invitado.inc.jsp"%><%
     	break; 
 }
 %>