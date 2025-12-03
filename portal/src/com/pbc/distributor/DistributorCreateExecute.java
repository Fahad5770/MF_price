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


@WebServlet(description = "Price List ", urlPatterns = { "/distributor/DistributorCreateExecute" })
public class DistributorCreateExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorCreateExecute() {
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
		
		
		int distributorId = Utilities.parseInt(request.getParameter("DistributorID"));
		String distributorName = request.getParameter("DistributorName");
		String address = request.getParameter("Address");
		int cityId = Utilities.parseInt(request.getParameter("city"));
		int regionId = Utilities.parseInt(request.getParameter("region"));
		String cityLabel = request.getParameter("cityLabel");
		
	//	System.out.println(distributorId+" "+distributorName+" "+address+" "+regionId+" "+cityId+" "+cityLabel);
		
	
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			s.executeUpdate("insert into common_distributors (`distributor_id`,`name`,`name2`,`city`,`region_id`,`route`,`address`,`contact_no`,`created_on`,`product_group_id`,`type_id`,`month_cycle`,`is_active`,`snd_id`,`rsm_id`,`kpo_id`,`desk_outlet_id`,`is_order_blocked`,`is_delivery_blocked`,`is_billing_blocked`,`is_central_blocked`,`category_id`,`tdm_id`,`is_scorecard_enabled`,`email_address`,`is_shifted_to_other_plant`,`city_id`) "
							+ "values("+distributorId+", '"+distributorName+"', '"+distributorName+"', '"+cityLabel+"', "+regionId+",null,'"+address+"', null, now(), 116, 1, 1, 1, null, null, null, 0, 0, 0, 0, 0, null, null, 0, null, 0, "+cityId+")");				
				
				
				obj.put("success", "true");				
			
			s.close();
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			//obj.put("error", e.toString());
			obj.put("error", "Distributor is not created");
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
