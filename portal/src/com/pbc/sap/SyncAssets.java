package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;
import org.apache.commons.lang3.time.DateUtils;


public class SyncAssets {

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
			Statement s2 = ds.createStatement();
			JCoTable[] tab = obj.getAssetMaster();
			
			//System.out.println(obj.getHTMLTable(tab[0]));
			/*
			ERDAT
			AEDAT
			KTEXT
			ASTNR
			IDAT1
			IDAT2
			IDAT3
			CYCLE
			USER1
			USER2
			USER3
			USER4
			USER5
			USER6
			USER7
			USER8
			USER9
			ERFZEIT
			AEZEIT
			ZZSSOK
			ZZGTPS
			ZZWKOR
			ZZQTY
			ZZITEM_CATGY
			ZZITEM_SIZE
			
			
			// tab[0]
			
			*/
			System.out.println("Synchronzation started for ANLA");
			
			tab[0].firstRow();
			
			for(int i = 0; i < tab[0].getNumRows(); i++){
				
				long ANLN1 = tab[0].getLong("ANLN1"); // Main Asset Number
				long ANLN2 = tab[0].getLong("ANLN2"); // Asset Subnumber
				long ANLKL = tab[0].getLong("ANLKL"); // Asset Class
				Date ERDAT = tab[0].getDate("ERDAT"); // Date on Which Record Was Created
				long KTOGR = tab[0].getLong("KTOGR"); // Account determination
				double MENGE = Utilities.parseLong(tab[0].getString("MENGE")); // Quantity
				String INKEN =  Utilities.filterString(tab[0].getString("INKEN"), 1, 2000); // Inventory indicator
				String INVNR = Utilities.filterString(tab[0].getString("INVNR"), 1, 2000); // Inventory number
				String TXT50 = Utilities.filterString(tab[0].getString("TXT50"), 1, 2000); // Asset description
				String MCOA1 = Utilities.filterString(tab[0].getString("MCOA1"), 1, 2000); // Match code search
				String SERNR = Utilities.filterString(tab[0].getString("SERNR"), 1, 2000); // Serial number
				Date XV1DT = tab[0].getDate("XV1DT"); // Change date
				
				int INKEN_int = 0;
				if (INKEN != null && INKEN.equals("X")){
					INKEN_int = 1;
				}
				ResultSet rs = s2.executeQuery("select id from common_assets where main_asset_number = "+ANLN1+" and asset_subnumber = "+ANLN2);
				if (rs.first()){
					
				}else{
					s.executeUpdate("INSERT INTO `common_assets` (`main_asset_number`, `asset_subnumber`,`asset_class`,`created_on_sap`,`created_on`,`account_determination`,`quantity`,`inventory_indicator`,`inventory_number`,`asset_description`,`match_code_search`,`serial_number`,`changed_on_sap`) VALUES ("+ANLN1+","+ANLN2+","+ANLKL+","+Utilities.getSQLDate(ERDAT)+",now(),"+KTOGR+","+MENGE+","+INKEN_int+",'"+INVNR+"','"+TXT50+"','"+MCOA1+"','"+SERNR+"',"+Utilities.getSQLDate(XV1DT)+")");
				}
				tab[0].setRow(i+1);
			}
			// end tab[0]
			
			System.out.println("Synchronzation started for ANLU");
			
			//System.out.println(obj.getHTMLTable(tab[1]));
			
			tab[1].firstRow();
			
			for(int i = 0; i < tab[1].getNumRows(); i++){
				
				long ANLN1 = tab[1].getLong("ANLN1"); // Main Asset Number
				long ANLN2 = tab[1].getLong("ANLN2"); // Asset Subnumber
				String ZZSSOK = Utilities.filterString(tab[1].getString("ZZSSOK"),1,20); // Outlet ID
				String ZZSSOK_parsed = null; 
				
				ResultSet rs = s2.executeQuery("select id from common_outlets where id = '"+ZZSSOK+"'");
				if (rs.first()){
					ZZSSOK_parsed = rs.getString(1);
				}
				
				String ZZAGR_REC = Utilities.filterString(tab[1].getString("ZZAGR_REC"),1,20); // Agreement Record
				String ZZRND_VERI = Utilities.filterString(tab[1].getString("ZZRND_VERI"),1,20); // R&D Verification
				int ZZSTD = tab[1].getInt("ZZSTD"); // 250 ML Quantity
				int ZZLTR = tab[1].getInt("ZZLTR"); // Litre Quantity
				String ZZTOT_STATUS = Utilities.filterString(tab[1].getString("ZZTOT_STATUS"),1,100); // TOT Status
				String ZDATE = Utilities.filterString(tab[1].getString("ZDATE"),1,20); // Movement Date
				
				Date ZDATE_parsed = Utilities.parseDateYYYYMMDD(ZDATE);
				
				s.executeUpdate("update common_assets set outlet_id = '"+ZZSSOK+"', outlet_id_parsed = "+ZZSSOK_parsed+", agreement_record = '"+ZZAGR_REC+"', rnd_verification = '"+ZZRND_VERI+"', 250ml_quantity = "+ZZSTD+", litre_quantity = "+ZZLTR+", tot_status = '"+ZZTOT_STATUS+"', movement_date = '"+ZDATE+"', movement_date_parsed = "+Utilities.getSQLDate(ZDATE_parsed)+" where main_asset_number = "+ANLN1+" and asset_subnumber = "+ANLN2);
				
				
				try{
					s.executeUpdate("insert into common_assets_tot_log (main_asset_number, asset_subnumber, outlet_id, outlet_id_parsed, tot_status, movement_date, movement_date_parsed, created_on) values ("+ANLN1+", "+ANLN2+", '"+ZZSSOK+"', "+ZZSSOK_parsed+", '"+ZZTOT_STATUS+"', '"+ZDATE+"', "+Utilities.getSQLDate(ZDATE_parsed)+", now())");
				}catch(Exception e){
					
				}
				
				
				tab[1].setRow(i+1);
			}
			// end tab[0]
			
			 
			System.out.println("Done");
			s2.close();
			s.close();
			ds.dropConnection();			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
