package com.pbc.slack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.pbc.employee.EmployeeHierarchy;
import com.pbc.util.Datasource;
import com.pbc.util.Utilities;


import javax.net.ssl.SSLContext;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;

/**
 * Servlet implementation class SlackLiftingBot
 */
@WebServlet("/slack/SlackLiftingBot")
public class SlackLiftingBot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SlackLiftingBot() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	
	boolean isValidLiftingUser(String user){
		boolean ret = true;
		String users[] = {"U015U3V7U7Q"};
		for (String u: users){
			if(u.equals(user)){
				ret = true;
			}
		}
		return ret;
		//Slack:qfcj0lrL8f5p2qT96U4T0By6 T09QW1SPR pbcpk U9S4K8M6V omerfk /secondarysales
	}
	
	
	
	
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		// U015U3V7U7Q Head Office 
		
		
		response.setContentType("application/json");
		
		
		
		PrintWriter out = response.getWriter();
		
		String token = request.getParameter("token");
		String TeamID = request.getParameter("team_id");
		final String ResponseURL = request.getParameter("response_url");
		String UserName = request.getParameter("user_name");
		final String UserID = request.getParameter("user_id");
		String TeamDomain = request.getParameter("team_domain");
		final String command = request.getParameter("command");
		final String text = request.getParameter("text");
		
		
		
		System.out.println("Slack:"+token+" "+TeamID+" "+TeamDomain+" "+UserID+" "+UserName+" "+command+" "+text);
		
		if (token.equals("tkGwETqBCFMdFF928FAN79gf") && TeamID.equals("T0161249NBV")){
		
		try{
		
			//LiftingUpdate2();
			
		StringBuffer sp = new StringBuffer();
		sp.append("{");
			sp.append("\"response_type\": \"in_channel\",");
			sp.append("\"text\": \"Hi "+UserName+"\nI'm crunching numbers, please wait...\"");
//			sp.append("\"attachments\": [");
//				sp.append("{");
//					sp.append("\"text\":\"I'm crunching numbers, need a few seconds...\"");
//				sp.append("}");
//			sp.append("]");
		sp.append("}");
		
		out.print(sp);
		
		Thread thread = new Thread(){
		    public void run(){
		    	try{
					StringBuffer sp2 = new StringBuffer();
					sp2.append("{");
						sp2.append("\"response_type\": \"in_channel\",");
						
						if (isValidLiftingUser(UserID)){
							if (command.contains("lifting")){
								//sp2.append("\"text\": \""+LiftingUpdateUM()+"\",");
							} else if (command.contains("cash")){
								if (!UserID.equals("U9SR3SV24")){
									//sp2.append("\"text\": \""+inflowsUpdateUM()+"\",");
								}
							} else if (command.contains("production")){
								//sp2.append("\"text\": \""+ProductionUpdate()+"\",");
							} else if (command.contains("orders")){
								sp2.append("\"text\": \""+secondarySalesUpdateUM(text)+"\",");
							}
						}else{
							sp2.append("\"text\": \"Insufficient privileges. Please register for TheiaBot.\",");
						}
//						sp.append("\"attachments\": [");
//						sp.append("{");
//							sp.append("\"text\":\"I'm crunching numbers, need a few seconds...\"");
//						sp.append("}");
//						sp.append("]");
						
					sp2.append("}");
	
					executePost(ResponseURL,sp2.toString());
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		    }
		  };

		  thread.start();
		
		
		//out.print("t:"+token);
		//out.print("t:"+TeamID);
		
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		}
	}
	
	boolean isValidUserOverall(String slackID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		boolean ret = false;
		
		Datasource ds = new Datasource();
   	    ds.createConnectionToReplica();

   	    Statement s = ds.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT * FROM pep.sms_slack_user_access where access_level_id=1 and slack_id='"+slackID+"'");
		if(rs.first()) {
			ret=true;
		}
		
		
		return ret;
	}
	
	boolean isValidUserInflowsUpdateUM(String slackID) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		boolean ret = false;
		
		Datasource ds = new Datasource();
   	    ds.createConnectionToReplica();

   	    Statement s = ds.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT * FROM pep.sms_slack_user_access where access_level_id=3 and slack_id='"+slackID+"'");
		if(rs.first()) {
			ret=true;
		}
		
		
		return ret;
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

	      
	      
	      System.out.println("1"+targetURL);
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
	      System.out.println("2"+targetURL);
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response
	      System.out.println("3"+targetURL);
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
	
    public static String LiftingUpdate2() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
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
    
    
    public static String LiftingUpdateUM() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
    	String response = "";
    	
    	Date YesterdayDate = new Date(); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date MonthToDateStartDate = Utilities.getStartDateByMonth(month, year);
        Date MonthEndDate = Utilities.getEndDateByMonth(month, year);
        
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
           int TotalDaysInMonth = 1;
           ResultSet rs101 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(MonthEndDate)+" as date))");
           if (rs101.first()){
        	   TotalDaysInMonth = rs101.getInt(1);
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
        	   
        	   double SNDTotalTarget = 0;
        	   ResultSet trs = s3.executeQuery("select sum((dtp.quantity*unit_per_case*ip.liquid_in_ml)/6000) from distributor_targets_packages dtp join inventory_packages ip on dtp.package_id = ip.id where dtp.id in ( "+
        			   								"SELECT id FROM pep.distributor_targets where month = month(curdate()) and year = year(curdate()) and distributor_id in ( "+
        			   									"select distributor_id from common_distributors where snd_id = "+EmployeeHierarchy.getSDHead(AreaID[i]).USER_ID+" "+
        			   								") "+
        			   							")");
        	   if (trs.first()){
        		   SNDTotalTarget = trs.getDouble(1);
        	   }
        	   double SNDMTDTarget = (SNDTotalTarget/(double)TotalDaysInMonth)*DayOfMonth;
        	   
	           double TotalMTD = 0;
	           ResultSet rs4 = s3.executeQuery("select distinct tdm_id, (select display_name from users where id = tdm_id) display_name from common_distributors where tdm_id is not null and snd_id = "+EmployeeHierarchy.getSDHead(AreaID[i]).USER_ID+" order by tdm_id");
	           while(rs4.next()){
	        	   
	        	   long RSMID = rs4.getLong(1);
	        	   String RSMName = rs4.getString(2).replace("Muhammad", "M.");
	               
	        	   double RSMTotalTarget = 0;
	        	   ResultSet trs1 = s.executeQuery("select sum((dtp.quantity*unit_per_case*ip.liquid_in_ml)/6000) from distributor_targets_packages dtp join inventory_packages ip on dtp.package_id = ip.id where dtp.id in ( "+
	        			   								"SELECT id FROM pep.distributor_targets where month = month(curdate()) and year = year(curdate()) and distributor_id in ( "+
	        			   									"select distributor_id from common_distributors where tdm_id = "+RSMID+" "+
	        			   								") "+
	        			   							")");
	        	   if (trs1.first()){
	        		   RSMTotalTarget = trs1.getDouble(1);
	        	   }
	        	   double RSMMTDTarget = (RSMTotalTarget/(double)TotalDaysInMonth)*DayOfMonth;
	        	   
	        	   
	               double CasesMTD = 0;
	               ResultSet rs3 = s.executeQuery("select sum(idnp.total_units*ip.liquid_per_unit)/6000 from inventory_delivery_note idn, inventory_delivery_note_products idnp, inventory_products ip, inventory_packages ipa, inventory_brands ib, common_distributors cd where idn.delivery_id = idnp.delivery_id and idnp.product_id = ip.id and ip.package_id = ipa.id and ip.brand_id = ib.id and idn.distributor_id = cd.distributor_id and ip.category_id = 1 and idn.distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+RSMID+")) and idn.created_on between "+Utilities.getSQLDateLifting(MonthToDateStartDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate));
	               if(rs3.first()){
	             	  CasesMTD = rs3.getDouble(1);
	               }
	               TotalMTD+= CasesMTD;
	               
		           double RSMTotalTargetAchieved = 0;
		           double RSMMTDTargetAchieved = 0;
		           if (RSMTotalTarget != 0){
		        	   RSMTotalTargetAchieved = (CasesMTD/RSMTotalTarget)*100;
		        	   RSMMTDTargetAchieved = (CasesMTD/RSMMTDTarget)*100;
		           }
	               
	               if (CasesMTD != 0){
	            	   messageWA += ">"+RSMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesMTD)+" | "+Utilities.getDisplayCurrencyFormatRounded(RSMMTDTargetAchieved)+"% | "+Utilities.getDisplayCurrencyFormatRounded(RSMTotalTargetAchieved)+"%\n";
	               }
	        	   
	           }
	           AllTotalMTD+= TotalMTD;

	           
	           double SNDTotalTargetAchieved = 0;
	           double SNDMTDTargetAchieved = 0;
	           if (SNDTotalTarget != 0){
	        	   SNDTotalTargetAchieved = (TotalMTD/SNDTotalTarget)*100;
	        	   SNDMTDTargetAchieved = (TotalMTD/SNDMTDTarget)*100;
	           }
	           
	           
	           messageWA += "Total: `"+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalMTD)+" | "+Utilities.getDisplayCurrencyFormatRounded(SNDMTDTargetAchieved)+"% | "+Utilities.getDisplayCurrencyFormatRounded(SNDTotalTargetAchieved)+"%`\n";
           }
           
          double AverageCasesTotal = AllTotalMTD / DayOfMonth;
          
          
          messageWA += "Total MTD: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AllTotalMTD)+"\n";

          response = messageWA;
          
          ds.dropConnection();
          
       }catch(Exception e)
       {
          e.printStackTrace();
       }
    	
    	return response;
    	
    }
    
    public static String secondarySalesUpdateUM(String pack) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException{
    	
    	
    	//int PackageID = Utilities.parseInt(pack);
    	
    	String response = "";
    	
    	Date YesterdayDate = new Date(); 
    	
    	Calendar cc = Calendar.getInstance();
    	cc.setTime(YesterdayDate);
    	int year = cc.get(Calendar.YEAR);
    	int month = cc.get(Calendar.MONTH);
       
        Date StartDate = new Date();
        Date EndDate = new Date();
        
        //MTD, mtd
        
    	if(pack.toLowerCase().equals("mtd")) {
    		StartDate = Utilities.getStartDateByMonth(month, year);
    		EndDate = YesterdayDate;
    	}else {
    		StartDate = YesterdayDate;
    		EndDate = YesterdayDate;
    	}
        
        
    	try {
        	
     	   Datasource ds = new Datasource();
     	   ds.createConnectionToReplica();

     	   Statement s = ds.createStatement();
     	   Statement s2 = ds.createStatement();
     	   Statement s3 = ds.createStatement();
     	   
     	   //New Patch - 05/10/2019
     	   String ConditionalCases = "sum(mop.total_units * ipv.liquid_in_ml)/6000";
     	   String ConvertedLabel ="Converted";
     	   
     	  /* String ProductIDs="";
     	   if(PackageID>0) {
     		  ResultSet rs23 = s2.executeQuery("select group_concat(product_id) prod_ids from inventory_products_view where liquid_in_ml="+PackageID+" and category_id=1");
         	  if(rs23.first()) {
         		 ProductIDs = rs23.getString("prod_ids");
         	  } 
         	  
         	  
     	   }   */  	  
     	  
     	  /*String WherePackageIDs ="";
     		
     	  if(ProductIDs.length()>0) {//Have some value, not blank
     		 WherePackageIDs = " and mop.product_id in ("+ProductIDs+")";
     		ConditionalCases = "sum(mop.total_units/ipv.unit_per_sku)";
     		ConvertedLabel = "Raw";
     	  }*/
     		 
     	   
     	   ////////////////////////////////////
     	   
     	   
     	   
           int DayOfMonth = 1;
           ResultSet rs10 = s.executeQuery("select dayofmonth(cast("+Utilities.getSQLDate(YesterdayDate)+" as date))");
           if (rs10.first()){
         	  DayOfMonth = rs10.getInt(1);
           }
     	   
     	   
           String messageWA = "```Secondary Orders ```\n\n`Today`\n";
           
           int AreaID[] = {1,2,3,4};
           
           
           double AllTotalToday = 0;
           for (int i = 0; i < AreaID.length; i++){
	           
        	   String LRBTypeName="";
        	   ResultSet rs3 = s2.executeQuery("select label from inventory_products_lrb_types where id="+AreaID[i]);
        	   if(rs3.first()) {
        		   LRBTypeName = rs3.getString(1);
        	}
        	   
        	   messageWA += "*"+LRBTypeName+"*\n";
           
	           double TotalToday = 0;
	           ResultSet rs1 = s2.executeQuery("select ipv.package_id,ipv.package_label, sum(mop.total_units/ipv.unit_per_sku) from mobile_order mo join mobile_order_products mop on mo.id=mop.id  join inventory_products_view ipv on mop.product_id=ipv.product_id where ipv.lrb_type_id="+AreaID[i]+" and mo.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" group by ipv.package_id");
	           while(rs1.next()){
	        	   
	        	   long RSMID = rs1.getLong(1);
	        	   String RSMName = rs1.getString(2);
	
	               double CasesToday = rs1.getDouble(3);
	              /* ResultSet rs2 = s.executeQuery("select  "+ConditionalCases+" converted_cases from mobile_order mo join mobile_order_products mop on mo.id = mop.id join inventory_products_view ipv on mop.product_id = ipv.product_id where mo.distributor_id in (SELECT distributor_id FROM common_distributors where tdm_id in ("+RSMID+")) and mo.created_on between "+Utilities.getSQLDateLifting(YesterdayDate)+" and "+Utilities.getSQLDateNextLifting(YesterdayDate)+WherePackageIDs);
	               if(rs2.first()){
	             	  CasesToday = rs2.getDouble(1);
	               }*/
	               
	               TotalToday += CasesToday;
	               if (CasesToday != 0){
	            	   messageWA += ">"+RSMName+": "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(CasesToday)+"\n";
	               }
	           }
	           AllTotalToday += TotalToday;
	           //messageWA += "Total: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(TotalToday)+"\n";
           }
           
         // messageWA += "Total Today: "+Utilities.getDisplayCurrencyFormatAbbreviatedOneDecimal(AllTotalToday)+"\n";
          response = messageWA;
          
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
