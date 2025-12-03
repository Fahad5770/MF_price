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

import org.json.simple.JSONObject;

import com.mysql.jdbc.PreparedStatement;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Executes GRN", urlPatterns = { "/inventory/ProductPerCaseDiscountsRequestDeactivateExecute" })
public class ProductPerCaseDiscountsRequestDeactivateExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductPerCaseDiscountsRequestDeactivateExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Hello ");
		HttpSession session = request.getSession();
		
		//Made by Ferhan
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
			System.out.println(UserID);
		}
		
		
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		int res_Id=Utilities.parseInt(request.getParameter("req"));
		String reason=Utilities.filterString(request.getParameter("res"),1, 300);
		Date currdate=new Date();
		String msg=null;
		
		
		Date ValidFrom = Utilities.parseDate(request.getParameter("dateD"));
		
		//System.out.println(Utilities.getSQLDate(ValidFrom));
		
		Date RealDate=null;
		
		//System.out.println(ValidFrom);
		if(Utilities.getDisplayDateFormat(currdate).equals(Utilities.getDisplayDateFormat(ValidFrom))){ //if current date = entered date
         RealDate=(Utilities.getDateByDays(ValidFrom, -1));
         // System.out.println("if case "+RealDate);  
              
		}
		else {
		RealDate=ValidFrom;
		//System.out.println("else case"+RealDate);
		}
		Datasource ds = new Datasource();
		
		
		try {
			ds.createConnection();
			 Statement s=ds.createStatement();
			  String SQL="select valid_to from inventory_primary_percase_request  WHERE request_id ="+res_Id ;
			  //System.out.println(SQL); 
			    ResultSet rs =s.executeQuery(SQL);
			    Date PreviousDate=null;
			    if(rs.first()){
			    	 PreviousDate=rs.getDate("valid_to");
			    }
			   
			  // System.out.println(PreviousDate);
		   Statement s2 = ds.createStatement();
		   String SQL1="Update inventory_primary_percase_request SET deactivated_by='"+UserID+"',deactivated_reason='"+reason+"',previous_valid_to="+Utilities.getSQLDate(PreviousDate)+",valid_to="+Utilities.getSQLDate(RealDate)+" WHERE request_id ="+res_Id ;    
		  // System.out.println(SQL1);
		   int i=s2.executeUpdate(SQL1);
		   
	        if(i>0)
	        {
	              msg="Data Updated  Successfully";
	        }
	              else
	        {
	             msg="Request ID could not be found";

	        }
	        ds.dropConnection(); 
		  
		 }	catch(Exception e){
			System.out.println(e);
		}
		  
		obj.put("Message",msg); 
		out.print(obj);
		out.close();
		
		
	}
	
}
