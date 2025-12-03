package com.pbc.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

@WebServlet(description = "Mobile Authention For Map", urlPatterns = { "/mobile/MobileOutletsForMap" })
public class MobileOutletsForMap extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MobileOutletsForMap() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("json");

		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();

		JSONArray jr = new JSONArray();
	

		System.out.println("Mobile Authention For Map");

		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 30000));

		if (!mr.isExpired()) {
			
			int city_id = Utilities.parseInt(mr.getParameter("selectedCityId"));

			System.out.println("Mobile Outlets For Map User:" + city_id);

			Datasource ds = new Datasource();
			try {

				ds.createConnection();

				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();

				
//					System.out.println(
//							"select Is_Geo_Fence,Geo_Radius,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label, co.id, co.name outlet_name,  coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,  IFNULL(co.pic_channel_id, 0) pic_channel_id from common_outlets co join common_outlets_contacts coc on co.id = coc.outlet_id and co.is_active=1 and city_id="+city_id);
					ResultSet rs3 = s2.executeQuery(
							"select Is_Geo_Fence,Geo_Radius,(SELECT label from pci_sub_channel psc where psc.id=co.pic_channel_id) as channel_label, co.id, co.name outlet_name,  coc.contact_name owner, co.address, coc.contact_number telepohone, co.nfc_tag_id, co.lat, co.lng, co.area_label, co.sub_area_label,  IFNULL(co.pic_channel_id, 0) pic_channel_id from common_outlets co join common_outlets_contacts coc on co.id = coc.outlet_id and co.is_active=1 and city_id="+city_id);
					while (rs3.next()) {
						LinkedHashMap rows = new LinkedHashMap();
						rows.put("OutletID", rs3.getString("id"));
						rows.put("OutletName", rs3.getString("outlet_name"));
						rows.put("DayNumber", "0");
						rows.put("Owner", rs3.getString("owner"));
						rows.put("Address", rs3.getString("address"));
						rows.put("Telepohone", rs3.getString("telepohone"));
						rows.put("NFCTagID", "0");
						rows.put("order_created_on_date", "");
						rows.put("SUBChannelLabel", rs3.getString("channel_label"));
						rows.put("lat", rs3.getString("lat"));
						rows.put("lng", rs3.getString("lng"));
						rows.put("IsGeoFence", rs3.getInt("Is_Geo_Fence"));
						rows.put("Radius", rs3.getInt("Geo_Radius"));
						rows.put("AreaLabel", rs3.getString("area_label"));
						rows.put("", rs3.getString("sub_area_label"));
						rows.put("IsAlternative", 0);
						rows.put("OutletPciSubChannelID", rs3.getInt("pic_channel_id"));
						jr.add(rows);
					}
					json.put("BeatPlanRows", jr);

					json.put("success", "true");
					
					s2.close();
					s.close();
					ds.dropConnection();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			json.put("success", "false");
			json.put("error_code", "101");
		}
	//	System.out.println(json);
		out.print(json);
		out.close();

	}

}