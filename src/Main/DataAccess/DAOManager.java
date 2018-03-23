package Main.DataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DAOManager {
	private static DAOManager instance = null;
	private MysqlDataSource dataSource;
	private Connection connection;

	private DAOManager() {
		setUpDataSource();
	}

	public static DAOManager getInstance() {
		if (instance == null) {
			instance = new DAOManager();
		}
		return instance;
	}

	public void open() {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				this.connection = dataSource.getConnection();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() throws SQLException {
		try {
			if (this.connection != null && !this.connection.isClosed())
				this.connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}

	public BaseDAO getDAO(String tableName) throws SQLException {

		if (this.connection == null || this.connection.isClosed()) {
			this.open();
		}

		// TODO change to enum factory
		if (tableName == "users") {
			return new UserDAO(this.connection, tableName);
		} else if (tableName == "cards") {
			return new CardDAO(this.connection, tableName);
		} else if (tableName == "transactions") {
			return new TransactionDAO(this.connection, tableName);
		}
		return null;
	}

	public void startTransaction() throws SQLException {
		this.connection.setAutoCommit(false);
	}

	public void endTransaction() throws SQLException {
		this.connection.commit();
	}

	public void rollBackTransaction() throws SQLException {
		this.connection.rollback();
	}

	private void setUpDataSource() {
		dataSource = new MysqlDataSource();
		dataSource.setPortNumber(3306);
		dataSource.setDatabaseName("prepaid");
		dataSource.setUser("root");
		dataSource.setPassword("geo123!!");
		dataSource.setServerName("localhost");
		dataSource.setUseSSL(false);
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.close();
		} finally {
			super.finalize();
		}

	}
}
