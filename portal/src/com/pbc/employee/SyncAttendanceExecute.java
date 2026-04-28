package com.pbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;

import com.pbc.cron.CheckOutSync;

@WebServlet(description = "Sync Attendance", urlPatterns = { "/employee/SyncAttendanceExecute" })
public class SyncAttendanceExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SyncAttendanceExecute() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		PrintWriter out = response.getWriter();
		CheckOutSync cos = new CheckOutSync();

		try {
			HttpSession session = request.getSession();

			long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

			try {
				if(Utilities.isAuthorized(400, SessionUserID) == false){
					response.sendRedirect("AccessDenied.jsp");
				}
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String UserID = Utilities.filterString(request.getParameter("SapCode"), 1, 12);
			String SyncAttendanceValidFrom = Utilities.filterString(request.getParameter("SyncAttendanceValidFrom"), 1, 12);
			Date StartDate = Utilities.parseDatewithdash(SyncAttendanceValidFrom);
			Statement s = ds.createStatement();
			Date EndDate = Utilities.getDateByDays(new Date(), -1);
			Date CurrentDate = StartDate;
			while(true){
				
				//System.out.println(Utilities.getSQLDate(StartDate)+" - "+Utilities.getSQLDate(EndDate)+" - "+Utilities.getSQLDate(CurrentDate));
				
				//PopulateData(CurrentDate,EmployeeIDs);
				cos.PopulateData(CurrentDate, UserID, 1);
				if(DateUtils.isSameDay(CurrentDate, EndDate)){
					break;
				}

				CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
			}
			
			obj.put("success", "true");
			System.out.println("UserID: " + UserID);
			System.out.println("SyncAttendanceValidFrom: " + StartDate);

		} catch (SQLException e) {
			obj.put("success", "false");
			obj.put("error", "Exception");
			e.printStackTrace();

		}

		out.print(obj);
		out.close();
	}

}
