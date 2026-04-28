package com.pbc.outlet;

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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

@WebServlet(description = "Aprrove Outlet", urlPatterns = { "/outlet/OutletRequestUpdateExecuteNew" })
public class OutletRequestUpdateExecuteNew extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OutletRequestUpdateExecuteNew() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		
		System.out.println("here");
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		
	//int OutletIDnew = Utilities.parseInt(request.getParameter("OutletIDnew"));
		int id_for_update = Utilities.parseInt(request.getParameter("id_for_update"));
		int ID = Utilities.parseInt(request.getParameter("ID"));
		long DistributorID = Utilities.parseLong(request.getParameter("distributorID"));
		int declineOutletFlag = Utilities.parseInt(request.getParameter("declineFlag"));
		String ContactNum = Utilities.filterString(request.getParameter("OutletContactNo"), 1, 300);			
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 300);
		String OutletAddress = Utilities.filterString(request.getParameter("OutletAddress"), 1, 1000);
		String OwnerName = Utilities.filterString(request.getParameter("OwnerName"), 1, 300);	
		String CnicNum = Utilities.filterString(request.getParameter("CNICNo"), 1, 100);
		String SubChannel = Utilities.filterString(request.getParameter("channelID"), 1, 100);
		double lat = Utilities.parseDouble(request.getParameter("lat"));
		double lng = Utilities.parseDouble(request.getParameter("lng"));
		String Area = Utilities.filterString(request.getParameter("OutletArea"), 1, 300);			
		String SubArea = Utilities.filterString(request.getParameter("OutletSubArea"), 1, 300);
		String PurchaserName = Utilities.filterString(request.getParameter("PurchaserName"), 1, 1000);
		String PurchaserMobile = Utilities.filterString(request.getParameter("PurchaserMobile"), 1, 1000);

		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
			
		
		try {
		
		
			ds.createConnection();
		
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3=ds.createStatement();
			Statement s4=ds.createStatement();
			Statement s5=ds.createStatement();
			Statement s6=ds.createStatement();
			Statement s7=ds.createStatement();
			Statement s8=ds.createStatement();
			Statement s9=ds.createStatement();
			Statement s10=ds.createStatement();
		
			if (declineOutletFlag==0) {
				System.out.println("=====================");
				System.out.println("idddd"+Utilities.parseInt(request.getParameter("ID")));
				System.out.println( Utilities.parseLong(request.getParameter("distributorID")));
				System.out.print("id_for_update "+id_for_update);
				if(id_for_update == 0) {
					 //String UpdateCO = ;
						System.out.println("update common_outlets_request set outlet_name='"+OutletName+"',outlet_address='"+OutletAddress+"',owner_cnic_number='"+CnicNum+"',area_label='"+Area+"',sub_area_label='"+SubArea+"',purchaser_name='"+PurchaserName+"',purchaser_number='"+PurchaserMobile+"',lat="+lat+",lng="+lng+" where id="+ID);
						s2.executeUpdate("update common_outlets_request set outlet_name='"+OutletName+"',outlet_address='"+OutletAddress+"',owner_cnic_number='"+CnicNum+"',area_label='"+Area+"',sub_area_label='"+SubArea+"',purchaser_name='"+PurchaserName+"',purchaser_number='"+PurchaserMobile+"',lat="+lat+",lng="+lng+" where id="+ID);
						 ds.commit(); 
						obj.put("success", "true");
							obj.put("OutletIDGen", ID);
				}else {
					System.out.println("inside else");
					System.out.println("Select *from common_outlets where id="+id_for_update);
					 ResultSet rs1 = s.executeQuery("Select *from common_outlets where id="+id_for_update);
					 
					 if(rs1.first()) {
						// System.out.println("update common_outlets set name="+OutletName+",address='"+OutletAddress+"',cache_contact_nic='"+CnicNum+"',area_label='"+Area+"',sub_area_label='"+SubArea+"',purchaser_name='"+PurchaserName+"',purchaser_mobile_no='"+PurchaserMobile+"' where id="+id_for_update);
						// System.out.println("update common_outlets_contacts set contact_name='"+OwnerName+"',contact_number='"+ContactNum+"',contact_nic='"+CnicNum+"'  outlet_id="+id_for_update);
						 
						 String UpdateCO = "update common_outlets_request set outlet_name='"+OutletName+"',outlet_address='"+OutletAddress+"',owner_cnic_number='"+CnicNum+"',area_label='"+Area+"',sub_area_label='"+SubArea+"',purchaser_name='"+PurchaserName+"',purchaser_number='"+PurchaserMobile+"',lat="+lat+",lng="+lng+" where id="+id_for_update;
						System.out.println(UpdateCO);
						s2.executeUpdate(UpdateCO);
						 //String UpdateCOC = "update common_outlets_contacts set contact_name='"+OwnerName+"',contact_number='"+ContactNum+"',contact_nic='"+CnicNum+"' where outlet_id="+id_for_update;
						// System.out.println(UpdateCOC);
						 //s2.executeUpdate(UpdateCOC);
					
						 
						 
						 //String UpdateOM = "update outletmaster set Outlet_Name='"+OutletName+"',Address='"+OutletAddress+"',Owner='"+OwnerName+"',Telepohone='"+ContactNum+"' where Outlet_ID="+id_for_update;
							//System.out.println(UpdateOM);
							// s2.executeUpdate(UpdateOM);
						// System.out.println("Update common_outlets_request set is_approved=1 where id="+ID+"");
						// s2.executeUpdate("Update common_outlets_request set is_approved=1 where id="+ID+"");
						 ds.commit();
						 obj.put("success", "true");
							obj.put("OutletIDGen", ID);
					 }
				}
	 }else { // decline case					
				//	System.out.println("Update common_outlets_request set is_declined=1 where id=" + ID + "");
				/*
				 * s.executeUpdate("Update common_outlets_request set is_declined=1 where id=" +
				 * ID + ""); ds.commit(); obj.put("success", "true");
				 */
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
					
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
