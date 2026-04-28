package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncDistributorStatement {

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
			
			Date FromDate = Utilities.parseDate("01/01/2017");
			Date ToDate = Utilities.parseDate("30/04/2017");
			
			JCoTable tab = obj.getDistributorStatement(100914, FromDate, ToDate);
			
			System.out.println("Synchronization started for Distributor Statement");
			
			tab.firstRow();
			
			int countInserts = 0;
			int countUpdates = 0;
			
			for(int i = 0; i <= tab.getNumRows(); i++){
				
				//String DistributorID = Utilities.filterString(tab.getString("KUNNR"), 1, 100);
				
				//String Name = Utilities.filterString(tab.getString("NAME1"), 1, 200);
				
				//INC_BAL
				//ATAX_BAL
				//CRSAL_BAL
				//VEH_BAL
				
				double INC_BAL = tab.getDouble("INC_BAL");
				double ATAX_BAL = tab.getDouble("ATAX_BAL");
				double CRSAL_BAL = tab.getDouble("CRSAL_BAL");
				double VEH_BAL = tab.getDouble("VEH_BAL");
				
				System.out.println(INC_BAL);
				System.out.println(ATAX_BAL);
				System.out.println(CRSAL_BAL);
				System.out.println(VEH_BAL);
				
				tab.setRow(i+1);
				
			}
			
			System.out.println(countInserts+" Inserts, "+countUpdates + "Updates");
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
