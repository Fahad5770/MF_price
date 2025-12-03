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
int FeatureID = 135;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
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

int IsVerified = Utilities.parseInt(request.getParameter("IsVerified"));

String SQL = "SELECT * FROM crm_complaints_assigned where id="+ComplaintID;

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
String FowardTo = "";

ResultSet rs2 = s.executeQuery(SQL);
if(rs2.first()){
	
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
	
	if( rs2.getString("type_id")!= null && !rs2.getString("type_id").equals("null") ){
		ComplaintType = rs2.getString("type_id");
	}
	
	if( rs2.getString("description")!= null && !rs2.getString("description").equals("null") ){
		ComplaintDescription = rs2.getString("description");
	}
	
	if( rs2.getString("urdu_description")!= null && !rs2.getString("urdu_description").equals("null") ){
		ComplaintDescriptionUrdu = rs2.getString("urdu_description"); 
	}
	
	if( rs2.getString("department_id")!= null && !rs2.getString("department_id").equals("null") ){
		FowardTo = rs2.getString("department_id"); 
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

function ComplaintVerficationFormDecline(){
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/ComplaintVerificationExecute",
	    data: {
	    	ComplaintID : $('#ComplaintID').val(),
	    	IsDeclined : "1"
	    	
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
			<li data-role="list-divider" data-theme="a">Forward To</li>
			<li>
			
			<form id="ComplaintForm" action="/" method="post" >
			
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
			
				<input type="hidden" name="ComplaintID" id="ComplaintID" value="<%=ComplaintID%>" >
				
				<input type="hidden" name="Update" id="Update" value="<%=IsVerified%>" >
				
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
						<td style="border: 0px">
							<label for="ComplaintType" data-mini="true">Complaint Type</label>
							
								
								<%
								String ComplaintTypeLabel = "";
								if( !ComplaintType.equals("") ){
									ResultSet rs1 = s2.executeQuery("SELECT * FROM crm_complaints_types where id="+ComplaintType);
									if( rs1.first() ){
										ComplaintTypeLabel = rs1.getString("label");
									}
								}
								%>
								<input type="text" name="ComplaintType" id="ComplaintType" placeholder="" value="<%=ComplaintTypeLabel%>" data-mini="true" readonly="readonly" >
							
							
							
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="Description" data-mini="true">Complaint</label>
							<textarea name="Description" id="Description" style="width: 100%; height: 50px" data-mini="true" readonly="readonly"><%=ComplaintDescription%></textarea>
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="UrduWord" data-mini="true">Complaint ( Urdu )</label>
							<textarea name="UrduWord" id="UrduWord"  style="font-family: Tahoma; font-size: 12pt; text-align: right; width: 100%; height: 50px" data-mini="true"  onkeydown="return changeToUrduinput();" readonly="readonly" ><%=ComplaintDescriptionUrdu%></textarea>
							<input type="hidden" name="UrduWordHidden" id="UrduWordHidden" value="" />
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px">
							<label for="ForwardTo" data-mini="true">Forward To</label>
							
								
								<%
								String FowardToLabel = "";
								if( !FowardTo.equals("") ){
									ResultSet rs3 = s2.executeQuery("SELECT * FROM crm_departments where id="+FowardTo);
									if( rs3.first() ){
										FowardToLabel = rs3.getString("label");
									}
								}
								%>
								<input type="text" name="ForwardTo" id="ForwardTo" placeholder="" value="<%=FowardToLabel%>" data-mini="true" readonly="readonly" >
							
						</td>
					</tr>
					
					
				</table>
			</form>
				</li>
				
				
				
				<li data-role="list-divider" data-theme="c">Images Before</li>
				<li>
				
					
					<table align="center">
						<tr>
						
						<%
						//System.out.println("SELECT * FROM crm_complaints_files where id="+ComplaintID);
						ResultSet rs4 = s2.executeQuery("SELECT * FROM crm_complaints_files where is_resolved=0 and id="+ComplaintID);
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
						ResultSet rs5 = s2.executeQuery("SELECT * FROM crm_complaints_files where is_resolved=1 and id="+ComplaintID);
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
							
							<%
							String ResolvedDescription = "";
							ResultSet rs6 = s2.executeQuery("SELECT resolved_description FROM crm_complaints where id="+ComplaintID);
							if(rs6.first()){
								
								if( rs6.getString("resolved_description")!= null && !rs6.getString("resolved_description").equals("null") ){
									ResolvedDescription = rs6.getString("resolved_description");
								}
							}
							%>
							
								<label for="ResolveDescription" data-mini="true">Resolve Description</label>
								<textarea name="ResolveDescription" id="ResolveDescription" style="width: 100%; height: 50px" data-mini="true" readonly="readonly" ><%=ResolvedDescription%></textarea>
							</td>
						</tr>
						
						<tr>
							<td>
								<%
								String VerifiedDescription = "";
								ResultSet rs7 = s2.executeQuery("SELECT verified_description FROM crm_complaints where id="+ComplaintID);
								if(rs7.first()){
									if( rs7.getString("verified_description")!= null && !rs7.getString("verified_description").equals("null") ){
										VerifiedDescription = rs7.getString("verified_description");
									}
								}
								%> 
														
								<label for="VerifiedDescription" data-mini="true">Verified Description</label>
								<textarea name="VerifiedDescription" id="VerifiedDescription" style="width: 100%; height: 50px" data-mini="true" ><%=VerifiedDescription%></textarea>
							</td>
						</tr>
						
					</table>
						
				
				</li>
				<li data-role="list-divider" data-theme="c">Action</li>
					
				<li>
					<table>
						<tr>
							<td>
								<input type="button" value="Verify" data-theme="a" data-mini="true" onclick="ComplaintVerficationFormSubmit()" <% if(IsVerified == 1){ %> disabled="disabled" <% } %> >
							</td>
							<td style="padding-left: 5px">
								<input type="button" value="Decline" data-theme="c" data-mini="true" onclick="ComplaintVerficationFormDecline()" <% if(IsVerified == 1){ %> disabled="disabled" <% } %> > 
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