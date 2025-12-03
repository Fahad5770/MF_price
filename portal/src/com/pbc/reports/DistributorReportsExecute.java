package com.pbc.reports;

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


@WebServlet(description = "Distributpr Reports Execute ", urlPatterns = { "/reports/DistributorReportsExecute" })
public class DistributorReportsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DistributorReportsExecute() {
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
		long SelectedPackages[]=null;
		long SelectedBrands[]=null;
		long SelectedDistributor[]=null;
		long SelectedOrderBookers[]=null;
		long SelectedVehicles[]=null;
		long SelectedEmployees[]=null;
		long SelectedOutlets[]=null;
		long SelectedPJP[]=null;
		long SelectedHOD[]=null;
		Date StartDate;
		Date EndDate;
		PrintWriter out = response.getWriter();
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==1) // 1 for package
		{
			 SelectedPackages=Utilities.parseLong(request.getParameterValues("PackagesCheckBox"));
			 session.setAttribute( "SR1SelectedPackages", SelectedPackages );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==2) // 2 for brand
		{
			SelectedBrands=Utilities.parseLong(request.getParameterValues("BrandsCheckBox"));
			session.setAttribute( "SR1SelectedBrands", SelectedBrands );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==3) // 3 for distributor
		{
			SelectedDistributor=Utilities.parseLong(request.getParameterValues("DistributorCheckBox"));
			session.setAttribute( "SR1SelectedDistributors", SelectedDistributor );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==4) // 4 for Order Bookers
		{
			SelectedOrderBookers=Utilities.parseLong(request.getParameterValues("OrderBookerCheckBox"));
			session.setAttribute( "SR1SelectedOrderBookers", SelectedOrderBookers );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==6) // 4 for Order Bookers
		{
			SelectedVehicles=Utilities.parseLong(request.getParameterValues("VehicleCheckBox"));
			session.setAttribute( "SR1SelectedVehicles", SelectedVehicles );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==7) // 4 for Order Bookers
		{
			SelectedEmployees=Utilities.parseLong(request.getParameterValues("EmployeeCheckBox"));
			session.setAttribute( "SR1SelectedEmployees", SelectedEmployees );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==8) //8 for Outlets
		{
			SelectedOutlets=Utilities.parseLong(request.getParameterValues("OutletCheckBox"));
			session.setAttribute( "SR1SelectedOutlets", SelectedOutlets );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==9) //9 for pjp
		{
			SelectedPJP=Utilities.parseLong(request.getParameterValues("PJPCheckbox"));
			session.setAttribute( "SR1SelectedPJP", SelectedPJP );
		}
		if(Utilities.parseInt(request.getParameter("FilterType"))==10) //9 for HOD
		{
			SelectedHOD=Utilities.parseLong(request.getParameterValues("HODCheckbox"));
			session.setAttribute( "SR1SelectedHOD", SelectedHOD );
		}
		
		if(Utilities.parseInt(request.getParameter("FilterType"))==5) // 5for Date Range
		{
			StartDate = Utilities.parseDate(request.getParameter("StartDate"));
			EndDate = Utilities.parseDate(request.getParameter("EndDate"));
			String SelectedDateType = Utilities.filterString(request.getParameter("SelectedDateType"), 1, 20);
			
			//SelectedOrderBookers=Utilities.parseLong(request.getParameterValues("OrderBookerCheckBox"));
			session.setAttribute( "SR1StartDate", StartDate );
			session.setAttribute( "SR1EndDate", EndDate );
			session.setAttribute( "SR1DateType", SelectedDateType );
			//System.out.print(SelectedDateType);
		
		}
		
		
		
		/*for(int i=0;i<SelectedPackages.length;i++)
		{
			System.out.println("Hello "+SelectedPackages[i]);
		}*/
		
		
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
