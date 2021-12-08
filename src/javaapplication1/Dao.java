package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;
	String ticketsTableName = "gtole_tickets_testing3";
	String usersTableName = "gtole_users_testing";

	// constructor
	public Dao() {

	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE " + ticketsTableName
				+ "(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), ticket_status VARCHAR(30))";
		final String createUsersTable = "CREATE TABLE " + usersTableName
				+ "(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into " + usersTableName + "(uname,upass,admin) " + "values('" + rowData.get(0) + "',"
						+ " '" + rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc) {
		System.out.println("TName: " + ticketName);
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate(
					"Insert into " + ticketsTableName + "(ticket_issuer, ticket_description, ticket_status) values("
							+ " '" + ticketName + "', '" + ticketDesc + "', 'open');",
					Statement.RETURN_GENERATED_KEYS); // ticket status is open when inserting new records

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}

	public ResultSet readRecords(Boolean chckIfAdmin, String uname) {
		String query = "";
		try {
			statement = getConnection().createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (chckIfAdmin == true) {
			query = "SELECT * FROM " + ticketsTableName;
		} else
			query = "SELECT * FROM " + ticketsTableName + " WHERE ticket_issuer = '" + uname + "';";
		ResultSet results = null;
		try {
			results = statement.executeQuery(query);
			// connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

	// continue coding for updateRecords implementation
	public int updateRecords(int id, String ticketDesc) {
		String query = "UPDATE " + ticketsTableName + " SET ticket_description = '" + ticketDesc
				+ "' WHERE ticket_id = ?;";
		try (PreparedStatement stmt = statement.getConnection().prepareStatement(query)) {
			stmt.setInt(1, id);
			int rs = stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;

	}

	// continue coding for deleteRecords implementation
	public int deleteTicket(int id) {
		String query = "Delete from " + ticketsTableName + " WHERE ticket_id = ?;";
		try (PreparedStatement stmt = statement.getConnection().prepareStatement(query)) {
			stmt.setInt(1, id);
			int rs = stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;

	}

	// continue coding for closeRecords implementation
	public int closeTicket(int id) {
		String query = "UPDATE " + ticketsTableName + " SET ticket_status = 'close' WHERE ticket_id = ?;";
		try (PreparedStatement stmt = statement.getConnection().prepareStatement(query)) {
			stmt.setInt(1, id);
			int rs = stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;

	}
}
