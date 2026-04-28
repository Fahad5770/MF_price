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
<%@page import="org.apache.commons.lang3.time.DateUtils"%>

<%
long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 452;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

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



String WhereDistributors = "";






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


String WhereDistributors1 = "";
if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
	for (int i = 0; i < SelectedDistributorsArray.length; i++) {

		if (i == 0) {
	DistributorIDs += SelectedDistributorsArray[i];
		} else {
	DistributorIDs += ", " + SelectedDistributorsArray[i];
		}
	}
	//WhereDistributors = " and distributor_id in (" + DistributorIDs + ") ";

	WhereDistributors1 = " and outlet_id in (select id from common_outlets where distributor_id in (" + DistributorIDs
	+ ")) ";

	//out.print(WhereDistributors);
}


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}




String ASMIDs = "";
boolean IsASMSelected = false;

long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedASM") != null) {
	SelectedASMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedASM");

	IsASMSelected = true;
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}


String ASMDitsributorsIds="", whereASMDitsributorsIds="";
int count2=0;
if (ASMIDs.length() > 0) {
//System.out.println("select distinct distributor_id from distributor_beat_plan_view where asm_id in("+ASMIDs+")");
ResultSet rsDis = s.executeQuery("select distinct distributor_id from distributor_beat_plan_view where asm_id in("+ASMIDs+")");
while(rsDis.next()){
	if(count2==0){
		ASMDitsributorsIds = rsDis.getString("distributor_id");
	}else{
		ASMDitsributorsIds += ", "+rsDis.getString("distributor_id");
	}
	count2++;
}
//System.out.println("ASMDitsributorsIds : "+ASMDitsributorsIds);

whereASMDitsributorsIds = ASMDitsributorsIds.equals("") ? "" : " and distributor_id in ("+ASMDitsributorsIds+")";
}

count2=0;
String RSMDitsributorsIds="", whereRSMDitsributorsIds="";
if(RSMIDs.length() >0){
	//System.out.println("select distinct distributor_id from common_distributors where rsm_id in ("+RSMIDs+")");
	ResultSet rsDis = s.executeQuery("select distinct distributor_id from common_distributors where rsm_id in ("+RSMIDs+")");
	while(rsDis.next()){
		if(count2==0){
			RSMDitsributorsIds = rsDis.getString("distributor_id");
		}else{
			RSMDitsributorsIds += ", "+rsDis.getString("distributor_id");
		}
		count2++;
	}
	//System.out.println("RSMDitsributorsIds : "+RSMDitsributorsIds);
	whereRSMDitsributorsIds = RSMDitsributorsIds.equals("") ? "" : " and distributor_id in ("+RSMDitsributorsIds+")";
}

count2=0;
String SNDDitsributorsIds="", whereSNDDitsributorsIds="";
if(HODIDs.length() >0){
//	System.out.println("select distinct distributor_id from common_distributors where snd_id in ("+HODIDs+")");
	ResultSet rsDis = s.executeQuery("select distinct distributor_id from common_distributors where snd_id in ("+HODIDs+")");
	while(rsDis.next()){
		if(count2==0){
			SNDDitsributorsIds = rsDis.getString("distributor_id");
		}else{
			SNDDitsributorsIds += ", "+rsDis.getString("distributor_id");
		}
		count2++;
	}
	//System.out.println("SNDDitsributorsIds : "+SNDDitsributorsIds);
	whereSNDDitsributorsIds = SNDDitsributorsIds.equals("") ? "" : " and distributor_id in ("+SNDDitsributorsIds+")";
}

String whereFilters = " and distributor_id in ("+SNDDitsributorsIds+", "+RSMDitsributorsIds+", "+ASMDitsributorsIds+")";
if(WhereDistributors.equals("")){
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	 DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);
}

WhereDistributors = " and distributor_id in (" + DistributorIDs  +" )";

String whereToatlDistributors = WhereDistributors + whereRSMDitsributorsIds + whereASMDitsributorsIds + whereSNDDitsributorsIds;

//WhereDistributors = " and distributor_id in (" + DistributorIDs + (SNDDitsributorsIds.equals("") ? "" : ","+SNDDitsributorsIds) + (RSMDitsributorsIds.equals("") ? "" : ","+RSMDitsributorsIds) + (ASMDitsributorsIds.equals("") ? "" : ","+ASMDitsributorsIds ) +" )";

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

		<%--  <%=ASM+"/"+TSO+"/"+RSM%> --%>
		<%-- <%=ASM + "/" + DISPLAY_NAME %>
 --%>
		<ul data-role="listview" data-inset="true" class="ui-icon-alt"
			data-theme="d" data-divider-theme="c" data-count-theme="c"
			style="margin-top: -13px;">
			<li data-role="list-divider" data-theme="a">Market Visit
				Report/Work With Document</li>
			<li data-role="list-divider" data-theme="a">Date : <%=Utilities.getDisplayDateFormat(new java.util.Date())%></li>

			<%
			//  ArrayList<Integer> countVisited = new ArrayList<Integer>();

			int[] TotalCountVisited =  { 0,0,0,0,0,0,0,0,0,0};
			int[] TotalCountWithStock =  { 0,0,0,0,0,0,0,0,0,0};
			%>
			<td style="width: 30%" valign="top">



				<table border=0
					style="font-size: 13px; font-weight: 400; width: 100%"
					cellpadding="0" cellspacing="0" adata-role="table"
					class="GridWithBorder">

					<thead>
						<tr style="font-size: 11px;">
							<th>Sr#</th>
							<th>Shop Name</th>
							<th>PSR Visit</th>
							<th>TSO</th>
							<th>ASM</th>
							<th>Region</th>
							<th>Name</th>
							<th>Created On</th>
							<%
							ArrayList<Integer> lrbIds = new ArrayList<Integer>();
							ResultSet rs24 = s2.executeQuery("select  id,label from inventory_products_lrb_types");
							while (rs24.next()) {
								lrbIds.add(rs24.getInt("id"));
							%>
							<th><%=rs24.getString("label")%></th>
							<%
							}
							%>
							<th>LMP 800 Gm</th>
							<!-- product code :  10011336 Product id: 59 -->
							<th>SMP 350 Gm</th>
							<!-- product code :  10011343 Product id: 64 -->
							<th>Comments</th>
						</tr>
					</thead>

					<%
					String name="";
					ArrayList<Long> Users = new ArrayList<Long>();
					ResultSet rs233 = s.executeQuery("SELECT distinct created_by FROM mobile_order_sm_zero where  created_on between "
							+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " " + whereToatlDistributors
							);
					while (rs233.next()) {
						Users.add(rs233.getLong("created_by"));
					}

					// Stsrt Danamic Work
					int sr=1;
					int datcount=0;
					for (int y = 0; y < Users.size(); y++) {
						int[] LocalCountVisited = { 0,0,0,0,0,0,0,0,0,0};
						int[] LocalCountWithStock = { 0,0,0,0,0,0,0,0,0,0};
						
						System.out.println(
						"SELECT (select DISPLAY_NAME from users where id=created_by) as name,created_on,id, outlet_id, (SELECT name FROM common_outlets where id=o.outlet_id) outlet_name,mobile_order_no FROM mobile_order_sm_zero o where  created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " and created_by="
								+ Users.get(y) + " order by date(created_on) desc, mobile_timestamp desc");
						ResultSet rs22 = s.executeQuery(
						"SELECT (select DISPLAY_NAME from users where id=created_by) as name,created_on,id, outlet_id, (SELECT name FROM common_outlets where id=o.outlet_id) outlet_name,mobile_order_no FROM mobile_order_sm_zero o where  created_on between "
								+ Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " and created_by="
								+ Users.get(y) + " order by date(created_on) desc, mobile_timestamp desc");
						while (rs22.next()) {
							name = rs22.getString("name");
							long distributorId= 0;
							
							
							int outletId=rs22.getInt("outlet_id");
							int pjpId=0;
							
							ResultSet rsPJP = s2.executeQuery("select distinct id from distributor_beat_plan_view  where outlet_id="+outletId);
							if(rsPJP.first()) {
								pjpId = rsPJP.getInt("id");
							}
							
							String tso="";
							ResultSet rsNames = s2.executeQuery("select  (select DISPLAY_NAME from users where ID=asm_id) as TSO,distributor_id from distributor_beat_plan_view where id="+pjpId);
							if(rsNames.first()) {
								 tso=rsNames.getString("TSO");
								 distributorId=rsNames.getLong("distributor_id");
							}
							
							String asm="", region="";
							ResultSet rsASM = s2.executeQuery("select (select region_name from common_regions r where r.region_id=cd.region_id) as Region,(select DISPLAY_NAME from users where ID=rsm_id) as ASM from common_distributors cd where distributor_id="+distributorId);
							if(rsASM.first()) {
								asm=rsASM.getString("ASM");
								region=rsASM.getString("Region");
							}
							
							tso = tso == null ? "" : tso;
							asm = asm == null ? "" : asm;
					%>
					<tr>
						<td><%=sr %></td>
						<td><%=rs22.getString("outlet_id") + " - " + rs22.getString("outlet_name")%></td>
						<td>Yes</td>
						<td><%=tso %></td>
						<td><%=asm %></td>
						<td><%=region %></td>
						<td><%=name %></td>
						<td><%=Utilities.getDisplayDateTimeFormatUniversal(rs22.getTimestamp("created_on"))%></td>
						<%
						
						int visitcalculate = 0;
						int l=0;
						for(int u=0; u<lrbIds.size(); u++){
							TotalCountVisited[visitcalculate] = TotalCountVisited[visitcalculate] + 1;
							LocalCountVisited[l] = LocalCountVisited[l] + 1;
							int visit = 0;
							//	System.out.println("SELECT group_concat(brand_label,'-',package_label) as product_name FROM mobile_retailer_sm_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where ipv.is_visible=1 and lrb_type_id="+rs241.getInt("id")+" and   mrs.order_no="+rs22.getLong("mobile_order_no")+" and outlet_id="+ rs22.getString("outlet_id"));
							ResultSet rs111 = s3.executeQuery(
							"SELECT mrs.product_id FROM mobile_retailer_sm_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where ipv.is_visible=1 and lrb_type_id="
									+ lrbIds.get(u) + " and   mrs.order_no=" + rs22.getLong("mobile_order_no") + " and outlet_id=" + rs22.getString("outlet_id"));
							if (rs111.first()) {
								visit = (rs111.getInt("product_id") > 0) ? 1 : 0;
								%>
								<td><%=visit%></td>
								<%
							}else{
								%>
								<td>0</td>
								<%
							}
							if (visit == 1) {
								//System.out.println("visitcalculate " + visitcalculate);
								LocalCountWithStock[l] = LocalCountWithStock[l] + 1;
								TotalCountWithStock[visitcalculate] = TotalCountWithStock[visitcalculate] + 1;
							}
							visitcalculate++;
							l++;
						}
						// For product LMP 800 Gm
						int visit=0;
						ResultSet rs122 = s2.executeQuery(
								"SELECT mrs.product_id FROM mobile_retailer_sm_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where ipv.is_visible=1 and sap_code=10011336 and mrs.product_id=59 and lrb_type_id=6  and   mrs.order_no=" + rs22.getLong("mobile_order_no") + " and outlet_id="+ rs22.getString("outlet_id") );
						if(rs122.first()){
							TotalCountVisited[8] = TotalCountVisited[8] + 1;
							LocalCountVisited[8] = LocalCountVisited[8] + 1;
							visit = (rs122.getInt("product_id") > 0) ? 1 : 0;
						}
						if (visit == 1) {
							//System.out.println("visitcalculate " + visitcalculate);
							LocalCountWithStock[8] = LocalCountWithStock[8] + 1;
							TotalCountWithStock[8] = TotalCountWithStock[8] + 1;
						}
						%>
						<td><%=visit %></td>
						<%
						// For product SMP 350 Gm
						visit=0;
						ResultSet rs123 = s2.executeQuery(
								"SELECT mrs.product_id FROM mobile_retailer_sm_stock mrs join inventory_products_view ipv on mrs.product_id=ipv.product_id where ipv.is_visible=1 and  sap_code=10011343 and mrs.product_id=64 and lrb_type_id=5 and   mrs.order_no=" + rs22.getLong("mobile_order_no") + " and outlet_id="+ rs22.getString("outlet_id") );
						if(rs123.first()){
							TotalCountVisited[9] = TotalCountVisited[9] + 1;
							LocalCountVisited[9] = LocalCountVisited[9] + 1;
							visit = (rs123.getInt("product_id") > 0) ? 1 : 0;
						}
						if (visit == 1) {
							//System.out.println("visitcalculate " + visitcalculate);
							LocalCountWithStock[9] = LocalCountWithStock[9] + 1;
							TotalCountWithStock[9] = TotalCountWithStock[9] + 1;
						}
						%>
						<td><%=visit %></td>
						<%
						// Comments Query
						ResultSet rsComments = s2
								.executeQuery("SELECT moz.comments from mobile_order_sm_zero moz where moz.id=" + rs22.getInt("id"));
						%>
						<td>
							<%
							out.print(
									(rsComments.first() ? (rsComments.getString("comments") != null) ? rsComments.getString("comments") : "" : ""));
							%>
						</td>

					</tr>
					<%
					sr++;
					}
						int dateVisits=0;
					//	System.out.println("select count(distinct MYDATE ) as c from (SELECT date(created_on) AS MYDATE, created_on FROM mobile_order_sm_zero where  created_on between  "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) +" and created_by="+Users.get(y)+" ) t");
						ResultSet rsDateCount = s.executeQuery("select count(distinct MYDATE ) as c from (SELECT date(created_on) AS MYDATE, created_on FROM mobile_order_sm_zero where  created_on between  "+Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) +" and created_by="+Users.get(y)+" ) t");
					if(rsDateCount.first()){
						 dateVisits= rsDateCount.getInt("c");
					}
					datcount += dateVisits;
						%>
						<tr style="font-size: 11px;">
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td style="font-size: 12px; background-color: #F7EFC5;"><%=name %></td>
						<th style="font-size: 12px; background-color: yellow;">No of Visits</th>
						<%
						for(int i=0; i<10; i++){
							%>
							<td style="font-size: 12px; background-color: yellow;"><%=dateVisits%></td>
							<%
						}
						%>
						<td></td>

					</tr>
						<tr style="font-size: 11px;">
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<th style="font-size: 12px; background-color: yellow;">No of Shops Visited</th>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[0]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[1]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[2]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[3]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[4]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[5]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[6]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[7]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[7]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountVisited[7]%></td>
						<td></td>

					</tr>
					<tr style="font-size: 11px;">
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td style="font-size: 12px; background-color: yellow;">No of
							Shops with stock availability</th>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[0]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[1]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[2]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[3]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[4]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[5]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[6]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[7]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[8]%></td>
						<td style="font-size: 12px; background-color: yellow;"><%=LocalCountWithStock[9]%></td>
						<td></td>
					</tr>
					<tr style="font-size: 11px;">
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td style="font-size: 12px; background-color: yellow;">Availability %age</th>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[0] != 0) ? Math.round((LocalCountWithStock[0] * 100) / LocalCountVisited[0]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[1] != 0) ? Math.round((LocalCountWithStock[1] * 100) / LocalCountVisited[1]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[2] != 0) ? Math.round((LocalCountWithStock[2] * 100) / LocalCountVisited[2]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[3] != 0) ? Math.round((LocalCountWithStock[3] * 100) / LocalCountVisited[3]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[4] != 0) ? Math.round((LocalCountWithStock[4] * 100) / LocalCountVisited[4]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[5] != 0) ? Math.round((LocalCountWithStock[5] * 100) / LocalCountVisited[5]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[6] != 0) ? Math.round((LocalCountWithStock[6] * 100) / LocalCountVisited[6]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[7] != 0) ? Math.round((LocalCountWithStock[7] * 100) / LocalCountVisited[7]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[7] != 0) ? Math.round((LocalCountWithStock[8] * 100) / LocalCountVisited[7]) : 0)%>%</td>
						<td style="font-size: 12px; background-color: yellow;"><%=((LocalCountVisited[7] != 0) ? Math.round((LocalCountWithStock[9] * 100) / LocalCountVisited[7]) : 0)%>%</td>
						<td></td>
					</tr>
						<%
						
					} // End of Main
					%>
					<tr style="font-size: 11px;">
					<td>&nbsp;</td>
<td>&nbsp;</td>	<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>				</tr>
<tr style="font-size: 11px;">
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th style="font-size: 12px; background-color: yellow;">Total No of Shops Visits</th>
						<%
						for(int i=0; i<10; i++){
							%>
							<td style="font-size: 12px; background-color: yellow;"><%=datcount%></td>
							<%
						}
						%>
						<th></th>

					</tr>
					<tr style="font-size: 11px;">
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th style="font-size: 12px; background-color: yellow;">Total No of
							Shops Visited</th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[0]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[1]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[2]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[3]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[4]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[5]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[6]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[7]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[7]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountVisited[7]%></th>
						<th></th>

					</tr>
					<tr style="font-size: 11px;">
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th style="font-size: 12px; background-color: yellow;">Total No of
							Shops with stock availability</th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[0]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[1]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[2]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[3]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[4]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[5]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[6]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[7]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[8]%></th>
						<th style="font-size: 12px; background-color: yellow;"><%=TotalCountWithStock[9]%></th>
						<th></th>
					</tr>
					<tr style="font-size: 11px;">
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th style="font-size: 12px; background-color: yellow;">Total Availability
							%age</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[0] != 0) ? Math.round((TotalCountWithStock[0] * 100) / TotalCountVisited[0]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[1] != 0) ? Math.round((TotalCountWithStock[1] * 100) / TotalCountVisited[1]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[2] != 0) ? Math.round((TotalCountWithStock[2] * 100) / TotalCountVisited[2]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[3] != 0) ? Math.round((TotalCountWithStock[3] * 100) / TotalCountVisited[3]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[4] != 0) ? Math.round((TotalCountWithStock[4] * 100) / TotalCountVisited[4]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[5] != 0) ? Math.round((TotalCountWithStock[5] * 100) / TotalCountVisited[5]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[6] != 0) ? Math.round((TotalCountWithStock[6] * 100) / TotalCountVisited[6]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[7] != 0) ? Math.round((TotalCountWithStock[7] * 100) / TotalCountVisited[7]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[7] != 0) ? Math.round((TotalCountWithStock[8] * 100) / TotalCountVisited[7]) : 0)%>%</th>
						<th style="font-size: 12px; background-color: yellow;"><%=((TotalCountVisited[7] != 0) ? Math.round((TotalCountWithStock[9] * 100) / TotalCountVisited[7]) : 0)%>%</th>
						<th></th>
					</tr>
				</table>
		</ul>
		<%

		%>

	</tr>
</table>
<table style="width: 91%">

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