package Main.Controllers;

import java.sql.SQLException;
import java.util.List;

import javax.management.RuntimeErrorException;

import Main.DataAccess.CardDAO;
import Main.DataAccess.DAOManager;
import Main.DataAccess.TransactionDAO;
import Main.DataAccess.UserDAO;
import Main.Models.Card;
import Main.Models.Transaction;
import Main.Models.Transaction.TransactionType;
import Main.Models.User;

/*	
 * A simple class created to handle all transactions related operations
 * All functions that are mainly related to the transactions should be added to this controller
 */
public class TransactionController {
	private DAOManager daoManager;
	private TransactionDAO transactionDAO;
	
	public TransactionController() throws SQLException {
		this.daoManager = DAOManager.getInstance();
		this.transactionDAO = (TransactionDAO) daoManager.getDAO("transactions");
	}
	
	public List<Transaction> listTransactions(TransactionType typeFilter) throws SQLException {
		return this.transactionDAO.getByType(typeFilter);
	}
	
}
