<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLConnection" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.pbc.util.Datasource" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html>
<%
Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();

ResultSet rs = s.executeQuery("SELECT md.mobile_no FROM pep.mobile_devices md join users u on md.issued_to = u.id where designation in ('Customer Representative','Pre Seller') and mobile_no != ''");
while(rs.next()){

String SMSNumber = rs.getString(1);

if (SMSNumber != null && SMSNumber.length() > 2){
	SMSNumber = "92"+SMSNumber.substring(1,SMSNumber.length());
	//SMSNumber = "923008406444";
	
	out.print(SMSNumber+"<br>");
	
	//String message = "We have announced special daily incentive for PRE-Sellers on productivity. Please get details from your supervisors.\n\nBE PRODUCTIVE ON DAILY BASIS & EARN ON DAILY BASIS";
	
	String message = "Dear Preseller,\nPlease focus on orders of 500ml pepsi diet for next three days.\nRegards,\nSales Management";
		
	
	URL url = new URL("http://155.135.0.70/default.aspx?number="+SMSNumber+"&msg="+URLEncoder.encode(message));
	URLConnection urlConnection = url.openConnection();
	
	HttpURLConnection connection = null;
	if(urlConnection instanceof HttpURLConnection){
	   connection = (HttpURLConnection) urlConnection;
	}else{
	   System.out.println("Utilities.sendSMS: Invalid URL");
	}
	BufferedReader in = new BufferedReader(
	new InputStreamReader(connection.getInputStream()));
	String urlString = "";
	String current;
	while((current = in.readLine()) != null)
	{
	   urlString += current;
	}
	
	if (urlString != null && urlString.indexOf("true") == -1){

	}
	//break;
	
	Thread.currentThread().sleep(2000);
	
}
/*
String message = "We have announced special daily incentive for PRE-Sellers on productivity. Please get details from your supervisors.\n\nBE PRODUCTIVE ON DAILY BASIS & EARN ON DAILY BASIS";

URL url = new URL("http://155.135.0.70/default.aspx?number="+SMSNumber+"&msg="+URLEncoder.encode(message));
URLConnection urlConnection = url.openConnection();

HttpURLConnection connection = null;
if(urlConnection instanceof HttpURLConnection){
   connection = (HttpURLConnection) urlConnection;
}else{
   System.out.println("Utilities.sendSMS: Invalid URL");
}
BufferedReader in = new BufferedReader(
new InputStreamReader(connection.getInputStream()));
String urlString = "";
String current;
while((current = in.readLine()) != null)
{
   urlString += current;
}

if (urlString != null && urlString.indexOf("true") == -1){

}
*/

 //break;
}

%>