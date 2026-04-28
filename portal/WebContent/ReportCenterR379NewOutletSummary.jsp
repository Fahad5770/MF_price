<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.text.DateFormatSymbols"%>


<style>
td {
	font-size: 8pt;
}

th {
	font-size: 9pt;
}

#map {
	width: 100%;
	height: 400px;
	margin-top: 10px;
}
</style>

<script type="text/javascript" src="js/Report238.js"></script>

<%
long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 503;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();

ds.createConnection();
Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();

int CensusID = Utilities.parseInt(request.getParameter("CensusID"));

System.out.println("CensusID : " + CensusID);

String WhereCensusID = "";

//Distributor

boolean IsDistributorSelected = false;
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors") != null) {
	SelectedDistributorsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors");
	IsDistributorSelected = true;
} else {
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];

	for (int x = 0; x < UserDistributor.length; x++) {
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";

if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
	for (int i = 0; i < SelectedDistributorsArray.length; i++) {

		if (i == 0) {
	DistributorIDs += SelectedDistributorsArray[i];
		} else {
	DistributorIDs += ", " + SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and census_distributor_id in (" + DistributorIDs + ") ";
}

//RSM

String RSMIDs = "";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRSM") != null) {
	SelectedRSMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0) {
	WhereDistributors = " and dbpauov.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("
	+ RSMIDs + ")) ";
}

//Outlet Type

String OutletTypes = "";
String SelectedOutletTypeArray[] = {};
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOutletType") != null) {
	SelectedOutletTypeArray = (String[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOutletType");
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll = "";

for (int i = 0; i < SelectedOutletTypeArray.length; i++) {
	if (SelectedOutletTypeArray[i].equals("Discounted - All")) {
		WhereDiscountedAll = " and co.id in (select outlet_id from sampling where active = 1) ";
	}

	if (SelectedOutletTypeArray[i].equals("Discounted - Fixed")) {
		WhereDiscountedFixed = " and co.id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}

	if (SelectedOutletTypeArray[i].equals("Discounted - Per Case")) {
		WhereDiscountedPerCase = " and co.id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}

	if (SelectedOutletTypeArray[i].equals("Non Discounted")) {
		WhereNonDiscountedAll = " and co.id not in (select outlet_id from sampling where active = 1) ";
	}

	if (SelectedOutletTypeArray[i].equals("Active")) {
		WhereActive = " and co.is_active=1 ";
	}

	if (SelectedOutletTypeArray[i].equals("Deactivated")) {
		WhereDeactivated = " and co.is_active=0 ";
	}
}
%>
<script>
	
function ApproveOutlet(ID) {
	// Disable approve button to prevent multiple clicks
	$("#approvebutton").addClass("ui-disabled");

	$.ajax({
		url: "outlet/ShelfRentOutletRequestApprove2",
		type: "POST",
		data: $('#general_tab').serialize(),
		dataType: "json",
		success: function (json) {
			$.mobile.loading("hide");
			$("#approvebutton").removeClass("ui-disabled");

			if (json.success === "true") {
				alert("Data has been updated successfully.");

				// Remove buttons after approval
				$("#approvebutton, #savebutton, #declinebutton").remove();
				
				// Refresh page
				location.reload(); // Reloads the current page
			} else {
				alert("Data could not be saved.");
			}
		},
		error: function () {
			$.mobile.loading("hide");
			$("#approvebutton").removeClass("ui-disabled");
			alert("Server could not be reached. Please try again later.");
		}
	});
}


function DeclineOutlet(ID) {
	// Disable decline button to prevent multiple clicks
	$("#declinebutton").addClass("ui-disabled");

	// Set decline flag value
	$("#declineFlag").val(ID);

	$.ajax({
		url: "outlet/ShelfRentOutletRequestDecline",
		type: "POST",
		data: $('#general_tab').serialize(),
		dataType: "json",
		success: function (json) {
			$.mobile.loading("hide");
			$("#declinebutton").removeClass("ui-disabled");

			if (json.success === "true") {
				alert("Outlet declined successfully.");

				// Optionally remove buttons
				$("#approvebutton, #savebutton, #declinebutton").remove();

				// Refresh the page after decline
				location.reload();
			} else {
				alert("Data could not be saved.");
			}
		},
		error: function () {
			$.mobile.loading("hide");
			$("#declinebutton").removeClass("ui-disabled");
			alert("Server could not be reached. Please try again later.");
		}
	});
}
</script>


<form id="general_tab">
	<ul data-role="listview" data-inset="true"
		style="font-size: 10pt; font-weight: normal; margin-top: -13px;"
		data-icon="false">


		<li data-role="list-divider" data-theme="a">Outlet Summary</li>
		<%
			if (CensusID != 0) {
		%>

		<li>
			<%
			
			String distributorName = "";
			String outletName = "";
			String ownerName = "";
			String outletAddress = "";
			String regionName = "";
			String cityName = "";
			int fromMonth = 0;
			int toMonth = 0;
			double lat = 0.0;
			double lng = 0.0;
			double shelfRent = 0.0;
			
			System.out.println("select co.id,cd.name as distributorName,co.name,co.cache_contact_name,co.address,cr.region_name as regionName,cc.label as cityName,co.lat,co.lng,rd.shelf_rent,rd.to_month,rd.from_month from common_outlets co join rental_discount rd on co.id = rd.outlet_id join common_cities cc on cc.id = co.city_id join common_distributors cd on co.distributor_id = cd.distributor_id join common_regions cr on cr.region_id = co.region_id where co.id = "+ CensusID);
			
			ResultSet rs = s.executeQuery("select co.id,cd.name as distributorName,co.name,co.cache_contact_name,co.address,cr.region_name as regionName,cc.label as cityName,co.lat,co.lng,rd.shelf_rent,rd.to_month,rd.from_month from common_outlets co join rental_discount rd on co.id = rd.outlet_id join common_cities cc on cc.id = co.city_id join common_distributors cd on co.distributor_id = cd.distributor_id join common_regions cr on cr.region_id = co.region_id where co.id = "+ CensusID);

			while (rs.next()) {
				distributorName = rs.getString("distributorName");
				outletName = rs.getString("name");
				ownerName = rs.getString("cache_contact_name");
				outletAddress = rs.getString("address");
				regionName = rs.getString("regionName");
				cityName = rs.getString("cityName");
				lat = rs.getDouble("lat");
				lng = rs.getDouble("lng");
				shelfRent = rs.getInt("shelf_rent");
				fromMonth = rs.getInt("from_month");
				toMonth = rs.getInt("to_month");

			}
 
			    
			%> 
			<input type="hidden" name="ID" value="<%=CensusID%>">
			<input type="hidden" name="lat" value="<%=lat%>">
			<input type="hidden" name="lng" value="<%=lng%>">
		
			<table style="width: 100%; margin-top: 15px;">
				<tr>
				</tr>
				<tr style="text-align: left">
					<th style="width: 25%">Distributor</th>

				</tr>
				<tr>
					<td colspan="3" style="width: 25%"><input type="text"
						placeholder="Distributor Name" id="DistributorName" name="DistributorName"
						value="<%=distributorName%>" readonly></td>

				</tr>
				<tr>
				</tr>
				<tr style="text-align: left">
					<th style="width: 25%">Outlet Name</th>
					<th style="width: 25%">Owner Name</th>
					<th>Outlet Address</th>

				</tr>

				<tr>
					<td  style="width: 25%"><input type="text"
						placeholder="Outlet Name" id="OutletName" name="OutletName"
						value="<%=outletName%>"></td>
					<td style="width: 25%"><input type="text"
						placeholder="Owner Name" id="OwnerName" name="OwnerName"
						value="<%=ownerName%>"></td>
					<td><input type="text" placeholder="Outlet Address"
						id="OutletAddress" name="OutletAddress" value="<%=outletAddress%>"></td>

				</tr>
				<tr>
				</tr>
				<tr>
				</tr>
				<tr style="text-align: left">
					
					<th>City</th>
					<th>Lat</th>
					<th>Lng</th>

				</tr>
				<tr>
					<td><input type="text" placeholder="Area" id="OutletArea"
						name="OutletArea" value="<%=cityName%>"></td>
					<td><input type="text" placeholder="Sub Area"
						id="OutletSubArea" name="OutletSubArea" value="<%=lat%>"></td>
						<td><input type="text" placeholder="City"
						id="City" name="City" value="<%=lng%>" Readonly></td>
					
				</tr>
				
				<%
				String[] months = new DateFormatSymbols().getMonths();
				String fromMonthName = (fromMonth >= 1 && fromMonth <= 12) ? months[fromMonth - 1] : "";
				String toMonthName = (toMonth >= 1 && toMonth <= 12) ? months[toMonth - 1] : "";
				%>
				
			<tr style="text-align: left">
					
					<th>Rent</th>
					<th>From Month</th>
					<th>To Month</th>

				</tr>
				<tr>
					<td><input type="text" placeholder="Area" id="OutletArea"
						name="OutletArea" value="<%=shelfRent%>"></td>
						<td><input type="text" placeholder="Area" id="OutletArea"
						name="OutletArea" value="<%=fromMonthName%>"></td>
						<td><input type="text" placeholder="Area" id="OutletArea"
						name="OutletArea" value="<%=toMonthName%>"></td>
					
						
					
				</tr>
				
			</table>
 
				<div id="SuccessData" style="width:30%;margin-left:40%"></div>
	
		
			
			<table style="margin-top:30px">
			<tr style="text-align: right">
<%

	%>

	<%
	
//}else{
	
%>
<td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="b" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="approvebutton"
						onclick='ApproveOutlet("<%=CensusID%>")'>Approve</a></td>
						
						<td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="a" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="declinebutton"
						onclick='DeclineOutlet("<%=CensusID%>")'>Decline</a></td>

<%
	
	
//}


%>
					
				</tr>
			
			</table> 
			<%
 	}
 %>







			<div data-role="footer" class="ui-bar" data-theme="b"></div>
	</ul>


</form>






<%
	s3.close();
s2.close();
s.close();

ds.dropConnection();
%>