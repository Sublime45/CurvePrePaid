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

public class TransactionController {
	private DAOManager daoManager;
	private TransactionDAO transactionDAO;
	
	public TransactionController() throws SQLException {
		this.daoManager = DAOManager.getInstance();
		this.transactionDAO = (TransactionDAO) daoManager.getDAO("transactions");
	}
	
	public List<Transaction> listTransactions(List<TransactionType> typeFilter) {
		return null;
	}
	
}
