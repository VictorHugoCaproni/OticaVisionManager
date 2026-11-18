<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>Ótica Vision - Cadastro de Armação</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty frame ? 'Atualizar' : 'Adicionar'} Armação</h3>
		
		<form action="${pageContext.request.contextPath}/frame/${action}" method="POST">
			<input type="hidden" value="${frame.getId()}" name="frameId">
			
			<div class="row">
				<div class="form-group col-md-4">
					<label for="modelName">Modelo da Armação</label>
					<input type="text" class="form-control" id="modelName" name="modelName" 
						   autofocus placeholder="Ex: Aviador Clássico RB3025" 
						   required value="${frame.getModelName()}" />
				</div>
				
				<div class="form-group col-md-4">
					<label>Material</label><br/>
					<label class="radio-inline">
						<input type="radio" name="material" value="Acetato" ${frame.getMaterial() == 'Acetato' || empty frame ? 'checked' : ''}> Acetato
					</label>
					<label class="radio-inline">
						<input type="radio" name="material" value="Metal" ${frame.getMaterial() == 'Metal' ? 'checked' : ''}> Metal
					</label>
					<label class="radio-inline">
						<input type="radio" name="material" value="Titânio" ${frame.getMaterial() == 'Titânio' ? 'checked' : ''}> Titânio
					</label>
				</div>

				<div class="form-group col-md-4">
					<label for="price">Preço de Venda (R$)</label>
					<input type="number" step="0.01" min="0" class="form-control" id="price" name="price" 
						   placeholder="Ex: 399.90" 
						   required value="${frame.getPrice()}" />
				</div>
			</div>
			
			<div class="row">
				<div class="form-group col-md-4">
					<label for="stock">Quantidade em Estoque</label>
					<input type="number" min="0" class="form-control" id="stock" name="stock" 
						   placeholder="Ex: 15" 
						   required value="${frame.getStock()}" />
				</div>

				<div class="form-group col-md-4">
					<label for="manufactureDate">Data de Fabricação</label>
					<input type="date" class="form-control" id="manufactureDate" name="manufactureDate" 
						   required value="${frame.getManufactureDate()}" />
				</div>
				
				<div class="form-group col-md-4">
					<label for="company">Fabricante / Fornecedor</label>
					<select id="company" class="form-control" name="company" required>
					  <option value="" ${not empty frame ? "" : 'selected'}>Selecione o fabricante</option>
					  <c:forEach var="comp" items="${companies}">
					  	<option value="${comp.getId()}" ${frame.getCompany().getId() == comp.getId() ? 'selected' : ''}>
					  		${comp.getName()}
					  	</option>	
					  </c:forEach>
					</select>
				</div>
			</div>
			
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/frames" class="btn btn-default">Cancelar</a>
					<button type="submit" class="btn btn-primary">${not empty frame ? 'Atualizar' : 'Cadastrar'} Armação</button>
				</div>
			</div>
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
