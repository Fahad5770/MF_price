package com.pbc.sap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;



@WebServlet(description = "BI Sales Sampling", urlPatterns = { "/sap/SyncSalesInternal" })
public class SyncSalesInternal extends HttpServlet {
	
	
       
    public SyncSalesInternal() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		Date StartDDate = Utilities.parseDate(Utilities.filterString(request.getParameter("StartDate1"),1,100));
		Date EndDDate = Utilities.parseDate(Utilities.filterString(request.getParameter("EndDate1"),1,100));
		
		SyncSalesInternal ssi = new SyncSalesInternal();
		String output = ssi.process(StartDDate, EndDDate);
		
		out.print(output);
		out.close();
		
	}
	
	public static void main(String args[]){
		
		Date StartDDate = Utilities.getDateByDays(-5);
		Date EndDDate = new Date(); 
		
		System.out.println("SyncSalesInternal "+ StartDDate + " " + EndDDate);
		
		SyncSalesInternal ssi = new SyncSalesInternal();
		String output = ssi.process(StartDDate, EndDDate);
		
	}
	
	public String process(Date StartDate, Date EndDate){
		
		Datasource ds = new Datasource();
		
		JSONObject obj=new JSONObject();
		
		String SQLStDate = Utilities.getSQLDate(StartDate);
		String SQLEndDate = Utilities.getSQLDate(EndDate);
		String SQLEndDateNext = Utilities.getSQLDateNext(EndDate);
		
		
		try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			ds.startTransaction();
			
			s1.executeUpdate("delete from sap_sales where created_on_erdat between "+SQLStDate+" and "+SQLEndDate+" and chkdis = 'portal'");//deleting old records
		
				
			s1.executeUpdate("INSERT INTO sap_sales (`chkdis`,`lat`,`lng`,`sstid`,`amount_amtnt`,`client_mandt`,`created_by_ernam`,`created_on_erdat`,`customer_kunnr`,`description_maktg`,`fixsm_fixsm`,`fixsm_pcassm`,`material_matnr`,`materialgroup_1_mvgr1`,`month_zmonth`,`outlet_id`,`personnel_no_asmid`,`personnel_no_crid`,`personnel_no_rsmid`,`quty_quant`,`transaction_number`,`vehicle_vehnum`,`year_zyear`) "+ 
										" select 'portal' chkdis, '0' lat, '0' lng, '0' sstid, isap.net_amount amount_amtnt, '200' client_mandt, isa.created_by created_by_ername, date_format(isa.created_on,'%Y-%m-%d') created_on_erdat, isa.distributor_id customer_kunnr, '' description_maktg, '0' fixsm_fixsm, '0' fixsm_pcassm, ip.sap_code material_matnr, '0' materialgroup_1_mvgr1, date_format(isa.created_on,'%m') month_zmonth, isa.outlet_id outlet_id, '0' personnel_no_asmid, isa.booked_by personnel_no_crid,'0' personnel_no_rsmid, isap.raw_cases quty_quant, '0' transaction_number, '' vehicle_vehnum, date_format(isa.created_on, '%Y') year_zyear from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products ip on isap.product_id = ip.id "+
										" where created_on between "+SQLStDate+" and "+SQLEndDateNext+" and isap.is_promotion=0");
				
			obj.put("success", "true");	
			ds.commit();
			s.close();			
			ds.dropConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				ds.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			obj.put("error", e.toString());
			e.printStackTrace();
		}finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return obj.toString();
		
	}
	
}
