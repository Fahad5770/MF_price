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

int IsAssigned = Utilities.parseInt(request.getParameter("IsAssigned"));

String SQL = "SELECT * FROM crm_complaints where id="+ComplaintID;

if( IsAssigned == 1 ){
	SQL = "SELECT *, (SELECT name FROM common_distributors where distributor_id=crm_complaints_assigned.distributor_id) distributor_name FROM crm_complaints_assigned where id="+ComplaintID;
}


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
String RegionID = "";
String DistributorID = "";
String DistributorName = "";

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
	
	if( IsAssigned == 1 ){
		
		if( rs2.getString("department_id")!= null && !rs2.getString("department_id").equals("null") ){
			FowardTo = rs2.getString("department_id"); 
		}
		
		if( rs2.getString("region_id")!= null && !rs2.getString("region_id").equals("null") ){
			RegionID = rs2.getString("region_id"); 
		}
		
		if( rs2.getString("distributor_id")!= null && !rs2.getString("distributor_id").equals("null") ){
			DistributorID = rs2.getString("distributor_id");
			DistributorName = rs2.getString("distributor_name");
		}
		
	}
	
}


%>

<script>

function ComplaintFormSubmit(){
	
	if( $('#IsOutletValid').val() == 'false' && $('#OutletID').val() != '0' && $('#OutletID').val() != '' ){
		alert('Please enter Valid Outlet ID');
		document.getElementById("OutletID").focus();
		return false;
	}
	
	if( $('#RegionID').val() == "" ){
		alert('Please select Region');
		document.getElementById("RegionID").focus();
		return false;
	}
	/*
	if( $('#DistributorID').val() == "" ){
		alert('Please select Distributor');
		document.getElementById("DistributorID").focus();
		return false;
	}
	*/
	var urduVal =document.getElementById("UrduWord").value;
	//alert(urduVal.length);
	var urduValConcat="";
	var x  =0;
	for (x = 0; x < urduVal.length; x++){
		urduValConcat += "&#"+urduVal.charCodeAt(x)+";"; //converting to unicode bec we are storing unicodes in db
	}
	document.getElementById("UrduWordHidden").value = urduValConcat;
	
	
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/ComplaintFormExecute",
	    data: $('#ComplaintForm').serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		window.location = 'ReportCenter.jsp?ReportID=48';
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

function getOutletName(isOnLoad){
	//alert();
	if(isOnLoad == false){
		
		if(isInteger($('#OutletID').val()) == false ){
			alert('Outlet ID must be a Number');
			$('#OutletID').val('');
			return false;
		}
		
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
				//$('#OutletName').val('Outlet not found');
				//$('#OutletAddress').val('');
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
				
				$('#RegionID').val(json.region_id_csd).change();
				$("#DistributorID").val(json.distributor_id_csd);
				getDistributorName();
				
			}else{
				$.mobile.hidePageLoadingMsg();
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

function getDistributorName(){
	
	//alert(DistributorIds.length);
	if(isInteger($('#DistributorID').val()) == false ){
		$('#DistributorID').val('');
		return false;
	}
	
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: $('#DistributorID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#DistributorName').val(json.DistributorName);
			}else{
				$('#DistributorID').val("");
				$('#DistributorName').val("");
			}
			
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

getOutletName(true);

</script>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Forward To</li>
			<li>
			
			<form id="ComplaintForm" action="/" method="post" >
			
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
			
				<input type="hidden" name="ComplaintID" id="ComplaintID" value="<%=rs2.getString("id")%>" >
				
				<input type="hidden" name="Update" id="Update" value="<%=IsAssigned%>" >
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td style="border: 0px"><label for="OutletID" data-mini="true">Outlet ID</label><input type="text" name="OutletID" id="OutletID" placeholder="" value="<%=OutletID%>" data-mini="true" onChange="getOutletName(false); " ondblclick="OpenPopup()" ></td>
						<td style="border: 0px"><label for="OutletName" data-mini="true">Outlet Name</label><input type="text" name="OutletName" id="OutletName" placeholder="" value="<%=OutletName%>" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2"><label for="OutletAddress" data-mini="true">Outlet Address</label><input type="text" name="OutletAddress" id="OutletAddress" placeholder="" value="<%=OutletAddress%>" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px"><label for="ContactNo" data-mini="true">Contact No</label><input type="text" name="ContactNo" id="ContactNo" placeholder="" value="<%=OutletContactNo%>" data-mini="true" ></td>
						<td style="border: 0px"><label for="PersonContactNo" data-mini="true">Contact Person</label><input type="text" name="PersonContactNo" id="PersonContactNo" placeholder="" value="<%=OutletContactPerson%>" data-mini="true" ></td>
					</tr>
					<tr>
						<td style="border: 0px">
							<label for="ComplaintType" data-mini="true">Complaint Type</label>
							<select name="ComplaintType" id="ComplaintType" data-mini="true" >
								
								<%
								ResultSet rs1 = s2.executeQuery("SELECT * FROM crm_complaints_types");
								while( rs1.next() ){
									%>
										<option value="<%=rs1.getString("id")%>"><%=rs1.getString("label")%></option>
									<%
								}
								%>
								
							</select>
							
							<script>
								$('#ComplaintType').val(<%=ComplaintType%>).change();
							</script>
							
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="Description" data-mini="true">Complaint</label>
							<textarea name="Description" id="Description" style="width: 100%; height: 50px" data-mini="true"><%=ComplaintDescription%></textarea>
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="UrduWord" data-mini="true">Complaint ( Urdu )</label>
							<textarea name="UrduWord" id="UrduWord"  style="font-family: Tahoma; font-size: 12pt; text-align: right; width: 100%; height: 50px" data-mini="true"  onkeydown="return changeToUrduinput();" ><%=ComplaintDescriptionUrdu%></textarea>
							<input type="hidden" name="UrduWordHidden" id="UrduWordHidden" value="" />
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px">
							<label for="ForwardTo" data-mini="true">Forward To</label>
							<select name="ForwardTo" id="ForwardTo" data-mini="true" >
								
								<%
								ResultSet rs3 = s2.executeQuery("SELECT * FROM crm_departments");
								while( rs3.next() ){
									%>
										<option value="<%=rs3.getString("id")%>"><%=rs3.getString("label")%></option>
									<%
								}
								%>
								<script>
									$('#ForwardTo').val("<%=FowardTo%>").change();
								</script>
							</select>
						</td>
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
										<option value="<%=rs5.getString("region_id")%>"><%=rs5.getString("region_short_name")%> - <%=rs5.getString("region_name")%></option>
									<%
								}
								%>
								
							</select>
							
							
							<script>
									$('#RegionID').val("<%=RegionID%>").change();
								</script>
							
						</td>
						
						<td style="width: 50%; border: 0px;">
							<label for="DistributorID" data-mini="true" style="margin: 0px">Distributor</label>
							<table border="0" style="width: 100%" cellpadding="0" cellspacing="0" style="border: 0px" >
								<tr>
									<td style="width: 20%; border: 0px; padding: 0px"><input type="text" name="DistributorID" id="DistributorID" value="<%=DistributorID%>" data-mini="true" onChange="getDistributorName()" ></td>
									<td style="width: 1%; border: 0px; padding: 0px">&nbsp;</td>
									<td style="width: 79%; border: 0px; padding: 0px"><input type="text" name="DistributorName" id="DistributorName" value="<%=DistributorName%>" data-mini="true" readonly="readonly" ></td>
								</tr>
							</table>
						</td>
						
					</tr>
					
					
					
				</table>
			</form>
				</li>
				
				<li data-role="list-divider" data-theme="c">Action</li>
				
				<li>
					<table>
						<tr>
							<td>
								<input type="button" value="Foward" data-theme="a" data-mini="true" onclick="ComplaintFormSubmit()" >
							</td>
						</tr>
					</table>
				</li>
				
				<li data-role="list-divider" data-theme="c">Images</li>
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
					
			</ul>



<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>