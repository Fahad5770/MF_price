<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>
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
int FeatureID = 125;

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

//System.out.println("sDate "+StartDate);

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
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
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//OrderBooker

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
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and assigned_to in ("+OrderBookerIDs+") ";
}


SalesIndex si = new SalesIndex(StartDate,EndDate);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Order Booker Performance</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; width:20%;">&nbsp;</th>
							
							
							<th data-priority="1"  style="text-align:center; width:10%;" title="ECO = Unique Outlets Billed / Planned Universe">ECO</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="ECO >= 90% : 100 score">ECO Score</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="Bill Productivity = Total Cash Memos / Total Scheduled Outlets">Bill Productivity</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="Bill Productivity >= 65% : 100 score">Bill Productivity Score</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="Range Sold = Total Lines Sold / Total Cash Memos">Range Sold</th>
							<th data-priority="1"  style="text-align:center; width:10%;" title="Range Sold >= 15 lines : 100 score">Range Sell Score</th>
							<th data-priority="1"  style="text-align:center; width:10%;background-color:#F6F6F6;">Total Score</th>
							<th data-priority="1"  style="text-align:center; width:10%;background-color:#F6F6F6;">Status</th>
							
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						//creating temp table for sorting on Grades
						


						s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500),eco double(10,2),eco_score int,bill_productivity double(10,2),bill_productivity_score int,range_sold double(10,2),range_sold_score int,total_score double(10,2), status varchar(10))");
												
						ResultSet rs = s.executeQuery("SELECT distinct assigned_to,(select display_name from users u where u.id=dbpv.assigned_to) orderbooker_name FROM distributor_beat_plan_view dbpv where 1=1 "+WhereDistributors+WhereHOD+WhereRSM+OrderBookerIDsWher);
						double ECO=0;
						double BillProductivity=0;
						double RangeSelling=0;
						
						
						while(rs.next()){
							long OrderBookerID = rs.getLong("assigned_to");
							ECO = si.getECO(OrderBookerID);
							BillProductivity = si.getBillProductivity(OrderBookerID);
							RangeSelling = si.getRangeSelling(OrderBookerID);
							
							int ECOScore = 0;
							int BillProductivityScore = 0;
							int RangeSellingScore = 0;
							
							ECOScore=si.getECOScore(ECO);
							BillProductivityScore = si.getBillProductivityScore(BillProductivity);
							RangeSellingScore = si.getRangeSellingScore(RangeSelling);
							
							int TotalScore = ECOScore+BillProductivityScore+RangeSellingScore ;
							
							String OrderbookerName = rs.getLong("assigned_to")+" - "+rs.getString("orderbooker_name");
							
							s2.executeUpdate("insert into temp_orderbooker_performance values('"+OrderbookerName+"',"+ECO+","+ECOScore+","+BillProductivity+","+BillProductivityScore+","+RangeSelling+","+RangeSellingScore+","+TotalScore+",'"+si.getGrade(TotalScore)+"') ");
						}
						ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by status");
						while(rs1.next()){	
						%>
						<tr>
							<td><%=rs1.getString("name") %></td>							
							<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("eco")) %>%</td>
							<td style="text-align:center;"><%=rs1.getInt("eco_score") %></td>
							<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("bill_productivity")) %>%</td>
							<td style="text-align:center;"><%=rs1.getInt("bill_productivity_score") %></td>
							<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("range_sold")) %></td>
							<td style="text-align:center;"><%=rs1.getInt("range_sold_score") %></td>
							<td style="text-align:center;background-color:#F6F6F6;"><%=rs1.getInt("total_score") %></td>
							<td style="text-align:center;background-color:#F6F6F6;"><%if(!rs1.getString("status").equals("D")){%><%=rs1.getString("status") %><%}%></td>
						</tr>
						<%
						}
						
						s.executeUpdate("drop temporary table temp_orderbooker_performance");
						%>
						
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul>

<%
si.close();


s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>