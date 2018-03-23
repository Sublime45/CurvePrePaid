package Main.DataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import Main.Models.Card;

/*
 * DAO used to access the cards table. Some of its functions are not optimized as they should be and
 * could use a bit extending. The get functions could be generalized to a single function that gets 
 * dynamic filters to use in the where statement of the query.
 */
public class CardDAO extends BaseDAO<Card>{
	private final static String tableName = "cards";
	
	protected CardDAO(Connection connection, String tableName) {
		super(connection, tableName);
	}

	@Override
	public List<Card> get() throws SQLException {
		String query = "select * from " + tableName;
		ResultSet rs = this.executeQuery(query);
		List<Card> cards = new ArrayList<Card>();
		while(rs.next()) {
			Card card = getCardFromRow(rs);
			cards.add(card);
		}
		
		return cards;
	}
	
	public List<Card> getByUserId(int userId) throws SQLException {
		String query = String.format("select * from cards where user_id = %d", userId);

		ResultSet rs = this.executeQuery(query);
		List<Card> cards = new ArrayList<Card>();
		while(rs.next()) {
			Card card = getCardFromRow(rs);
			cards.add(card);
		}
		
		return cards;
	}
	
	@Override
	public Card getById(int id) throws SQLException {
		String query = String.format("select * from cards where id = %d", id);
		ResultSet rs = this.executeQuery(query);
		Card card = null;
		if(rs.next()) {
			card = getCardFromRow(rs);
		}
		return card;
	}

	public void update(Card row) throws SQLException {
		String query = String.format("update cards set "
				+ "user_id = %d, "
				+ "balance = %d,"
				+ "card_num = %s "
				+ "where id = %d",row.getUserId(), row.getBalance(), row.getCardNumber(), row.getId());
		this.executeQuery(query);
	}

	@Override
	public void insert(Card row) throws SQLException {
		String query = String.format("insert into cards (user_id, balance, card_num, blocked_funds) "
				+ "select %d, 0, max(card_num) + 1, 0 "
				+ "from cards;", row.getUserId());
		this.executeQuery(query);		
	}	
	
	/*
	 * Function used to map the table fields to the appropriate fields of the Model.
	 */
	private Card getCardFromRow(ResultSet rs) throws SQLException {
		Card card = new Card();
		card.setId(rs.getInt("id"));
		card.setUserId(rs.getInt("user_id"));
		card.setBalance(rs.getFloat("balance"));
		card.setCardNumber(rs.getInt("card_num"));
		card.setBlockedFunds(rs.getFloat("blocked_funds"));

		return card;
	}
}
