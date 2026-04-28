package com.pbc.sampling;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

/**
 * Servlet implementation class SlipCancellationExecute
 */
@WebServlet(description = "Slip Cancellation", urlPatterns = { "/sampling/SlipCancellationExecute" })
public class SlipCancellationExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SlipCancellationExecute() {
        super();
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
		Datasource ds = new Datasource();
		HttpSession session = request.getSession();
		JSONObject obj = new JSONObject();
		PrintWriter out = response.getWriter();
		try {
			ds.createConnection();
			Statement s = ds.createStatement();	
			
			String [] SelectedApprovalID = request.getParameterValues("ApprovalIDNew");
			//System.out.println("Hello "+SelectedFeatures[1]);
			if(SelectedApprovalID != null)
			{
				for(int i =0;i<SelectedApprovalID.length;i++)
				{
					String query = "update `sampling_monthly_approval`" +
							" set status_id=3,is_cancelled=1,cancelled_on=now(),cancelled_by="+Utilities.parseInt(session.getAttribute("UserID").toString())+" where approval_id = "+SelectedApprovalID[i];
					s.executeUpdate(query); 
					//System.out.println("approval id "+SelectedApprovalID[i]);
					
					//deleteting from sampling_monthly_approval_dispatch table 
					
					query = "delete from `sampling_monthly_approval_dispatch` where approval_id = "+SelectedApprovalID[i];
					s.executeUpdate(query);
					
				}
			}
			obj.put("success", "true");
		s.close();			
		ds.dropConnection();
	} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
		// TODO Auto-generated catch block
		obj.put("success", "false");
		obj.put("error", e.toString());
		e.printStackTrace();
	}
		out.print(obj);
		out.close();	
		
	}

}
