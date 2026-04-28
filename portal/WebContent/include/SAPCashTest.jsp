<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
com.pbc.sap.SAPUtilities su = new com.pbc.sap.SAPUtilities();

try{
	
	su.connectDEV();
	
	//su.setCashSummary(new java.util.Date(), 123, 234, 345);
	
	su.dropConnection();
	
}catch(Exception e){
	out.print(e+"<br>");
	e.printStackTrace();
}

out.close();
%>
</body>
</html>