package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.Statement;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncOutlets {

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
			
			JCoTable tab = obj.getOutletList();

			System.out.println("Synchronization started for Outlets");
			
			tab.firstRow();
			
			int countInserts = 0;
			int countUpdates = 0;
			
			for(int i = 0; i < tab.getNumRows(); i++){
				
				long OutletID = Utilities.parseLong(tab.getString("SSOK"));
				
				if (OutletID == 83709){
					System.out.println(OutletID);
				}
				
				
				if (OutletID != 0){
				
					long DistributorID = Utilities.parseLong(tab.getString("KUNNR"));
					String BusinessName = Utilities.filterString(tab.getString("BSNAME"), 1, 100);
					long BusinessID = Utilities.parseLong(tab.getString("BSID"));
					String OutletName = Utilities.filterString(tab.getString("ONAME"), 1, 100);
					String OutletFullName = OutletName + " " + BusinessName;
					String OwnerName = Utilities.filterString(tab.getString("OWNAME"), 1, 100);
					String StreetAddress = Utilities.filterString(tab.getString("STRAS"), 1, 100);
					String FirstPhoneNumber = Utilities.filterString(tab.getString("TELF1"), 1, 100);
					String OwnerNIC = Utilities.filterString(tab.getString("OWNID"), 1, 100);
					int status = Utilities.parseInt(tab.getString("OSTACT"));
					String Region = Utilities.filterString(tab.getString("BLAND"), 1, 100);
					
					String slat = tab.getString("LAT");
					String slng = tab.getString("LNG");
					
					if (slat != null && slat.length() > 22){
						slat = slat.substring(0, 22);
					}
					if (slng != null && slng.length() > 22){
						slng = slng.substring(0, 22);
					}
					
					
					double lat = Utilities.parseDouble(slat);
					double lng = Utilities.parseDouble(slng);
					
					if (lat > 999){
						lat = 0;
					}
					if (lng > 999){
						lng = 0;
					}
					
					
					
					
					
					boolean isRegionValid = false;
					int RegionID = 0;
					ResultSet rs = s.executeQuery("select region_id from common_regions where region_short_name = '"+Region+"'");
					if(rs.first()) {
						isRegionValid = true;
						RegionID = rs.getInt("region_id");
					}
					
					if (isRegionValid == true){
						
						boolean isDistributorValid = false;
						ResultSet rs2 = s.executeQuery("select distributor_id from common_distributors where distributor_id = "+DistributorID);
						if(rs2.first()) {
							isDistributorValid = true;
						}
						
						if (isDistributorValid == true){
						
							
							
							ResultSet rs9 = s.executeQuery("select label from common_outlets_types where id = "+BusinessID);
							if (rs9.first()){
								
							}else{
								s2.executeUpdate("insert into common_outlets_types (id, label) values ("+BusinessID+", '"+BusinessName+"')");
							}
							
							boolean AlreadyExists = false;
							
							ResultSet rs3 = s.executeQuery("select id from common_outlets where id = "+OutletID);
							if (rs3.first()){
								AlreadyExists = true;
							}					
							
							if (AlreadyExists == true){ // update case
								String DeactivationQuery = "";
								
								if (status == 0){
									DeactivationQuery = ", deactivated_on = now(), deactivated_by = 1";
								}
								
								//s.executeUpdate("update common_outlets set name = '"+OutletFullName+"', address = '"+StreetAddress+"', region_id = "+RegionID+", distributor_id = "+DistributorID+", lat = "+lat+", lng = "+lng+", is_active = "+status+" "+DeactivationQuery+" where id = "+OutletID);
								
								
								
								//s.executeUpdate("update common_outlets set name = '"+OutletFullName+"', address = '"+StreetAddress+"', region_id = "+RegionID+", distributor_id = "+DistributorID+", type_id = "+BusinessID+" ,lat ="+lat+", lng = "+lng+", is_active = "+status+" "+DeactivationQuery+" where id = "+OutletID);
								
								s.executeUpdate("update common_outlets set name = '"+OutletFullName+"', address = '"+StreetAddress+"', region_id = "+RegionID+", distributor_id = "+DistributorID+", type_id = "+BusinessID+", is_active = "+status+" "+DeactivationQuery+" where id = "+OutletID);
								s.executeUpdate("update outletmaster set outlet_name = '"+OutletName+"', bsi_name = '"+BusinessName+"', owner = '"+OwnerName+"', telepohone = '"+FirstPhoneNumber+"'  where outlet_id = "+OutletID);
								
								s.executeUpdate("update common_outlets_contacts set contact_name = '"+OwnerName+"', contact_nic = '"+OwnerNIC+"', contact_number = '"+FirstPhoneNumber+"' where outlet_id = "+OutletID+" and type_id = 5");
								//s.executeUpdate("delete from common_outlets_distributors where outlet_id = "+OutletID);
								//s.executeUpdate("insert into common_outlets_distributors (outlet_id, distributor_id, created_on, created_by) values ("+OutletID+", '"+DistributorID+"', now(), 1)");
								
								/*
								long ContactID = 0;
								ResultSet rs5 = s.executeQuery("select id from common_outlets_contacts where outlet_id = "+OutletID+" and type_id = 5");
								if(rs5.first()){
									ContactID = rs5.getLong(1);
								}
								*/
								//s.executeUpdate("update common_outlets_contacts_numbers set contact_number = '"+FirstPhoneNumber+"' where contact_id = "+ContactID);
								
								countUpdates++;
							}else{ // insert case
								/*
								if (OutletID == 83709){
									System.out.println("insert into common_outlets (id, name, address, region_id, distributor_id, lat, lng, is_active, created_on, created_by) values ("+OutletID+", '"+OutletFullName+"', '"+StreetAddress+"', "+RegionID+", "+DistributorID+", "+lat+", "+lng+", "+status+", now(), 1)");
								}*/
								////////////////////////////////s.executeUpdate("insert into common_outlets (id, name, address, region_id, distributor_id, lat, lng, type_id, is_active, created_on, created_by) values ("+OutletID+", '"+OutletFullName+"', '"+StreetAddress+"', "+RegionID+", "+DistributorID+", "+lat+", "+lng+", "+BusinessID+","+status+", now(), 1)");
								
								////////////////////////////////s.executeUpdate("insert into common_outlets_contacts (outlet_id, contact_name, contact_nic, type_id, is_primary, contact_number) values ("+OutletID+", '"+OwnerName+"', '"+OwnerNIC+"', 5, 1, '"+FirstPhoneNumber+"')");
								
								//////////////////////////////////s.executeUpdate("delete from common_outlets_distributors where outlet_id = "+OutletID);
								//////////////////////////////s.executeUpdate("insert into common_outlets_distributors (outlet_id, distributor_id, created_on, created_by) values ("+OutletID+", '"+DistributorID+"', now(), 1)");
								
								/*long ContactID = 0;
								ResultSet rs4 = s.executeQuery("select LAST_INSERT_ID()");
								if(rs4.first()){
									ContactID = rs4.getLong(1); 
								}
								*/
								countInserts++;
							}
						}else{
							
							System.out.println("Invalid Distributor `"+tab.getString("KUNNR")+"` for Outlet: "+OutletID);
						}
						
					}else{
						System.out.println("Invalid Region `"+Region+"` for Outlet: "+OutletID);
					}
				
				}else{
					System.out.println("Invalid OutletID: "+tab.getString("SSOK"));
				}

				tab.setRow(i+1);
			}
			
			System.out.println(countInserts+" Inserts, "+countUpdates + "Updates");
			
			System.out.println("Done.");
			s2.close();
			s.close();
			ds.dropConnection();
			obj.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print(e);
			e.printStackTrace();
		}
		
		
	}

}
