package model.dao;

import java.util.List;
import model.Frame;
import model.ModelException;

public interface FrameDAO {
	boolean save(Frame frame) throws ModelException;
	boolean update(Frame frame) throws ModelException;
	boolean delete(Frame frame) throws ModelException;
	List<Frame> listAll() throws ModelException;
	List<Frame> listAll(int limit, int offset) throws ModelException;
	int count() throws ModelException;
	Frame findById(int id) throws ModelException;
}
