<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@page import="com.pbc.util.Datasource"%>
<%
Class.forName("com.mysql.jdbc.Driver").newInstance();
Connection r_c = DriverManager.getConnection("jdbc:mysql://ep.pbc.com.pk/pbc","admin","lion@pbc");
Statement r_s = r_c.createStatement();

Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();

/*
ResultSet r_rs = r_s.executeQuery("select outletid, gps_n lat, gps_e lng from pcioutletsverification where outletid != \"\" and outletid != 0 and gps_e != 0 and gps_n != 0");
while(r_rs.next()){
	
	long OutletID = r_rs.getLong(1);
	double lat = r_rs.getDouble(2);
	double lng = r_rs.getDouble(3);
	
	out.print(OutletID + " " +lat + " "+lng + "<br>");
	
	s.executeUpdate("update common_outlets set lat = "+lat+", lng = "+lng+" where id =" +OutletID);
	
}
*/
ResultSet r_rs = r_s.executeQuery("SELECT user_outlet_id, latitude, longitude FROM pbc.outlets where user_outlet_id != 0 and latitude !=0 and longitude != 0");
while(r_rs.next()){
	
	long OutletID = r_rs.getLong(1);
	double lat = r_rs.getDouble(2);
	double lng = r_rs.getDouble(3);
	
	out.print(OutletID + " " +lat + " "+lng + "<br>");
	
	s.executeUpdate("update common_outlets set lat = "+lat+", lng = "+lng+" where id =" +OutletID);
	
}





ds.dropConnection();
r_c.close();

%>