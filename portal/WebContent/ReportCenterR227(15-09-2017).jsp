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
<%@page import="com.pbc.common.Region"%>
<script>

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
int FeatureID = 288;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
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

//System.out.print("Date = "+EndDate+"\n");

int Year = Utilities.getYearByDate(EndDate);
int Month = Utilities.getMonthNumberByDate(EndDate);

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

//region

Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);
String RegionIDs = UserAccess.getRegionQueryString(RegionObj);

long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";

if (RegionIDs.length() > 0){
	WhereRegion = " and cd.region_id in ("+RegionIDs+") ";	
}


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sampling Detail</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">

	<tr>
		<td style="padding-top: 10px; padding-bottom: 10px">
			<table width="15%">
				<tr>
					<td><b>From Date</b></td>
					<td><b>To Date</b></td>
				</tr>
				<tr>
					<td><%=Utilities.getDisplayDateFormat(StartDate)%></td>
					<td><%=Utilities.getDisplayDateFormat(EndDate)%></td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">					 
				<thead>
					<tr style="font-size:11px;">
						<th style="text-align: center">Year</th>
						<th style="text-align: center">Month</th>
						<th style="text-align: center">Date</th>
						<th style="text-align: center">Distributor</th>
						<th style="text-align: center">Fixed Sampling</th>
						<th style="text-align: center">Per Case Sampling</th>
						<th style="text-align: center">Adjustment</th>
						<th style="text-align: center">Net Amount</th>					 
					</tr>
				</thead>
				
				<tbody>
					
					<%
					//System.out.println("select date_format(sampling_month,'%Y') syear, date_format(sampling_month,'%m') smonth, sampling_month, distributor_id, fixed_sampling, percase_sampling, (ifnull(fixed_sampling,0)+ifnull(percase_sampling,0)-ifnull(net_amount,0)) adjustment, net_amount from (select sampling_month, distributor_id, sum(fixed_net_paid) fixed_sampling, sum(percase_net_paid) percase_sampling, sum(net_payable) net_amount from (SELECT sma.month sampling_month, sma.distributor_id, (SELECT sum(net_payable) FROM sampling_monthly_approval_fixed where approval_id = sma.approval_id) fixed_net_paid, (SELECT sum(net_payable) FROM sampling_monthly_approval_percase where approval_id = sma.approval_id) percase_net_paid, sma.net_payable, (SELECT name FROM common_distributors where distributor_id=sma.distributor_id) distributor_name FROM sampling_monthly_approval sma where sma.status_id = 1 and sma.status_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") tab group by sampling_month, distributor_id) tab2 where net_amount != 0 order by sampling_month, distributor_id");
					//ResultSet rs = s.executeQuery("select date_format(sampling_month,'%Y') syear, date_format(sampling_month,'%m') smonth, sampling_month, distributor_id, distributor_name, fixed_sampling, percase_sampling, (ifnull(fixed_sampling,0)+ifnull(percase_sampling,0)-ifnull(net_amount,0)) adjustment, net_amount from (select sampling_month, distributor_id, distributor_name, sum(fixed_net_paid) fixed_sampling, sum(percase_net_paid) percase_sampling, sum(net_payable) net_amount from (SELECT sma.month sampling_month, sma.distributor_id, (SELECT name FROM common_distributors where distributor_id=sma.distributor_id) distributor_name, (SELECT sum(net_payable) FROM sampling_monthly_approval_fixed where approval_id = sma.approval_id) fixed_net_paid, (SELECT sum(net_payable) FROM sampling_monthly_approval_percase where approval_id = sma.approval_id) percase_net_paid, sma.net_payable FROM sampling_monthly_approval sma where sma.status_id = 1 and sma.status_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") tab group by sampling_month, distributor_id) tab2 where net_amount != 0 order by sampling_month, distributor_id");
					
					ResultSet rs = s.executeQuery("SELECT "+
						   " DATE_FORMAT(sampling_month, '%Y') syear, "+
						   " DATE_FORMAT(sampling_month, '%m') smonth, "+
						    "sampling_month, "+
						    "distributor_id, "+
						    "distributor_name, "+
						    "outlet_id, "+
						    "outlet_name, "+
						    "fixed_sampling, "+
						    "percase_sampling, "+
						    "adjustment, "+
						    "net_amount "+
					"	FROM "+
					"	    (SELECT "+
					"	        sampling_month, "+
					"	            distributor_id, "+
					"	            distributor_name, "+
					"	            outlet_id, "+
					"	            outlet_name, "+
					"	            (fixed_net_paid) fixed_sampling, "+
					"	            (percase_net_paid) percase_sampling, "+
					"	            (net_payable) net_amount, "+
					"	            (adjustment) adjustment "+
					"	    FROM "+
					"	        (SELECT "+ 
					"	        sma.month sampling_month, "+
					"	            sma.outlet_id, "+
					"	            (SELECT "+
					"	                    name "+
					"	                FROM "+
					"	                    common_outlets "+
					"	                WHERE "+
					"	                    id = sma.outlet_id) outlet_name, "+
					"	            sma.distributor_id, "+
					"	            (SELECT "+
					"	                    name "+
					"	                FROM "+
					"	                    common_distributors "+
					"	                WHERE "+
					"	                    distributor_id = sma.distributor_id) distributor_name, "+
					"	            (SELECT "+
					"	                    fixed_company_share "+
					"	                FROM "+
					"	                    sampling s "+
					"	                WHERE "+
					"	                    s.sampling_id = sma.sampling_id "+
					"	                        AND sma.month BETWEEN s.fixed_valid_from AND s.fixed_valid_to) fixed_net_paid, "+
					"	            sma.adjustment, "+
					"	            IFNULL(sma.net_payable, 0) - IFNULL(sma.adjustment, 0) - IFNULL((SELECT "+ 
					"	                    fixed_company_share "+
					"	                FROM "+
					"	                    sampling s "+
					"	                WHERE "+
					"	                    s.sampling_id = sma.sampling_id "+
					"	                        AND sma.month BETWEEN s.fixed_valid_from AND s.fixed_valid_to), 0) percase_net_paid, "+
					"	            sma.net_payable "+
					"	    FROM "+
					"	        sampling_monthly_approval sma "+
					"	    WHERE "+
					"	        sma.status_id = 1 "+
					"	            AND sma.status_on BETWEEN '2017-07-01' AND '2017-07-31' "+
					"	            AND net_payable != 0) tab) tab2 "+
					"	WHERE "+
					"	    net_amount != 0");
					
					
					while(rs.next()){
						%>
						<tr style="font-size:11px;">
							<td style="text-align: right"><%=rs.getString("syear")%></td>
							<td style="text-align: right"><%=rs.getString("smonth")%></td>
							<td style="text-align: right"><%=Utilities.getDisplayDateFormat(rs.getDate("sampling_month"))%></td>
							<td style="text-align: left; padding-left: 20px"><%=rs.getString("distributor_id")+" - "+rs.getString("distributor_name")%></td>
							<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("fixed_sampling"))%></td>
							<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("percase_sampling"))%></td>
							<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("adjustment"))%></td>
							<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("net_amount"))%></td>
						</tr>
						<%
					}
					%>
					
				</tbody>
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