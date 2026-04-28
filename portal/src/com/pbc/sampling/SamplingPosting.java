package com.pbc.sampling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;

public class SamplingPosting {
	Connection c;
	Datasource ds;
	
	public int ACCOUNT_PREFIX = 100000000;
	
	public SamplingPosting() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public void postNewAdvanceSampling(long SamplingID, long OutletID, long PostedBy, double amount, String remarks, long uvid) throws SQLException{
		Statement s = c.createStatement();
		
		double outlet_debit = amount;
		double outlet_credit = 0;
		
		double sampling_debit = 0;
		double sampling_credit = amount;
		
		long PostingID = 0;
		
		boolean isAlreadyPosted = false;
		
		ResultSet rs2 = s.executeQuery("select uvid from sampling_posting where uvid = "+uvid);
		if (rs2.first()){
			isAlreadyPosted = true;
		}
		
		if (isAlreadyPosted == false){
			
			c.setAutoCommit(false);
			
			s.executeUpdate("insert into sampling_posting (sampling_id, posting_id, posted_on, posted_by, remarks, uvid) values ("+SamplingID+", null, now(), "+PostedBy+", '"+remarks+"', "+uvid+")");
			
			ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
			if (rs.first()){
				PostingID = rs.getLong(1);
			}
			
			s.executeUpdate("insert into sampling_posting_accounts (posting_id, outlet_id, debit, credit, remarks, posting_type) values ("+PostingID+", "+OutletID+", "+outlet_debit+", "+outlet_credit+", '"+remarks+"', 1)");
			
			c.commit();
			
		}
		s.close();
	}
	
	public void postBalanceAdjustment(long OutletID, long PostedBy, int AdjustmentTypeID, double amount, String remarks) throws SQLException{
		Statement s = c.createStatement();
		
		String DefaultEffect = "D";
		ResultSet rs2 = s.executeQuery("select default_effect from sampling_posting_types where id = "+AdjustmentTypeID);
		if (rs2.first()){
			DefaultEffect = rs2.getString(1);
		}
		
		double outlet_debit = 0;
		double outlet_credit = 0;
		
		if (DefaultEffect.equals("D")){
			outlet_debit = amount;
		}else{
			outlet_credit = amount;
		}
		
		long PostingID = 0;
		
		s.executeUpdate("insert into sampling_posting (sampling_id, posting_id, posted_on, posted_by, remarks) values (0, null, now(), "+PostedBy+", '"+remarks+"')");
		
		ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
		if (rs.first()){
			PostingID = rs.getLong(1);
		}
		
		s.executeUpdate("insert into sampling_posting_accounts (posting_id, outlet_id, debit, credit, remarks, posting_type) values ("+PostingID+", "+OutletID+", "+outlet_debit+", "+outlet_credit+", '"+remarks+"', "+AdjustmentTypeID+")");
		
		s.close();
	}
	
	public void postMonthlyAdjustment(long ApprovalID, long SamplingID, long OutletID, long PostedBy, int AdjustmentTypeID, double amount, String remarks) throws SQLException{
		Statement s = c.createStatement();
		
		String DefaultEffect = "D";
		ResultSet rs2 = s.executeQuery("select default_effect from sampling_posting_types where id = "+AdjustmentTypeID);
		if (rs2.first()){
			DefaultEffect = rs2.getString(1);
		}
		
		double outlet_debit = 0;
		double outlet_credit = 0;
		
		if (DefaultEffect.equals("D")){
			outlet_debit = amount;
		}else{
			outlet_credit = amount;
		}
		
		long PostingID = 0;
		
		s.executeUpdate("insert into sampling_posting (sampling_id, posting_id, posted_on, posted_by, remarks) values ("+SamplingID+", null, now(), "+PostedBy+", '"+remarks+"')");
		
		ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
		if (rs.first()){
			PostingID = rs.getLong(1);
		}
		
		s.executeUpdate("insert into sampling_posting_accounts (posting_id, outlet_id, debit, credit, remarks, posting_type, approval_id) values ("+PostingID+", "+OutletID+", "+outlet_debit+", "+outlet_credit+", '"+remarks+"', "+AdjustmentTypeID+", "+ApprovalID+")");
		
		s.close();
	}
	
	public void deleteMonthlyAdjustment(long ApprovalID) throws SQLException{
		Statement s = c.createStatement();
		
		long ExistingPostingID = 0;
		ResultSet rs3 = s.executeQuery("select posting_id from sampling_posting_accounts where approval_id = "+ApprovalID);
		if (rs3.first()){
			ExistingPostingID = rs3.getLong(1);
		}
		
		if (ExistingPostingID > 0){
			s.executeUpdate("delete from sampling_posting where posting_id = "+ExistingPostingID);
			s.executeUpdate("delete from sampling_posting_accounts where posting_id = "+ExistingPostingID);
		}
		
		s.close();
	}
	
	
	public void postMonthlySampling(Date from, Date to, long UserID) throws SQLException{
		
		Statement s = c.createStatement();
		Statement s2 = c.createStatement();
		Statement s3 = c.createStatement();
		Statement s4 = c.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT s.request_id, s.sampling_id, s.outlet_id, s.outlet_name, s.business_type, s.address, s.region, s.asm, s.cr, s.market, s.vehicle, s.advance_company_share, s.advance_agency_share, s.advance_valid_from, s.advance_valid_to, s.fixed_company_share fixed_company_share, s.fixed_agency_share, s.fixed_deduction_term fixed_deduction_term, s.fixed_valid_from, s.fixed_valid_to, m.latitude, m.longitude FROM sampling s, outletmaster m, workflow_requests wr where s.outlet_id = m.outlet_id and s.request_id = wr.request_id and wr.status_id = 2");
		while (rs.next()){
			
			long RequestID = rs.getLong(1);
			long SamplingID = rs.getLong(2);
			long OutletID = rs.getLong(3);
			
			
			
			double OpeningBalance = 0;
			// Get Opening Balance
			
			ResultSet rs3 = s2.executeQuery("SELECT sum(debit)-sum(credit) from sampling_posting_accounts where outlet_id = "+OutletID);
			if (rs3.first()){
				OpeningBalance = rs3.getDouble(1);
			}
			
			
			double FixedCompanyShare = rs.getDouble("fixed_company_share");
			double FixedDeductionTerm = rs.getDouble("fixed_deduction_term");
			long PostingID = 0;
			
			s4.executeUpdate("insert into sampling_posting (sampling_id, posting_id, posted_on, posted_by, remarks) values ("+SamplingID+", null, now(), "+UserID+", 'Monthly Sampling Batch')");
			
			ResultSet rs5 = s4.executeQuery("select LAST_INSERT_ID()");
			if (rs5.first()){
				PostingID = rs.getLong(1);
			}
			
			
			if (FixedDeductionTerm != 0){
				s.executeUpdate("insert into sampling_posting_accounts (posting_id, outlet_id, debit, credit, remarks, posting_type) values ("+PostingID+", "+OutletID+", 0, "+FixedDeductionTerm+", 'Deduction againt Advance', 1)");
			}
			
			
			
			System.out.println(RequestID + " " + OutletID + " " + OpeningBalance);
			
			/*
			// One Time Posting
			ResultSet rs2 = s2.executeQuery("select package, agency_share, company_share, deduction_term, valid_from, valid_to from sampling_percase where sampling_id ="+rs.getString(2));
			while(rs2.next()){
				
			}
			
			rs2.close();
			*/
		}
		rs.close();
		
		s4.close();
		s3.close();
		s2.close();
		s.close();
		
	}	
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
