package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
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
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Spot Discount ", urlPatterns = { "/inventory/BulkDiscountExecute" })
public class BulkDiscountExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BulkDiscountExecute() {
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
		System.out.println("BulkDiscountExecute");
		
		
		//Master table
		String BulkDiscountLabel = Utilities.filterString(request.getParameter("BulkDiscountLabel"), 1, 20);
		System.out.println("BulkDiscountLabel"+BulkDiscountLabel);
		String ValidFrom = Utilities.filterString(request.getParameter("BulkDiscountValidFrom"),1,12);
		String ValidTo = Utilities.filterString(request.getParameter("BulkDiscountValidTo"),1,12);
		int Active = Utilities.parseInt(request.getParameter("BulkDiscountIsActive"));
		
		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);

		//Detail table
		long[] ProductID = Utilities.parseLong(request.getParameterValues("BulkDiscountProductCode"));
		double[] RawCases = Utilities.parseDouble(request.getParameterValues("BulkDiscountRawCase"));
		double[] Bottles = Utilities.parseDouble(request.getParameterValues("BulkDiscountBottle"));
		String ProductType = request.getParameter("BulkDiscountProductType");
		double[] Discount = Utilities.parseDouble(request.getParameterValues("Discountper"));
		int PCI = Utilities.parseInt(request.getParameter("PCISelect"));
		int isEdit = Utilities.parseInt(request.getParameter("isEditCase"));

//		System.out.println("ProductType  "	+ProductType);
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		long MasterTableSpotDiscountID = 0;
		String pci_channel_id = "" ;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
				

				if(isEdit == 0)//insertion case master table
				{
				
					s.executeUpdate("insert into inventory_hand_to_hand_discount(label,valid_from,valid_to,is_active,created_on,created_by,activated_by,activated_on,pci_channel_id) values('"+BulkDiscountLabel+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+Active+",now(),"+UserID+","+UserID+",now(),'"+PCI+"')");
					
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
					s.executeUpdate("update inventory_hand_to_hand_discount set label='"+BulkDiscountLabel+"',valid_from="+Utilities.getSQLDate(ValidFromDate)+",valid_to="+Utilities.getSQLDate(ValidToDate)+Activation+" where id="+BulkDiscountMasterTableID);
					
					System.out.println("delete from inventory_hand_to_hand_discount_products where hand_discount_id="+BulkDiscountMasterTableID);
					s.executeUpdate("delete from inventory_hand_to_hand_discount_products where hand_discount_id="+BulkDiscountMasterTableID);
					//s.executeUpdate("delete from inventory_hand_to_hand_discount_products where id="+BulkDiscountMasterTableID); //deleting previous records from detail table
					MasterTableSpotDiscountID = BulkDiscountMasterTableID;
				}
				
System.out.println("Bottles "+Bottles.length);
System.out.println("RawCases "+RawCases.length);

		int j = 0;
				for(int i=0;i<RawCases.length;i++)
				{
					//System.out.println("i "+i);
					if(RawCases[i] ==0 && Bottles[i] ==0)
					{
						
					}else {
						j++;
						System.out.println("insert into inventory_hand_to_hand_discount_products(product_id,from_qty,to_qty,hand_discount_id,discount_percentage,pci_channel_id) values("+ ProductID[i] +","+RawCases[i]+","+Bottles[i]+",'"+MasterTableSpotDiscountID+"','"+Discount[i]+"','"+PCI+"')");
						s.executeUpdate("insert into inventory_hand_to_hand_discount_products(product_id,from_qty,to_qty,hand_discount_id,discount_percentage,pci_channel_id) values("+ ProductID[i] +","+RawCases[i]+","+Bottles[i]+",'"+MasterTableSpotDiscountID+"','"+Discount[i]+"','"+PCI+"')");
					}
				}
				
					
				obj.put("spot_discount_id",MasterTableSpotDiscountID);
				obj.put("success", "true");
				ds.commit();
			
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
