<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
String DistributorNameToShow = request.getParameter("DistributorNameToShow");
long DistributorCode = Utilities.parseLong(request.getParameter("DistributorCode"));
int PageIndex = Utilities.parseInt(request.getParameter("PageIndex"));

int ActivePageIndex = 0;

String ReportTitleToDisp = Utilities.filterString(request.getParameter("ReportTitleToShow"), 1, 100);
%>
<script>
function LoadActivitySummary(ReportID,DistributorCode,PageIndex,DistributorName,HeaderType,ReportTitleQuery){
	
	document.getElementById("DistributorDashboardActivitySummaryReportID").value = ReportID;
	document.getElementById("DistributorDashboardActivitySummaryDistributorCode").value = DistributorCode;
	document.getElementById("DistributorDashboardActivitySummaryPageIndex").value = PageIndex;
	document.getElementById("DistributorDashboardActivitySummaryDistributorName").value = DistributorName;
	document.getElementById("DistributorDashboardActivitySummaryHeaderType").value = HeaderType;
	document.getElementById("DistributorDashboardActivitySummaryReportTitleQuery").value = ReportTitleQuery;
	
	document.getElementById("DistributorDashboardActivitySummaryFormID").submit();
}

function LoadDistDashboardOverview(){
	document.getElementById("DistributorDashboardOverviewFormID").submit();
}
</script>

<div data-role="header" data-id="DistributorDashboardHeader" data-theme="d">
     <!--  <h1><%=DistributorCode+" - "+DistributorNameToShow%></h1>-->
   <div style="width:100%; min-height:50px; b1ackground-color:red;">
    	<div style="width:35%; height:50px; float:left; b1ackground-color:green">
    		<a href="home.jsp" data-role="button" data-inline="true" data-ajax="false" data-icon="back">Back</a> 
    	</div>
    	<div style="width:30%; height:50px; float:left; b1ackground-color:yellow">
    		<h1 style="font-size:16px; text-align:center;"><%=ReportTitleToDisp %></h1>
    	</div>
    	
    	<div style="width:35%; height:50px; float:left; b1ackground-color:blue;float:left;">
    		<div style="width:350px; float:right;">
    			<div data-role="controlgroup" data-type="horizontal" style="float:right; margin-right: 5px;">
			    	<a href="#" id="ChangeDistributor" data-role="button" ><%=DistributorCode+" - "+Utilities.truncateStringToMax(DistributorNameToShow, 20)  %></a>
			    	<a href="#distdashboardmenupanel"  data-role="button" data-inline="true" data-mini="true" data-icon="bars" data-theme="a"  adata-iconpos="notext">Menu</a>
				</div>
    		</div>
    		
    	</div>
   
   </div>
    
    
    <input type="hidden" id="ActivePageIndex" value="HeaderLink<%=ActivePageIndex%>">
    
    <div data-role="panel" id="distdashboardmenupanel" data-display="overlay" data-position="right" data-position-fixed="true" data-theme="d" >
		<ul data-role="listview" data-theme="d" class="nav-search" data-filter="true" data-filter-placeholder="Search..." data-filter-theme="d">
          
		 	<li data-icon="grid" ><a id="HeaderLink0" href="" onClick="LoadDistDashboardOverview()" data-ajax="false" data-transition="none" >Overview</a></li>
		 		<%
        if(UserAccess.isAuthorized(67, SessionUserID, request) == true){
        	ActivePageIndex = 1;
        %>
            <!-- <li data-icon="grid" ><a id="HeaderLink1" href="DistributorReports.jsp?ReportID=1&DistributorCode=<%=DistributorCode%>&PageIndex=1&DistributorName=<%=DistributorNameToShow%>&HeaderType=DistributorDashboard&ReportTitleQuery=Activity Summary" data-ajax="false" data-transition="none" >Activity Summary</a></li> -->
       		<li data-icon="grid" ><a id="HeaderLink1" href="" onClick="LoadActivitySummary(1,'<%=DistributorCode%>',1,'<%=DistributorNameToShow%>','DistributorDashboard','Activity Summary')" data-ajax="false" data-transition="none" >Activity Summary</a></li>
       	<%
        }
       	%>
        <%
        if(UserAccess.isAuthorized(68, SessionUserID, request) == true){
        	if (ActivePageIndex == 0){
        		ActivePageIndex = 2;
        	}
        %>       	
			<li data-icon="grid"><a id="HeaderLink2" href="" onClick="LoadActivitySummary(2,'<%=DistributorCode%>',2,'<%=DistributorNameToShow%>','DistributorDashboard','Stock Position')" data-ajax="false" data-transition="none" >Stock Position</a></li>
		<%
        }
		%>
        <%
        if(UserAccess.isAuthorized(69, SessionUserID, request) == true){
        	if (ActivePageIndex == 0){
        		ActivePageIndex = 4;
        	}
        %>              
			<li data-icon="grid"><a href="" onClick="LoadActivitySummary(3,'<%=DistributorCode%>',4,'<%=DistributorNameToShow%>','DistributorDashboard','Sales Summary')" data-ajax="false" data-transition="none" >Sales Summary</a></li>
		<%
        }
		%>			
        <%
        if(UserAccess.isAuthorized(86, SessionUserID, request) == true){
        	if (ActivePageIndex == 0){
        		ActivePageIndex = 4;
        	}
        %>              
			<li data-icon="grid"><a href="" onClick="LoadActivitySummary(10,'<%=DistributorCode%>',7,'<%=DistributorNameToShow%>','DistributorDashboard','Adjusted Sales Summary')" data-ajax="false" data-transition="none" >Adjusted Sales Summary</a></li>
		<%
        }
		%>			
		
        <%
        if(UserAccess.isAuthorized(70, SessionUserID, request) == true){
        	if (ActivePageIndex == 0){
        		ActivePageIndex = 5;
        	}
        %>              
			<li data-icon="grid"><a href="" onClick="LoadActivitySummary(4,'<%=DistributorCode%>',5,'<%=DistributorNameToShow%>','DistributorDashboard','Dispatch')"  data-ajax="false" data-transition="none" >Dispatch</a></li>
		<%
        }
		%>
        <%
        if(UserAccess.isAuthorized(71, SessionUserID, request) == true){
        	if (ActivePageIndex == 0){
        		ActivePageIndex = 6;
        	}
        %>              
			<li data-icon="grid"><a href="" onClick="LoadActivitySummary(5,'<%=DistributorCode%>',6,'<%=DistributorNameToShow%>','DistributorDashboard','Outlets')" data-ajax="false" data-transition="none" >Outlets</a></li>
		<%
        }
		%>
		<%
        if(UserAccess.isAuthorized(37, SessionUserID, request) == true){  //lifting report feature id =37
        	if (ActivePageIndex == 0){
        		ActivePageIndex = 8;
        	}
        %>              
			<li data-icon="grid"><a href="" onClick="LoadActivitySummary(11,'<%=DistributorCode%>',8,'<%=DistributorNameToShow%>','DistributorDashboard','Lifting')" data-ajax="false" data-transition="none" >Lifting</a></li>
		<%
        }
		%>
		<!-- <li data-icon="grid" data-theme="e">
          <a href="javascript:changeDistributorForDashboard()"  >Change Distributor</a>  	
        </li>  -->  

        </ul>
        <br/>
        <!-- <a href="javascript:changeDistributorForDashboard()" data-role="button" data-theme="c"  data-icon="back">Change Distributor</a> -->
	
	<form id="DistributorDashboardActivitySummaryFormID" name="DistributorDashboardActivitySummaryFormID" action="DistributorReports.jsp" method="POST" data-ajax="false">
    	
    	<input type="hidden" name="ReportID" id="DistributorDashboardActivitySummaryReportID" value=""/>
    	<input type="hidden" name="DistributorCode" id="DistributorDashboardActivitySummaryDistributorCode" value=""/>
    	<input type="hidden" name="PageIndex" id="DistributorDashboardActivitySummaryPageIndex" value=""/>
    	<input type="hidden" name="DistributorName" id="DistributorDashboardActivitySummaryDistributorName" value=""/>
    	<input type="hidden" name="HeaderType" id="DistributorDashboardActivitySummaryHeaderType" value=""/>
    	<input type="hidden" name="ReportTitleQuery" id="DistributorDashboardActivitySummaryReportTitleQuery" value=""/>
    </form>
    
    
    <form id="DistributorDashboardOverviewFormID" name="DistributorDashboardOverviewFormID" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false">
    	
    	<input type="hidden" name="DistributorCode" id="DistributorDashboardActivitySummaryReportID" value="<%=DistributorCode%>"/>    	
    	<input type="hidden" name="PageIndex" id="DistributorDashboardActivitySummaryPageIndex" value="1"/>
    	<input type="hidden" name="DistributorName" id="DistributorDashboardActivitySummaryDistributorName" value="<%=DistributorNameToShow%>"/>
    	
    </form>
	
	</div>
</div><!-- /header -->


