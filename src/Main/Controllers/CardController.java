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

/*
 * The card controller is responisble for the operations related to the cards of the user.
 * It is organized as an entity and uses multiple DAO so there is not 1-1 correspondance with the DAO
 * For now the controller just logs the different outputs for the operations. 
 * If this controller is used by a webservice the System logs should be removed and replaced by the 
 * appropriate response mechanism.
 */
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

	/*
	 * Returns the total balance of the card. This balance should not be confused with available funds
	 * since some of these funds may be blocked by the marchant
	 */
	public float getCardBalance(int cardId) throws SQLException {
		Card card = this.cardDAO.getById(cardId);
		return card.getBalance();
	}

	/*
	 * A function used to load funds into a specific card for the specific user.
	 * Although cardId is unique among the users, the userId is also required 
	 * for verification purposes
	 */
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
			this.transactionDAO.insert(transaction);
			this.cardDAO.update(card);
			this.daoManager.endTransaction();
		} catch (SQLException e) {
			this.daoManager.rollBackTransaction();
			e.printStackTrace();
		}
	}

	/*
	 * Function for the merchant to request fund authorization. The function first checks if enough 
	 * funds are available in the specific card. If the funds are enough it blocks them. 
	 */
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
			this.transactionDAO.insert(transaction);
			this.cardDAO.update(card);
			this.daoManager.endTransaction();
		} catch (SQLException e) {
			this.daoManager.rollBackTransaction();
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * Method to add a new card for a specific user
	 */
	public void addCard(int userId) throws SQLException {
		Card card = new Card(userId, 0);
		this.cardDAO.insert(card);
	}

	/*
	 * Function to capture blocked funds from a specific card.
	 * The function first checks if a transaction already exists for the specific merchant.
	 * If a transaction exists a check for available blocked funds is been done on the transaction itself
	 * This is done so a merchant cannot capture more funds than those blocked and also enables the merchant
	 * to capture funds in more than one go.
	 */
	public void captureFunds(int merchantId, int cardId, float amount) throws SQLException {
		Card card = this.cardDAO.getById(cardId);
		Transaction transaction = this.transactionDAO.getByMerchantIdCardId(merchantId, cardId);
		User user = this.userDAO.getById(card.getUserId());
		if (transaction == null || transaction.getCancelled() == 1) {
			System.out.println("Cannot find previously authorized funds for this merchant id");
			return;
		}
		if (transaction.getAmount() == transaction.getCapturedFunds()) {
			System.out.println("Cannot capture any more funds for this transaction");
			return;
		}
		if (transaction.getAmount() - transaction.getCapturedFunds() < amount) {
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

	/*
	 * This function is used to reverse the authorization of funds given a specific transactionId. 
	 * If the transaction exists, it sets the cancelled flag and also creates a new transaction 
	 * with AUTH_CANCEL type. This is done in case the user wants a report on all its transactions
	 */
	public void reverseAuth(int transactionId) throws SQLException {
		Transaction transaction = this.transactionDAO.getById(transactionId);
		if (transaction == null || transaction.getCancelled() == 1) {
			System.out.println("Cannot find authorized funds for the specific transaction ID");
		}
		transaction.setCancelled(1);

		Transaction cancelTransaction = new Transaction(transaction.getUserId(), transaction.getCardId(), 0,
				TransactionType.AUTH_CANCEL, transaction.getMerchantId());

		this.daoManager.startTransaction();
		this.transactionDAO.insert(transaction);
		this.transactionDAO.insert(cancelTransaction);
		this.transactionDAO.update(transaction);
		this.daoManager.endTransaction();
	}

	/*
	 * The function refunds a specific user's card with an amount.
	 * It adds the given amount to the total funds of the card and to the total funds of the user.
	 */
	public void refund(int merchantId, int cardId, float amount) throws SQLException {
		Card card = this.cardDAO.getById(cardId);
		if(card == null) {
			System.out.println("Specified card can't be found");
		}
		Transaction transaction = new Transaction(card.getUserId(), cardId, amount, TransactionType.REFUND, merchantId);
		User user = this.userDAO.getById(card.getUserId());

		card.setBalance(card.getBalance() + amount);
		user.setBalance(user.getBalance() + amount);
		
		this.daoManager.startTransaction();
		this.transactionDAO.insert(transaction);
		this.cardDAO.update(card);
		this.userDAO.update(user);
		this.daoManager.endTransaction();
	}
}
