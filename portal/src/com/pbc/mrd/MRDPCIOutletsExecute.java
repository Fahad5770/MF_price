package com.pbc.mrd;

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
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;


@WebServlet(description = "MRD PCI Outlets Execute", urlPatterns = { "/mrd/MRDPCIOutletsExecute" })
public class MRDPCIOutletsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MRDPCIOutletsExecute() {
        super();
        //System.out.println("contructor");
    }
    

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//System.out.println("service");
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("UserID") == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
		long OutletID = Utilities.parseLong(request.getParameter("OutletID"));
		
		String OutletName = Utilities.filterString(request.getParameter("OutletName"), 1, 100);
		String Address = Utilities.filterString(request.getParameter("Address"), 1, 100);
		String Comments = Utilities.filterString(request.getParameter("Comments"), 1, 100);
		String ContactPerson = Utilities.filterString(request.getParameter("ContactPerson"), 1, 100);
		String Phone = Utilities.filterString(request.getParameter("Phone"), 1, 100);
		
		long UserOutletID = Utilities.parseLong(request.getParameter("UserOutletID"));
		
		Datasource ds = new Datasource();
		 
		try { 
			
			ds.createConnectionToMRD();
			
			Statement s = ds.createStatement();
			
			response.setContentType("application/json");
			JSONObject obj=new JSONObject();
			
			String SQL = "update pcioutletsverification set OutletID="+UserOutletID+", SHOP_NAME='"+OutletName+"', comments='"+Comments+"', KEY_CONTACT_PERSON='"+ContactPerson+"', mobile_no='"+Phone+"', changed_on=now(), changed_by="+SessionUserID+" where ID="+OutletID;
			//String SQL = "update outlets set user_outlet_id="+UserOutletID+", outlet_name='"+OutletName+"', changed_on=now(), changed_by="+SessionUserID+" where outlet_id="+OutletID;
			s.executeUpdate(SQL);
			obj.put("success", "true"); 
			
			
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
			
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
