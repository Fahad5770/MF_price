<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<jsp:useBean id="bean" class="com.pbc.workflow.WorkflowManager" scope="page"/>
<jsp:setProperty name="bean" property="*"/>

<%@page import="com.pbc.util.Utilities"%>

<%

session.removeAttribute("MonthlyDiscountRegions");
session.removeAttribute("MonthlyDiscountDistributors");
session.removeAttribute("MonthlyDiscountOutlets");
session.removeAttribute("MonthlyDiscountMonth");
session.removeAttribute("MonthlyDiscountYear");
session.setAttribute("MonthlyDiscountStatus", 0);

if( request.getParameter("requestID") != null ){
	bean.isLoaded();
}
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


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 95;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long EditID = 0;
boolean isEditCase = false;

if( request.getParameter("RequestID") != null ){
	EditID = Utilities.parseLong(request.getParameter("RequestID"));
	isEditCase = true;
}


%>

<script type="text/javascript">
<!--

//-->

var EditID = '<%=EditID%>';
var isEditCase = false;

if(EditID > 0){
	isEditCase = true;
}

</script>



<%

int Subpage = Utilities.parseInt(request.getParameter("Subpage"));
int ShowFilters = Utilities.parseInt(request.getParameter("ShowFilters"));

if(Subpage==0){ 

%>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script> <% } %>
<script src="js/MonthlyDiscountMain.js"></script>
<% if(Subpage==0){  %>
<div data-role="page" id="MonthlyDiscountMain" data-url="MonthlyDiscountMain" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Monthly Discount" name="title"/>
    </jsp:include>

    
    
        
    <div data-role="content" data-theme="d">
  <% } %> 
    
	    <table style="width: 100%">
	    	<tr>
	    	<% if(ShowFilters == 0) { %>
	    		<td style="width: 20%" valign="top">
	    		
	    			<div id="MainFiltersDiv">
	    		
		    			<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-top:-10px; margin-left:-8px" data-icon="false">
		    
					    	<li data-role="list-divider" data-theme="a">Filter By</li>
					    	
					    	<li data-theme="c"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadRegions()">Region</a></li>
					    	<li data-theme="c"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadDistributors()">Distributors</a></li>
					    	<li data-theme="c"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadOutlets()">Outlet</a></li>
					    	<li data-theme="c"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadPeriod()">Period</a></li>
					    	<li data-theme="e"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadStatus()">Type</a></li>
					    	
					    	<li data-icon="check" >
			     				<a href="#" onClick="LoadMonthlyDiscountMainContent()" data-iconpos="left" data-mini="true" style="font-size:10pt">Generate Discounts</a>
			     			</li>
			     			
			     			<li data-icon="refresh" >
			     				<a href="#" onClick="window.location='MonthlyDiscountMain.jsp'" data-iconpos="left" data-mini="true" style="font-size:10pt">Reset</a>
			     			</li>
					    	
					    </ul>
				    
				    </div>
				    
	    		</td>
	    		<td style="width: 20%" valign="top" >
	    			<form id="MonthlyDiscountMainForm" name="MonthlyDiscountMainForm" action="#" method="POST" data-ajax="false">
	    			<div id="FilterBy">&nbsp;</div>
	    			</form>
	    		</td>
	    		<% } %>
	    		<td valign="top">
	    			
	    			
	    			<form id="SamplingDashboardApprovalForm" name="SamplingDashboardApprovalForm" method="POST" action="sampling/MonthlyDiscountRequestExecute" data-ajax="false">
	    				
	    				<input type="hidden" name="RequestIDVal" id="RequestIDVal" value="<%=EditID%>">
	    				<% if( request.getParameter("requestID") != null ){ %>
	    				<input type="hidden" name="WorkflowStepRemarks" id="WorkflowStepRemarks" value="">
						<input type="hidden" name="StepID" id="StepID" value="<%=bean.CURRENT_STEP.STEP_ID%>">
						<input type="hidden" name="NextStepID" id="NextStepID" value="<%=bean.NEXT_STEP.STEP_ID%>">
						<input type="hidden" name="NextActionID" id="NextActionID" value="<%=bean.NEXT_STEP.ACTION_ID%>">
						<input type="hidden" name="NextUserID" id="NextUserID" value="<%=bean.NEXT_STEP.USER_ID%>">
						<input type="hidden" name="isLastStep" id="isLastStep" value="<%=bean.isLastStep%>">
	    				<% } %>
	    				
	    				
	    				<% if(Subpage==0){  %>
	    					<ul data-role="listview" data-inset="true"  style="font-size:12px; font-weight:normal; margin-top:-10px; margin-left:0px" data-icon="false">
	    				<% }else{ %>
	    					<ul data-role="listview" data-inset="true"  style="font-size:12px; font-weight:normal; margin-left:0px" data-icon="false">
	    				<% } %>
	    
							<li data-role="list-divider" data-theme="a">Monthly Discount List</li>
							
							<li>
	    				
	    						<div id="MonthlyDiscountContent">&nbsp;</div>
	    				
	    				
	    				
	    					</li>
	    					
	    				</ul>
	    				
	    				
	    			</form>
	    		</td>
	    	</tr>
	    </table>
    

	<form id="DashboardDistributorFormID" name="DashboardDistributorFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="DistributorCode" id="DashboardDistributorDistributorCode"/>
    </form>
    
    <form id="DashboardOrderBookerFormID" name="DashboardOrderBookerFormID" action="OrderBookerDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="EmployeeCode" id="DashboardOrderBookerEmployeeCode"/>
    </form>
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackAtDistributorReports" name="CallBack" />
    	<jsp:param value="44" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" >
    	<jsp:param value="EmployeeSearchCallBackAtOrderBookerDashboard" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    
<% if(Subpage==0){ %>
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<!-- <button data-icon="check" data-theme="a" data-inline="true" id="RetursGenerateButton" onClick="GetAllSalesForReturn()">Generate Returns</button>
		 -->
		 <a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="MonthlyDiscountApproveButton" href="#" class="ui-disabled" >Submit</a>
		 <!-- <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="MonthlyDiscountOnHoldButton" href="#" class="ui-disabled" >On Hold</a>
		 <a data-icon="check" data-theme="b" data-role="button" data-inline="true" id="MonthlyDiscountCancelledButton" href="#" class="ui-disabled" >Cancel</a> -->
		 
		 <!-- <button data-icon="check" data-theme="a" data-inline="true" id="MonthlyDiscountApproveButton" class="ui-disabled">Approve</button> 
			<button data-icon="check" data-theme="b" data-inline="true" id="MonthlyDiscountOnHoldButton" class="ui-disabled">On Hold</button>
			<button data-icon="check" data-theme="b" data-inline="true" id="MonthlyDiscountCancelledButton" class="ui-disabled">Cancel</button>
		-->
		 
	</div>    	
    </div>
    <% } %>
    
	<div data-role="popup" id="SamplingDetailPopup" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="max-width:800px;min-width:800px;max-height:600px;" class="ui-corner-all">
		<a href="#SamplingDashboard" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
		    <div data-role="content" data-theme="c" class="ui-corner-bottom ui-content" id="SamplingDetailContent">
				<img src="images/loader.gif">
		    </div>
		</div>
		
<% if(Subpage==0){ %>
</div>
<% } %>

</body>
</html>

<%
if( request.getParameter("requestID") != null ){
	bean.close();
}
%>