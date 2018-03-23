package Main.Models;

import Main.Models.Transaction.TransactionType;

public class Transaction {
	public enum TransactionType {
		DEPOSIT(1), AUTH(2), CAPTURE(3), AUTH_CANCEL(4), REFUND(5);

		private int id;

		private TransactionType(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}
		
		public static TransactionType getById(int id) {
			for(TransactionType type: TransactionType.values()) {
				if(type.getId() == id) {
					return type;
				}
			}
			return null;
		}
	};

	private int id;
	private int userId;
	private TransactionType type;
	private float amount;
	private int cardId;
	private float capturedFunds;
	private int merchantId;
	private int cancelled;

	public Transaction() {
	}

	public Transaction(int userId, int cardId, float amount, TransactionType type, int merchantId) {
		this.userId = userId;
		this.cardId = cardId;
		this.amount = amount;
		this.type = type;
		this.merchantId = merchantId;
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

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	
	public float getCapturedFunds() {
		return capturedFunds;
	}

	public void setCapturedFunds(float capturedFunds) {
		this.capturedFunds = capturedFunds;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getCancelled() {
		return cancelled;
	}

	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}
	
}
