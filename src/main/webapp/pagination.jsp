<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="bottom" class="row">
	<div class="col-md-12">
		<ul class="pagination">
			<li class="${currentPage <= 1 ? 'disabled' : ''}">
				<a href="${currentPage <= 1 ? '#' : '?page='.concat(currentPage - 1)}">&lt; Anterior</a>
			</li>
			<c:forEach begin="1" end="${totalPages}" var="p">
				<li class="${p == currentPage ? 'active' : ''}">
					<a href="?page=${p}">${p}</a>
				</li>
			</c:forEach>
			<li class="${currentPage >= totalPages ? 'disabled' : ''}">
				<a href="${currentPage >= totalPages ? '#' : '?page='.concat(currentPage + 1)}" rel="next">Próximo &gt;</a>
			</li>
		</ul>
	</div>
</div>
