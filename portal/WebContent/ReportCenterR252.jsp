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
<%@page import="com.pbc.outlet.LedgerTransaction"%>
<%@page import="com.pbc.outlet.InventoryPackage"%>
<%@page import="com.pbc.outlet.MonthlyDiscount"%>

<%@page import="com.pbc.outlet.Advance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}
%>


<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 316;
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
int SelectedYear = Utilities.getYearByDate(StartDate);
System.out.println("hey"+SelectedYear);
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
	System.out.println(WhereOutlets);
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
int CurrentYear = Utilities.getYearByDate(new Date());

  //int SelectedYear = Utilities.parseInt(request.getParameter("SelectedYear"));--
if(SelectedYear == 0){
	SelectedYear = CurrentYear;
}

int PreviousYear = SelectedYear - 1;
int NextYear = SelectedYear + 1;

Date dates[] = Utilities.getPastMonthsInDate(Utilities.getEndDateByMonth(11, SelectedYear), 12);



double SummaryTotals[] = new double[dates.length];

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

     
<%if(OutletIds.length()>0){ %>
<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%"  id="SalesComparisonTable" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				 	
							
							         <thead>
							           <tr class="ui-bar-d">
							             <th>Package</th>
							             <%
							             for (Date idate : dates){
							             %>
							             	<th  ><%= Utilities.getDisplayDateMonthYearFormat(idate) %></th>
							             <%
							             }
							             %>
							             <th >Total</th>
							           </tr>
							         </thead>
							         <tbody>
										<%
										InventoryPackage PackList[] = bean.getPackageList();
										for (InventoryPackage pack : PackList ){
										%>
							           <tr>
							             <td> <%=pack.LABEL %></th>
							             <%
							             double TotalQty = 0;
							             int count = 0;
							             for (Date idate : dates){
							            	 int imonth = Utilities.getMonthNumberByDate(idate);
							            	 int iyear = Utilities.getYearByDate(idate);
							            	 double qty[] = bean.getSalesByPackage(pack.ID, imonth, iyear,OutletIds);
							            	 TotalQty += qty[0];
							            	 SummaryTotals[count] += qty[1];
							             %>
							             	<td style="text-align: center;"><%if (qty[0] != 0){out.print(Math.round(qty[0]));}%></td>
							             <%
							             count++;
							             }
							             %>
							             <td style="text-align: center;"><%if (TotalQty != 0){out.print(Math.round(TotalQty));}%></td>
							           </tr>
							           <%
										}
							           %>
							           <tr>
							             <th>Converted Total</th>
							             <%
							             double ConvertedTotal = 0;
							             for (double total : SummaryTotals){
							            	 ConvertedTotal += total;
							             %>
							             	<th style="text-align: center;"><%= Math.round(total)%></th>
							             <%
							             }
							             %>
							             <th style="text-align: center;"><%if (ConvertedTotal != 0){out.print(Math.round(ConvertedTotal));}%></th>
							           </tr>
							           
							         </tbody>
							   </table>        
				
		</td>
	</tr>
</table>

<%
bean.close();
%>

<%}else{%>
	
	
	Please Select Outlet. 
	
	
<%
}
%>
</li>	
</ul>
 

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>