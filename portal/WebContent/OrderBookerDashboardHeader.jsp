<%@page import="com.pbc.util.Utilities"%>
<%
String title = request.getParameter("title");
int tab = Utilities.parseInt(request.getParameter("tab"));
int EmployeeCode = Utilities.parseInt(request.getParameter("EmployeeCode"));
String ReportTitle=Utilities.filterString(request.getParameter("DisDashHeadReportTitle"), 1, 100);

%>
<script>



function OrderBookerDashboardRedirectPerformanceTab(EmpCode,TabID,DisDashHeadReportTitle,RptID){
	if(TabID==5){//performance		
		document.getElementById("OrderBookerDashboardHeaderFormForPerformanceHiddenField").value = EmpCode;	
		document.getElementById("DisDashHeadReportTitle").value = DisDashHeadReportTitle;	
		document.getElementById("OrderBookerDashboardHeaderFormForPerformance").submit();
		
	}else if(TabID==1){//for overview		
		document.getElementById("OrderBookerDashboardHeaderFormForPerformanceHiddenField").value = EmpCode;	
		document.getElementById("DisDashHeadReportTitle").value = DisDashHeadReportTitle;
		document.getElementById("OrderBookerDashboardHeaderFormForPerformance").action="OrderBookerDashboardOverview.jsp";
		document.getElementById("OrderBookerDashboardHeaderFormForPerformance").submit();
		
	}else{
		
		document.getElementById("OrderBookerDashboardHeaderFormReportID").value = RptID;
		document.getElementById("OrderBookerDashboardHeaderFormPageIndex").value = TabID;
		document.getElementById("DisDashHeadReportTitle").value = DisDashHeadReportTitle;
		document.getElementById("OrderBookerDashboardHeaderFormForPerformance").action="DistributorReports.jsp";
		document.getElementById("OrderBookerDashboardHeaderFormForPerformance").submit();
	}
	
	
	
}

</script>
<div data-role="header" data-id="BeatPlanHeader" data-theme="d">
    <!--  <h1><%=EmployeeCode%> - <%=title%></h1>
    <a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
    <a href="javascript:changeEmployeeForDashboard()" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back" >Change Order Booker</a>
    <div data-role="navbar" >
        <ul>
            <li><a onclick="OrderBookerDashboardRedirectPerformanceTab(<%=EmployeeCode%>,1)" data-ajax="false" data-transition="none" <%if (tab == 1){%>class="ui-btn-active ui-state-persist"<%} %> >Overview</a></li>
			<li><a onclick="OrderBookerDashboardRedirect(7, 2)" href="#" data-ajax="false" data-transition="none" <%if (tab == 2){%>class="ui-btn-active ui-state-persist"<%} %> >Outlets</a></li>
			<li><a onclick="OrderBookerDashboardRedirect(6, 3)" href="#" data-ajax="false" data-transition="none" <%if (tab == 3){%>class="ui-btn-active ui-state-persist"<%} %> >Sales</a></li>
			<li><a onclick="OrderBookerDashboardRedirect(8, 4)" href="#" data-ajax="false" data-transition="none" <%if (tab == 4){%>class="ui-btn-active ui-state-persist"<%} %> >Orders</a></li>
			<li><a onclick="OrderBookerDashboardRedirectPerformanceTab(<%=EmployeeCode%>,5)" data-ajax="false" data-transition="none"  <%if (tab == 5){%>class="ui-btn-active ui-state-persist"<%} %> >Performance</a></li>
        </ul>
    </div><!-- /navbar -->
      <div style="width:100%; min-height:50px; b1ackground-color:red;">
    	<div style="width:35%; height:50px; float:left; b1ackground-color:green">
    		<a href="home.jsp" data-role="button" data-inline="true" data-ajax="false" data-icon="back">Back</a> 
    	</div>
    	<div style="width:30%; height:50px; float:left; b1ackground-color:yellow">
    		<h1 style="font-size:16px; text-align:center;"><%if(ReportTitle!=null){%><%=ReportTitle%><%}else{ %>Overview<%} %></h1>
    	</div>
    	
    	<div style="width:35%; height:50px; float:left; b1ackground-color:blue;float:left;">
    		<div style="width:350px; float:right;">
    			<div data-role="controlgroup" data-type="horizontal" style="float:right; margin-right: 5px;">
			    	<a href="#" id="ChangeOrderBooker" data-role="button" ><%=EmployeeCode%> - <%=Utilities.truncateStringToMax(title,20)%></a>
			    	<a href="#distdashboardmenupanel"  data-role="button" data-inline="true" data-mini="true" data-icon="bars" data-theme="a"  adata-iconpos="notext">Menu</a>
				</div>
    		</div>
    		
    	</div>
   
   </div>
   <div data-role="panel" id="distdashboardmenupanel" data-display="overlay" data-position="right" data-position-fixed="true" data-theme="d" >
    <ul data-role="listview" data-theme="d" class="nav-search" data-filter="true" data-filter-placeholder="Search..." data-filter-theme="d">
     	<li><a onclick="OrderBookerDashboardRedirectPerformanceTab(<%=EmployeeCode%>,1,'Overview','')" data-ajax="false" data-transition="none"  >Overview</a></li>
		<li><a onclick="OrderBookerDashboardRedirectPerformanceTab('',2,'Outlets',7)" href="#" data-ajax="false" data-transition="none">Outlets</a></li>
		<li><a onclick="OrderBookerDashboardRedirectPerformanceTab('',3,'Sales',6)" href="#" data-ajax="false" data-transition="none" >Sales</a></li>
		<li><a onclick="OrderBookerDashboardRedirectPerformanceTab('',4,'Orders',8)" href="#" data-ajax="false" data-transition="none" >Orders</a></li>
		<li><a onclick="OrderBookerDashboardRedirectPerformanceTab(<%=EmployeeCode%>,5,'Performance')" data-ajax="false" data-transition="none"   >Performance</a></li>
    
    </ul>
    </div>
    
    
    
</div><!-- /header -->

<form data-ajax="false" name="OrderBookerDashboardHeaderFormForPerformance" id="OrderBookerDashboardHeaderFormForPerformance" action="DistributorReportsOrderBookerPerformance.jsp" method="post" style="display:none">
<input type="hidden" name="ReportID" id="OrderBookerDashboardHeaderFormReportID" value="" />
	<input type="hidden" name="HeaderType" id="OrderBookerDashboardHeaderFormHeaderType" value="OrderBookerDashboard" />
	<input type="hidden" name="EmployeeName" id="OrderBookerDashboardHeaderFormEmployeeName" value="<%=title%>" />
	<input type="hidden" name="EmployeeCode" id="OrderBookerDashboardHeaderFormEmployeeCode" value="<%=EmployeeCode%>" />
	<input type="hidden" name="PageIndex" id="OrderBookerDashboardHeaderFormPageIndex" value="" />
	<input type="hidden" name="DisDashHeadReportTitle1" id="DisDashHeadReportTitle1"/>
	<input type="hidden" name="EmployeeCode" id="OrderBookerDashboardHeaderFormForPerformanceHiddenField" value="" />
	<input type="hidden" name="DisDashHeadReportTitle" id="DisDashHeadReportTitle"/>

</form>

