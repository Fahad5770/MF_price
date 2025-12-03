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


@WebServlet(description = "Executes sampling dispatch", urlPatterns = { "/sampling/SamplingDispatchExecute" })

public class SamplingDispatchExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SamplingDispatchExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}
		
		JSONObject obj=new JSONObject();
		
		long ApprovalID[] = Utilities.parseLong(request.getParameterValues("ApprovalID"));
		int DispatchMethodMainForm = Utilities.parseInt(request.getParameter("DispatchMethodMainForm"));
		long DispatchMethodValue[] = Utilities.parseLong(request.getParameterValues("DispatchMethodValue"));
		
		Datasource ds = new Datasource();
		
		try {
			
			ds.createConnection();
			
			Statement s = ds.createStatement();
			
			for (int i = 0; i < ApprovalID.length; i++){
				s.executeUpdate("update sampling_monthly_approval set is_dispatched=1 where approval_id="+ApprovalID[i]);
				
				String DispatchCode = "";
				long EmployeeSAPCode = 0;
				
				if(DispatchMethodMainForm == 2){
					DispatchCode = DispatchMethodValue[i]+"";
				}else{
					EmployeeSAPCode = DispatchMethodValue[i];
				}
				
				s.executeUpdate("insert into sampling_monthly_approval_dispatch (approval_id, dispatched_on, method_id, dispatch_code, byhand_userid, created_by) values ("+ApprovalID[i]+", now(), "+DispatchMethodMainForm+", '"+DispatchCode+"', '"+EmployeeSAPCode+"', "+UserID+")");
			}
			
			
			obj.put("success", "true");
			//obj.put("ReceivingID", ""+ReceivingID);
			
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();
		
	}
	
}