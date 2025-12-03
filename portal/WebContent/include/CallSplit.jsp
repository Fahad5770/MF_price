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
SalesPosting.splitOrder(5);
%>

	       							