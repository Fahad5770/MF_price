package com.pbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * It provides data sources for MySQL Master Server, Replication Server and SAP Oracle Database
 * @author PBC
 *
 */
public class Datasource {
	
	public Connection c;
	
	/**
	 * To connect master data source of Theia
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnection() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
//	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
	//c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/pep", "admin", "a1m01z");
	//	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "toor");
		
		
		
	}
	
	
	/**
	 * To connect master data source of KSML
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnectionKSML() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
//		c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
	//c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/pep", "admin", "a1m01z");
	//c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "toor");

	}
	
	/**
	 * To connect replication server of Theia
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnectionToReplica() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
	//	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
	//	c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
		
//		c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/pep", "admin", "a1m01z");
	//	 c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/pep", "admin", "a1m01z");
	//	//c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "toor");
		
		
		
	}
	
	/**
	 * To connect replication server of Theia
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnectionToReplica2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
	//	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
	//	c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/pep", "admin", "a1m01z");
	//	c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
		//c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "toor");
	
		
	}
	
	/**
	 * To connect master server of Oracle behind SAP
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnectionToSAPDB() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		//Class.forName("com.mysql.jdbc.Driver").newInstance();
		DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
		c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");;
//	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
		//c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
	}
	
	/**
	 * To connect master server of KSML LAB
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnectionToKSMLLab() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
	//	c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
	//	c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/canelab", "admin", "a1m01z");
		
	}
	
	/**
	 * To connect MRD database server
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void createConnectionToMRD() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
	//c = DriverManager.getConnection("jdbc:mysql://localhost:3306/pep", "root", "Pbc@1472");
	//	c = DriverManager.getConnection("jdbc:mysql://13.251.213.226/pep", "admin", "a1m01z");
	//	c = DriverManager.getConnection("jdbc:mysql://192.169.188.163/pbc", "admin", "a1m01z");
		
	}

	
	
	/**
	 * To get the connection object
	 * @return
	 */
	public Connection getConnection(){
		return c;
	}
	
	/**
	 * Starts the transaction and sets AutoCommit to false
	 * @throws SQLException
	 */
	public void startTransaction() throws SQLException {
		if (c != null){
			c.setAutoCommit(false);
		}
	}
	
	/**
	 * Commits the transaction
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		if (c != null){
			c.commit();
		}
	}
	
	/**
	 * Rollbacks the uncommitted transaction
	 * @throws SQLException
	 */
	public void rollback() throws SQLException{
		if (c != null){
			c.rollback();
		}
	}
	
	/**
	 * Creates a new statement object after connection is made
	 * @return
	 * @throws SQLException
	 */
	public Statement createStatement() throws SQLException {
		if (c != null){
			return c.createStatement();
		}else{
			return null;
		}
	}
	
	/**
	 * To get the alias of database of logs
	 * @return
	 */
	public String logDatabaseName(){
		return "peplogs";
		//return "almoiz_peplogs";
		
	}
	
	/**
	 * To get the alias of SAP instance in Oracle
	 * @return
	 */
	public String getSAPDatabaseAlias(){
		
		return "sapsr3";
		
	}
	
	/**
	 * To close the connection
	 * @throws SQLException
	 */
	public void dropConnection() throws SQLException {
		if (c != null){
			c.close();
		}
	}
public String getDatabaseName(){
		
		return "pep";
		
	}
}
