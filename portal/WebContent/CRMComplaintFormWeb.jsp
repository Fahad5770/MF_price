<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<script src="js/lookups.js"></script>
<script src="js/DictionaryUrduAddWords.js"></script>
<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 138;
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


%>

<script>

function ComplaintFormSubmit(){
	
	if( $('#IsOutletValid').val() == 'false' && $('#OutletID').val() != '0' && $('#OutletID').val() != '' ){
		alert('Please enter Valid Outlet ID');
		document.getElementById("OutletID").focus();
		return false;
	}
	
	if( $('#OutletName').val() == "" ){
		alert("Please enter Outlet Name");
		document.getElementById("OutletName").focus();
		return false;
	}
	
	if( $('#OutletAddress').val() == "" ){
		alert("Please enter Outlet Address");
		document.getElementById("OutletAddress").focus();
		return false;
	}
	
	var urduVal =document.getElementById("UrduWord").value;
	//alert(urduVal.length);
	var urduValConcat="";
	var x  =0;
	for (x = 0; x < urduVal.length; x++){
		urduValConcat += "&#"+urduVal.charCodeAt(x)+";"; //converting to unicode bec we are storing unicodes in db
	}
	document.getElementById("UrduWordHidden").value = urduValConcat;
	
	var CreatedBy = '<%=SessionUserID%>';
	
	var currentdate = new Date(); 
	var datetime = currentdate.getDate()+""
					+ (currentdate.getMonth()+1)+""
					+ currentdate.getFullYear().toString().substring(2,4)+""
					+ currentdate.getHours()+""
					+ currentdate.getMinutes()+""
					+ currentdate.getSeconds();
	
	var CreatedByLength = parseInt(CreatedBy.toString().length);						
	if (CreatedByLength > 4){
		CreatedBy = CreatedBy.toString().substring((CreatedByLength-4),CreatedByLength);
	}
	
	var CurrentComplaintID = CreatedBy + "" + datetime;
	$('#CurrentComplaintID').val( CurrentComplaintID );
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "crm/ComplaintExecute",
	    data: $('#ComplaintForm').serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	
	    	if (json.success == "true"){
	    		
	    		
	    		$('#value1').val( json.InsertedComplaintID );
	    		$('#ComplaintFormFileUpload').submit();
	    		
	    		//window.location = 'CRMComplaintFormWeb.jsp';
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

function OutletSearchCallBackComplaintFormWeb(SAPCode, OutletName){
	$('#OutletID').val(SAPCode);
	$('#OutletName').val(OutletName);
	$('#IsOutletValid').val('true');
}

</script>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<body>

<div data-role="page" id="DeliveryNote" data-url="DeliveryNote" data-theme="d">

    <div data-role="header" data-theme="c" data-position="fixed">
	    <div>
		    <div style="float:left; width:10%">
		    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
		    </div>
		    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;">
		    	<h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Complaint Form</h1>
			</div>
		</div>
	</div>
	
	<div data-role="content" data-theme="d">

			
			
			<form id="ComplaintForm" action="/" method="post" >
			
				<input type="hidden" name="CurrentComplaintID" id="CurrentComplaintID" value="" >
				<input type="hidden" name="IsOutletValid" id="IsOutletValid" value="false" >
				
				<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					
					<tr>
						<td style="border: 0px"><label for="OutletID" data-mini="true">Outlet ID</label><input type="text" name="OutletID" id="OutletID" placeholder="" value="" data-mini="true" onChange="getOutletName();" ondblclick="OpenPopup()" ></td>
						<td style="border: 0px"><label for="OutletName" data-mini="true">Outlet Name</label><input type="text" name="OutletName" id="OutletName" placeholder="" value="" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2"><label for="OutletAddress" data-mini="true">Outlet Address</label><input type="text" name="OutletAddress" id="OutletAddress" placeholder="" value="" data-mini="true" ></td>
					</tr>
					
					<tr>
						<td style="border: 0px"><label for="ContactNo" data-mini="true">Contact No</label><input type="text" name="ContactNo" id="ContactNo" placeholder="" value="" data-mini="true" ></td>
						<td style="border: 0px"><label for="PersonContactNo" data-mini="true">Contact Person</label><input type="text" name="PersonContactNo" id="PersonContactNo" placeholder="" value="" data-mini="true" ></td>
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
							
							
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="Description" data-mini="true">Complaint</label>
							<textarea name="Description" id="Description" style="width: 100%; height: 100px" data-mini="true"></textarea>
						</td>
					</tr>
					
					<tr>
						<td style="border: 0px" colspan="2">
							<label for="UrduWord" data-mini="true">Complaint ( Urdu )</label>
							<textarea name="UrduWord" id="UrduWord"  style="font-family: Tahoma; font-size: 16pt; text-align: right; width: 100%; height: 100px" data-mini="true"  onkeydown="return changeToUrduinput();" ></textarea>
							<input type="hidden" name="UrduWordHidden" id="UrduWordHidden" value="" />
						</td>
					</tr>
					
					
					
					
				</table>
			</form>
			
			<form name="ComplaintFormFileUpload" id="ComplaintFormFileUpload" action="crm/WebUploadImage" method="post" enctype="multipart/form-data" data-ajax="false"  >
			<input type="hidden" name="value1" id="value1" value="" >
			<input type="hidden" name="value2" value="0" >
				<table>
					<tr>
						<td>Upload Images</td>
					</tr>
					<tr>
						<td><input type="file" name="file1" ></td>
						<td><input type="file" name="file2" ></td>
					</tr>
					<tr>
						<td><input type="file" name="file3" ></td>
						<td><input type="file" name="file4" ></td>
					</tr>
					<tr>
						<td><input type="file" name="file5" ></td>
					</tr>
				</table>
			</form>
			
			</div>
			
			
		<div data-role="footer" data-position="fixed" data-theme="b">
			<div>
				<button data-icon="check" data-theme="a" data-inline="true" id="ComplaintFormSaveButton" onClick="ComplaintFormSubmit()">Save</button>
			</div>
		</div>
		
		<jsp:include page="LookupOutletSearchPopup.jsp" > 
	    	<jsp:param value="OutletSearchCallBackComplaintFormWeb" name="CallBack" />
	    	<jsp:param value="<%=FeatureID %>" name="OutletSearchFeatureID" />
	    </jsp:include><!-- Include Outlet Search -->
			
</div>
</body>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>