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

public class DamagedStock {
	
	long DamagedStockID;
	
	Connection c;
	Datasource ds;	
	
	public DamagedStock() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public void setDamagedStockID(long DamagedStockID) throws SQLException{
		this.DamagedStockID = DamagedStockID;
		
	}
	
	public HashMap<Integer, String> getDamagedStockTypes() throws SQLException{
		
		Statement s = c.createStatement();

		HashMap<Integer, String> DamagedStockTypes = new HashMap<Integer, String>();
		
		ResultSet rs = s.executeQuery("select id, label from inventory_damaged_stock_types");
		while( rs.next() ){
			DamagedStockTypes.put(rs.getInt(1), rs.getString(2));
		}
		
		s.close();
		return DamagedStockTypes;
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
		
		ResultSet rs = s.executeQuery("SELECT document_id, created_on, remarks, (select display_name from users where id=created_by) user_name, distributor_id, distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_damaged_stock where created_on between "+Utilities.getSQLDate(FromDate)+" and "+Utilities.getSQLDateNext(ToDate) + ExtraClauses );
		while( rs.next() ){
			DocumentHeader item = new DocumentHeader();
			
			item.DOCUMENT_ID = rs.getLong("document_id");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.REMARKS = rs.getString("remarks");
			item.DISTRIBUTOR_NAME = rs.getString("distributor_name");
			item.CREATED_BY = rs.getString("user_name");
			item.DISTRIBUTOR_ID = rs.getLong("distributor_id");
			
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
