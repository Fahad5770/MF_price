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
<%@page import="com.pbc.employee.OrderBookerDashboard"%>



 

<style>
td{
font-size: 8pt;
}
 #map {
        width: 100%;
        height: 800px;
        margin-top: 10px;
      }
      
      
 .SelectedBold{
 	font-weight:bold;
 }     
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 263;

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



String SecondaryDistributorString="";
int SecondaryDistributor=0;

String WhereSecDistributor="";

SecondaryDistributorString=(String)session.getAttribute("UserDistributorID");
SecondaryDistributor = Utilities.parseInt(SecondaryDistributorString);

if(SecondaryDistributor!=0){
	WhereSecDistributor = " and mo.distributor_id in("+SecondaryDistributor+")";
}

//System.out.println("Distributor ID - "+SecondaryDistributor);



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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//
//OrderBooker
boolean IsOrderBookerSelected=false;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");  
	IsOrderBookerSelected = true;
}

String OrderBookerIDs = "";
String WhereOrderBooker = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
	WhereOrderBooker = " and created_by in ("+OrderBookerIDs+") ";
}


//Warehouse


String WarehouseIDs="";
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
}
//System.out.println(WarehouseIDs);
String WhereWarehouse = "";
if (WarehouseIDs.length() > 0){
	WhereWarehouse = " and idn.warehouse_id in ("+WarehouseIDs+") ";	
}

OrderBookerDashboard OrderBooker = new OrderBookerDashboard();

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
<script>

function OrderBookerTracking(OrderBookerID){
	
	var url="";
		
		url = 'ReportCenterR209MapSummary.jsp?OrderBookerID='+OrderBookerID+'&UniqueSessionID=<%=UniqueSessionID%>';
	
	
	$("#FilterType").val("4"); //4 for Order Bookers
	$("#OrderBookerIDCheckbox").val(OrderBookerID);
	
	
	
	$("#OrderActivityReportTD").html("<img src='images/snake-loader.gif'>");
	
	
	$("#OrderBookerHyperlinkID_"+$("#TempStoringIDOfLastClick").val()).css('font-weight','normal'); //reseting the old click font
	
	$("#OrderBookerHyperlinkID_"+OrderBookerID).css('font-weight','bold');
	
	$("#TempStoringIDOfLastClick").val(OrderBookerID); //storing first clicked id - will use it to reset the font to normal when other link will be clicked
	//alert($("#TempStoringIDOfLastClick").val());
	
	$.mobile.showPageLoadingMsg();
	$.get(url, function(data) {
		  $.mobile.hidePageLoadingMsg();
		  $("#OrderActivityReportTD").html(data);
		  $("#OrderActivityReportTD").trigger('create');
		 
	});
	
	
}
//OrderBookerTracking(0);

</script>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<input type="hidden" id="TempStoringIDOfLastClick" />
	<tr>
		
		<td style="width:35%" valign="top" >
		
		<%
		String DistributorIds="";
        long SelectedDistributorArray[] = null;
        if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
     	   SelectedDistributorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
     	   DistributorIds = Utilities.serializeForSQL(SelectedDistributorArray);
     	   //System.out.println("I am in if");
        }
        else
        {
     	  
            if (session.getAttribute("SR1FeatureID") != null)
            {
            	FeatureID = (Integer)session.getAttribute("SR1FeatureID");
            }
     	   Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
     	   DistributorIds = UserAccess.getDistributorQueryString(UserDistributor);
     	   //System.out.println("I am in else");
        }
		
		
		//System.out.println("select mo.created_by, u.DISPLAY_NAME, count(*) no_of_orders, sum(mo.net_amount) total_amount from mobile_order mo join users u on u.id = mo.created_by where mo.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+WhereSecDistributor+" and status_type_id in (1,2) group by mo.created_by");
        
        //ResultSet rs = s.executeQuery("select * from employee_view where sap_code in (select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv  where dbpv.distributor_id in ("+DistributorIds+") "+WhereHOD+WhereRSM+")");
		  ResultSet rs = s.executeQuery("select mo.created_by, u.DISPLAY_NAME, count(*) no_of_orders, sum(mo.net_amount) total_amount from mobile_order mo join users u on u.id = mo.created_by where mo.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+"and mo.distributor_id="+SecondaryDistributor+" and status_type_id in (1,2) group by mo.created_by");
		//System.out.println("select mo.created_by, u.DISPLAY_NAME, count(*) no_of_orders, sum(mo.net_amount) total_amount from mobile_order mo join users u on u.id = mo.created_by where mo.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateTimeNext(EndDate)+" and mo.distributor_id in ("+DistributorIds+")"+WhereHOD+WhereRSM+" group by mo.created_by");
		%>
		
		<table border=0 style="font-size:13px; font-weight: 400; width:100%; margin-right:10px; margin-top:10px;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						
						<thead style="background-color:#F0F0F5;">
						    <tr style="font-size:11px;">
								<th style="width:70%"">Order Booker</th>
								<th  >Orders*</th>
								<th  >Amount*</th>
							</tr>
						</thead>
		<% 
						
		while(rs.next()){
			
		%>    		
			    		<tr style="font-size:10px;">
			    			<td >
			    				
			    				<a href="#" id="OrderBookerHyperlinkID_<%=rs.getString("created_by")%>" data-ajax="false" style="font-size:8pt; font-weight: normal; text-decoration:none; color:#000" onClick="OrderBookerTracking(<%=rs.getString("created_by")%>)" ><%=rs.getString("created_by")+" - "+rs.getString("display_name") %></a>
			    				
			    			</td>
			    			<td ><%=rs.getLong("no_of_orders") %></td>
			    			<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(rs.getDouble("total_amount")) %></td>
			    		</tr>
			
		<%
		}
		%>
		
		</table>
		<br>
		* This information is based on Mobile Date & Time. It should not be confused with Order Upload or Invoice Date & Time
		</td>
		<td style="width: 65%" valign="top" id="OrderActivityReportTD">
			
					 <!--  load map here -->
					
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