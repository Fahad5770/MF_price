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
<%@page import="java.util.Calendar"%>

<script src="js/lookups.js"></script>
<script>
	function redirect(url) {
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
int FeatureID = 499;

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

Date StartDate = (Date) session.getAttribute(UniqueSessionID + "_SR1StartDate");
Date EndDate = (Date) session.getAttribute(UniqueSessionID + "_SR1EndDate");

if (session.getAttribute(UniqueSessionID + "_SR1StartDate") == null) {
	StartDate = new Date();
	Calendar cal = Calendar.getInstance();
	cal.setTime(StartDate);
	cal.add(Calendar.MONTH, -1);
	StartDate = cal.getTime();
}

if (session.getAttribute(UniqueSessionID + "_SR1EndDate") == null) {
	EndDate = new Date();
}

//Distributor

boolean IsDistributorSelected = false;

int DistributorArrayLength = 0;

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors") != null) {
	SelectedDistributorsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors");
	IsDistributorSelected = true;
	DistributorArrayLength = SelectedDistributorsArray.length;

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
	WhereDistributors = "and sales_id in (select id from inventory_sales_invoices where distributor_id in ("
	+ DistributorIDs + ")) ";
	System.out.println(" JSP DistributorIDs : " + DistributorIDs);
}

//Region Filter
String RegionIDs = "";
long SelectedRegionsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRegion") != null) {
	SelectedRegionsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionsArray);
}

String whereRegions = (RegionIDs.length() > 0) ? " and region_id in (" + RegionIDs + ")" : "";
%>


<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">Ledger</li>
	<li>
		<table style="width: 100%; margin-top: -8px;" cellpadding="0"
			cellspacing="0">
			<tr>
				<td style="width: 100%;" valign="top">
					<table border="0"
						style="font-size: 13px; font-weight: 400; width: 100%;"
						cellpadding="0" cellspacing="0" class="GridWithBorder">
						<%
						if (DistributorIDs.equals("")) {
						%>
						<tr>
							<td style="text-align: left;">
								<p>
									<b>Please select Distributor for Report.</b>
								</p>
							</td>
						</tr>
						<%
						} else {
						double Balance = 0;
						double OpeningBalance = 0;
						double Debit = 0;
						double Credit = 0;
						%>
						<thead>
							<tr style="font-size: 11px;">
								<th style="text-align: center; width: 10%;">Outlet</th>
								<th style="text-align: center; width: 10%;">Distributor</th>
								<th style="text-align: center; width: 13%;">Debit</th>
								<th style="text-align: center; width: 13%;">Credit</th>
								<th style="text-align: center; width: 13%;">Balance</th>
							</tr>
						</thead>
						<tbody>
							<%
							System.out.println(
									"select outlet_id , distributor_id, (select name from common_distributors cd where cd.distributor_id=dbpv.distributor_id) as distributorName ,(select name from common_outlets co where co.id=dbpv.outlet_id) as outletName from distributor_beat_plan_view dbpv WHERE distributor_id IN ("
									+ DistributorIDs + ") " + whereRegions + " ");

							ResultSet rs = s.executeQuery(
									"select outlet_id , distributor_id, (select name from common_distributors cd where cd.distributor_id=dbpv.distributor_id) as distributorName ,(select name from common_outlets co where co.id=dbpv.outlet_id) as outletName from distributor_beat_plan_view dbpv WHERE distributor_id IN ("
									+ DistributorIDs + ") " + whereRegions + " ");

							while (rs.next()) {
								int OutletId = rs.getInt("outlet_id");
								int DistributorId = rs.getInt("distributor_id");
								String OutletIName = rs.getString("outletName");
								String DistributorName = rs.getString("distributorName");
							%>
							<tr>
								<%
								System.out.println("Select sum(debit) as debit , sum(credit) as credit FROM gl_transactions_sales where outlet_id="
										+ OutletId + " ");
								ResultSet rs2 = s2
										.executeQuery("Select sum(debit) as debit , sum(credit) as credit FROM gl_transactions_sales where outlet_id="
										+ OutletId + " ");
								while (rs2.next()) {
									Debit = rs2.getDouble("debit");
									Credit = rs2.getDouble("credit");
								}

								Balance = Debit - Credit;
								if (Balance != 0) {
								%>
								<td><%=OutletId + " - " + OutletIName%></td>
								<td><%=DistributorId + " - " + DistributorName%></td>
								<td><%=Debit%></td>
								<td><%=Credit%></td>
								<td><%=Balance%></td>
							</tr>
							<%
							}
							} // end while
							%>
						</tbody>
						<%
						} // end else
						%>
					</table>
				</td>
			</tr>
		</table>
	</li>
</ul>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>