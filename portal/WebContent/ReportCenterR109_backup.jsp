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
int FeatureID = 108;

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


//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


if (WhereHOD.length() > 0 || WhereASM.length() > 0 || WhereTDM.length() > 0 || WhereSM.length() > 0){


SalesIndex si = new SalesIndex(StartDate,EndDate);
//out.print(StartDate + " " + EndDate);
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Order Booker KPIs</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:10px;">
							<th data-priority="1"  style="text-align:center; width:23%;">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="Percetange of orders taken before 10AM">% before 10AM</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="Percetange of orders taken after 6PM">% after 6PM</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="Average time of first order booked">Avg Time In</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#F7FFF7;" title="Average time of last order booked">Avg Time Out</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="No. of Outlets in PJP for selected date period">Outlets in PJP</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Converted Cases Sold (Excluding Promotions)">Converted Cases Sold</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FFFFE6;" title="Gross of Invoices">Gross Revenue</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FAFAF8;" title="ECO = Unique Outlets Ordered / Planned Unique Outlets">ECO</th>							
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FAFAF8;" title="Bill Productivity = Total Invoices / Planned Universe">Bill Productivity</th>							
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FAFAF8;" title="SKU/Bill = Total Lines Sold / Total Invoices (excluding promotions)">SKU/Bill</th>
							<th data-priority="1"  style="text-align:center; width:7%;background-color:#FAFAF8;" title="Pack Size/Bill = Total SKUs per Invoice / Total Invoices (excluding promotions)">Pack Size/Bill</th>
							
					
							
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						//creating temp table for sorting on Grades
						


						s.executeUpdate("create temporary table temp_orderbooker_performance (name varchar(500),eco double(10,2),bill_productivity double(10,2),range_sold double(10,2),sku_per_bill double(10,2), no_of_outlets int(11), invoice_amount double, converted_cases int(11),avg_time_in varchar(100), avg_time_out varchar(100),before_10 double(10,2), after_6 double(10,2))");
												
						ResultSet rs = s.executeQuery("select distinct dbpl.assigned_to, (select display_name from users u where u.id=dbpl.assigned_to) orderbooker_name, (SELECT count(*) FROM mobile_order where created_by = dbpl.assigned_to and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+") order_count from distributor_beat_plan_log dbpl where dbpl.log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and dbpl.assigned_to is not null "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+OrderBookerIDsWher+" having order_count > 0");
						//System.out.println("select distinct dbpl.assigned_to, (select display_name from users u where u.id=dbpl.assigned_to) orderbooker_name from distributor_beat_plan_log dbpl where dbpl.log_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+OrderBookerIDsWher);
						double ECO=0;
						double BillProductivity=0;
						double RangeSelling=0;
						double SKUPerBill=0;
						int NoOfOutlets;
						double InvoiceAmount = 0;
						int ConvertedCases = 0;
						
						
						String AvgTimeIn = "";
						String AvgTimeOut = "";
						double TotalOrdersBef10=0;
						double TotalOrderAft6=0;
						
						while(rs.next()){
							long OrderBookerID = rs.getLong("assigned_to");
							ECO = si.getECO(OrderBookerID);
							BillProductivity = si.getBillProductivity(OrderBookerID);
							RangeSelling = si.getRangeSelling(OrderBookerID);
							SKUPerBill = si.getSKUPerBill(OrderBookerID);
							
							NoOfOutlets = si.getPlannedUniverse(OrderBookerID);
							InvoiceAmount=  si.getTotalInvoiceAmount(OrderBookerID);
							
							ConvertedCases = si.getConvertedCasesSold(OrderBookerID);
							
							
							
							AvgTimeIn = Utilities.getDisplayTimeFormat(si.getAvgTimeIn(OrderBookerID));
							AvgTimeOut = Utilities.getDisplayTimeFormat(si.getAvgTimeOut(OrderBookerID));
							
							if (AvgTimeIn != null){
								AvgTimeIn = "'"+AvgTimeIn+"'";
							}else{
								AvgTimeIn = "''";
							}
							if (AvgTimeOut != null){
								AvgTimeOut = "'"+AvgTimeOut+"'";
							}else{
								AvgTimeOut = "''";
							}
							
							TotalOrdersBef10 = si.getTotalNumberOfOrdersBefore10(OrderBookerID);
							TotalOrderAft6 = si.getTotalNumberOfOrdersAfter6(OrderBookerID);
							
							String OrderbookerName = rs.getLong("assigned_to")+" - "+rs.getString("orderbooker_name");
							
							//if(AvgTimeIn == null){
								//s2.executeUpdate("insert into temp_orderbooker_performance values('"+OrderbookerName+"',"+ECO+","+BillProductivity+","+RangeSelling+","+SKUPerBill+","+NoOfOutlets+","+InvoiceAmount+","+ConvertedCases+",'','',"+TotalOrdersBef10+","+TotalOrderAft6+") ");
							//}else{
							
								
							s2.executeUpdate("insert into temp_orderbooker_performance values('"+OrderbookerName+"',"+ECO+","+BillProductivity+","+RangeSelling+","+SKUPerBill+","+NoOfOutlets+","+InvoiceAmount+","+ConvertedCases+","+ AvgTimeIn +","+AvgTimeOut+","+TotalOrdersBef10+","+TotalOrderAft6+") ");
							//}
							
						}
						ResultSet rs1 = s3.executeQuery("select * from temp_orderbooker_performance order by eco desc");
						while(rs1.next()){	
						%>
						<tr>
							<td><%=rs1.getString("name") %></td>							
							<td style="text-align:center;background-color:#F7FFF7;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("before_10")) %>%</td>
							<td style="text-align:center;background-color:#F7FFF7;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("after_6")) %>%</td>
							<td style="text-align:center;background-color:#F7FFF7;"><%=rs1.getString("avg_time_in")%></td>
							<td style="text-align:center;background-color:#F7FFF7;"><%=rs1.getString("avg_time_out")%></td>
							<td style="text-align:center;background-color:#FFFFE6;"><%=rs1.getInt("no_of_outlets") %></td>							
							<td style="text-align:center;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getInt("converted_cases")) %></td>
							<td style="text-align:right;background-color:#FFFFE6;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("invoice_amount")) %></td>

							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("eco")) %>%</td>							
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("bill_productivity")) %>%</td>							
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("range_sold")) %></td>
							<td style="text-align:center;background-color:#FAFAF8;"><%=Utilities.getDisplayCurrencyFormatRounded(rs1.getDouble("sku_per_bill")) %></td>
							
							
							
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

}else{
	%>
	<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
	<li data-role="list-divider" data-theme="a">Order Booker KPIs</li>
	<li>
	<span style="font-weight: normal;">Please select a Date Range and HOD to proceed</span>
	</li>
	</ul>
	<%
}

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>