package com.pbc.bi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.commons.lang3.time.DateUtils;

//import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class TestingAlflah {

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
	          
	        
			
			String urlParameters =
			        "fName=" + URLEncoder.encode("Zulqurnan", "UTF-8") +
			        "&lName=" + URLEncoder.encode("Aslam", "UTF-8");
			
			/////String URL = "http://192.168.102.97:7806/Wallet-API/services/wallet/TitleFetch,S0VZX0FNT1VOVD0yNTAwMC4wMCYmS0VZX0ZST01fQUNDT1VOVD0xMjMxMjMxMzEmJktFWV9UT19BQ0NPVU5UPTkzMDUwMzQxMjkwMDMzOCYmS0VZX0NIQU5ORUw9S1dJQ0tQQVkmJktFWV9DSEFOTkVMX1VTRVJOQU1FPTExMTExMTExMTExMSYmS0VZX0NIQU5ORUxfTU9CSUxFPTAzNDEyOTAwMzM4JiZLRVlfRlJPTV9PUl9UTz0x";
			
			
			String URL = "http://192.168.102.97:7806/Wallet-API/services/wallet/MoneyTransfer,S0VZX1RSQU5TQUNUSU9OX1JFRj0xMzAwNDY4MDg2ODAmJktFWV9BTU9VTlQ9NS4wMCYmS0VZX0ZST01fQUNDT1VOVD0wMzQzMzg5MDk4NyYmS0VZX1RPX0FDQ09VTlQ9OTMwMDAzMjE3ODk4NTc4JiZLRVlfQ0hBTk5FTD1zZF93ZWJsaXRlJiZLRVlfVFJBTl9UWVBFPURlcG9zaXQmJktFWV9DSEFOTkVMX01PQklMRT0wMzQzMzg5MDk4NyYmS0VZX0NIQU5ORUxfUElOPTExMTEmJktFWV9DSEFOTkVMX1VTRVJOQU1FPXdoYXNzYW4mJktFWV9UT19XQUxMRVQ9OTMwMDAzMjE3ODk4NTc4";
			
			
			excutePost(URL,"");
           
                       
			
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
	
	
	public static String excutePost(String targetURL, String urlParameters)
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
