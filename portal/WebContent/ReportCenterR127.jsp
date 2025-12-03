<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>

<script src="js/jquery.excoloSlider.min.js"></script>
<link href="css/jquery.excoloSlider.css" rel="stylesheet">

<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

//for slider images

$(function () {
   // $("#slider").excoloSlider();
});


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
int FeatureID = 129;

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

//System.out.println("sDate "+StartDate);

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
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
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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

//OrderBooker

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
	OrderBookerIDsWher =" and assigned_to in ("+OrderBookerIDs+") ";
}


SalesIndex si = new SalesIndex(StartDate,EndDate);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Complaint List</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; width:12%;" >Outlet</th>
							<th data-priority="1"  style="text-align:center; width:12%;" >Address</th>
							<th data-priority="1"  style="text-align:center; width:10%;" >Contact#</th>
							<th data-priority="1"  style="text-align:center; width:12%;" >Distributor</th>
							<th data-priority="1"  style="text-align:center; width:12%;" >Complaint Type</th>
							<th data-priority="1"  style="text-align:center; width:12%;" >Description</th>
							<th data-priority="1"  style="text-align:center; width:10%;" >Images</th>														
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						//System.out.println("SELECT cc.outlet_id,co.name,co.address,(select contact_number from common_outlets_contacts coc where coc.outlet_id=cc.outlet_id) contact_number,type_id,(select label from crm_complaints_types cct where cct.id=cc.type_id) complaint_type,description FROM crm_complaints cc join common_outlets co on co.id=cc.outlet_id where cc.outlet_id in (select outlet_id from distributor_beat_plan_all_view where 1=1"+WhereDistributors+WhereRSM+WhereHOD+")");
						ResultSet rs = s.executeQuery("SELECT cc.id,cc.outlet_id,cc.outlet_name,outlet_address,outlet_contact_no,type_id,(select label from crm_complaints_types cct where cct.id=cc.type_id) complaint_type,description FROM crm_complaints cc  where cc.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
						while(rs.next()){
							String Distributor="";
							String OrderBooker="";
							if(rs.getLong("outlet_id")!=0){
								//ResultSet rs1 = s2.executeQuery("SELECT distinct distributor_id,distributor_name FROM distributor_beat_plan_all_view where outlet_id="+rs.getLong("outlet_id"));
								ResultSet rs1 = s2.executeQuery("SELECT distinct dbp.distributor_id, (select name from common_distributors cd where distributor_id = dbp.distributor_id) distributor_name FROM distributor_beat_plan dbp join distributor_beat_plan_schedule dbps on dbp.id = dbps.id where dbps.outlet_id = "+rs.getLong("outlet_id"));
								while(rs1.next()){
									Distributor +=rs1.getLong("distributor_id")+" - "+rs1.getString("distributor_name")+"<br/>";										
											
								}
								/*
								ResultSet rs2 = s2.executeQuery("SELECT distinct assigned_to,assigned_to_name FROM distributor_beat_plan_all_view where outlet_id="+rs.getLong("outlet_id"));
								while(rs2.next()){
									OrderBooker +=rs2.getLong("assigned_to")+" - "+rs2.getString("assigned_to_name")+"<br/>";										
											
								}*/
							}
							
						%>
						
						
						<tr>
							<td><%=rs.getLong("outlet_id")+" - "+rs.getString("outlet_name") %></td>
							<td><%=rs.getString("outlet_address") %></td>
							<td><%=rs.getString("outlet_contact_no") %></td>
							<td><%=Distributor %></td>
							<td><%=rs.getString("complaint_type") %></td>
							<td><%=rs.getString("description") %></td>
							<td style="text-align:center;"><a href="#popupDialog_<%=rs.getLong("id") %>" style="font-size: 10px;" data-rel="popup"   data-inline="true" data-position-to="window" data-transition="pop" onClick="runSlider(<%=rs.getLong("id")%>)">Show</a></td>
							
						</tr>
						<%} %>
					</tbody>
							
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

	<div data-role="popup" id="popupDialog_<%=rs.getLong("id")%>" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width:800px; overflow-y: auto; height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			<div id="slider_<%=rs.getLong("id")%>">
			    <%
			    ResultSet rs1 = s2.executeQuery("select filename from crm_complaints_files where id="+rs.getLong("id"));
			    while(rs1.next()){			    
			    %>
			    <img src="/WorkflowAttachments/<%=rs1.getString("filename")%>">
			    <%
			    }
			    %>
			   
			</div>
            
        </div>
    </div>

<%
}
si.close();


s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>