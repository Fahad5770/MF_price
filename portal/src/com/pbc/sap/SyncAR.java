package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncAR {

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
			
			JCoTable tab = obj.getARMaster();
			
			String x = obj.getHTMLTable(tab);
			//System.out.println(x);
			
			// tab[0]

			System.out.println("Synchronization started for Distributors:");
			
			tab.firstRow();
			
			int countInserts = 0;
			int countUpdates = 0;
			
			for(int i = 0; i < tab.getNumRows(); i++){
				
				
				
				
				
				String DistributorID = Utilities.filterString(tab.getString("KUNNR"), 1, 100);
				
				
				
				boolean isKeyAccount = false;
				
				if ((Utilities.parseLong(DistributorID)+"").startsWith("2")){
					isKeyAccount = true;
				}
				
				
				
				
				String Name = Utilities.filterString(tab.getString("NAME1"), 1, 200);
				String Name2 = Utilities.filterString(tab.getString("NAME2"), 1, 200);
				String City = Utilities.filterString(tab.getString("ORT01"), 1, 100);
				String RegionIDShortName = Utilities.filterString(tab.getString("REGIO"), 1, 100);
				String Route = Utilities.filterString(tab.getString("SORTL"), 1, 100);
				String Address = Utilities.filterString(tab.getString("STRAS"), 1, 255);
				String ContactNumber = Utilities.filterString(tab.getString("TELF1"), 1, 100);
				
				String AUFSD = Utilities.filterString(tab.getString("AUFSD"), 1, 100); // Order Block
				String LIFSD = Utilities.filterString(tab.getString("LIFSD"), 1, 100); // Delivery Block
				String FAKSD = Utilities.filterString(tab.getString("FAKSD"), 1, 100); // Billing Block
				String CASSD = Utilities.filterString(tab.getString("CASSD"), 1, 100); // Central Block
				
				int KUKLA = tab.getInt("KUKLA"); // Customer Classification
				
				
				String sKUKLA = "11";
				if (KUKLA != 0){
					sKUKLA = KUKLA+"";
				}
				
				
				int AUFSDinsert = 0;
				int LIFSDinsert = 0;
				int FAKSDinsert = 0;
				int CASSDinsert = 0;
				
				if (AUFSD.length() > 0){
					AUFSDinsert = 1;
				}
				if (LIFSD.length() > 0){
					LIFSDinsert = 1;
				}
				if (FAKSD.length() > 0){
					FAKSDinsert = 1;
				}
				if (CASSD.length() > 0){
					CASSDinsert = 1;
				}
				
				int RegionID=0;
				//getting region id against region short name
				ResultSet rs = s.executeQuery("select region_id from common_regions where region_short_name = '"+RegionIDShortName+"'");
				//System.out.println("select region_id from common_regions where region_short_name = '"+RegionIDShortName+"'");
				if(rs.first())
				{
					RegionID = rs.getInt("region_id");
				}
				else
				{
					RegionID = 9;
				}
				
				boolean AlreadyExists = false;
				
				ResultSet rs2 = s.executeQuery("select distributor_id from common_distributors where distributor_id = "+DistributorID);
				if (rs2.first()){
					AlreadyExists = true;
				}
				
				if (AlreadyExists == false){
					countInserts++;
					s.executeUpdate("insert INTO common_distributors(distributor_id,name,name2,city,region_id,route,address,contact_no, created_on,is_order_blocked,is_delivery_blocked,is_billing_blocked,is_central_blocked,category_id) VALUES("+DistributorID+",'"+Name+"','"+Name2+"','"+City+"',"+RegionID+",'"+Route+"','"+Address+"','"+ContactNumber+"', now(),"+AUFSDinsert+","+LIFSDinsert+","+FAKSDinsert+","+CASSDinsert+","+sKUKLA+")");
				}else{
					countUpdates++;
					s.executeUpdate("update common_distributors set name = '"+Name+"', name2 = '"+Name2+"', city='"+City+"', region_id = "+RegionID + ", route = '"+Route+"', address = '"+Address+"', contact_no = '"+ContactNumber+"', is_order_blocked = "+AUFSDinsert+", is_delivery_blocked = "+LIFSDinsert+", is_billing_blocked = "+FAKSDinsert+", is_central_blocked = "+CASSDinsert+", category_id = "+sKUKLA+" where distributor_id = "+DistributorID);
				}
				
				
				if (isKeyAccount){
					
					//s.executeUpdate("update common_distributors set type_id = 3 where distributor_id = "+DistributorID);
				}
				
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
		syncGLAccounts();
		
	}
	public static void syncGLAccounts(){
		
		Datasource ds = new Datasource();
		
		try{
			
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			
			ResultSet rs = s.executeQuery("select distributor_id, name from common_distributors");
			while(rs.next()){
				
				long DistributorID = rs.getLong(1);
				String DistributorName = rs.getString(2);
				
				
				for (int i  = 1; i <= 7; i++){
					ResultSet rs2 = s2.executeQuery("select id from gl_accounts where customer_id = "+DistributorID+" and type_id = "+i);
					if(rs2.first()){
						
					}else{
						s3.executeUpdate("insert into gl_accounts (label, category_id, type_id, customer_id) values ('"+DistributorName+"', 1, "+i+", "+DistributorID+")");
					}
				}
				
			}
			
			ds.commit();
			
			s.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
