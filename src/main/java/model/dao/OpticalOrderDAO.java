package model.dao;

import java.util.List;
import model.ModelException;
import model.OpticalOrder;

public interface OpticalOrderDAO {
	boolean save(OpticalOrder order) throws ModelException;
	boolean update(OpticalOrder order) throws ModelException;
	boolean delete(OpticalOrder order) throws ModelException;
	List<OpticalOrder> listAll() throws ModelException;
	List<OpticalOrder> listAll(int limit, int offset) throws ModelException;
	int count() throws ModelException;
	OpticalOrder findById(int id) throws ModelException;
}
