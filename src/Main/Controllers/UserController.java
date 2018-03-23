package Main.Controllers;

import java.sql.SQLException;
import java.util.List;

import Main.DataAccess.CardDAO;
import Main.DataAccess.DAOManager;
import Main.DataAccess.UserDAO;
import Main.Models.Card;
import Main.Models.User;

/*
 * Simple controller responsible to handle all user related operations. All operations directly related to the user 
 * should be handled by this controller.
 */
public class UserController {
	private DAOManager daoManager;
	private UserDAO userDAO;
	private CardDAO cardDAO;


	public UserController() throws SQLException {
		this.daoManager = DAOManager.getInstance();
		this.cardDAO = (CardDAO) daoManager.getDAO("cards");
		this.userDAO = (UserDAO) daoManager.getDAO("users");
	}
	
	/*
	 * Simple function that returns all the cards of the user
	 */
	public List<Card> getUserCards(int userId) throws SQLException {
		List<Card> cards = this.cardDAO.getByUserId(userId);
		return cards;
	}
	
	/*
	 * Simple function to return the total_funds of the user.
	 */
	public Float getUserFunds(int userId) throws SQLException {
		User user = this.userDAO.getById(userId);
		if(user == null) {
			System.out.println("The specified user could not be found");
			return null;
		}
		return user.getBalance();
	}
}
