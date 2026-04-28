package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

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

/**
 * Servlet implementation class UserAdditionalRightExecute
 */
@WebServlet(description = "User Additional Rights", urlPatterns = { "/inventory/ProductPromotionsRequestAdditionalRightExecute" })
public class ProductPromotionsRequestAdditionalRightExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductPromotionsRequestAdditionalRightExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//warehouse updatation
		//first deleting all the records from warehouse against user
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		String query = "";
		String query1 = "";
		String query2 = "";
		
		String UserID = request.getParameter("UserIDSapCode");
		
		long ProductPromotionsID = Utilities.parseLong(request.getParameter("ProductPromotionMasterTableID"));
		try {
			ds.createConnection();
			Statement s = ds.createStatement();		
		
				//region updation
				
				query = "delete from inventory_sales_promotions_request_regions where product_promotion_id = "+ProductPromotionsID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("RegionIDhiddenfield") != null)
				{
					String [] SelectedRegions = request.getParameterValues("RegionIDhiddenfield");						
					for(int ii=0;ii<SelectedRegions.length;ii++)
					{
						String [] FeatureIDndRegionID = SelectedRegions[ii].split(","); //RegionID,Feature ID
						//System.out.println("Region ID "+FeatureIDndRegionID[0]+" Feature ID "+FeatureIDndRegionID[1]);
						//System.out.println(SelectedRegions[ii]);
						query2 = "insert into " +
				                "inventory_sales_promotions_request_regions(product_promotion_id,region_id)" +
				                "values("+ProductPromotionsID+","+FeatureIDndRegionID[0]+")";
						//System.out.println(query2);
						s.executeUpdate(query2);
						
						
						
						
					}
				}
					
				
				//updating Distributor 
				query = "delete from inventory_sales_promotions_request_distributors where product_promotion_id = "+ProductPromotionsID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("DistributorIDhiddenfield") != null)
				{
					String [] SelectedDistributor = request.getParameterValues("DistributorIDhiddenfield");						
					for(int ii=0;ii<SelectedDistributor.length;ii++)
					{
						String [] FeatureIDndDistributorID = SelectedDistributor[ii].split(",");  //DistributorID,FeatureID
						//System.out.println("Distributor ID "+FeatureIDndDistributorID[0]+" Feature ID "+FeatureIDndDistributorID[1]);
						query2 = "insert into " +
				                "inventory_sales_promotions_request_distributors(product_promotion_id,distributor_id)" +
				                "values("+ProductPromotionsID+","+FeatureIDndDistributorID[0]+")";
						s.executeUpdate(query2);
						//System.out.println(query2);
						
						
						
					}
				}
				
				//Distributor Group updation
				//System.out.println("Distributor Group ");
				query = "delete from inventory_sales_promotions_request_pjp where product_promotion_id = "+ProductPromotionsID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("PJPhiddenfield") != null)
				{
					String [] SelectedPJPs = request.getParameterValues("PJPhiddenfield");						
					for(int ii=0;ii<SelectedPJPs.length;ii++)
					{
						//String [] FeatureIDndRegionID = SelectedPJPs[ii].split(","); //GroupID,Feature ID						
						
						query2 = "insert into " +
				                "inventory_sales_promotions_request_pjp(product_promotion_id,pjp_id)" +
				                "values("+ProductPromotionsID+","+SelectedPJPs[ii]+")";
						
						System.out.println(query2);
						
						s.executeUpdate(query2);
					}
				}
				
				
				//updating outlet 
				query = "delete from inventory_sales_promotions_request_outlet where product_promotion_id = "+ProductPromotionsID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("OutletIDhiddenfield") != null)
				{
					String [] SelectedOutlet = request.getParameterValues("OutletIDhiddenfield");						
					for(int ii=0;ii<SelectedOutlet.length;ii++)
					{
						String [] FeatureIDndDistributorID = SelectedOutlet[ii].split(",");  //DistributorID,FeatureID
						//System.out.println("Distributor ID "+FeatureIDndDistributorID[0]+" Feature ID "+FeatureIDndDistributorID[1]);
						query2 = "insert into " +
				                "inventory_sales_promotions_request_outlet(product_promotion_id,outlet_id)" +
				                "values("+ProductPromotionsID+","+FeatureIDndDistributorID[0]+")";
						s.executeUpdate(query2);
						//System.out.println(query2);
						
						
						
					}
				}
				
				//updating Order booker / Order booker 
				query = "delete from inventory_sales_promotions_request_employee where product_promotion_id = "+ProductPromotionsID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("OrderBookerhiddenfield") != null)
				{
					String [] SelectedOrderBooker = request.getParameterValues("OrderBookerhiddenfield");						
					for(int ii=0;ii<SelectedOrderBooker.length;ii++)
					{
						String [] FeatureIDndDistributorID = SelectedOrderBooker[ii].split(",");  //DistributorID,FeatureID
						//System.out.println("Distributor ID "+FeatureIDndDistributorID[0]+" Feature ID "+FeatureIDndDistributorID[1]);
						query2 = "insert into " +
				                "inventory_sales_promotions_request_employee(product_promotion_id,employee_id)" +
				                "values("+ProductPromotionsID+","+FeatureIDndDistributorID[0]+")";
						s.executeUpdate(query2);
						//System.out.println(query2);
						
					}
				}
				
				
				obj.put("success", "true");
				
		s.close();			
		ds.dropConnection();
	} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
		// TODO Auto-generated catch block
		obj.put("success", "false");
		obj.put("error", e.toString());
		e.printStackTrace();
	}
		out.print(obj);
		out.close();
	}

}
