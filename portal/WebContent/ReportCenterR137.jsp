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
 
 
 <script src="js/jquery.excoloSlider.min.js"></script>
<link href="css/jquery.excoloSlider.css" rel="stylesheet">
 
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

function runSlider(SliderID){
	//alert(SliderID);
	
	
	if($("#IsSliderAlreadyClicked_"+SliderID).val()=="0"){
		$("#slider_"+SliderID).excoloSlider();
		$("#IsSliderAlreadyClicked_"+SliderID).val("1");
	} 
	
	 
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
int FeatureID = 143;

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

String WhereDate = "";


if(session.getAttribute(UniqueSessionID+"_SR1StartDate") != null && session.getAttribute(UniqueSessionID+"_SR1EndDate") != null){
	WhereDate =" and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
}

//if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
//	EndDate = new Date();
//}

//System.out.print("StartDate = "+StartDate);
//System.out.print("EndDate = "+EndDate);


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
	WhereHOD = " and cca.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and cca.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
			DistributorIDs += "0,"+SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and cca.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//

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


//Complaint Type

long ComplaintTypeID=0;
String ComplaintTypeIDWhere="";

if (session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintType") != null){
	ComplaintTypeID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintType");	
}

if(ComplaintTypeID!=0){
	ComplaintTypeIDWhere = " and cca.type_id="+ComplaintTypeID;
}


String SelectedComplaintStatusArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintStatus") != null){
	SelectedComplaintStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintStatus");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereAssigned = " and cc.is_resolved != 1 ";
String WhereResolved = "";
String WhereVerified = "";



for(int i=0;i<SelectedComplaintStatusArray.length;i++){
	WhereAssigned = "";
	
	if(SelectedComplaintStatusArray[i].equals("Assigned")){	
		//WhereAssigned = " and  ( (cc.is_resolved=0 and cc.is_verified=0) or (cc.is_resolved=0 and cc.is_verified=1) or (cc.is_resolved=1 and cc.is_verified=1)";
		WhereAssigned = " and  cc.is_resolved=0 and cc.is_verified=0 ";
	}
	
	if(SelectedComplaintStatusArray[i].equals("Resolved")){	
		WhereResolved = " and  cc.is_resolved=1 and cc.is_verified=0 ";
	}
	
	if(SelectedComplaintStatusArray[i].equals("Verified")){	
		WhereVerified = " and cc.is_resolved=1 and cc.is_verified=1 ";
	}
}


%>



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
							
							
							<th data-priority="1"  style="text-align:center; width: 25% ">&nbsp;</th>							
							<th data-priority="1"  style="text-align:center; width: 15% ">TOT Repair</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">TOT Misuse</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Merchandising</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Other</th>							
							<th data-priority="1"  style="text-align:center; width: 15% ">Total</th>
													
					    </tr>
					  </thead> 
					<tbody>
	<%
	//Date Calculations
	
	//This Week's date
	Calendar calendar = Calendar.getInstance();
	int day = calendar.get(Calendar.DAY_OF_WEEK); 
	
	Date ThisWeekStartDate = new Date();
	Date ThisWeekEndDate = new Date();	
	
	if(day==1){//if sunday
		ThisWeekStartDate = Utilities.getDateByDays(-6);	
	}else{
		ThisWeekStartDate = Utilities.getDateByDays(-(day-2));  //starting from monday ==> Our Monday = 1 while Java Monday number =2	
	}
	//System.out.println("This week's Start Date - End Date : "+Utilities.getDisplayDateFormat(ThisWeekStartDate)+" - "+Utilities.getDisplayDateFormat(ThisWeekEndDate));
	
	//Last Week's Date
	
	Date date = new Date();
    Calendar cd = Calendar.getInstance();
    cd.setTime(date);
    int i = cd.get(Calendar.DAY_OF_WEEK) - cd.getFirstDayOfWeek();
    cd.add(Calendar.DATE, -i - 6);
    Date LastWeekStartDate = cd.getTime();
    cd.add(Calendar.DATE, 6);
    Date LastWeekEndDate = cd.getTime();
    
    //System.out.println("Last week's Start Date - End Date : "+Utilities.getDisplayDateFormat(LastWeekStartDate)+" - "+Utilities.getDisplayDateFormat(LastWeekEndDate));
	
	
	
	
	//This Month's Date
	
	Calendar cc = Calendar.getInstance();
   	int year = cc.get(Calendar.YEAR);
   	int month = cc.get(Calendar.MONTH);
       
    Date ThisMonthStartDate = Utilities.getStartDateByMonth(month, year);
    Date ThisMonthEndDate = new Date();
	
   // System.out.println("This Month's Start Date - End Date : "+Utilities.getDisplayDateFormat(ThisMonthStartDate)+" - "+Utilities.getDisplayDateFormat(ThisMonthEndDate));
	
	//Last Month's Date
	
	Calendar LMD = Calendar.getInstance();
	LMD.add(Calendar.MONTH, -1);
	int lmonth = LMD.get(LMD.MONTH) + 1; // beware of month indexing from zero
	int lyear  = LMD.get(LMD.YEAR);
	
	Date LastMonthStartDate = Utilities.getStartDateByMonth(lmonth-1,lyear);
	Date LastMonthEndDate = Utilities.getEndDateByMonth(lmonth-1,lyear);
	
	//System.out.println("Last Month's Start Date - End Date : "+Utilities.getDisplayDateFormat(LastMonthStartDate)+" - "+Utilities.getDisplayDateFormat(LastMonthEndDate));
	
	
	//This Year Date
	Calendar now = Calendar.getInstance();   // Gets the current date and time.	 
	
	Date ThisYearStartDate = new Date();
	String ThisYearStarDateString = "01/01/"+now.get(Calendar.YEAR)+"";
	ThisYearStartDate = Utilities.parseDate(ThisYearStarDateString);
	
	Date ThisYearEndDate = new Date();
	
	//System.out.println(ThisYearStartDate);
	%>				
					
					<tr  style="background:#ececec">
						<td colspan="6">Current Status</td>
					</tr>
						<%
						long PFRTotMisuse=0;
						long PFRMerchandising=0;
						long PFROther=0;
						long PFRTotRepair=0;
						
						long PFVTotMisuse=0;
						long PFVMerchandising=0;
						long PFVOther=0;
						long PFVTotRepair=0;
						
						long TSTotMisuse=0;
						long TSMerchandising=0;
						long TSOther=0;
						long TSTotRepair=0;
						
						long TSRTotMisuse=0;
						long TSRMerchandising=0;
						long TSROther=0;
						long TSRTotRepair=0;
						
						long TSVTotMisuse=0;
						long TSVMerchandising=0;
						long TSVOther=0;
						long TSVTotRepair=0;
						
						long TWTotMisuse=0;
						long TWMerchandising=0;
						long TWOther=0;
						long TWTotRepair=0;
						
						long TWRTotMisuse=0;
						long TWRMerchandising=0;
						long TWROther=0;
						long TWRTotRepair=0;
						
						long TWVTotMisuse=0;
						long TWVMerchandising=0;
						long TWVOther=0;
						long TWVTotRepair=0;
						
						long LWTotMisuse=0;
						long LWMerchandising=0;
						long LWOther=0;
						long LWTotRepair=0;
						
						long LWRTotMisuse=0;
						long LWRMerchandising=0;
						long LWROther=0;
						long LWRTotRepair=0;
						
						long LWVTotMisuse=0;
						long LWVMerchandising=0;
						long LWVOther=0;
						long LWVTotRepair=0;
						
						long TMTotMisuse=0;
						long TMMerchandising=0;
						long TMOther=0;
						long TMTotRepair=0;
						
						long TMRTotMisuse=0;
						long TMRMerchandising=0;
						long TMROther=0;
						long TMRTotRepair=0;
						
						long TMVTotMisuse=0;
						long TMVMerchandising=0;
						long TMVOther=0;
						long TMVTotRepair=0;
						
						long LMTotMisuse=0;
						long LMMerchandising=0;
						long LMOther=0;
						long LMTotRepair=0;
						
						long LMRTotMisuse=0;
						long LMRMerchandising=0;
						long LMROther=0;
						long LMRTotRepair=0;
						
						long LMVTotMisuse=0;
						long LMVMerchandising=0;
						long LMVOther=0;
						long LMVTotRepair=0;
						
						long TYTotMisuse=0;
						long TYMerchandising=0;
						long TYOther=0;
						long TYTotRepair=0;
						
						long TYRTotMisuse=0;
						long TYRMerchandising=0;
						long TYROther=0;
						long TYRTotRepair=0;
						
						long TYVTotMisuse=0;
						long TYVMerchandising=0;
						long TYVOther=0;
						long TYVTotRepair=0;
						
						
						ResultSet rs = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved != 1 and cc.type_id=2");
						if(rs.first()){
							PFRTotMisuse = rs.getLong("tot_misuse");
						}
						
						ResultSet rs1 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved != 1 and cc.type_id=3");
						if(rs1.first()){
							PFRMerchandising = rs1.getLong("merchandising");
						}
						
						ResultSet rs2 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved != 1 and cc.type_id=4");
						if(rs2.first()){
							PFROther = rs2.getLong("other");
						}
						
						ResultSet rs3 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved != 1  and cc.type_id=1");
						if(rs3.first()){
							PFRTotRepair = rs3.getLong("tot_repair");
						}
						 
						
						//Pending for Verification
						
						ResultSet rs4 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.is_verified=0");
						if(rs4.first()){
							PFVTotMisuse = rs4.getLong("tot_misuse");
						}
						
						ResultSet rs5 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=3 and cc.is_verified=0");
						if(rs5.first()){
							PFVMerchandising = rs5.getLong("merchandising");
						}
						
						ResultSet rs6 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=4 and cc.is_verified=0");
						if(rs6.first()){
							PFVOther = rs6.getLong("other");
						}
						
						ResultSet rs7 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.is_verified=0");
						if(rs7.first()){
							PFVTotRepair = rs7.getLong("tot_repair");
						}
						
						//Today Status
						
						//Raised
						Date TSStartDate = new Date();
						
						
						ResultSet rs8 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs8.first()){
							TSTotMisuse = rs8.getLong("tot_misuse");
						}
						
						ResultSet rs9 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs9.first()){
							TSMerchandising = rs9.getLong("merchandising");
						}
						
						ResultSet rs10 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs10.first()){
							TSOther = rs10.getLong("other");
						}
						
						ResultSet rs11 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs11.first()){
							TSTotRepair = rs11.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Resolved
						
						
						
						ResultSet rs12 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs12.first()){
							TSRTotMisuse = rs12.getLong("tot_misuse");
						}
						
						ResultSet rs13 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.type_id=3 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs13.first()){
							TSRMerchandising = rs13.getLong("merchandising");
						}
						
						ResultSet rs14 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=4 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs14.first()){
							TSROther = rs14.getLong("other");
						}
						
						ResultSet rs15 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs15.first()){
							TSRTotRepair = rs15.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Verified						
						
						
						ResultSet rs16 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs16.first()){
							TSVTotMisuse = rs16.getLong("tot_misuse");
						}
						
						ResultSet rs17 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs17.first()){
							TSVMerchandising = rs17.getLong("merchandising");
						}
						
						ResultSet rs18 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs18.first()){
							TSVOther = rs18.getLong("other");
						}
						
						ResultSet rs19 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(TSStartDate)+" and "+Utilities.getSQLDateNext(TSStartDate));
						if(rs19.first()){
							TSVTotRepair = rs19.getLong("tot_repair");
						}
						
						
						// ---------------------------------------------------------------- 
						
						//This Week Status
						
						//Raised
						
						
						
						ResultSet rs20 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs20.first()){
							TWTotMisuse = rs20.getLong("tot_misuse");
						}
						
						ResultSet rs21 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs21.first()){
							TWMerchandising = rs21.getLong("merchandising");
						}
						
						ResultSet rs22 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs22.first()){
							TWOther = rs22.getLong("other");
						}
						
						ResultSet rs23 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_resolved = 0 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs23.first()){
							TWTotRepair = rs23.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Resolved
						
						
						
						ResultSet rs24 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs24.first()){
							TWRTotMisuse = rs24.getLong("tot_misuse");
						}
						
						ResultSet rs25 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=3 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs25.first()){
							TWRMerchandising = rs25.getLong("merchandising");
						}
						
						ResultSet rs26 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=4 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs26.first()){
							TWROther = rs26.getLong("other");
						}
						
						ResultSet rs27 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs27.first()){
							TWRTotRepair = rs27.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Verified						
						
						
						ResultSet rs28 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs28.first()){
							TWVTotMisuse = rs28.getLong("tot_misuse");
						}
						
						ResultSet rs29 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs29.first()){
							TWVMerchandising = rs29.getLong("merchandising");
						}
						
						ResultSet rs30 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs30.first()){
							TWVOther = rs30.getLong("other");
						}
						
						ResultSet rs31 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisWeekStartDate)+" and "+Utilities.getSQLDateNext(ThisWeekEndDate));
						if(rs31.first()){
							TWVTotRepair = rs31.getLong("tot_repair");
						}
						
						
						// ---------------------------------------------------------------- 
						
						//Last Week Status
						
						//Raised
						
						
						
						ResultSet rs32 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs32.first()){
							LWTotMisuse = rs32.getLong("tot_misuse");
						}
						
						ResultSet rs33 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs33.first()){
							LWMerchandising = rs33.getLong("merchandising");
						}
						
						ResultSet rs34 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs34.first()){
							LWOther = rs34.getLong("other");
						}
						
						ResultSet rs35 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_resolved = 0 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs35.first()){
							LWTotRepair = rs35.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Resolved
						
						
						
						ResultSet rs36 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs36.first()){
							LWRTotMisuse = rs36.getLong("tot_misuse");
						}
						
						ResultSet rs37 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=3 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs37.first()){
							LWRMerchandising = rs37.getLong("merchandising");
						}
						
						ResultSet rs38 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.type_id=4 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs38.first()){
							LWROther = rs38.getLong("other");
						}
						
						ResultSet rs39 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs39.first()){
							LWRTotRepair = rs39.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Verified						
						
						
						ResultSet rs40 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs40.first()){
							LWVTotMisuse = rs40.getLong("tot_misuse");
						}
						
						ResultSet rs41 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs41.first()){
							LWVMerchandising = rs41.getLong("merchandising");
						}
						
						ResultSet rs42 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs42.first()){
							LWVOther = rs42.getLong("other");
						}
						
						ResultSet rs43 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastWeekStartDate)+" and "+Utilities.getSQLDateNext(LastWeekEndDate));
						if(rs43.first()){
							LWVTotRepair = rs43.getLong("tot_repair");
						}
						
						
						// ---------------------------------------------------------------- 
						
						//This Month Status
						
						//Raised
						
						
						
						ResultSet rs44 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs44.first()){
							TMTotMisuse = rs44.getLong("tot_misuse");
						}
						
						ResultSet rs45 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs45.first()){
							TMMerchandising = rs45.getLong("merchandising");
						}
						
						ResultSet rs46 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs46.first()){
							TMOther = rs46.getLong("other");
						}
						
						ResultSet rs47 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.is_resolved = 0 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs47.first()){
							TMTotRepair = rs47.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Resolved
						
						
						
						ResultSet rs48 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs48.first()){
							TMRTotMisuse = rs48.getLong("tot_misuse");
						}
						
						ResultSet rs49 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.type_id=3 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs49.first()){
							TMRMerchandising = rs49.getLong("merchandising");
						}
						
						ResultSet rs50 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=4 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs50.first()){
							TMROther = rs50.getLong("other");
						}
						
						ResultSet rs51 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs51.first()){
							TMRTotRepair = rs51.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Verified						
						
						
						ResultSet rs52 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs52.first()){
							TMVTotMisuse = rs52.getLong("tot_misuse");
						}
						
						ResultSet rs53 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs53.first()){
							TMVMerchandising = rs53.getLong("merchandising");
						}
						
						ResultSet rs54 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs54.first()){
							TMVOther = rs54.getLong("other");
						}
						
						ResultSet rs55 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_resolved = 0 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisMonthStartDate)+" and "+Utilities.getSQLDateNext(ThisMonthEndDate));
						if(rs55.first()){
							TMVTotRepair = rs55.getLong("tot_repair");
						}
						
						
						// ---------------------------------------------------------------- 
						
						//Last Month Status
						
						//Raised
						
						
						
						ResultSet rs56 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.type_id=2 and cc.created_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs56.first()){
							LMTotMisuse = rs56.getLong("tot_misuse");
						}
						
						ResultSet rs57 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.type_id=3 and cc.created_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs57.first()){
							LMMerchandising = rs57.getLong("merchandising");
						}
						
						ResultSet rs58 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs58.first()){
							LMOther = rs58.getLong("other");
						}
						
						ResultSet rs59 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.is_resolved = 0 and cc.is_verified=0 and cc.created_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs59.first()){
							LMTotRepair = rs59.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Resolved
						
						
						
						ResultSet rs60 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs60.first()){
							LMRTotMisuse = rs60.getLong("tot_misuse");
						}
						
						ResultSet rs61 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.type_id=3 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs61.first()){
							LMRMerchandising = rs61.getLong("merchandising");
						}
						
						ResultSet rs62 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=4 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs62.first()){
							LMROther = rs62.getLong("other");
						}
						
						ResultSet rs63 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.is_verified=0 and cc.resolved_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs63.first()){
							LMRTotRepair = rs63.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Verified						
						
						
						ResultSet rs64 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=2 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs64.first()){
							LMVTotMisuse = rs64.getLong("tot_misuse");
						}
						
						ResultSet rs65 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 0 and cc.type_id=3 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs65.first()){
							LMVMerchandising = rs65.getLong("merchandising");
						}
						
						ResultSet rs66 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 0 and cc.type_id=4 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs66.first()){
							LMVOther = rs66.getLong("other");
						}
						
						ResultSet rs67 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_resolved = 0 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(LastMonthStartDate)+" and "+Utilities.getSQLDateNext(LastMonthEndDate));
						if(rs67.first()){
							LMVTotRepair = rs67.getLong("tot_repair");
						}
						
						
						// ---------------------------------------------------------------- 
						
						
						//This Year Status
						
						//Raised
						
						
						
						ResultSet rs68 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.type_id=2 and cc.created_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs68.first()){
							TYTotMisuse = rs68.getLong("tot_misuse");
						}
						
						ResultSet rs69 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.type_id=3 and cc.created_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs69.first()){
							TYMerchandising = rs69.getLong("merchandising");
						}
						
						ResultSet rs70 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.type_id=4 and cc.created_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs70.first()){
							TYOther = rs70.getLong("other");
						}
						
						ResultSet rs71 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id where cc.created_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs71.first()){
							TYTotRepair = rs71.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Resolved
						
						
						
						ResultSet rs72 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=2 and cc.resolved_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs72.first()){
							TYRTotMisuse = rs72.getLong("tot_misuse");
						}
						
						ResultSet rs73 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.type_id=3 and cc.resolved_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs73.first()){
							TYRMerchandising = rs73.getLong("merchandising");
						}
						
						ResultSet rs74 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.is_resolved = 1 and cc.type_id=4 and cc.resolved_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs74.first()){
							TYROther = rs74.getLong("other");
						}
						
						ResultSet rs75 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.is_resolved = 1 and cc.resolved_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs75.first()){
							TYRTotRepair = rs75.getLong("tot_repair");
						}
						
						// *******************************//
						
						//Verified						
						
						
						ResultSet rs76 = s.executeQuery("SELECT count(cc.id) tot_misuse FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.type_id=2 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs76.first()){
							TYVTotMisuse = rs76.getLong("tot_misuse");
						}
						
						ResultSet rs77 = s.executeQuery("SELECT count(cc.id) merchandising FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id   where cc.type_id=3 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs77.first()){
							TYVMerchandising = rs77.getLong("merchandising");
						}
						
						ResultSet rs78 = s.executeQuery("SELECT count(cc.id) other FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id    where cc.type_id=4 and cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs78.first()){
							TYVOther = rs78.getLong("other");
						}
						
						ResultSet rs79 = s.executeQuery("SELECT count(cc.id) tot_repair FROM crm_complaints cc join crm_complaints_types cct on cc.type_id = cct.id  where cc.is_verified=1 and cc.verified_on between "+Utilities.getSQLDate(ThisYearStartDate)+" and "+Utilities.getSQLDateNext(ThisYearEndDate));
						if(rs79.first()){
							TYVTotRepair = rs79.getLong("tot_repair");
						}
						
						
						// ---------------------------------------------------------------- 
						
						
						
						long PendingForResolveTotal = PFRTotMisuse+PFRMerchandising+PFROther+PFRTotRepair;
						long PendingForVerificationTotal = PFVTotMisuse+PFVMerchandising+PFVOther+PFVTotRepair;
						
						long TodayStatusRaisedTotal = TSTotMisuse+TSMerchandising+TSOther+TSTotRepair;
						long TodayStatusResolvedTotal = TSRTotMisuse+TSRMerchandising+TSROther+TSRTotRepair;
						long TodayStatusVerifiedTotal = TSVTotMisuse+TSVMerchandising+TSVOther+TSVTotRepair;
						
						long ThisWeekRaisedTotal = TWTotMisuse+TWMerchandising+TWOther+TWTotRepair;
						long ThisWeekResolvedTotal = TWRTotMisuse+TWRMerchandising+TWROther+TWRTotRepair;
						long ThisWeekVerifiedTotal = TWVTotMisuse+TWVMerchandising+TWVOther+TWVTotRepair;
						
						long LastWeekRaisedTotal = LWTotMisuse+LWMerchandising+LWOther+LWTotRepair;
						long LastWeekResolvedTotal = LWRTotMisuse+LWRMerchandising+LWROther+LWRTotRepair;
						long LastWeekVerifiedTotal = LWVTotMisuse+LWVMerchandising+LWVOther+LWVTotRepair;
						
						long ThisMonthRaisedTotal = TMTotMisuse+TMMerchandising+TMOther+TMTotRepair;
						long ThisMonthResolvedTotal = TMRTotMisuse+TMRMerchandising+TMROther+TMRTotRepair;
						long ThisMonthVerifiedTotal = TMVTotMisuse+TMVMerchandising+TMVOther+TMVTotRepair;
						
						long LastMonthRaisedTotal = LMTotMisuse+LMMerchandising+LMOther+LMTotRepair;
						long LastMonthResolvedTotal = LMRTotMisuse+LMRMerchandising+LMROther+LMRTotRepair;
						long LastMonthVerifiedTotal = LMVTotMisuse+LMVMerchandising+LMVOther+LMVTotRepair;
						
						long ThisYearRaisedTotal = TYTotMisuse+TYMerchandising+TYOther+TYTotRepair;
						long ThisYearResolvedTotal = TYRTotMisuse+TYRMerchandising+TYROther+TYRTotRepair;
						long ThisYearVerifiedTotal = TYVTotMisuse+TYVMerchandising+TYVOther+TYVTotRepair;
						
						%>
						
						<tr>
							<td style="text-align:left">Pending for Resolve</td>							
							<td style="text-align:left"><%=PFRTotRepair %></td>
							<td style="text-align:left"><%=PFRTotMisuse %></td>
							<td style="text-align:left"><%=PFRMerchandising %></td>
							<td style="text-align:left"><%=PFROther %></td>							
							<td style="text-align:right"><%=PendingForResolveTotal %></td>						
							
						</tr>
						<tr>
							<td style="text-align:left">Pending for Verification</td>							
							<td style="text-align:left"><%=PFVTotRepair %></td>
							<td style="text-align:left"><%=PFVTotMisuse %></td>
							<td style="text-align:left"><%=PFVMerchandising %></td>
							<td style="text-align:left"><%=PFVOther %></td>							
							<td style="text-align:right"><%=PendingForVerificationTotal %></td>						
							
						</tr>
					<tr  style="background:#ececec">
						<td colspan="6">Today Status</td>
					</tr>
						<tr>
							<td style="text-align:left">Raised</td>							
							<td style="text-align:left"><%=TSTotRepair %></td>
							<td style="text-align:left"><%=TSTotMisuse %></td>
							<td style="text-align:left"><%=TSMerchandising %></td>
							<td style="text-align:left"><%=TSOther %></td>							
							<td style="text-align:right"><%=TodayStatusRaisedTotal %></td>							
							
						</tr>
						<tr>
							<td style="text-align:left">Resolved</td>							
							<td style="text-align:left"><%=TSRTotRepair %></td>
							<td style="text-align:left"><%=TSRTotMisuse %></td>
							<td style="text-align:left"><%=TSRMerchandising %></td>
							<td style="text-align:left"><%=TSROther %></td>							
							<td style="text-align:right"><%=TodayStatusResolvedTotal %></td>							
							
						</tr>
						<tr>
							<td style="text-align:left">Verified</td>							
							<td style="text-align:left"><%=TSVTotRepair %></td>
							<td style="text-align:left"><%=TSVTotMisuse %></td>
							<td style="text-align:left"><%=TSVMerchandising %></td>
							<td style="text-align:left"><%=TSVOther %></td>							
							<td style="text-align:right"><%=TodayStatusVerifiedTotal %></td>					
							
						</tr>
						
						
						<tr  style="background:#ececec">
						<td colspan="6">This Week Status</td>
					</tr>
						<tr>
							<td style="text-align:left">Raised</td>							
							<td style="text-align:left"><%=TWTotRepair %></td>
							<td style="text-align:left"><%=TWTotMisuse %></td>
							<td style="text-align:left"><%=TWMerchandising %></td>
							<td style="text-align:left"><%=TWOther %></td>							
							<td style="text-align:right"><%=ThisWeekRaisedTotal %></td>					
							
						</tr>
						<tr>
							<td style="text-align:left">Resolved</td>							
							<td style="text-align:left"><%=TWRTotRepair %></td>
							<td style="text-align:left"><%=TWRTotMisuse %></td>
							<td style="text-align:left"><%=TWRMerchandising %></td>
							<td style="text-align:left"><%=TWROther %></td>							
							<td style="text-align:right"><%=ThisWeekResolvedTotal %></td>						
							
						</tr>
						<tr>
							<td style="text-align:left">Verified</td>							
							<td style="text-align:left"><%=TWVTotRepair %></td>
							<td style="text-align:left"><%=TWVTotMisuse %></td>
							<td style="text-align:left"><%=TWVMerchandising %></td>
							<td style="text-align:left"><%=TWVOther %></td>							
							<td style="text-align:right"><%=ThisWeekVerifiedTotal %></td>					
							
						</tr>
						
						<tr  style="background:#ececec">
						<td colspan="6">Last Week Status</td>
					</tr>
						<tr>
							<td style="text-align:left">Raised</td>							
							<td style="text-align:left"><%=LWTotRepair %></td>
							<td style="text-align:left"><%=LWTotMisuse %></td>
							<td style="text-align:left"><%=LWMerchandising %></td>
							<td style="text-align:left"><%=LWOther %></td>							
							<td style="text-align:right"><%=LastWeekRaisedTotal %></td>					
							
						</tr>
						<tr>
							<td style="text-align:left">Resolved</td>							
							<td style="text-align:left"><%=LWRTotRepair %></td>
							<td style="text-align:left"><%=LWRTotMisuse %></td>
							<td style="text-align:left"><%=LWRMerchandising %></td>
							<td style="text-align:left"><%=LWROther %></td>							
							<td style="text-align:right"><%=LastWeekResolvedTotal %></td>						
							
						</tr>
						<tr>
							<td style="text-align:left">Verified</td>							
							<td style="text-align:left"><%=LWVTotRepair %></td>
							<td style="text-align:left"><%=LWVTotMisuse %></td>
							<td style="text-align:left"><%=LWVMerchandising %></td>
							<td style="text-align:left"><%=LWVOther %></td>							
							<td style="text-align:right"><%=LastWeekVerifiedTotal %></td>					
							
						</tr>
						
						<tr  style="background:#ececec">
						<td colspan="6">This Month Status</td>
					</tr>
						<tr>
							<td style="text-align:left">Raised</td>							
							<td style="text-align:left"><%=TMTotRepair %></td>
							<td style="text-align:left"><%=TMTotMisuse %></td>
							<td style="text-align:left"><%=TMMerchandising %></td>
							<td style="text-align:left"><%=TMOther %></td>							
							<td style="text-align:right"><%=ThisMonthRaisedTotal %></td>					
							
						</tr>
						<tr>
							<td style="text-align:left">Resolved</td>							
							<td style="text-align:left"><%=TMRTotRepair %></td>
							<td style="text-align:left"><%=TMRTotMisuse %></td>
							<td style="text-align:left"><%=TMRMerchandising %></td>
							<td style="text-align:left"><%=TMROther %></td>							
							<td style="text-align:right"><%=ThisMonthResolvedTotal %></td>						
							
						</tr>
						<tr>
							<td style="text-align:left">Verified</td>							
							<td style="text-align:left"><%=TMVTotRepair %></td>
							<td style="text-align:left"><%=TMVTotMisuse %></td>
							<td style="text-align:left"><%=TMVMerchandising %></td>
							<td style="text-align:left"><%=TMVOther %></td>							
							<td style="text-align:right"><%=ThisMonthVerifiedTotal %></td>					
							
						</tr>
						
						<tr  style="background:#ececec">
						<td colspan="6">Last Month Status</td>
					</tr>
						<tr>
							<td style="text-align:left">Raised</td>							
							<td style="text-align:left"><%=LMTotRepair %></td>
							<td style="text-align:left"><%=LMTotMisuse %></td>
							<td style="text-align:left"><%=LMMerchandising %></td>
							<td style="text-align:left"><%=LMOther %></td>							
							<td style="text-align:right"><%=LastMonthRaisedTotal %></td>					
							
						</tr>
						<tr>
							<td style="text-align:left">Resolved</td>							
							<td style="text-align:left"><%=LMRTotRepair %></td>
							<td style="text-align:left"><%=LMRTotMisuse %></td>
							<td style="text-align:left"><%=LMRMerchandising %></td>
							<td style="text-align:left"><%=LMROther %></td>							
							<td style="text-align:right"><%=LastMonthResolvedTotal %></td>						
							
						</tr>
						<tr>
							<td style="text-align:left">Verified</td>							
							<td style="text-align:left"><%=LMVTotRepair %></td>
							<td style="text-align:left"><%=LMVTotMisuse %></td>
							<td style="text-align:left"><%=LMVMerchandising %></td>
							<td style="text-align:left"><%=LMVOther %></td>							
							<td style="text-align:right"><%=LastMonthVerifiedTotal %></td>					
							
						</tr>
						
						<tr  style="background:#ececec">
						<td colspan="6">This Year Status</td>
					</tr>
						<tr>
							<td style="text-align:left">Raised</td>							
							<td style="text-align:left"><%=TYTotRepair %></td>
							<td style="text-align:left"><%=TYTotMisuse %></td>
							<td style="text-align:left"><%=TYMerchandising %></td>
							<td style="text-align:left"><%=TYOther %></td>							
							<td style="text-align:right"><%=ThisYearRaisedTotal %></td>					
							
						</tr>
						<tr>
							<td style="text-align:left">Resolved</td>							
							<td style="text-align:left"><%=TYRTotRepair %></td>
							<td style="text-align:left"><%=TYRTotMisuse %></td>
							<td style="text-align:left"><%=TYRMerchandising %></td>
							<td style="text-align:left"><%=TYROther %></td>							
							<td style="text-align:right"><%=ThisYearResolvedTotal %></td>						
							
						</tr>
						<tr>
							<td style="text-align:left">Verified</td>							
							<td style="text-align:left"><%=TYVTotRepair %></td>
							<td style="text-align:left"><%=TYVTotMisuse %></td>
							<td style="text-align:left"><%=TYVMerchandising %></td>
							<td style="text-align:left"><%=TYVOther %></td>							
							<td style="text-align:right"><%=ThisYearVerifiedTotal %></td>					
							
						</tr>
					
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