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
int FeatureID = 307;
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
	WhereDistributors = " and census_distributor_id in ("+DistributorIDs+") ";
	//out.print("Hello "+WhereDistributors);
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

System.out.println(WhereCensusUser);

%>


<script type="text/javascript">



	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
	
	

	


		
</script>
<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
		<tr style="font-size:11px;">
					   <th data-priority="1"  style="text-align:center; ">Description</th>
					   <th data-priority="1"  style="text-align:center; ">Issued To</th>
					   <th data-priority="1"  style="text-align:center; ">Track On</th>
					   <th data-priority="1"  style="text-align:center; ">Track Date</th>
					   <th data-priority="1"  style="text-align:center; ">Issue Date</th>
					   <th>Order Booker</th>
					   <th>Distributor</th>
												
		</tr>	
	
<%
	//Outlet Requester
 	
	Date date1=null;
	Date date2=null;
	Date OrderDate=null;
    Date CreatedON=null;
    
	long OutletID=0;
	long OutletID2=0;
	
	String OrderBookerName="";
	String BarCode= "";	
	String OutletName= "";
	String OutletName2="";
	int CreatedBy=0;
	String DistributorName= "";
	String AgencyName ="";
	long DistributorID=0;
	
	
 
//System.out.println("SELECT *,(select display_name from users u where u.id=mc.created_by) created_display_name FROM mrd_census mc where mc.id="+CensusID);
ResultSet rs = s.executeQuery("SELECT barcode,mo.outlet_id,(Select name from common_outlets co where co.id=mo.outlet_id) outlet_name,mou.mobile_timestamp,mo.distributor_id,mo.created_by,(Select name from common_distributors cm where cm.distributor_id=mo.distributor_id) distributor_name,(Select distinct DISPLAY_NAME from users u where u.id=mo.created_by ) Order_booker_name FROM mobile_order mo join mobile_order_assets moa on mo.id=moa.id join mobile_order_unedited mou on mo.id=mou.id where mou.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
//ResultSet rs = s.executeQuery("SELECT bar_code,outlet_id,mobile_timestamp FROM common_assets where bar_code is not null and tot_status='INJECTED'");

while(rs.next()){
	//System.out.println("----------------------------------------------------");
	BarCode= rs.getString("barcode"); 
	//System.out.println("Bar Code :"+BarCode);
	OrderDate=rs.getTimestamp("mobile_timestamp");
	//System.out.println("Mobile TimeStmp"+OrderDate);
	OutletID=rs.getLong("outlet_id"); 
	OutletName=rs.getString("outlet_name");
	//System.out.println("MAin TAble Outlet ID:"+OutletID);
	
	
	DistributorID = rs.getLong("distributor_id");
	DistributorName=rs.getString("distributor_name");
	//CreatedON=rs.getDate("created_on");
	CreatedBy=rs.getInt("created_by");
	OrderBookerName=rs.getString("Order_booker_name");
	//System.out.println("DISTRI"+DistributorName);
	//System.out.println(CreatedBy);
	//System.out.println("Date on"+CreatedON);
	//System.out.println("orderbooker name:"+OrderBookerName);
	
	
	String ChillerDesc="";
	
	ResultSet rs1 = s2.executeQuery("SELECT movement_date_parsed,outlet_id_parsed,(Select name from common_outlets co where co.id=ca.outlet_id) outlet_name2,asset_description FROM common_assets ca where barcode='"+BarCode+"' ");
			while(rs1.next()){
			OutletID2= rs1.getLong("outlet_id_parsed");
			OutletName2=rs1.getString("outlet_name2");
			date1=rs1.getDate("movement_date_parsed");	
			//System.out.println("Second Outlet Id:"+OutletID2);
			//System.out.println("Order Date:"+date1);
			//System.out.println("----------------------------------------------------");
			
			ChillerDesc = rs1.getString("asset_description");
				
			%>		
				<%if(OutletID2!=OutletID){%>
				<tr style="font-size:11px;">
					<td><%=ChillerDesc %></td>
					<td><%=OutletID2%> - <%=OutletName2%></td>
					<td><%=OutletID %> - <%=OutletName %></td>
					
					<td><%=Utilities.getDisplayDateFormat(OrderDate)%></td>
					<td><%=Utilities.getDisplayDateFormat(date1)%></td>
					<td><%=CreatedBy%> - <%=OrderBookerName%></td>
					<td><%=DistributorID%> - <%=DistributorName%></td>
				</tr>
				
				<% } %>
				
				


<%
			}
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