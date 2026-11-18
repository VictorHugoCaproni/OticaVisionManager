<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>Ótica Vision - Pedido Ótico</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty order ? 'Atualizar' : 'Novo'} Pedido Ótico</h3>
		
		<form action="${pageContext.request.contextPath}/order/${action}" method="POST">
			<input type="hidden" value="${order.getId()}" name="orderId">
			
			<div class="row">
				<div class="form-group col-md-4">
					<label for="prescriptionCode">Código da Receita / OS</label>
					<input type="text" class="form-control" id="prescriptionCode" name="prescriptionCode" 
						   autofocus placeholder="Ex: OS-2026-001" 
						   required value="${order.getPrescriptionCode()}" />
				</div>
				
				<div class="form-group col-md-4">
					<label for="lensType">Tipo de Lente</label>
					<select id="lensType" class="form-control" name="lensType" required>
						<option value="" disabled ${empty order ? 'selected' : ''}>Selecione o tipo de lente</option>
						<option value="Visão Simples Antirreflexo" ${order.getLensType() == 'Visão Simples Antirreflexo' ? 'selected' : ''}>Visão Simples Antirreflexo</option>
						<option value="Multifocal Digital" ${order.getLensType() == 'Multifocal Digital' ? 'selected' : ''}>Multifocal Digital</option>
						<option value="Fotossensível (Transitions)" ${order.getLensType() == 'Fotossensível (Transitions)' ? 'selected' : ''}>Fotossensível (Transitions)</option>
						<option value="Filtro Luz Azul" ${order.getLensType() == 'Filtro Luz Azul' ? 'selected' : ''}>Filtro Luz Azul</option>
					</select>
				</div>

				<div class="form-group col-md-4">
					<label for="totalValue">Valor Total (R$)</label>
					<input type="number" step="0.01" min="0" class="form-control" id="totalValue" name="totalValue" 
						   placeholder="Ex: 850.00" 
						   required value="${order.getTotalValue()}" />
				</div>
			</div>
			
			<div class="row">
				<div class="form-group col-md-4">
					<label for="deliveryDate">Previsão de Entrega</label>
					<input type="date" class="form-control" id="deliveryDate" name="deliveryDate" 
						   required value="${order.getDeliveryDate()}" />
				</div>

				<div class="form-group col-md-4">
					<label for="user">Cliente / Atendente (Usuário)</label>
					<select id="user" class="form-control" name="user" required>
					  <option value="" ${empty order ? 'selected' : ''}>Selecione o usuário</option>
					  <c:forEach var="u" items="${users}">
					  	<option value="${u.getId()}" ${order.getUser().getId() == u.getId() ? 'selected' : ''}>
					  		${u.getName()}
					  	</option>	
					  </c:forEach>
					</select>
				</div>

				<div class="form-group col-md-4">
					<label for="frame">Armação Escolhida</label>
					<select id="frame" class="form-control" name="frame" required>
					  <option value="" ${empty order ? 'selected' : ''}>Selecione a armação</option>
					  <c:forEach var="f" items="${frames}">
					  	<option value="${f.getId()}" ${order.getFrame().getId() == f.getId() ? 'selected' : ''}>
					  		${f.getModelName()} (${f.getMaterial()})
					  	</option>	
					  </c:forEach>
					</select>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-8">
					<label for="observations">Observações da Receita (Grau OD / OE / DNP)</label>
					<textarea class="form-control" id="observations" name="observations" rows="3" 
							  placeholder="Ex: OD -1.50 esf | OE -1.75 esf | DNP 32mm">${order.getObservations()}</textarea>
				</div>

				<div class="form-group col-md-4" style="padding-top: 25px;">
					<div class="checkbox">
						<label>
							<input type="checkbox" name="delivered" value="true" ${order.isDelivered() ? 'checked' : ''}>
							<strong>Óculos já entregue ao cliente</strong>
						</label>
					</div>
				</div>
			</div>
			
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/orders" class="btn btn-default">Cancelar</a>
					<button type="submit" class="btn btn-primary">${not empty order ? 'Atualizar' : 'Salvar'} Pedido</button>
				</div>
			</div>
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
