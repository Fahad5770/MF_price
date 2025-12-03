<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.pbc.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.sampling.SamplingPosting"%>

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
Utilities.sendPBCEmail(new String[]{"shahrukh.salman@pbc.com.pk","anas.wahab@pbc.com.pk"}, null, null, "Invoice Alert: Customer ID: Order# Amount:", "Customer ID: Order# Amount:", null);

//Utilities.sendSMS("923334566993", "Customer ID: Order# Amount:");

try{
	//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	/*
	Connection oc = DriverManager.getConnection("jdbc:odbc:canedsn","","");
	Statement os = oc.createStatement();
	Statement os2 = oc.createStatement();


	ResultSet rs = os.executeQuery("select sum(field1), sum(field2) from canedata");
	while(rs.next()){
		out.println(rs.getString(1)+"<br>");
	}
	*/
}catch (Exception e){
	e.printStackTrace();

}

%>