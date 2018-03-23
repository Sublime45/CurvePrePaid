package Main.Models;

public class Card {
	private int id;
	private int userId;
	private float balance;
	private int cardNumber;
	private float blockedFunds;

	public Card() {
	}
	
	public Card(int userId, float balance) {
		this.userId = userId;
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public float getBlockedFunds() {
		return blockedFunds;
	}

	public void setBlockedFunds(float blockedFunds) {
		this.blockedFunds = blockedFunds;
	}
}
