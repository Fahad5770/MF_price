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

@WebServlet(description = "", urlPatterns = { "/mobile/BankAlflahMoneyTransactionExecute" })
public class BankAlflahMoneyTransaction extends HttpServlet { 
	private static final long serialVersionUID = 1L;
       
    public BankAlflahMoneyTransaction() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Yes I am called Bank Alfalah Transaction ....");
		
		//String Acco1untFrom =Utilities.filterString(request.getParameter("Email"), 1, 100);//mr.getParameter("AccountFrom");
		//String Accou1ntTo = Utilities.filterString(request.getParameter("Password"), 1, 100);//mr.getParameter("AccountTo");
		//System.out.println(Acco1untFrom+"-"+Accou1ntTo);
		
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
		
		if (!mr.isExpired()){
			
			String AccountFrom =Utilities.filterString(mr.getParameter("AccountFrom"), 1, 100);//mr.getParameter("AccountFrom");
			String AccountTo = Utilities.filterString(mr.getParameter("AccountTo"), 1, 100);//mr.getParameter("AccountTo");
			Double Amount = Utilities.parseDouble(mr.getParameter("Amount"));//mr.getParameter("Amount");
			String PinCode=Utilities.filterString(mr.getParameter("pin_code"),1,100);//mr.getParameter("Pin Code");
			String MobileNo =Utilities.filterString(mr.getParameter("MobileNo"),1,100);//mr.getParameter("MobileNo");
			String  TRefNumb=Utilities.filterString(mr.getParameter("TRefNumb"),1,100);//mr.getParameter("TRefNumb");
			
			System.out.println(PinCode);
			
			
		//String credientialsString="KEY_TRANSACTION_REF=130046808680&&KEY_AMOUNT="+Amount+"&&KEY_FROM_ACCOUNT="+AccountFrom+"&&KEY_TO_ACCOUNT="+AccountTo+"&&KEY_CHANNEL=sd_weblite&&KEY_TRAN_TYPE=Deposit&&KEY_CHANNEL_MOBILE="+MobileNo+"&&KEY_CHANNEL_PIN=1111&&KEY_CHANNEL_USERNAME=whassan&&OTP_VAL_FLAG=Y&&OTP="+PinCode;
		//"KEY_AMOUNT="+Amount+"&&KEY_FROM_ACCOUNT="+AccountFrom+"&&KEY_TO_ACCOUNT=930503412900338&&KEY_CHANNEL=KWICKPAY&&KEY_CHANNEL_USERNAME=111111111111&&KEY_CHANNEL_MOBILE=03449279270&&KEY_FROM_OR_TO=1&&GENERATE_OTP=Y";
		String credientialsString="KEY_TRANSACTION_REF="+TRefNumb+"&&KEY_AMOUNT="+Amount+"&&KEY_FROM_ACCOUNT="+AccountFrom+"&&KEY_TO_ACCOUNT="+AccountTo+"&&KEY_CHANNEL=fee_no_pin&&KEY_TRAN_TYPE=PaymentCollection&&KEY_CHANNEL_MOBILE="+MobileNo+"&&KEY_CHANNEL_PIN=1111&&KEY_FEATURE=&&KEY_CONSUMER_NUMBER=&&KEY_CHANNEL_USERNAME="+AccountTo+"&&OTP_VAL_FLAG=Y&&OTP="+PinCode;
					
					
					//"KEY_TRANSACTION_REF=130046808680&&KEY_AMOUNT="+Amount+"&&KEY_FROM_ACCOUNT="+AccountFrom+"&&KEY_TO_ACCOUNT="+AccountTo+"&&KEY_CHANNEL=sd_weblite&&KEY_TRAN_TYPE=Deposit&&KEY_CHANNEL_MOBILE=&&KEY_CHANNEL_PIN=1111&&KEY_CHANNEL_USERNAME=whassan&&OTP_VAL_FLAG=Y&&OTP="+PinCode;	
				
		byte[]   bytesEncoded = Base64.encodeBase64(credientialsString .getBytes());
		encodeStringwithEquals= new String(bytesEncoded);
				 
		URL = "http://192.168.102.97:7806/Wallet-API/services/wallet/MoneyTransfer,"+encodeStringwithEquals;
		System.out.println("Transaction URL"+URL);
		
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
