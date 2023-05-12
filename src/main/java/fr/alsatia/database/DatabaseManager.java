package fr.alsatia.database;

import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;

public class DatabaseManager {
	
	private final String urlBase, host, database, username, password;
	private static Connection connection;
	
	public DatabaseManager() {
		this.urlBase = "jdbc:mysql://";
		this.host = "10.0.0.3";
		this.database = "alsatia?characterEncoding=utf8&useSSL=false";
		this.username = "system";
		this.password = "Pq=%aBx@qZ_21_5y5T3m!";
	}
	/**
	 * To make connect on database
	 */
	public Connection connection() {
			try {
				connection = DriverManager.getConnection(this.urlBase + this.host + "/" + this.database, this.username, this.password);
				getLogger().info(ChatColor.GREEN + "[DatabaseManager] Successfully connected!");
				return connection;
			} catch (SQLException e) {
				getLogger().severe(ChatColor.DARK_RED + "[DatabaseManager] Error on connecting to database !");
				e.printStackTrace();
			}

		return null;
	}
	
	/**
	 * To disconnect the database connection
	 */
	public void disconnection() {
		if(isOnline()) {
			try {
				connection.close();
				getLogger().info(ChatColor.GREEN + "[DatabaseManager] Successfully disconnected!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * To know if the connection is enable
	 * @return true if connected
	 * @return false if disconnected
	 */
	public boolean isOnline() {
		try {
			return (connection != null) && (!connection.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
