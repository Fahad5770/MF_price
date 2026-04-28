package com.pbc.tot;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


@WebServlet(description = "Get the information again outlet for cooler injection", urlPatterns = { "/tot/TOTCoolerInjectionGetCoInjInfoJson" })
public class TOTCoolerInjectionGetCoInjInfoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TOTCoolerInjectionGetCoInjInfoJson() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}		
		
		long ProductCoolerInjectionOutletID = Utilities.parseLong(request.getParameter("ProductCoolerInjectionOutletID"));		
		
		//System.out.println("brrrr - "+ProductCoolerInjectionOutletID);
		
		Datasource ds = new Datasource();
		
		try { 
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			JSONArray jr = new JSONArray();
			JSONArray BrandsJSONArray = new JSONArray();
			JSONArray ExistingTOTJSONArray = new JSONArray();
			
			ResultSet rs = s.executeQuery("SELECT co.id,co.name,co.address,co.region_id,(select region_name from common_regions cr where cr.region_id=co.region_id) region_name,(select region_short_name from common_regions cr where cr.region_id=co.region_id) region_short_name,co.cache_rsm_id,(select display_name from users u where u.id=co.cache_rsm_id) rsm_name,co.cache_snd_id,(select display_name from users u where u.id=co.cache_snd_id) snd_name,co.distributor_id,(select name from common_distributors cd where cd.distributor_id=co.distributor_id) distributor_name,(select coc.contact_name from common_outlets_contacts coc where coc.outlet_id=co.id) outlet_contact_name,(select coc.contact_number from common_outlets_contacts coc where coc.outlet_id=co.id) outlet_contact_number,co.channel_id,(select label from common_outlets_channels cocc where cocc.id=co.channel_id) channel_name,(select coc.contact_nic from common_outlets_contacts coc where coc.outlet_id=co.id) outlet_contact_nic FROM common_outlets co where co.id= "+ProductCoolerInjectionOutletID);
			if (rs.first()){
				
				
				obj.put("outlet_id", rs.getLong("id"));
				obj.put("outlet_name", rs.getString("name"));
				obj.put("outlet_address", rs.getString("address"));
				obj.put("outlet_region_id", rs.getLong("region_id"));
				obj.put("outlet_region_name", rs.getString("region_short_name")+" - "+rs.getString("region_name"));
				obj.put("outlet_rsm_id", rs.getLong("cache_rsm_id"));
				obj.put("outlet_rsm_name", rs.getString("rsm_name"));
				obj.put("outlet_snd_id", rs.getLong("cache_snd_id"));
				obj.put("outlet_snd_name", rs.getString("snd_name"));
				obj.put("outlet_distributor_id", rs.getLong("distributor_id"));
				obj.put("outlet_distributor_name", rs.getString("distributor_name"));
				obj.put("outlet_contact_name", rs.getString("outlet_contact_name"));
				obj.put("outlet_contact_number", rs.getLong("outlet_contact_number"));
				obj.put("outlet_channel_id", rs.getLong("channel_id"));
				obj.put("outlet_channel_name", rs.getString("channel_name"));
				obj.put("outlet_contact_nic", rs.getString("outlet_contact_nic"));
				
				
				//beat plan info
				
				ResultSet rs3 = s.executeQuery("SELECT * FROM employee_beat_plan_schedule where outlet_id="+ProductCoolerInjectionOutletID+" order by beat_plan_id desc");
				while(rs3.next()){
					LinkedHashMap rows = new LinkedHashMap();
					
					rows.put("day_number", rs3.getLong("day_number"));
					
					jr.add(rows);
				}
				
				ResultSet rs32= s2.executeQuery("SELECT inventory_number,agreement_record,rnd_verification,250ml_quantity,litre_quantity,tot_status,movement_date,main_asset_number FROM common_assets where outlet_id_parsed='"+ProductCoolerInjectionOutletID+"' order by movement_date_parsed desc");
				while(rs32.next()){
					
					LinkedHashMap rows1 = new LinkedHashMap();
					
					rows1.put("main_asset_number", rs32.getString("main_asset_number"));
					rows1.put("inventory_number", rs32.getString("inventory_number"));
					rows1.put("tot_status", rs32.getString("tot_status"));
					rows1.put("movement_date", rs32.getString("movement_date"));
					
					
					ExistingTOTJSONArray.add(rows1);
				}
				
				
				
				obj.put("rows", jr);
				obj.put("rows1", ExistingTOTJSONArray);
				obj.put("exists", "true");
				
				
			}else{

				obj.put("exists", "false");

			}
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
}
