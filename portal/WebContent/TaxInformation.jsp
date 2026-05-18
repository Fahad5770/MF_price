<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>


<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>


<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(510, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s1 = ds.createStatement();


%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
       
       <script src="js/TaxInformation.js?<%=Math.random() %>=<%=Math.random() %>"></script>

</head>

<body>

<div data-role="page" id="TaxInformation" data-url="TaxInformation" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Tax Information" name="title"/>

    		

    </jsp:include>
    
    <div data-role="content" data-theme="d">
	

	
	<form id="TaxInformation" data-ajax="false">	
	<ul data-role="listview" data-inset="false" data-divider-theme="c">
	<li>
	<input type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>"/>
	<input type="hidden" name="FeatureIDFOrWhole" id="FeatureIDFOrWhole" value="510"/>
	
	
	
	</li>
	 
    <li data-role="list-divider">Tax Details</li>
    <li>	
		
    
    <table border="0" width="100%">
		<tr>	
		    <td style="width: 10%;"></td>
			<td style="text-align:center; width:19%">
				Filer and Registered
			</td>
			<td style="width: 19%;text-align:center;">
			    Filer and Unregistered
			</td>
			<td style="width: 19%;text-align:center;">
				Non-filer and Registered
			</td>
			<td style="width: 19%; text-align:center;">
				Non-Filer and Unregistered
			</td>    
			<td style="width: 14%; text-align:center;">
				Action
			</td>    
		</tr>  
		<%
		
		
		double salesFR = 0;
		double salesFUR=0;
		double salesNFR=0;
		double salesNFUR=0;
		
		double incomeFR = 0;
		double incomeFUR=0;
		double incomeNFR=0;
		double incomeNFUR=0;
		
		ResultSet rs = s.executeQuery("Select * from inventory_sales_tax Where isActive=1");
		
		while(rs.next()){
			salesFR=rs.getDouble("FR");
			salesFUR=rs.getDouble("FUR");
			salesNFR=rs.getDouble("NFR");
			salesNFUR=rs.getDouble("NFUR");
		}
        
		ResultSet rs2= s1.executeQuery("Select * from inventory_income_tax Where isActive=1");
		while(rs2.next()){
			incomeFR=rs2.getDouble("FR");
			incomeFUR=rs2.getDouble("FUR");
			incomeNFR=rs2.getDouble("NFR");
			incomeNFUR=rs2.getDouble("NFUR");
		}
		%>
					<tr>
					       <td  style="width:8%">
					         Sales tax
							</td>
							<td valign="top" style="width:14%">
								<input type="text" placeholder="" id="SalesFR" name="SalesFR" value="<%=salesFR%>" data-mini="true">		
							</td>
							<td valign="top" style="width:14%">	
							<input  type="text" placeholder="" id="SalesFUR" name="SalesFUR" value="<%=salesFUR%>" data-mini="true">		
							</td>
							<td valign="top" style="width:14%">
							<input  type="text" placeholder="" id="SalesNFR" name="SalesNFR" value="<%=salesNFR%>" data-mini="true">
							</td>
							<td valign="top" style="width:16%">
								<input  type="text" placeholder="" id="SalesNFUR" name="SalesNFUR" value="<%=salesNFUR%>" data-mini="true">
							</td>	
							<td valign="top" style="width:12%">
								<input type="button" id="SalesTaxSubmit" value="Submit" data-mini="true">
							</td>	
					</tr>
					<tr>
					       <td  style="width:8%">
					         Income Tax
							</td>
							<td valign="top" style="width:14%">
								<input  type="text" placeholder="" id="IncomeFR" name="IncomeFR" value="<%=incomeFR%>" data-mini="true">		
							</td>
							<td valign="top" style="width:14%">	
							<input  type="text" placeholder="" id="IncomeFUR" name="IncomeFUR" value="<%=incomeFUR%>" data-mini="true">		
							</td>
							<td valign="top" style="width:14%">
							<input  type="text" placeholder="" id="IncomeNFR" name="IncomeNFR" value="<%=incomeNFR%>" data-mini="true">
							</td>
							<td valign="top" style="width:16%">
								<input  type="text" placeholder="" id="IncomeNFUR" name="IncomeNFUR" value="<%=incomeNFUR%>" data-mini="true">
							</td>	
							<td valign="top" style="width:12%">
								<input type="button" id="IncomeTaxSubmit" value="Submit" data-mini="true">
							</td>	
					</tr>			
			
	</table>	
	</li>
	</ul>

	</form>

 
</div>

</body>
</html>
<%
s.close();
ds.dropConnection();


bean.close();
%>
