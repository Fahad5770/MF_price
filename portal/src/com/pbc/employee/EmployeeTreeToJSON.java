package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pbc.util.Datasource;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.stream.JsonReader;
/**
 * Servlet implementation class ToJSON
 */
@WebServlet(description = "JSON String to Java Objects", urlPatterns = { "/employee/EmployeeTreeToJSON" })
public class EmployeeTreeToJSON extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Connection c;
	static Datasource ds;	
	static Statement s;
       
    /**
     * @throws SQLException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeTreeToJSON() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        super();
        ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String x1,x2,x3;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
	   String xr = request.getParameter("JSONString");
	   Gson gson = new Gson(); 
	   java.lang.reflect.Type type = new TypeToken<List<EmployeeJSONObj>>(){}.getType();
	   List<EmployeeJSONObj>l = gson.fromJson(xr, type);
	   
	   //System.out.println(l.get(0).getName());
	 //deleting old records first
		
	 		try {
	 			s = ds.createStatement();
	 			s.executeUpdate("DELETE FROM employee_tree"); //deleting old records
	 		} catch (SQLException e1) {
	 			// TODO Auto-generated catch block
	 			e1.printStackTrace();
	 		}		       			
	 	    
	   iterateOverEmployee(l,0);
	   response.sendRedirect("/portal/EmployeeTree.jsp");
	   
		//out.println("hello");
	}
	
	private static void iterateOverEmployee(List<EmployeeJSONObj> l,int counter) {
		String ParentID="1";
		String ParentIDDptDesig = "";
		String ParentIDDptDesigArray[];
		
		String Designation = "";
		String Department = "";
		String SapCode = "";
		String SapCodeDeptDesig = "";
		String SapCodeDeptDesigArray[];		
		
		
	    for(EmployeeJSONObj emp: l){

	        if(emp.getChild() != null){

	            for(int i=0;i<emp.getChild().size();i++)
	            {
	            	//System.out.println("Parent Name: "+emp.getName());
	            	//System.out.println("Child Name: "+emp.getChild().get(i).getName());
	            	
	            	ParentIDDptDesig = emp.getName(); //ID#DptID#DesigID
	            	if(ParentIDDptDesig != null)
	            	{ 
	            		ParentIDDptDesigArray = ParentIDDptDesig.split("#"); 
	            		ParentID = ParentIDDptDesigArray[0];
	            	}
	            	else
	            	{
	            		//ParentID = "-1"; // for root element
	            	}
	            	
	            	
	            	
	            	SapCodeDeptDesig = emp.getChild().get(i).getName(); //ID#DptID#DesigID
	            	if(SapCodeDeptDesig != null)
	            	{
	            		SapCodeDeptDesigArray = SapCodeDeptDesig.split("#");
		            	
		            	SapCode = SapCodeDeptDesigArray[0];
		            	Department = SapCodeDeptDesigArray[1];
		            	Designation = SapCodeDeptDesigArray[2];
	            		
	            	}
	            	
	            	
	            	//Inserting child into DB
		            try {	
		       			if(counter == 0) //for root - it should enter only once 
		       			{
		       				s.executeUpdate("INSERT INTO `employee_tree`(`sap_code`,`reporting_to`) VALUES ('"+ParentID+"','-1')");
		       				counter++;
		       				s.executeUpdate("INSERT INTO `employee_tree`(`sap_code`,`reporting_to`) VALUES ('"+SapCode+"','"+ParentID+"')");
		       			}
		       			else
		       			{
		       				s.executeUpdate("INSERT INTO `employee_tree`(`sap_code`,`reporting_to`) VALUES ('"+SapCode+"','"+ParentID+"')");
		       			}
		            	 
		            	
		       			
		       		} catch (Exception e) {
		       			e.printStackTrace();
		       		}
	            	
	            }

	            iterateOverEmployee(emp.getChild(),counter+1);
	        }           
	    }
	}

}
