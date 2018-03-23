package Main.DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 * Abstract class used as a parent class for all the data access objects
 * It includes a common executeQuery function that is used by all DAO
 */
public abstract class BaseDAO<T> {
	protected final String tableName;
	protected Connection connection;
	
	protected BaseDAO(Connection connection, String tableName) {
		this.tableName = tableName;
		this.connection = connection;
	}
	
	protected ResultSet executeQuery(String query) throws SQLException {
		PreparedStatement stmnt = this.connection.prepareStatement(query);
		ResultSet rs = stmnt.executeQuery();
		return rs;
	}
	
    public abstract List<T> get() throws SQLException;
    public abstract T getById(int id) throws SQLException;
    public abstract void insert(T row) throws SQLException; 
    public abstract void update(T row) throws SQLException; 

}
