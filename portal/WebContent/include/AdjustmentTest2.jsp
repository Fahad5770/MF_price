<%@page import="java.util.Date"%>
<%@page import="com.pbc.outlet.OutletDashboard"%>
<%@page import="com.pbc.inventory.PromotionItem"%>
<%@page import="com.pbc.inventory.Product"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="com.pbc.inventory.SalesPosting"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<%

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();

ResultSet rs = s.executeQuery("select * from mobile_order_products2 order by id");
while(rs.next()){
	out.println("replace into 	mobile_order_products values ("+rs.getString("id")+","+rs.getString("product_id")+","+rs.getString("raw_cases")+","+rs.getString("units")+","+rs.getString("total_units")+","+rs.getString("liquid_in_ml")+","+rs.getString("rate_raw_cases")+","+rs.getString("rate_units")+","+rs.getString("amount_raw_cases")+","+rs.getString("amount_units")+","+rs.getString("total_amount")+","+rs.getString("discount")+","+rs.getString("net_amount")+","+rs.getString("is_promotion")+","+rs.getString("promotion_id")+") <br>");
	
	try{
	s2.executeUpdate("insert into mobile_order_products values ("+rs.getString("id")+","+rs.getString("product_id")+","+rs.getString("raw_cases")+","+rs.getString("units")+","+rs.getString("total_units")+","+rs.getString("liquid_in_ml")+","+rs.getString("rate_raw_cases")+","+rs.getString("rate_units")+","+rs.getString("amount_raw_cases")+","+rs.getString("amount_units")+","+rs.getString("total_amount")+","+rs.getString("discount")+","+rs.getString("net_amount")+","+rs.getString("is_promotion")+","+rs.getString("promotion_id")+")");
	}catch(Exception e){out.println(e+ "<br>");}
}

ds.dropConnection();
%>

	       							