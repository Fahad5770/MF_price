package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;

/**
 * Servlet implementation class QuickAddEmployeeTree
 */
@WebServlet("/employee/QuickAddEmployeeTree")
public class QuickAddEmployeeTree extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection c;
	Datasource ds;	
	Statement s;
    /**
     * @throws SQLException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public QuickAddEmployeeTree() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResultSet rs = null;
		String SAPCode = request.getParameter("quickAddSapCode");
		JSONObject obj = new JSONObject();
		PrintWriter out = response.getWriter();
		try {
			 s = ds.createStatement();
			 //System.out.println("SELECT DISTINCT Emp.sap_code,CONCAT(Emp.first_name,' ',Emp.last_name) AS Name,orgtx,plstx,Dpt.orgeh,Desig.plans FROM pep.employee_view Emp,pep.sap_t527x_view Dpt,pep.sap_t528t_view Desig WHERE Emp.department_code = Dpt.orgeh AND Emp.designation_code = Desig.plans AND Emp.status2 = '3' AND Emp.sap_code LIKE '"+SAPCode+"'");
			//rs = s.executeQuery("SELECT DISTINCT Emp.sap_code,CONCAT(Emp.first_name,' ',Emp.last_name) AS Name,orgtx,plstx,Dpt.orgeh,Desig.plans FROM pep.employee_view Emp,pep.sap_t527x_view Dpt,pep.sap_t528t_view Desig WHERE Emp.department_code = Dpt.orgeh AND Emp.designation_code = Desig.plans AND Emp.status2 = '3' AND Emp.sap_code LIKE '"+SAPCode+"'");
			 rs = s.executeQuery("SELECT sap_code,CONCAT(first_name,' ',last_name) as Name,department_label as orgtx,designation_label as plstx,department_code as orgeh,designation_code as plans FROM pep.employee_view Emp WHERE Emp.sap_code LIKE '"+SAPCode+"'");
			 
			 if(rs.first()){
				obj.put("SapCode", rs.getString("sap_code"));
				obj.put("Name", rs.getString("Name"));
				obj.put("Department", rs.getString("orgtx"));
				obj.put("Designation", rs.getString("plstx"));
				obj.put("DeptID", rs.getString("orgeh"));
				obj.put("DesigID", rs.getString("plans"));
			}
			else
			{
				obj.put("SapCode", "-1"); //there is no record in db
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.print(obj);
		out.close();
	}

}
