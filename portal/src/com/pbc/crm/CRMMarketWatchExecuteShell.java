package com.pbc.crm;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import com.pbc.util.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@WebServlet(description = "Market Watch Execute Shell", urlPatterns = { "/crm/CRMMarketWatchExecuteShell" })
public class CRMMarketWatchExecuteShell extends HttpServlet {
	
       
    public CRMMarketWatchExecuteShell() {
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
			
			
			if(CommandID == 1){//for test
				exec = "/home/ftpshared/BashFiles/EmailMarketWatchTest.sh";
			}
			if(CommandID == 2){//for Live
				exec = "/home/ftpshared/BashFiles/EmailMarketWatch.sh";
			}
			
			ProcessBuilder builder = new ProcessBuilder( 
					exec,argument1,argument2
					);
	        builder.redirectErrorStream(true);
	        Process p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true){
	            line = r.readLine();
	            if (line == null) {
	            	break;
	            }
	        }
            LogMsg = LogMsg+line+"<br>";
            obj.put("success", "true");	
	        
	        obj.put("logmsg", LogMsg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.print(obj);
		out.close();	
		
	}
	
}
