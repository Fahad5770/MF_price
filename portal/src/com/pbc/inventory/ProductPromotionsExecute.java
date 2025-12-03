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

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price List ", urlPatterns = { "/inventory/ProductPromotionsExecute" })
public class ProductPromotionsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductPromotionsExecute() {
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
		
		
		//Master table
		String PriceListName = Utilities.filterString(request.getParameter("ProductPromotionsLabel"), 1, 20);
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
		double[] PromotionsRaweCases = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormRawCases"));
		double[] PromotionsUnits = Utilities.parseDouble(request.getParameterValues("ProductPromotionsPMainFormUnits"));
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		long MasterTablePromotionID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
				
				
				if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
				{
				
					s.executeUpdate("insert into inventory_sales_promotions(label,valid_from,valid_to,is_active,created_on,created_by) values('"+PriceListName+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+Active+",now(),"+UserID+")");
					
					//getting pricelist id
					
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						MasterTablePromotionID = rs.getInt(1); 
					}
					
				} 
				else if(Utilities.parseLong(request.getParameter("isEditCase"))==1) //updation case for master table
				{					
					long ProductPromotionsMasterTableID = Utilities.parseLong(request.getParameter("ProductPromotionMasterTableID"));
					//System.out.println("I am in edit case "+ProductPromotionsMasterTableID);
					s.executeUpdate("update inventory_sales_promotions set label='"+PriceListName+"',valid_from="+Utilities.getSQLDate(ValidFromDate)+",valid_to="+Utilities.getSQLDate(ValidToDate)+",is_active="+Active+" where id="+ProductPromotionsMasterTableID);
					
					s.executeUpdate("delete from inventory_sales_promotions_products where id="+ProductPromotionsMasterTableID); //deleting previous records from detail table
					s.executeUpdate("delete from inventory_sales_promotions_products_brands where id="+ProductPromotionsMasterTableID); //deleting previous records from detail table
					MasterTablePromotionID = ProductPromotionsMasterTableID;
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
						
						s.executeUpdate("insert into inventory_sales_promotions_products(id,package_id,raw_cases,units,type_id,total_units) values("+MasterTablePromotionID+","+ SalesPacakgeID[i] +","+SalesRaweCases[i]+","+SalesUnits[i]+",1,"+TotalUnits+")"); //type id = 1 for sales 2 for promotions

						//now inserting brand 
						long[] SalesBrandID = Utilities.parseLong(request.getParameterValues("Brands"+SalesPacakgeID[i]));
						if(SalesBrandID !=null)
						{
							for(int j=0;j<SalesBrandID.length;j++)
							{
								s.executeUpdate("insert into inventory_sales_promotions_products_brands(id,package_id,brand_id,type_id) values("+MasterTablePromotionID+","+ SalesPacakgeID[i] +","+SalesBrandID[j]+",1)"); //type id = 1 for sales 2 for promotions
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
						s.executeUpdate("insert into inventory_sales_promotions_products(id,package_id,raw_cases,units,type_id,total_units) values("+MasterTablePromotionID+","+ PromotionsPacakgeID[i] +","+PromotionsRaweCases[i]+","+PromotionsUnits[i]+",2,"+TotalUnits+")"); //type id = 1 for sales 2 for promotions
						

						//now inserting brand 
						long[] SalesBrandID = Utilities.parseLong(request.getParameterValues("PromotionBrand"+PromotionsPacakgeID[i]));
						if(SalesBrandID !=null)
						{
							for(int j=0;j<SalesBrandID.length;j++)
							{
								s.executeUpdate("insert into inventory_sales_promotions_products_brands(id,package_id,brand_id,type_id) values("+MasterTablePromotionID+","+ PromotionsPacakgeID[i] +","+SalesBrandID[j]+",2)"); //type id = 1 for sales 2 for promotions
							}
						}
					}
				}
				
				
				obj.put("product_promotion_id",MasterTablePromotionID);
				obj.put("success", "true");
				ds.commit();
				
				
				// Update promotions cache
				BiProcesses bip = new BiProcesses();
				bip.createPromotionsCache();
				bip.close();
				
				
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
