<%@page import="com.pbc.common.User"%>
<%@page import="com.pbc.common.EmployeeHierarchy"%>
<%@page import="com.pbc.employee.OrderBookerDashboard"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="java.text.SimpleDateFormat"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(41, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


long EmployeeCode = Utilities.parseLong(request.getParameter("EmployeeCode"));
String EmployeeName = "";
String Designation = "";
String ReportingLevel = "";
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();


ResultSet rs = s.executeQuery("select display_name, designation, u.current_reporting_level, (select short_name from common_hierarchy_levels where id = u.current_reporting_level) current_reporting_label from users u where id="+EmployeeCode);
if(rs.first()){
	EmployeeName = rs.getString(1);
	Designation = rs.getString(2);
	ReportingLevel = rs.getString("current_reporting_label");
}

OrderBookerDashboard OrderBooker = new OrderBookerDashboard();

int DateAttributes[] = OrderBooker.getDateAttributesInNumbers(OrderBooker.getSalesMinMaxDates(EmployeeCode)[0]);
int ChartYear = DateAttributes[0];
int ChartMonth = DateAttributes[1] - 1;
int ChartDay = DateAttributes[2];


Date Today = new Date();

int year = Calendar.getInstance().get(Calendar.YEAR);
int month = Calendar.getInstance().get(Calendar.MONTH);

Date MonthToDate = Utilities.getStartDateByMonth(month, year);

%>

<script type="text/javascript">

var DatePeriod = new Array();
var visits_due = new Array();
var orders = new Array();
var invoices = new Array();

</script>

<%

Date CurrentDate = Utilities.getDateByDays(-30);
int counter = 0;
while(true){
	
	int BIDetails[] = OrderBooker.getBIDetail(EmployeeCode, CurrentDate);
	
	SimpleDateFormat format = new SimpleDateFormat("MMM dd");
	
	//System.out.println("CurrentDate = "+format.format(CurrentDate));
	
	
	
	%>
	<script type="text/javascript">
		DatePeriod[<%=counter%>] = '<%=format.format(CurrentDate)%>';
		visits_due[<%=counter%>] = <%=BIDetails[0]%>;
		orders[<%=counter%>] = <%=BIDetails[1]%>;
		invoices[<%=counter%>] = <%=BIDetails[2]%>; 
	</script>
	<%

	if(DateUtils.isSameDay(CurrentDate, Today)){
		break;
	}
	
	CurrentDate = DateUtils.addDays(CurrentDate, 1);
	counter++;
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
	
	
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="js/lookups.js"></script>
		
		
		<style>
		
		
		
		
			.radio_style
			  {
			      display: block;
			      width: 80px;
			      height: 50px;
			      background-repeat: no-repeat;
			      background-position: -231px 0px;
			  }
			  
			  .ui-table-reflow.ui-responsive{
			  	display:block;
			  }
			
				a{
					font-size: 12px;
				
				}
		</style>
		
		<script>
				
		
		$( document ).delegate("#OrderBookerDashboardPage", "pageinit", function() {
			
			$("#ChangeOrderBooker").on ("click", function( event, ui ) {
				
				$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupEmployeeInit();
				} );
				$('#LookupEmployeeSearch').popup("open", {transition:"pop"});
				
			});
			
		});
		
		function EmployeeSearchCallBackAtOrderBookerDashboard(SAPCode, Name){
			$('#LookupEmployeeSearch').popup("close");
			$("#DashboardOrderBookerEmployeeCode").val(SAPCode);
			document.getElementById("DashboardOrderBookerFormID").submit();
		}
		
		$(function () {
	        $('#container').highcharts({
	            chart: {
	                type: 'area',
	                spacingBottom: 30
	            },
	            title: {
	                text: ''
	            },
	            subtitle: {
	                text: '',
	                floating: true,
	                align: 'right',
	                verticalAlign: 'bottom',
	                y: 15
	            },
	            legend: {
	                layout: 'horizontal',
	                align: 'center',
	                verticalAlign: 'bottom',
	                borderWidth: 1,
	                backgroundColor: '#FFFFFF'
	            },
	            xAxis: {
	                categories: DatePeriod
	            },
	            yAxis: {
	                title: {
	                    text: ''
	                },
	                labels: {
	                    formatter: function() {
	                        return this.value;
	                    }
	                }
	            },
	            tooltip: {
	                formatter: function() {
	                    return '<b>'+ this.series.name +'</b><br/>'+
	                    this.x +': '+ this.y;
	                }
	            },
	            plotOptions: {
	                area: {
	                    fillOpacity: 0.5
	                }
	            },
	            credits: {
	                enabled: false
	            },
	            series: [{
	                name: 'Visits Due',
	                data: visits_due 
	            }, {
	                name: 'Orders',
	                data: orders 
	            }, {
	                name: 'Invoices',
	                data: invoices 
	            }]
	        });
	    });
	    

	    

		</script>
		
	</head>
	
<body>

<div data-role="page" id="OrderBookerDashboardPage" data-url="OrderBookerDashboardPage" data-theme="d">

    <jsp:include page="OrderBookerDashboardHeader.jsp" >
    	<jsp:param value="<%=EmployeeName%>" name="title"/>
    	<jsp:param value="5" name="tab"/>
    	<jsp:param value="<%=EmployeeCode%>" name="EmployeeCode"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
	
	<table style="width: 100%">
	<tr>
	
	<td style="width: 30%" valign="top">

		<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
			<li data-role="list-divider">Today</li>
				<li data-icon="false"><a href="#" data-theme="e">Productivity<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getProductivity(EmployeeCode, Today, Today)%>% </span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Drop Size<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getDropSize(EmployeeCode, Today, Today)%></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">SKU Per Bill<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getSKUPerBill(EmployeeCode, Today, Today)%></span></a></li>
			<li data-role="list-divider">Month To Date</li>
				<li data-icon="false"><a href="#" data-theme="e">Productivity<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getProductivity(EmployeeCode, MonthToDate, Today)%>% </span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">Drop Size<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getDropSize(EmployeeCode, MonthToDate, Today)%></span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">SKU Per Bill<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getSKUPerBill(EmployeeCode, MonthToDate, Today)%></span></a></li>
		</ul>

       <td>
       
       <td style="width: 50%;" valign="top">
       
       <div style="margin-left: 25px; margin-top: -15px;">
<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
    <li data-role="list-divider">Beat Plan Activity</li>

<li>        

        	<div align="center" id="container" style="min-width: 810px; height: 400px; margin: 0 auto"></div>
</li>

    </ul>
</ul>
       </div>
       </td>
       
       <td style="width: 20%" valign="top">
       
		<ul data-role="listview" data-inset="false" style="margin-top: -15px;margin-left: 13px; max-width: 250px;" data-divider-theme="c" style="width: 20px;">
		
			<li data-role="list-divider">-</li>
				<li data-icon="false"><a href="#" data-theme="e">-<span class="ui-li-count" style="font-size: 10;">-</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">-<span class="ui-li-count" style="font-size: 10;">-</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">-<span class="ui-li-count" style="font-size: 10;">-</span></a></li>
			<li data-role="list-divider">-</li>
				<li data-icon="false"><a href="#" data-theme="e">-<span class="ui-li-count" style="font-size: 10;">-</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">-<span class="ui-li-count" style="font-size: 10;">-</span></a></li>
				<li data-icon="false"><a href="#" data-theme="e">-<span class="ui-li-count" style="font-size: 10;"></span></span></a></li>
		
		</ul>

		
       </td> 
       </tr>
       
       
       
       </table>
    </div><!-- /content -->
    



	<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="BeatPlanEditForm" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>

						<ul id="autocomplete_search" data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search by Employee ID or by Name" data-filter-theme="d"></ul>
						
                    
                    </td>
                    <td>
                    
						<span id="EmployeeNameSearch" style="padding-left:20px"></span>
						<input type="hidden" name="EmployeeIDSearch" id="EmployeeIDSearch" value="" >
                    
                    </td>
                    
                </tr>
                <tr>
                	<td>&nbsp;</td>
                </tr>
                <tr>
                	<td>

						<ul id="autocomplete_outlet_search" data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search by Outlet ID or by Name" data-filter-theme="d"></ul>
						
                    
                    </td>
                    <td>
                    
						<span id="OutletNameSearch" style="padding-left:20px"></span>
						<input type="hidden" name="OutletIDSearch" id="OutletIDSearch" value="" >
                    
                    </td>
                    
                </tr>
                
                <tr>
                	<td>
                    	<button data-role="button" data-icon="search" id="BeatPlanSearchButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="EmployeeBeatPlanSearchContent">
        </div>
            
        </div>
    </div>
    
    
    <form id="DashboardOrderBookerFormID" name="DashboardOrderBookerFormID" action="OrderBookerDashboardOverview.jsp" method="POST" data-ajax="false">
    	<input type="hidden" name="EmployeeCode" id="DashboardOrderBookerEmployeeCode"/>
    </form>

    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBackAtOrderBookerDashboard" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->
    
    <jsp:include page="LookupOutletSearchPopup.jsp" > 
    	<jsp:param value="OutletSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Outlet Search -->

</div>
</body>
</html>
<%
s2.close();
s.close();
ds.dropConnection();

OrderBooker.close();
%>