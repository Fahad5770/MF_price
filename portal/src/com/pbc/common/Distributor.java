package com.pbc.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;

public class Distributor {

	public int DISTRIBUTOR_ID;
	public String DISTRIBUTOR_NAME;
	
	public static double getLedgerBalance(long DistributorID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s2 = ds.createStatement();
		
		double CurrentBalance = 0;
		ResultSet rs4 = s2.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id in (SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id=1)");
		if( rs4.first() ){
			double TotalDebit = rs4.getDouble("total_debit");
			double TotalCredit = rs4.getDouble("total_credit");
			CurrentBalance = TotalDebit - TotalCredit;
		}
		
		s2.close();
		ds.dropConnection();
		
		return CurrentBalance;
	}

	public static double getLedgerBalanceBeforeOrderPosting(long DistributorID, long OrderPostingID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s2 = ds.createStatement();
		
		long TransactionID = 0;
		ResultSet rs2 = s2.executeQuery("select id from gl_transactions where order_posting_id = "+OrderPostingID);
		if (rs2.first()){
			TransactionID = rs2.getLong(1);
		}
		
		double CurrentBalance = 0;
		ResultSet rs4 = s2.executeQuery("SELECT sum(debit) total_debit, sum(credit) total_credit FROM gl_transactions_accounts where account_id in (SELECT id FROM gl_accounts where customer_id="+DistributorID+" and type_id=1) and id < "+TransactionID);
		if( rs4.first() ){
			double TotalDebit = rs4.getDouble("total_debit");
			double TotalCredit = rs4.getDouble("total_credit");
			CurrentBalance = TotalDebit - TotalCredit;
		}
		
		s2.close();
		ds.dropConnection();
		
		return CurrentBalance;
	}
	
}
