<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 156;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long EditID = Utilities.parseLong(request.getParameter("EditID"));
boolean isEditCase = false;

if(EditID > 0){
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

String OutletID = "";
String Month = "";
String Year = "";
String Type = "";
String Amount = "";
String SlipDescription = "";
String Description = "";



%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script>

var RowCount = 0;
var isEditCaseJS = <%=isEditCase%>;
var EditID = <%=EditID%>;

$( document ).delegate("#CreditSlipDistributorPage", "pageinit", function() {
	if(isEditCaseJS){
		//getOutletName();
		//$('#NoProductRow').css('display', 'none');
		EditCreditSlip(EditID);
		
	}
	
	$("#FromDateSpan").click( function( event ) {
		document.getElementById("FromDate").disabled = false;
		document.getElementById("ToDate").disabled = false;
		$("#FromDate").focus();
		
	});	
	$("#ToDateSpan").on( "click", function( event, ui ) {
		document.getElementById("FromDate").disabled = false;
		document.getElementById("ToDate").disabled = false;
		$("#ToDate").focus();
	});	
	
});

function CreditSlipSubmit(){
	
	if( $('#DistributorID').val() == "" ){
		alert("Please select Distributor");
		document.getElementById("DistributorID").focus();
		return false;
	}
	
	if( $('#SlipDescription').val() == "" ){
		alert("Please enter Slip Description");
		document.getElementById("SlipDescription").focus();
		return false;
	}
	
	
	$('#SamplingCreditSlipMonth').val( $('#Month').val() );
	$('#SamplingCreditSlipYear').val( $('#Year').val() );
	$('#SamplingCreditSlipType').val( $('#Type').val() );
	$('#SamplingCreditSlipDistributorID').val( $('#DistributorID').val() );
	
	$('#SamplingCreditSlipDescription').val( $('#SlipDescription').val() );
	$('#SamplingCreditSlipDescriptionOfficial').val( $('#Description').val() );
	
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "sampling/CreditSlipDistributorExecute",
	    data: $('#OutletForm').serialize(),
	    
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	
	    	if (json.success == "true"){
	    		window.location = 'SamplingCreditSlipDistributor.jsp';
	    	}else{
	    		alert(json.error);
	    	}
	    	
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}


function getOutletName(){
	//alert('function');
	if(isInteger($('#OutletID').val()) == false ){
		$('#OutletID').val('');
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "common/GetOutletBySAPCodeJSON",
		data: {
			SAPCode: $('#OutletID').val(),
			FeatureID:<%=FeatureID%>
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			
			if(json.exists == "true"){
				$('#OutletName').val(json.OutletName);
				
				getOutletMasterInfo();
			}else{
				$.mobile.hidePageLoadingMsg();
				
				$('#OutletID').val('');
				$('#OutletName').val('');
				//$('#OutletAddress').html('');
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
			FeatureID:<%=FeatureID%>
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			
			$.mobile.hidePageLoadingMsg();
			
			if(json.exists == "true"){
				//$("#DeskSaleDistributorName").val(json.distributor_id+" - "+json.distributor_name);	
				//$('#DeskSaleDistributorIDHidden').val(json.distributor_id);
				//$("#OutletAddress").html(json.address+" ("+json.owner_name+", "+json.owner_tele+")");
				
				
				
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

function openOutletSelectionPopup(){
	$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupOutletInit();
	} );
	$('#LookupOutletSearch').popup("open");
}

function openDistributorSelectionPopup(){
	$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
		//alert("hELLO");
		lookupDistributorInit();
	} );
	$('#LookupDistributorSearch').popup("open");
}

function OutletSearchCallBackCreditSlip(SAPCode, OutletName){
	$('#OutletID').val(SAPCode);
	$('#OutletName').val(OutletName);
	getOutletMasterInfo();
}

function DistributorSearchCallBackCreditSlip(SAPCode, DistributorName){
	$('#DistributorID').val(SAPCode);
	$('#DistributorName').val(DistributorName);
}

function showSearchContent(){
	
	$("#SearchContent").focus();
	
	document.getElementById("FromDate").disabled = "disabled";
	document.getElementById("ToDate").disabled = "disabled";
	
	$.get('SamplingCreditSlipDistributorSearch.jsp?FromDate='+$('#FromDate').val()+'&ToDate='+$('#ToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function isInteger (o) {
	if (o.length > 0){
		var val = parseFloat(o);
		if (!isNaN(val)){
		  return ! isNaN (o-0) && o != null ;
		}else{
			return false;
		}
	}else{
		return true;
	}
}

function AddRow(){
	//alert("add row");
	
	if( $('#OutletID').val() == '' && $('#OutletName').val() == '' ){
		$('#OutletID').focus();
		return false;
	}
	
	if( $('#Amount').val() == '' ){
		$('#Amount').focus();
		return false;
	}else{
		if( isInteger( $('#Amount').val() ) == false ){
			$('#Amount').focus();
			return false;
		}
	}
	
	var OutletID = $('#OutletID').val();
	var OutletName = $('#OutletName').val();
	var Amount = $('#Amount').val();
	
	
	var isAlreadyEntered = false;
	var RowLength = $('input[name=OutletIDList]').length;
	for(var i = 0; i < RowLength; i++){
		if( $('input[name=OutletIDList]')[i].value == OutletID && $('input[name=OutletNameList]')[i].value == OutletName  ){
			isAlreadyEntered = true;
		}
	}
	
	if( isAlreadyEntered ){
		
		
		
		return false;
	}
	
	//alert( isInteger( OutletID ) );
	
	if( OutletID != "" && isInteger( OutletID ) ){
	
		///////////////////
		$.mobile.showPageLoadingMsg();
		$.ajax({
			
			url: "common/GetOutletBySAPCodeJSON",
			data: {
				SAPCode: OutletID,
				FeatureID:<%=FeatureID%>
			},
			type:"POST",
			dataType:"json",
			success:function(json){
				
				$.mobile.hidePageLoadingMsg();
				
				if(json.exists == "true"){
					//json.OutletName
					
					populateAddRow( json.OutletName );
					
				}
			},
			error:function(xhr, status){
				$.mobile.hidePageLoadingMsg();
				alert("Server could not be reached.");
			}
			
		});
		
		///////////////////
	
	}else{
		
		populateAddRow( $('#OutletName').val() );
		
	}
	
	return false;
}

function populateAddRow(OutletName){
	
	var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
	
	var OutletInfo = $('#OutletID').val()+" - "+OutletName;
	if( $('#OutletID').val() == "" ){
		OutletInfo = OutletName;
	}
	
	var content = "<tr id='SamplingCreditSlip"+RowMaxID+"'>";
	content += "<td><input type='hidden' name='OutletIDList' id='OutletIDList' value='"+$('#OutletID').val()+"' ><input type='hidden' name='OutletNameList' id='OutletNameList' value='"+OutletName+"' >"+OutletInfo+"</td>";
	content += "<td><input type='hidden' name='AmountList' id='AmountList' value='"+$('#Amount').val()+"' >"+$('#Amount').val()+"</td>";
	content += "<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeleteRow('SamplingCreditSlip"+RowMaxID+"');\">Delete</a></td>";	
	content += "</tr>";
	
	$("#TableBody").append(content).trigger('create');
	RowCount++;
	$('#NoProductRow').css('display', 'none');
	$('#OutletID').val('');
	$('#OutletName').val('');
	$('#Amount').val('');
	$('#isValidOutlet').val('0');
	$('#OutletName').html('');
	$('#OutletAddress').html('');
	$('#OutletID').focus();
	$('#RowMaxID').val(RowMaxID);
	
}

function EditCreditSlip(EditIDParam){
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "sampling/CreditSlipDistributorInfoJson", 
	    data: {
	    	EditID: EditIDParam
	    },
	    
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	
	    	if (json.success == "true"){
	    		
	    		$('#Month').val( json.Month ).change();
	    		$('#Year').val( json.Year).change();
	    		$('#Type').val( json.Type ).change();
	    		
	    		$('#DistributorID').val( json.DistributorID );
	    		$('#DistributorName').val( json.DistributorName );
	    		
	    		$('#SlipDescription').val( json.SlipDescription );
	    		$('#Description').val( json.Description );
	    		
	    		for(var i = 0; i < json.rows.length; i++){
	    			
	    			var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
	    			
	    			var OutletInfo = json.rows[i].OutletID+" - "+json.rows[i].OutletName;
	    			if( json.rows[i].OutletID == '0' ){
	    				OutletInfo = json.rows[i].OutletName;
	    			}
	    			
	    			var content = "<tr id='SamplingCreditSlip"+RowMaxID+"'>";
	    			content += "<td><input type='hidden' name='OutletIDList' id='OutletIDList' value='"+json.rows[i].OutletID+"' ><input type='hidden' name='OutletNameList' id='OutletNameList' value='"+json.rows[i].OutletName+"' >"+OutletInfo+"</td>";
	    			content += "<td><input type='hidden' name='AmountList' id='AmountList' value='"+json.rows[i].Amount+"' >"+json.rows[i].Amount+"</td>";
	    			content += "<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeleteRow('SamplingCreditSlip"+RowMaxID+"');\">Delete</a></td>";	
	    			content += "</tr>";
	    			
	    			$("#TableBody").append(content).trigger('create');
	    			
	    			
	    			RowCount++;
	    			$('#NoProductRow').css('display', 'none');
	    			$('#RowMaxID').val(RowMaxID);
	    			
	    		}
	    		
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    	
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}

function DeleteRow(RowID){
	
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#CreditSlipSave').addClass('ui-disabled');
	}
	
}

function getDistributorName(){
			
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
				$('#DistributorName').val('');
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
		
}

</script>

<body>

<div data-role="page" id="CreditSlipDistributorPage" data-url="CreditSlipDistributorPage" data-theme="d">

    <div data-role="header" data-theme="c" data-position="fixed">
	    <div>
		    <div style="float:left; width:10%">
		    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
		    </div>
		    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;">
		    	<h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Credit Slip Distributor</h1>
			</div>
		</div>
	</div>
	
	<div data-role="content" data-theme="d">

			
				
					
						
						
						<table style="width:100%">
							<tr>
								<td style="width:50%; border-right: 1px solid #ccc; padding-right: 10px" valign="top">
									<form id="CreditSlipForm" action="/" method="post" >
									
										
						
									
									<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
										<tr>
											<td style="border: 0px; width: 33%">
												<label for="Month" data-mini="true">Month</label>
												<select name="Month" id="Month" data-mini="true">
													<option value="1">January</option>
													<option value="2">February</option>
													<option value="3">March</option>
													<option value="4">April</option>
													<option value="5">May</option>
													<option value="6">June</option>
													<option value="7">July</option>
													<option value="8">August</option>
													<option value="9">September</option>
													<option value="10">October</option>
													<option value="11">November</option>
													<option value="12">December</option>
												</select>
												
												<script>
													$('#Month').val(<%=Month%>).change();
												</script>
												
											</td>
											<td style="border: 0px; width: 33%">
												<label for="Year" data-mini="true">Year</label>
												<select name="Year" id="Year" data-mini="true">
												<%
													
												    Calendar cal = Calendar.getInstance();
												    int CurYear = cal.get(Calendar.YEAR);
												
													for(int i = 2000; i <= 2050; i++ ){
														%>
														<option value="<%=i%>" <% if(i == CurYear){ out.print("selected"); } %> ><%=i%></option>
														<%
													}
												%>
													<script>
														$('#Year').val(<%=Year%>).change();
													</script>
												</select>
											</td>
										
											<td style="border: 0px; width: 34%">
													<label for="Type" data-mini="true">Type</label>
													<select name="Type" id="Type" data-mini="true">
													<%
														ResultSet rs = s.executeQuery("SELECT * FROM sampling_credit_slip_types order by id");
														while(rs.next()){
													%>
														<option value="<%=rs.getString("id")%>"><%=rs.getString("label")%></option>
													<%
													}
													%>
														<script>
															$('#Type').val(<%=Type%>).change();
														</script>
													</select>
												</td>
											
										</tr>
									</table>
									<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
										
										<tr>
											<td style="width: 20%; border: 0px; padding-right: 2px">
											<label for="DistributorID" data-mini="true">Distributor ID</label>
												<input type="text" name="DistributorID" id="DistributorID" value="" data-mini="true" ondblclick="openDistributorSelectionPopup()" onChange="getDistributorName()" >
											</td>
											<td style="width: 80%; border: 0px; padding-left: 2px; ">
												<label for="DistributorName" data-mini="true">Distributor Name</label>
												<input type="text" name="DistributorName" id="DistributorName" value="" data-mini="true" readonly="readonly" >
											</td>
										</tr>
									</table>
									<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
										<tr>
											<td style="border: 0px" colspan="2" style="width: 50%">
												<label for="SlipDescription" data-mini="true">Slip Description (Pritable)</label>
												<textarea name="SlipDescription" id="SlipDescription" style="width: 100%; height: 100px" data-mini="true"><%=SlipDescription%></textarea>
											</td>
										</tr>
										<tr>
											<td style="border: 0px" colspan="2" style="width: 50%">
												<label for="Description" data-mini="true">Description (Office Use)</label>
												<textarea name="Description" id="Description" style="width: 100%; height: 100px" data-mini="true"><%=Description%></textarea>
											</td>
										</tr>
										
									</table>
									</form>
									
								</td>
								<td style="width:50%; padding-left: 10px" valign="top">
									
											<form name="OutletForm" id="OutletForm" action="#" method="post" onSubmit="return AddRow()" data-ajax="false" >
											
												<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>"/>
												<input type="hidden" name="UniqueVoucherID" id="UniqueVoucherID" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>"/>
											
												<input type="hidden" name="RowMaxID" id="RowMaxID" value="0" >
												<input type="hidden" name="isValidOutlet" id="isValidOutlet" value="0" >
												
												<input type="hidden" name="SamplingCreditSlipMonth" id="SamplingCreditSlipMonth" value="" >
												<input type="hidden" name="SamplingCreditSlipYear" id="SamplingCreditSlipYear" value="" >
												<input type="hidden" name="SamplingCreditSlipType" id="SamplingCreditSlipType" value="" >
												<input type="hidden" name="SamplingCreditSlipDistributorID" id="SamplingCreditSlipDistributorID" value="" >
												<input type="hidden" name="SamplingCreditSlipDescription" id="SamplingCreditSlipDescription" value="" >
												<input type="hidden" name="SamplingCreditSlipDescriptionOfficial" id="SamplingCreditSlipDescriptionOfficial" value="" >
												
												<table style="width: 100%">
													<tr>
														<td style="width: 20%">
															<input type="text" name="OutletID" id="OutletID" placeholder="Outlet ID" value="" data-mini="true" onChange="getOutletName(); "  maxlength="10" ondblclick="openOutletSelectionPopup()" >
														</td>
														<td style="width: 50%">
															<input type="text" name="OutletName" id="OutletName" placeholder="Outlet Name" value="" data-mini="true"  >
														</td>
														<td style="width: 20%">
															<input type="text" data-mini="true" name="Amount" id="Amount" placeholder="Amount" >
														</td>
														<td style="width: 10%">
															<input type="submit" data-mini="true" value="Add" data-icon="plus" >
														</td>
														<!-- <td style="width: 50%; padding-left: 20px">
															<span id="OutletName" style="font-size: 12px; font-weight: bold"></span><br><span id="OutletAddress" style="font-size: 12px"></span>
														</td> -->
													</tr>
												</table>
										
												<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%; margin-top: 10px">
												  <thead>
												    
												    <tr class="ui-bar-c">
														<th data-priority="1" style="width: 45%">Outlet</th>
														<th data-priority="1" style="width: 45%">Amount</th>
														<th data-priority="1" style="width: 10%">&nbsp;</th>
												    </tr>
												  </thead>
												  
													<tbody id="TableBody">
													
													<tr id="NoProductRow">
														<td colspan="10" style="margin: 1px; padding: 0px;">
															<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No Outlet added.</div>
														</td>
													</tr>
													
													
													
													
													
													</tbody>
												</table>
											</form>
										
										
								</td>
							</tr>
						</table>
					
				
			
			
			</div>
			
			
		<div data-role="footer" data-position="fixed" data-theme="b">
			<div>
				<table style="width: 100%">
					<tr>
						<td>
							<button data-icon="check" data-theme="a" data-inline="true"  onClick="CreditSlipSubmit()" id="CreditSlipSave">Save</button>
							<button data-icon="check" data-theme="b" data-inline="true" id="CreditSlipReset" onClick="javascript:window.location='SamplingCreditSlipDistributor.jsp'" >Reset</button>
						</td>
						<td align="right"><a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="CreditSlipSearch" >Search</a></td>
					</tr>
				</table>
				
			</div>
			
		</div>
		
		<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="FormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>
                    	<span id="FromDateSpan"><input type="text" data-mini="true" name="FromDate" id="FromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    </td>
                    <td>
						<span id="ToDateSpan"><input type="text" data-mini="true" name="ToDate" id="ToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="DateButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="SearchContent">
        </div>
            
        </div>
    </div>
		
		 <jsp:include page="LookupOutletSearchPopup.jsp" > 
	    	<jsp:param value="OutletSearchCallBackCreditSlip" name="CallBack" />
	    	<jsp:param value="<%=FeatureID%>" name="OutletSearchFeatureID" />
	    </jsp:include><!-- Include Outlet Search -->
	    
	    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
	    	<jsp:param value="DistributorSearchCallBackCreditSlip" name="CallBack" />
	    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
	    </jsp:include><!-- Include Outlet Search -->
</div>
			
</div>
</body>

<%
s.close();
c.close();
ds.dropConnection();
%>