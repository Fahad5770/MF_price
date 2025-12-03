package com.pbc.sap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncGLPrimary {

	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
//		
		//SyncInvoices();
		//SyncOrders();
		
		
	}
	
	public static void SyncInvoices() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		System.out.println("Invoices:");
		
		Datasource ds = new Datasource();
		ds.createConnectionToSAPDB();
		Connection c = ds.getConnection();
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		Statement s3 = c.createStatement();


		Datasource dsMySQL = new Datasource();
		dsMySQL.createConnection();
		Statement sMySQL = dsMySQL.createStatement();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date StartDate = cal.getTime();
		
		Date EndDate = new Date();
		
		
		try {
			
			dsMySQL.startTransaction();
			String Query = "select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.kunag /*Customer*/, kna1.name1, vbrk.fksto /*is cancelled */ from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr where ((vbrk.kurrf_dat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+") or (vbrk.aedat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+") or (vbrk.fkdat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+") or (vbrk.erdat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+"))";
			//System.out.println(Query);
			ResultSet rs = s.executeQuery(Query);
			
			String TransactionDate = null;
			String BillingDate =  null;
			String ChangedOn = null;
			long DocumentID =0;
			double InvoiceAmount=0;
			long CustomerNumber = 0;
			String CustomerName = "";
			String IsCancelled = "";
			long GlTransactionsID = 0;
			long AccountIDForDebit = 0;
			long AccountIDForCredit = 0;
			boolean isAlreadyEntryExist = false;
			
			
			int InsertCounter=0;
			int UpdateCounter=0;
			int DeleteCounter=0;
			
			while(rs.next()){
				TransactionDate = Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("kurrf_dat")));
				BillingDate =  Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("fkdat")));
				ChangedOn = Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("aedat")));
				DocumentID =rs.getLong("vbeln");
				InvoiceAmount=rs.getDouble("invoice_amount");
				CustomerNumber = rs.getLong("kunag");
				CustomerName = rs.getString("name1");	
				IsCancelled = rs.getString("fksto");
				 
				ResultSet rs2 = sMySQL.executeQuery("select id from gl_accounts where type_id=2 and customer_id="+CustomerNumber);
				if(rs2.first()){
					AccountIDForDebit = rs2.getLong("id");
				}
				ResultSet rs3 = sMySQL.executeQuery("select id from gl_accounts where type_id=2 and category_id=4");
				if(rs3.first()){
					AccountIDForCredit = rs3.getLong("id");
				}
				 
				 //checking entry already exist or not
				 
				 ResultSet rs4 = sMySQL.executeQuery("select * from gl_transactions where document_id="+DocumentID);
					if(rs4.first()){
						isAlreadyEntryExist = true;
					}else{
						isAlreadyEntryExist = false;
					}
					
				 if(!isAlreadyEntryExist){
					 
					 if(!IsCancelled.equals("X")){
						 
						 String InsertionQuery = "insert into gl_transactions(document_id,document_type_id,document_date,remarks,created_on,created_on_date,created_by,sap_invoice_no) values("+DocumentID+",51,"+BillingDate+",'Invoice# "+DocumentID+" of "+CustomerNumber+" "+CustomerName+"',now(),curdate(),1,"+DocumentID+")";
						 
						 sMySQL.executeUpdate(InsertionQuery);
						 InsertCounter++;
						 
						 ResultSet rs1 = sMySQL.executeQuery("select LAST_INSERT_ID()");
						 if(rs1.first()){
							GlTransactionsID = rs1.getInt(1);
						 }
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+GlTransactionsID+","+AccountIDForDebit+","+InvoiceAmount+",0,'Invoice# "+DocumentID+"')");
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+GlTransactionsID+","+AccountIDForCredit+",0,"+InvoiceAmount+",'Invoice# "+DocumentID+"')");
							
							
					 }
					 
				 }else{
					 
					 //get id from master table against the sap invoice number/document id then update the document date against document id 
					 //and delete the sub table records against that document ID and then insert them again
					 long MasterTableID =0;
					 
					 ResultSet rs5 = sMySQL.executeQuery("select id from gl_transactions where sap_invoice_no="+DocumentID);
					 if(rs5.first()){
						 MasterTableID = rs5.getLong("id");
					 }
					 if(!IsCancelled.equals("X")){
						 sMySQL.executeUpdate("update gl_transactions set document_date="+BillingDate+" where id="+MasterTableID);
						 UpdateCounter++;
						 
						 sMySQL.executeUpdate("delete from gl_transactions_accounts where id="+MasterTableID);
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+MasterTableID+","+AccountIDForDebit+","+InvoiceAmount+",0,'Invoice# "+DocumentID+"')");
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+MasterTableID+","+AccountIDForCredit+",0,"+InvoiceAmount+",'Invoice# "+DocumentID+"')");
						
					 }else{
						 sMySQL.executeUpdate("delete from gl_transactions where id="+MasterTableID);
						 sMySQL.executeUpdate("delete from gl_transactions_accounts where id="+MasterTableID);
					 }
				 }
			}
			
			dsMySQL.commit();
			s.close();
			sMySQL.close();
			
			System.out.println("Done");
			System.out.println(InsertCounter+" Records inserted");
			System.out.println(UpdateCounter+" Records Updated");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				dsMySQL.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
			
		}finally {
			
			try {
				if (dsMySQL != null){
					dsMySQL.dropConnection();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	public static void SyncOrders() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		System.out.println("Orders:");
		
		Datasource ds = new Datasource();
		ds.createConnectionToSAPDB();
		Connection c = ds.getConnection();
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		Statement s3 = c.createStatement();


		Datasource dsMySQL = new Datasource();
		dsMySQL.createConnection();
		Statement sMySQL = dsMySQL.createStatement();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date StartDate = cal.getTime();
		
		Date EndDate = new Date();
		
		
		try {
			
			dsMySQL.startTransaction();
			//String Query = "select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.kunag /*Customer*/, kna1.name1, vbrk.fksto /*is cancelled */ from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr where ((vbrk.kurrf_dat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+") or (vbrk.aedat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+") or (vbrk.fkdat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+") or (vbrk.erdat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+"))";
			String Query = "SELECT vbak.vbeln, vbak.kunnr, vbak.erdat, vbak.audat, vbak.aedat, (vbak.netwr + (select sum(mwsbp) from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, kna1.name1 FROM "+ds.getSAPDatabaseAlias()+".vbak vbak join "+ds.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join "+ds.getSAPDatabaseAlias()+".kna1 kna1 on vbak.kunnr = kna1.kunnr where vbak.auart in ('ZDIS', 'ZMRS', 'ZCLA', 'ZSAM', 'ZCAN', 'ZTGT', 'ZOSM','ZDFR') and (vbak.erdat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+" or vbak.audat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+" or vbak.aedat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+" ) and vbuk.vbtyp = 'C'";
			//System.out.println(Query);
			ResultSet rs = s.executeQuery(Query);
			
			String TransactionDate = null;
			String OrderDate =  null;
			String ChangedOn = null;
			long DocumentID =0;
			double InvoiceAmount=0;
			long CustomerNumber = 0;
			String CustomerName = "";
			String IsCancelled = "";
			long GlTransactionsID = 0;
			long AccountIDForDebit = 0;
			long AccountIDForCredit = 0;
			boolean isAlreadyEntryExist = false;
			
			
			int InsertCounter=0;
			int UpdateCounter=0;
			int DeleteCounter=0;
			
			while(rs.next()){
				TransactionDate = Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("erdat")));
				OrderDate =  Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("audat")));
				ChangedOn = Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("aedat")));
				DocumentID =rs.getLong("vbeln");
				InvoiceAmount=rs.getDouble("order_amount");
				CustomerNumber = rs.getLong("kunnr");
				CustomerName = rs.getString("name1");	

				 
				ResultSet rs2 = sMySQL.executeQuery("select id from gl_accounts where type_id=1 and customer_id="+CustomerNumber);
				if(rs2.first()){
					AccountIDForDebit = rs2.getLong("id");
				}
				ResultSet rs3 = sMySQL.executeQuery("select id from gl_accounts where type_id=1 and category_id=4");
				if(rs3.first()){
					AccountIDForCredit = rs3.getLong("id");
				}
				 
				 //checking entry already exist or not
				 
				 ResultSet rs4 = sMySQL.executeQuery("select * from gl_transactions where sap_order_no="+DocumentID);
					if(rs4.first()){
						isAlreadyEntryExist = true;
					}else{
						isAlreadyEntryExist = false;
					}
					
				 if(!isAlreadyEntryExist){
					 
					 if(!IsCancelled.equals("X")){
						 
						 String InsertionQuery = "insert into gl_transactions(document_id,document_type_id,document_date,remarks,created_on,created_on_date,created_by,sap_order_no) values("+DocumentID+",52,"+OrderDate+",'Order# "+DocumentID+" of "+CustomerNumber+" "+CustomerName+"',now(),curdate(),1,"+DocumentID+")";
						 
						 sMySQL.executeUpdate(InsertionQuery);
						 InsertCounter++;
						 
						 ResultSet rs1 = sMySQL.executeQuery("select LAST_INSERT_ID()");
						 if(rs1.first()){
							GlTransactionsID = rs1.getInt(1);
						 }
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+GlTransactionsID+","+AccountIDForDebit+","+InvoiceAmount+",0,'Order# "+DocumentID+"')");
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+GlTransactionsID+","+AccountIDForCredit+",0,"+InvoiceAmount+",'Order# "+DocumentID+"')");
							
							
					 }
					 
				 }else{
					 
					 //get id from master table against the sap invoice number/document id then update the document date against document id 
					 //and delete the sub table records against that document ID and then insert them again
					 long MasterTableID =0;
					 
					 ResultSet rs5 = sMySQL.executeQuery("select id from gl_transactions where sap_order_no="+DocumentID);
					 if(rs5.first()){
						 MasterTableID = rs5.getLong("id");
					 }
					 if(!IsCancelled.equals("X")){
						 sMySQL.executeUpdate("update gl_transactions set document_date="+OrderDate+" where id="+MasterTableID);
						 UpdateCounter++;
						 
						 sMySQL.executeUpdate("delete from gl_transactions_accounts where id="+MasterTableID);
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+MasterTableID+","+AccountIDForDebit+","+InvoiceAmount+",0,'Order# "+DocumentID+"')");
						 sMySQL.executeUpdate("insert into gl_transactions_accounts(id,account_id,debit,credit,remarks) values("+MasterTableID+","+AccountIDForCredit+",0,"+InvoiceAmount+",'Order# "+DocumentID+"')");
						
					 }else{
						 sMySQL.executeUpdate("delete from gl_transactions where id="+MasterTableID);
						 sMySQL.executeUpdate("delete from gl_transactions_accounts where id="+MasterTableID);
					 }
				 }
			}
			
			dsMySQL.commit();
			s.close();
			sMySQL.close();
			
			System.out.println("Done");
			System.out.println(InsertCounter+" Records inserted");
			System.out.println(UpdateCounter+" Records Updated");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				dsMySQL.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
			
		}finally {
			
			try {
				if (dsMySQL != null){
					dsMySQL.dropConnection();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
