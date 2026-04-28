<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.pbc.util.Datasource"%>
<%@ page import="com.pbc.util.Utilities"%>
<%@ page import="java.sql.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
Datasource ds = new Datasource();
ds.createConnectionToCISKSML();
Statement s = ds.createStatement();

ResultSet rs = s.executeQuery("select TokenNo from dbo.token");
while(rs.next()){
	out.print(rs.getString(1)+"<br>");
}

ds.dropConnection();
%>
</body>
</html>