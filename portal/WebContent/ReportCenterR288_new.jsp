<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>

<%
long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 358;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
/* long OrderID = Utilities.parseLong(request.getParameter("OrderID"));
System.out.print("OrderID"+OrderID); */

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}
//
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
boolean IsOutletSelected = false;
String OutletIds = "";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOutlets") != null) {
	IsOutletSelected = true;
	SelectedOutletArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0) {
	WhereOutlets = " and outlet_id in (" + OutletIds + ") ";
}

boolean IsOrderBookerSelected = false;

int OrderBookerArrayLength = 0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers") != null) {
	SelectedOrderBookerArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOrderBookers");

	IsOrderBookerSelected = true;
	OrderBookerArrayLength = SelectedOrderBookerArray.length;
}

String OrderBookerIDs = "";
if (SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0) {
	for (int i = 0; i < SelectedOrderBookerArray.length; i++) {
		if (i == 0) {
	OrderBookerIDs += SelectedOrderBookerArray[i];
		} else {
	OrderBookerIDs += ", " + SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher = "";
if (OrderBookerIDs.length() > 0) {
	OrderBookerIDsWher = " and created_by in (" + OrderBookerIDs + ") ";
}

//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected = false;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors") != null) {
	IsOrderBookerSelected = true;
	SelectedDistributorsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors");
	IsDistributorSelected = true;
} else {
}

String DistributorIDs = "";
String WhereDistributors = "";

String WhereDistributors1 = "";
if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
	for (int i = 0; i < SelectedDistributorsArray.length; i++) {

		if (i == 0) {
	DistributorIDs += SelectedDistributorsArray[i];
		} else {
	DistributorIDs += ", " + SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and distributor_id in (" + DistributorIDs + ") ";

	WhereDistributors1 = " and outlet_id in (select id from common_outlets where distributor_id in (" + DistributorIDs
	+ ")) ";

	//out.print(WhereDistributors);
}

String ASMIDs = "";
boolean IsASMSelected = false;

long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedASM") != null) {
	SelectedASMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedASM");

	IsASMSelected = true;
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0) {
	WhereASM = " and created_by in (" + ASMIDs + ") ";
}
%>


<script type="text/javascript">
	function redirect(url) {
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}

	function DistReportsOrderBookerOrderListOutletDashboardRedirect(OutletID1) {
		//alert(OutletID1);
		$("#DistReportsOrderBookerOrderListOutletID").val(OutletID1);
		document.getElementById("DistReportsOrderBookerOrderListFormID")
				.submit();
	}
</script>

<table border="0" style="width: 100%">
	<tr>


		<td style="width: 30%" valign="top">



			<ul data-role="listview" data-inset="true" class="ui-icon-alt"
				data-theme="d" data-divider-theme="c" data-count-theme="c"
				style="margin-top: -13px;">
				<li data-role="list-divider" data-theme="a">Orders_new</li>

				<%
				if (IsASMSelected || IsOutletSelected) {
				%>

				<%
				System.out.println(
						"SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order_sm.outlet_id) outlet_name, net_amount, created_on, mobile_timestamp, status_type_id FROM mobile_order_sm where status_type_id in (1,2,3)  "
						+ WhereDistributors + WhereASM + " and created_on between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate) + " " + WhereOutlets
						+ " order by date(created_on) desc, mobile_timestamp desc");

				int OrderID = 0;
				Date MobileTimestamp;
				ResultSet rs22 = s.executeQuery(
						"SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order_sm.outlet_id) outlet_name, net_amount, created_on, mobile_timestamp, status_type_id FROM mobile_order_sm where status_type_id in (1,2,3)  "
						+ WhereDistributors + WhereASM + " and created_on between " + Utilities.getSQLDate(StartDate) + " and "
						+ Utilities.getSQLDateNext(EndDate) + " " + WhereOutlets
						+ " order by date(created_on) desc, mobile_timestamp desc");
				int counter = 0;
				while (rs22.next()) {

					OrderID = rs22.getInt("id");
					MobileTimestamp = rs22.getTimestamp("mobile_timestamp");
					System.out.print("OrderID : " + OrderID);
					

					 

				}
				%>

				<%
				}else{  // end of IsOutletSelected condition %>
				
				<li style="padding:20px;">Please select an SO or Outlet</li>
				<%} %>

			</ul> <%

 %>
		
	</tr>
</table>
<form id="DistReportsOrderBookerOrderListFormID"
	name="DistReportsOrderBookerOrderListFormID"
	action="OutletDashboard.jsp" method="POST" data-ajax="false"
	target="_blank">
	<input type="hidden" name="outletID"
		id="DistReportsOrderBookerOrderListOutletID" />
</form>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>