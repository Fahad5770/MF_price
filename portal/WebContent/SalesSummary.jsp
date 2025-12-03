<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>





<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/SalesSummary.js"></script>





<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 59;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 59);
%>
<% 
String DistributorName = "";
long DistributorID = 0;


if( UserDistributor != null ){
	if(UserDistributor.length>1) //if it has more than 1 distributor
	{
		response.sendRedirect("AccessDenied.jsp");
	}
	else
	{
		DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
		DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
	}
}

%>


<div data-role="page" id="SalesSummaryMain" data-url="SalesSummaryMain" data-theme="d">

    
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Sales Summary" name="title"/>
    </jsp:include>

    
    <div data-role="content" data-theme="d">

	<table border="0" style="width:100%;">
<%
boolean IsPackageSelected=false;
int IsPackageSelectedOnLoad=0;
long SelectedPackagesArray[] = null;
if (session.getAttribute("SR1SelectedPackages") != null){
	SelectedPackagesArray = (long[])session.getAttribute("SR1SelectedPackages");
	if(SelectedPackagesArray.length>0)
	{
		IsPackageSelected = true;
		IsPackageSelectedOnLoad=1;
	}
}

%>      
	<tr>
		<td style="width:20%" valign="top">
		<input type="hidden" name="IsPackageSelectedOnLoad" id="IsPackageSelectedOnLoad" value="<%=IsPackageSelectedOnLoad%>"/>
		
		<div id="LoadSalesSummaryFilterBy">
		
		</div>
			
		</td>
		<td style="width:20%" valign="top">
			<form id="SalesSummaryMainForm" data-ajax="false">
				<input type="hidden" name="FilterType" id="FilterType" value=""/>
				<div id="LoadAllSearchResultsDIV">
				
				</div>
			</form>
		</td>
		<td style="width:60%" valign="top" id="ReportSaleSummaryTD">&nbsp;</td>
	</tr>
		
	</table>


	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<!-- <button data-icon="check" data-theme="a" data-inline="true" id="RetursGenerateButton" onClick="GetAllSalesForReturn()">Generate Returns</button>
		 -->
	</div>    	
    </div>
	

</div>

</body>
</html>



<%
s.close();
ds.dropConnection();
%>