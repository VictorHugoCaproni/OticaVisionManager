<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>Ótica Vision Manager - Login</title>
</head>
<body>
	<div class="container" style="margin-top: 80px; max-width: 420px;">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<img height="40px" width="40px" alt="Logo" src="${pageContext.request.contextPath}/images/logo.png">
				<h3 class="panel-title" style="margin-top: 10px;"><strong>Ótica Vision Manager - Acesso</strong></h3>
			</div>
			<div class="panel-body">
				<div id="alert" style="${not empty message ? 'display: block;' : 'display: none;'}" class="alert alert-dismissable ${alertType eq 1 ? 'alert-success' : 'alert-danger'}">
					<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
					${message}
				</div>

				<form action="${pageContext.request.contextPath}/login" method="POST">
					<div class="form-group">
						<label for="email">E-mail</label>
						<input type="email" class="form-control" id="email" name="email" placeholder="Digite seu e-mail" required autofocus>
					</div>
					<div class="form-group">
						<label for="password">Senha</label>
						<input type="password" class="form-control" id="password" name="password" placeholder="Digite sua senha" required>
					</div>
					<button type="submit" class="btn btn-primary btn-block">Entrar</button>
				</form>
			</div>
		</div>
	</div>
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
