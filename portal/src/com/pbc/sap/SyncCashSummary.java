package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncCashSummary {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SAPUtilities obj = new SAPUtilities();
		obj.connectPRD();
		
		try {
			
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			for (int i = 0; i < 7; i++){
			
				Date YesterdayDate = Utilities.getDateByDays((1+i)*-1);
				
				
				double ledger = 0;
				double credit = 0;
				double security = 0;
				double vehicle = 0;
				double empty = 0;
				
				ResultSet rs5 = s.executeQuery("SELECT glcr.receipt_type, sum(glcri.amount) total_amount FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri"+
						" on glcr.id=glcri.id where created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.warehouse_id = 1 and glcri.instrument_id = 1 group by glcr.receipt_type ");
				
				while(rs5.next()){
					
					
					int type = rs5.getInt("receipt_type");
					
					if (type == 1){
						ledger = rs5.getDouble(2);
					}else if (type == 2){
						security = rs5.getDouble(2);
					}else if (type == 3){
						credit = rs5.getDouble(2);
					}else if (type == 4){
						vehicle = rs5.getDouble(2);
					}else if (type == 5){
						empty = rs5.getDouble(2);
					}
					
				}
				
				obj.setCashSummary(YesterdayDate, ledger, credit, security, vehicle, empty);
				
				Thread.currentThread().sleep(5000);
				
			}
			
			System.out.println("Done!");
			s.close();
			ds.dropConnection();
			obj.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
