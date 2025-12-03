package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncKONV {

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
			ds.startTransaction();
			Statement s = ds.createStatement();
			
			JCoTable tab = obj.getKONV();
			
			//String x = obj.getHTMLTable(tab);
			//System.out.println(x);
			
			// tab[0]

			System.out.println("Synchronization started KONV");
			
			tab.firstRow();
			
			int countInserts = 0;
			int countUpdates = 0;
			
			
			
			
			
			s.executeUpdate("delete from sap_konv");
			
			for(int i = 0; i < tab.getNumRows(); i++){
				
				
				
				String knumv = Utilities.filterString(tab.getString("KNUMV"), 1, 100);
				String kposn = Utilities.filterString(tab.getString("KPOSN"), 1, 200);
				String kbetr = Utilities.filterString(tab.getString("KBETR"), 1, 200);
				
				
				countInserts++;
				
				
				
				s.executeUpdate("insert INTO sap_konv (knumv, kposn, kbetr) values ("+knumv+","+kposn+","+kbetr+")");
				
				
				tab.setRow(i+1);
				
			}
			
			System.out.println(countInserts+" Inserts, "+countUpdates + "Updates");
			System.out.println("Done!");
			
			ds.commit();
			
			s.close();
			
			ds.dropConnection();
			obj.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//syncGLAccounts();
		
	}
	
}
