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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.inventory.SalesPosting;
import com.pbc.inventory.StockPosting;

@WebServlet(description = "Dispatch Execute ", urlPatterns = { "/distributor/DispatchSalesExecute" })
public class DispatchSalesExecute extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatchSalesExecute() {
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
		
		//String DistributorVehicleNumber = Utilities.filterString(request.getParameter("VehicleNum"), 1, 100);
		int DispatchType = Utilities.parseInt(request.getParameter("DispatchVehicleType"));
		String VehicleIDLabel = Utilities.filterString(request.getParameter("DispatchVehicleSelect"),1,200);
		long DriverID = Utilities.parseLong(request.getParameter("DistributorDriverName"));
		long DistributorID = Utilities.parseLong(request.getParameter("DistributorIDD"));
		long MasterTableID =0;
		long UVID = Utilities.parseLong(request.getParameter("UniqueVoucherID"));
		String VehicleIDLabelArray[] = VehicleIDLabel.split(","); //vehicle id contain id,label --> id at 0 index
		long VehicleID = Utilities.parseLong(VehicleIDLabelArray[0]);
		String DriverIDString=null;
		String VehicleIDString=null;
		String SpotSellingCheckBx = Utilities.filterString(request.getParameter("SpotSellingCheckBxHidden"), 1, 10);
		if (SpotSellingCheckBx == null){
			SpotSellingCheckBx = "0";
		}
		
		if(DriverID != 0)
		{
			DriverIDString = DriverID+""; 
		}
		if(VehicleID != 0)
		{
			VehicleIDString=VehicleID+"";
		}
		
		
		Datasource ds = new Datasource();
		JSONObject obj = new JSONObject();		
		try {
			
			ds.createConnection();
			ds.startTransaction();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			
				
			ResultSet rs1 = s.executeQuery("select id from inventory_sales_dispatch where uvid="+UVID);
			if( rs1.first() ){
				
				obj.put("success", "false");
				obj.put("error", "Already Exists");
				
			}else{
				
				if(Utilities.parseLong(request.getParameter("isEditCase"))==0)//insertion case master table
				{
					String QQuerrry ="";
					if(SpotSellingCheckBx.equals("0")){ //if normal case
						if(DispatchType==2){//mean by hand then is_adjusted=1 and created_on=date
							 QQuerrry = "insert into inventory_sales_dispatch (is_adjusted,adjusted_on,created_on,created_by,dispatch_type,distributor_id,uvid) values(1,now(),now(),"+UserID+","+DispatchType+","+DistributorID+","+UVID+")";
						}else{ //in case of vehicle
							QQuerrry = "insert into inventory_sales_dispatch (created_on,created_by,dispatch_type,vehicle_id,driver_id,distributor_id,uvid) values(now(),"+UserID+","+DispatchType+","+VehicleIDString+","+DriverIDString+","+DistributorID+","+UVID+")";
						}
					}else{ //means spot selling so insert the spot selling flag
						QQuerrry = "insert into inventory_sales_dispatch (created_on,created_by,dispatch_type,vehicle_id,driver_id,distributor_id,uvid,is_spot_selling) values(now(),"+UserID+","+DispatchType+","+VehicleIDString+","+DriverIDString+","+DistributorID+","+UVID+",1)";
					}
					
					s.executeUpdate(QQuerrry);					
					ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
					if(rs.first()){
						MasterTableID = rs.getInt(1); 
					}
					if(SpotSellingCheckBx.equals("0")){//checking for spot selling - if it is spot selling then no need to insert in sub table
						//System.out.println("hello i am not spot selling");
						//inserting in sub table
						long[] InventorySalesID = Utilities.parseLong(request.getParameterValues("InventorySalesID"));
						int[] DispatchSalesSelect = Utilities.parseInt(request.getParameterValues("DispatchSalesSelect"));;
						if(DispatchSalesSelect != null)
						{
							for(int x=0;x<DispatchSalesSelect.length;x++)
							{
								if(DispatchSalesSelect[x] == 1) //if selected
								{
									s.executeUpdate("insert into inventory_sales_dispatch_invoices(id,sales_id) values("+MasterTableID+","+InventorySalesID[x]+")");
									
									s.executeUpdate("update inventory_sales_invoices set is_dispatched=1 where id="+InventorySalesID[x]);
									
									boolean posted = SalesPosting.post(InventorySalesID[x], Long.parseLong(UserID));
									
									if (posted == false){
										throw new SQLException("Could not post sales into adjustment");
									}
									
								}
								
							}
						}				
						
						ds.commit();
						
						StockPosting so = new StockPosting();
						boolean isPosted = so.postDispatch(MasterTableID);
						so.close();	
					
					
					}else{
						ds.commit();
					}
					
					
								
				}
				else if(Utilities.parseLong(request.getParameter("isEditCase"))==1) //updation case for master table
				{
					//deletingg from master table										
					
					//first update the inventory sales table then delete
					
					/*
					ResultSet rs2 = s.executeQuery("select * from inventory_sales_dispatch_invoices where id="+Utilities.parseLong(request.getParameter("EditIDForExecute")));
					while(rs2.next())
					{
						s1.executeUpdate("update inventory_sales_invoices set is_dispatched=0 where id="+rs2.getLong("sales_id"));
					}
					//deletting from sub table
					s.executeUpdate("Delete from inventory_sales_dispatch where id="+Utilities.parseLong(request.getParameter("EditIDForExecute")));
					
					s1.executeUpdate("Delete from inventory_sales_dispatch_invoices where id="+Utilities.parseLong(request.getParameter("EditIDForExecute")));
					
					ds.commit();
					
					StockPosting so = new StockPosting();
					boolean isUnposted = so.unPostStock(13, Utilities.parseLong(request.getParameter("EditIDForExecute")));
					so.close();	
					*/
					
					
				}
				
				obj.put("success", "true");

			}
			s.close();

		} catch (Exception e) {
			try {
				ds.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// TODO Auto-generated catch block
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
