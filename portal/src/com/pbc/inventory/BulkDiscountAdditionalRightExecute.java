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
@WebServlet(description = "User Additional Rights", urlPatterns = { "/inventory/BulkDiscountAdditionalRightExecute" })
public class BulkDiscountAdditionalRightExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
        
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BulkDiscountAdditionalRightExecute() {
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
		
		long BulkDiscountID = Utilities.parseLong(request.getParameter("BulkDiscountIDForWhole"));
		try {
			ds.createConnection();
			Statement s = ds.createStatement();		
		
				//region updation
				
				query = "delete from inventory_spot_discount_regions where spot_discount_id = "+BulkDiscountID;
				s.executeUpdate(query);
				
				if(request.getParameterValues("PCIIDhiddenfield") != null)
				{
					String [] SelectedRegions = request.getParameterValues("PCIIDhiddenfield");						
					for(int ii=0;ii<SelectedRegions.length;ii++)
					{
						String [] FeatureIDndRegionID = SelectedRegions[ii].split(","); //RegionID,Feature ID
						//System.out.println("Region ID "+FeatureIDndRegionID[0]+" Feature ID "+FeatureIDndRegionID[1]);
						//System.out.println(SelectedRegions[ii]);
						query2 = "insert into " +
				                "inventory_spot_discount_regions(spot_discount_id,region_id)" +
				                "values("+BulkDiscountID+","+FeatureIDndRegionID[0]+")";
						//System.out.println(query2);
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
