

<%@page import="com.pbc.util.Utilities"%>
<%
String title = request.getParameter("title");
int tab = Utilities.parseInt(request.getParameter("tab"));
int OutletID = Utilities.parseInt(request.getParameter("OutletID"));
String OutletDashboardHeaderReportTitle=Utilities.filterString(request.getParameter("ReportTitle"), 1, 100);
//System.out.println("Header OutletID "+OutletID);
%>
<script>
function OutletDashboardRedirect(flag,OutletID,RptTitle){
	if(flag==1){//mean summary page
		//alert(OutletID);
		$("#OutletIDForAllOtherPages").val(OutletID);
		$("#ReportTitle").val(RptTitle);
		document.OutletDashboardHeaderForm.action = "OutletDashboard.jsp";
	}
	//document.getElementById("OutletDashboardHeaderFormReportID").value = ReportID;
	//document.getElementById("OutletDashboardHeaderFormPageIndex").value = PageIndex;
	document.getElementById("OutletDashboardHeaderForm").submit();
	//alert("form sumbitted");
}


</script>

<div data-role="header" data-id="OutletHeader" data-theme="d">
    <!-- <h1><%=OutletID+" - "+title %></h1>
    <a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
    <a href="javascript:changeOutletForDashboard()" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Change Outlet</a>
    
  <div data-role="navbar" >
        <ul>
            <li><a href="#OutletDashboardSummary"  data-transition="none" <%if (tab == 1){%>class="ui-btn-active ui-state-persist"<%} %>>Summary</a></li>
			<li><a href="OutletDashboardLedger.jsp?outletID=<%=OutletID%>" data-transition="none" <%if (tab == 2){%>class="ui-btn-active ui-state-persist"<%} %>>Ledger</a></li>	            
			<li><a href="OutletDashboardSales.jsp?outletID=<%=OutletID%>" data-transition="none" <%if (tab == 3){%>class="ui-btn-active ui-state-persist"<%} %>>Sales</a></li>
            <li><a href="OutletDashboardDiscount.jsp?outletID=<%=OutletID%>" data-transition="none" <%if (tab == 4){%>class="ui-btn-active ui-state-persist"<%} %>>Discount</a></li>
			<li><a href="DistributorReports.jsp?ReportID=9&HeaderType=OutletDashboard&PageIndex=5&OutletDashboardOutletID=<%=OutletID %>&OutletDashboardOutletName=<%=title %>" data-transition="none" <%if (tab == 5){%>class="ui-btn-active ui-state-persist"<%} %>>Orders</a></li>
        </ul>
    </div><!-- /navbar -->
    
    
   <form data-ajax="false" name="OutletDashboardHeaderForm" id="OutletDashboardHeaderForm" action="" method="post" style="display:none">
	
	<input type="hidden" name="ReportID" id="OutletDashboardHeaderFormReportID" value="" />	
	<input type="hidden" name="HeaderType" id="OrderBookerDashboardHeaderFormHeaderType" value="OutletDashboard" />
	<input type="hidden" name="PageIndex" id="OutletDashboardHeaderFormPageIndex" value="" />
	<input type="hidden" name="OutletDashboardOutletID" id="OutletDashboardOutletID" value="<%=OutletID %>" />
	<input type="hidden" name="OutletDashboardOutletName" id="OutletDashboardOutletName" value="<%=title %>" />
	<input type="hidden" name="outletID" id="OutletIDForAllOtherPages"/>
	<input type="hidden" name="ReportTitle" id="ReportTitle"/>
	
  </form> 
  
  <div style="width:100%; min-height:50px; b1ackground-color:red;">
    	<div style="width:35%; height:50px; float:left; b1ackground-color:green">
    		<a href="home.jsp" data-role="button" data-inline="true" data-ajax="false" data-icon="back">Back</a> 
    	</div>
    	<div style="width:30%; height:50px; float:left; b1ackground-color:yellow">
    		<h1 style="font-size:16px; text-align:center;"><%if(OutletDashboardHeaderReportTitle != null){%><%=OutletDashboardHeaderReportTitle %><%}else{ %>Summary<%} %></h1>
    	</div>
    	
    	<div style="width:35%; height:50px; float:left; b1ackground-color:blue;float:left;">
    		<div style="width:350px; float:right;">
    			<div data-role="controlgroup" data-type="horizontal" style="float:right; margin-right: 5px;">
			    	<a href="javascript:changeOutletForDashboard()" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><%=OutletID+" - "+Utilities.truncateStringToMax(title,20)%></a>
			    	<a href="#distdashboardmenupanel"  data-role="button" data-inline="true" data-mini="true" data-icon="bars" data-theme="a"  adata-iconpos="notext">Menu</a>
				</div>
    		</div>
    		
    	</div>
   
   </div>
  <div data-role="panel" id="distdashboardmenupanel" data-display="overlay" data-position="right" data-position-fixed="true" data-theme="d" >
  	<ul data-role="listview" data-theme="d" class="nav-search" data-filter="true" data-filter-placeholder="Search..." data-filter-theme="d">
        <li><a href=""  data-transition="none"  onClick="OutletDashboardRedirect(1,<%=OutletID%>,'Summary')">Summary</a></li>
		<li><a href="OutletDashboardLedger.jsp?outletID=<%=OutletID%>&ReportTitle=Ledger"  data-transition="none" >Ledger</a></li>
		<li><a href="OutletDashboardSales.jsp?outletID=<%=OutletID%>&ReportTitle=Sales"   data-transition="none" >Sales</a></li>
        <li><a href="OutletDashboardDiscount.jsp?outletID=<%=OutletID%>&ReportTitle=Discount" data-transition="none" >Discount</a></li>
		<li><a href="DistributorReports.jsp?ReportID=9&HeaderType=OutletDashboard&PageIndex=5&OutletDashboardOutletID=<%=OutletID %>&OutletDashboardOutletName=<%=title %>&ReportTitle=Orders" data-transition="none" >Orders</a></li>
		<li><a href="OutletDashboardApprovedDiscountRequests.jsp?outletID=<%=OutletID%>&ReportTitle=Discount Requests" data-transition="none" >Discount Requests</a></li>
    </ul>
  
  </div>
    
</div><!-- /header -->