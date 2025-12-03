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
							
							
							<th data-priority="1"  style="text-align:center; width: 20% ">Date</th>
							<th data-priority="1"  style="text-align:center; width: 20% ">Order Booker</th>
							<th data-priority="1"  style="text-align:center; width: 20% ">No. of Orders</th>
							<th data-priority="1"  style="text-align:center; width: 20% ">DFS(m)</th>
							<th data-priority="1"  style="text-align:center; width: 20%" nowrap="nowrap">TIM(hr)</th>
							
													
					    </tr>
					  </thead>
					<tbody>
					
					<%
					 StartDate = new Date();
			         EndDate =  new Date();
			        
			         StartDate= Utilities.parseDate("01/12/2017");//Utilities.getDateByDays(StartDate,-5);
			         EndDate=Utilities.parseDate("31/12/2017");
			        
			        //System.out.println("StartDate"+StartDate+" "+"EndDate"+StartDate);
			        Date CurrDate=StartDate;
			        int x=10;
			        while( true ){
			        	long CreatedBy=0;
			        	%>
			        	
			        	
			        	
			        	<%
			        	ResultSet rs = s.executeQuery("SELECT distinct created_by, (select display_name from users u where u.id=created_by) display_name FROM pep.mobile_order where mobile_timestamp between "+Utilities.getSQLDate(CurrDate)+" and "+Utilities.getSQLDateNext(CurrDate));
			        	while(rs.next()){
			        		CreatedBy = rs.getLong("created_by");
			        		
			        		//Calculating TIM
			        		
			        		double MaxTime=0;
			        		double MinTime=0;
			        		
			        		double TIM=0;
			        		
			        		
			        		ResultSet rs4 = s2.executeQuery("Select time_to_sec(max(mobile_timestamp)) max_time, time_to_sec(min(mobile_timestamp)) min_time from mobile_order mo where mobile_timestamp between "+Utilities.getSQLDate(CurrDate)+" and "+Utilities.getSQLDateNext(CurrDate)+" and mo.created_by="+CreatedBy);
			        		if(rs4.first()){
			        			MaxTime = rs4.getLong(1);
			        			MinTime = rs4.getLong(2);
			        			
			        		}
			        		
			        		TIM = (MaxTime-MinTime)/3600;
			        		///////////////////////////////////////////////////////////////////
			        		
			        		//Calculateing # of Orders
			        		
			        		long NumberOfOrders=0;
			        		
			        		ResultSet rs6 = s2.executeQuery("SELECT count(*) FROM mobile_order where mobile_timestamp between "+Utilities.getSQLDate(CurrDate)+" and "+Utilities.getSQLDateNext(CurrDate)+" and created_by ="+CreatedBy);
			        		if(rs6.first()){
			        			NumberOfOrders = rs6.getLong(1);
			        		}
			        		
			        		
			        		
			        		
			        		//////////////////////////////////////////////////////////////////////	        		
			        		
			        		
			        		
			        		ResultSet rs2 = s2.executeQuery("SELECT 	sum((( 3959 * acos( cos( radians(mo.lat) ) * cos( radians( co.lat ) ) * cos( radians( co.lng ) - radians(mo.lng) ) + sin ( radians(mo.lat) )  * sin( radians( co.lat ) ) ) ) * 1609.34 )) AS distance "+ 
		                            " FROM mobile_order mo, common_outlets co where mo.outlet_id=co.id and mo.mobile_timestamp between "+Utilities.getSQLDate(CurrDate)+" and "+Utilities.getSQLDateNext(CurrDate)+" and mo.created_by ="+CreatedBy+" and mo.lat!=0 and mo.lng!=0 and co.lat!=0 and co.lng!=0");
			        		while(rs2.next()){
			        			double Distance = rs2.getDouble("distance");
			        			
			        			
			        		
			        		
			        	
			        %> 
			         	 <tr>
				        	<td><%=Utilities.getDisplayDateFormat(CurrDate) %></td>
				       		<td><%=rs.getLong("created_by") %> - <%=rs.getString("display_name") %></td>
			        		<td><%=NumberOfOrders %></td>
			        		<td><%=Utilities.getDisplayCurrencyFormatRounded(Distance) %></td>
			        		<td><%=Utilities.getDisplayCurrencyFormatOneDecimal(TIM) %></td>
			         	</tr>
			         
			         
			         
			         <%
			        		}
			        	}
			         CurrDate=Utilities.getDateByDays(CurrDate,+1);
			         if(CurrDate.equals(EndDate)){
			          break;
			         }
			         %>
			         
			         
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