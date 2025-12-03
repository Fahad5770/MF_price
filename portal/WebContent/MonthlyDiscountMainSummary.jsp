<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%

long SelectedRegions[] = {0};

if (session.getAttribute("MonthlyDiscountRegions") != null){
	SelectedRegions = (long[])session.getAttribute("MonthlyDiscountRegions");
	
	
	
	for(int i = 0; i < SelectedRegions.length; i++ ){
		out.print(SelectedRegions[i]+"<br>");
	}
	
}
out.print("<br><br><br><br><br>");
if (session.getAttribute("MonthlyDiscountDistributors") != null){
	SelectedRegions = (long[])session.getAttribute("MonthlyDiscountDistributors");
	
	
	
	for(int i = 0; i < SelectedRegions.length; i++ ){
		out.print(SelectedRegions[i]+"<br>");
	}
	
}

out.print("<br><br><br><br><br>");
if (session.getAttribute("MonthlyDiscountOutlets") != null){
	SelectedRegions = (long[])session.getAttribute("MonthlyDiscountOutlets");
	
	
	
	for(int i = 0; i < SelectedRegions.length; i++ ){
		out.print(SelectedRegions[i]+"<br>");
	}
	
}



%>


