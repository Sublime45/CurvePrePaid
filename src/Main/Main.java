package Main;

import java.sql.SQLException;
import java.util.List;

import Main.DataAccess.DAOManager;
import Main.DataAccess.UserDAO;
import Main.Models.User;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");

		DAOManager dm = DAOManager.getInstance();
		try {
			UserDAO userDAO = (UserDAO) dm.getDAO("users");
			List<User> users = userDAO.get();
			User user = userDAO.getById(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
