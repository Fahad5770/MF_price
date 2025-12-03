package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Spot Discount ", urlPatterns = { "/inventory/BrandBulkDiscountExecute" })
public class BrandBulkDiscountExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BrandBulkDiscountExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
	//	System.out.println("BulkDiscountExecute");
		
		
		//Master table
		String BulkDiscountLabel = Utilities.filterString(request.getParameter("BulkDiscountLabel"), 1, 20);
	//	System.out.println("BulkDiscountLabel"+BulkDiscountLabel);
		String ValidFrom = Utilities.filterString(request.getParameter("BulkDiscountValidFrom"),1,12);
		String ValidTo = Utilities.filterString(request.getParameter("BulkDiscountValidTo"),1,12);
		int Active = Utilities.parseInt(request.getParameter("BulkDiscountIsActive"));
		
		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);

		//Detail table
		double[] RawCases = Utilities.parseDouble(request.getParameterValues("BulkDiscountRawCase"));
		double[] Bottles = Utilities.parseDouble(request.getParameterValues("BulkDiscountBottle"));
		double[] Discount = Utilities.parseDouble(request.getParameterValues("Discountper"));
		int PCI = Utilities.parseInt(request.getParameter("PCISelect"));
		int isEdit = Utilities.parseInt(request.getParameter("isEditCase"));
		int BrandSelect = Utilities.parseInt(request.getParameter("BrandSelect"));
		
//		System.out.println("=============================================================================================================================");
//		System.out.println("isEdit  "	+isEdit);
//		System.out.println("Pci_Channel  "	+PCI);
//		System.out.println("ValidFrom  "	+ValidFrom);
//		System.out.println("ValidTo  "	+ValidTo);
//		System.out.println("Active  "	+Active);
//		for(int i=0; i<RawCases.length; i++) {
//			System.out.println("---------------------------------------------------------------------------------");
//			System.out.println("RawCases "+ RawCases[i]);
//			System.out.println("Bottles "+ Bottles[i]);
//			System.out.println("Discount "+ Discount[i]);
//			System.out.println("---------------------------------------------------------------------------------");
//		}
//		System.out.println("=============================================================================================================================");

		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		long MasterTableSpotDiscountID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
				

				if(isEdit == 0)//insertion case master table
				{
				//	System.out.println("insert into inventory_hand_to_hand_discount(label,valid_from,valid_to,is_active,created_on,created_by,activated_by,activated_on,pci_channel_id,is_brand_discount,brand_id) values('"+BulkDiscountLabel+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+Active+",now(),"+UserID+","+UserID+",now(),'"+PCI+"',1,"+BrandSelect+")");
					s.executeUpdate("insert into inventory_hand_to_hand_discount(label,valid_from,valid_to,is_active,created_on,created_by,activated_by,activated_on,pci_channel_id,is_brand_discount,brand_id) values('"+BulkDiscountLabel+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+Active+",now(),"+UserID+","+UserID+",now(),'"+PCI+"',1,"+BrandSelect+")");
					
					//getting SpotDiscount id
					
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						MasterTableSpotDiscountID = rs.getInt(1); 
					}
					
				} 
				else if(isEdit == 1) //updation case for master table
				{
					long BulkDiscountMasterTableID = Utilities.parseLong(request.getParameter("BulkDiscountMasterTableID"));
					
					String Activation = "";
					if(Active == 1) {
						Activation = ", is_active="+Active+", activated_on=now(), activated_by="+UserID;
					}else {
						Activation = ", is_active="+Active+", deactivated_on=now(), deactivated_by="+UserID;
					}
				//	System.out.println("update inventory_hand_to_hand_discount set label='"+BulkDiscountLabel+"',valid_from="+Utilities.getSQLDate(ValidFromDate)+",valid_to="+Utilities.getSQLDate(ValidToDate)+Activation+" where id="+BulkDiscountMasterTableID);					
					s.executeUpdate("update inventory_hand_to_hand_discount set label='"+BulkDiscountLabel+"',valid_from="+Utilities.getSQLDate(ValidFromDate)+",valid_to="+Utilities.getSQLDate(ValidToDate)+Activation+" where id="+BulkDiscountMasterTableID);
					
				//	System.out.println("delete from inventory_hand_to_hand_discount_products where hand_discount_id="+BulkDiscountMasterTableID);
					s.executeUpdate("delete from inventory_hand_to_hand_discount_products where hand_discount_id="+BulkDiscountMasterTableID);
					//s.executeUpdate("delete from inventory_hand_to_hand_discount_products where id="+BulkDiscountMasterTableID); //deleting previous records from detail table
					MasterTableSpotDiscountID = BulkDiscountMasterTableID;
				}
				
//System.out.println("Bottles "+Bottles.length);
//System.out.println("RawCases "+RawCases.length);

		ResultSet rsBrandProducts = s.executeQuery("SELECT product_id,unit_per_sku FROM inventory_products_view where lrb_type_id="+BrandSelect);
		while(rsBrandProducts.next()) {
			int ProductID = rsBrandProducts.getInt("product_id");
				for(int i=0;i<RawCases.length;i++)
				{
					//System.out.println("i "+i);
					if(RawCases[i] ==0 && Bottles[i] ==0)
					{
						
					}else {
						//System.out.println("insert into inventory_hand_to_hand_discount_products(product_id,from_qty,to_qty,hand_discount_id,discount_percentage,pci_channel_id) values("+ ProductID +","+RawCases[i]+","+Bottles[i]+",'"+MasterTableSpotDiscountID+"','"+Discount[i]+"','"+PCI+"')");
						s2.executeUpdate("insert into inventory_hand_to_hand_discount_products(product_id,from_qty,to_qty,hand_discount_id,discount_percentage,pci_channel_id) values("+ ProductID +","+RawCases[i]+","+Bottles[i]+",'"+MasterTableSpotDiscountID+"','"+Discount[i]+"','"+PCI+"')");
					}
				}
		}
					
				obj.put("spot_discount_id",MasterTableSpotDiscountID);
				obj.put("success", "true");
				ds.commit();
			
				s2.close();	
			s.close();	
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
