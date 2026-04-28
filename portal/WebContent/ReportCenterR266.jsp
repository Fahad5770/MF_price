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
int FeatureID = 333;
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	System.out.print("Hello "+WhereDistributors);
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


//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and distributor_Id in ("+DistributorID1+") ";
	System.out.print(WhereCustomerID);
}

//CensusUser


String CensusUserIDs="";
long SelectedCensusUserArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCensusUser") != null){
	SelectedCensusUserArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCensusUser");
	CensusUserIDs = Utilities.serializeForSQL(SelectedCensusUserArray);
}

String WhereCensusUser = "";
if (CensusUserIDs.length() > 0){
	WhereCensusUser = " and created_by in ("+CensusUserIDs+") ";	
}

//System.out.println(WhereCensusUser);


//System.out.println(WhereCensusUser);
String DistributorOrderStatusID="";
long SelectedDistributorOrderStatusArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorOrderStatus") != null){
 SelectedDistributorOrderStatusArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorOrderStatus");
 DistributorOrderStatusID = Utilities.serializeForSQL(SelectedDistributorOrderStatusArray);
}

String WhereDistributorStatusOrder = "";
if (DistributorOrderStatusID.length() > 0){
 WhereDistributorStatusOrder = "  and ido.status_type_id in ("+DistributorOrderStatusID+") ";
// System.out.println("WhereDistributorStatusOrder "+WhereDistributorStatusOrder);
}

%>


<script type="text/javascript">



	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	
	
	function LoadPendingOrderDetailReport(ID){
	var url = 'ReportCenterR266DPOrderSummary.jsp?ID='+ID+'&UniqueSessionID=<%=UniqueSessionID%>';
		
		$("#PendingOrderReportTD").html("<img src='images/snake-loader.gif'>");
		
		$.mobile.showPageLoadingMsg();
		$.get(url, function(data) {
			  $.mobile.hidePageLoadingMsg();
			  $("#PendingOrderReportTD").html(data);
			  $("#PendingOrderReportTD").trigger('create');
			 
		});
		
	}
	//LoadPendingOrderDetailReport();
	//LoadCensusDetailReport();
		
</script>

<table id="PendingOrdersList" border="0" style="width: 100%">
	<tr>
		<td style="width: 30%" valign="top">
			<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:-13px;">
				<li data-role="list-divider" data-theme="a">Pending Orders</li>
				
				<%
				
				
				int counter = 0;
				//////////////////////////////////
				int TotalOrders=0;
				int StatusID=0;
			    String StatusMsg="";
			    int OrderNumberID=0;
				String Created_on="";
				String StatusLabel="";
				
				//String SQl="SELECT ido.created_on,ido.id,ido.status_type_id,(SELECT idot.Label FROM pep.inventory_delivery_order_type idot where idot.id=ido.status_type_id) status_label FROM pep.inventory_delivery_order ido where ido.created_on between  "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereCustomerID+"  and ido.status_type_id='1' order by ido.id desc";
				//System.out.println(SQl);
				ResultSet rs2 = s.executeQuery("SELECT ido.created_on,ido.id,ido.status_type_id,(SELECT idot.Label FROM pep.inventory_delivery_order_type idot where idot.id=ido.status_type_id) status_label,distributor_id,(select name from common_distributors cd where cd.distributor_id=ido.distributor_id) distributor_name FROM pep.inventory_delivery_order ido where ido.created_on between  "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereCustomerID+WhereDistributorStatusOrder+" and is_cancelled=0  order by ido.id desc");
				while(rs2.next()){
					StatusLabel=rs2.getString("status_label");
					StatusID=rs2.getInt("status_type_id");
					if(StatusID==1)
					{
						StatusMsg="Pending";
					}
					
					OrderNumberID=rs2.getInt("id");
					//System.out.println("OrderNumberID"+OrderNumberID);
					Created_on=Utilities.getDisplayDateTimeFormat(rs2.getTimestamp("created_on"));
				//	System.out.println(StatusMsg+" "+StatusID);
					
					
					%>
					<li style="height: 50px" >
						<a data-ajax="false" id="order_<%=OrderNumberID%>" href="javascript: LoadPendingOrderDetailReport(<%=OrderNumberID%>)" style="font-size:11px; font-weight:normal;"> <span style="font-weight:bold" >Order # <%=OrderNumberID%> <%=rs2.getLong("distributor_id") %> - <%=rs2.getString("distributor_name") %></span> </br> <%=Created_on%> <span style="float:right" class="ui-bar-c">[<%=StatusLabel %>]</span></a>
					</li>
					<%
					TotalOrders++;
				}				
				//System.out.println(AllPJPIDs);
				
					%>	
				
				
				<li style="height: 35px" >
						<a data-ajax="false"  style="font-size:11px; font-weight:bold;">Total Orders:&nbsp;<%=TotalOrders%></a>
					</li>
				
			</ul>
		
		<input type="hidden" id="TempStoringIDOfLastClick"/>
		
		
		</td>
		<td style="width: 70%" valign="top" id="PendingOrderReportTD">&nbsp;</td>
			
		
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