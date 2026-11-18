package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();
		if (action.endsWith("/logout")) {
			HttpSession session = req.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			ControllerUtil.redirect(resp, req.getContextPath() + "/login");
			return;
		}
		ControllerUtil.transferSessionMessagesToRequest(req);
		ControllerUtil.forward(req, resp, "/login.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		try {
			User user = dao.findByEmailAndPassword(email, password);
			if (user != null) {
				HttpSession session = req.getSession(true);
				session.setAttribute("usuarioLogado", user);
				ControllerUtil.redirect(resp, req.getContextPath() + "/orders");
			} else {
				ControllerUtil.errorMessage(req, "E-mail ou senha inválidos.");
				ControllerUtil.redirect(resp, req.getContextPath() + "/login");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, "Erro ao autenticar: " + e.getMessage());
			ControllerUtil.redirect(resp, req.getContextPath() + "/login");
		}
	}
}
