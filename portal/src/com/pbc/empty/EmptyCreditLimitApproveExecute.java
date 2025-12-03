package com.pbc.empty;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.employee.EmployeeHierarchy;
import com.pbc.outlet.OutletDashboard;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import com.pbc.workflow.WorkflowEmail;


@WebServlet(description = "Empty Credit Limit Request Approve", urlPatterns = { "/cash/EmptyCreditLimitApproveExecute" })

public class EmptyCreditLimitApproveExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmptyCreditLimitApproveExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Yes... m hit");
		
		Datasource ds = new Datasource();
		
		
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		try {
			
			
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			
			
			
			
			
			
			///System.out.println(RequestIDVal+"-"+StepID+"-"+isLastStep+"-"+NextUserID+"-"+NextActionID+"-"+WorkflowStepRemarks);
			
			long RequestID = Utilities.parseLong(request.getParameter("RequestIDD"));
			
			long CustomerID=Utilities.parseLong(request.getParameter("CustomerIDD"));
			
			
					//System.out.println("Yes I am going");
					
					//s.executeUpdate("update gl_customer_credit_limit set is_active=0 where customer_id="+CustomerID);
					
					//s.executeUpdate("insert into gl_customer_credit_limit(customer_id,credit_limit,valid_from,valid_to,uvid,is_active,activated_on,activated_by,deactivated_on,deactivated_by,one_time_credit_account_id,request_id,comments,cheque_no,cheque_bank,cheque_branch,processed_by,processed_on) select customer_id,credit_limit,valid_from,valid_to,uvid,is_active,activated_on,activated_by,deactivated_on,deactivated_by,one_time_credit_account_id,request_id,comments,cheque_no,cheque_bank,cheque_branch,"+UserID+",now() from gl_customer_credit_limit_request where request_id="+RequestID);
					//
					//s.executeUpdate("update gl_customer_credit_limit_request set is_processed=1 where request_id="+RequestID);
					
					
					
					
					long MainTableID=0;
					
					ResultSet rs = s.executeQuery("select id from ec_empty_credit_limit_request where request_id="+RequestID);
					if(rs.first()){
						MainTableID = rs.getLong("id");
					}
					
					
					
					//Resetting old active requests to 0
					
					s.executeUpdate("update ec_empty_credit_limit set is_active=0 where distributor_id="+CustomerID);
					
					
					///////////////////////////////////////////////
					//////////////////////////////////////////////
					
					
					int MasterTableID=0;
					
					s.executeUpdate("INSERT INTO ec_empty_credit_limit (created_on,created_by,uvid,distributor_id,credit_type,reason,start_date,end_date,is_active,activated_on,deactivated_on,request_id) select created_on,"+UserID+",uvid,distributor_id,credit_type,reason,start_date,end_date,is_active,activated_on,deactivated_on,request_id from ec_empty_credit_limit_request where request_id="+RequestID);
					
					ResultSet rs11 = s.executeQuery("select LAST_INSERT_ID()");
					if(rs11.first()){
						MasterTableID = rs11.getInt(1); 
					}
					
					
					
					//System.out.println(MainTableID);
					
					ResultSet rs12 = s.executeQuery("select * from ec_empty_credit_limit_request_products where id="+MainTableID);
					while(rs12.next()){
						s2.executeUpdate("INSERT INTO ec_empty_credit_limit_products (id,package_id,raw_cases,units,total_units,liquid_in_ml,type_id) values("+MasterTableID+","+rs12.getInt("package_id")+","+rs12.getInt("raw_cases")+","+rs12.getInt("units")+","+rs12.getInt("total_units")+","+rs12.getInt("liquid_in_ml")+","+rs12.getInt("type_id")+")");
					}
					
					
					s.executeUpdate("update ec_empty_credit_limit_request set is_processed=1 where request_id="+RequestID);
					
					
					obj.put("success", "true");
				
				ds.commit();
				
				
			
			
		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		out.print(obj);
		out.close();
		
		
		
	}
	
}