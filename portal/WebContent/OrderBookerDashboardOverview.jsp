<%@page import="com.pbc.common.User"%>
<%@page import="com.pbc.common.EmployeeHierarchy"%>
<%@page import="com.pbc.employee.OrderBookerDashboard"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(UserAccess.isAuthorized(41, SessionUserID, request) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

long EmployeeCode = Utilities.parseLong(request.getParameter("EmployeeCode"));

if (UserAccess.isOrderBookerAllowed(EmployeeCode, SessionUserID, 41) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}


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

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
	
	
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="lib/highcharts301/js/highcharts.js"></script>
		<script src="js/lookups.js"></script>
		<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
	
	
	<script>
	/*$( document ).delegate("#OrderBookerDashboardPage", "pageinit", function() {
		
		var DistdbClickNameArray = new Array();
		DistdbClickNameArray = document.getElementsByName("OrderBookerDashboardOverviewDistdbClickName")
		for(var i =0;i<DistdbClickNameArray.length;i++){
			//alert(DistdbClickNameArray[i].id);
			$( "#OrderBookerDashboardOverviewDistdbClickID_"+DistdbClickNameArray[i].id ).dblclick(function() {				  
				  var htmlText = $(this).html().split("-");  //100669 - ABC
				  //alert(htmlText[0].trim());
				  $("#OrderBookerDashboardOverviewDistributorCode1").val(htmlText[0].trim());
				  document.getElementById("OrderBookerDashboardOverviewFormID1").submit();
				});
		}
		
		
		/*$( "#OrderBookerDashboardOverviewDistdbClickID" ).dblclick(function() {
			  //alert($(this).html());
			  var htmlText = $(this).html().split("-");  //100669 - ABC
			  alert(htmlText[0].trim());
			});)*/
	//});*/
	
	
	function DistributorDashboardRedirect(DistID){		
		$("#OrderBookerDashboardOverviewDistributorCode1").val(DistID);
		  document.getElementById("OrderBookerDashboardOverviewFormID1").submit();
	}

	
	</script>
	
	
		
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
			
			google.maps.event.addDomListener(window, 'load', initialize);
			
			
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
		
		function initialize() {
			  var myLatlng = new google.maps.LatLng(31.427335740000000,73.094063400000000);
			  var mapOptions = {
			    zoom: 15,
			    center: myLatlng
			  };

			  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

			  var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';

			  var infowindow = new google.maps.InfoWindow({
			      content: contentString,
			      maxWidth: 200
			  });
			  
			  var markers = new Array();
			  
			  <%
			  ResultSet rs2 = OrderBooker.getMarkers(EmployeeCode);
			  int i = 0;
			  while(rs2.next()){
				  
				  String IconType = "";
				  if(rs2.getInt("is_discounted") == 1){
					  IconType = "d";
				  }else{
					  IconType = "o";
				  }
				  
				  if(i == 0){
					  %>
					  	map.setCenter(new google.maps.LatLng(<%=rs2.getString("lat")%>,<%=rs2.getString("lng")%>));
					  <%
				  }
				  
				  %>
				  
				  markers[<%=i%>] = new google.maps.Marker({
				      position: new google.maps.LatLng(<%=rs2.getString("lat")%>,<%=rs2.getString("lng")%>),
				      map: map,
				      title: '<%=rs2.getString("name")%>',
				      icon: 'images/markers/letter_<%=IconType%>.png'
				  });
				  google.maps.event.addListener(markers[<%=i%>], 'click', function() {
				    infowindow.open(map,markers[<%=i%>]);
				    
				    var infoWindowContent = "<%=rs2.getString("id")%>-<%=rs2.getString("name")%><br>"; 
				    infoWindowContent += "<a href='#'>View Detail</a>";
				    infowindow.setContent(infoWindowContent);
				    //infowi
				  });
				  
				  <%
				  i++;
			  }
			  
			  %>

 
			}
		
		$(function () {
	        $('#container').highcharts({
	            chart: {
	                zoomType: 'x',
	                spacingRight: 20,
	                marginTop: 40	                
	            },
	            credits: {
	            	enabled: false
	            },
	            title: {
	                text: '<font style="font-size: 8pt;">Click and drag in the plot area to zoom in</font>',
	            },
	            xAxis: {
	                type: 'datetime',
	                maxZoom: 14 * 24 * 3600000, // fourteen days
	                title: {
	                    text: null
	                }
	            },
	            yAxis: {
	                title: {
	                    text: 'Rupees in thousands'
	                }
	            },
	            tooltip: {
	                shared: true
	            },
	            legend: {
	                enabled: false
	            },
	            plotOptions: {
	                area: {
	                    fillColor: {
	                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
	                        stops: [
	                            [0, Highcharts.getOptions().colors[0]],
	                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
	                        ]
	                    },
	                    lineWidth: 1,
	                    marker: {
	                        enabled: false
	                    },
	                    shadow: false,
	                    states: {
	                        hover: {
	                            lineWidth: 1
	                        }
	                    },
	                    threshold: null
	                }
	            },
	    
	            series: [{
	                type: 'area',
	                name: 'Rupees',
	                pointInterval: 24 * 3600 * 1000,
	                
	                
	                pointStart: Date.UTC(<%=ChartYear%>, <%=ChartMonth%>, <%=ChartDay%>),
	                data: [
	                    <%=OrderBooker.getDailySales(EmployeeCode)%>
	                    
	                ]
	            }]
	        });
	    });
	    
		
		</script>
		
	</head>
	
<body>

<div data-role="page" id="OrderBookerDashboardPage" data-url="OrderBookerDashboardPage" data-theme="d">

    <jsp:include page="OrderBookerDashboardHeader.jsp" >
    	<jsp:param value="<%=EmployeeName%>" name="title"/>
    	<jsp:param value="1" name="tab"/>
    	<jsp:param value="<%=EmployeeCode%>" name="EmployeeCode"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
	
	<table style="width: 100%">
	<tr>
	
	<td style="width: 30%" valign="top">

		<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
			<li data-role="list-divider">Profile</li>
			<li data-icon="false"><a href="#" data-theme="e">Employee Since<span class="ui-li-count" style="font-size: 10;"><%=Utilities.getDisplayFullDateFormatShort(OrderBooker.getJoiningDate(EmployeeCode))%></span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Service Length<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getServiceDuration(EmployeeCode)%> Years</span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Daily Sales (Average)<span class="ui-li-count" style="font-size: 10;">Rs. <%=Utilities.getDisplayCurrencyFormatRounded(OrderBooker.getAverageDailySales(EmployeeCode)) %></span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Monthly Sales (Average)<span class="ui-li-count" style="font-size: 10;">Rs. ---</span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Sales Rank<span class="ui-li-count" style="font-size: 10;">--- out of ---</span></a></li>
			<li data-role="list-divider">Outlets</li>
			<li data-icon="false"><a href="#" data-theme="e">Total Assigned<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getTotalOutletAssigned(EmployeeCode)%></span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Discounted<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getTotalOutletsDiscountStatus(EmployeeCode)[0]%></span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Non-Discounted<span class="ui-li-count" style="font-size: 10;"><%=OrderBooker.getTotalOutletsDiscountStatus(EmployeeCode)[1]%></span></span></a></li>
			<li data-icon="false"><a href="#" data-theme="e">Average Daily Visits<span class="ui-li-count" style="font-size: 10;">-</span></a></li>
		</ul>

	


		<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d" style="margin-top: 20px;">
			<li data-role="list-divider" data-theme="d">Product Group</li>
			
			<%
			int ProductGroupID = 0;
			String ProductGroupName = "";
			ResultSet rs4 = s.executeQuery("SELECT epg.product_group_id, epg.product_group_name FROM employee_product_specification eps, employee_product_groups epg where eps.employee_product_group_id=epg.product_group_id and eps.employee_sap_code="+EmployeeCode); 
			if(rs4.first()){
				ProductGroupID = rs4.getInt("product_group_id");
				ProductGroupName = rs4.getString("product_group_name");
			}
			%>
			
			<li data-icon="false"><a href="#" data-theme="e"><%=ProductGroupName%></a></li>
			
			<%
			ResultSet rs5 = s.executeQuery("SELECT distinct ipv.package_label, ipv.package_id FROM employee_product_groups_list epgl, inventory_products_view ipv where epgl.product_id=ipv.product_id and  epgl.product_group_id="+ProductGroupID+" and ipv.category_id=1 order by ipv.package_sort_order"); 
			while(rs5.next()){
				%>
				<li data-role="list-divider"><%=rs5.getString("package_label")%></li>
				<%
				
				ResultSet rs6 = s2.executeQuery("select brand_label from inventory_products_view where package_id="+rs5.getString("package_id")+" and category_id=1 order by brand_label");
				while(rs6.next()){
					%>
					<li data-icon="false"><a href="#" data-theme="e"><%=rs6.getString("brand_label")%></a></li>
					<%
				}
				
			}
			%>
			
			<!-- <li data-role="list-divider">1000 ML PET</li>
			<li data-icon="false"><a href="#" data-theme="e">PEPSI</a></li>
			<li data-icon="false"><a href="#" data-theme="e">Mirinda</a></li>
			<li data-role="list-divider">250 ML STD</li>
			<li data-icon="false"><a href="#" data-theme="e">PEPSI</a></li>
			<li data-icon="false"><a href="#" data-theme="e">Teem</a></li>
			 -->
		</ul>


       <td>
       
       <td style="width: 50%;" valign="top">
       
       <div style="margin-left: 25px; margin-top: -15px;">
<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d">
    <li data-role="list-divider">Sales</li>

<li>        

        	<div align="center" id="container" style="min-width: 810px; height: 400px; margin: 0 auto"></div>
</li>
<li data-role="list-divider">Outlets</li>
<li>
<div id="map-canvas" style="width: 100%; height: 500px;"></div>
</li>
    </ul>
</ul>
       </div>
       </td>
       
       <td style="width: 20%" valign="top">
       
       
       <ul data-role="listview" data-inset="false" style="margin-top: -15px;margin-left: 13px; max-width: 250px;" data-divider-theme="c" style="width: 20px;">
	<li data-role="list-divider">Hierarchy</li>
	<%
	EmployeeHierarchy eh = new EmployeeHierarchy();
	User[] list = eh.getReportingToAll(EmployeeCode);
	
	for (int x = (list.length-1); x >= 0; x--){
	%>
    <li data-icon="false"><a href="#" >
        <img src="images/noimage.jpg">
        <h2 style="font-size: 12px; margin-left: -10px;"><%= list[x].USER_DISPLAY_NAME %></h2>
        <p style="margin-left: -10px;"><%= list[x].DESIGNATION %><br><%= list[x].HIERARCHY_LEVEL_LABEL %></p></a>
    </li>
    <%
	}
	
    %>
    <li data-theme="c" data-icon="false">
        <img src="images/noimage.jpg">
        <h2 style="font-size: 12px; margin-left: -10px;"><%=EmployeeName %></h2>
        <p style="margin-left: -10px;"><%=Designation %><br><%=ReportingLevel %></p>
    </li>
</ul>

		<ul data-role="listview" data-inset="false" data-theme="d" data-divider-theme="c" data-count-theme="d" style="margin-top: 20px;margin-left: 13px; max-width: 250px;">
			<li data-role="list-divider">Distributors</li>
			
			<%
			ResultSet rs3 = OrderBooker.getDistributors(EmployeeCode);
			while(rs3.next()){
				%>
				<li data-icon="false"><a href="#" data-theme="e" <%if(Utilities.isAuthorized(44, SessionUserID) == true){%>ondblclick="DistributorDashboardRedirect(<%=rs3.getString("distributor_id")%>)"<%} %>><%=rs3.getString("distributor_id")+" - "+rs3.getString("name")%></a></li>
				<%
			}
			%>
			  <form id="OrderBookerDashboardOverviewFormID1" name="OrderBookerDashboardOverviewFormID1" action="DistributorDashboardOverview.jsp" method="POST" data-ajax="false" target="_blank">
			  	<input type="hidden" name="DistributorCode" id="OrderBookerDashboardOverviewDistributorCode1"/>
			  </form>
			
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