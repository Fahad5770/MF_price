//package com.pbc.bi;
package com.pbc.bi;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;

import com.pbc.bi.BiProcesses;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;




public class DispatchBlocking {
	static Datasource ds = new Datasource();
	public static void main(String[] args) {
		try {
			
			ds.createConnection();
			//ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3=ds.createStatement();
			Statement s4=ds.createStatement();
			Statement s5=ds.createStatement();
			
			Date CurrentDate = new Date();
			
			Date CreatedOn = null;
			long MainTableID=0;
			
			//System.out.println("SELECT * FROM pep.inventory_sales_dispatch where is_adjusted=0 and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by id desc limit 100000");
			ResultSet rs = s.executeQuery("SELECT * FROM pep.inventory_sales_dispatch where is_adjusted=0 and is_blocked=0 order by id desc");
			while(rs.next()) {
				CreatedOn = rs.getDate("created_on");
				MainTableID = rs.getLong("id");
			
				 float daysBetween = ((CurrentDate.getTime()-CreatedOn.getTime()) / (1000*60*60*24));
				
				if(daysBetween>3) {
					//System.out.println("Yes Not a Valid Date Range "+CreatedOn+" - "+daysBetween);
					
					
					s2.executeUpdate("update inventory_sales_dispatch set is_blocked=1, blocked_on=now(), blocked_by=1 where id="+MainTableID);
				}
				
			}
			
		    s5.close();
		    s3.close(); 
			s2.close();
			s.close();
			ds.dropConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
				e.printStackTrace();
			}			
			finally {
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		

		
	}

}
