package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.Frame;
import model.ModelException;
import model.dao.DAOFactory;
import model.dao.FrameDAO;

@WebServlet(urlPatterns = {"/frames", "/frame/form", "/frame/insert", "/frame/update", "/frame/delete"})
public class FramesController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/frame/form": {
			CommonsController.listCompanies(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-frame.jsp");
			break;
		}
		case "/crud-manager/frame/update": {
			int idFrame = Integer.parseInt(req.getParameter("frameId"));
			FrameDAO dao = DAOFactory.createDAO(FrameDAO.class);
			Frame frame = null;
			try {
				frame = dao.findById(idFrame);
			} catch (ModelException e) {
				e.printStackTrace();
			}
			CommonsController.listCompanies(req);
			req.setAttribute("action", "update");
			req.setAttribute("frame", frame);
			ControllerUtil.forward(req, resp, "/form-frame.jsp");
			break;
		}
		default:
			listFrames(req);
			ControllerUtil.transferSessionMessagesToRequest(req);
			ControllerUtil.forward(req, resp, "/frames.jsp");
		}
	}

	private void listFrames(HttpServletRequest req) {
		FrameDAO dao = DAOFactory.createDAO(FrameDAO.class);
		int currentPage = ControllerUtil.getCurrentPage(req);
		int limit = ControllerUtil.RECORDS_PER_PAGE;
		int offset = (currentPage - 1) * limit;

		List<Frame> frames = null;
		try {
			int totalRecords = dao.count();
			frames = dao.listAll(limit, offset);
			ControllerUtil.setPaginationAttributes(req, currentPage, totalRecords);
		} catch (ModelException e) {
			e.printStackTrace();
		}

		if (frames != null)
			req.setAttribute("frames", frames);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/frame/insert":
			insertFrame(req);
			break;
		case "/crud-manager/frame/update":
			updateFrame(req);
			break;
		case "/crud-manager/frame/delete":
			deleteFrame(req);
			break;
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/frames");
	}

	private void insertFrame(HttpServletRequest req) {
		Frame frame = new Frame();
		frame.setModelName(req.getParameter("modelName"));
		frame.setMaterial(req.getParameter("material"));
		frame.setPrice(Double.parseDouble(req.getParameter("price")));
		frame.setStock(Integer.parseInt(req.getParameter("stock")));
		frame.setManufactureDate(ControllerUtil.formatDate(req.getParameter("manufactureDate")));
		frame.setCompany(new Company(Integer.parseInt(req.getParameter("company"))));

		FrameDAO dao = DAOFactory.createDAO(FrameDAO.class);
		try {
			if (dao.save(frame)) {
				ControllerUtil.sucessMessage(req, "Armação '" + frame.getModelName() + "' cadastrada com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Armação '" + frame.getModelName() + "' não pôde ser cadastrada.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateFrame(HttpServletRequest req) {
		Frame frame = new Frame(Integer.parseInt(req.getParameter("frameId")));
		frame.setModelName(req.getParameter("modelName"));
		frame.setMaterial(req.getParameter("material"));
		frame.setPrice(Double.parseDouble(req.getParameter("price")));
		frame.setStock(Integer.parseInt(req.getParameter("stock")));
		frame.setManufactureDate(ControllerUtil.formatDate(req.getParameter("manufactureDate")));
		frame.setCompany(new Company(Integer.parseInt(req.getParameter("company"))));

		FrameDAO dao = DAOFactory.createDAO(FrameDAO.class);
		try {
			if (dao.update(frame)) {
				ControllerUtil.sucessMessage(req, "Armação '" + frame.getModelName() + "' atualizada com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Armação '" + frame.getModelName() + "' não pôde ser atualizada.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteFrame(HttpServletRequest req) {
		int frameId = Integer.parseInt(req.getParameter("id"));
		FrameDAO dao = DAOFactory.createDAO(FrameDAO.class);
		try {
			Frame frame = dao.findById(frameId);
			if (frame == null)
				throw new ModelException("Armação não encontrada para exclusão.");
			if (dao.delete(frame)) {
				ControllerUtil.sucessMessage(req, "Armação '" + frame.getModelName() + "' excluída com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Armação '" + frame.getModelName() + "' não pode ser excluída pois está vinculada a pedidos.");
			}
		} catch (ModelException e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				ControllerUtil.errorMessage(req, e.getMessage());
			}
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
