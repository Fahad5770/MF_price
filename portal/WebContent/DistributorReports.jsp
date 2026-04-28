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

boolean noFilters = false;

String HeaderType = Utilities.filterString(request.getParameter("HeaderType"),1,100);
if (HeaderType == null){
	HeaderType = "Standard";
}
/*
DistribtuorDashboard
OrderBookerDashboard
Standard
*/

String DistributorName = Utilities.filterString(request.getParameter("DistributorName"), 1, 100);
long DistributorCode = Utilities.parseLong(request.getParameter("DistributorCode"));
int PageIndex = Utilities.parseInt(request.getParameter("PageIndex"));

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

int FeatureID = 0;
String ReportURL="";
String ReportTitle="";
String DefaultFilter = "LoadDateRange(\'LoadDateRangeHyperlink\')";

int ReportID = Utilities.parseInt(request.getParameter("ReportID"));
//System.out.println(ReportID);
if(ReportID==0)
{
	
}
else if (ReportID == 1)
{
	FeatureID=67;
	ReportURL="DistributorReportsActivitySummary.jsp";
	ReportTitle = "Activity Summary";
}
else if (ReportID == 2)
{
	FeatureID=68;
	ReportURL="DistributorReportsStockSummary.jsp";
	ReportTitle = "Stock Summary";

}else if (ReportID == 3){
	
	FeatureID=69;
	ReportURL="DistributorReportsOrderActivity.jsp";
	ReportTitle = "Sales Activity";
}else if (ReportID == 4){
	
	FeatureID=70;
	ReportURL="DistributorReportsDispatchSummary.jsp";
	ReportTitle = "Dispatch Summary";

}else if (ReportID == 5){
	
	FeatureID=71;
	ReportURL="DistributorReportsOutletList.jsp";
	ReportTitle = "Outlet List";
	DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
}else if (ReportID == 6){
	
	FeatureID=69;
	ReportURL="DistributorReportsOrderActivity.jsp";
	ReportTitle = "Sales Activity";
	
	//DefaultFilter = "LoadAllOrderBookers(\'LoadAllOrderBookersHyperlink\')";
}else if (ReportID == 7){
	
	FeatureID=69;
	ReportURL="DistributorReportsOrderBookerOutletList.jsp";
	ReportTitle = "Sales Activity";
	
	DefaultFilter = "LoadAllDistributors(\'LoadAllDistributorsHyperlink\')";
	//noFilters = true;
}else if (ReportID == 8){
	
	FeatureID=41;
	ReportURL="DistributorReportsOrderBookerOrderList.jsp";
	ReportTitle = "Orders Summary";
	
}else if (ReportID == 9){
	
	FeatureID=41;
	ReportURL="DistributorReportsOrderBookerOrderList.jsp";
	ReportTitle = "Orders Summary";
	
}else if (ReportID == 10){
	
	FeatureID=86;
	ReportURL="DistributorReportsAdjustedSalesSummary.jsp";
	ReportTitle = "Adjusted Sales Summary";
	
}else if (ReportID == 11){
	
	FeatureID=37; //lifting report feature id
	ReportURL="DistributorReportsLifting.jsp";
	ReportTitle = "Lifting Report";
	
}else if (ReportID == 12){
	
	FeatureID=90; //lifting report feature id
	ReportURL="DistributorReportsReceiptFromProduction.jsp";
	ReportTitle = "Production Report";
	
}else if (ReportID == 13){
	
	FeatureID=92; //lifting report feature id
	ReportURL="DistributorReportsReceiptFromProductionGRN.jsp";
	ReportTitle = "Production GRN Report";
	
}else if (ReportID == 14){
	
	FeatureID=96; 
	ReportURL="DistributorReportsConvertedSales.jsp";
	ReportTitle = "Converted Sales";
	
}

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


session.removeAttribute("SR1SelectedPackages");
session.removeAttribute("SR1SelectedBrands");
session.removeAttribute("SR1SelectedDistributors");
session.removeAttribute("SR1SelectedOrderBookers");
session.removeAttribute("SR1SelectedVehicles");
session.removeAttribute("SR1SelectedEmployees");
session.removeAttribute("SR1SelectedOutlets");
session.removeAttribute("SR1StartDate");
session.removeAttribute("SR1EndDate");
session.removeAttribute("SR1DateType");
session.setAttribute( "SR1StartDate", new java.util.Date() );
session.setAttribute( "SR1EndDate", new java.util.Date() );
session.setAttribute( "SR1FeatureID",FeatureID );
session.removeAttribute("SR1SelectedPJP");
session.removeAttribute("SR1SelectedHOD");




%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/DistributorReports.js"></script>



<div data-role="page" id="SalesSummaryMain" data-url="SalesSummaryMain" data-theme="d">
<%
//if (isDashboard == false){

if (HeaderType.equals("Standard")){
%>
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="<%=ReportTitle%>" name="title"/>
    </jsp:include>
<%
}else if (HeaderType.equals("DistributorDashboard")){
	//System.out.println("Hello "+DistributorCode);
	
	long DistributorID[] = {DistributorCode};
	session.setAttribute( "SR1SelectedDistributors", DistributorID);
	String ReportTitleToShow = Utilities.filterString(request.getParameter("ReportTitleQuery"), 1, 100);
	//System.out.println(DistributorName);
	%>
    <jsp:include page="DistributorDashboardHeader.jsp" >
    	<jsp:param value="<%=DistributorName%>" name="DistributorNameToShow"/>
    	<jsp:param value="<%=DistributorCode%>" name="DistributorCode"/>
    	<jsp:param value="<%=PageIndex%>" name="PageIndex"/>
    	<jsp:param value="<%=ReportTitleToShow%>" name="ReportTitleToShow"/>
    </jsp:include>
<%
}else if (HeaderType.equals("OrderBookerDashboard")){
	String EmployeeName = Utilities.filterString(request.getParameter("EmployeeName"),1,30);
	long EmployeeCode = Utilities.parseLong(request.getParameter("EmployeeCode"));
	long EmployeeCodeArray[] = {EmployeeCode};
	session.setAttribute( "SR1SelectedOrderBookers", EmployeeCodeArray);

%>
    <jsp:include page="OrderBookerDashboardHeader.jsp" >
    	<jsp:param value="<%=EmployeeName%>" name="title"/>
    	<jsp:param value="<%=PageIndex%>" name="tab"/>
    	<jsp:param value="<%=EmployeeCode%>" name="EmployeeCode"/>
    </jsp:include>
<%
}else if(HeaderType.equals("OutletDashboard")){
	//System.out.println(request.getParameter("OutletIDDD"));
	
	long OutletDashboardOultetID = Utilities.parseLong(request.getParameter("OutletDashboardOutletID"));
	//System.out.println(OutletDashboardOultetID);
	
	String OutletDashboardOutletName = Utilities.filterString(request.getParameter("OutletDashboardOutletName"),1,30);
	long OultetID1Array[] = {OutletDashboardOultetID};
	session.setAttribute( "SR1SelectedOutlets", OultetID1Array);	
%>
	<jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=OutletDashboardOutletName%>" name="title"/>
    	<jsp:param value="<%=PageIndex%>" name="tab"/>
    	<jsp:param value="<%=OutletDashboardOultetID%>" name="OutletID"/>
    </jsp:include>
<%} %>
    
    
        
    <div data-role="content" data-theme="d">
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackAtDistributorReports" name="CallBack" />
    	<jsp:param value="44" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackAtOrderBookerDashboard" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    
    
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
		<td style="width:15%;<%if (noFilters == true){ %>display: none;<%} %>" valign="top" >
		<input type="hidden" name="IsPackageSelectedOnLoad" id="IsPackageSelectedOnLoad" value="<%=IsPackageSelectedOnLoad%>"/>
		
		<div id="LoadSalesSummaryFilterBy">
		
		</div>
			
		</td>
		<td style="width:17%; <%if (noFilters == true){ %>display: none;<%} %>" valign="top">
		
			<form id="SalesSummaryMainForm" data-ajax="false">
				<input type="hidden" name="FilterType" id="FilterType" value=""/>
				<input type="hidden" name="ReportURL" id="ReportURL" value="<%=ReportURL%>"/>
				<input type="hidden" name="ReportID" id="ReportID" value="<%=ReportID%>"/>
				<input type="hidden" name="DefaultFilter" id="DefaultFilter" value="<%=DefaultFilter%>"/>
				
				
				<div id="LoadAllSearchResultsDIV">
				
				</div>
			</form>
		</td>
		<td style="width:68%" valign="top" id="ReportSaleSummaryTD"></td>
	</tr>
		
	</table>


	<form id="DashboardDistributorFormID" name="DashboardDistributorFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="DistributorCode" id="DashboardDistributorDistributorCode"/>
    </form>
    
    <form id="DashboardOrderBookerFormID" name="DashboardOrderBookerFormID" action="OrderBookerDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="EmployeeCode" id="DashboardOrderBookerEmployeeCode"/>
    </form>

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

