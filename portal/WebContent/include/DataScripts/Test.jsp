<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%

Datasource ds = new Datasource();
ds.createConnectionToReplica2();
Connection c = ds.getConnection();
Statement s = c.createStatement();

ResultSet rs = s.executeQuery("SELECT count(distinct package_id) FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products where id in (select id from inventory_sales_adjusted where created_on between '2015-11-01' and '2015-11-10')) order by package_sort_order");
while(rs.next()){
	out.print(rs.getString(1));
}

s.close();
c.close();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html>
