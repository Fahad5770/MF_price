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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price List ", urlPatterns = { "/distributor/DistributorVehicleExecute" })
public class DistributorVehicleExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorVehicleExecute() {
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
		
		String DistributorVehicleNumber = Utilities.filterString(request.getParameter("VehicleNum"), 1, 100);
		long DistributorVehicleTypeID = Utilities.parseLong(request.getParameter("VehiclesTypeIDSelect"));
		//long DistributorVehicleDriverID = Utilities.parseLong(request.getParameter("DistributorDriverID"));	
		long DistributorVehDistID = Utilities.parseLong(request.getParameter("DistributorVehDistID"));		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
				if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
				{
					s.executeUpdate("insert into distribtuor_vehicles (vehicle_no,type_id,distributor_id) values('"+DistributorVehicleNumber+"',"+DistributorVehicleTypeID+","+DistributorVehDistID+")");					
				} 
				else if(Utilities.parseLong(request.getParameter("isEditCase"))==1) //updation case for master table
				{
					s.executeUpdate("update distribtuor_vehicles set vehicle_no='"+DistributorVehicleNumber+"',type_id="+DistributorVehicleTypeID+" where id="+Utilities.parseLong(request.getParameter("DistVehIDForWhole")));					
				}
				
				obj.put("success", "true");				
			
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			//obj.put("error", e.toString());
			obj.put("error", "Vehicle Number already exists");
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
