package com.pbc.sampling;

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


@WebServlet(description = "Monthly Discount Main Execute", urlPatterns = { "/sampling/MonthlyDiscountMainExecute" })
public class MonthlyDiscountMainExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MonthlyDiscountMainExecute() {
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
		
		long SelectedRegions[]=null;
		long SelectedDistributors[]=null;
		String SelectedOutletString = "";
		int SelectedMonth = 0;
		int SelectedYear = 0;
		int SelectedStatus = 0;
		
		PrintWriter out = response.getWriter();
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==1)
		{
			 SelectedRegions = Utilities.parseLong(request.getParameterValues("RegionCheckBox"));
			 
			 session.setAttribute( "MonthlyDiscountRegions", SelectedRegions );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==2)
		{
			SelectedDistributors = Utilities.parseLong(request.getParameterValues("DistributorCheckBox"));
			
			 session.setAttribute( "MonthlyDiscountDistributors", SelectedDistributors );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==3)
		{
			 SelectedOutletString = Utilities.filterString(request.getParameter("MonthlyDiscountMainOutlets"), 1, 100);
			 session.setAttribute( "MonthlyDiscountOutlets", SelectedOutletString );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==4)
		{
			SelectedMonth = Utilities.parseInt(request.getParameter("month"));
			SelectedYear = Utilities.parseInt(request.getParameter("year"));
			
			session.setAttribute( "MonthlyDiscountMonth", SelectedMonth );
			session.setAttribute( "MonthlyDiscountYear", SelectedYear );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==5)
		{
			SelectedStatus = Utilities.parseInt(request.getParameter("status"));
			
			session.setAttribute( "MonthlyDiscountStatus", SelectedStatus );
			
		}
		
		
		JSONObject obj = new JSONObject();		
		try {
			
			obj.put("success", "true");				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("success", "false");
			//obj.put("error", e.toString());
			obj.put("error", "");
			e.printStackTrace();
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
