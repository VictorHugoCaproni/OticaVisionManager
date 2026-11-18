package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.Frame;
import model.ModelException;
import model.OpticalOrder;
import model.User;

public class MySQLOpticalOrderDAO implements OpticalOrderDAO {

	@Override
	public boolean save(OpticalOrder order) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "INSERT INTO optical_orders (prescription_code, lens_type, total_value, delivery_date, delivered, observations, user_id, frame_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		db.prepareStatement(sql);
		db.setString(1, order.getPrescriptionCode());
		db.setString(2, order.getLensType());
		db.setDouble(3, order.getTotalValue());
		db.setDate(4, order.getDeliveryDate());
		db.setBoolean(5, order.isDelivered());
		db.setString(6, order.getObservations());
		db.setInt(7, order.getUser().getId());
		db.setInt(8, order.getFrame().getId());
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(OpticalOrder order) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "UPDATE optical_orders SET prescription_code = ?, lens_type = ?, total_value = ?, delivery_date = ?, delivered = ?, observations = ?, user_id = ?, frame_id = ? WHERE id = ?;";
		db.prepareStatement(sql);
		db.setString(1, order.getPrescriptionCode());
		db.setString(2, order.getLensType());
		db.setDouble(3, order.getTotalValue());
		db.setDate(4, order.getDeliveryDate());
		db.setBoolean(5, order.isDelivered());
		db.setString(6, order.getObservations());
		db.setInt(7, order.getUser().getId());
		db.setInt(8, order.getFrame().getId());
		db.setInt(9, order.getId());
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(OpticalOrder order) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "DELETE FROM optical_orders WHERE id = ?;";
		db.prepareStatement(sql);
		db.setInt(1, order.getId());
		return db.executeUpdate() > 0;
	}

	@Override
	public List<OpticalOrder> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		List<OpticalOrder> list = new ArrayList<>();
		String sql = "SELECT o.*, u.nome AS user_name, f.model_name AS frame_name FROM optical_orders o "
				+ "INNER JOIN users u ON o.user_id = u.id "
				+ "INNER JOIN frames f ON o.frame_id = f.id "
				+ "ORDER BY o.delivery_date DESC;";
		db.createStatement();
		db.executeQuery(sql);
		while (db.next()) {
			list.add(createOrder(db));
		}
		return list;
	}

	@Override
	public List<OpticalOrder> listAll(int limit, int offset) throws ModelException {
		DBHandler db = new DBHandler();
		List<OpticalOrder> list = new ArrayList<>();
		String sql = "SELECT o.*, u.nome AS user_name, f.model_name AS frame_name FROM optical_orders o "
				+ "INNER JOIN users u ON o.user_id = u.id "
				+ "INNER JOIN frames f ON o.frame_id = f.id "
				+ "ORDER BY o.delivery_date DESC LIMIT ? OFFSET ?;";
		db.prepareStatement(sql);
		db.setInt(1, limit);
		db.setInt(2, offset);
		db.executeQuery();
		while (db.next()) {
			list.add(createOrder(db));
		}
		return list;
	}

	@Override
	public int count() throws ModelException {
		DBHandler db = new DBHandler();
		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM optical_orders;");
		if (db.next()) {
			return db.getInt("total");
		}
		return 0;
	}

	@Override
	public OpticalOrder findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		String sql = "SELECT o.*, u.nome AS user_name, f.model_name AS frame_name FROM optical_orders o "
				+ "INNER JOIN users u ON o.user_id = u.id "
				+ "INNER JOIN frames f ON o.frame_id = f.id WHERE o.id = ?;";
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		OpticalOrder order = null;
		if (db.next()) {
			order = createOrder(db);
		}
		return order;
	}

	private OpticalOrder createOrder(DBHandler db) throws ModelException {
		OpticalOrder o = new OpticalOrder(db.getInt("id"));
		o.setPrescriptionCode(db.getString("prescription_code"));
		o.setLensType(db.getString("lens_type"));
		o.setTotalValue(db.getDouble("total_value"));
		o.setDeliveryDate(db.getDate("delivery_date"));
		o.setDelivered(db.getBoolean("delivered"));
		o.setObservations(db.getString("observations"));

		User u = new User(db.getInt("user_id"));
		u.setName(db.getString("user_name"));
		o.setUser(u);

		Frame f = new Frame(db.getInt("frame_id"));
		f.setModelName(db.getString("frame_name"));
		o.setFrame(f);

		return o;
	}
}
