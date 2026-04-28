<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.AlmoizDateUtils"%>
<%@page import="com.pbc.reports.R372Data"%>
<%@page import="com.pbc.reports.R372DataList"%>
<%@page import="com.pbc.reports.R372Excel"%>
<%@page import="com.pbc.util.AlmoizFormulas"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}



</script>

<style>
td {
	font-size: 8pt;
}
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 488;

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

String filename = "Merchandiser_Report" + Utilities.getSQLDateWithoutSeprator(new java.util.Date()) + ".xlsx";

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

Date StartDate = (Date) session.getAttribute(UniqueSessionID + "_SR1StartDate");
Date EndDate = (Date) session.getAttribute(UniqueSessionID + "_SR1EndDate");

if (session.getAttribute(UniqueSessionID + "_SR1StartDate") == null) {
	StartDate = new Date();
}

if (session.getAttribute(UniqueSessionID + "_SR1EndDate") == null) {
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

// Distributor Filter
String WhereDistributors = "", DistributorIDs = "";
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors") != null) {
	SelectedDistributorsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors");

	System.out.println("DistributorIDs : " + SelectedDistributorsArray.length);

} else {
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

}

if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
	for (int i = 0; i < SelectedDistributorsArray.length; i++) {

		if (i == 0) {
	DistributorIDs += SelectedDistributorsArray[i];
		} else {
	DistributorIDs += ", " + SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and distributor_id in (" + DistributorIDs + ")";
}

//System.out.println("WhereDistributors : "+WhereDistributors);

//Region Filter
String RegionIDs = "";
long SelectedRegionsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRegion") != null) {
	SelectedRegionsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionsArray);
}

String whereRegions = (RegionIDs.length() > 0) ? " and region_id in (" + RegionIDs + ")" : "";

//System.out.println("whereRegions : "+whereRegions);

//Channel Filter
String ChannelIDs = "";
long SelectedChannelsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedChannels") != null) {
	SelectedChannelsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedChannels");
	ChannelIDs = Utilities.serializeForSQL(SelectedChannelsArray);
}

String whereChannels = (ChannelIDs.length() > 0)
		? " and outlet_id in (select id from common_outlets where pic_channel_id in (" + ChannelIDs + "))"
		: "";

//System.out.println("whereChannels : "+whereChannels);

//City Filter
String CityIDs = "";
long SelectedCityArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedCity") != null) {
	SelectedCityArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedCity");
	CityIDs = Utilities.serializeForSQL(SelectedCityArray);
}

String whereCities = (CityIDs.length() > 0) ? " and city_id in (" + CityIDs + ")" : "";

//System.out.println("whereCities : "+whereCities);

//Order Bookers Filter
String OrderBookersIDs = "";
long SelectedOrderBookersArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers") != null) {
	SelectedOrderBookersArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers");
	OrderBookersIDs = Utilities.serializeForSQL(SelectedOrderBookersArray);
}

String whereOrderBooker = (OrderBookersIDs.length() > 0) ? " and created_by in (" + OrderBookersIDs + ")" : "";

//System.out.println("OrderBookersIDs : "+OrderBookersIDs);
%>
<script>

function outletImages(id, outletId){
	//alert(id);
//	$("#SearchContent").focus();
	//document.getElementById("DeliveryNoteFromDate").disabled = "disabled";
	//document.getElementById("DeliveryNoteToDate").disabled = "disabled";
	$("#R372" ).popup( "open" );
	 $.get('ReportCenterR372OutletImagesPopUP.jsp?id='+id + '&outletId='+outletId, function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	}); 
	//return false;

}




</script>

<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">Merchandiser Report</li>
	<li>
		<table border="0"
			style="width: 100%; margin-bottom: 12px; text-align: center;">
			<tr>
				<td style="width: 100%" valign="top">

					<div id="aExcelFileReady">
						<a href="common/CommonFileDownload?file=<%=filename%>"
							data-theme="c" data-inline="true" target="_blank"
							style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px; color: Gray; text-decoration: none; font-weight: bold; cursor: pointer;">
							Download</a>
					</div>

				</td>
			</tr>
		</table>

		<table style="width: 100%; margin-top: -8px" cellpadding="0"
			cellspacing="0">
			<tr>

				<td style="width: 100%" valign="top">
					<table border=0
						style="font-size: 13px; font-weight: 400; width: 100%; overflow-x: scroll;"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						<thead>



							<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center; width: 4%;">Sr
									No.</th>
								<th data-priority="1" style="text-align: center; width: 12%;">User
									(Merchandiser) ID</th>
								<th data-priority="1" style="text-align: center; width: 15%;">User
									(Merchandiser) Name</th>
								<th data-priority="1" style="text-align: center; width: 9%;">Outlet
									ID</th>
								<th data-priority="1" style="text-align: center; width: 15%;">Outlet
									Name</th>
								<th data-priority="1" style="text-align: center; width: 10%;">Channel</th>

								<th data-priority="1" style="text-align: center; width: 5%;">Visit
									Date</th>
								<th data-priority="1" style="text-align: center; width: 5%;">Actual
									Distance</th>
								<th data-priority="1" style="text-align: center; width: 5%;">Start
									Time</th>
								<th data-priority="1" style="text-align: center; width: 5%;">End
									Time</th>
								<th data-priority="1" style="text-align: center; width: 5%;">Shop
									Time</th>
								<th data-priority="1" style="text-align: center; width: 5%;">Journey
									Time</th>
								<th data-priority="1" style="text-align: center; width: 5%;">Outlet
									Images</th>
							</tr>
						</thead>
						<tbody>
							<%
							R372DataList r372List = new R372DataList();

							String mobileTImestamp = " created_on between " + Utilities.getSQLDate(StartDate) + " and "
									+ Utilities.getSQLDateNext(EndDate);
							Date Start_time = null, End_time = null;

							/* 	System.out.println(
								"select  id,created_by, outlet_id,created_on,lat,lng, (select Display_Name from users where id=created_by) as user from mobile_merchandiser where created_on between "
								+ AlmoizDateUtils.getSQLDate(StartDate) + " and " + AlmoizDateUtils.getSQLDateNext(EndDate) + WhereDistributors + whereRegions + whereChannels + whereCities + whereOrderBooker); */
							ResultSet rsData = s.executeQuery(
									"select  id,created_by, outlet_id,created_on,lat,lng, (select Display_Name from users where id=created_by) as user from mobile_merchandiser where created_on between "
									+ AlmoizDateUtils.getSQLDate(StartDate) + " and " + AlmoizDateUtils.getSQLDateNext(EndDate)
									+ WhereDistributors + whereRegions + whereChannels + whereCities + whereOrderBooker);
							while (rsData.next()) {
								long id = rsData.getLong("id");
								int userId = rsData.getInt("created_by");
								long outletID = rsData.getLong("outlet_id");

								System.out.println("select created_on from common_outlets_visit_duration where visit_type=1 and outlet_id="
								+ outletID + " and " + mobileTImestamp + " and created_by=" + userId + " limit 1");
								ResultSet rs5 = s2
								.executeQuery("select created_on from common_outlets_visit_duration where visit_type=1 and outlet_id="
										+ outletID + " and " + mobileTImestamp + " and created_by=" + userId + " limit 1");
								if (rs5.first()) {
									Start_time = rs5.getTimestamp("created_on");
								}

								ResultSet rs6 = s2
								.executeQuery("select created_on from common_outlets_visit_duration where visit_type=2 and outlet_id="
										+ outletID + " and " + mobileTImestamp + " and created_by=" + userId + " order by id desc limit 1");
								if (rs6.first()) {
									End_time = rs6.getTimestamp("created_on");
								}

								long Shop_Time_milliseconds = (Start_time != null && End_time != null)
								? AlmoizDateUtils.getTimeDifference(Start_time, End_time)
								: 0;
								long Shop_Time = (Shop_Time_milliseconds / 1000);

								double outletLat = 0.0, outletLng = 0.0;
								String outlet = "", channel = "";
								ResultSet rsOutlat = s2.executeQuery(
								"select lat, lng, (select label from pep.pci_sub_channel where id=pic_channel_id) as channel, name from common_outlets where id="
										+ outletID);
								if (rsOutlat.first()) {
									outletLat = rsOutlat.getDouble("lat");
									outletLng = rsOutlat.getDouble("lng");
									outlet = rsOutlat.getString("name");
									channel = rsOutlat.getString("channel");
								}

								double distance = (rsData.getDouble("lat") == outletLat && rsData.getDouble("lng") == outletLng) ? 1
								: AlmoizFormulas.calculateHaversineDistance(rsData.getDouble("lat"), rsData.getDouble("lng"), outletLat,
										outletLng);
								/* Journey time */
								Date StartJourney = null;
								Date EndJourney = null;

								ResultSet rsStartJourney = s2.executeQuery("SELECT created_on FROM pep.common_outlets_visit_duration where "
								+ mobileTImestamp + " and created_by=" + userId + " and visit_type=1 order by id limit 1");
								if (rsStartJourney.first()) {
									StartJourney = rsStartJourney.getTimestamp("created_on");

								}

								ResultSet rsEndJourney = s2.executeQuery("SELECT created_on FROM pep.common_outlets_visit_duration where "
								+ mobileTImestamp + " and created_by=" + userId + " and visit_type=2 order by id desc limit 1");
								if (rsEndJourney.first()) {
									EndJourney = rsEndJourney.getTimestamp("created_on");

								}

								long Journey_Time_milliseconds = (StartJourney != null && EndJourney != null)
								? AlmoizDateUtils.getTimeDifference(StartJourney, EndJourney)
								: 0;

								long Journey_Time = (Journey_Time_milliseconds / 1000);
								/* insert Data in temporay table */

								R372Data r372Data = new R372Data();

								r372Data.id = id;
								r372Data.user_id = userId;
								r372Data.user = rsData.getString("user");
								r372Data.outlet_id = outletID;
								r372Data.outlet_name = outlet;
								r372Data.channel = channel;
								r372Data.visit_date = AlmoizDateUtils.getDisplayDateFormat(rsData.getTimestamp("created_on"));
								r372Data.distance = Math.round(distance);
								r372Data.start_time = ((Start_time) != null ? AlmoizDateUtils.getDisplayTimeFormat(Start_time) : "");
								r372Data.end_time = ((End_time) != null ? AlmoizDateUtils.getDisplayTimeFormat(End_time) : "");
								r372Data.shop_time = Shop_Time;
								r372Data.journey_time = Journey_Time;

								r372List.R372s.add(r372Data);
							}

							R372Data items[] = r372List.getR372();

							int count = 1;

							new com.pbc.reports.R372Excel().createPdf(Utilities.getCommonFilePath() + "/" + filename, r372List);

							for (R372Data data : items) {
							%>
							<tr>
								<td><%=count%></td>
								<td><%=data.user_id%></td>
								<td><%=data.user%></td>
								<td><%=data.outlet_id%></td>
								<td><%=data.outlet_name%></td>
								<td><%=data.channel%></td>
								<td><%=data.visit_date%></td>
								<td><%=data.distance%></td>
								<td><%=data.start_time%></td>
								<td><%=data.end_time%></td>
								<td><%=data.shop_time%></td>
								<td><%=data.journey_time%></td>
								<td><a data-inline="true" id="PhysicalStockAdjustmentSave"
									href="#"
									onClick="outletImages(<%=data.id%>,<%=data.outlet_id%>)"
									data-rel="popup" data-position-to="window"
									data-transition="pop">Outlet Images</a></td>
							</tr>

							<%
							count++;
							}
							%>



						</tbody>

					</table>
				</td>
			</tr>
		</table>

	</li>
</ul>
<div data-role="popup" id="R372" data-overlay-theme="a" data-theme="c"
	data-dismissible="true"
	style="min-width: 700px; overflow-y: auto; min-height: 600px; max-height: 600px"
	aclass="ui-corner-all">
	<div data-role="header" data-theme="a" class="ui-corner-top">
		<h3 style="text-align: center;">Outlet Images</h3>
	</div>
	<div data-role="content" data-theme="d"
		class="ui-corner-bottom ui-content">



		<div id="SearchContent"></div>

	</div>
</div>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>