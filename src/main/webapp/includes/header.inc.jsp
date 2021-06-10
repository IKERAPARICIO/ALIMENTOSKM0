<%
String userrol=(String)session.getAttribute("userrol");
userrol = "gestor";
//userrol = "produc";
//userrol = "consum";
//userrol = "";

if (userrol == null){
	//response.sendRedirect("index.jsp");
}
String msgJsp = null;
%>
<header>
	<div class="cabizq">
		<div class="logo">
			<a href="index.jsp"><img src="img/cesta.png" alt="biblioteca" title="logo" ></a>
		</div>
	</div>
	
	<div class="cabcenter">
		Alimentos KM0
	</div>

	<div class=cabder>
		<div class="logo">
			<%
	      	switch(userrol){
	            case "gestor":
		            %><img src="img/gestor.png" alt="biblioteca" title="imgRol">
		            <%break; 
	            case "produc":
	            	%><img src="img/productor.png" alt="biblioteca" title="imgRol">
	            	<%break; 
	            case "consum":
	            	%><img src="img/consumidor.png" alt="biblioteca" title="imgRol">
	            	<%break; 
	            default :%><%
	            	break; 
	        }
	        %>
		</div>
	</div>
</header>