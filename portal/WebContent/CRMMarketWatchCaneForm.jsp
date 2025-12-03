<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 248;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionKSML();
Connection cCane = ds.getConnection();
Statement s = cCane.createStatement();
Statement s2 = cCane.createStatement();
Statement s3 = cCane.createStatement();

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

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	//
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}*/
}

long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");           	
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and mo.outlet_id in ("+OutletIds+") ";	
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
	WhereOrderBooker = " and mo.created_by in ("+OrderBookerIDs+") ";
}






long WatchID = Utilities.parseLong(request.getParameter("WatchID"));

//System.out.println(WatchID);

String OutletID = "";
String OutletName = "";
String OutletAddress = "";
String OutletContactNo = "";
String OutletContactPerson = "";

String ComplaintDescription = "";
//String ComplaintDescriptionUrdu = "";
String FowardTo = "";
String RegionID = "";
String DistributorID = "";
String DistributorName = "";
boolean isResolved = false;
String ResolveDescription = "";
String MarketWatchRateName ="";
String MarketWatchRateName1 ="";
boolean RowFlag=false;

%>

<script>

function TaskFormSubmit(){
	
	
	if( $('#OutletID').val() == '0' && $('#OutletID').val() == '' ){
		alert('Please enter Valid Outlet ID');
		document.getElementById("OutletID").focus();
		return false;
	}
	
	if( $('#RegionID').val() == "" ){
		alert('Please select Region');
		document.getElementById("RegionID").focus();
		return false;
	}
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/MarketWatchFormExecute",
	    data: $('#MarketWatchForm').serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		//window.location = 'ReportCenter.jsp?ReportID=96';
	    	}else{
	    		alert('Server could not be reached.');
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}


function MWDeleteRow(RowID,ID,CompanyID,PackageID){
	
	$.ajax({
	    url: "crm/MarketWatchDeleteExecute",
	    data: {
	    	WatchID: ID,
	    	mwpackageid: PackageID,
	    	companyid: CompanyID
		},
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		$("#"+RowID).remove();
	    	}else{
	    		alert('Server could not be reached.');
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
	
	
}

function MWGenerateEmail(CID){
	
	
	$.ajax({			
		//url: "util/BiBatch",
		url: "crm/CRMMarketWatchCaneExecuteShell",
		data: {
			CommandID: CID						
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.success == "true"){
				$("#LogOutputMsg").html(json.logmsg);
				$("#SyncInProgressStatus").html("Done.");
				
			}else{
				$("#SyncInProgressStatus").html("The request could not be completed.");
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
			$("#SyncInProgressStatus").html("The request could not be completed.");
		}
	});
}

function SaveMessage(){
	$.ajax({
	    url: "crm/CRMMarketWatchCaneSaveEmail",
	    data: {
	    	
	    	CRMMWCaneMsg: $("#EmailMessage").val()	    	
		},
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		$("#EmailMessage").val("");
	    	}else{
	    		alert('Server could not be reached.');
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
}

</script>

<%

ResultSet rs2 = s.executeQuery("SELECT id,mill_id,(select label from crman_mills where id=mill_id) mill_name,center_type,center_id,(select name from crman_centers where id=center_id) center_name,(select circle from crman_centers where id=center_id) center_circle,rate,comments FROM crman_valid_messages where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and id="+WatchID);
if(rs2.first()){
	
	
%>
<form id="MarketWatchForm" action="/" method="post" >
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Detail</li>
			<li>
			
			
			
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
			
				<input type="hidden" name="WatchID" id="CaneID" value="<%=rs2.getString("id")%>" >
				
				
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td style="border: 0px"><label for="MillID" data-mini="true">Mill ID</label><input type="text" name="MillID" id="MillID" placeholder="" value="<%=rs2.getInt("mill_id")%>" data-mini="true"  readonly></td>
						<td style="border: 0px"><label for="MillName" data-mini="true">Mill Name</label><input type="text" name="MillName" id="MillName" placeholder="" value="<%=rs2.getString("mill_name")%>" data-mini="true" readonly></td>
					</tr>
					
					<tr>
						<td style="border: 0px" ><label for="CenterType" data-mini="true">Type</label><input type="text" name="CenterType" id="CenterType" placeholder="" value="<%=rs2.getString("center_type")%>" data-mini="true" readonly></td>
						<td style="border: 0px" ><label for="CaneRate" data-mini="true">Rate</label><input type="text" name="CaneRate" id="CaneRate" placeholder="" value="<%=rs2.getString("rate")%>" data-mini="true" readonly></td>
					</tr>
					
					<tr>
						<td style="border: 0px"><label for="CenterID" data-mini="true">Center ID</label><input type="text" name="CenterID" id="CenterID" placeholder="" value="<%=rs2.getInt("center_id")%>" data-mini="true" readonly></td>
						<td style="border: 0px"><label for="CenterName" data-mini="true">Center Name</label><input type="text" name="CenterName" id="CenterName" placeholder="" value="<%=rs2.getString("center_name")%>" data-mini="true" readonly></td>
						
					</tr>
					<tr>
						<td style="border: 0px"><label for="CenterCircle" data-mini="true">Center Circle</label><input type="text" name="CenterCircle" id="CenterCircle" placeholder="" value="<%=rs2.getString("center_circle")%>" data-mini="true" readonly></td>
						<td style="border: 0px" ><label for="Comments" data-mini="true">Comments</label><input type="text" name="Comments" id="Comments" placeholder="" value="<%=rs2.getString("comments")%>" data-mini="true" readonly></td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2"><label for="CenterCircle" data-mini="true">Email Body</label><textarea cols="40" rows="8" name="EmailMessage" id="EmailMessage"></textarea></td>
						
					</tr>
					<tr>
						<td style="border: 0px" ></td>
						<td style="border: 0px; width:10%" ><input type="button" value="Save Message" data-theme="c" data-mini="true" onclick="SaveMessage();" ></td>
						
					</tr>
					
					
					
					
					
					
					
					
					
				</table>
			
				</li>
				
				<%
}
%>
			
			<!-- 
				<li data-role="list-divider" data-theme="c">Action</li>
				
				<li>
					<table>
						<tr>
							
							<td style="padding-left: 5px">
								<input type="button" value="Save" data-theme="c" data-mini="true" onclick="TaskFormSubmit()" >
							</td>
							
						</tr>
					</table>
				</li>
				 -->
				
			<li data-role="list-divider" data-theme="c">Send Email</li>
				
				<li>
					<table>
						<tr>
							<td style="padding-left: 5px">
								<input type="button" value="Test" data-theme="c" data-mini="true" onclick="MWGenerateEmail(1);" >
							</td>
							<td style="padding-left: 5px">
								<input type="button" value="Live" data-theme="c" data-mini="true" onclick="MWGenerateEmail(2);" >
							</td>
							<td>&nbsp;</td>
							<td>
								<span id="LogOutputMsg"></span> <br/>
								<span id="SyncInProgressStatus"></span>
							</td>
						</tr>
					</table>
				</li>	
			
				
				
					
			</ul>
	</form>	


<%
s3.close();
s2.close();
s.close();
cCane.close();
ds.dropConnection();
%>