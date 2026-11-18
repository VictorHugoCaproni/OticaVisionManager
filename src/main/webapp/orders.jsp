<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>Ótica Vision - Pedidos Óticos</title>
</head>
<body>
	<%@include file="modal.html"%>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<div id="alert" style="${not empty message ? 'display: block;' : 'display: none;'}" class="alert alert-dismissable ${alertType eq 1 ? 'alert-success' : 'alert-danger'}">
		  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		  ${message}
		</div>
		
		<div id="top" class="row">
 			<div class="col-md-3">
		        <h3>Pedidos Óticos</h3>
		    </div>
		 
		    <div class="col-md-6">
		        <div class="input-group h2">
		            <input name="data[search]" class="form-control" id="search" type="text" placeholder="Pesquisar pedidos">
		            <span class="input-group-btn">
		                <button class="btn btn-danger" type="submit">
		                    <span class="glyphicon glyphicon-search"></span>
		                </button>
		            </span>
		        </div>
		    </div>
		 
		    <div class="col-md-3">
		        <a href="${pageContext.request.contextPath}/order/form" class="btn btn-danger pull-right h2"><span class="glyphicon glyphicon-plus"></span>&nbsp;Novo Pedido</a>
		    </div>
     	</div>
     	
     	<hr />
     	
     	<div id="list" class="row">
     		<div class="table-responsive col-md-12">
		        <table class="table table-striped table-hover" cellspacing="0" cellpadding="0">
		            <thead>
		                <tr>
		                    <th>Cód. Receita</th>
		                    <th>Tipo de Lente</th>
		                    <th>Armação</th>
		                    <th>Cliente / Responsável</th>
		                    <th>Valor Total</th>
		                    <th>Entrega</th>
		                    <th>Status</th>
		                    <th>Editar</th>
		                    <th>Excluir</th>
		                 </tr>
		            </thead>
		            <tbody>
		            	<c:forEach var="order" items="${orders}">
							<tr>
			                    <td>${order.getPrescriptionCode()}</td>
			                    <td>${order.getLensType()}</td>
			                    <td>${order.getFrame().getModelName()}</td>
			                    <td>${order.getUser().getName()}</td>
			                    <td>R$ ${order.getTotalValue()}</td>
			                    <td>${order.getDeliveryDate()}</td>
			                    <td>
			                    	<span class="label ${order.isDelivered() ? 'label-success' : 'label-warning'}">
			                    		${order.isDelivered() ? 'Entregue' : 'Em Laboratório'}
			                    	</span>
			                    </td>
			                    
			                    <td class="actions">
			                        <a class="btn btn-info btn-xs" 
			                           href="${pageContext.request.contextPath}/order/update?orderId=${order.getId()}" >
			                           <span class="glyphicon glyphicon-edit"></span>
			                        </a>
			                    </td>
			                    
			                    <td class="actions">
			                        <a class="btn btn-danger btn-xs modal-remove"
			                           order-id="${order.getId()}" 
			                           order-code="${order.getPrescriptionCode()}" data-toggle="modal" 
			                           data-target="#delete-modal" href="#"><span 
			                           class="glyphicon glyphicon-trash"></span></a>
			                    </td>
			                </tr>
						</c:forEach>
		            </tbody>
		         </table>
		     </div>
     	</div>
     	
     	<%@include file="pagination.jsp"%>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		    setTimeout(function() {
		        $("#alert").slideUp(500);
		    }, 3000);
		    
		    $(".modal-remove").click(function () {
	            var orderCode = $(this).attr('order-code');
	            var orderId = $(this).attr('order-id');
	            $(".modal-body #hiddenValue").text("o pedido '"+orderCode+"'");
	            $("#id").attr("value", orderId);
	            $("#form").attr("action", "${pageContext.request.contextPath}/order/delete");
	        });
		});
	</script>
</body>
</html>
