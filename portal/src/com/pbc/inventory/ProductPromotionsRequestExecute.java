package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price List ", urlPatterns = { "/inventory/ProductPromotionsRequestExecute" })
public class ProductPromotionsRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductPromotionsRequestExecute() {
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
		JSONObject obj = new JSONObject();
		boolean isValidated = true;
		
		//Master table
		String PriceListName = Utilities.filterString(request.getParameter("ProductPromotionsLabel"), 1, 20);
		String ValidFrom = Utilities.filterString(request.getParameter("ProductPromotionsValidFrom"),1,12);
		String ValidTo = Utilities.filterString(request.getParameter("ProductPromotionsValidTo"),1,12);
		int Active = Utilities.parseInt(request.getParameter("ProductPromotionsIsActive"));
		
		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		Date CurrentDate = cal.getTime();
		
		//System.out.println("ValidFromDate = "+ValidFromDate);
		//System.out.println("CurrentDate = "+CurrentDate);
		//System.out.println("ValidFromDate.compareTo(CurrentDate) = "+ValidFromDate.compareTo(CurrentDate));
		
		
		
		
		if( ValidFromDate.compareTo(CurrentDate) < 0){
			isValidated = false;
			obj.put("success", "false");
			obj.put("error", "Date Range Invalid");
		}
		
		Date MaximumRangePeriodDate = Utilities.getDateByDays(ValidFromDate, 365);
		
		if(ValidToDate.compareTo(MaximumRangePeriodDate) > 0){
			isValidated = false;
			obj.put("success", "false");
			obj.put("error", "Range should not exceed a year");
		}
		
		if(isValidated){

		//Detail table
		long[] SalesPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsMainFormPackage"));
		
		double[] SalesRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsMainFormRawCases"));
		double[] SalesUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsMainFormUnits"));
		
		long[] PromotionsPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsPMainFormPackage"));
		//long[] PromotionsBrandID = Utilities.parseLong(request.getParameterValues("ProductPromotionsPMainFormProductIDIssue"));
		double[] PromotionsRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormRawCases"));
		double[] PromotionsUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormUnits"));

		
		double estimated_sales_volume = Utilities.parseDouble(request.getParameter("EstimatedSalesVolumeRawCases"));
		double sales_sku_price = Utilities.parseDouble(request.getParameter("SalesSKUPrice"));
		double free_sku_price = Utilities.parseDouble(request.getParameter("FreeSKUPriceBottles"));
		double variable_cost_and_taxes = Utilities.parseDouble(request.getParameter("VariableCost"));
		double marginal_contribution = Utilities.parseDouble(request.getParameter("MarginalContribution"));
		double NetPrice = Utilities.parseDouble(request.getParameter("netprice"));
		
		String Comments = Utilities.filterString(request.getParameter("comments"),1,500);
		
		
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		
		
		Datasource ds = new Datasource();
		
		long MasterTablePromotionID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
			ResultSet rs11 = s.executeQuery("select id from inventory_sales_promotions_request where uvid="+UniqueVoucherID);
			if( rs11.first() ){
				
				obj.put("success", "false");
				obj.put("error", "Already Exists");
				
			}else{
				
					if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
					{
					
						s.executeUpdate("insert into inventory_sales_promotions_request(uvid,label,valid_from,valid_to,is_active,created_on,created_by,estimated_sales_volume,sales_sku_price,free_sku_price,variable_cost_and_taxes,marginal_contribution,net_price,comments) values("+UniqueVoucherID+",'"+PriceListName+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+Active+",now(),"+UserID+","+estimated_sales_volume+","+sales_sku_price+","+free_sku_price+","+variable_cost_and_taxes+","+marginal_contribution+","+NetPrice+",'"+Comments+"')");
						
						//getting pricelist id
						
						ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
						if(rs.first()){
							MasterTablePromotionID = rs.getInt(1); 
						}
						
					} 
					else if(Utilities.parseLong(request.getParameter("isEditCase"))==1) //updation case for master table
					{					
						//long ProductPromotionsMasterTableID = Utilities.parseLong(request.getParameter("ProductPromotionMasterTableID"));
						//System.out.println("I am in edit case "+ProductPromotionsMasterTableID);
						//s.executeUpdate("update inventory_sales_promotions_request set label='"+PriceListName+"',valid_from="+Utilities.getSQLDate(ValidFromDate)+",valid_to="+Utilities.getSQLDate(ValidToDate)+",is_active="+Active+",estimated_sales_volume="+estimated_sales_volume+",sales_sku_price="+sales_sku_price+",free_sku_price="+free_sku_price+",variable_cost_and_taxes="+variable_cost_and_taxes+",marginal_contribution="+marginal_contribution+" where id="+ProductPromotionsMasterTableID);
						
						//s.executeUpdate("delete from inventory_sales_promotions_request_products where id="+ProductPromotionsMasterTableID); //deleting previous records from detail table
						//s.executeUpdate("delete from inventory_sales_promotions_request_products_brands where id="+ProductPromotionsMasterTableID); //deleting previous records from detail table
						//MasterTablePromotionID = ProductPromotionsMasterTableID;
					}
					//inserting in sub tables
					String brandid=null;
					String brandid1=null;
					long SubTableID=0;
					long SubTableID1=0;
					if(SalesPacakgeID != null)
					{
						for(int i=0;i<SalesPacakgeID.length;i++)
						{					
							
							int UnitPerCase=0;	
							double TotalUnits=0;
							//calculating Total Units
							ResultSet rs1 = s.executeQuery("select unit_per_case from inventory_packages where id="+SalesPacakgeID[i]);
							if(rs1.first())
							{
								UnitPerCase = Utilities.parseInt(rs1.getString("unit_per_case"));
							}
							TotalUnits = (SalesRaweCases[i] * UnitPerCase)+SalesUnits[i]; // (RawCases * UnitPerCase)+Bottles
							
							s.executeUpdate("insert into inventory_sales_promotions_request_products(id,package_id,raw_cases,units,type_id,total_units) values("+MasterTablePromotionID+","+ SalesPacakgeID[i] +","+SalesRaweCases[i]+","+SalesUnits[i]+",1,"+TotalUnits+")"); //type id = 1 for sales 2 for promotions
	
							//now inserting brand 
							long[] SalesBrandID = Utilities.parseLong(request.getParameterValues("Brands"+SalesPacakgeID[i]));
							if(SalesBrandID !=null)
							{
								for(int j=0;j<SalesBrandID.length;j++)
								{
									s.executeUpdate("insert into inventory_sales_promotions_request_products_brands(id,package_id,brand_id,type_id) values("+MasterTablePromotionID+","+ SalesPacakgeID[i] +","+SalesBrandID[j]+",1)"); //type id = 1 for sales 2 for promotions
								}
							}
						}
					}
					
					//inserting for promotions
					if(PromotionsPacakgeID != null)
					{
						for(int i=0;i<PromotionsPacakgeID.length;i++)
						{					
							
							int UnitPerCase=0;	
							double TotalUnits=0;
							//calculating Total Units
							ResultSet rs1 = s.executeQuery("select unit_per_case from inventory_packages where id="+PromotionsPacakgeID[i]);
							if(rs1.first())
							{
								UnitPerCase = Utilities.parseInt(rs1.getString("unit_per_case"));
							}
							TotalUnits = (PromotionsRaweCases[i] * UnitPerCase)+PromotionsUnits[i]; // (RawCases * UnitPerCase)+Bottles
							s.executeUpdate("insert into inventory_sales_promotions_request_products(id,package_id,raw_cases,units,type_id,total_units) values("+MasterTablePromotionID+","+ PromotionsPacakgeID[i] +","+PromotionsRaweCases[i]+","+PromotionsUnits[i]+",2,"+TotalUnits+")"); //type id = 1 for sales 2 for promotions
							
	
							//now inserting brand 
							long[] SalesBrandID = Utilities.parseLong(request.getParameterValues("PromotionBrand"+PromotionsPacakgeID[i]));
							if(SalesBrandID !=null)
							{
								for(int j=0;j<SalesBrandID.length;j++)
								{
									s.executeUpdate("insert into inventory_sales_promotions_request_products_brands(id,package_id,brand_id,type_id) values("+MasterTablePromotionID+","+ PromotionsPacakgeID[i] +","+SalesBrandID[j]+",2)"); //type id = 1 for sales 2 for promotions
								}
							}
						}
					}
					
					
					
					
					
					obj.put("product_promotion_id",MasterTablePromotionID);
					obj.put("success", "true");
					
					ds.commit();
					
					
					
					Workflow wf = new Workflow();
					//long WorkFlowRequestID = wf.createRequest(3, Integer.parseInt(UserID), 2011, 3, "Promotion Request Raised");
					long WorkFlowRequestID = wf.createRequest(3, Integer.parseInt(UserID), 4104, 3, "Promotion Request Raised"); //updated by Zulqurnan on req of Anas Shb on 20/03/2018
					s.executeUpdate("update inventory_sales_promotions_request set request_id="+WorkFlowRequestID+" where id="+MasterTablePromotionID);
					
					ds.commit();
			}
				
				
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
		
		}// end if of Date validation
		
		out.print(obj);
		out.close();
		
	}
	
}
