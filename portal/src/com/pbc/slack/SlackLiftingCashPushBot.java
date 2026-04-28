package com.pbc.slack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


public class SlackLiftingCashPushBot  {
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public static void main(String args[]){
		
		try{
			
			String ResponseURLSummary = "https://hooks.slack.com/services/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";
			
			String ResponseURLLifting = "https://hooks.slack.com/services/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";
			
			String ResponseURLProduction = "https://hooks.slack.com/services/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";

			StringBuffer production = new StringBuffer();
			production.append("{");
			production.append("\"text\": \""+ProductionUpdate()+"\",");
			production.append("}");
			
			StringBuffer lifting = new StringBuffer();
			lifting.append("{");
				lifting.append("\"text\": \""+LiftingUpdateUM()+"\",");
			lifting.append("}");

			StringBuffer cash = new StringBuffer();
			cash.append("{");
				cash.append("\"text\": \""+inflowsUpdateUM()+"\",");
			cash.append("}");
	
			executePost(ResponseURLSummary,production.toString());
			Thread.currentThread().sleep(5000);
			executePost(ResponseURLSummary,lifting.toString());
			Thread.currentThread().sleep(5000);
			executePost(ResponseURLSummary,cash.toString());
			
	
			executePost(ResponseURLProduction,production.toString());
			executePost(ResponseURLLifting,lifting.toString());

			

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static StringBuffer executePost(String targetURL, String urlParameters)
	  {
	    URL url;
	    HttpsURLConnection connection = null;  
	    
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      
	      SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); 
	      sslContext.init(null, null, new SecureRandom());
	      
	      connection = (HttpsURLConnection) url.openConnection();
	      connection.setSSLSocketFactory(sslContext.getSocketFactory());

	      
	      //connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("User-Agent", "Mozilla/5.0");
	      connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	      connection.setRequestProperty("Content-Type", "application/json");

	      //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	      //connection.setRequestProperty("Content-Language", "en-US");  
				
	      //connection.setUseCaches (false);
	      //connection.setDoInput(true);
	      connection.setDoOutput(true);

	      
	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response
	      
	      InputStream is =  connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        //response.append('\r');
	      }
	      
	     // out.println("Yoooo "+response);
	      
	      rd.close();
	      System.out.println("HTTPResponse:"+response);
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
	
	
    public static String LiftingUpdateUM() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
    	String response = "";
    	
    	Date YesterdayDate = new Date(); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        
    	try {
        	
     	   Datasource ds = new Datasource();
     	   ds.createConnectionToReplica();

     	   Statement s = ds.createStatement();
     	   Statement s2 = ds.createStatement();
     	   Statement s3 = ds.createStatement();
     	   
           int DayOfMonth = 1;
           ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
           if (rs10.first()){
         	  DayOfMonth = rs10.getInt(1);
           }
     	   
     	   
           String messageWA = "```Lifting in Converted Cases```\n\n`Today`\n";
           
           
           int AreaID[] = {1,2,4,5,6};
           
           
           double AllTotalToday = 0;
           for (int i = 0; i < AreaID.length; i++){
	           
        	   messageWA += "*"+EmployeeHierarchy.getSDHead(AreaID[i]).USER_DISPLAY_NAME+"*\n";
	           double TotalToday = 0;
	           ResultSet rs1 = s2.executeQuery("select distinct tdm_id, (select display_name from users where id = tdm_id) display_name from common_distributors where tdm_id is not null and snd_id = "+EmployeeHierarchy.getSDHead(AreaID[i]).USER_ID+" order by tdm_id");
	           while(rs1.next()){
	        	   
	        	   long RSMID = rs1.getLong(1);
	        	   String RSMName = rs1.getString(2).replace("Muhammad", "M.");
	               
	               double CasesToday = 0;
	               ResultSet rs2 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+RSMID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	               if(rs2.first()){
	             	  CasesToday = rs2.getDouble(1);
	               }
	               TotalToday += CasesToday;
	               if (CasesToday != 0){
	            	   messageWA += ">"+RSMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
	               }
	        	   
	           }
	           AllTotalToday += TotalToday;
	           messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalToday)+"\n";
           }
           messageWA += "Total Today: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AllTotalToday)+"\n";
           
           messageWA += "\n`MTD "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+"`\n";
           
           
           double AllTotalMTD = 0;
           for (int i = 0; i < AreaID.length; i++){
	           
        	   messageWA += "*"+EmployeeHierarchy.getSDHead(AreaID[i]).USER_DISPLAY_NAME+"*\n";
        	   
	           double TotalMTD = 0;
	           ResultSet rs4 = s3.executeQuery("select distinct tdm_id, (select display_name from users where id = tdm_id) display_name from common_distributors where tdm_id is not null and snd_id = "+EmployeeHierarchy.getSDHead(AreaID[i]).USER_ID+" order by tdm_id");
	           while(rs4.next()){
	        	   
	        	   long RSMID = rs4.getLong(1);
	        	   String RSMName = rs4.getString(2).replace("Muhammad", "M.");
	               
	               double CasesMTD = 0;
	               ResultSet rs3 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+RSMID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	               if(rs3.first()){
	             	  CasesMTD = rs3.getDouble(1);
	               }
	               TotalMTD+= CasesMTD;
	               
	               double AverageCases = CasesMTD / DayOfMonth;
	               if (CasesMTD != 0){
	            	   messageWA += ">"+RSMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCases)+"/Day)\n";
	               }
	        	   
	           }
	           AllTotalMTD+= TotalMTD;
	           messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD/DayOfMonth)+"/Day)\n";
           }
           
          double AverageCasesTotal = AllTotalMTD / DayOfMonth;
          
          
          messageWA += "Total MTD: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AllTotalMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesTotal)+"/Day)\n";

          response = messageWA;
          
          ds.dropConnection();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    	
    	return response;
    	
    }
	
    public static String LiftingUpdate() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
String response = "";
    	
    	Date YesterdayDate = new Date(); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        
    	try {
        	
     	   Datasource ds = new Datasource();
     	   ds.createConnectionToReplica();

     	   Statement s = ds.createStatement();
          
          //Converted Cases Today
          double CasesToday = 0;
          ResultSet rs2 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesToday = rs2.getDouble(1);
          }

  		
          //Converted Cases Today
          double CasesTodaySD3 = 0;
          ResultSet rs2SD3 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD3 = rs2SD3.getDouble(1);
          }
          
          
          
          //Converted Cases Today
          double CasesTodaySD2 = 0;
          ResultSet rs2SD2 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD2 = rs2SD2.getDouble(1);
          }
          
          //Converted Cases Today
          //double CasesTodaySD2B = 0;
//          ResultSet rs2SD2B = s.executeQuery("select  sum(idnp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in (780)) and idn.created_on between "+getSQLDateLifting(YesterdayDate)+" and "+getSQLDateNextLifting(YesterdayDate));
//          if(rs2SD2B.first()){
//        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
//        	  CasesTodaySD2B = rs2SD2B.getDouble(1);
//        	  
//          }

          //Converted Cases Today
          double CasesTodayBulk = 0;
          ResultSet rs2Bulk = s.executeQuery("select  sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(6).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2Bulk.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodayBulk = rs2Bulk.getDouble(1);
        	  
          }
          
          
          double CasesTodaySD5 = 0;
          ResultSet rs2SD5 = s.executeQuery("select  sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(7).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD5.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD5 = rs2SD5.getDouble(1);
        	  
          }
          
          
          double CasesTodaySD6 = 0;
          ResultSet rs2SD6 = s.executeQuery("select  sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(8).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2SD6.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodaySD6 = rs2SD6.getDouble(1);
        	  
          }
          
          double CasesTodayKA = 0;
          ResultSet rs2KA = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(5).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs2KA.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs2.getDouble(1)));
        	  CasesTodayKA = rs2KA.getDouble(1);
          }
      		
          //Converted Cases Month to date          
          double CasesMTD = 0;
          ResultSet rs3 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTD = rs3.getDouble(1);
          }
          
          
          double CasesMTDSD3 = 0;
          ResultSet rs3SD3 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3SD3.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD3 = rs3SD3.getDouble(1);
          }
          
          
          double CasesMTDSD2 = 0;
          ResultSet rs3SD2 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3SD2.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD2 = rs3SD2.getDouble(1);
          }
          
          double CasesMTDBulk = 0;
          ResultSet rs3Bulk = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(6).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3Bulk.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDBulk = rs3Bulk.getDouble(1);
          }
			
          double CasesMTDSD5 = 0;
          ResultSet rs3MTDSD5 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(7).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3MTDSD5.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD5 = rs3MTDSD5.getDouble(1);
          }
          
          double CasesMTDSD6 = 0;
          ResultSet rs3MTDSD6 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(8).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3MTDSD6.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDSD6 = rs3MTDSD6.getDouble(1);
          }
          
          
          double CasesMTDKA = 0;
          ResultSet rs3KA = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(5).USER_ID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
          if(rs3KA.first()){
        	  //System.out.println(Utilities.getDisplayCurrencyFormat(rs3.getDouble(1)));
        	  CasesMTDKA = rs3KA.getDouble(1);
          }

          int DayOfMonth = 1;
          ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
          if (rs10.first()){
        	  DayOfMonth = rs10.getInt(1);
          }
          
          
          double TotalMTD = CasesMTD+CasesMTDSD3+CasesMTDSD2+CasesMTDBulk+CasesMTDKA+CasesMTDSD5+CasesMTDSD6;
          
          double AverageCases = CasesMTD / DayOfMonth;
          double AverageCasesSD3 = CasesMTDSD3 / DayOfMonth;
          double AverageCasesSD2 = CasesMTDSD2 / DayOfMonth;
          //double AverageCasesSD2B = CasesMTDSD2B / DayOfMonth;
          double AverageCasesBulk = CasesMTDBulk / DayOfMonth;
          double AverageCasesSD5 = CasesMTDSD5 / DayOfMonth;
          double AverageCasesSD6 = CasesMTDSD6 / DayOfMonth;
          double AverageCasesKA = CasesMTDKA / DayOfMonth;
          double AverageCasesTotal = TotalMTD / DayOfMonth;
          
          
          String messageWA = "Lifting in Converted Cases\nToday\n";
          messageWA += "SD1: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD3)+"\n";
          messageWA += "SD2: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
          messageWA += "SD3: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD2)+"\n";
          messageWA += "SD4: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodayBulk)+"\n";
          messageWA += "SD5: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD5)+"\n";
          messageWA += "SD6: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodaySD6)+"\n";
          messageWA += "KA: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesTodayKA)+"\n";
          messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday+CasesTodaySD3+CasesTodaySD2+CasesTodayBulk+CasesTodayKA+CasesTodaySD5+CasesTodaySD6)+"\n";
          
          messageWA += "\nMTD "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWA += "SD1: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD3)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesSD3)+"/Day)\n";
          messageWA += "SD2: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCases)+"/Day)\n";
          messageWA += "SD3: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD2)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesSD2)+"/Day)\n";
          messageWA += "SD4: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDBulk)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesBulk)+"/Day)\n";
          messageWA += "SD5: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD5)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesSD5)+"/Day)\n";
          messageWA += "SD6: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDSD6)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesSD6)+"/Day)\n";
          messageWA += "KA: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTDKA)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesKA)+"/Day)\n";
          messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesTotal)+"/Day)\n";

          response = messageWA;
          
          ds.dropConnection();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    	
    	return response;
    	
    }
    public static String inflowsUpdateUM() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
    	String response = "";
    	
    	Date YesterdayDate = new Date(); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        
    	try {
        	
     	   Datasource ds = new Datasource();
     	   ds.createConnectionToReplica();

     	   Statement s = ds.createStatement();
     	   Statement s2 = ds.createStatement();
     	   Statement s3 = ds.createStatement();
     	   
           int DayOfMonth = 1;
           ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
           if (rs10.first()){
         	  DayOfMonth = rs10.getInt(1);
           }
     	   
           
           String messageWA = "```Inflows```\n\n`Today`\n";
           
           int AreaID[] = {1,2,4,5,6};
           
           
           double AllTotalToday = 0;
           for (int i = 0; i < AreaID.length; i++){
	           
        	   messageWA += "*"+EmployeeHierarchy.getSDHead(AreaID[i]).USER_DISPLAY_NAME+"*\n";

	           double TotalToday = 0;
	           ResultSet rs1 = s2.executeQuery("select distinct tdm_id, (select display_name from users where id = tdm_id) display_name from common_distributors where tdm_id is not null and snd_id = "+EmployeeHierarchy.getSDHead(AreaID[i]).USER_ID+" order by tdm_id");
	           while(rs1.next()){
	        	   
	        	   long RSMID = rs1.getLong(1);
	        	   String RSMName = rs1.getString(2).replace("Muhammad", "M.");
	               
	         		double CashTodaySD2 = 0;
	          		ResultSet crs2SD2 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where tdm_id = "+RSMID+") and glci.is_internal = 0 ");
	          		if(crs2SD2.first()){
	          			CashTodaySD2 = crs2SD2.getDouble(1);
	          		}
	               TotalToday += CashTodaySD2;
	               
	               
	               if (CashTodaySD2 != 0){
	            	   messageWA += ">"+RSMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD2)+"\n";
	               }
	               
	        	   
	           }
	           AllTotalToday += TotalToday;
	           messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalToday)+"\n";
           }
           
           messageWA += "Total Today: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AllTotalToday)+"\n";
           
           messageWA += "\n`Instruments`\n";
 			ResultSet crca = s.executeQuery("SELECT instrument_id, (select short_label from gl_cash_instruments where id= glcri.instrument_id) ilabel, sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glci.is_internal = 0 group by instrument_id");
 			while(crca.next()){
 				String CashTypeInstrument = crca.getString(2);
 				double CashTypeAmount = crca.getDouble(3);
 				messageWA += ">"+CashTypeInstrument+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTypeAmount)+"\n";
 			}
           
           
           messageWA += "\n`MTD "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+"`\n";
           
           
           double AllTotalMTD = 0;
           for (int i = 0; i < AreaID.length; i++){
	           
        	   messageWA += "*"+EmployeeHierarchy.getSDHead(AreaID[i]).USER_DISPLAY_NAME+"*\n";
           
	           double TotalMTD = 0;
	           ResultSet rs4 = s3.executeQuery("select distinct tdm_id, (select display_name from users where id = tdm_id) display_name from common_distributors where tdm_id is not null and snd_id = "+EmployeeHierarchy.getSDHead(AreaID[i]).USER_ID+" order by tdm_id");
	           while(rs4.next()){
	        	   
	        	   long RSMID = rs4.getLong(1);
	        	   String RSMName = rs4.getString(2).replace("Muhammad", "M.");
	               
					double CashMTD = 0;
					ResultSet crs7 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where tdm_id in ("+RSMID+")) and glci.is_internal = 0 ");
					if(crs7.first()){
						CashMTD = crs7.getDouble(1);
					}
	               TotalMTD+= CashMTD;
	               
	               double AverageCases = CashMTD / DayOfMonth;
	               if (CashMTD != 0){
	            	   messageWA += ">"+RSMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCases)+"/Day)\n";
	               }
	        	   
	           }
	           double AverageCasesTotal = TotalMTD / DayOfMonth;
	           AllTotalMTD += TotalMTD;
	           
	           messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCasesTotal)+"/Day)\n";
	           
           }
          double TotalAverageCasesTotal = AllTotalMTD / DayOfMonth;
          
          
          messageWA += "Total MTD: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AllTotalMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalAverageCasesTotal)+"/Day)\n";

          response = messageWA;
          
          ds.dropConnection();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    	
    	return response;
    	
    }
    
    public static String InflowsUpdate() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
    	
    	String response = "";
    	
    	Date YesterdayDate = new Date(); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        
    	try {
        	
     	   Datasource ds = new Datasource();
     	   ds.createConnection();

     	   Statement s = ds.createStatement();
          
  		double CashTodaySD2 = 0;
  		ResultSet crs2SD2 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(2).USER_ID+") and glci.is_internal = 0 ");
  		if(crs2SD2.first()){
  			CashTodaySD2 = crs2SD2.getDouble(1);
  		}

          
		double CashToday = 0;
		ResultSet crs2 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(1).USER_ID+") and glci.is_internal = 0 ");
		if(crs2.first()){
			CashToday = crs2.getDouble(1);
		}
          
          
  		double CashTodaySD3 = 0;
  		ResultSet crs2SD3 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(4).USER_ID+") and glci.is_internal = 0 ");
  		if(crs2SD3.first()){
  			CashTodaySD3 = crs2SD3.getDouble(1);
  		}
          //Converted Cases Today
    		double CashTodaySD4 = 0;
      		ResultSet crs2SD4 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(6).USER_ID+") and glci.is_internal = 0 ");
      		if(crs2SD4.first()){
      			CashTodaySD4 = crs2SD4.getDouble(1);
      		}
          
  		double CashTodaySD5 = 0;
  		ResultSet crs2SD5 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(7).USER_ID+") and glci.is_internal = 0 ");
  		if(crs2SD5.first()){
  			CashTodaySD5 = crs2SD5.getDouble(1);
  		}
  		
  		double CashTodaySD6 = 0;
  		ResultSet crs2SD6 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(8).USER_ID+") and glci.is_internal = 0 ");
  		if(crs2SD6.first()){
  			CashTodaySD6 = crs2SD6.getDouble(1);
  		}
  		
    		double CashTodayKA = 0;
      		ResultSet crs2KA = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(5).USER_ID+") and glci.is_internal = 0 ");
      		if(crs2KA.first()){
      			CashTodayKA = crs2KA.getDouble(1);
      		}
      		
			double CashMTDSD2 = 0;
			ResultSet crs7SD2 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(2).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7SD2.first()){
				CashMTDSD2 = crs7SD2.getDouble(1);
			}
          
          
      		double CashMTD = 0;
			ResultSet crs7 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(1).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7.first()){
				CashMTD = crs7.getDouble(1);
			}
          
          
			double CashMTDSD3 = 0;
			ResultSet crs7SD3 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(4).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7SD3.first()){
				CashMTDSD3 = crs7SD3.getDouble(1);
			}
          
          	double CashMTDSD4 = 0;
			ResultSet crs7SD4 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(6).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7SD4.first()){
				CashMTDSD4 = crs7SD4.getDouble(1);
			}

	    	double CashMTDSD5 = 0;
			ResultSet crs7SD5 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(7).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7SD5.first()){
				CashMTDSD5 = crs7SD5.getDouble(1);
			}
			
			double CashMTDSD6 = 0;
			ResultSet crs7SD6 = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(8).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7SD6.first()){
				CashMTDSD6 = crs7SD6.getDouble(1);
			}
			
	    	double CashMTDKA = 0;
			ResultSet crs7KA = s.executeQuery("SELECT sum(glcri.amount) FROM gl_cash_receipts glcr join gl_cash_receipts_instruments glcri on glcr.id=glcri.id join gl_cash_instruments glci on glcri.instrument_id = glci.id where glcr.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and glcr.customer_id in (select distributor_id from common_distributors where snd_id in ("+EmployeeHierarchy.getSDHead(5).USER_ID+")) and glci.is_internal = 0 ");
			if(crs7KA.first()){
				CashMTDKA = crs7KA.getDouble(1);
			}
          

          int DayOfMonth = 1;
          ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
          if (rs10.first()){
        	  DayOfMonth = rs10.getInt(1);
          }
          
          
          double TotalMTDCash = CashMTD+CashMTDSD2+CashMTDSD3+CashMTDSD4+CashMTDSD5+CashMTDKA+CashMTDSD6;
          
          double AverageCash = CashMTD / DayOfMonth;
          double AverageCashSD2 = CashMTDSD2 / DayOfMonth;
          double AverageCashSD3 = CashMTDSD3 / DayOfMonth;
          double AverageCashSD4 = CashMTDSD4 / DayOfMonth;
          double AverageCashSD5 = CashMTDSD5 / DayOfMonth;
          double AverageCashSD6 = CashMTDSD6 / DayOfMonth;
          double AverageCashKA = CashMTDKA / DayOfMonth;
          double AverageCashTotal = TotalMTDCash / DayOfMonth;
          
          String messageWAC = "Inflows\nToday\n";
          messageWAC += "SD1: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashToday)+"\n";
          messageWAC += "SD2: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD2)+"\n";
          messageWAC += "SD3: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD3)+"\n";
          messageWAC += "SD4: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD4)+"\n";
          messageWAC += "SD5: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD5)+"\n";
          messageWAC += "SD6: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodaySD6)+"\n";
          messageWAC += "KA: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashTodayKA)+"\n";
          messageWAC += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashToday+CashTodaySD2+CashTodaySD3+CashTodaySD4+CashTodaySD5+CashTodayKA+CashTodaySD6)+"\n";
          
          messageWAC += "\nMTD "+Utilities.getDisplayDateFullMonthYearFormat(YesterdayDate)+":\n";
          messageWAC += "SD1: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTD)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCash)+"/Day)\n";
          messageWAC += "SD2: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD2)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD2)+"/Day)\n";
          messageWAC += "SD3: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD3)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD3)+"/Day)\n";
          messageWAC += "SD4: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD4)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD4)+"/Day)\n";
          messageWAC += "SD5: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD5)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD5)+"/Day)\n";
          messageWAC += "SD6: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDSD6)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashSD6)+"/Day)\n";
          messageWAC += "KA: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CashMTDKA)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashKA)+"/Day)\n";
          messageWAC += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTDCash)+" ("+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AverageCashTotal)+"/Day)\n";
          
          response = messageWAC;
          
          ds.dropConnection();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    	
    	return response;
    	
    }    
    public static String ProductionUpdate() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
    	
    	String response = "";
    	
    	Date YesterdayDate = new Date(); 
        double ConvertedCases = 0;
        
    	try {
    	
    		
      	   Datasource ds = new Datasource();
      	   ds.createConnection();
    		Statement s = ds.createStatement();
    		Statement s2 = ds.createStatement();
    		
     		  String WAMessage = "```Production Summary```\n\n`Today`";
       		  
              String Sql1 = "select ip.category_id,ip.package_id,ipa.label,ipa.unit_per_case,sum(iprp.total_units) bottles,sum(iprp.liquid_in_ml) / ipa.conversion_rate_in_ml converted_cases from inventory_production_receipts ipr, inventory_production_receipts_products iprp, inventory_products ip, inventory_packages ipa, inventory_brands ib where ipr.id = iprp.id and iprp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and ipr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and ip.category_id = 1 group by ip.category_id, ip.package_id order by ip.category_id, ipa.sort_order";
              ResultSet rs1 = s.executeQuery(Sql1);
              while(rs1.next()){
              	
            	ConvertedCases += rs1.getDouble("converted_cases");
            	  
    			String PackageLabel = rs1.getString("ipa.label");
    			String PackageRawCases = Utilities.convertToRawCases(rs1.getLong("bottles"), rs1.getInt("ipa.unit_per_case"));
    			
    			WAMessage += "\n\n*" +PackageLabel +"*\t *"+PackageRawCases+"*";
    			
    			String Sql = "select ip.category_id, ip.package_id, ipa.label, ip.brand_id, ib.label, ip.unit_per_sku, sum(iprp.total_units) bottles, sum(iprp.liquid_in_ml)/ipa.conversion_rate_in_ml converted_cases from inventory_production_receipts ipr, inventory_production_receipts_products iprp, inventory_products ip, inventory_packages ipa, inventory_brands ib where ipr.id = iprp.id and iprp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id  and ipr.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+" and ip.category_id = 1  and ip.package_id="+rs1.getInt("ip.package_id")+"  group by ip.category_id, ip.package_id, ip.brand_id order by ip.category_id, ip.package_id, ip.brand_id";
    			ResultSet rs2 = s2.executeQuery(Sql);
    			while( rs2.next() ){
                  	
    				String BrandLabel = rs2.getString("ib.label");
      				String BrandRawCases = Utilities.convertToRawCases( rs2.getLong("bottles"), rs2.getInt("ip.unit_per_sku"));
      				
      				WAMessage += "\n>" +BrandLabel +"\t "+BrandRawCases;
      				
                  }
                 
              }
             
              WAMessage += "\n\nTotal Converted\t "+ Utilities.getDisplayCurrencyFormatRounded(ConvertedCases)+"";
    		
              response = WAMessage;
    		
    		s.close();
    		ds.dropConnection();
    		
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    	
    	return response;
    	
    }
}
