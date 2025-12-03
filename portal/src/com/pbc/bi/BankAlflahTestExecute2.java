package com.pbc.bi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "", urlPatterns = { "/bi/BankAlflahTestExecute2" })
public class BankAlflahTestExecute2 extends HttpServlet { 
	private static final long serialVersionUID = 1L;
       
    public BankAlflahTestExecute2() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			//response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}

		PrintWriter out = response.getWriter();
		
		
		
		
		
		out.print("Hello...");
		
		
		//////String URL = "http://192.168.102.97:7806/Wallet-API/services/wallet/TitleFetch,S0VZX0FNT1VOVD0yNTAwMC4wMCYmS0VZX0ZST01fQUNDT1VOVD0xMjMxMjMxMzEmJktFWV9UT19BQ0NPVU5UPTkzMDUwMzQxMjkwMDMzOCYmS0VZX0NIQU5ORUw9S1dJQ0tQQVkmJktFWV9DSEFOTkVMX1VTRVJOQU1FPTExMTExMTExMTExMSYmS0VZX0NIQU5ORUxfTU9CSUxFPTAzNDEyOTAwMzM4JiZLRVlfRlJPTV9PUl9UTz0x";
		
		
		String URL = "http://192.168.102.97:7806/Wallet-API/services/wallet/MoneyTransfer,S0VZX1RSQU5TQUNUSU9OX1JFRj0xMzAwNDY4MDg2ODAmJktFWV9BTU9VTlQ9MSYmS0VZX0ZST01fQUNDT1VOVD0yMjIyMjImJktFWV9UT19BQ0NPVU5UPTkzMDUwMzQyOTkwMDAwOCYmS0VZX0NIQU5ORUw9c2Rfd2VibGl0ZSYmS0VZX1RSQU5fVFlQRT1EZXBvc2l0JiZLRVlfQ0hBTk5FTF9NT0JJTEU9MDM0MzM4OTA5ODcmJktFWV9DSEFOTkVMX1BJTj0xMTExJiZLRVlfQ0hBTk5FTF9VU0VSTkFNRT13aGFzc2FuJiZLRVlfVE9fV0FMTEVUPTkzMDAwMzIxNzg5ODU3OA";
		
		excutePost(URL,"",out);
		
		
		//out.print(obj);
		out.close();
		
	}
	
	
	public static String excutePost(String targetURL, String urlParameters,PrintWriter out)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    
	    
	    
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	     // wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is =  connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      
	      out.println("Yoooo "+response);
	      
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
	
	
}
