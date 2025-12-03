package com.pbc.cash;

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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "GL Credit Limit Execute", urlPatterns = { "/cash/GLCreditLimitRequestExecute" })
public class GLCreditLimitRequestExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GLCreditLimitRequestExecute() {
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
		
		long UniqueVoucherID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		long CustomerID = Utilities.parseLong(request.getParameter("CustomerID"));
		double CreditLimit = Utilities.parseDouble(request.getParameter("CreditLimit"));
		Date ValidFrom = Utilities.parseDate(request.getParameter("ValidFromDate"));
		Date ValidTo = Utilities.parseDate(request.getParameter("ValidToDate"));
		String CreditLimitComments = Utilities.filterString(request.getParameter("CreditLimitComments"), 2, 500);
		
		String CreditLimitCheckNumber = Utilities.filterString(request.getParameter("CheckNumber"), 2, 500);
		String CreditLimitBankName = Utilities.filterString(request.getParameter("Bank"), 2, 500);
		String CreditLimitBranchName = Utilities.filterString(request.getParameter("Branch"), 2, 500);
		
		
		JSONObject obj = new JSONObject();
		
		// do validation
		boolean isValidationClear = true;
		if( CustomerID == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please select Customer");
			isValidationClear = false;
		}else if( CreditLimit == 0 ){
			obj.put("success", "false");
			obj.put("error", "Please enter Credit Limit");
			isValidationClear = false;
		}else if( ValidFrom == null ){
			obj.put("success", "false");
			obj.put("error", "Please enter Valid From");
			isValidationClear = false;
		}else if( ValidTo == null ){
			obj.put("success", "false");
			obj.put("error", "Please enter Valid To");
			isValidationClear = false;
		}// end validation
		
		if( isValidationClear ){
		
			Datasource ds = new Datasource();
			
			
			try {
				
				ds.createConnection();
				ds.startTransaction();
				
				Statement s = ds.createStatement();
				Statement s2 = ds.createStatement();
				Statement s3 = ds.createStatement();
				
				long MasterTableCreditLimitID=0;
				
				ResultSet rs2 = s2.executeQuery("select id from gl_customer_credit_limit where uvid="+UniqueVoucherID);
				if( rs2.first() ){
					obj.put("success", "false");
					obj.put("error", "Already Exists");
				}else{
					
					s.executeUpdate("INSERT INTO `gl_customer_credit_limit_request`(`customer_id`,`credit_limit`,`valid_from`,`valid_to`,`uvid`,`is_active`,`activated_on`,`activated_by`,comments,cheque_no,cheque_bank,cheque_branch)VALUES("+CustomerID+","+CreditLimit+","+Utilities.getSQLDate(ValidFrom)+","+Utilities.getSQLDate(ValidTo)+","+UniqueVoucherID+",1,now(),"+UserID+",'"+CreditLimitComments+"','"+CreditLimitCheckNumber+"','"+CreditLimitBankName+"','"+CreditLimitBranchName+"')"); 
					
					obj.put("success", "true");
					
				}
				
				ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
				if(rs.first()){
					MasterTableCreditLimitID = rs.getInt(1); 
				}
				
				//step id=2 processid=5
				
				long ProcessUserID = 0;
				
				////ResultSet rs1 = s.executeQuery("select * from workflow_processes_steps where step_id=2 and process_id=5");
				ResultSet rs1 = s.executeQuery("SELECT snd_id FROM pep.common_distributors where distributor_id="+CustomerID);
				if(rs1.first()){
					ProcessUserID = rs1.getLong("snd_id");
				}
				if (ProcessUserID == 0 || ProcessUserID == 1){
					ProcessUserID = 2911;
				}
				
				
				//if(ProcessUserID==3845){ // sd2 is on hajj leaves
					//ProcessUserID = 3628;
				//}
				
				
				
				Workflow wf = new Workflow();
				long WorkFlowRequestID = wf.createRequest(5, Integer.parseInt(UserID), ProcessUserID, 4, "Credit Limit Request Raised");
				s.executeUpdate("update gl_customer_credit_limit_request set request_id="+WorkFlowRequestID+" where id="+MasterTableCreditLimitID);
				
				
				
				
				ds.commit();
				
				s3.close();
				s2.close();
				s.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				try {
					
					ds.rollback();
					
					obj.put("success", "false");
					obj.put("error", e.toString());
					e.printStackTrace();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}finally{
				try {
					ds.dropConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		} // end if validation
		
		out.print(obj);
		out.close();
		
	}
	
}
