package com.pbc.mobile;

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

import com.pbc.mobile.MobileRequest;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;
import org.apache.commons.codec.binary.Base64;
//import javax.xml.bind.DatatypeConverter;
//import java.util.Base64;
import java.io.UnsupportedEncodingException;

@WebServlet(description = "", urlPatterns = { "/mobile/BankAlflahTestExecute" })
public class BankAlflahTestExecute extends HttpServlet { 
	private static final long serialVersionUID = 1L;
       
    public BankAlflahTestExecute() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Yes I am called Bank Alfalah....");
		
		HttpSession session = request.getSession();
		
		 
		JSONObject json = new JSONObject();
		/*
				String UserID = null;

				if (session.getAttribute("UserID") != null){
					UserID = (String)session.getAttribute("UserID");
				}
				
				if (UserID == null){
					//response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
				}
		*/
		PrintWriter out = response.getWriter();
		
		
		////farhan work starts
		String URL="";
		String encodeStringwithEquals="";
		MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));
		//System.out.println("mr "+mr);
		if (!mr.isExpired()){
			
			long AccountFrom = 0;//mr.getParameter("AccountFrom");
			String AccountTo = Utilities.filterString(mr.getParameter("AccountTo"), 1, 100);
			Double Amount = Utilities.parseDouble(mr.getParameter("Amount"));
			
			//System.out.println(AccountFrom+" : "+AccountTo+" : "+Amount+"  ");
		
		//out.print("Hello...");
		//String credientialsString="KEY_AMOUNT=1&&KEY_FROM_ACCOUNT=0000025180927&&KEY_CHANNEL=sd_weblite&&KEY_CHANNEL_MOBILE=03449279270&&KEY_CHANNEL_USERNAME=whassan&&KEY_TO_WALLET=930003217898578&&GENERATE_OTP=Y";
		
		
		
		String credientialsString="KEY_AMOUNT="+Amount+"&&KEY_FROM_ACCOUNT=123123131&&KEY_TO_ACCOUNT="+AccountTo+"&&KEY_CHANNEL=KWICKPAY&&KEY_CHANNEL_USERNAME=111111111111&&KEY_CHANNEL_MOBILE=03449279270&&KEY_FROM_OR_TO=1&&GENERATE_OTP=Y";
		
		
		System.out.println("credientialsString : "+credientialsString);
		
		byte[]   bytesEncoded = Base64.encodeBase64(credientialsString .getBytes());
		encodeStringwithEquals= new String(bytesEncoded);
		
		System.out.println("encoded String with Equals : "+encodeStringwithEquals);
		
		 System.out.println("Without equals : "+encodeStringwithEquals);
		
		
		 
		 
		 
		URL = "http://192.168.102.97:7806/Wallet-API/services/wallet/TitleFetch,"+encodeStringwithEquals;
		System.out.println("URL is " + URL);
		
		}
		
		
		StringBuffer ResponseIs=excutePost(URL,"",out);
		json.put("success", "true");
		json.put("ResponseIs", ResponseIs.toString());
		//excutePost(URL,"",out);
		
		
		out.print(json);
		out.close();
	////farhan Work Ends
	}
	
	
	public static StringBuffer excutePost(String targetURL, String urlParameters,PrintWriter out)
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
	      
	     // out.println("Yoooo "+response);
	      
	      rd.close();
	      return response;

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
