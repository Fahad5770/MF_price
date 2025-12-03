package com.pbc.inventory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
@WebServlet(description = "User Additional Rights", urlPatterns = { "/inventory/PriceListGetActivePriceListInfoJson" })
public class PriceListGetActivePriceListInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PriceListGetActivePriceListInfoJson() {
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
		
		long RegionID = Utilities.parseLong(request.getParameter("RegionID"));		
		long DistributorGroupID = Utilities.parseLong(request.getParameter("DistributorGroupID"));
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
		long PJPID = Utilities.parseLong(request.getParameter("PJPID"));
		
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();	
			ResultSet rs=null;
			if(RegionID != 0)  //for region
			{
				rs = s.executeQuery("SELECT * FROM pep.inventory_price_list_regions ipr,inventory_price_list ipl where ipr.price_list_id=ipl.id and ipr.region_id="+RegionID);	
				
			}
			
			if(DistributorGroupID != 0)  //for Distributor Group
			{
				 rs = s.executeQuery("SELECT * FROM inventory_price_list_distributor_groups ipr,inventory_price_list ipl where ipr.price_list_id=ipl.id and ipr.group_id="+DistributorGroupID);	
				
			}
			
			if(DistributorID != 0)  //for Distributor 
			{
				 rs = s.executeQuery("SELECT * FROM inventory_price_list_distributors ipr,inventory_price_list ipl where ipr.price_list_id=ipl.id and ipr.distributor_id="+DistributorID);	
				
			}
			
			if(PJPID != 0)  //for PJP 
			{
				 rs = s.executeQuery("SELECT * FROM inventory_price_list_pjp ipr,inventory_price_list ipl where ipr.price_list_id=ipl.id and ipr.pjp_id="+PJPID);	
				
			}
			
			if(rs.first())
			{
				if(rs.getInt("is_active")==1)
				{
					obj.put("region_exist", "true");
					obj.put("success", "false");
					obj.put("price_list_name", rs.getString("label"));
				}
			}
			
				
				
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
