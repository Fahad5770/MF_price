<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>



<script>


var VerticalTotals = new Array();

var OrderID = 0;
var OrderBookerInfo = "";
var OutletInfo = "";


function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 126;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

String PackageIDs = "";
String WherePackage = "";

if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = " and package_id in ("+PackageIDs+") ";
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and mo.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}


boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String WhereOrderBookerIDs="";
if(OrderBookerIDs.length()>0){
	WhereOrderBookerIDs =" and mo.created_by in ("+OrderBookerIDs+") ";
}

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

%>



<table style="width: 100%">

<tr>
	<td>
		<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Quick View</li>
			<li>
				<div id="container56985235446" style="min-width: 310px; height: 300px; margin: 0 auto"></div>
			</li>
		</ul>
	</td>
</tr>
	<tr>
		<td valign="top" style="width: 100%">
		
		<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Order List</li>
<li>
<%


//System.out.println(StartDateMonth6+" - "+EndDateMonth6);

%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							
							<%							
							int PJPID=0;
							%>
							<th data-priority="1"  style="text-align:center; width: 30% ">Order Booker</th>
							<th data-priority="1"  style="text-align:center; width: 30% ">Outlet</th>
					  	    <!-- <th data-priority="1"  style="text-align:center; width: 10%" nowrap="nowrap">Order Location</th>
							<th data-priority="1"  style="text-align:center; width: 10%" nowrap="nowrap">Outlet Location</th> -->
							<th data-priority="1"  style="text-align:center; width: 10%" nowrap="nowrap">Distance (m)</th>
							<th data-priority="1"  style="text-align:center; width: 10%" nowrap="nowrap">&nbsp;</th>
													
					    </tr>
					  </thead>
					<tbody>
					<%
					
					
					
					double VerticalTotal[] = new double[9];
					
					String SQL = "SELECT mo.id, mo.outlet_id, mo.lat, mo.lng, co.name outlet_name, mo.created_by, (select display_name from users where id=mo.created_by) orderbooker_name, co.lat outlet_lat, co.lng outlet_lng, "
							
							+ "(( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 ) AS distance "
							
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '00:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '7:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'before_6'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '6:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '7:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_6_8'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '8:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '9:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_8_10'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '10:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '11:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_10_12'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '12:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '13:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_12_14'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '14:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '15:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_14_16'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '16:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '17:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_16_18'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '18:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '19:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'slot_18_20'"
							+ ", if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '20:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '23:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0) as 'overnight'"
							
							+ ", (time_to_sec(time(mobile_timestamp))/60)/60 booking_time "
							
					        + " FROM mobile_order mo, common_outlets co where mo.outlet_id=co.id and mo.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+WhereOrderBookerIDs+" having distance < 1000 order by distance desc ";
					
					System.out.println(SQL);
					
					String GraphPoints = "";
					int counter = 0;
					ResultSet rs = s.executeQuery(SQL);
					while(rs.next()){
						
						VerticalTotal[0]+=rs.getDouble("before_6");
						VerticalTotal[1]+=rs.getDouble("slot_6_8");
						VerticalTotal[2]+=rs.getDouble("slot_8_10");
						VerticalTotal[3]+=rs.getDouble("slot_10_12");
						VerticalTotal[4]+=rs.getDouble("slot_12_14");
						
						VerticalTotal[5]+=rs.getDouble("slot_14_16");
						VerticalTotal[6]+=rs.getDouble("slot_16_18");
						VerticalTotal[7]+=rs.getDouble("slot_18_20");
						VerticalTotal[8]+=rs.getDouble("overnight");
						
						if( rs.getDouble("lat") > 0 && rs.getDouble("lng") > 0 && rs.getDouble("outlet_lat") > 0 && rs.getDouble("outlet_lng") > 0 ){
							
							if(counter > 0){
								GraphPoints += ", ";
							}
							
							GraphPoints += " [ "+Utilities.getDisplayCurrencyFormatSimple(rs.getDouble("booking_time"))+", "+Math.round(rs.getDouble("distance"))+" ] ";
						
					%>
						<tr>
							<td><%=rs.getLong("created_by") %> - <%=rs.getString("orderbooker_name") %> </td>
							<td><%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %>
								<input type="hidden" id="OrderLocation_<%=rs.getLong("id") %>" value="<%=rs.getString("lat")%> , <%=rs.getString("lng")%>" >
								<input type="hidden" id="OutletLocation_<%=rs.getLong("id") %>" value="<%=rs.getString("outlet_lat")%> , <%=rs.getString("outlet_lng")%>" >
							</td>							
					   		<!-- <td style="text-align:right" nowrap="nowrap" id="OrderLocation_<%//=rs.getLong("id") %>"><%//=rs.getString("lat")%> , <%//=rs.getString("lng")%></td>	
							<td style="text-align:right" nowrap="nowrap" id="OutletLocation_<%//=rs.getLong("id") %>"><%//=rs.getString("outlet_lat")%> , <%//=rs.getString("outlet_lng")%></td> -->
							<td style="text-align:right" nowrap="nowrap"><%= Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("distance"))%></td>
							<td style="text-align:center" nowrap="nowrap"><a href="#" onclick="openPopup(<%=rs.getString("id")%>, '<%=rs.getLong("created_by") %> - <%=rs.getString("orderbooker_name") %>', '<%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %>')" >Map</a></td>
						</tr>
					<%
					
						counter++;
					
						}
					
					}
					%>
					
					<%
					for(int i=0;i<VerticalTotal.length;i++){						
					%>
					
					<script> VerticalTotals[<%=i%>] = <%=VerticalTotal[i]%>; </script>
					
						
					<% } %>
					
						
					</tbody>
							
				</table>
		</td>
	</tr>
	
</table>




	</li>	
</ul>
		
		
		</td>
		
	</tr>
</table>

<script>
/*
$(function () {
    $('#container56985235446').highcharts({
        chart: {
            type: 'line'
        },
        credits: {
        	enabled: false
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        xAxis: {
            categories: ['Before 06AM', '06AM - 08AM', '08AM - 10AM', '10AM - 12PM', '12PM - 02PM', '02PM - 04PM', '04PM - 06PM', '06PM - 08PM', 'After 08PM']
        },
        yAxis: {
            title: {
                text: 'No of Orders'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [{
            name: '',
            showInLegend:false,
            data: VerticalTotals
        }]
    });
});
*/ 

$(function () {
    $('#container56985235446').highcharts({
        chart: {
            type: 'scatter',
            zoomType: 'xy'
        },
        credits: {
        	enabled: false
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        xAxis: {
            title: {
                enabled: true,
                text: 'Time'
            },
            startOnTick: true,
            endOnTick: true,
            showLastLabel: true
        },
        yAxis: {
            title: {
                text: 'Distance'
            }
        },
        legend: {
            layout: 'vertical',
            align: 'left',
            verticalAlign: 'top',
            x: 100,
            y: 70,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',
            borderWidth: 1
        },
        plotOptions: {
        	
        	series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function (e) {
                        	alert(this.x+', '+this.y);
                        	openPopup(81384, '', '');
                        }
                    }
                },
                marker: {
                    lineWidth: 1
                }
            },
        	
            scatter: {
                marker: {
                    radius: 5,
                    states: {
                        hover: {
                            enabled: true,
                            lineColor: 'rgb(100,100,100)'
                        }
                    }
                },
                states: {
                    hover: {
                        marker: {
                            enabled: false
                        }
                    }
                },
                tooltip: {
                    headerFormat: '',
                    pointFormat: 'Booked at {point.x} Hrs, {point.y} meters away from Outlet.'
                }
            }
        },
        series: [{
            name: '',
            color: 'rgba(223, 83, 83, .5)',
            showInLegend:false,
            data: [ <%=GraphPoints%> ]

        }]
    });
});
    


function openPopup(OrderIDParam, OrderBookerInfoParam, OutletInfoParam){
	

	OrderID = OrderIDParam;
	OrderBookerInfo = OrderBookerInfoParam;
	OutletInfo = OutletInfoParam;
	
	$.get("ReportCenterR125MapView.jsp", function(data) {
		  
		  $("#map_view").html(data);
		  $("#map_view").trigger('create');
		 
	});
		 
	
	/*
	$('#tempdiv').html(order_lat+", "+order_lng+"<br>"+outlet_lat+", "+outlet_lng);
	*/
	$("#R125_ShowMapView" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
}

</script>



<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>