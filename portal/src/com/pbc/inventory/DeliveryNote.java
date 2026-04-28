package com.pbc.inventory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pbc.common.DocumentHeader;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.workflow.WorkflowDocument;
import com.pbc.common.Warehouse;
import com.pbc.common.Region;
import com.pbc.common.Distributor;
import com.pbc.util.UserAccess;

public class DeliveryNote {
	
	long DeliveryID;
	
	Connection c;
	Datasource ds;	
	
	
	public DeliveryNote() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public void setDeliveryID(long DeliveryID) throws SQLException{
		this.DeliveryID = DeliveryID;
	}
	
	public String getPaymentMethodOptions() throws SQLException{
		
		Statement s = c.createStatement();
		
		String Options = "<option value=''>Select Payment Method</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_delivery_note_payment_methods order by label");
		while( rs.next() ){
			Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		return Options;
		
	}
	
	public String getVehicleTypeOptions() throws SQLException{
		
		Statement s = c.createStatement();
		
		String Options = "<option value=''>Select Vehicle Type</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_delivery_note_vehicle_types order by label");
		while( rs.next() ){
			Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		return Options;
		
	}
	
	
	
	
	public String getHaulageContractorOptions() throws SQLException{
		
		Statement s = c.createStatement();
		
		String Options = "<option value=''>Select Contractor</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_delivery_note_haulage_contractors order by label");
		while( rs.next() ){
			Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		return Options;
		
	}
	
	
	public String getPalatizeOption() throws SQLException{
		  
		  Statement s = c.createStatement();
		  
		  String Options = "";
		  
		  ResultSet rs = s.executeQuery("select id, label from inventory_delivery_note_palletize_types");
		  while( rs.next() ){
		   Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
		  }
		  
		  return Options;
		  
		 }
	
	
	public String getPackageOptions() throws SQLException{
		Statement s = c.createStatement();
		
		String Options = "<option value=''>Select Package</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_packages order by label");
		while( rs.next() ){
			Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		return Options;
	}
	
	public String getBrandOptions() throws SQLException{
		Statement s = c.createStatement();
		
		String Options = "<option value=''>Select Brand</option>";
		
		ResultSet rs = s.executeQuery("select id, label from inventory_brands order by label");
		while( rs.next() ){
			Options += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
		}
		
		return Options;
	}
	
	public DocumentHeader[] getDocumentList(Date FromDate, Date ToDate, long Distributor, String SaleOrderNumber, String VehicleNumber, String Remarks, int PaymentMethod, String BatchCode, String Status, long UserSAPCode, long InvoiceNumber, int VehicleType, int Region, int WarehouseID,long SessionUserID) throws SQLException{
		
		List <DocumentHeader>list = new ArrayList<DocumentHeader>();
		int num = 0;
		
		String SQL = "";
		
		String ExtraClauses = "";
		
		try{
			if(FromDate == null || ToDate == null ){
				FromDate = new java.util.Date(new Date().getTime() + TimeUnit.DAYS.toMillis( -1 ));
				ToDate = new java.util.Date(new Date().getTime() + TimeUnit.DAYS.toMillis( 1 ));
				
			}
		}catch(NullPointerException e){
			e.printStackTrace();			
		}
		
		if(Region > 0){
			ExtraClauses += " and cd.region_id = "+Region;
		}
		
		if(Distributor > 0){
			ExtraClauses += " and idn.distributor_id = "+Distributor+" ";
		}
		
		if(!SaleOrderNumber.equals("")){
			ExtraClauses += " and sap_order_no like '%"+SaleOrderNumber+"%' ";
		}
		
		if(!VehicleNumber.equals("")){
			ExtraClauses += " and vehicle_no like '%"+VehicleNumber+"%'";
		}
		
		if(VehicleType > 0){
			ExtraClauses += " and vehicle_type = "+VehicleType+"";
		}
		
		if(!Remarks.equals("")){
			ExtraClauses += " and remarks like '%"+Remarks+"%' ";
		}
		
		if(PaymentMethod > 0){
			ExtraClauses += " and payment_method = "+PaymentMethod;
		}
		
		if(UserSAPCode > 0){
			ExtraClauses += " and created_by = "+UserSAPCode;
		}
		
		if(InvoiceNumber > 0){
			ExtraClauses += " and invoice_no = "+InvoiceNumber;
		}
		
		if(WarehouseID > 0){
			ExtraClauses += " and warehouse_id = "+WarehouseID;
		}
		
		int FeatureID = 40;
		
		//getting user regions
        if(SessionUserID != 0)
        {
        	try{
        		String RegionIds;

					RegionIds = UserAccess.getRegionQueryString(UserAccess.getUserFeatureRegion(SessionUserID, FeatureID));
		
        		
        		String WarehouseIds = UserAccess.getWarehousesQueryString(UserAccess.getUserFeatureWarehouse(SessionUserID, FeatureID));				
        		
        		String DistributorsIds = UserAccess.getDistributorQueryString(UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID));
        		
        		ExtraClauses += " and idn.warehouse_id in ("+WarehouseIds+")";
        		
        		ExtraClauses += " and cd.region_id in ("+RegionIds+")";
        		
        		ExtraClauses += " and idn.distributor_id in ("+DistributorsIds+")";
        	}
        		catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException e) {
					e.printStackTrace();
				}
        		
        }
		
		
		
		
		if(!Status.equals("")){
			
			if(Status.equals("1")){
				ExtraClauses += " and is_delivered = 0 and is_received = 0";
			}else if(Status.equals("2")){
				ExtraClauses += " and is_delivered = 1 and is_received = 0";
			}else if(Status.equals("3")){
				ExtraClauses += " and is_received = 1";
			}else if(Status.equals("0")){
				ExtraClauses += "";
			}
			
			ExtraClauses += " and remarks like '%"+Remarks+"%' ";
		}
		
		if(!BatchCode.equals("")){
			SQL = "SELECT idn.delivery_id, idn.created_on, remarks, sap_order_no, vehicle_no, (select display_name from users where id=created_by) user_name, idn.distributor_id, payment_method, (select label from inventory_delivery_note_payment_methods where id=payment_method) payment_method_label, idn.is_received, idn.is_delivered, invoice_no, vehicle_type, (select label from inventory_delivery_note_vehicle_types where id=vehicle_type) vehichle_label, idn.distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_delivery_note idn, inventory_delivery_note_products idnp, common_distributors cd  where idn.delivery_id = idnp.delivery_id and idn.distributor_id=cd.distributor_id and idnp.batch_code like '%"+BatchCode+"%' and idn.created_on between "+Utilities.getSQLDateTime(FromDate)+" and "+Utilities.getSQLDateTime(ToDate) + ExtraClauses + " limit 1";
		}else{
			SQL = "SELECT delivery_id, idn.created_on, remarks, sap_order_no, vehicle_no, (select display_name from users where id=created_by) user_name, idn.distributor_id, payment_method, (select label from inventory_delivery_note_payment_methods where id=payment_method) payment_method_label, is_received, is_delivered, invoice_no, vehicle_type, (select label from inventory_delivery_note_vehicle_types where id=vehicle_type) vehichle_label, idn.distributor_id as distributor_id_val, (SELECT name FROM common_distributors where distributor_id=distributor_id_val) distributor_name FROM inventory_delivery_note idn, common_distributors cd where idn.distributor_id=cd.distributor_id and idn.created_on between "+Utilities.getSQLDateTime(FromDate)+" and "+Utilities.getSQLDateTime(ToDate) + ExtraClauses ;
		}
		
		//System.out.println(SQL);
		
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery(SQL);
		while( rs.next() ){
			DocumentHeader item = new DocumentHeader();
			
			item.DOCUMENT_ID = rs.getLong("delivery_id");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.REMARKS = rs.getString("remarks");
			item.DISTRIBUTOR_NAME = rs.getString("distributor_name");
			item.SALE_ORDER_NUMBER = rs.getString("sap_order_no");
			item.VEHICLE_NUMBER = rs.getString("vehicle_no");
			item.CREATED_BY = rs.getString("user_name");
			item.DISTRIBUTOR_ID = rs.getLong("distributor_id");
			item.PAYMENT_METHOD_ID = rs.getInt("payment_method");
			item.PAYMENT_METHOD_LABEL = rs.getString("payment_method_label");
			item.INVOICE_NUMBER = rs.getLong("invoice_no");
			item.VEHICLE_TYPE_ID = rs.getInt("vehicle_type");
			
			if(rs.getInt("is_delivered") == 0){
				item.STATUS = "Generated";
			}else if(rs.getInt("is_delivered") == 1 && rs.getInt("is_received") == 0){
				item.STATUS = "Delivered";
			}
			
			if(rs.getInt("is_received") == 1){
				item.STATUS = "Received";
			}
			
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
