package com.pbc.employee;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;



public class EmployeeTree {
	
	Connection c;
	Datasource ds;	
	Statement s;
	
	public EmployeeTree() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public ResultSet getAllEmployees() throws SQLException{
			
		//System.out.println("test 1");
		ResultSet rs = null;
		try {
			 s = ds.createStatement();
			 
			//rs = s.executeQuery("SELECT DISTINCT sap_code as pernr,CONCAT(first_name,' ',last_name) as ename,orgtx,plstx,Dpt.orgeh,Desig.plans FROM pep.employee_view Emp,pep.sap_t527x_view Dpt,pep.sap_t528t_view Desig WHERE Emp.department_code = Dpt.orgeh AND Emp.designation_code = Desig.plans ORDER BY first_name LIMIT 100");
			rs = s.executeQuery("SELECT sap_code as pernr,CONCAT(first_name,' ',last_name) as ename,department_label as orgtx,designation_label as plstx,department_code as orgeh,designation_code as plans FROM pep.employee_view Emp ORDER BY first_name LIMIT 100");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
			
		}
	
	public ResultSet loadAllDepartments()
	{
		ResultSet rs = null;
		
		try {
			 s = ds.createStatement();			 
			 rs = s.executeQuery("SELECT * FROM pep.sap_t527x_view");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet loadAllDesignations()
	{
		ResultSet rs = null;
		try {
			 s = ds.createStatement();			 
			 rs = s.executeQuery("SELECT st.plans, st.plstx, (select count(p1.mandt) from pep.sap_pa0001 p1,pep.sap_pa0000 p0 where p1.plans = st.plans and p1.pernr = p0.pernr) ct FROM pep.sap_t528t st having ct > 0 ORDER BY st.plstx");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
		
	}
	
	public ResultSet getEmployeesByDepartment(int DepartmentID) throws SQLException{		
		
		//System.out.println("search by department");
		ResultSet rs = null;
		try {	
			 s = ds.createStatement();
			 
			 rs = s.executeQuery("SELECT * FROM pep.employee_view WHERE department_code='"+DepartmentID+"'");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
			
		}
	
	public ResultSet getEmployeesByDepartmentAndDesignation(int DepartmentID,int DesignationID) throws SQLException{		
		
		//System.out.println("search by dept and designation");
		ResultSet rs = null;
		try {	
			 s = ds.createStatement();
			 
			 rs = s.executeQuery("SELECT * FROM pep.employee_view WHERE department_code="+DepartmentID+" AND designation_code="+DesignationID+" ORDER BY sname");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
			
		}
	
	public ResultSet getEmployeesByDesignation(int DesignationID) throws SQLException{		
		
		//System.out.println("search by designation "+DesignationID);
		ResultSet rs = null;
		try {	
			 s = ds.createStatement();
			 
			 rs = s.executeQuery("SELECT Emp.pernr,ename,orgtx,plstx FROM pep.sap_pa0001 Emp,pep.sap_pa0000 p0,pep.sap_t527x_view Dpt,pep.sap_t528t Desig WHERE Emp.orgeh = Dpt.orgeh AND Emp.plans = Desig.plans AND Emp.plans='"+DesignationID+"' AND p0.stat2 ='3' AND Emp.pernr=p0.pernr  ORDER BY sname");
			
		 } catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
			
		}
	
	public ResultSet searchEmployeesBySAPCode(String SAPCode)
	{
		ResultSet rs = null;
		try {	
			 s = ds.createStatement();
			 
			 //rs = s.executeQuery("SELECT DISTINCT Emp.pernr,ename,orgtx,plstx,Dpt.orgeh,Desig.plans FROM pep.sap_pa0001 Emp, pep.sap_pa0000 p0,pep.sap_t527x_view Dpt,pep.sap_t528t Desig WHERE Emp.orgeh = Dpt.orgeh AND Emp.plans = Desig.plans AND Emp.pernr = p0.pernr AND p0.stat2 = '3' AND p0.pernr LIKE '%"+SAPCode+"%' ORDER BY sname");
			 rs = s.executeQuery("SELECT sap_code as pernr,CONCAT(first_name,' ',last_name) as ename,department_label as orgtx,designation_label as plstx,department_code as orgeh,designation_code as plans FROM pep.employee_view Emp WHERE Emp.sap_code LIKE '"+SAPCode+"'");
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet getSavedTree() throws SQLException{
		ResultSet rs = null;
		try {	
			 s = ds.createStatement();
			 
			 /*rs = s.executeQuery("SELECT  SUPERVISOR.sap_code AS SuperVisor, "+
					 			    "GROUP_CONCAT(SUPERVISEE.sap_code  ORDER BY SUPERVISEE.sap_code DESC) AS SuperVisee,"+ 
								    "COUNT(*) as child "+  
								"FROM pep.employee_tree AS SUPERVISOR "+ 
								      "INNER JOIN pep.employee_tree SUPERVISEE ON  SUPERVISOR.sap_code = SUPERVISEE.reporting_to "+
								"GROUP BY SuperVisor ORDER BY  child DESC;");*/
			 rs = s.executeQuery("SELECT * FROM pep.employee_tree t,pep.employee_view Emp,pep.sap_t527x_view Dpt,pep.sap_t528t_view Desig WHERE Emp.sap_code = t.sap_code AND Emp.department_code = Dpt.orgeh AND Desig.plans = Emp.designation_code;");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
			
		}	
	
	
	public ResultSet getPertiSavedTree(int sapCode) throws SQLException{
		ResultSet rs = null;
		try {	
			 s = ds.createStatement();			 
			
			 rs = s.executeQuery("SELECT * FROM pep.employee_tree WHERE reporting_to = "+sapCode+" order by reporting_to;");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
			
		}	
	
	
	public String Operator()
	{
		String x="";
		String name = "";
		try {
			ArrayList<EmployeeJSONObj> arraylist = new ArrayList<EmployeeJSONObj>();		      
		      
			ResultSet SavedEmployeesTree = getSavedTree();
		    while(SavedEmployeesTree.next())
		    {
		    	EmployeeJSONObj emp = new EmployeeJSONObj();
		    	name = SavedEmployeesTree.getString("sap_code")+"#"+SavedEmployeesTree.getString("first_name")+SavedEmployeesTree.getString("last_name");
		    	emp.setName(name);
		    	emp.setDepartment(SavedEmployeesTree.getString("orgeh")+"#"+SavedEmployeesTree.getString("orgtx"));  //DepartmentID#DepartmentName
		    	emp.setDesignation(SavedEmployeesTree.getString("plans")+"#"+SavedEmployeesTree.getString("plstx")); //DesignationID#DesignationName
		    	emp.setParentId(SavedEmployeesTree.getString("reporting_to"));
		    	arraylist.add(emp);
		    }
		    x=recursiveTree(arraylist,"-1",0);
		}catch (Exception e) {
			e.printStackTrace();
		}
	    return x;
		
	}
	
	public String recursiveTree(ArrayList<EmployeeJSONObj> categories , String parent,int counter)
	{
		String ret ="";
		if(counter == 0) //root node
		{
			ret = "<ul id='org' style='display:none;'>";
		}
		else if(counter == 1) //2nd child
		{
			 ret = "<ul id='main_child_ul' class='children'>";
		}
		else //normal case
		{
		   ret = "<ul>";
		}
		
		String sub ="";
		String SapCodeName="";
		String SapCodeNameArray[];
		String Name="";
		String SapCode="";
		
		
		try {
			//String xx=categories.getString("sap_code");
			for(int i=0;i<categories.size();i++)
		    {
				SapCodeName = categories.get(i).getName();
				SapCodeNameArray = SapCodeName.split("#");
				SapCode = SapCodeNameArray[0];
				Name = SapCodeNameArray[1];
				
				//gettind departmentName and splitting them DepartmentID#DepartmentName
				String DepartmentIDName = categories.get(i).getDepartment();
				String DepartmentIDNameArray [] = DepartmentIDName.split("#");
				String DepartmentName = DepartmentIDNameArray[1];
				String DepartmentID = DepartmentIDNameArray[0];
				
				String DesignationIDName = categories.get(i).getDesignation();
				String DesignationIDNameArray [] = DesignationIDName.split("#");
				String DesignationName = DesignationIDNameArray[1];
				String DesignationID = DesignationIDNameArray[0];
				
				
				
				if(categories.get(i).getParentId().equals(parent))
		        {
		        	
		        	if(counter == 0)
		        	{
		        		ret += "<li><a href=='#' id='"+SapCode+"#"+DepartmentID+"#"+DesignationID+"' class='ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-e ui-btn-up-e'><span class='ui-btn-inner'><span class='ui-btn-text' style='font-size:10px;'>"+SapCode+"<br/>"+Name+"<br/>"+DepartmentName+"<br/>"+DesignationName+"</span></span></a>";
		        	}
		        	else
		        	{
		        		ret += "<li><a href=='#' id='"+SapCode+"#"+DepartmentID+"#"+DesignationID+"' class='ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b'><span class='ui-btn-inner'><span class='ui-btn-text' style='font-size:10px;'>"+SapCode+"<br/>"+Name+"<br/>"+DepartmentName+"<br/>"+DesignationName+"</span></span></a>";
		        	}
		        	
		            sub = recursiveTree(categories,SapCode,counter+1);
		            if(sub != "<ul></ul>")
		                ret += sub;
		            ret += "</li>";
		        }
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
		return ret+"</ul>";
	}
	
	public void close() throws SQLException
	{
		s.close();
		ds.dropConnection();
	}

}
