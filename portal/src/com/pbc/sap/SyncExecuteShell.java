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
import org.omg.CORBA.portable.OutputStream;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


@WebServlet(description = "Sync Execute Shell", urlPatterns = { "/sap/SyncExecuteShell" })
public class SyncExecuteShell extends HttpServlet {
	
       
    public SyncExecuteShell() {
        super();
    }

    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		PrintWriter out = response.getWriter();
		JSONObject obj=new JSONObject();
		String LogMsg="";
		try {
			
			int CommandID = Utilities.parseInt(request.getParameter("CommandID"));
			String argument1 = "";
			String argument2 = "";
			String exec = "";
			if(CommandID == 1){//for Distributor
				exec = "/home/ftpshared/BashFiles/SyncAR.sh";
			}
			if(CommandID == 2){//for Employees
				exec = "/home/ftpshared/BashFiles/SyncHCM.sh";
			}
			if(CommandID == 3){//for Outlet
				exec = "/home/ftpshared/BashFiles/SyncOutlets.sh";
			}
			if(CommandID == 4){//for Sales
				int Month = Utilities.parseInt(request.getParameter("Month"));
				int Year = Utilities.parseInt(request.getParameter("Year"));
				
				argument1 = Month+ "";
				argument2 = Year + "";
				
				exec = "/home/ftpshared/BashFiles/SyncSales.sh";
				//System.out.println(exec);
			}
			
			//System.out.println("About to execute:");
			//Runtime.getRuntime().exec(EnvirnmntVariableSet);
			
			/*ProcessBuilder builder = new ProcessBuilder( 
					"cmd.exe", "/c", "dir");*/
			ProcessBuilder builder = new ProcessBuilder( 
					exec,argument1,argument2
					);
	        builder.redirectErrorStream(true);
	        Process p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            LogMsg = LogMsg+line+"<br>";
	            //System.out.println(line);
	            obj.put("success", "true");	
	        }
	        obj.put("logmsg", LogMsg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();	
		
	}
	
}
