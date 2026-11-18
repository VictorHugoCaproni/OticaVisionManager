package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Frame;
import model.ModelException;
import model.OpticalOrder;
import model.User;
import model.dao.DAOFactory;
import model.dao.OpticalOrderDAO;

@WebServlet(urlPatterns = {"/orders", "/order/form", "/order/insert", "/order/update", "/order/delete"})
public class OpticalOrdersController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/order/form": {
			CommonsController.listUsers(req);
			CommonsController.listFrames(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-order.jsp");
			break;
		}
		case "/crud-manager/order/update": {
			int idOrder = Integer.parseInt(req.getParameter("orderId"));
			OpticalOrderDAO dao = DAOFactory.createDAO(OpticalOrderDAO.class);
			OpticalOrder order = null;
			try {
				order = dao.findById(idOrder);
			} catch (ModelException e) {
				e.printStackTrace();
			}
			CommonsController.listUsers(req);
			CommonsController.listFrames(req);
			req.setAttribute("action", "update");
			req.setAttribute("order", order);
			ControllerUtil.forward(req, resp, "/form-order.jsp");
			break;
		}
		default:
			listOrders(req);
			ControllerUtil.transferSessionMessagesToRequest(req);
			ControllerUtil.forward(req, resp, "/orders.jsp");
		}
	}

	private void listOrders(HttpServletRequest req) {
		OpticalOrderDAO dao = DAOFactory.createDAO(OpticalOrderDAO.class);
		int currentPage = ControllerUtil.getCurrentPage(req);
		int limit = ControllerUtil.RECORDS_PER_PAGE;
		int offset = (currentPage - 1) * limit;

		List<OpticalOrder> orders = null;
		try {
			int totalRecords = dao.count();
			orders = dao.listAll(limit, offset);
			ControllerUtil.setPaginationAttributes(req, currentPage, totalRecords);
		} catch (ModelException e) {
			e.printStackTrace();
		}

		if (orders != null)
			req.setAttribute("orders", orders);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/order/insert":
			insertOrder(req);
			break;
		case "/crud-manager/order/update":
			updateOrder(req);
			break;
		case "/crud-manager/order/delete":
			deleteOrder(req);
			break;
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/orders");
	}

	private void insertOrder(HttpServletRequest req) {
		OpticalOrder order = new OpticalOrder();
		order.setPrescriptionCode(req.getParameter("prescriptionCode"));
		order.setLensType(req.getParameter("lensType"));
		order.setTotalValue(Double.parseDouble(req.getParameter("totalValue")));
		order.setDeliveryDate(ControllerUtil.formatDate(req.getParameter("deliveryDate")));
		order.setDelivered(req.getParameter("delivered") != null);
		order.setObservations(req.getParameter("observations"));
		order.setUser(new User(Integer.parseInt(req.getParameter("user"))));
		order.setFrame(new Frame(Integer.parseInt(req.getParameter("frame"))));

		OpticalOrderDAO dao = DAOFactory.createDAO(OpticalOrderDAO.class);
		try {
			if (dao.save(order)) {
				ControllerUtil.sucessMessage(req, "Pedido '" + order.getPrescriptionCode() + "' cadastrado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Pedido '" + order.getPrescriptionCode() + "' não pôde ser cadastrado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateOrder(HttpServletRequest req) {
		OpticalOrder order = new OpticalOrder(Integer.parseInt(req.getParameter("orderId")));
		order.setPrescriptionCode(req.getParameter("prescriptionCode"));
		order.setLensType(req.getParameter("lensType"));
		order.setTotalValue(Double.parseDouble(req.getParameter("totalValue")));
		order.setDeliveryDate(ControllerUtil.formatDate(req.getParameter("deliveryDate")));
		order.setDelivered(req.getParameter("delivered") != null);
		order.setObservations(req.getParameter("observations"));
		order.setUser(new User(Integer.parseInt(req.getParameter("user"))));
		order.setFrame(new Frame(Integer.parseInt(req.getParameter("frame"))));

		OpticalOrderDAO dao = DAOFactory.createDAO(OpticalOrderDAO.class);
		try {
			if (dao.update(order)) {
				ControllerUtil.sucessMessage(req, "Pedido '" + order.getPrescriptionCode() + "' atualizado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Pedido '" + order.getPrescriptionCode() + "' não pôde ser atualizado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteOrder(HttpServletRequest req) {
		int orderId = Integer.parseInt(req.getParameter("id"));
		OpticalOrderDAO dao = DAOFactory.createDAO(OpticalOrderDAO.class);
		try {
			OpticalOrder order = dao.findById(orderId);
			if (order == null)
				throw new ModelException("Pedido não encontrado para exclusão.");
			if (dao.delete(order)) {
				ControllerUtil.sucessMessage(req, "Pedido '" + order.getPrescriptionCode() + "' excluído com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Pedido '" + order.getPrescriptionCode() + "' não pôde ser excluído.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
