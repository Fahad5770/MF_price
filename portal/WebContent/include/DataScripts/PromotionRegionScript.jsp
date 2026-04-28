<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>

<%
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

/*
ResultSet rs = s.executeQuery("SELECT * FROM inventory_sales_promotions_request_regions where region_id=5");
while(rs.next()){
	s2.executeUpdate("INSERT INTO `inventory_sales_promotions_request_regions`(`product_promotion_id`,`region_id`)VALUES("+rs.getString("product_promotion_id")+", 11)");
	out.print("done<br>");
}

ResultSet rs2 = s.executeQuery("SELECT * FROM inventory_sales_promotions_request_regions where region_id=1");
while(rs2.next()){
	s2.executeUpdate("INSERT INTO `inventory_sales_promotions_request_regions`(`product_promotion_id`,`region_id`)VALUES("+rs2.getString("product_promotion_id")+", 10)");
	out.print("done<br>");
}
*/

s2.close();
s.close();
c.close();
ds.dropConnection();
%>
        	