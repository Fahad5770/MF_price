var INVENTORY_PACKAGE_JSON = null;
var INVENTORY_BRAND_JSON = null;

$( document ).delegate("#WorkflowManagerApproval", "pageinit", function() {
	initWorkflow("#WorkflowManagerApproval");
});

$( document ).delegate("#WorkflowManager", "pageinit", function() {
	initWorkflow("#WorkflowManager");
});


$( document ).delegate("#SamplingAdjustment", "pageinit", function() {
	
	$("#SamplingAdjustmentSaveButton").on ("click", function( event, ui ) {
		$('#SamplingAdjustmentForm').submit();
	});

	
	var pageid = "#SamplingAdjustment";
	
	$(pageid + ' #OutletID').change(function() {
		$.ajax({
		    url: "sampling/GetOutletInfoJson",
		    data: {
		        OutletID: this.value
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		$(pageid + ' #OutletName').val(json.OutletName);
		    		$(pageid + ' #BusinessType').val(json.BusinessType);
		    		$(pageid + ' #address').val(json.address);
		    		$(pageid + ' #region').val(json.region);
		    		$(pageid + ' #asm').val(json.asm);
		    		$(pageid + ' #cr').val(json.cr);
		    		$(pageid + ' #market').val(json.market);
		    		$(pageid + ' #vehicle').val(json.vehicle);
		    		$(pageid + ' #CurrentBalance').val(json.CurrentBalance);
		    		
		    		
		    		
		    		
		    		//$(pageid + ' #AdvanceCompanyShare').focus();	
		    		
		    		var lat = json.latitude;
		    		var lng = json.longitude;
		    		
		    		if (parseFloat(lat) > 1){
		    			$(pageid + ' #OutletMap').attr("src","http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C"+lat+","+lng+"&sensor=false");
		    		}else{
		    			$(pageid + ' #OutletMap').attr("src","images/GPSUnavailable.png");
		    		}
		    		
		    		
		    		
		    		
		    		
		    	}else{
		    		$(pageid + ' #BusinessType').val("");
		    		$(pageid + ' #address').val("");
		    		$(pageid + ' #region').val("");
		    		$(pageid + ' #asm').val("");
		    		$(pageid + ' #cr').val("");
		    		$(pageid + ' #market').val("");
		    		$(pageid + ' #vehicle').val("");
		    		
		    		$(pageid + ' #OutletName').val("Outlet doesn't exist.");
		    		$(pageid + ' #OutletID').focus();
		    		$(pageid + ' #OutletID').select();
		    		
		    		//$(pageid +" #WorkflowOutletDashboardLink").attr("href", "OutletDashboardMain.jsp");
		    		
		    	}
		    },
		    error: function( xhr, status ) {
		        alert( "Sorry, the server did not respond." );
		    }
		});
		
	});	

});

$( document ).delegate("#HomePage", "pageinit", function() {
	
	var DefaultTypeID = parseFloat(document.getElementById("WorkflowDefaultTypeID").innerHTML);
	
	showWorkflowStatus(DefaultTypeID);
	
	$("#HistorySlider").on( "slidestop", function( event, ui ) {
		showWorkflowStatus(1);
	} );
	
	
	$("#HomePage #ChangePasswordButton").on( "click", function( event, ui ) {
		$("#HomePage #popup_change_password" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});	
	
	/*
	var wlocation = "Faisalabad, PK";
	
    $.getJSON('http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20location%20in%20(%0A%20%20select%20id%20from%20weather.search%20where%20query%3D%22'+wlocation+'%22%0A)%20limit%201&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=?',function(data){
        //$('#weathertext').show();
        //$('#placetitle').html($('#placename').val());
        $('#weatherimage').attr('src','images/'+data.query.results.channel.item.condition.code+'.png');
        $('#temperature').html(data.query.results.channel.item.condition.temp+' &deg;F');
        $('#condition').html(data.query.results.channel.item.condition.text);
        var winddirection=parseInt(data.query.results.channel.wind.direction);
        var direction='';
        switch(true)
        {
            case (winddirection==0):
                direction='N';
                break;
            case (winddirection<90):
                direction='NE';
                break;
            case (winddirection==90):
                direction='E';
                break;
            case (winddirection<180):
                direction='SE';
                break;
            case (winddirection==180):
                direction='S';
                break;
            case (winddirection<270):
                direction='SW';
                break;
            case (winddirection==270):
                direction='W';
                break;
            case (winddirection<360):
                direction='NW';
                break;
            case (winddirection==360):
                direction='N';
                break;
        }
        //$('#dirspeed').html('Wind: '+direction+' at '+data.query.results.channel.wind.speed+' mph');
        $('#humidity').html('Humidity: '+data.query.results.channel.atmosphere.humidity+'%');
    });
	*/
});

function ChangePasswordSubmit(){
	
	var currentpass = $("#CurrentPassword").val();
	var newpass = $("#NewPassword").val();
	var confirmpass = $("#ConfirmPassword").val();
	
	if (currentpass.length < 2){
		alert("Please enter current password.");
		document.getElementById("CurrentPassword").focus();
		return false;
	}
	
	if (newpass.length < 8 || checkPasswordCharacters(newpass) == false ){
		alert("The password must be at least 8 characters long and contain:\n1) Atlest one number.\n2) Atleast one alphabet.");
		
		document.getElementById("NewPassword").focus();
		return false;
	}
	
	if (newpass != confirmpass){
		alert("Password doesn't match.");
		document.getElementById("ConfirmPassword").focus();
		return false;
	}
	
	$.ajax({
	    url: "util/ChangePasswordExecute",
	    data: $("#ChangePasswordForm").serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		alert("Password changed.");
	    		$("#ChangePasswordForm").trigger('reset');
	    		$.mobile.changePage("#HomePage");
	    	}else{
	    		if (json.invalid == "true"){
	    			alert("Invalid Current Password");
	    			document.getElementById("CurrentPassword").focus();
	    		}else if (json.invalid_new == "true"){
	    			alert("Invalid Password");
	    			document.getElementById("NewPassword").focus();
	    		}else{
	    			alert("Server could not be reached.");
	    		}
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});	
	return false;
}

function checkPasswordCharacters(password){
	
	var hasInteger = false;
	var hasAlphabets = false;
	
	var letters=/^[a-zA-Z]+$/;
	//var numbers=/^[0-9]+$/;
	
	for(var i = 0; i < password.length; i++){
		
		if( parseInt(password.charAt(i)) > 0 ){
			hasInteger = true;
		}
		
		if( password.charAt(i).search(letters) == 0 ){
			hasAlphabets = true;
		}
		
		if(hasInteger && hasAlphabets){
			return true;
		}
		
	}// end for
	
	if(!hasInteger || !hasAlphabets){
		return false;
	}
	
}

$( document ).delegate("#CreateWorkflow", "pageinit", function() {
	
});

$( document ).delegate("#SamplingDashboard", "pageinit", function() {
	
	
	$("#SamplingDashboardApproveButton").on( "click", function( event, ui ) {
		
		$("#SamplingDashboardStatus").val("1");
		SamplingDashboardSubmit();
		
	});
	$("#SamplingDashboardOnHoldButton").on( "click", function( event, ui ) {
		
		$("#SamplingDashboardStatus").val("2");
		SamplingDashboardSubmit();
		
	});
	$("#SamplingDashboardCancelledButton").on( "click", function( event, ui ) {
		
		$("#SamplingDashboardStatus").val("3");
		SamplingDashboardSubmit();
		
	});
	
	
	
	
});

$( document ).delegate("#SamplingReceiving", "pageinit", function() {

	setTimeout(function(){
		$("#SamplingReceivingDistributorID").focus();	
	}, 2000);
	
	
});

function SamplingReceivingSetDistributor(DistributorID){
	if (DistributorID != ""){
		$.ajax({
		    url: "common/GetDistributorInfoJson",
		    data: {
		    	DistributorID: DistributorID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		$("#SamplingReceivingDistributorName").val(json.DistributorName);
		    		$("#SamplingReceiving #DistributorID").val(DistributorID);
		    	}else{
		    		$("#SamplingReceivingDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}



function SamplingReceivingSubmit(){
	var DistributorID = $("#SamplingReceiving #DistributorID").val();
	
	if (DistributorID != ""){
		$.ajax({
		    url: "sampling/SamplingReceivingExecute",
		    
		    data: $("#SamplingReceivingMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		location = "SamplingReceiving.jsp?ReceivingID="+json.ReceivingID;
		    	}else{
		    		alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
}

function SlipDispatchSubmit(){
	
	$.ajax({
	    url: "sampling/SamplingDispatchExecute",
	    
	    data: $("#SlipDispatchMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "SlipDispatch.jsp";
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function SamplingReceivingTotal(){
	var x = 0;
	var total = 0;
	var amounts = document.getElementsByName("SamplingReceivingBarcodeAmount");
	for (x = 0; x < amounts.length; x++){
		var thisamt = parseFloat(amounts[x].value);
		if (isNaN(thisamt)){
			thisamt = 0;
		}
		total += thisamt;
	}
	
	$("#SamplingReceivingTotal").html("Total: "+ getFormattedNumber(total));
}
function SamplingReceivingDeleteRow(RowID){
	$("#"+RowID).remove();
	SamplingReceivingTotal();
}



function SamplingReceivingAddBarcode(){
	
	$("#SamplingReceivingMainForm").css("visibility","visible");
	
	var val = $("#SamplingReceivingBarcodeField").val();
	
	if (val != "" && val.length > 0){
		
		$.ajax({
		    url: "sampling/GetBarcodeInformationJson",
		    data: {
		    	SamplingReceivingBarcode: val
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		
		    		var isAlreadyEntered = false;
		    		
		    		var existing = document.getElementsByName("ApprovalID");
		    		var x = 0;
		    		for (x = 0; x < existing.length; x++){
		    			if (existing[x].value == json.ApprovalID){
		    				isAlreadyEntered = true;
		    				break;
		    			}
		    		}
		    		
		    		if (isAlreadyEntered != true){
		    			
				    	var content = ""+
						"<tr id='Barcode"+json.ApprovalID+"'>"+
							"<td>"+json.CustomerID + " - " + json.CustomerName +"</td>"+						
							"<td>"+json.OutletID + " - " + json.OutletName +"<input type='hidden' name='ApprovalID' value='"+json.ApprovalID+"'></td>"+
							"<td>"+json.month+"</td>"+
							"<td style='text-align: right;'>"+json.FormattedAmount+"<input type='hidden' name='SamplingReceivingBarcodeAmount' value='"+json.Amount+"'></td>"+
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"SamplingReceivingDeleteRow('Barcode"+json.ApprovalID+"')\">Delete</a></td>"+
						"</tr>";
				    	
						
						$("#SamplingReceivingTableBody").append(content).trigger('create');
						
						SamplingReceivingTotal();
					
		    		}
					
		    	}else{
		    		alert("Invalid Barcode");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
	
	
	$("#SamplingReceivingBarcodeField").val("");
	
	return false;
}

function SlipCancellationAddBarcode(){	
	
	$("#SlipCancellationMainForm").css("visibility","visible");
	
	var val = $("#SamplingReceivingBarcodeField").val();
	
	if (val != "" && val.length > 0){
		
		$.ajax({
		    url: "sampling/GetBarcodeInformationJson",
		    data: {
		    	SamplingReceivingBarcode: val,
		    	SlipCancellationIsActive: $("#SlipCancellationIsActive").val()
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		
		    		var isAlreadyEntered = false;
		    		
		    		var existing = document.getElementsByName("ApprovalID");
		    		var x = 0;
		    		for (x = 0; x < existing.length; x++){
		    			if (existing[x].value == json.ApprovalID){
		    				isAlreadyEntered = true;
		    				break;
		    			}
		    		}
		    		
		    		if (isAlreadyEntered != true){
		    			
				    	var content = ""+
						"<tr id='Barcode"+json.ApprovalID+"'>"+
							"<td>"+json.CustomerID + " - " + json.CustomerName +"</td>"+						
							"<td>"+json.OutletID + " - " + json.OutletName +"<input type='hidden' name='ApprovalID' value='"+json.ApprovalID+"'></td>"+
							"<td>"+json.month+"</td>"+
							"<td style='text-align: right;'>"+json.FormattedAmount+"<input type='hidden' name='SamplingReceivingBarcodeAmount' value='"+json.Amount+"'></td>"+
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"SamplingReceivingDeleteRow('Barcode"+json.ApprovalID+"')\">Delete</a></td>"+
							"<td><input type='hidden' name='ApprovalIDNew' ID='ApprovalIDNew' value='"+json.ApprovalID+"'</td>";
						"</tr>";
				    	//content += "<tr><td colspan='6'><input type='text' placeholder='Remarks for Cancellation' id='SamplingReceivingBarcodeField'></td></tr>";
						
						$("#SamplingReceivingTableBody").append(content).trigger('create');
						
						SamplingReceivingTotal();
					
		    		}
					
		    	}else{
		    		alert("Invalid Barcode");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
	
	
	$("#SamplingReceivingBarcodeField").val("");
	
	return false;
}



function SlipCancellationSubmit(){	
	
	
		$.ajax({
		    url: "sampling/SlipCancellationExecute",
		    
		    data: $("#SlipCancellationMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		location = "SlipCancellation.jsp";
		    	}else{
		    		alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	
}



function SlipDispatchingAddBarcode(){
	
	var barcodeValue = '';
	var dispatchMethodLabel = '';
	var dispatchMethodValue = '';
	
	if ($("#DispatchMethod").val() == '2'){
		
		$('#DispatchMethodMainForm').val('2');		
		dispatchMethodLabel = 'TCS Code';
		
		if( $("#SlipDispatchBarcodeFieldTCS").val() == ''){
			$("#SlipDispatchBarcodeFieldTCS").focus();
			return false;
		}
		
		barcodeValue = $("#SlipDispatchBarcodeFieldTCS").val();
		
		if( $("#TCSBarcodeField").val() == ''){
			$("#TCSBarcodeField").focus();
			return false;
		}
		
		dispatchMethodValue = $("#TCSBarcodeField").val();
		
		
	}else if ($("#DispatchMethod").val() == '1'){
		
		$('#DispatchMethodMainForm').val('1');
		dispatchMethodLabel = 'Employee SAP Code';
		
		if( $("#SlipDispatchBarcodeFieldByHand").val() == ''){
			$("#SlipDispatchBarcodeFieldByHand").focus();
			return false;
		}
		
		barcodeValue = $("#SlipDispatchBarcodeFieldByHand").val();
		
		if( $("#SlipDispatchUserID").val() == ''){
			//$("#SlipDispatchUserID").focus();
			return false;
		}
		
		dispatchMethodValue = $("#SlipDispatchUserID").val();
		
	}
	
	$('#DispatchMethod').addClass("ui-disabled");
	$('#DispatchMethodLabel').html(dispatchMethodLabel);
	$("#SlipDispatchMainForm").css("visibility","visible");
	
	var val = barcodeValue;
	
	if (val != "" && val.length > 0){
		$.ajax({
		    url: "sampling/GetBarcodeInformationJson",
		    data: {
		    	SamplingReceivingBarcode: val
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		
		    		if(json.IsDispatched == "1"){
		    			alert("Barcode already dispatched.");
		    			return false;
		    		}
		    		
		    		var isAlreadyEntered = false;
		    		
		    		var existing = document.getElementsByName("ApprovalID");
		    		var x = 0;
		    		for (x = 0; x < existing.length; x++){
		    			if (existing[x].value == json.ApprovalID){
		    				isAlreadyEntered = true;
		    				break;
		    			}
		    		}
		    		
		    		if (isAlreadyEntered != true){
		    			
				    	var content = ""+
						"<tr id='Barcode"+json.ApprovalID+"'>"+
							"<td>"+json.CustomerID + " - " + json.CustomerName +"</td>"+						
							"<td>"+json.OutletID + " - " + json.OutletName +"<input type='hidden' name='ApprovalID' value='"+json.ApprovalID+"'></td>"+
							"<td>"+json.month+"</td>"+
							"<td style='text-align: right;'>"+json.FormattedAmount+"<input type='hidden' name='SamplingReceivingBarcodeAmount' value='"+json.Amount+"'></td>"+
							"<td style='text-align: right;'>"+dispatchMethodValue+"<input type='hidden' name='DispatchMethodValue' value='"+dispatchMethodValue+"'></td>"+
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"SamplingReceivingDeleteRow('Barcode"+json.ApprovalID+"')\">Delete</a></td>"+
						"</tr>";
						
						$("#SamplingReceivingTableBody").append(content).trigger('create');
						
						SamplingReceivingTotal();
						
						ResetDispatchForm();
					
		    		}
					
		    	}else{
		    		alert("Invalid Barcode");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
	
	
	//$("#SamplingReceivingBarcodeField").val("");
	
	return false;
}

function ResetDispatchForm(){
	$('#SlipDispatchBarcodeFieldTCS').val('');
	$('#TCSBarcodeField').val('');
	$('#SlipDispatchBarcodeFieldByHand').val('');
	//$('#SlipDispatchUserID').val('');
	
	if ($("#DispatchMethod").val() == '2'){
		$('#SlipDispatchBarcodeFieldTCS').focus();
	}else if ($("#DispatchMethod").val() == 1){
		$('#SlipDispatchBarcodeFieldByHand').focus();
	}
	
	
}


$( document ).delegate("#SamplingDashboardMain", "pageinit", function() {
	
	$("#SamplingDashboardGenerateButton").on ("click", function( event, ui ) {
		var params = $('#SamplingDashboardGenerrateForm').serialize();
		
		//window.open("SamplingDashboardXLS.jsp?"+params,"_blank");
		
		$('#SamplingDashboardGenerrateForm').attr("action", "SamplingDashboard.jsp?params="+encodeURIComponent(params));
		
		$('#SamplingDashboardGenerrateForm').submit();
	});
	
	$("#SamplingDashboardGenerateButtonXLS").on ("click", function( event, ui ) {
		var params = $('#SamplingDashboardGenerrateForm').serialize();
		window.open("SamplingDashboardXLS.jsp?"+params,"_blank");
	});
	
	$("#SamplingDashboardPerCaseButtonXLS").on ("click", function( event, ui ) {
		var params = $('#SamplingDashboardGenerrateForm').serialize();
		window.open("SamplingDashboardProjected.jsp?"+params,"_blank");
	});
	
});



$( document ).delegate("#SamplingPrintingMain", "pageinit", function() {
	
	$("#SamplingDashboardGenerateButton").on ("click", function( event, ui ) {
		var params = $('#SamplingDashboardGenerrateForm').serialize();
		
		$('#SamplingDashboardGenerrateForm').attr("action", "SamplingPrinting.jsp?params="+encodeURIComponent(params));
		
		$('#SamplingDashboardGenerrateForm').submit();
	});
	
});

function SamplingDashboardSubmit(){
	
	$("#SamplingDashboardApprovalForm").submit();
	
	/*
	$.ajax({
	    url: "sampling/SamplingDashboardExecute",
	    data: $("#SamplingDashboardApprovalForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "SamplingDashboard.jsp?"+$("#ReturnParams").val()+"&params="+encodeURIComponent($("#ReturnParams").val());
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});*/	
}

function SamplingDashboardCalc(num){
	var adjustment = parseFloat($("#FormAdjustment"+num).val());
	var payable = parseFloat($("#FormPayable"+num).val());
	
	if (isNaN(adjustment)){
		adjustment = 0;
	}
	if (isNaN(payable)){
		payable = 0;
	}
	
	var net = payable - adjustment; 
		
	$("#FormNetPayable"+num).val(net);
	
}

function SamplingDashboardPopup(OutletID, month, year){
	
	$.mobile.showPageLoadingMsg();	
	$.get('SamplingDashboardDetail.jsp?OutletID='+OutletID+'&month='+month+'&year='+year, function(data) {
		  $("#SamplingDetailContent").html(data);
		  $("#SamplingDetailContent").trigger('create');
		  $("#SamplingDashboard #SamplingDetailPopup").css('overflow-y', 'scroll');
		  $.mobile.hidePageLoadingMsg();
		  $("#SamplingDashboard #SamplingDetailPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});
	
}

	
function getFormattedNumber(nStr) {
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}	



function CreateWorkflowAllowDrop(ev) {
	ev.preventDefault();
}

function CreateWorkflowDrag(ev) {
	ev.dataTransfer.setData("Text",ev.target.id);
}

function CreateWorkflowDrop(ev) {
	ev.preventDefault();
	var data=ev.dataTransfer.getData("Text");
	
	var content = '<fieldset class="ui-grid-d">'+
	    '<div class="ui-block-a" style="padding: 5px;"><button data-corners="false" data-theme="b" >'+data+'</button></div>'+
	    '<div class="ui-block-b" style="padding: 5px;"><input type="text" placeholder="User ID" data-theme="d" ></div>'+
	    '<div class="ui-block-c" style="padding: 5px;"><input type="text" placeholder="Position Code" readonly="readonly" data-theme="d"></div>'+
	    '<div class="ui-block-d" style="padding: 5px;">'+
			'<div class="CreateWorkflowAllowEdititing">'+
				'<select name="flip-min" id="flip-min" data-role="slider">'+
					'<option value="off">Allowed</option>'+
					'<option value="on" selected>Not Allowed</option>'+
				'</select>'+
			'</div>'+
	    '</div>'+
	    '<div class="ui-block-e" style="padding: 5px;"><input type="text" placeholder="Due Days" data-theme="d"></div>'+
	'</fieldset>';

	
	$("#CreateWorkflowDropZone").append( content );
	
	$("#CreateWorkflowDropZone").trigger('create');
	
	//alert(data);
	//ev.target.appendChild(document.getElementById(data));
}



function showWorkflowStatus(TypeID){
	
	var days = parseFloat($("#HistorySlider").val());
	
	if( !isNaN(days) ){
		$.get('HomeWorkflowStatus.jsp?days='+days+'&TypeID='+TypeID, function(data) {
			  $("#WorkflowStatusContent").html(data);
			  $("#WorkflowStatusContent").trigger('create');
		});
	}
	
	
}


function initWorkflow(pageid){
	
	$.ajax({
	    url: "common/GetLiquidPackageListJson",
	    data: {
	        NoParam: 1
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	INVENTORY_PACKAGE_JSON = json;
	    	
	    	$.ajax({
	    	    url: "common/GetBrandListJson",
	    	    data: {
	    	        NoParam: 1
	    	    },
	    	    type: "POST",
	    	    dataType : "json",
	    	    success: function( json2 ) {
	    	    	INVENTORY_BRAND_JSON = json2;
	    	    	
	    	    	setSamplingFormEvents(pageid);
	    	    	setWorkflowEvents(pageid);
	    	    	showAttachments(pageid);
	    	    	showChatContent(pageid);
	    	    	
	    	    },
	    	    error: function( xhr, status ) {
	    	        alert( "Sorry, the server did not respond." );
	    	    }
	    	});
	    	
	    	
	    	
	    },
	    error: function( xhr, status ) {
	        alert( "Sorry, the server did not respond." );
	    }
	});
	
}

function SalesThresholdCalc(){
	
	
	var per2 = parseFloat($("#SalesThresholdPercentage2").val());
	var per3 = parseFloat($("#SalesThresholdPercentage3").val());
	var per4 = parseFloat($("#SalesThresholdPercentage4").val());
	var per5 = parseFloat($("#SalesThresholdPercentage5").val());
	
	var sales = parseFloat($("#SalesThresholdSales1").val());
	if (!isNaN(sales)){
		$("#SalesThresholdSales2").val(Math.round(sales*per2/100));
		$("#SalesThresholdSales3").val(Math.round(sales*per3/100));
		$("#SalesThresholdSales4").val(Math.round(sales*per4/100));
		$("#SalesThresholdSales5").val(Math.round(sales*per5/100));
	}
	
	var discount = parseFloat($("#SalesThresholdDiscount1").val());
	if (!isNaN(discount)){
		
		$("#SalesThresholdDiscount2").val(Math.round(discount*per2/100));
		$("#SalesThresholdDiscount3").val(Math.round(discount*per3/100));
		$("#SalesThresholdDiscount4").val(Math.round(discount*per4/100));
		$("#SalesThresholdDiscount5").val(Math.round(discount*per5/100));
	}
	
}

function SalesThresholdDiscountValidation(){
	var discount1 = parseFloat($("#SalesThresholdDiscount1").val());
	var discount2 = parseFloat($("#SalesThresholdDiscount2").val());
	var discount3 = parseFloat($("#SalesThresholdDiscount3").val());
	var discount4 = parseFloat($("#SalesThresholdDiscount4").val());
	var discount5 = parseFloat($("#SalesThresholdDiscount5").val());
	
	if (discount2 > discount1){
		$("#SalesThresholdDiscount2").val("0");
	}
	discount2 = parseFloat($("#SalesThresholdDiscount2").val());

	if (discount3 > discount2){
		$("#SalesThresholdDiscount3").val("0");
	}
	discount3 = parseFloat($("#SalesThresholdDiscount3").val());
	
	if (discount4 > discount3){
		$("#SalesThresholdDiscount4").val("0");
	}
	discount4 = parseFloat($("#SalesThresholdDiscount4").val());
	
	if (discount5 > discount4){
		$("#SalesThresholdDiscount5").val("0");
	}
	
}

function setSamplingFormEvents(pageid){
	
	
	$(pageid + " #SalesThresholdSales1").on ("change", function( event, ui ) {
		SalesThresholdCalc();
	});
	
	$(pageid + " #SalesThresholdDiscount1").on ("change", function( event, ui ) {
		SalesThresholdCalc();
	});
	
	$(pageid + " #FixedCompanyShare").on ("change", function( event, ui ) {
		$("#SalesThresholdDiscount1").val($(pageid + " #FixedCompanyShare").val());
		SalesThresholdCalc();
	});
	
	$(pageid + " #SalesThresholdDiscount2").on ("change", function( event, ui ) {
		SalesThresholdDiscountValidation();
	});
	$(pageid + " #SalesThresholdDiscount3").on ("change", function( event, ui ) {
		SalesThresholdDiscountValidation();
	});
	$(pageid + " #SalesThresholdDiscount4").on ("change", function( event, ui ) {
		SalesThresholdDiscountValidation();
	});
	$(pageid + " #SalesThresholdDiscount5").on ("change", function( event, ui ) {
		SalesThresholdDiscountValidation();
	});
	
	$.validator.addMethod(
		    "pkDate",
		    function(value, element) {
		        
		    	if (value.length > 0){
		    	
				if (!value.match(/^\d\d?\/\d\d?\/\d\d\d\d$/)){
					return false;
				}
		    	try{
		    		var dd = $.datepicker.parseDate('dd/mm/yy', value);
		    		return true;
		    	}catch(e){
		    		return false;
		    	}
		    	
		    	}else{
		    		return true;
		    	}
		        
		    },
		    "Please enter a date in the format dd/mm/yyyy."
	);
	
	$(pageid+' .WorkflowForm').validate({
		rules: {
			OutletID: {
				required: true,
				number:true
			},
			AdvanceCompanyShare: {
				number: true,
				maxlength: 12
			},
			AdvanceAgencyShare: {
				number: true,
				maxlength: 12
			},
			AdvanceValidFrom: {
				pkDate: true
			},
			AdvanceValidTo: {
				pkDate: true
			},
			FixedCompanyShare: {
				number: true,
				maxlength: 12
			},
			FixedCompanyShareOP: {
				number: true,
				maxlength: 12
			},			
			FixedAgencyShare: {
				number: true,
				maxlength: 12
			},
			FixedAgencyShareOP: {
				number: true,
				maxlength: 12
			},
			FixedDeductionTerm: {
				number: true,
				maxlength: 12
			},
			FixedDeductionTermOP: {
				number: true,
				maxlength: 12
			},
			FixedValidFrom: {
				pkDate: true
			},
			FixedValidTo: {
				pkDate: true
			}
								
		},
		submitHandler: function(form) {
			$( pageid +" #popup_workflow_forward" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
		}
		
	});			
	$(pageid + " #AddPerCasePackage").on ("click", function( event, ui ) {
		event.preventDefault();
		addPerCaseRow(pageid);
		
	});	
	$(pageid + ' #OutletID').change(function() {
		$.ajax({
		    url: "sampling/GetOutletInfoJson",
		    data: {
		        OutletID: this.value
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		$(pageid + ' #OutletName').val(json.OutletName);
		    		$(pageid + ' #BusinessType').val(json.BusinessType);
		    		$(pageid + ' #address').val(json.address);
		    		$(pageid + ' #region').val(json.region);
		    		$(pageid + ' #asm').val(json.asm);
		    		$(pageid + ' #cr').val(json.cr);
		    		$(pageid + ' #market').val(json.market);
		    		$(pageid + ' #vehicle').val(json.vehicle);
		    		$(pageid + ' #AdvanceCompanyShare').focus();	
		    		
		    		$(pageid +" #WorkflowOutletDashboardLink").attr("href", "OutletDashboard.jsp?outletID="+$(pageid + ' #OutletID').val());
		    		
		    		var lat = json.latitude;
		    		var lng = json.longitude;
		    		
		    		if (parseFloat(lat) > 1){
		    			$(pageid + ' #OutletMap').attr("src","http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C"+lat+","+lng+"&sensor=false");
		    		}else{
		    			$(pageid + ' #OutletMap').attr("src","images/GPSUnavailable.png");
		    		}
		    		
		    	}else{
		    		$(pageid + ' #BusinessType').val("");
		    		$(pageid + ' #address').val("");
		    		$(pageid + ' #region').val("");
		    		$(pageid + ' #asm').val("");
		    		$(pageid + ' #cr').val("");
		    		$(pageid + ' #market').val("");
		    		$(pageid + ' #vehicle').val("");
		    		
		    		$(pageid + ' #OutletName').val("Outlet doesn't exist.");
		    		$(pageid + ' #OutletID').focus();
		    		$(pageid + ' #OutletID').select();
		    		
		    		$(pageid +" #WorkflowOutletDashboardLink").attr("href", "OutletDashboardMain.jsp");
		    	}
		    },
		    error: function( xhr, status ) {
		        alert( "Sorry, the server did not respond." );
		    }
		});
		
	});	
	$("label").css("font-size","10pt");
	$("label").css("font-weight","400");	
	addPerCaseRow(pageid);
	addPerCaseRow(pageid);	
	
	var RequestID = parseFloat($(pageid + " #RequestID").val());
	if (RequestID != 0){
		getSampling(pageid, RequestID);
	}
	
}

function showAttachments(pageid){
	
	if (parseFloat($(pageid + " #RequestID").val()) > 0){
		$.get('WorkflowManagerAttachments.jsp?RequestID='+$(pageid + " #RequestID").val(), function(data) {
			  $(pageid + " #WorkflowAttachmentsBar").html(data);
			  $(pageid + " #WorkflowAttachmentsBar").trigger('create');
			  
		});    	
	}
}

function WorkflowChatReply(request_id, conversation_id, reply_message, pageid){
	$.ajax({
	    url: "workflow/WorkflowChatExecute",
	    data: {
	    	ConversationID: conversation_id,
	    	ReplyMessage: reply_message,
	    	RequestID: request_id
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		showChatContent(pageid);
	    	}else{
	    		$( pageid + " #AJAXErrorMessage" ).html(json.error);
	    		setTimeout(function() {
		    		$( pageid + " #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	    		}, 2000);
	    	}
	    },
	    error: function( xhr, status ) {
	    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
    		setTimeout(function() {
	    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
    		}, 2000);
	    }
	});
	return false;
}

function showChatContent(pageid){
	
	if (parseFloat($(pageid + " #RequestID").val()) > 0){
		
		$.get('WorkflowManagerChat.jsp?RequestID='+$(pageid + " #RequestID").val()+'&PageID='+pageid, function(data) {
			  $(pageid + " #WorkflowChatContent").html(data);
			  $(pageid + " #WorkflowChatContent").trigger('create');
			  
			  var ChatPreview = $(pageid + " #WorkflowChatLastMessage").val();
			  
			  if (ChatPreview.length > 5){
				  $(pageid + " #WorkflowChatPreview").text(ChatPreview);
				  $(pageid + " #WorkflowChatPreviewContainer").css("display","block");
			  }
			  
			  
			  
				$(pageid + " #WorkflowChatSendNewButton").on( "click", function( event, ui ) {
					$.ajax({
					    url: "workflow/WorkflowChatExecute",
					    data: $(pageid + " #WorkflowChatSendNewForm" ).serialize(),
					    type: "POST",
					    dataType : "json",
					    success: function( json ) {
					    	if (json.success == "true"){
					    		showChatContent(pageid);
					    	}else{
					    		$( pageid + " #AJAXErrorMessage" ).html(json.error);
					    		setTimeout(function() {
						    		$( pageid + " #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
					    		}, 2000);
					    	}
					    },
					    error: function( xhr, status ) {
					    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
				    		setTimeout(function() {
					    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
				    		}, 2000);
					    }
					});
				});
		});    	
	}
	
	
	
}




function setWorkflowEvents(pageid){

    var manualuploader = $(pageid + ' #manual-fine-uploader').fineUploader({
        autoUpload: false,
        text: {
        	uploadButton: '<i class="icon-plus icon-white"></i> Select Files'
        },
        request: {
        	endpoint: 'util/UploadFile',
        	paramsInBody: true,
            params: {
            	RequestID: $(pageid + " #RequestID").val(),
            	description: function(){
            		return $(pageid + " #WorkflowAttachmentDescription").val();
            	}
            }
        }
    }).on('error', function(event, id, name, reason) {

    })
    .on('complete', function(event, id, name, responseJSON){
    	showAttachments(pageid);
    	$.mobile.changePage(pageid);
    	
    });
   
    $(pageid + ' #triggerUpload').click(function() {
    	manualuploader.fineUploader('uploadStoredFiles');
    });
      
	$(pageid + " .WorkflowAttachButton").on( "click", function( event, ui ) {
		$( pageid +" #popup_workflow_attach" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});
	
	$(pageid + " .WorkflowConversationButton").on( "click", function( event, ui ) {
		$( pageid +" #popup_workflow_conversation" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});
	
	$(pageid + " .WorkflowDeclineButton").on( "click", function( event, ui ) {
		$( pageid +" #popup_workflow_decline" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});

	$(pageid + " .WorkflowDeclineConfirmButton").on( "click", function( event, ui ) {
		
		$.ajax({
		    url: "workflow/DeclineRequestExecute",
		    data: $(pageid + " .WorkflowForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.mobile.changePage("WorkflowManager.jsp?requestID="+json.RequestID+"&processID=1&SecondPage=1");
		    	}else{
		    		$( pageid + " #AJAXErrorMessage" ).html(json.error);
		    		setTimeout(function() {
			    		$( pageid + " #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
		    		}, 2000);
		    	}
		    },
		    error: function( xhr, status ) {
		    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
	    		setTimeout(function() {
		    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	    		}, 2000);
		    }
		});
		
	});
	
	
	$(pageid + " .WorkflowSubmitButton").on( "click", function( event, ui ) {
		
		$(pageid+ ' .WorkflowForm').submit();
	});

	$(pageid +" .WorkflowSubmitConfirm").on("click", function( event, ui ) {
		event.preventDefault();
		$(pageid + " #WorkflowStepRemarks").val($( pageid + " #WorkflowStepRemarksPopup").val());
		
		$.mobile.changePage(pageid);
		
		$.ajax({
		    url: "inventory/WorkflowManagerDiscountRequestExecute",
		    data: $(pageid + " .WorkflowForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		//alert();
		    		$.mobile.changePage("WorkflowManagerPromotionRequest.jsp?requestID="+json.RequestID+"&processID=1&SecondPage=1");
		    	}else{
		    		$( pageid + " #AJAXErrorMessage" ).html(json.error);
		    		setTimeout(function() {
			    		$( pageid + " #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
		    		}, 2000);
		    	}
		    },
		    error: function( xhr, status ) {
		    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
	    		setTimeout(function() {
		    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	    		}, 2000);
		    }
		});
	});		
}




function addPerCaseRow(pageid){
	
	var num = parseFloat($(pageid + ' #DetailRows').val());
	num++;

	var rowsnum = INVENTORY_PACKAGE_JSON.rows.length;
	var rowsbrand = INVENTORY_BRAND_JSON.rows.length;
	
	var x = 1;
	
	var content = '<tr>'+
   
   
    
    
   
    
    
    
	'</tr>';
	
	$(pageid + " #PerCaseSamplingContainer").append( content ).trigger('create');
	
				
				
				
					
				
	
	
	$(pageid + ' #DetailRows').val(num);
	
}

function GetBrandList(PackageIDVal, num){
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
	    url: "common/GetLiquidBrandListJson",
	    data: {
	    	'PackageID' : PackageIDVal
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	if (json.exists == "true"){
	    		//alert((json.SelectOptions).replace("<select id='select_id' name='select_id' data-mini='true' ><option value=''>Select Brand</option>", "<select id='PerCaseBrand"+num+"' name='PerCaseBrand"+num+"' data-mini='true' ><option value='0'>All</option>"));
	    		$('#BrandSpan'+num).html((json.SelectOptions).replace("<select id='select_id' name='select_id' data-mini='true' ><option value=''>Select Brand</option>", "<select id='PerCaseBrand"+num+"' name='PerCaseBrand"+num+"' data-mini='true' ><option value='0'>All</option>"));
	    		$('#BrandSpan'+num).trigger('create');
	    	}else{
	    		alert('Server could not be reached');
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert('Server could not be reached');
	    }
	});
	
}

function populateDispatchForm(value){

	if( value == "2" ){
		$('#TCSBarcodeTD').css('display', 'table-row');
		$('#UserIDTD').css('display', 'none');
	}else if( value == "1" ){
		$('#TCSBarcodeTD').css('display', 'none');
		$('#UserIDTD').css('display', 'table-row');
	}else{
		$('#TCSBarcodeTD').css('display', 'none');
		$('#UserIDTD').css('display', 'none');
	}
	
}

	
function getSampling(pageid, request_id){
	$.ajax({
	    url: "sampling/GetSamplingJson",
	    data: {
	        RequestID: request_id
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	if (json.exists == "true"){
	    		
	    		
	    		$(pageid + ' #OutletID').attr("readonly","readonly");
	    		
	    		$(pageid + ' #SamplingID').val(json.sampling_id);

				$(pageid + ' #OutletID').val(json.outlet_id);
								
				
				$(pageid +" #WorkflowOutletDashboardLink").attr("href", "OutletDashboard.jsp?outletID="+json.outlet_id);
				
				
				$(pageid + ' #OutletName').val(json.outlet_name);
				$(pageid + ' #BusinessType').val(json.business_type);
				$(pageid + ' #address').val(json.address);
				$(pageid + ' #region').val(json.region);
				$(pageid + ' #asm').val(json.asm);
				$(pageid + ' #cr').val(json.cr);
				$(pageid + ' #market').val(json.market);
				$(pageid + ' #vehicle').val(json.vehicle);
				$(pageid + ' #AdvanceCompanyShare').val(json.advance_company_share);
				$(pageid + ' #AdvanceAgencyShare').val(json.advance_agency_share);
				$(pageid + ' #AdvanceValidFrom').val(json.advance_valid_from);
				$(pageid + ' #AdvanceValidTo').val(json.advance_valid_to);
				$(pageid + ' #FixedCompanyShare').val(json.fixed_company_share);
				$(pageid + ' #FixedAgencyShare').val(json.fixed_agency_share);
				$(pageid + ' #FixedDeductionTerm').val(json.fixed_deduction_term);
				$(pageid + ' #FixedValidFrom').val(json.fixed_valid_from);
				$(pageid + ' #FixedValidTo').val(json.fixed_valid_to);
				$(pageid + ' #FixedCompanyShareOP').val(json.fixed_company_share_offpeak);
				$(pageid + ' #FixedAgencyShareOP').val(json.fixed_agency_share_offpeak);
				$(pageid + ' #FixedDeductionTermOP').val(json.fixed_deduction_term_offpeak);
				
				var rowsnum = json.rows.length;
				var x = 1;
				for (x = 0; x < rowsnum; x++){
					
					if (x > 1){
						addPerCaseRow(pageid);
					}
					
					$(pageid + " #PerCasePackage" + (x+1)).val(json.rows[x].package);
					$(pageid + " #PerCasePackage" + (x+1)).selectmenu("refresh", true);
					
					$(pageid + " #PerCaseBrand" + (x+1)).val(json.rows[x].brand_id);
					$(pageid + " #PerCaseBrand" + (x+1)).selectmenu("refresh", true);
					
					
					$(pageid + " #PerCaseAgencyShare" + (x+1)).val(json.rows[x].agency_share);
					$(pageid + " #PerCaseCompanyShare" + (x+1)).val(json.rows[x].company_share);
					$(pageid + " #PerCaseDeductionTerm" + (x+1)).val(json.rows[x].deduction_term);
					$(pageid + " #PerCaseValidFrom" + (x+1)).val(json.rows[x].valid_from);
					$(pageid + " #PerCaseValidTo" + (x+1)).val(json.rows[x].valid_to);
					$(pageid + " #PerCaseHandToHand" + (x+1)).val(json.rows[x].hand_to_hand);
					
					
				}
	    		
				var threshold_rowsnum = json.threshold_rows.length;
				
				for (x = 0; x < threshold_rowsnum; x++){
					$(pageid + " #SalesThresholdPercentage" + (x+1)).val(json.threshold_rows[x].percentage);
					$(pageid + " #SalesThresholdSales" + (x+1)).val(json.threshold_rows[x].converted_sales);
					$(pageid + " #SalesThresholdDiscount" + (x+1)).val(json.threshold_rows[x].discount);
				}
				
	    		var lat = json.latitude;
	    		var lng = json.longitude;
	    		
	    		if (parseFloat(lat) > 1){
	    			$(pageid + ' #OutletMap').attr("src","http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C"+lat+","+lng+"&sensor=false");
	    		}else{
	    			$(pageid + ' #OutletMap').attr("src","images/GPSUnavailable.png");
	    		}
	    	}else{
	    	}
	    },
	    error: function( xhr, status ) {
	        alert( "Sorry, the server did not respond." );
	    }
	});
}

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function openLookupEmployee(){
	$('#PopupDashboard').popup("close");
	setTimeout(function(){
		$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupEmployeeInit();
		} );
		$('#LookupEmployeeSearch').popup("open", {transition:"pop"});
	},"500");
}

function openLookupOutlet(){
	$('#PopupDashboard').popup("close");
	setTimeout(function(){
		$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupOutletInit();
		} );
		$('#LookupOutletSearch').popup("open", {transition:"pop"});
	},"500");
}

function openLookupDistributor(){
	$('#PopupDashboard').popup("close");
	setTimeout(function(){
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open", {transition:"pop"});
	},"500");
}

function openLookupRegion(){
	$('#PopupDashboard').popup("close");
	setTimeout(function(){
		$( "#LookupRegionSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupRegionInit();
		} );
		$('#LookupRegionSearch').popup("open", {transition:"pop"});
	},"500");
}

function setEmployeeLookupAtHome(SAPCode, EmployeeName){
	 $('#LookupEmployeeSearch').popup("close");
	 $("#DashboardOrderBookerEmployeeCode").val(SAPCode);
	 document.getElementById("DashboardOrderBookerFormID").submit();
	 //window.location = 'OrderBookerDashboardOverview.jsp?EmployeeCode='+SAPCode;
	}


function setOutletLookupAtHome(SAPCode, OutletName){
 $('#LookupOutletSearch').popup("close");
 
 $("#DashboardOutletoutletID").val(SAPCode);
 document.getElementById("DashboardOutletFormID").submit();
 //window.location = 'OutletDashboard.jsp?outletID='+SAPCode;
}

function setDistributorLookupAtHome(SAPCode, DistributorName){
	 $('#LookupDistributorSearch').popup("close");
	 
	 $("#DashboardDistributorDistributorCode").val(SAPCode);
	 document.getElementById("DashboardDistributorFormID").submit();
	 //window.location = 'DistributorDashboard.jsp?DistributorCode='+SAPCode;
	}

function setRegionLookupAtHome(SAPCode, RegionName){
	$('#LookupRegionSearch').popup("close");
	window.location = 'RegionDashboard.jsp?RegionCode='+SAPCode;
}