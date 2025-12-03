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
int FeatureID = 139;

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
<li data-role="list-divider" data-theme="a">Summary<span style="float:right; "><a href="ReportCenterR135ComplaintListPrint.jsp" style="text-decoration:none; color:white;" target="_blank">Print</a></span></li>
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
							
							
							<th data-priority="1"  style="text-align:center; width: 10% ">Complaint ID</th>							
							<th data-priority="1"  style="text-align:center; width: 15% ">Outlet</th>
							<th data-priority="1"  style="text-align:center; width: 20% ">Outlet Contact</th>
							<th data-priority="1"  style="text-align:center; width: 10% ">Complaint Type</th>
							<th data-priority="1"  style="text-align:center; width: 20% ">Description</th>							
							<th data-priority="1"  style="text-align:center; width: 20% ">Description Urdu</th>
							<th data-priority="1"  style="text-align:center; width: 5% ">Images</th>
							
							
														
													
					    </tr>
					  </thead> 
					<tbody>
					
					<%
					boolean isHasPicture = false;
					ResultSet rs1 = null;
					ResultSet rs = s.executeQuery("SELECT cca.id,cca.outlet_id,cca.outlet_name,cca.outlet_address,cca.outlet_contact_no,cca.type_id,cct.label type_label,cca.description,cca.urdu_description,cca.department_id,cd.label department_label FROM crm_complaints_assigned cca join crm_complaints_types cct on cca.type_id = cct.id join crm_departments cd on cca.department_id = cd.id and cca.department_id=2 join crm_complaints cc on cc.id=cca.id "+WhereDate+" "+ComplaintTypeIDWhere+WhereDistributors+WhereRSM+WhereHOD+WhereSM+WhereTDM+WhereASM+WhereAssigned+WhereResolved+WhereVerified);
					while(rs.next()){
						
						
						isHasPicture = false;
						 rs1 = s2.executeQuery("select filename from crm_complaints_files where id="+rs.getLong("id"));
						 while(rs1.next()){
								if(!rs1.getString("filename").equals("")){
									isHasPicture = true;
								}
							}
					%>
					
					
						<tr>
							<td style="text-align:left"><%=rs.getLong("id") %></td>							
							<td style="text-align:left"><%=rs.getLong("outlet_id") %> - <%=rs.getString("outlet_name") %></td>
							<td style="text-align:left"><%=rs.getString("outlet_address") %>  <%=rs.getString("outlet_contact_no") %></td>
							<td style="text-align:left"><%=rs.getString("type_label") %></td>
							<td style="text-align:left"><%=rs.getString("description") %></td>							
							<td style="text-align:right"><%=rs.getString("urdu_description") %></td>							
							<td style="text-align:left;"><a href="#popupDialog1_<%=rs.getLong("id") %>" style="font-size: 13px;" data-rel="popup"   data-inline="true" data-position-to="window" data-transition="pop" onClick="runSlider(<%=rs.getLong("id")%>)"><%if(isHasPicture){ %>Show<%} %></a></td>
							<!-- <td style="text-align:left"><a href="#popupDialog1" data-rel="popup"  data-position-to="window" data-transition="pop" id="ResolveComplain" onClick="SetComplaintID(<%=rs.getLong("id") %>)">Resolve</a></td> -->
						</tr>
					<%
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
rs.beforeFirst(); //reseting the cursor
while(rs.next()){
%>
<input type="hidden" id="IsSliderAlreadyClicked_<%=rs.getLong("id") %>" value="0"/>

	<div data-role="popup" id="popupDialog1_<%=rs.getLong("id")%>" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width:450px; overflow-y: auto; height: 650px" aclass="ui-corner-all">
        <div style="width:450px; height:600px; b1ackground-color:red; margin:0 auto;">
        
	        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
				<div id="slider_<%=rs.getLong("id")%>">
				    <%
				    ResultSet rs2 = s2.executeQuery("select filename from crm_complaints_files where id="+rs.getLong("id"));
				    while(rs2.next()){			    
				    %>
				    <img src="/WorkflowAttachments/<%=rs2.getString("filename")%>">
				    <%
				    }
				    %>
				   
				</div>
	            
	        </div>
        
        </div>
        
    </div>



<%
}

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>