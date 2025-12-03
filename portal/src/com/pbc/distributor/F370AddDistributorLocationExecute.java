package com.pbc.distributor;

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

import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Add Distributor Location ", urlPatterns = { "/distributor/F370AddDistributorLocationExecute" })
public class F370AddDistributorLocationExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public F370AddDistributorLocationExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("F370AddDistributorLocationExecute");
		
		HttpSession session = request.getSession();
		 
		String UserID = null;
		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		long DistributorID =Utilities.parseLong(request.getParameter("DistributorID"));
		double[] Lats = Utilities.parseDouble(request.getParameterValues("Latitude1"));
		double[] Lngs=Utilities.parseDouble(request.getParameterValues("Longitude1"));
		
		String[] Description = Utilities.filterString(request.getParameterValues("LocationDescription1"), 1, 1000);
		String[] Address = Utilities.filterString(request.getParameterValues("Address1"), 1, 1000);
		String[] City = Utilities.filterString(request.getParameterValues("City1"), 1, 1000);
		String[] Phone = Utilities.filterString(request.getParameterValues("Phone1"), 1, 1000);
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			

			//s.executeUpdate("update inventory_sales_dispatch set is_liquid_returned=1,liquid_returned_on=now(),liquid_returned_by="+UserID+" where id="+DispatchID);
			
			//System.out.println("Delete from common_distributor_location where distributor_id="+DistributorID);
			s.executeUpdate("Delete from common_distributor_location where distributor_id="+DistributorID);

			if(Lats !=null) {
				int CityID=0;
				
				
				for(int i=0; i<Lats.length; i++) {
					//System.out.println("insert into common_distributor_location (distributor_id,lat, lng,created_on, created_by, location_label) values("+DistributorID+","+Lats[i]+","+Lngs[i]+",now(),"+UserID+",'"+Description[i]+"')");
					s.executeUpdate("insert into common_distributor_location (distributor_id,lat, lng,created_on, created_by, location_label,address,city_id,phone_no) values("+DistributorID+","+Lats[i]+","+Lngs[i]+",now(),"+UserID+",'"+Description[i]+"','"+Address[i]+"',"+ CityID+",'"+Phone[i]+"')");
				}
			}
			
			ds.commit();
			
			

			
			
		
				obj.put("success", "true");
		
			s.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
