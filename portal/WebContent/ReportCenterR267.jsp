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
<%@page import="java.util.Random"%>



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
int FeatureID = 335;

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
							<th data-priority="1"  style="text-align:center; width: 15% ">Outlet</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Order Booker</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Order#</th>
							<th data-priority="1"  style="text-align:center; width: 10% ">Order Size (Raw Cases)</th>
							<th data-priority="1"  style="text-align:center; width: 15%" nowrap="nowrap">Distance (m) from Outlet</th>
							<th data-priority="1"  style="text-align:center; width: 15%" nowrap="nowrap">Previous Order (Minutes Ago)</th>
							<th data-priority="1"  style="text-align:center; width: 15%" nowrap="nowrap">Next Order (Minutes After)</th>							
							<th data-priority="1"  style="text-align:center; width: 15%" nowrap="nowrap">Order Time</th>
													
					    </tr>
					  </thead>
					<tbody>
					<%
					
					
					
					double VerticalTotal[] = new double[9];
					
					String SQL = "SELECT  mo.id, mo.outlet_id, mo.lat, mo.lng, co.name outlet_name, mo.created_by, (select display_name from users where id=mo.created_by) orderbooker_name, co.lat outlet_lat, co.lng outlet_lng, "
							
							+ "(( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 ) AS distance "
							
							
							
					        + " FROM mobile_order mo, common_outlets co where mo.outlet_id=co.id and mo.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+WhereOrderBookerIDs+" and mo.outlet_id in (select distinct s.outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where curdate() between s.activated_on and s.deactivated_on and curdate() between sp.valid_from and sp.valid_to) having distance < 1000 order by created_by desc, mobile_timestamp desc ";
					
					System.out.println(SQL);
					
					String GraphPoints = "";
					int counter = 0;
					ResultSet rs = s.executeQuery(SQL);
					while(rs.next()){
						
						
						//Order Time 
						Date CurrentOrderTime =new Date();
						long CurrentOrderSeconds = 0;
						
						//out.print("SELECT mobile_timestamp, time_to_sec(time(mobile_timestamp)) FROM pep.mobile_order where id="+rs.getInt("id")+" and created_by="+rs.getLong("created_by"));
						
						ResultSet rs2 = s2.executeQuery("SELECT mobile_timestamp, time_to_sec(time(mobile_timestamp)) FROM pep.mobile_order where id="+rs.getInt("id")+" and created_by="+rs.getLong("created_by"));
						if(rs2.first()){
							
							
							CurrentOrderTime = rs2.getTimestamp("mobile_timestamp");
							CurrentOrderSeconds = rs2.getLong(2);
						}
						
						
						//Raw Cases 
						int RawCases =0;
						
						ResultSet rs3 = s2.executeQuery("SELECT sum(raw_cases) raw_cases FROM pep.mobile_order_products where id="+rs.getInt("id"));
						if(rs3.first()){
							RawCases = rs3.getInt("raw_cases");
							
						}
						
						
						//Previous Order
						double PrevOrderTime =0;
						double NextOrderTime =0;
						
						long LastOrderTime=0;
						long SecondLastOrderTime=0;
						long NextOrderTime2=0;
						
						Date MaxDateTime = new Date();
						
						
						//System.out.println("Last Query - "+"select TIME_TO_SEC(max(mobile_timestamp)) last from mobile_order where id="+rs.getInt("id"));
						
						/*
						
						Date MaxDate=new Date();
						Date MinDate=new Date();
						
						Date CurrentDate = new Date();
						
						
						ResultSet rs41 = s3.executeQuery("SELECT max(mobile_timestamp) Max_Date,min(mobile_timestamp) Min_Date FROM pep.mobile_order where mobile_timestamp between "+Utilities.getSQLDate(CurrentOrderTime)+" and "+Utilities.getSQLDateNext(CurrentOrderTime)+" and created_by="+rs.getLong("created_by")+" order by mobile_timestamp desc");
						if(rs41.first()){
							MaxDate = rs41.getTimestamp("Max_Date");
							MinDate = rs41.getTimestamp("Min_Date");
						}
						
						
						CurrentDate=MinDate;
						
						while(true){
							
							System.out.println(CurrentDate);
							
							
							CurrentDate = Utilities.getDateByDays(MinDate,1);
							if(CurrentDate==MaxDate){
								System.out.println("Yes Break!!!");
								break;
							}
							
							
							
						}
						
						
						*/

						ResultSet rs4 = s2.executeQuery("SELECT TIME_TO_SEC(max(mobile_timestamp)) last,max(mobile_timestamp) t  FROM pep.mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by="+rs.getLong("created_by")+" order by mobile_timestamp desc");
						if(rs4.first()){
							LastOrderTime = rs4.getInt("last");
							MaxDateTime=rs4.getTimestamp("t");
							
						}
						
						//out.print("select TIME_TO_SEC(time(mobile_timestamp)) secondlast from mobile_order where mobile_timestamp = (select max(mobile_timestamp) from mobile_order where created_by = created_by="+rs.getLong("created_by")+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mobile_timestamp < (select mobile_timestamp from mobile_order where id = "+rs.getInt("id")+")) and created_by = created_by="+rs.getLong("created_by")+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"");						
						ResultSet rs6 = s2.executeQuery("select TIME_TO_SEC(time(mobile_timestamp)) secondlast from mobile_order where mobile_timestamp = (select max(mobile_timestamp) from mobile_order where created_by = "+rs.getLong("created_by")+" and mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mobile_timestamp < (select mobile_timestamp from mobile_order where id = "+rs.getInt("id")+")) and created_by="+rs.getLong("created_by")+" and mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"");
						//ResultSet rs6 = s2.executeQuery("SELECT TIME_TO_SEC(mobile_timestamp)  secondlast FROM pep.mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by="+rs.getLong("created_by")+" and mobile_timestamp not in (SELECT max(mobile_timestamp)  FROM pep.mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by="+rs.getLong("created_by")+") order by mobile_timestamp desc");
						if(rs6.first()){
							SecondLastOrderTime = rs6.getInt("secondlast");
						}
						
						long LastOrderSeconds=0;
						if(SecondLastOrderTime!=0){
							 LastOrderSeconds = CurrentOrderSeconds - SecondLastOrderTime;
						}
						
						
						
						//PrevOrderTime=((LastOrderTime-SecondLastOrderTime)/60);
						
						//Next Order
						//double NextOrderTime =0;
						
						ResultSet rs7 = s2.executeQuery("SELECT TIME_TO_SEC(mobile_timestamp) secondlast  FROM pep.mobile_order where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and outlet_id="+rs.getLong("outlet_id")+" and mobile_timestamp> (select mobile_timestamp from mobile_order where id="+rs.getInt("id")+") order by mobile_timestamp limit 1");
						if(rs7.first()){
							NextOrderTime2 = rs7.getInt("secondlast");
						}
						
						NextOrderTime=((NextOrderTime2-LastOrderTime)/60);
						
						
						Random rand = new Random();

						int  n = rand.nextInt(50) + 1;
						
					%>
						<tr>
							<td><%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %>
								
							</td>	
							<td><%=rs.getLong("created_by") %> - <%=rs.getString("orderbooker_name") %> </td>													
					   		<td style="text-align:center;"><%=rs.getLong("id") %></td>
							<td style="text-align:right;"><%=RawCases %></td>
							<td style="text-align:right" nowrap="nowrap"><%= Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("distance"))%></td>
							<td style="text-align:right;"><%if(LastOrderSeconds!=0){%><%=LastOrderSeconds/60 %><%} %></td>
							<td style="text-align:right;"><%if(NextOrderTime>0){%><%=Utilities.getDisplayCurrencyFormatRounded(NextOrderTime) %><%} %></td>
							<!-- <td style="text-align:right;"><%=rand.nextInt(50) + 1 %></td>
							<td style="text-align:right;"><%=rand.nextInt(50) + 1 %></td> -->
							
							
							<td style="text-align:center;"><%=Utilities.getDisplayTimeFormat(CurrentOrderTime) %></td>
						</tr>
					<%
					
						counter++;
					//break;
					}
					%>
					
					
					
						
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





<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>