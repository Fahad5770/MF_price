package com.pbc.inventory;

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
import org.omg.CORBA.portable.ValueFactory;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;



@WebServlet(description = "Executes Desk Sale", urlPatterns = { "/inventory/SalesPeriodStatusExecute" })
public class SalesPeriodStatusExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SalesPeriodStatusExecute() {
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
		
		
		
		
		String DistributorIDs [] = Utilities.filterString(request.getParameterValues("DistributorIDhiddenfield"),1,300);
		
		int Year = Utilities.parseInt(request.getParameter("Year"));
		int Month = Utilities.parseInt(request.getParameter("Month"));
		String Remarks = Utilities.filterString(request.getParameter("remarks"), 1, 300);
		
		int ActualMonthDB=Month+1;
		
		Date StartDate = Utilities.getStartDateByMonth(Month, Year); //bec java month start from 0
		Date EndDate = Utilities.getEndDateByMonth(Month, Year);
		
		
		Date CurrentDate = new Date();
		int CurrentMonth = Utilities.getMonthNumberByDate(CurrentDate);
		int CurrentYear = Utilities.getYearByDate(CurrentDate);
		//System.out.println(PeriodID);
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		
		
		try {
			
			ds.createConnection();
			ds.startTransaction();			
			
			System.out.println(ActualMonthDB+" - "+CurrentMonth);
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			boolean MainFlag=false;
			
			System.out.println(Year+" - "+CurrentYear);
			
			if(Year==2020 && CurrentYear==2021 && ActualMonthDB==12 && CurrentMonth==1) { //for year closing of december 2020 on Jan 2021
				MainFlag=true;
			}else if(ActualMonthDB<=CurrentMonth || CurrentYear==Year) {
				MainFlag=true;
			}
			
			if(MainFlag) {
				if(DistributorIDs!=null) {
					for(int i=0;i<DistributorIDs.length;i++) {
						boolean shouldIgnore=false;
						String [] iDistributorID = DistributorIDs[i].split(",");
						
						if(!iDistributorID[0].equals("")) {
							
							System.out.println("I am innnnnnn!");
							ResultSet rs = s.executeQuery("select * from inventory_sales_period_status where month_number="+ActualMonthDB+" and year_number="+Year+" and distributor_id="+iDistributorID[0]);
							if(rs.first()) {
								shouldIgnore=true;
							}else {
								s.executeUpdate("insert into inventory_sales_period_status(start_date,end_date,month_number,year_number,remarks,distributor_id) values("+Utilities.getSQLDate(StartDate)+","+Utilities.getSQLDate(EndDate)+","+ActualMonthDB+","+Year+",'"+Remarks+"',"+iDistributorID[0]+")");
								
								s.executeUpdate("insert into inventory_sales_period_status_log(start_date,end_date,month_number,year_number,remarks,distributor_id,status_changed_on,status_changed_by,is_open) values("+Utilities.getSQLDate(StartDate)+","+Utilities.getSQLDate(EndDate)+","+ActualMonthDB+","+Year+",'"+Remarks+"',"+iDistributorID[0]+",now(),"+UserID+",1)");
								
							}
							
							
						}
						
					}
					
					
					ds.commit();
					s1.close();
					s.close();
					ds.dropConnection();
					
					obj.put("success", "true");
					
				}else {
					obj.put("success", "false");
					obj.put("error", "Please select distributor");
				}
			}else {
				obj.put("success", "false");
				obj.put("error", "Please select proper month and year");
			}
			
			
			
			
			
			
			
			//s.executeUpdate("insert into inventory_sales_period_status_log(start_date,end_date,month_number,year_number,closed_on,closed_by,remarks,status_changed_on,status_changed_by) select start_date,end_date,month_number,year_number,closed_on,closed_by,remarks,now(),"+UserID+" from inventory_sales_period_status where id="+PeriodID);
			
			
			
			
		} catch (Exception e) {
			
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			obj.put("success", "false");
			obj.put("exception", e);
			e.printStackTrace();
			//out.print(e);
		}finally{
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println("before end close");
		out.print(obj);
		out.close();
		//System.out.println("after end close");
		
	}
	
	private String getProductName(Statement s, int ProductID) throws SQLException{
		
		String ProductName = "";
		ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product_name FROM pep.inventory_products_view where product_id="+ProductID);
		if(rs.first()){
			ProductName = rs.getString("product_name");
		}
		
		return ProductName;
		
	}
	
}