<%@page import="com.pbc.util.*"%>
<%@page import="java.sql.*"%>

<% 
Datasource ds = new Datasource();

ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
/*
ResultSet rs = s.executeQuery("select id, label from inventory_packages");
while(rs.next()){

	s2.executeUpdate("update inventory_products set package_id = "+rs.getInt(1)+" where package_id = '"+rs.getString(2)+"'");

}

ResultSet rs = s.executeQuery("select id, label from inventory_categories");
while(rs.next()){

	s2.executeUpdate("update inventory_products set category_id = "+rs.getInt(1)+" where category_id = '"+rs.getString(2)+"'");

}

ResultSet rs = s.executeQuery("select id, label from inventory_brands");
while(rs.next()){

	s2.executeUpdate("update inventory_products set brand_id = "+rs.getInt(1)+" where brand_id = '"+rs.getString(2)+"'");

}
*/


out.print("done");

s2.close();
s.close();

ds.dropConnection();

%>