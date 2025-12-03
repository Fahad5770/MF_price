package com.pbc.cash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;



@WebServlet(description = "Price List ", urlPatterns = { "/cash/PerCaseDiscountApprovalExecute" })
public class PerCaseDiscountApprovalExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PerCaseDiscountApprovalExecute() {
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
		
		long RequestID=Utilities.parseLong(request.getParameter("RequestID"));
		String StartDate = Utilities.filterString(request.getParameter("StartDate"),1,100);
		String EndDate = Utilities.filterString(request.getParameter("EndDate"),1,100);
		
		
		//System.out.println(RequestID+" - "+StartDate+" - "+EndDate);
		
		PrintWriter out = response.getWriter();
		
		
		
		
		JSONObject obj = new JSONObject();
		Datasource ds = new Datasource();
		
		long MasterTableCoolerInjectionID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();
			Statement s5 = ds.createStatement();
			Statement s6 = ds.createStatement();
			

			
				//System.o
				
			long DistributorID=0;
			long PacakgeID=0;
			long MainTableID=0;
			long LrbTypeID=0;
			Date LiftingStartDate=new Date();
			Date LiftingEndDate=new Date();
			int AmountTotal=0;
			Date ReviewedDate = new Date();
			
			
			
			ResultSet rs1 = s.executeQuery("SELECT * FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id=ipprd.product_promotion_id where request_id="+RequestID);
			if(rs1.first()){
				DistributorID=rs1.getLong("distributor_id");
				MainTableID = rs1.getLong("id");
				
				LiftingStartDate=rs1.getDate("valid_from");
				LiftingEndDate=rs1.getDate("valid_to");
				
				ReviewedDate = rs1.getDate("created_on");
			}
			
				
			
			//int AmountTotal=0;
			
			int loopFlag=0;
			int MasterTablePerCaseApprovalID=0; 
			ResultSet rs122 = s2.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id where ippr.created_on between '"+StartDate+"' and '"+EndDate+"') and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
			while(rs122.next()){
				
				LrbTypeID  = rs122.getLong("lrb_type_id");
				
				ResultSet rs2 = s3.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id where ippr.created_on between '"+StartDate+"' and '"+EndDate+"')  and category_id = 1 and lrb_type_id="+rs122.getLong("lrb_type_id")+" order by package_sort_order");
				while(rs2.next()){
					
					
					long PerCaseDiscount=0;
					long Quota=0;
					
					PacakgeID = rs2.getLong("package_id");
					
					ResultSet rs3 = s4.executeQuery("SELECT ipprp.percase_discount_rate,ipprp.quantity FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprpl on ippr.id=ipprpl.id where ipprp.id="+MainTableID+" and ipprp.package_id="+PacakgeID+" and ipprpl.lrb_type_id="+rs122.getLong("lrb_type_id"));
					if(rs3.first()){
						PerCaseDiscount = rs3.getLong("percase_discount_rate");
						Quota = rs3.getLong("quantity");
					}
					
					//lifiting 
					
					long TotalUnitsLifting=0;
					int UnitPerSKU=0;
					
					double Converted=0;
					double Amount =0;
					
					
					ResultSet rs4 = s4.executeQuery("select sum(total_units) total_units, cache_units_per_sku from inventory_delivery_note_source_invoice idnsi where distributor_id = "+DistributorID+" and delivery_created_on between "+Utilities.getSQLDateLifting(LiftingStartDate)+" and "+Utilities.getSQLDateNextLifting(LiftingEndDate)+" and cache_package_id="+PacakgeID+" and is_revenue = 1 and cache_lrb_type_id="+rs122.getLong("lrb_type_id"));
					//ResultSet rs4 = s4.executeQuery("select sum(idnp.total_units) total_units,unit_per_sku  from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = "+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(LiftingStartDate)+" and "+Utilities.getSQLDateNextLifting(LiftingEndDate)+" and package_id="+PacakgeID+" and lrb_type_id="+rs122.getLong("lrb_type_id"));
					if(rs4.first()){
						TotalUnitsLifting = rs4.getLong("total_units");
						UnitPerSKU = rs4.getInt("cache_units_per_sku");
						
						
						
						
						if(UnitPerSKU!=0){
							Converted = TotalUnitsLifting/UnitPerSKU;
						}
						
						
					}
					
					
					
					
					
					
					Converted = Math.round(Converted);
					
					if(Converted>=Quota){ // if lifting exceeds quota
						Converted=Quota;
					}
					
					
					
					Amount = Converted*PerCaseDiscount;
					
					
					
					
					AmountTotal+=Amount;
					
					
					Double NetPrice=null;
					String Comment="";
					
					double LiftingTTotal=0;
					
					if(PerCaseDiscount!=0){
						LiftingTTotal = TotalUnitsLifting; 
					}
					
					
					if(loopFlag==0){
						String rs5SQL = "INSERT INTO inventory_primary_percase_approval(valid_from,is_active,valid_to,request_id,net_price,comments,created_on,created_by,reviewed_on,reviewed_by,distributor_id)VALUES('"+LiftingStartDate+"',1,'"+LiftingEndDate+"', "+RequestID+", "+NetPrice+", '"+Comment+"', now(),"+UserID+",'"+ReviewedDate+"', "+UserID+","+DistributorID+")";	
						//System.out.println(rs5SQL);			
						
						int rs5 = s5.executeUpdate(rs5SQL);
						
						 
						
						
						ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					    if(rs.first()){
					     MasterTablePerCaseApprovalID = rs.getInt(1); 
					     //System.out.print(MasterTablePerCaseApprovalID);	
					    }
					    loopFlag++; 
					    //System.out.println(loopFlag);
					}
				    String rs6SQL = "INSERT INTO inventory_primary_percase_approval_products(id,package_id,lrb_type_id,quota,rate,lifting,amount)VALUES("+MasterTablePerCaseApprovalID+", "+PacakgeID+", "+LrbTypeID+", "+Quota+", "+PerCaseDiscount+", "+LiftingTTotal+","+Amount+")";	
					
					//System.out.println(rs6SQL);
					int rs6 = s6.executeUpdate(rs6SQL);
					
				}
			}
			
				
				//obj.put("product_promotion_id",MasterTableCoolerInjectionID);
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
			
			// TODO Auto-generated catch block
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
