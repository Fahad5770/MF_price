package com.pbc.workflow;

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


@WebServlet(description = "Executes chat requests", urlPatterns = { "/workflow/WorkflowChatExecute" })

public class WorkflowChatExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WorkflowChatExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		
		long RequestID = Utilities.parseLong(Utilities.filterString(request.getParameter("WorkflowChatRequestID"), 0, MaxLength.CURRENCY));
		
		String WorkflowChatSendNewMessage = Utilities.filterString(request.getParameter("WorkflowChatSendNewMessage"), 1, MaxLength.WORKFLOW_REMARKS);
		long WorkflowChatSendNewTo = Utilities.parseLong(Utilities.filterString(request.getParameter("WorkflowChatSendNewTo"), 0, MaxLength.EMPLOYEE_ID));
		
		long ReplyConversationID = Utilities.parseLong(Utilities.filterString(request.getParameter("ConversationID"), 0, MaxLength.CURRENCY));
		String ReplyMessage = Utilities.filterString(request.getParameter("ReplyMessage"), 1, MaxLength.WORKFLOW_REMARKS);
		
		
		try {
			WorkflowChat chat = new WorkflowChat(RequestID);
			
			if (ReplyConversationID == 0){
				chat.createConversation(Long.parseLong(UserID), WorkflowChatSendNewTo, WorkflowChatSendNewMessage);
			}else{
				chat.doReply(ReplyConversationID, Long.parseLong(UserID), ReplyMessage);
			}
			chat.close();			
			
			response.setContentType("application/json");
			
			obj.put("success", "true");
			obj.put("RequestID", ""+RequestID);
			
		} catch (Exception e) {
			obj.put("success", "false");
			obj.put("error", e.toString());
		}
		
		out.print(obj);
		out.close();
		
	}
	
}