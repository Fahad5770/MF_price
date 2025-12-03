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
 <%@page import="com.pbc.common.Region"%>
 
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
int FeatureID = 152;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Region [] RegionObj = UserAccess.getUserFeatureRegion(SessionUserID, FeatureID);

String RegionIds = UserAccess.getRegionQueryString(RegionObj);

//System.out.println("RegionIds = "+RegionIds);

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

//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and cca.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and  cca.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and cca.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
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

//Region


String RegionIDs="";
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	RegionIDs = Utilities.serializeForSQL(SelectedRegionArray);
}

String WhereRegion = "";
if (RegionIDs.length() > 0){
	WhereRegion = " and ct.region_id in ("+RegionIDs+") ";	
}

//System.out.println("Region ID "+RegionIDs);




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
<li data-role="list-divider" data-theme="a">Summary<span style="float:right; "></span></li>
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
							
							
							<th data-priority="1"  style="text-align:center; width: 5% ">Sr#</th>
							<th data-priority="1"  style="text-align:center; width: 10% ">ID</th>							
							<th data-priority="1"  style="text-align:center; width: 25% ">Outlet</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Outlet Contact</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Complaint Type</th>
							<th data-priority="1"  style="text-align:center; width: 30% ">Description</th>							
							
							
							
														
													
					    </tr>
					  </thead> 
					<tbody>
					
					<%
					//System.out.println("SELECT * FROM crm_tasks ct where region_id in (5, 2, 4, 7) and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on > '2014-08-22' and is_resolved=0 and is_sent = 1 and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1");
					
					boolean isHasPicture = false;
					ResultSet rs1 = null;
					
					ResultSet rs = s.executeQuery("SELECT * FROM crm_tasks ct where region_id in ("+RegionIds+") "+WhereRegion+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_on > '2014-08-22' and is_resolved=0 and is_sent = 1 and ifnull((select '1' from crm_tasks_list ctl where ctl.id = ct.id and ((ctl.type_id >= 1 and ctl.type_id <= 14) or (ctl.type_id >= 21 and ctl.type_id <= 24)) limit 1),0) = 1");
					
					int srnumber=1;
					while(rs.next()){
					%>
					
					
						<tr>
							<td style="text-align:left"><%=srnumber %></td>
							<td style="text-align:left"><%=rs.getLong("id") %></td>							
							<td style="text-align:left"><%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %></td>
							<td style="text-align:left"><%=rs.getString("outlet_address") %>  <%=rs.getString("outlet_contact_no") %></td>
							<td style="text-align:left">
							<%
							ResultSet rs2 = s2.executeQuery("SELECT ctt.english_label FROM crm_tasks_list ctl join crm_tasks_types ctt on ctl.type_id = ctt.id where ctl.id = "+rs.getLong("id")+" and ((ctt.id >= 1 and ctt.id <= 14) or (ctt.id >= 21 and ctt.id <= 24))");
							while(rs2.next()){
							%>
							<%=rs2.getString("english_label") %><br/>
							<% 
							}
							%>
							</td>
							<td style="text-align:left"><%=rs.getString("description") %></td>							
							
						</tr>
					<%
					srnumber++;
					}
					%>
					</tbody>
						<input type="hidden" name="SelectedComplaintIDHidden" id="SelectedComplaintIDHidden"/>	
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