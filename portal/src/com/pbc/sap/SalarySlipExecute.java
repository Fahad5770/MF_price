package com.pbc.sap;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pbc.util.Utilities;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

/**
 * Servlet implementation class SalarySlipExecute
 */
@WebServlet("/sap/SalarySlipExecute")
public class SalarySlipExecute extends HttpServlet {
	private static final long serialVersionUID = 441L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalarySlipExecute() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(Utilities.getSessionExpiredPageURL(request));
		}

		int sequence = Utilities.parseInt(request.getParameter("sequence"));
		
		try{
			
		
        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_PRD");
        JCoFunction function = destination.getRepository().getFunction("BAPI_GET_PAYSLIP_HTML");
        function.getImportParameterList().setValue("EMPLOYEENUMBER", Integer.parseInt(UserID));
        function.getImportParameterList().setValue("SEQUENCENUMBER", sequence);
        if(function == null)
            throw new RuntimeException("ZBAPI_OUTLET_TRANSACTION_DATA not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        JCoTable codes = function.getTableParameterList().getTable(0);
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        //System.out.println(rows + " " + columns);
        
        PrintWriter out = response.getWriter();
        
        codes.firstRow();
        for (int i = 0; i < rows; i++) 
        {
        	out.print(codes.getString(0));        	
            codes.setRow(i+1);
        }
		
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
