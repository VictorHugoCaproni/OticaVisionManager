package model.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.Company;
import model.Frame;
import model.ModelException;

public class MySQLFrameDAO implements FrameDAO {

	@Override
	public boolean save(Frame frame) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "INSERT INTO frames (model_name, material, price, stock, manufacture_date, company_id) VALUES (?, ?, ?, ?, ?, ?);";
		db.prepareStatement(sql);
		db.setString(1, frame.getModelName());
		db.setString(2, frame.getMaterial());
		db.setDouble(3, frame.getPrice());
		db.setInt(4, frame.getStock());
		db.setDate(5, frame.getManufactureDate());
		db.setInt(6, frame.getCompany().getId());
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Frame frame) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "UPDATE frames SET model_name = ?, material = ?, price = ?, stock = ?, manufacture_date = ?, company_id = ? WHERE id = ?;";
		db.prepareStatement(sql);
		db.setString(1, frame.getModelName());
		db.setString(2, frame.getMaterial());
		db.setDouble(3, frame.getPrice());
		db.setInt(4, frame.getStock());
		db.setDate(5, frame.getManufactureDate());
		db.setInt(6, frame.getCompany().getId());
		db.setInt(7, frame.getId());
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Frame frame) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "DELETE FROM frames WHERE id = ?;";
		db.prepareStatement(sql);
		db.setInt(1, frame.getId());
		try {
			return db.executeUpdate() > 0;
		} catch (ModelException e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				return false;
			}
			throw e;
		}
	}

	@Override
	public List<Frame> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		List<Frame> list = new ArrayList<>();
		String sql = "SELECT f.*, c.name AS company_name FROM frames f INNER JOIN companies c ON f.company_id = c.id ORDER BY f.model_name;";
		db.createStatement();
		db.executeQuery(sql);
		while (db.next()) {
			list.add(createFrame(db));
		}
		return list;
	}

	@Override
	public List<Frame> listAll(int limit, int offset) throws ModelException {
		DBHandler db = new DBHandler();
		List<Frame> list = new ArrayList<>();
		String sql = "SELECT f.*, c.name AS company_name FROM frames f INNER JOIN companies c ON f.company_id = c.id ORDER BY f.model_name LIMIT ? OFFSET ?;";
		db.prepareStatement(sql);
		db.setInt(1, limit);
		db.setInt(2, offset);
		db.executeQuery();
		while (db.next()) {
			list.add(createFrame(db));
		}
		return list;
	}

	@Override
	public int count() throws ModelException {
		DBHandler db = new DBHandler();
		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM frames;");
		if (db.next()) {
			return db.getInt("total");
		}
		return 0;
	}

	@Override
	public Frame findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "SELECT f.*, c.name AS company_name FROM frames f INNER JOIN companies c ON f.company_id = c.id WHERE f.id = ?;";
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		Frame f = null;
		if (db.next()) {
			f = createFrame(db);
		}
		return f;
	}

	private Frame createFrame(DBHandler db) throws ModelException {
		Frame f = new Frame(db.getInt("id"));
		f.setModelName(db.getString("model_name"));
		f.setMaterial(db.getString("material"));
		f.setPrice(db.getDouble("price"));
		f.setStock(db.getInt("stock"));
		f.setManufactureDate(db.getDate("manufacture_date"));

		Company c = new Company(db.getInt("company_id"));
		c.setName(db.getString("company_name"));
		f.setCompany(c);
		return f;
	}
}
