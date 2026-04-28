package com.pbc.bi;

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


@WebServlet(description = "Bi Batch", urlPatterns = { "/bi/BiOrderBookerStatistics" })
public class BiOrderBookerStatistics extends HttpServlet {
	
       
    public BiOrderBookerStatistics() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		Date StartDDate = Utilities.parseDate(Utilities.filterString(request.getParameter("StartDate1"),1,100));
		Date EndDDate = Utilities.parseDate(Utilities.filterString(request.getParameter("EndDate1"),1,100));
		try {
			BiProcesses bip = new BiProcesses();
			if(bip.CreateOrderBookerStatistics(StartDDate, EndDDate)){
				obj.put("success", "true");
			}
			bip.close();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			ds.startTransaction();
			int rank=1;
			ResultSet rs = s.executeQuery("SELECT booked_by,avg(total_amount_day_wise) avg_sales,sum(total_amount_day_wise) total_sales from ("+
					  "SELECT booked_by,DATE(created_on) created_date,sum(net_amount) total_amount_day_wise FROM pep.inventory_sales_adjusted  group by DATE(created_on),booked_by order by total_amount_day_wise desc"+
					  ") t group by booked_by order by total_sales desc");
			s1.executeUpdate("DELETE  FROM bi_order_booker_ranking");//deleting old records
			while( rs.next() ){
				
				s1.executeUpdate("insert into bi_order_booker_ranking(order_booker_id,total_sales,average_sales_per_day,created_on,rank) values("+rs.getLong("booked_by")+","+rs.getDouble("total_sales")+","+rs.getDouble("avg_sales")+",now(),"+rank+")");
				rank++;
			}
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
		}*/
		
		
		out.print(obj);
		out.close();
		
	}
	
}
