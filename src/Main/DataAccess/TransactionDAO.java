package Main.DataAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Main.Models.Card;
import Main.Models.Transaction;
import Main.Models.Transaction.TransactionType;

public class TransactionDAO extends BaseDAO<Transaction> {
	private final static String tableName = "transactions";

	protected TransactionDAO(Connection connection, String tableName) {
		super(connection, tableName);
	}

	@Override
	public List<Transaction> get() throws SQLException {
		String query = "select * from " + tableName;
		ResultSet rs = this.executeQuery(query);
		List<Transaction> transactions = new ArrayList<Transaction>();
		while(rs.next()) {
			Transaction transaction = getTransactionFromRow(rs);
			transactions.add(transaction);
		}
		
		return transactions;
	}
	
	public List<Transaction> getByMerchantIdUserId(int merchantId, int userId) throws SQLException {
		String query = String.format("select * from transactions where merchant_id = %d and user_id = %d", merchantId, userId);
		ResultSet rs = this.executeQuery(query);
		List<Transaction> transactions = new ArrayList<Transaction>();
		while(rs.next()) {
			Transaction transaction = getTransactionFromRow(rs);
			transactions.add(transaction);
		}
		
		return transactions;
	}

	@Override
	public Transaction getById(int id) throws SQLException {
		String query = String.format("select * from %s where id = %d", tableName, id);
		ResultSet rs = this.executeQuery(query);

		Transaction transaction = null;
		if (rs.next()) {
			transaction = getTransactionFromRow(rs);
		}
		return transaction;
	}

	public Transaction getByMerchantIdCardId(int merchantId, int cardId) throws SQLException {
		String query = String.format("select * from transactions where merchant_id = %d and card_id = %d", merchantId,
				cardId);
		ResultSet rs = this.executeQuery(query);

		Transaction transaction = null;
		if (rs.next()) {
			transaction = getTransactionFromRow(rs);
		}
		return transaction;
	}
	
	public List<Transaction> getByType(TransactionType type) throws SQLException {
		String query = String.format("select * from transactions where transaction_type = %d", type);
		ResultSet rs = this.executeQuery(query);
		List<Transaction> transactions = new ArrayList<Transaction>();
		while(rs.next()) {
			Transaction transaction = getTransactionFromRow(rs);
			transactions.add(transaction);
		}
		
		return transactions;
	}

	@Override
	public void insert(Transaction row) throws SQLException {
		String query = String.format(
				"insert into transactions (user_id, transaction_type, amount, card_id) values (%d, %d, %d, %d)",
				row.getId(), row.getType(), row.getAmount(), row.getCardId());
		ResultSet rs = this.executeQuery(query);
	}

	@Override
	public void update(Transaction row) throws SQLException {
		String query = String.format(
				"update transactions set " + "user_id = %d, " + "transaction_type = %d, " + "amount = %s, "
						+ "card_id = %d, " + "captured_funds = %d, " + "merchant_id = %d, " + "cancelled = %d "
						+ "where id = %d",
				row.getUserId(), row.getType().getId(), row.getAmount(), row.getCardId(), row.getCapturedFunds(),
				row.getMerchantId(), row.getCancelled(), row.getId());
		this.executeQuery(query);
	}

	private Transaction getTransactionFromRow(ResultSet rs) throws SQLException {
		Transaction transaction = new Transaction();
		transaction.setAmount(rs.getFloat("amount"));
		transaction.setCapturedFunds(rs.getFloat("captures_funds"));
		transaction.setCardId(rs.getInt("card_id"));
		transaction.setId(rs.getInt("id"));
		transaction.setMerchantId(rs.getInt("merchant_id"));
		transaction.setType(TransactionType.getById(rs.getInt("transaction_type")));
		transaction.setUserId(rs.getInt("user_id"));
		return transaction;
	}
}
