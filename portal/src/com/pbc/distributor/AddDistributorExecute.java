package com.pbc.distributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class AddDistributorExecute
 */
@WebServlet("/distributor/AddDistributorExecute")
public class AddDistributorExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDistributorExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);

		/* Data from Form */
		int distributor_id = Utilities.parseInt(request.getParameter("distributor_id"));
		String distributor_name = request.getParameter("distributor_name");
		String address = request.getParameter("address"); 
		int cityid = Utilities.parseInt(request.getParameter("cityid"));
		int regionid = Utilities.parseInt(request.getParameter("regionid"));
		int productid = Utilities.parseInt(request.getParameter("productid"));
		String cityName ="";
		
		
		
		//System.out.println(distributor_id+" "+distributor_name+" "+cityid+" "+regionid+" "+productid+" "+address);
		
		boolean flag = false;
		
		Datasource ds = new Datasource();
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			/*get city name*/
			ResultSet rscity = s.executeQuery("SELECT * FROM common_cities where id="+cityid);
			if(rscity.first()) {
				cityName = rscity.getString("label");
			}
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			ResultSet rs = s.executeQuery("SELECT * FROM common_distributors where distributor_id="+distributor_id);
			
			while(rs.next()) { 
				String temp = rs.getString("distributor_id");
			
				if(distributor_id == rs.getInt("distributor_id"))
				{
					flag = true;
					
				}
			}
			
			//System.out.println(flag);
			
			if(flag == false) {
				s2.executeUpdate("INSERT INTO common_distributors(`distributor_id`, `name`, `name2`, `city`, `region_id`, `address`, `created_on`, `product_group_id`, `type_id`, `month_cycle`, `is_active`,`desk_outlet_id`, `is_order_blocked`, `is_delivery_blocked`, `is_billing_blocked`, `is_central_blocked`,`is_scorecard_enabled`, `is_shifted_to_other_plant`, `city_id`)values('"+distributor_id+"','"+distributor_name+"','"+distributor_name+"','"+cityName+"','"+regionid+"','"+address+"',now(),'"+productid+"',2,1,1,0,0,0,0,0,0,0,'"+cityid+"')");
				obj.put("success", "true");
			}else {
				obj.put("success", "false");
			}
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			s2.close();  
			ds.dropConnection();
	}catch (Exception e) {
		e.printStackTrace();
	}	
	}

}
