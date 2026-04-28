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
int FeatureID = 141;
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

long OrderID = Utilities.parseLong(request.getParameter("OrderID"));

String WherePackage = "";
String WhereBrand = "";
String OutletName2 = "Orders Activity";

String OrderIDsQuery = "SELECT distinct mo.id FROM mobile_order mo where mo.status_type_id in (1,2) and mo.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"  "+WhereOrderBooker+" "+WhereOutlets+" "+WhereDistributors;



String InvoiceIDsQuery = "select distinct id from inventory_sales_invoices where order_id in ("+OrderIDsQuery+")";

long ComplaintID = Utilities.parseLong(request.getParameter("ComplaintID"));



String SQL = "SELECT * FROM crm_tasks where id="+ComplaintID;

/*if( IsVerified == 1 ){
	SQL = "SELECT * FROM crm_complaints_assigned where is_verified=1 and id="+ComplaintID;
}*/

//System.out.println("IsAssigned = "+IsAssigned);
//System.out.println(SQL);


String OutletID = "";
String OutletName = "";
String OutletAddress = "";
String OutletContactNo = "";
String OutletContactPerson = "";
String ComplaintType = "";
String ComplaintDescription = "";
String ComplaintDescriptionUrdu = "";
String ResolvedDescription = "";

String StatusIsResolved = "No";
String StatusIsSent = "No";

ResultSet rs2 = s.executeQuery(SQL);
if(rs2.first()){
	
	
	if( rs2.getString("is_resolved") != null && rs2.getString("is_resolved").equals("1") ){
		StatusIsResolved = "Yes";
	}
	
	if( rs2.getString("is_sent") != null && rs2.getString("is_sent").equals("1") ){
		StatusIsSent = "Yes";
	}
	
	if( rs2.getString("outlet_id")!= null && !rs2.getString("outlet_id").equals("null") ){
		OutletID = rs2.getString("outlet_id");
	}
	
	if( rs2.getString("outlet_name")!= null && !rs2.getString("outlet_name").equals("null") ){
		OutletName = rs2.getString("outlet_name");
	}
	
	if( rs2.getString("outlet_address")!= null && !rs2.getString("outlet_address").equals("null") ){
		OutletAddress = rs2.getString("outlet_address");
	}
	
	if( rs2.getString("outlet_contact_no")!= null && !rs2.getString("outlet_contact_no").equals("null") ){
		OutletContactNo = rs2.getString("outlet_contact_no");
	}
	
	if( rs2.getString("outlet_contact_person")!= null && !rs2.getString("outlet_contact_person").equals("null") ){
		OutletContactPerson = rs2.getString("outlet_contact_person");
	}
	
	
	
	if( rs2.getString("description")!= null && !rs2.getString("description").equals("null") ){
		ComplaintDescription = rs2.getString("description");
	}
	/*
	if( rs2.getString("urdu_description")!= null && !rs2.getString("urdu_description").equals("null") ){
		ComplaintDescriptionUrdu = rs2.getString("urdu_description"); 
	}
	*/
	if( rs2.getString("resolved_description")!= null && !rs2.getString("resolved_description").equals("null") ){
		ResolvedDescription = rs2.getString("resolved_description"); 
	}
	
	
}



%>

<script>

function ComplaintVerficationFormSubmit(){
	
	
	
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/ComplaintVerificationExecute",
	    data: {
	    	ComplaintID : $('#ComplaintID').val(),
	    	VerifiedDescription : $('#VerifiedDescription').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		//alert('success');
	    		window.location = 'ReportCenter.jsp?ReportID=51';
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

function getOutletName(){
	//alert();
	if(isInteger($('#OutletID').val()) == false ){
		alert('Outlet ID must be a Number');
		$('#OutletID').val('');
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "common/GetOutletBySAPCodeJSON",
		data: {
			SAPCode: $('#OutletID').val(),
			FeatureID:135
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			
			
			
			if(json.exists == "true"){
				$('#OutletName').val(json.OutletName);
				$('#IsOutletValid').val('true');
				getOutletMasterInfo();
			}else{
				$.mobile.hidePageLoadingMsg();
				$('#OutletName').val('Outlet not found');
				$('#OutletAddress').val('');
			}
			
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
	
}

function getOutletMasterInfo()
{
	$.ajax({
		
		url: "sampling/GetOutletInfoJson",
		data: {
			OutletID: $('#OutletID').val(),
			FeatureID:135
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.exists == "true"){
				$("#OutletAddress").val(json.address);
				$("#ContactNo").val(json.owner_tele);
				$("#PersonContactNo").val(json.owner_name);
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
}

function OpenPopup(){
	$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupOutletInit();
	} );
	$('#LookupOutletSearch').popup("open");
}


function OutletSearchCallBackReporCenter(SAPCode, OutletName){
	$('#OutletID').val(SAPCode);
	$('#OutletName').val(OutletName);
	$('#IsOutletValid').val('true');
}

</script>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Complaint</li>
			
			<!--  <li data-role="list-divider" data-theme="c">Status</li>  -->
			
			<li>
			
			<table data-role="table" data-mode="reflow" class="ui-body-d ui-responsive" style="font-size: 10pt; width:30%">
				<thead>
					<tr style="background-color: #f1f1f1">
						<th>Sent</th>
						<th>Resolved</th>
						
					</tr>
				</thead>
				<tr>
					<td><%=StatusIsSent%></td>
					<td><%=StatusIsResolved%></td>
					
				</tr>
			</table>
			</li>
			<li data-role="list-divider" data-theme="c">Detail</li>
			<li>
			
			<form id="ComplaintForm" action="/" method="post" >
			
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
			
				<input type="hidden" name="ComplaintID" id="ComplaintID" value="<%=ComplaintID%>" >
				
				
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td style="border: 0px"><label for="OutletID" data-mini="true">Outlet ID</label><input type="text" name="OutletID" id="OutletID" placeholder="" value="<%=OutletID%>" data-mini="true" onChange="getOutletName(); " readonly="readonly" ></td>
						<td style="border: 0px"><label for="OutletName" data-mini="true">Outlet Name</label><input type="text" name="OutletName" id="OutletName" placeholder="" value="<%=OutletName%>" data-mini="true" readonly="readonly" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2"><label for="OutletAddress" data-mini="true">Outlet Address</label><input type="text" name="OutletAddress" id="OutletAddress" placeholder="" value="<%=OutletAddress%>" data-mini="true" readonly="readonly" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px"><label for="ContactNo" data-mini="true">Contact No</label><input type="text" name="ContactNo" id="ContactNo" placeholder="" value="<%=OutletContactNo%>" data-mini="true" readonly="readonly" ></td>
						<td style="border: 0px"><label for="PersonContactNo" data-mini="true">Contact Person</label><input type="text" name="PersonContactNo" id="PersonContactNo" placeholder="" value="<%=OutletContactPerson%>" data-mini="true" readonly="readonly" ></td>
					</tr>
					
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="Description" data-mini="true">Complaint</label>
							<textarea name="Description" id="Description" style="width: 100%; height: 100px" data-mini="true" readonly="readonly"><%=ComplaintDescription%></textarea>
						</td>
					</tr>
					
					
					
				</table>
			</form>
				</li>
				
				<li data-role="list-divider" data-theme="c">Types</li>
				<li>
					<table>
					
						<%
						//System.out.println("SELECT * FROM crm_complaints_files where id="+ComplaintID);
						ResultSet rs6 = s2.executeQuery("SELECT ctl.id, ctl.type_id, ctt.short_label FROM crm_tasks_list ctl, crm_tasks_types ctt where ctl.type_id=ctt.id and ctl.id="+ComplaintID);
						while(rs6.next()){
						%>
							<tr>
								<td> - <%=rs6.getString("short_label")%></td>
							</tr>
						<%
						}
						%>
					</table>
				</li>
				
				<li data-role="list-divider" data-theme="c">Images Before</li>
				<li>
				
					
					<table align="center">
						<tr>
						
						<%
						//System.out.println("SELECT * FROM crm_complaints_files where id="+ComplaintID);
						ResultSet rs4 = s2.executeQuery("SELECT * FROM crm_tasks_files where is_resolved=0 and id="+ComplaintID);
						while(rs4.next()){
						%>
							<td>
								<img style="-webkit-user-select: none;" src="/WorkflowAttachments/<%=rs4.getString("filename")%>" width="120" >
							</td>
						<%
						}
						%>
						</tr>
					</table>
						
				
				</li>
				
				<li data-role="list-divider" data-theme="c">Images After</li>
				<li>
				
					
					<table align="center" style="width: 100%">
						<tr>
							<td align="center">
								<table>
									<tr>
									
						<%
						//System.out.println("SELECT * FROM crm_complaints_files where id="+ComplaintID);
						ResultSet rs5 = s2.executeQuery("SELECT * FROM crm_tasks_files where is_resolved=1 and id="+ComplaintID);
						while(rs5.next()){
						%>
										<td>
											<img style="-webkit-user-select: none;" src="/WorkflowAttachments/<%=rs5.getString("filename")%>" width="120" >
										</td>
						<%
						}
						%>
									</tr>
								</table>
						
						
							</td>
						</tr>
						<tr>
							<td>
							
								<label for="ResolveDescription" data-mini="true">Resolve Description</label>
								<textarea name="ResolveDescription" id="ResolveDescription" style="width: 100%; height: 100px" data-mini="true" readonly="readonly" ><%=ResolvedDescription%></textarea>
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