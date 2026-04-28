package com.pbc.distributor;

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

import com.pbc.inventory.StockPosting;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Dispatch Returns ", urlPatterns = { "/distributor/DispatchEmptyReturnsExecute" })
public class DispatchEmptyReturnsExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatchEmptyReturnsExecute() {
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
		
		long DispatchID =Utilities.parseLong(request.getParameter("DispatchIDForInsertion"));
		int[] ProductID = Utilities.parseInt(request.getParameterValues("ProductID"));
		int[] RawCases=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormRawCases"));
		int[] Units=Utilities.parseInt(request.getParameterValues("DispatchReturnsMainFormUnits"));
		long[] LiquidInMl=Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormLiquidInML"));
		long[] UnitPerSKU = Utilities.parseLong(request.getParameterValues("DispatchReturnsMainFormUnitPerSKU"));
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			

			s.executeUpdate("update inventory_sales_dispatch set is_empty_returned=1,empty_returned_on=now(),empty_returned_by="+UserID+" where id="+DispatchID);
			s.executeUpdate("Delete from inventory_sales_dispatch_returned_products where is_empty=1 and dispatch_id="+DispatchID);
				
				//inserting in sub table
			if(ProductID !=null) {
				for(int i=0;i<ProductID.length;i++) {
					long TotalUnits =(RawCases[i]*UnitPerSKU[i])+Units[i];
					long LiquInML = TotalUnits*LiquidInMl[i];
					
					s.executeUpdate("insert into inventory_sales_dispatch_returned_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml,is_empty) values("+DispatchID+","+ProductID[i]+","+RawCases[i]+","+Units[i]+","+TotalUnits+","+LiquInML+",1)");
				}
				obj.put("success", "true");	
			}
			
			ds.commit();
			StockPosting sp = new StockPosting();
			boolean isPosted = sp.postDispatchEmptyReturn(DispatchID);
			sp.close();
			
			if (isPosted == true){
				obj.put("success", "true");
			}else{
				obj.put("success", "false");
				obj.put("error", "Could not post stock in store.");				
			}
				
			s.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
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
