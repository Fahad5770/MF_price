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
int FeatureID = 218;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

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
		url: "crm/CRMMarketWatchExecuteShell",
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

</script>

<%

ResultSet rs2 = s.executeQuery("SELECT *, (select name from common_distributors cd where cd.distributor_id=cmw.distributor_id) distributor_name, (select label from inventory_packages ip where ip.id=cmwr.package_id) package_name,(select display_name from users u where u.id=cmw.created_by) created_by_name FROM crm_market_watch cmw join crm_market_watch_rates cmwr  on  cmw.id = cmwr.id where cmwr.id="+WatchID);
if(rs2.first()){
	
	
%>
<form id="MarketWatchForm" action="/" method="post" >
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Detail</li>
			<li>
			
			
			
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
			
				<input type="hidden" name="WatchID" id="ComplaintID" value="<%=rs2.getString("id")%>" >
				
				
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td style="border: 0px"><label for="OutletID" data-mini="true">Outlet ID</label><input type="text" name="OutletID" id="OutletID" placeholder="" value="<%=rs2.getInt("outlet_id")%>" data-mini="true" onChange="getOutletName(false); " onclick="OpenPopup()" ></td>
						<td style="border: 0px"><label for="OutletName" data-mini="true">Outlet Name</label><input type="text" name="OutletName" id="OutletName" placeholder="" value="<%=rs2.getString("outlet_name")%>" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2"><label for="OutletAddress" data-mini="true">Outlet Address</label><input type="text" name="OutletAddress" id="OutletAddress" placeholder="" value="<%=rs2.getString("outlet_address")%>" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px"><label for="DistID" data-mini="true">Distributor ID</label><input type="text" name="DistID" id="DistID" placeholder="" value="<%=rs2.getInt("distributor_id")%>" data-mini="true" ></td>
						<td style="border: 0px"><label for="DistName" data-mini="true">Distributor Name</label><input type="text" name="DistName" id="DistName" placeholder="" value="<%=rs2.getString("distributor_name")%>" data-mini="true" ></td>
					</tr>
					
					
					
					
					
					
					<tr>
						<td style="border: 0px">
							<label for="RegionID" data-mini="true">Region</label>
							<select name="RegionID" id="RegionID" data-mini="true" >
								<option value="">Select</option>
								<%
								ResultSet rs5 = s2.executeQuery("SELECT * FROM common_regions");
								while( rs5.next() ){
									%>
										<option value="<%=rs5.getString("region_id")%>" <%if(rs2.getInt("region_id")==rs5.getInt("region_id")){ %> selected <%} %>><%=rs5.getString("region_short_name")%> - <%=rs5.getString("region_name")%></option>
									<%
								}
								%>
								
							</select>
							
							
							
							
						</td>
						<td>
							<label  data-mini="true" for="CreatedBy">Created By</label>
							<input type="text" name="CreatedBy" id="CreatedBy" placeholder="" value="<%=rs2.getString("created_by")%> - <%=rs2.getString("created_by_name")%>" data-mini="true" readonly>
							
						</td>
						
						
					</tr>
					
					
					
				</table>
			
				</li>
				
				<%
}
int RowNumber=0;
				ResultSet rs3 = s2.executeQuery("SELECT *, (select label from inventory_packages ip where ip.id=package_id) package_name FROM crm_market_watch_rates where id="+WatchID);
				while(rs3.next()){
					
				
				if(rs3.getInt("company_id")==1){
					MarketWatchRateName = "Pepsi";
				}else if(rs3.getInt("company_id")==2){
					MarketWatchRateName = "Coke";
				}else if(rs3.getInt("company_id")==3){
					MarketWatchRateName = "Gourmet";
				}
				
				
				
				
				%>
				
				<%if(!MarketWatchRateName1.equals(MarketWatchRateName)){%>
					<li data-role="list-divider" data-theme="c"><%=MarketWatchRateName %></li>
					
					
				<%} %>
				
				
				
				<li>
					<table   style="font-size:13px; font-weight: 400; width:100%">
						<%if(!MarketWatchRateName1.equals(MarketWatchRateName)){%>
						<tr>
							<th style="text-align:left; width:30%">Package</th>
							<th style="text-align:left; width:30%">Rate</th>
							<th style="text-align:left; width:30%">Remarks</th>
							<th style="text-align:left; width:10%"></th>
						</tr>
						<%} %>
						<tr id="MWItemRow_<%=RowNumber %>">
							<td style="text-align:left;width:30%">
							<%=rs3.getString("package_name") %>
							<input type="hidden" name='mwpackageid' value='<%=rs3.getLong("package_id") %>'/>
							<input type="hidden" name='companyid' value='<%=rs3.getInt("company_id") %>'/>
							</td>
							<td style="text-align:left;width:30%">
							<input type="text" name='rate' value='<%=rs3.getDouble("rate") %>'/>
							</td>
							<td style="text-align:left;width:30%"><input type="text" name='remarks' value='<%=rs3.getString("promotion_remarks") %>'/></td>
							<td style="text-align:left;width:10%"><a href="#" onClick="MWDeleteRow('MWItemRow_<%=RowNumber %>','<%=rs3.getInt("id") %>','<%=rs3.getInt("company_id") %>','<%=rs3.getInt("package_id") %>')">Delete</a></td>
						</tr>
					
					</table>
					</li>
					
			
						
				
				
				
				
			<%
			
		
			
			MarketWatchRateName1 = MarketWatchRateName;
			RowNumber++;
				}

			%>
			
			
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
c.close();
ds.dropConnection();
%>