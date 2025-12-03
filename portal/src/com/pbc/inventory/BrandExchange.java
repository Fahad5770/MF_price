package com.pbc.inventory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.pbc.common.DocumentHeader;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.workflow.WorkflowDocument;

public class BrandExchange {
	
	long BrandExchangeID;
	
	Connection c;
	Datasource ds;	
	
	public BrandExchange() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public void setDamagedStockID(long DamagedStockID) throws SQLException{
		this.BrandExchangeID = DamagedStockID;
		
	}
	
	public String getPackageList() throws SQLException{
		
		Statement s = c.createStatement();

		String PackageOptions = "<select id='BrandExchangePackage' name='BrandExchangePackage' data-mini='true' ><option value=''>Select Package</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_packages order by sort_order");
		while( rs.next() ){
			PackageOptions += "<option value='"+rs.getInt("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		PackageOptions += "</select>";
		
		s.close();
		return PackageOptions;
	}
	public String getPackageListPromotions() throws SQLException{
		
		Statement s = c.createStatement();

		String PackageOptions = "<select id='ProductPromotionPPackage' name='BrandExchangePackage' data-mini='true' ><option value=''>Select Package</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_packages order by sort_order");
		while( rs.next() ){
			PackageOptions += "<option value='"+rs.getInt("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		PackageOptions += "</select>";
		
		s.close();
		return PackageOptions;
	}	
	public String getBrandList() throws SQLException{
		
		Statement s = c.createStatement();

		String BrandOptions = "";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_brands order by label");
		while( rs.next() ){
			BrandOptions += "<option value='"+rs.getInt("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		s.close();
		return BrandOptions;
	}
	
	
	
	public DocumentHeader[] getDocumentList(Date FromDate, Date ToDate, long Distributor, String Remarks) throws SQLException{
		
		List <DocumentHeader>list = new ArrayList<DocumentHeader>();
		int num = 0;
		
		String ExtraClauses = "";
		
		try{
			if(FromDate == null || ToDate == null ){
				FromDate = new java.util.Date(new Date().getTime() + TimeUnit.DAYS.toMillis( -1 ));
				ToDate = new java.util.Date(new Date().getTime() + TimeUnit.DAYS.toMillis( 1 ));
			}
		}catch(NullPointerException e){
			e.printStackTrace();			
		}
		
		if(Distributor > 0){
			ExtraClauses += " and distributor_id = "+Distributor+" ";
		}
		
		if(!Remarks.equals("")){
			ExtraClauses += " and remarks like '%"+Remarks+"%' ";
		}
		
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT document_id, created_on, remarks, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name, (select display_name from users where id=created_by) user_name, distributor_id, distributor_id as distributor_id_val FROM inventory_damaged_stock where created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate) + ExtraClauses );
		while( rs.next() ){
			DocumentHeader item = new DocumentHeader();
			
			item.DOCUMENT_ID = rs.getLong(1);
			item.CREATED_ON = rs.getTimestamp(2);
			item.REMARKS = rs.getString(3);
			item.DISTRIBUTOR_NAME = rs.getString(4);
			item.CREATED_BY = rs.getString(5);
			item.DISTRIBUTOR_ID = rs.getLong(6);
			
			list.add(item);
		}
		
		rs.close();
		s.close();
		
		
		return list.toArray(new DocumentHeader[num]);
		 
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
