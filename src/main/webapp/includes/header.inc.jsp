<%
HttpSession sesion = request.getSession();
int nivelAcceso = 0;
//si el usuario no existe, lo redirecciona en el protec.inc a login con un mensaje de error
if (sesion.getAttribute("nivelAcceso")==null){ 
	nivelAcceso = -1;
}
else{
	//devulve: 1 para invitado, 3 consumidor, 5 productor y 9 gestor
	nivelAcceso = (int)sesion.getAttribute("nivelAcceso");
}
%>
<header>
	<div class="cabizq">
		<div class="logo">
			<a href="LoginController?opcion=4"><img src="img/cesta.png" alt="cesta" title="logo" ></a>
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
