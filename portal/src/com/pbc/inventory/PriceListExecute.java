package com.pbc.inventory;

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
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "Price List ", urlPatterns = { "/inventory/PriceListExecute" })
public class PriceListExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PriceListExecute() {
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
		
		
		//Master table
		String PriceListName = Utilities.filterString(request.getParameter("PriceListLabel"), 1, 20);
		String ValidFrom = Utilities.filterString(request.getParameter("PriceListValidFrom"),1,12);
		String ValidTo = Utilities.filterString(request.getParameter("PriceListValidTo"),1,12);
		int Active = Utilities.parseInt(request.getParameter("PriceListIsActive"));
		
		Date ValidFromDate = Utilities.parseDate(ValidFrom);
		Date ValidToDate = Utilities.parseDate(ValidTo);

		//Detail table
		long[] ProductID = Utilities.parseLong(request.getParameterValues("PriceListProductCode"));
		double[] RawCases = Utilities.parseDouble(request.getParameterValues("PriceListRawCase"));
		double[] Bottles = Utilities.parseDouble(request.getParameterValues("PriceListBottle"));
		
		//double[] Discounts = Utilities.parseDouble(request.getParameterValues("PriceListDisount"));
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();
		long MasterTablePriceListID = 0;
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			
				
				
				if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
				{
				
					s.executeUpdate("insert into inventory_price_list(label,valid_from,valid_to,is_active,created_on,created_by) values('"+PriceListName+"',"+ Utilities.getSQLDate(ValidFromDate) +","+Utilities.getSQLDate(ValidToDate)+","+Active+",now(),"+UserID+")");
					
					//getting pricelist id
					
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						MasterTablePriceListID = rs.getInt(1); 
					}
					
				} 
				else if(Utilities.parseLong(request.getParameter("isEditCase"))==1) //updation case for master table
				{
					long PriceListMasterTableID = Utilities.parseLong(request.getParameter("PriceListMasterTableID"));
					
					s.executeUpdate("update inventory_price_list set label='"+PriceListName+"',valid_from="+Utilities.getSQLDate(ValidFromDate)+",valid_to="+Utilities.getSQLDate(ValidToDate)+",is_active="+Active+" where id="+PriceListMasterTableID);
					
					s.executeUpdate("delete from inventory_price_list_products where id="+PriceListMasterTableID); //deleting previous records from detail table
					MasterTablePriceListID = PriceListMasterTableID;
				}
				for(int i=0;i<RawCases.length;i++)
				{
					if(RawCases[i] !=0 && Bottles[i] !=0)
					{
						s.executeUpdate("insert into inventory_price_list_products(id,product_id,raw_case,unit,discount) values("+MasterTablePriceListID+","+ ProductID[i] +","+RawCases[i]+","+Bottles[i]+", 0)");
					}
				}
				
				obj.put("price_list_id",MasterTablePriceListID);
				obj.put("success", "true");
				ds.commit();
			
			s.close();
			ds.dropConnection();
		} catch (Exception e) {

			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			obj.put("success", "false");
			obj.put("error", e.toString());
			e.printStackTrace();
		} finally {
			
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		out.print(obj);
		out.close();
		
	}
	
}
