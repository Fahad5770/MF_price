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
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 484;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
//
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
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
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
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and created_by in ("+OrderBookerIDs+") ";
}


//Distributor

boolean IsDistributorSelected=false;
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	IsOrderBookerSelected=true;
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");
	IsDistributorSelected = true;
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
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
	WhereDistributors = " and dbpav.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
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
	WhereHOD = " and dbpav.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and dbpav.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}



//Outlet Type


String OutletTypes="";
String SelectedOutletTypeArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType") != null){
	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll ="";


for(int i=0;i<SelectedOutletTypeArray.length;i++){
	if(SelectedOutletTypeArray[i].equals("Discounted - All")){	
		WhereDiscountedAll = " and outlet_id in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Fixed")){	
		WhereDiscountedFixed = " and outlet_id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Per Case")){	
		WhereDiscountedPerCase = " and outlet_id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Non Discounted")){	
		WhereNonDiscountedAll = " and outlet_id not in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Active")){	
		WhereActive = " and co.is_active=1 ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Deactivated")){	
		WhereDeactivated = " and co.is_active=0 ";
	}
}



%>


<script type="text/javascript">



	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	
	
	function LoadPJPOutletReport(PjpID){
		
		
		//("PJPLi_"+PjpID)
		//alert($("#TempStoringIDOfLastClick").val());
		
	$("#PJPLi_"+$("#TempStoringIDOfLastClick").val()).css('font-weight','normal'); //reseting the old click font
	
	$("#PJPLi_"+PjpID).css('font-weight','bold');
	
	$("#TempStoringIDOfLastClick").val(PjpID); //storing first clicked id - will use it to reset the font to normal when other link will be clicked
		
	
		
		
		//alert("OrderBookerGetOrderActivityReport Function");
	if(PjpID == "x125"){
		var url = 'ReportCenterR128PJPOutletSummary.jsp?PJPID=x125&UniqueSessionID=<%=UniqueSessionID%>';
	}	
	
	var url = 'ReportCenterR128PJPOutletSummary.jsp?PJPID='+PjpID+'&UniqueSessionID=<%=UniqueSessionID%>';
		
		$("#OrderActivityReportTD").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get(url, function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#OrderActivityReportTD").html(data);
			  $("#OrderActivityReportTD").trigger('create');
			 
		});
		
	}
	
	LoadPJPOutletReport();
		
</script>

<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 30%" valign="top">
		
			
			
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">PJP</li>
				
			
				
				<%
				//System.out.println("SELECT id, outlet_id, (SELECT name FROM common_outlets where id=mobile_order.outlet_id) outlet_name, net_amount, created_on FROM mobile_order where status_type_id in (1,2) and "+OrderBookerIDsWher+" created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+WhereOutlets+" order by created_on desc");
				ResultSet rs22 = s.executeQuery("SELECT distinct dbpav.id, label ,count(distinct outlet_id) outlet_count,assigned_to,assigned_to_name FROM distributor_beat_plan_all_view dbpav join common_outlets co on dbpav.outlet_id=co.id where dbpav.outlet_active=1 "+WhereDistributors+WhereHOD+WhereRSM+WhereDiscountedAll+WhereDiscountedFixed+WhereDiscountedPerCase+WhereNonDiscountedAll+WhereActive+WhereDeactivated+" group by dbpav.id order by outlet_count desc");
				int counter = 0;
				int TotalOutlets =0;
				
				while(rs22.next()){
					TotalOutlets +=rs22.getInt("outlet_count");
					
					int pjpStatusID = 0;
			         
			         ResultSet rs111 = s3.executeQuery("SELECT is_active FROM distributor_beat_plan where id=" + rs22.getString("id"));
			         if (rs111.first()) {
			            pjpStatusID = rs111.getInt("is_active");
			         }
			         
			         String status = (pjpStatusID == 1) ? "Active" : "Inactive";
					
					%>
					<li style="height: 35px" >
						<a data-ajax="false" id="PJPLi_<%=rs22.getString("id")%>" href="javascript: LoadPJPOutletReport(<%=rs22.getString("id")%>)" style="font-size:11px; font-weight:normal;"><%=rs22.getString("id")+" - "+rs22.getString("label")+" - "+status%><span class="ui-li-count" style="font-size:10px"><%=rs22.getString("outlet_count")%></span></a>
					</li>
					<%
					
					}				
				//System.out.println(AllPJPIDs);
				
					%>	
				<li style="height: 35px" >
						<a data-ajax="false" id="PJPLiAll" href="javascript: LoadPJPOutletReport('x125')" style="font-size:12px; font-weight:bold;">All<span class="ui-li-count" style="font-size:10px"><%=TotalOutlets%></span></a>
				</li>
			</ul>
		
		<input type="hidden" id="TempStoringIDOfLastClick"/>
		
		
		</td>
		<td style="width: 70%" valign="top" id="OrderActivityReportTD">&nbsp;</td>
			
		
	</tr>
</table>
 <form id="DistReportsOrderBookerOrderListFormID" name="DistReportsOrderBookerOrderListFormID" action="OutletDashboard.jsp" method="POST" data-ajax="false" target="_blank">
    	<input type="hidden" name="outletID" id="DistReportsOrderBookerOrderListOutletID"/>
 </form>
 
 

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>