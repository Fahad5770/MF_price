package com.pbc.employee;

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

/**
 * Servlet implementation class UserAdditionalRightExecute
 */
@WebServlet(description = "User Additional Rights", urlPatterns = { "/employee/UserAdditionalRightExecute" })
public class UserAdditionalRightExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserAdditionalRightExecute() {
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
		String FeatureID = request.getParameter("FeatureID");	
		try {
			ds.createConnection();
			Statement s = ds.createStatement();		
		
			query = "delete from `user_access_warehouses` where user_id = "+UserID+" and feature_id="+FeatureID;
			s.executeUpdate(query);
		
								
				if(request.getParameterValues("Warehousehiddenfield") != null)
				{
					String [] SelectedWareHouses = request.getParameterValues("Warehousehiddenfield");
					for(int ii=0;ii<SelectedWareHouses.length;ii++)
					{
						String [] FeatureIDndWarehouseID = SelectedWareHouses[ii].split(","); //WarehouseID,Feature ID
						//System.out.println("Warehouse ID "+FeatureIDndWarehouseID[0]+" Feature ID "+FeatureIDndWarehouseID[1]);
						//System.out.println(SelectedWareHouses[ii]);
						query2 = "insert into " +
				                "`user_access_warehouses`(`user_id`,`feature_id`,`warehouse_id`)" +
				                "values("+UserID+","+FeatureID+","+FeatureIDndWarehouseID[0]+")";
						s.executeUpdate(query2);
						//System.out.println(query2);
						
						
					}
				}
					
				
				//region updation
				
				query = "delete from `user_access_regions` where user_id = "+UserID+" and feature_id="+FeatureID;
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
				                "user_access_regions(user_id,feature_id,region_id)" +
				                "values("+UserID+","+FeatureID+","+FeatureIDndRegionID[0]+")";
						//System.out.println(query2);
						s.executeUpdate(query2);
						
						
						
						
					}
				}
					
				
				//updating Distributor 
				query = "delete from `user_access_distributors` where user_id = "+UserID+" and feature_id="+FeatureID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("DistributorIDhiddenfield") != null)
				{
					String [] SelectedDistributor = request.getParameterValues("DistributorIDhiddenfield");						
					for(int ii=0;ii<SelectedDistributor.length;ii++)
					{
						String [] FeatureIDndDistributorID = SelectedDistributor[ii].split(",");  //DistributorID,FeatureID
						//System.out.println("Distributor ID "+FeatureIDndDistributorID[0]+" Feature ID "+FeatureIDndDistributorID[1]);
						query2 = "insert into " +
				                "`user_access_distributors`(`user_id`,`feature_id`,`distributor_id`)" +
				                "values("+UserID+","+FeatureID+","+FeatureIDndDistributorID[0]+")";
						s.executeUpdate(query2);
						//System.out.println(query2);
						
						
						
					}
				}
				
				//Distributor Group updation
				//System.out.println("Distributor Group ");
				query = "delete from `user_access_distributor_groups` where user_id = "+UserID+" and feature_id="+FeatureID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("GroupDistributorhiddenfield") != null)
				{
					String [] SelectedDistributorGroups = request.getParameterValues("GroupDistributorhiddenfield");						
					for(int ii=0;ii<SelectedDistributorGroups.length;ii++)
					{
						String [] FeatureIDndRegionID = SelectedDistributorGroups[ii].split(","); //GroupID,Feature ID						
						query2 = "insert into " +
				                "user_access_distributor_groups(user_id,feature_id,group_id)" +
				                "values("+UserID+","+FeatureID+","+FeatureIDndRegionID[0]+")";						
						s.executeUpdate(query2);
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
