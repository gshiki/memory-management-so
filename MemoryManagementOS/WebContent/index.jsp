<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link type="text/css" rel="stylesheet" href="stylesheet.css">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		
		<script type="text/javascript">
		<c:if test="${ready}">
			window.setTimeout( 'execute()' , 1000 );
		</c:if>
		
		<c:if test="${readyStep}">
			window.setTimeout( 'executeOneStep()' , 1000 );
		</c:if>
		
		function execute() {
			var xmlhttp;
			if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else{// code for IE6, IE5
			  	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange=function(){
			  	if (xmlhttp.readyState==4 && xmlhttp.status==200){
					updateContainers(xmlhttp.responseText, true);			    	
			    }
			};
			xmlhttp.open("GET", "ManagerServlet?action=execute", true);
			xmlhttp.send();
		}
		
		function executeOneStep() {
			var xmlhttp;
			if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else{// code for IE6, IE5
			  	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange=function(){
			  	if (xmlhttp.readyState==4 && xmlhttp.status==200){
					updateContainers(xmlhttp.responseText, false);			    	
			    }
			};
			xmlhttp.open("GET", "ManagerServlet?action=executeStep", true);
			xmlhttp.send();
		}
		
		function addProcess() {
			var xmlhttp;
			if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}else{// code for IE6, IE5
			  	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange=function(){
			  	if (xmlhttp.readyState==4 && xmlhttp.status==200){
			    	console.log("Processo adicionado com sucesso.");
			    }
			};
			xmlhttp.open("GET", "ManagerServlet?action=add-process", true);
			xmlhttp.send();
		}
		
		function updateContainers(response, infinity) {
			var containers = response.split("<separator>");
	    	
	    	for (var indexContainer = 0; indexContainer < containers.length; indexContainer++) {
	    		var container = containers[indexContainer];
	    		
	    		if (container.indexOf("processContainer") > -1) {
	    			var elementContainer = document.getElementById("processContainer");
	    			
	    			container = container.replace("processContainer=", "");
	    			
	    			elementContainer.innerHTML =container + "<hr>";
	    			
	    		} else if (container.indexOf("completedContainer") > -1) {
	    			var elementContainer = document.getElementById("completedContainer");
	    			
	    			container = container.replace("completedContainer=", "");
	    			
	    			elementContainer.innerHTML =container + "<hr>";
	    		} else if (container.indexOf("abortedContainer") > -1) {
	    			var elementContainer = document.getElementById("abortedContainer");
	    			
	    			container = container.replace("abortedContainer=", "");
	    			
	    			elementContainer.innerHTML =container + "<hr>";
	    		} else if (container.indexOf("memoryBlockContainer") > -1) {
	    			var elementContainer = document.getElementById("memoryBlockContainer");
	    			
	    			container = container.replace("memoryBlockContainer=", "");
	    			
	    			elementContainer.innerHTML = container + "<hr>";
	    		}
	    	}
	    	
	    	if (infinity) {
		    	window.setTimeout( 'execute()' , 2000 );
	    	}
		}
		
		</script>
		
		<title>Gerenciador de Memória</title>
	</head>
	<body>
		<form method="post" action="ManagerServlet">
			<div class="container botoes">
			
				<div class="holder">
					<div class="text">Tamanho da memória:</div>
					<div class="input">
						<c:if test="${not ready and not running and not readyStep}">
							<input type="text" name="memorySize" value="0">
						</c:if>
						<c:if test="${ready or running or readyStep}">
							<input disabled="disabled" type="text" name="memorySize">
						</c:if>
					</div>
				</div>
				
				<div class="holder">
					<div class="text">Processos:</div>
					<div class="input">
						<c:if test="${not ready and not running and not readyStep}">
							<input type="text" name="processNo" value="0">
						</c:if>
						<c:if test="${ready or running or readyStep}">
							<input disabled="disabled" type="text" name="processNo">
						</c:if>
					</div>
				</div>
					
				<div class="holder">
					<div class="text">Algoritmo de alocação:</div>
					<div class="input">
						<c:if test="${not ready and not running and not readyStep}">
							<select class="select" name="algorithm">
								<option value="1"> First Fit
								<option value="2"> Best Fit
								<option value="3"> Worst Fit
								<option value="4"> Nest Fit
								<option value="5"> Quick Fit
								<option value="6"> Merge Fit
							</select>
						</c:if>
						
						<c:if test="${ready or running or readyStep}">
							<select disabled="disabled" class="select" name="algorithm">
								<option value="1"> First Fit
								<option value="2"> Best Fit
								<option value="3"> Worst Fit
								<option value="4"> Nest Fit
								<option value="5"> Quick Fit
								<option value="6"> Merge Fit
							</select>
						</c:if>
					</div>
				</div>
				
				<div class="button">
					<c:if test="${not ready and not running}">
						<button type="submit" name="action" value="start">Iniciar</button>
					</c:if>
					<c:if test="${ready or running}">
						<button disabled="disabled" type="submit" name="action" value="start">Iniciar</button>
					</c:if>
					
					<button type="button" onclick="addProcess();">Adicionar Processo</button>
				</div>
				
				<hr>
			</div>
			
			<div class="container" id="memoryBlockContainer">
<!-- 				<p>Blocos de Memória : </p> -->
			</div>
			
			<div class="container" id="processContainer">
<!-- 				<p>Processos Aptos : </p> -->
				
				<c:forEach items="${processList}" var="process">
					<div class="ready">
						<div class="process-info">
							#P<c:out value="${process.getId()}"/> <br/>
							Tempo: <c:out value="${process.getTime()}"/> s <br/>
							Tamanho: <c:out value="${process.getBytes()}"/> bytes <br/>
						</div>
					</div>
				</c:forEach>	
				<hr>
			</div>
		
			<div class="container" id="completedContainer">
<!-- 				<p>Processos Completos : </p> -->
			</div>
			
			<div class="container" id="abortedContainer">
<!-- 				<p>Processos Abortados : </p> -->
			</div>
		</form>
			
		<div id="footer"> 
			<div id="name">Sistemas Operacionais - Maria Stephanie de Souza Fernandes (1320717)</div>
		</div>
			
	</body>
</html>