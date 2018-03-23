package Main.Controllers;

import java.sql.SQLException;
import java.util.List;

import Main.DataAccess.CardDAO;
import Main.DataAccess.DAOManager;
import Main.DataAccess.TransactionDAO;
import Main.DataAccess.UserDAO;
import Main.Models.Card;
import Main.Models.Transaction;
import Main.Models.User;
import Main.Models.Transaction.TransactionType;

public class CardController {
	private DAOManager daoManager;
	private CardDAO cardDAO;
	private UserDAO userDAO;
	private TransactionDAO transactionDAO;

	public CardController() throws SQLException {
		this.daoManager = DAOManager.getInstance();
		this.cardDAO = (CardDAO) daoManager.getDAO("cards");
		this.userDAO = (UserDAO) daoManager.getDAO("users");
		this.transactionDAO = (TransactionDAO) daoManager.getDAO("transactions");
	}

	public List<Card> getUserCards(int userId) throws SQLException {
		List<Card> cards = this.cardDAO.getByUserId(userId);
		return cards;
	}

	public float getCardBalance(int cardId) throws SQLException {

		Card card = this.cardDAO.getById(cardId);
		return card.getBalance();
	}

	public void loadFunds(int userId, int cardId, float amount) throws SQLException {
		User user = this.userDAO.getById(userId);
		if (user == null) {
			System.out.println("User does not exist");
			return;
		}

		Card card = this.cardDAO.getById(cardId);
		if (card == null) {
			System.out.println("Card does not exist");
			return;
		}
		card.setBalance(card.getBalance() + amount);

		Transaction transaction = new Transaction(userId, cardId, amount, TransactionType.DEPOSIT, 0);
		try {
			this.daoManager.startTransaction();
			transactionDAO.insert(transaction);
			cardDAO.update(card);
			this.daoManager.endTransaction();
		} catch (SQLException e) {
			this.daoManager.rollBackTransaction();
			e.printStackTrace();
		}
	}

	public Boolean requestAuth(int merchantId, int cardId, float amount) throws SQLException {
		Card card = this.cardDAO.getById(cardId);
		float totalFunds = card.getBalance();
		float blockedFunds = card.getBlockedFunds();
		float availableFunds = totalFunds - blockedFunds;

		if (availableFunds < amount) {
			return false;
		}

		blockedFunds += amount;
		card.setBlockedFunds(blockedFunds);

		Transaction transaction = new Transaction(card.getUserId(), cardId, amount, TransactionType.AUTH, merchantId);

		try {
			this.daoManager.startTransaction();
			transactionDAO.insert(transaction);
			cardDAO.update(card);
			this.daoManager.endTransaction();
		} catch (SQLException e) {
			this.daoManager.rollBackTransaction();
			e.printStackTrace();
		}

		return true;
	}
	
	public void addCard(int userId) throws SQLException {		
		Card card = new Card(userId, 0);
		this.cardDAO.insert(card);
	}

	public void captureFunds(int merchantId, int cardId, float amount) throws SQLException {
		Card card = this.cardDAO.getById(cardId);
		Transaction transaction = this.transactionDAO.getByMerchantIdCardId(merchantId, cardId);
		User user = this.userDAO.getById(card.getUserId());
		if(transaction == null || transaction.getCancelled() == 1) {
			System.out.println("Cannot find previously authorized funds for this merchant id");
			return;
		}
		if(transaction.getAmount() == transaction.getCapturedFunds()) {
			System.out.println("Cannot capture any more funds for this transaction");
			return;
		}
		if(transaction.getAmount() - transaction.getCapturedFunds() < amount) {
			System.out.println("Insufficient available funds for this transaction");
			return;
		}
		
		transaction.setCapturedFunds(transaction.getCapturedFunds() + amount);
		card.setBalance(card.getBalance() - amount);
		user.setBalance(user.getBalance() - amount);
		
		this.daoManager.startTransaction();
		this.transactionDAO.update(transaction);
		this.cardDAO.update(card);
		this.userDAO.update(user);
		this.daoManager.endTransaction();
	}

	public void reverseAuth(int transactionId) throws SQLException {
		Transaction transaction = this.transactionDAO.getById(transactionId);
		if(transaction == null || transaction.getCancelled() == 1) {
			System.out.println("Cannot find authorized funds for the specific merchant ID");
		}
		transaction.setCancelled(1);
		
		Transaction cancelTransaction = new Transaction(transaction.getUserId(), transaction.getCardId(), 0, TransactionType.AUTH_CANCEL, transaction.getMerchantId());
		
		this.daoManager.startTransaction();
		transactionDAO.insert(transaction);
		this.transactionDAO.update(transaction);
		this.daoManager.endTransaction();
	}

	public void refund() {

	}
}
