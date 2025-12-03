package com.pbc.reports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.common.Distributor;
import com.pbc.util.Datasource;
import com.pbc.util.UserAccess;
import com.pbc.util.Utilities;
import com.pbc.util.DateUtils;

/**
 * Servlet implementation class ReportsR124DownloadExcel
 */
@WebServlet("/reports/ReportR345Main")
public class ReportR345Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportR345Main() {
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
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		//System.out.println("hellooo ");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		
		
		HttpSession session = request.getSession();
		
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		
		String startDate = Utilities.filterString(request.getParameter("StartDate"), 1, 100);
		//System.out.println(startDate);
		Date StartDate= DateUtils.parseDateYYYYMMDDWithBackSlash(startDate);
	//	System.out.println(StartDate);
		
		String endDate = Utilities.filterString(request.getParameter("EndDate"), 1, 100);
	//	System.out.println(startDate);
		Date EndDate= DateUtils.parseDateYYYYMMDDWithBackSlash(endDate);
	//	System.out.println(EndDate);
		
		
		
		Date DateToday = new Date();
		String FileName = "ReportsR45_"+Utilities.getSQLDateWithoutSeprator(DateToday)+".xlsx";

	//	Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
	//	Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");
		
		
		
		
		
		try {
			new ReportR345Excel().createExcel(Utilities.getCommonFilePath()+"/"+FileName, StartDate, EndDate);
			json.put("success", "true");
			json.put("FileName", FileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			json.put("success", "false");
			json.put("error", e.toString());
		}
		
		out.print(json);
		
		
	}

}