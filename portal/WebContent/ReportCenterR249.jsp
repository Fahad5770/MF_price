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
int FeatureID = 313;
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
					    <th data-priority="1"  style="text-align:center;width: 11% ">Track On</th>
					   <th data-priority="1"  style="text-align:center;width: 5%  ">Track Date</th>
					   <th style="text-align:center;width: 11% ">Order Booker</th>
					   <th style="text-align:center;width: 11% ">Distributor</th>
					   <th data-priority="1"  style="text-align:center;width: 5%  ">Bar Code</th>
					   <th data-priority="1"  style="text-align:center;width: 6%  ">Asset #</th>
					   <th data-priority="1"  style="text-align:center;width: 11%  ">Description</th>
					   <th data-priority="1"  style="text-align:center; width: 11% ">Issued To</th>
					   <th data-priority="1"  style="text-align:center;width: 5%  ">Issue Date</th>
												
		</tr>	
	
					<%
					Date TrackDate=null;
					Date OrderDate=null;
					Date CreatedON=null;
					
					long OutletID=0;
					long OutletID2=0;
					
					String TrackOn="";
					String OrderBookerName="";
					String BarCode= "";	
					String OutletName= "";
					String OutletName2="";
					int CreatedBy=0;
					String DistributorName= "";
					String Distributor="";
					String AgencyName ="";
					long DistributorID=0;
					
					String query="select distinct moa.barcode, mo.outlet_id as track_on_outlet_id,(select name from common_outlets co where co.id=mo.outlet_id) track_on_outlet_name,mo.mobile_timestamp as track_date,mo.created_by,(select display_name from users u where u.id=mo.created_by) order_booker_name,mo.distributor_id,(select name from common_distributors cd where cd.distributor_id=mo.distributor_id) distributor_name  from mobile_order mo join mobile_order_assets moa on mo.unedited_order_id=moa.id where  mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
					//System.out.println(query);
					ResultSet rs=s.executeQuery(query);
					while(rs.next()){
						
						OutletID=rs.getLong("track_on_outlet_id"); 
						OutletName=rs.getString("track_on_outlet_name");
						TrackOn=OutletID+"-"+OutletName;
						TrackDate=rs.getTimestamp("track_date");
						DistributorID = rs.getLong("distributor_id");
						DistributorName=rs.getString("distributor_name");
						Distributor=DistributorID+"-"+DistributorName;
						
						CreatedBy=rs.getInt("created_by");
						OrderBookerName=CreatedBy+"-"+rs.getString("order_booker_name");
						BarCode= rs.getString("barcode"); 
						
						long AssetCode=0;
						String Description="";
						Date IssuedDate=null;	
						String IssuedTo="";
									
							
								String query1="SELECT main_asset_number,asset_description,outlet_id_parsed, (select name from common_outlets co where co.id=outlet_id_parsed) issued_to_outlet_name,movement_date_parsed as issued_date FROM pep.common_assets where barcode ='"+BarCode+"'";
								//System.out.println(query1);
								ResultSet rs1=s2.executeQuery(query1);	
									while(rs1.next()){
											
												 AssetCode= rs1.getLong("main_asset_number");
												 Description= rs1.getString("asset_description");
												 IssuedDate=rs1.getDate("issued_date");	
												 IssuedTo=rs1.getLong("outlet_id_parsed")+"-"+rs1.getString("issued_to_outlet_name");
												
									}
						
								 %>
								
								<tr  style="font-size:11px;">
										<td data-priority="1"  style="text-align:left; "><%=TrackOn%></td>
									    <td data-priority="1"  style="text-align:left; "><%=Utilities.getDisplayDateFormat(TrackDate)%></td>
										<td><%=OrderBookerName%></td>
										<td><%=Distributor%></td>
										<td data-priority="1"  style="text-align:left; "><%=BarCode%></td>
										<td data-priority="1"  style="text-align:left; "><%if(AssetCode!=0){%><%=AssetCode%><%}else{%><%}%></td>
										<td data-priority="1"  style="text-align:left; "><%=Description%></td>
										<td data-priority="1"  style="text-align:left; "><%=IssuedTo%></td>
										<td data-priority="1"  style="text-align:left; "><%if(IssuedDate!=null){%><%=Utilities.getDisplayDateFormat(IssuedDate)%><%}else{%><%}%></td>
								</tr>
							
							
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


 

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>