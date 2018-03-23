package Main.DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import Main.Models.Card;
import Main.Models.User;

public class UserDAO extends BaseDAO<User>{
	private final static String tableName = "users";
	
	protected UserDAO(Connection connection, String tableName) {
		super(connection, tableName);
	}

	@Override
	public List<User> get() throws SQLException {
		String query = "select * from " + tableName;
		ResultSet rs = this.executeQuery(query);
		List<User> users = new ArrayList<User>();
		while(rs.next()) {
			User user = getUserFromRow(rs);
			users.add(user);
		}
		
		return users;
	}
	
	@Override
	public User getById(int id) throws SQLException {
		String query = String.format("select * from %s where id = %d", tableName, id);
		ResultSet rs = this.executeQuery(query);
		User user = null;
		if(rs.next()) {
			user = getUserFromRow(rs);
		}
		return user;
	}
	
	public void update(User user) throws SQLException {
		String query = String.format("update users set "
				+ "total_balance = %d"
				+ "where id = %d", user.getBalance(), user.getId());
		this.executeQuery(query);
	}
	
	
	@Override
	public void insert(User row) throws SQLException {
		// TODO Auto-generated method stub
		
	}	
	
	private User getUserFromRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setBalance(rs.getFloat("total_balance"));
		return user;
	}
}
