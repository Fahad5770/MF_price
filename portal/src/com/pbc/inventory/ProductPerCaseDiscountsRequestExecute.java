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


@WebServlet(description = "Price List ", urlPatterns = { "/inventory/ProductPerCaseDiscountsRequestExecute" })
public class ProductPerCaseDiscountsRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductPerCaseDiscountsRequestExecute() {
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
		//String PriceListName = Utilities.filterString(request.getParameter("ProductPromotionsLabel"), 1, 20);
		String ValidFrom = Utilities.filterString(request.getParameter("ProductPromotionsValidFrom"),1,12);
		String ValidTo = Utilities.filterString(request.getParameter("ProductPromotionsValidTo"),1,12);
		int Active = Utilities.parseInt(request.getParameter("ProductPromotionsIsActive"));
		
		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);
		
		
		
		
		
		//Detail table
				long[] SalesPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsMainFormPackage"));
				
				double[] SalesRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsMainFormRawCases"));
				double[] SalesUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsMainFormUnits"));
				
				long[] PromotionsPacakgeID = Utilities.parseLong(request.getParameterValues("ProductPromotionsPMainFormPackage"));
				//long[] PromotionsBrandID = Utilities.parseLong(request.getParameterValues("ProductPromotionsPMainFormProductIDIssue"));
				//double[] PromotionsRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormRawCases"));
				//double[] PromotionsUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormUnits"));

				
				double Quantity[] = Utilities.parseDouble(request.getParameterValues("Quantity"));
				double DiscountRate[] = Utilities.parseDouble(request.getParameterValues("PercaseDiscountRate"));
				double SellingPrice[] = Utilities.parseDouble(request.getParameterValues("SellingPrice"));
				
				
				String Comments = Utilities.filterString(request.getParameter("comments"),1,500);
				
				
				long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		Date CurrentDate = cal.getTime();
		
		//System.out.println("ValidFromDate = "+ValidFromDate);
		//System.out.println("CurrentDate = "+CurrentDate);
		//System.out.println("ValidFromDate.compareTo(CurrentDate) = "+ValidFromDate.compareTo(CurrentDate));
		
		Datasource ds = new Datasource();
		
		
		if( ValidFromDate.compareTo(CurrentDate) < 0){
			isValidated = false;
			obj.put("success", "false");
			obj.put("error", "Date Range Invalid");
		}
		
		if(ValidToDate.compareTo(Utilities.getEndDateByDate(ValidFromDate))>0){ // Date should not exceed month boundary
			isValidated = false;
			obj.put("success", "false");
			obj.put("error", "Date Range should not exceed Month boundary");
		}
		
		//Overlapping Target Check
		
		try{
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
			String InsertedPackageId="";
			String InsertedDistID="";
			String InsertedLRBTypes = "0";
			
			if(SalesPacakgeID!=null){
				for(int i=0;i<SalesPacakgeID.length;i++)
				{
					InsertedPackageId+=SalesPacakgeID[i]+",";
					
					long[] InsertedSalesBrandID = Utilities.parseLong(request.getParameterValues("Brands"+SalesPacakgeID[i]+"_"+i));
					if(InsertedSalesBrandID !=null){
						for(int j=0;j<InsertedSalesBrandID.length;j++){
							InsertedLRBTypes += ","+InsertedSalesBrandID[j];
						}
					}
					
				}
			}
			
			
			
			
			String [] SelectedDistributor=null;
			String [] SelectedDistributorID1=null
					;
			if(request.getParameterValues("DistributorIDhiddenfield") != null)
			{
				 SelectedDistributor = request.getParameterValues("DistributorIDhiddenfield");
				for(int t=0;t<SelectedDistributor.length;t++){
					
					String [] FeatureIDndDistributorID = SelectedDistributor[t].split(",");  //DistributorID,FeatureID
					
					
					InsertedDistID=FeatureIDndDistributorID[0];
				}
				
			}
			
			
			InsertedPackageId=InsertedPackageId.substring(0, InsertedPackageId.length()-1);
			
			
			//System.out.println("SELECT ippr.id,ippr.valid_to,ippr.valid_from,ippr.request_id,ipprd.distributor_id,(select name from common_distributors cd where cd.distributor_id=ipprd.distributor_id) distributor_name,ipprp.package_id,(select label from inventory_packages ip where ip.id=ipprp.package_id) package_name,ipprp.type_id,ipprp.quantity,ipprp.percase_discount_rate,ipprp.amount, ipprlrb.lrb_type_id, (select label from inventory_products_lrb_types iplrb where iplrb.id=ipprlrb.lrb_type_id) lrb_type_labe FROM pep.inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id=ipprd.product_promotion_id join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprlrb on ipprp.id=ipprlrb.id and ipprp.package_id=ipprlrb.package_id where (( "+Utilities.getSQLDate(ValidFromDate)+" between valid_from and valid_to ) OR ( "+Utilities.getSQLDate(ValidToDate)+" between valid_from and valid_to )) and ipprd.distributor_id in ("+InsertedDistID+") and ippr.is_active=0 and ipprp.package_id in ("+InsertedPackageId+")");
			
			
			ResultSet rs20 = s.executeQuery("SELECT ippr.id, ippr.valid_to, ippr.valid_from, ippr.request_id, ipprd.distributor_id, (select name from common_distributors cd where cd.distributor_id=ipprd.distributor_id) distributor_name, ipprp.package_id, (select label from inventory_packages ip where ip.id=ipprp.package_id) package_name,ipprp.type_id,ipprp.quantity,ipprp.percase_discount_rate,ipprp.amount, ipprlrb.lrb_type_id, (select label from inventory_products_lrb_types iplrb where iplrb.id=ipprlrb.lrb_type_id) lrb_type_labe FROM pep.inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id=ipprd.product_promotion_id join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprlrb on ipprp.id=ipprlrb.id and ipprp.package_id=ipprlrb.package_id where (( "+Utilities.getSQLDate(ValidFromDate)+" between valid_from and valid_to ) OR ( "+Utilities.getSQLDate(ValidToDate)+" between valid_from and valid_to )) and ipprd.distributor_id in ("+InsertedDistID+") and ipprp.package_id in ("+InsertedPackageId+") and ipprlrb.lrb_type_id in ("+InsertedLRBTypes+") and ippr.activated_on is not null");
			if(rs20.first()){
				isValidated = false;
				obj.put("success", "false");
				obj.put("error", "This distribuor has already discount request in the system with Request ID : "+rs20.getLong("request_id"));
				obj.put("errorM","1");
				
				
				obj.put("RequestID", rs20.getString("request_id"));
				obj.put("Distributor", rs20.getString("distributor_id")+" - "+rs20.getString("distributor_name"));
				obj.put("PackageLabel", rs20.getString("package_name"));
				obj.put("LrbType", rs20.getString("lrb_type_labe"));
				obj.put("ValidTo", rs20.getString("valid_to"));
				obj.put("ValidFrom", rs20.getString("valid_from"));
				obj.put("Quantity", rs20.getString("quantity"));
				obj.put("DiscountRate", rs20.getString("percase_discount_rate"));
				obj.put("DiscountAmount", rs20.getString("amount"));
				
			}
			
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		Date MaximumRangePeriodDate = Utilities.getDateByDays(ValidFromDate, 365);
		
		if(ValidToDate.compareTo(MaximumRangePeriodDate) > 0){
			isValidated = false;
			obj.put("success", "false");
			obj.put("error", "Range should not exceed a year");
		}
		
		if(isValidated){
		//if(false){
		
		
		
		
		
		long MasterTablePromotionID = 0;
		long MasterTableSerialID = 0;
		try {
			
			
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			ResultSet rs11 = s.executeQuery("select id from inventory_primary_percase_request where uvid="+UniqueVoucherID);
			if( rs11.first() ){
				
				obj.put("success", "false");
				obj.put("error", "Already Exists");
				
			}else{
				
					if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
					{
					
						s.executeUpdate("insert into inventory_primary_percase_request(uvid,valid_from,valid_to,is_active,created_on,created_by,comments) values("+UniqueVoucherID+","+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+",0,now(),"+UserID+",'"+Comments+"')");
						
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
					int ProductID=0;
					if(SalesPacakgeID != null)
					{
						int RowCount=0;
						//getting max serial number from brand table
						long SerialNo=0;
						
						
						
						for(int i=0;i<SalesPacakgeID.length;i++)
						{					
							
							//calculating variable cost
							
							double CalculatedVariableCost=0;
							
							
							
							
							long[] SalesBrandID1 = Utilities.parseLong(request.getParameterValues("Brands"+SalesPacakgeID[i]+"_"+RowCount));
							if(SalesBrandID1 !=null)
							{
								for(int j=0;j<SalesBrandID1.length;j++)
								{
									//System.out.println("SELECT cost FROM pep.inventory_products_variable_costs ipvc join inventory_products ip on ipvc.sap_code=ip.sap_code join inventory_sales_discounts_request_products_brands isdrpb on isdrpb.product_id=ip.id where isdrpb.package_id="+SalesPacakgeID[i]+" and isdrpb.brand_id="+SalesBrandID1[0]);	
									ResultSet rss1 = s2.executeQuery("SELECT cost FROM inventory_products_variable_costs ipvc join inventory_products ip on ipvc.sap_code=ip.sap_code  where ip.package_id="+SalesPacakgeID[i]+" and ip.brand_id="+SalesBrandID1[0]);
										if(rss1.first()){
											CalculatedVariableCost = rss1.getDouble("cost");
											
										}
										break; //get only first brand	
								}
							}
							
							//System.out.println("hello - "+CalculatedVariableCost);
							
							s.executeUpdate("insert into inventory_primary_percase_request_products(id,package_id,type_id,quantity,percase_discount_rate,amount) values("+MasterTablePromotionID+","+ SalesPacakgeID[i] +",1"+","+Quantity[i]+","+DiscountRate[i]+","+SellingPrice[i]+")"); //type id = 1 for sales 2 for promotions
							ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
							if(rs.first()){
								MasterTableSerialID = rs.getInt(1); 
							}
							
							//now inserting brand 
							long[] SalesBrandID = Utilities.parseLong(request.getParameterValues("Brands"+SalesPacakgeID[i]+"_"+RowCount));
							if(SalesBrandID !=null)
							{
								for(int j=0;j<SalesBrandID.length;j++)
								{
									
									s.executeUpdate("insert into inventory_primary_percase_request_products_lrb_types(id,package_id,lrb_type_id,type_id,serial_no) values("+MasterTablePromotionID+","+ SalesPacakgeID[i] +","+SalesBrandID[j]+",1,"+MasterTableSerialID+")"); //type id = 1 for sales 2 for promotions
								}
							}
							RowCount++;
							SerialNo++;
						}
					}
					
					
					
					
					
					obj.put("product_promotion_id",MasterTablePromotionID);
					obj.put("success", "true");
					
					
					String [] SelectedDistributor=null;
					String [] SelectedDistributorID1=null
							;
					if(request.getParameterValues("DistributorIDhiddenfield") != null)
					{
						 SelectedDistributor = request.getParameterValues("DistributorIDhiddenfield");
						 SelectedDistributorID1 = SelectedDistributor[0].split(",");
						
					}
					
					//System.out.println("Hello - "+SelectedDistributorID1[0]);
					
					//Getting User ID of Forwarded User
					long ProcessUserID = 0;
					
					////ResultSet rs1 = s.executeQuery("select * from workflow_processes_steps where step_id=2 and process_id=5");
					ResultSet rs1 = s.executeQuery("SELECT snd_id FROM common_distributors where distributor_id="+SelectedDistributorID1[0]);
					if(rs1.first()){
						ProcessUserID = rs1.getLong("snd_id");
					}
					
					
					//if(ProcessUserID==3845){ // sd2 is on hajj leaves
						//ProcessUserID = 3628;
					//}
					
					
					Workflow wf = new Workflow();
					long WorkFlowRequestID = wf.createRequest(8, Integer.parseInt(UserID), ProcessUserID, 3, "Per Case Discount Request Raised");
					s.executeUpdate("update inventory_primary_percase_request set request_id="+WorkFlowRequestID+" where id="+MasterTablePromotionID);
					
					ds.commit();
			}
				
				
			s.close();
			s1.close();
			s2.close();
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
