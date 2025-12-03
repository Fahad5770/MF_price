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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class UserAdditionalRightExecute
 */
@WebServlet(description = "User Additional Rights", urlPatterns = { "/inventory/PriceListAdditionalRightExecute" })
public class PriceListAdditionalRightExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PriceListAdditionalRightExecute() {
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
		
		long PriceListID = Utilities.parseLong(request.getParameter("PriceListIDForWhole"));
		try {
			ds.createConnection();
			Statement s = ds.createStatement();		
		
				//region updation
				
				query = "delete from inventory_price_list_regions where price_list_id = "+PriceListID;
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
				                "inventory_price_list_regions(price_list_id,region_id)" +
				                "values("+PriceListID+","+FeatureIDndRegionID[0]+")";
						//System.out.println(query2);
						s.executeUpdate(query2);
						
						
						
						
					}
				}
					
				
				//updating Distributor 
				query = "delete from inventory_price_list_distributors where price_list_id = "+PriceListID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("DistributorIDhiddenfield") != null)
				{
					String [] SelectedDistributor = request.getParameterValues("DistributorIDhiddenfield");						
					for(int ii=0;ii<SelectedDistributor.length;ii++)
					{
						String [] FeatureIDndDistributorID = SelectedDistributor[ii].split(",");  //DistributorID,FeatureID
						//System.out.println("Distributor ID "+FeatureIDndDistributorID[0]+" Feature ID "+FeatureIDndDistributorID[1]);
						query2 = "insert into " +
				                "inventory_price_list_distributors(price_list_id,distributor_id)" +
				                "values("+PriceListID+","+FeatureIDndDistributorID[0]+")";
						s.executeUpdate(query2);
						//System.out.println(query2);
						
						
						
					}
				}
				
				//Distributor Group updation
				//System.out.println("Distributor Group ");
				query = "delete from inventory_price_list_distributor_groups where price_list_id = "+PriceListID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("GroupDistributorhiddenfield") != null)
				{
					String [] SelectedDistributorGroups = request.getParameterValues("GroupDistributorhiddenfield");						
					for(int ii=0;ii<SelectedDistributorGroups.length;ii++)
					{
						String [] FeatureIDndRegionID = SelectedDistributorGroups[ii].split(","); //GroupID,Feature ID						
						query2 = "insert into " +
				                "inventory_price_list_distributor_groups(price_list_id,group_id)" +
				                "values("+PriceListID+","+FeatureIDndRegionID[0]+")";						
						s.executeUpdate(query2);
					}
				}
				
				
				//updating PJP 
				query = "delete from inventory_price_list_pjp where price_list_id = "+PriceListID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("PJPIDhiddenfield") != null)
				{
					String [] SelectedPJPID = request.getParameterValues("PJPIDhiddenfield");						
					for(int ii=0;ii<SelectedPJPID.length;ii++)
					{
						String [] PJPID = SelectedPJPID[ii].split(",");  //PJPID
						System.out.println("PJP ID "+PJPID[0]);
						query2 = "insert into " +
				                "inventory_price_list_pjp(price_list_id,pjp_id)" +
				                "values("+PriceListID+","+PJPID[0]+")";
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
