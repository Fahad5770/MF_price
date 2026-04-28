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



@WebServlet(description = "Dispatch Extra Loading Servlet ", urlPatterns = { "/distributor/DispatchExtraLoadingExecute" })
public class DispatchExtraLoadingExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatchExtraLoadingExecute() {
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
		
		long DistributorID = Utilities.parseLong(request.getParameter("DispatchExtraLoadDistributorIDHidden"));
		
		
		
		
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
			StockPosting sp = new StockPosting();
			
			s.executeUpdate("update inventory_sales_dispatch set is_extra_loaded=1, extra_loaded_on=now(), extra_loaded_by = "+UserID+" where id = "+DispatchID);
			s.executeUpdate("delete from inventory_sales_dispatch_extra_products where dispatch_id="+DispatchID);
			
			if(ProductID !=null) {
				for(int i=0; i<ProductID.length; i++) {
					long TotalUnits=0;
					long LiquInML=0;
					 TotalUnits =(RawCases[i]*UnitPerSKU[i])+Units[i];
					 LiquInML = TotalUnits*LiquidInMl[i];
					
					 long AvailableTotalUnits = sp.getClosingBalanceExInvoiced(DistributorID, ProductID[i], new Date());
					 if(TotalUnits > AvailableTotalUnits){
						
						 
						 String ProductName = getProductName(s, ProductID[i]);
							obj.put("success", "false");
							obj.put("error", "Quantity should not exceed "+Utilities.convertToRawCases(AvailableTotalUnits, Utilities.parseInt(UnitPerSKU[i]+""))+ " for "+ProductName);
							
							
							out.print(obj);
							out.close();
							 ds.rollback();	
							return;
							
						}else{
							
							
							 s.executeUpdate("insert into inventory_sales_dispatch_extra_products (dispatch_id,product_id,raw_cases,units,total_units,liquid_in_ml) values("+DispatchID+","+ProductID[i]+","+RawCases[i]+","+Units[i]+","+TotalUnits+","+LiquInML+")");
						}
					 
					 
					
				}
			}
			//obj.put("success", "true");
			
			ds.commit();
			
			
			boolean isPosted = sp.postDispatch(DispatchID);
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
	
private String getProductName(Statement s, int ProductID) throws SQLException{
		
		String ProductName = "";
		ResultSet rs = s.executeQuery("SELECT concat(package_label, ' ', brand_label) product_name FROM pep.inventory_products_view where product_id="+ProductID);
		if(rs.first()){
			ProductName = rs.getString("product_name");
		}
		
		return ProductName;
		
	}
	
}
