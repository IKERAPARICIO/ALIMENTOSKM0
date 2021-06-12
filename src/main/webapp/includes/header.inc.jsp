<%
HttpSession sesion = request.getSession();
int nivelAcceso = 0;
String msgJsp = null;
//si el usuario no existe, vamos a login
if (sesion.getAttribute("nivelAcceso")==null){ 
	response.sendRedirect("index.jsp");
}
else{
	//devulve: 1 para invitado, 3 consumidor, 5 productor y 9 gestor
	nivelAcceso = (int)sesion.getAttribute("nivelAcceso");
}
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
			switch(nivelAcceso){
	            case 9:
		            %><img src="img/gestor.png" alt="biblioteca" title="imgRol">
		            <%break; 
	            case 5:
	            	%><img src="img/productor.png" alt="biblioteca" title="imgRol">
	            	<%break; 
	            case 3:
	            	%><img src="img/consumidor.png" alt="biblioteca" title="imgRol">
	            	<%break; 
	            default :%><%
	            	break; 
	        }
	        %>
		</div>
	</div>
</header>
