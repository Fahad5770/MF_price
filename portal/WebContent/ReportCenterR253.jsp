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
int FeatureID = 317;
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
System.out.println("Start Date"+StartDate);
System.out.println("End date"+EndDate);


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

String CensusUserIDs="";
long SelectedCensusUserArray[] = null;
String WhereCensusUser = "";
int CurrentYear=0;
int NumberOfAdvancesIssued = 0;

int PreviousYear = 0;
int NextYear = 0;
double TotalAdvances = 0;
double TotalSampling = 0;
double TotalDeduction = 0;
double TotalPayable = 0;
double TotalAdjustment = 0;
double TotalNetPayable = 0;
int MonthCount =0;
Date LastMonth = null;
Advance advances[] = null;
MonthlyDiscount slips[] = null;

double AverageDeduction =0;

double CurrentBalance = 0;


if(OutletIds.length()>0){
//CensusUser



if (session.getAttribute(UniqueSessionID+"_SR1SelectedCensusUser") != null){
	SelectedCensusUserArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCensusUser");
	CensusUserIDs = Utilities.serializeForSQL(SelectedCensusUserArray);
}

WhereCensusUser = "";
CurrentYear = Utilities.getYearByDate(new Date());

//int SelectedYear = Utilities.parseInt(request.getParameter("SelectedYear"));-->
System.out.println(SelectedYear);
if(SelectedYear == 0){
	SelectedYear = CurrentYear;
}

PreviousYear = SelectedYear - 1;
NextYear = SelectedYear + 1;

LastMonth = bean.getLastDateOfDiscount1(OutletIds);
advances = bean.getAdvancesIssued1(OutletIds);



for (int i = 0; i < advances.length; i++){
	 TotalAdvances += advances[i].AMOUNT;
}

NumberOfAdvancesIssued = advances.length;


slips= bean.getSlips1(OutletIds);
for (int i = 0; i < slips.length; i++){
	 TotalSampling += slips[i].SAMPLING_AMOUNT;
	 TotalDeduction += slips[i].DEDUCTION_AGAINST_ADVANCE;
	 TotalPayable += slips[i].PAYABLE;
	 TotalAdjustment += slips[i].MANUAL_ADJUSTMENT;
	 TotalNetPayable += slips[i].NET_PAYABLE;
}

  
MonthCount = slips.length;

AverageDeduction = TotalDeduction / MonthCount;

CurrentBalance = bean.OUTLET.getCurrentBalance1(OutletIds);
}

%>
<head>
<script type="text/javascript">
	function redirect(url){
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
</script>


<style>

</style>

</head>

<html>
<body>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;width:100%" data-icon="false">
	<li data-role="list-divider" data-theme="a">Summary</li>
	<li width="90%">

<%if(OutletIds.length()>0){ %>
		<table  width="90%" border="0" style="font-size:10pt">
				<tr>
					<td style="width:90%;font-size:10pt" valign="top">
							<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width:100%;margin:0px !important;font-size:10pt" >
							    <li data-role="list-divider">Discount Summary</li>
							    <li style="font-size: 10pt;" data-icon="false"><a style="font-size: 10pt;" href="#" >Total Discount (Rs.)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.getTotalDiscount1(OutletIds)) %></span></a></li>
							    <li style="font-size: 10pt"  data-icon="false"><a style="font-size: 10pt;" href="#" >Average Discount (Rs.)<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.getAverageDiscount1(OutletIds)) %> / Month</span></a></li>
							    <li style="font-size: 10pt"  data-icon="false"><a style="font-size: 10pt;" href="#" >Average Converted Discount (Rs.)<span class="ui-li-count" style="font-size: 10pt;">5 / Case</span></a></li>
							    <li style="font-size: 10pt"  data-icon="false"><a style="font-size: 10pt;" href="#" >Last Month Discount (<%= Utilities.getDisplayDateMonthYearFormat(LastMonth) %>) <span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(bean.getDiscountOfMonth1(LastMonth,OutletIds)) %></span></a></li>
							    <li  data-role="list-divider">Advances Summary</li>
							    <li style="font-size: 10pt;"  data-icon="false"><a style="font-size: 10pt;" href="#" >Total Advances<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(TotalAdvances) %></span></a></li>
							    <li style="font-size: 10pt;"  data-icon="false"><a style="font-size: 10pt;" href="#" >Adjustment against Advances<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(TotalDeduction) %></span></a></li>
							    <li style="font-size: 10pt;"  data-icon="false"><a style="font-size: 10pt;" href="#" >Average Deduction<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(AverageDeduction) %> / Month</span></a></li>
							    <li style="font-size: 10pt;"  data-icon="false"><a style="font-size: 10pt;" href="#" data-theme="a">No. of Advances Issued<span class="ui-li-count" style="font-size: 10pt;"><%=NumberOfAdvancesIssued %></span></a></li>
							    <li  style="font-size: 10pt;" data-icon="false"><a style="font-size: 10pt;" href="#" data-theme="a">Current Balance<span class="ui-li-count" style="font-size: 10pt;"><%=Utilities.getDisplayCurrencyFormat(CurrentBalance) %></span></a></li>
						
							</ul>	
					</td>
					
					
				</tr>
				<tr>
					<td colspan="2">
					
							<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%;margin:0px !important;" > 
							
								<li><div style="padding-bottom: 10px;">Monthly Discount</div>
									<table class="table-stripe GridWithBorder  "  style="font-size: 10pt; width: 100%;">
						         <thead>
						           <tr class="ui-bar-d">
						             <th style="text-align: right;width:12.5">Month</th>
						             <th style="text-align: right;width:12.5">Discount Amount</th>
						             <th style="text-align: right;width:12.5">Advance Deduction</th>
						             <th style="text-align: right;width:12.5">Payable</th>
						             <th style="text-align: right;width:12.5">Adjustment</th>
						             <th style="text-align: right;width:12.5">Net Payable</th>
						             <th style="text-align: right;width:12.5">Approval Status</th>
						             <th style="text-align: right;width:12.5">Slip Status</th>
						             
						             <th style="text-align: right;width:12.5">Dispatch Status</th>
						             
						           </tr>
						         </thead>
						         <tbody>
						         <%
						         for (int i = 0; i < slips.length; i++){
						         %>
						           <tr>
						             <td><%=Utilities.getDisplayDateMonthYearFormat(slips[i].DATE)%></th>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].SAMPLING_AMOUNT) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].DEDUCTION_AGAINST_ADVANCE) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].PAYABLE) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].MANUAL_ADJUSTMENT) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(slips[i].NET_PAYABLE) %></td>
						             <td style="text-align: right;"><%=slips[i].APPROVAL_STATUS %></td>
						             <td style="text-align: right;"><%=slips[i].SLIP_STATUS %></td>
						             
						             <td style="text-align: right;">
						             	<%
						             	if( slips[i].METHOD_ID != 0 && slips[i].METHOD_ID == 1 ){
						             		out.print("By Hand ["+slips[i].BY_HAND_USERID+"]");
						             	}else if( slips[i].METHOD_ID != 0 && slips[i].METHOD_ID == 2 ){
						             		out.print("By TCS [<a href='http://www.tcscouriers.com/pk/Tracking/Default.aspx?TrackBy=ReferenceNumberHome&trackNo="+slips[i].DISPATCH_CODE+"' target='_blank'>"+slips[i].DISPATCH_CODE+"</a>]");
						             	}else{
						             		out.print("Not Dispatched");
						             	}
						             	%>
						             </td>
						             
						           </tr>
								<%
						         }
								%>
						           <tr>
						             <td>Total</th>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalSampling) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalDeduction) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalPayable) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalAdjustment) %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalNetPayable) %></td>
						             <td></td>
						             <td></td>
						             <td></td>
						           </tr>
								
								</tbody>
								</table>
								</li>
							</ul>											
							<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%;margin:0px !important;" >
							
								<li><div style="padding-bottom: 10px;">Advances Issued</div>
		
									<table class="table-stripe GridWithBorder " style="font-size: 10pt; width: 100%;">
						         <thead>
						           <tr class="ui-bar-d">
						             <th>Date</th>
						             <th>Document #</th>
						             <th>Remarks</th>
						             <th style="text-align: right;">Amount</th>
						           </tr>
						         </thead>
						         <tbody>
						         <%
						         for (int i = 0; i < advances.length; i++){
						         %>
						           <tr>
						             <td><%=Utilities.getDisplayDateFormat(advances[i].DATE)%></th>
						             <td><%=advances[i].APPROVAL_ID %></td>
						             <td><%=advances[i].REMARKS %></td>
						             <td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(advances[i].AMOUNT)%></td>
						           </tr>
								<%
						         }
								%>
						           <tr>
						             <th></th>
						             <th></th>
						             <th>Total</th>
						             <th style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TotalAdvances) %></th>
						           </tr>
								
								</tbody>
								</table>
								</li>
							</ul>									           
		<%
		//Date dates[] = Utilities.getPastMonthsInDate(new Date(), 6);
		Date dates[] = Utilities.getPastMonthsInDate(Utilities.getEndDateByMonth(11, SelectedYear), 12);
		
		double TotalDiscount[] = new double[dates.length];
		
		%>			
							<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%;margin:0px !important;" >
							
								<li><div style="padding-bottom: 10px;">Projection</div>
					
									<!--  <table data-role="table" id="SalesComparisonTable" data-mode="columntoggle" class="ui-body-d ui-shadow table-stripe ui-responsive" data-column-btn-theme="c" data-column-btn-text="Export to Excel" data-column-popup-theme="a" style="font-size: 10pt;"> -->
									
									<table style="width: 25%" border=0>
										<tr>
											<td style="display:none">
												
												<a id="OutletDashboardSalesLink" href="#" data-transition="none" style="display:none" >Sales</a>
												<script>
												
													var OutletID = <%=OutletIds%>;
								
													function reloadOutletDashboardSales(SelectedYear){
														alert("Call");
														$('#OutletDashboardSalesLink').attr("href", "ReportCenter.jsp?ReportID=178?outletID="+OutletID+"&ReportTitle=Discount&SelectedYear="+SelectedYear);
														$('#OutletDashboardSalesLink').trigger('click');
													}
													
												</script>
													<select style="display:none" name="FilterByYear" id="FilterByYear" data-ajax="false" data-mini="true" onchange="reloadOutletDashboardSales(this.value)">
														<option value="<%=PreviousYear%>" <% //if( SelectedYear == PreviousYear ){ out.print("selected"); } %> ><%=PreviousYear%></option>
														<option value="<%=CurrentYear%>" <% //if( SelectedYear == SelectedYear ){ out.print("selected"); } %> selected="selected" ><%=SelectedYear%></option>
														<option value="<%=NextYear%>" <% //if( SelectedYear == NextYear ){ out.print("selected"); } %> ><%=NextYear%></option>
													</select>
													
											</td>
										</tr>
									</table>
									
									<table class="table-stripe GridWithBorder " style="font-size: 9pt; width: 100%;">
									         <thead>
									           <tr>
									             <td style="padding:2px !important" colspan="2">Status</td>
									             <%
									             int count = 0;
									             for (Date idate: dates){
									            	 System.out.println("idate"+idate);
									            	 Date iEndDate = Utilities.getEndDateByDate(idate);
									            	 System.out.println("iEndDate"+iEndDate);
													 String status = "Not Generated";
									            	 boolean generated = false;
									            	 for (int i = 0; i < slips.length; i++){
									            		 if (DateUtils.isSameDay(slips[i].DATE, iEndDate)){
									            			 status = slips[i].APPROVAL_STATUS;
									            			 generated = true;
									            		 }
									            	 }
									             %>
									             <td colspan="2" style="text-align: center;padding:2px !important; <%if (status.equals("Approved")){%>color: green;<%}%>"><%=status%></td>
									             <%
									             count++;
									             }
									             %>							             
									           </tr>
									           <tr class="ui-bar-d">
									           		
									             <th colspan="2"></th>
									             <%
									             for (Date idate: dates){
									             %>
									             	<th colspan="2" style="text-align: center;padding:2px !important;"><%= Utilities.getDisplayDateMonthYearFormat(idate) %></th>
									             <%
									             }
									             %>
									           </tr>
									           
									           <tr aclass="ui-bar-d">
									             <th style="padding:2px !important;">Package</th>
									             <th style="padding:2px !important;">Brand</th>
									             <%
									             for (Date idate: dates){
									             %>
									             <th style="width: 6.5%; text-align: center;padding:2px !important;">Rate</th>
									             <th style="width: 6.5%; text-align: center;padding:2px !important;">Discount</th>
									             <%
									             }
									             %>
									           </tr>
									         </thead>
									         <tbody>
									         
									           <tr>
									             <td colspan="2">Fixed Discount</td>
									             <%
									             count = 0;
									             for (Date idate: dates){
									            	 Date iEndDate = Utilities.getEndDateByDate(idate);
									            	 double FixedSampling = bean.getFixedDiscountAmount1(iEndDate,OutletIds);
									            	 
									            	 TotalDiscount[count] += FixedSampling;
									             %>
									             <td></td>
									             <td style="text-align: center;padding:2px !important;"><% if (FixedSampling != 0){ out.print(Utilities.getDisplayCurrencyFormat(FixedSampling));  } %></td>
									             <%
									             count++;
									             }
									             %>							             
									           </tr>
									           <%
									           InventoryPackage PackList[] = bean.getDiscountPackageListWithBrands1(OutletIds);
									           for (InventoryPackage pack : PackList ){
									           %>
									           <tr>
									             <td><%=pack.LABEL %></td>
									             <td><%=pack.BRAND_LABEL %></td>
									             <%
									             count = 0;
									             for (Date idate: dates){
									            	 Date iEndDate = Utilities.getEndDateByDate(idate);
									            	 double discount[] = bean.getPerCaseDiscountAmountAndRate1(iEndDate, pack.ID, pack.BRAND_ID,OutletIds);
									            	 TotalDiscount[count] += discount[1];
									             %>
									             <td style="text-align: center;padding:2px !important;"><%if (discount[0] != 0){out.print(Utilities.getDisplayCurrencyFormat(discount[0]));} %></td>
									             <td style="text-align: center;padding:2px !important;"><%if (discount[1] != 0){out.print(Utilities.getDisplayCurrencyFormat(discount[1]));} %></td>
									             <%
									             count++;
									             }
									             %>
									           </tr>
									           <%
									           }
									           %>
									           <tr >
									             <th colspan="2">Total</th>
									             <%
									             count = 0;
									             for (Date idate: dates){
									             %>
									             <th></th>
									             <th style="text-align: center;"><%if (TotalDiscount[count] != 0){out.print(Utilities.getDisplayCurrencyFormat(TotalDiscount[count]));} %></th>
									             <%
									             count++;
									             }
									             %>
									           </tr>
									         </tbody>
									 </table>
									</li>
								</ul>
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
 </body>
</html>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>