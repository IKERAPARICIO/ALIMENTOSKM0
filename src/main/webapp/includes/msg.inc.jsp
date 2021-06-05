<%
	String msg = (String)request.getAttribute("mensaje");
	if(msg != null){ 
	%>
		<div id="msg"><%=msg%></div>
	<%
	}
%>