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
var SyncVerticalTotals = new Array();

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
int FeatureID = 124;

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
	WhereOrderBookerIDs =" and created_by in ("+OrderBookerIDs+") ";
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

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
	<li data-role="list-divider" data-theme="a">Quick View</li>
	<li>
	
	<div id="container56985235446" style="min-width: 310px; height: 300px; margin: 0 auto"></div>
	
	</li>
</ul>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
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
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">Before 06AM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">06AM - 08AM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">08AM - 10AM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">10AM - 12PM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">12PM - 02PM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">02PM - 04PM</th>
							<th data-priority="1"  style="text-align:center; width: 7% " nowrap="nowrap">04PM - 06PM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">06PM - 08PM</th>
							<th data-priority="1"  style="text-align:center; width: 7%" nowrap="nowrap">After 08PM</th>
							<th data-priority="1"  style="text-align:center; width: 7%">Total</th>
													
					    </tr>
					  </thead>
					<tbody>
					<%
					double VerticalTotal[] = new double[9];
					
					double SyncVerticalTotal[] = new double[9];
					
					String SQL = "SELECT created_by, (select display_name from users where id=created_by) orderbooker_name "
							
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '00:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '5:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'before_6'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '6:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '7:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_6_8'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '8:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '9:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_8_10'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '10:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '11:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_10_12'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '12:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '13:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_12_14'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '14:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '15:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_14_16'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '16:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '17:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_16_18'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '18:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '19:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'slot_18_20'"
							+ ", sum(if(mobile_timestamp between date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '20:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(mobile_timestamp,'%Y-%m-%d'), ' ', '23:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'overnight'"
							
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '00:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '5:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_before_6'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '6:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '7:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_6_8'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '8:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '9:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_8_10'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '10:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '11:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_10_12'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '12:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '13:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_12_14'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '14:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '15:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_14_16'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '16:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '17:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_16_18'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '18:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '19:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_slot_18_20'"
							+ ", sum(if(created_on between date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '20:00:00'), '%Y-%m-%d %H:%i:%s') and  date_format(concat( date_format(created_on,'%Y-%m-%d'), ' ', '23:59:59'), '%Y-%m-%d %H:%i:%s') ,1,0)) as 'sync_overnight'"

							
					        + " FROM mobile_order"
					        + " where mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and status_type_id in (1,2) "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+WhereOrderBookerIDs+" group by created_by";
					
					//System.out.println(SQL);
					
					ResultSet rs = s.executeQuery(SQL);
					while(rs.next()){
						double HoriToal=0;
						HoriToal = 		rs.getDouble("before_6")+rs.getDouble("slot_6_8")+rs.getDouble("slot_8_10")+rs.getDouble("slot_10_12")+rs.getDouble("slot_12_14")
									+	rs.getDouble("slot_14_16")+rs.getDouble("slot_16_18")+rs.getDouble("slot_18_20")+rs.getDouble("overnight");
						
						VerticalTotal[0]+=rs.getDouble("before_6");
						VerticalTotal[1]+=rs.getDouble("slot_6_8");
						VerticalTotal[2]+=rs.getDouble("slot_8_10");
						VerticalTotal[3]+=rs.getDouble("slot_10_12");
						VerticalTotal[4]+=rs.getDouble("slot_12_14");
						
						VerticalTotal[5]+=rs.getDouble("slot_14_16");
						VerticalTotal[6]+=rs.getDouble("slot_16_18");
						VerticalTotal[7]+=rs.getDouble("slot_18_20");
						VerticalTotal[8]+=rs.getDouble("overnight");
						
						
						SyncVerticalTotal[0]+=rs.getDouble("sync_before_6");
						SyncVerticalTotal[1]+=rs.getDouble("sync_slot_6_8");
						SyncVerticalTotal[2]+=rs.getDouble("sync_slot_8_10");
						SyncVerticalTotal[3]+=rs.getDouble("sync_slot_10_12");
						SyncVerticalTotal[4]+=rs.getDouble("sync_slot_12_14");
						
						SyncVerticalTotal[5]+=rs.getDouble("sync_slot_14_16");
						SyncVerticalTotal[6]+=rs.getDouble("sync_slot_16_18");
						SyncVerticalTotal[7]+=rs.getDouble("sync_slot_18_20");
						SyncVerticalTotal[8]+=rs.getDouble("sync_overnight");
						
						
					%>
						
						
						<tr>
							<td><%=rs.getLong("created_by") %> - <%=rs.getString("orderbooker_name") %></td>							
							<td style="text-align:right"><%if(rs.getDouble("before_6")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("before_6")) %><%}else{ %>-<%} %></td>	
							<td style="text-align:right"><%if(rs.getDouble("slot_6_8")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_6_8")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("slot_8_10")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_8_10")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("slot_10_12")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_10_12")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("slot_12_14")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_12_14")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("slot_14_16")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_14_16")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("slot_16_18")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_16_18")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("slot_18_20")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("slot_18_20")) %><%}else{ %>-<%} %></td>
							<td style="text-align:right"><%if(rs.getDouble("overnight")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("overnight")) %><%}else{ %>-<%} %></td>
							
							<td style="text-align:right"><%if(HoriToal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(HoriToal)%><%}else{ %>-<%} %></td>
						</tr>
					<%
					}
					%>
					<tr>
							<td style="font-weight:bold;">Total</td>
					<%
					double HoriTotalOfTotal=0;
					for(int i=0;i<VerticalTotal.length;i++){
						
						HoriTotalOfTotal += VerticalTotal[i];
						//System.out.println(VerticalTotal[i]+" - "+HoriTotalOfTotal);
					%>
					
					<script>
						VerticalTotals[<%=i%>] = <%=VerticalTotal[i]%>;
						SyncVerticalTotals[<%=i%>] = <%=SyncVerticalTotal[i]%>;
						
					</script>
					
					<td style="text-align:right; "><%if(VerticalTotal[i]!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(VerticalTotal[i]) %><%}else{ %>-<%} %></td>	
					<%}
					%>
					<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormatRounded(HoriTotalOfTotal) %></td>	
					</tr>	
					</tbody>
							
				</table>
		</td>
	</tr>
</table>




	</li>	
</ul>




<script>

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
        legend: {
            layout: 'vertical',
            verticalAlign: 'top',
            align: 'right',
            floating: true,
            borderWidth: '0px'
            
        },
        series: [{
            name: 'Mobile Timestamp',
            showInLegend:true,
            data: VerticalTotals
        },{
            name: 'Sync Timestamp',
            color: 'rgba(160, 160, 160, .5)',
            showInLegend:true,
            data: SyncVerticalTotals
        }]
    });
});

</script>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>