

$( document ).delegate("#GLCustomerOpeningBalancesPage", "pageinit", function() {
	
});

function isInteger (o) {
	return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	return ! isNaN (o-0) && o != null;
}

function FormSubmit(){
	
	var len = $('input[name=Amount]').length;
	var isEmpty = true;
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=Amount]')[i].value;
		if( Amount != '' && Amount != '0' ){
			isEmpty = false;
			break;
		}
	}
	
	if( isEmpty ){
		alert("Atleast one entry is mandatory.");
		$('#DistributorID').focus();
		return false;
	}
	
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=Amount]')[i].value;
		if( isFloat(Amount) == false ){
			alert("Amount must be integer or in decimal value.");
			$('#DistributorID').focus();
			return false;
		}
	}
	
	$('#SubmitButton').addClass("ui-disabled");
	
	$.ajax({
	    url: "cash/GLCustomerOpeningBalancesExecute",
	    
	    data: $('#MainForm').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLCustomerOpeningBalances.jsp';
	    	}else{
	    		alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function setDateFormat(DateVal){
	if(DateVal == ''){
		return '';
	}
	var Year = DateVal.substr(0, 4);
	var Month = DateVal.substr(5, 2);
	var Day = DateVal.substr(8, 2);
	return Day+"/"+Month+"/"+Year;
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
				$('#isDistributorValid').val('true');
			}else{
				$('#DistributorName').val('');
				$('#isDistributorValid').val('false');
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
		
}

function openDistributorSelectionPopup(){
	$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
		//alert("hELLO");
		lookupDistributorInit();
	} );
	$('#LookupDistributorSearch').popup("open");
}

function DistributorSearchCallBackCashReceipt(SAPCode, DistributorName){
	$('#DistributorID').val(SAPCode);
	$('#DistributorName').val(DistributorName);
	$('#isDistributorValid').val('true');
}

function setTotal(){ 
	var len = $('input[name=Amount]').length;
	var TotalAmount = 0;
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=Amount]')[i].value;
		if( Amount == '' ){
			Amount = 0;
		}
		TotalAmount += parseFloat(Amount);
	}
	$('#AmountTotal').val( numberWithCommas((TotalAmount).toFixed(2)));
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function DeleteRow(RowID){
	$('#AmountSpan'+RowID).html('0.00');
	RowID = parseInt(RowID) - 1;
	$('input[name=Amount]')[RowID].value = 0;
	
	/*$("#"+RowID).remove();
	var RowCount = parseInt($('#RowMaxID').val());
	RowCount--;
	if( RowCount < 1){
		$('#NoItemRow').css('display', 'table-row');
		$('#SubmitButton').addClass('ui-disabled');
	}
	
	$('#RowMaxID').val(RowCount);*/
	setTotal();
}

function AddRow(){
	
	if( $('#DistributorID').val() == "" ){
		alert("Please select Customer");
		$('#DistributorID').focus();
		return false;
	}else{
		if( isInteger( $('#DistributorID').val() ) == false ){
			alert("Customer ID must be an Integer.");
			$('#DistributorID').focus();
			return false;
		}/*else{
			if( $('#isDistributorValid').val() == 'false' ){
				alert("Please enter valid Customer ID.");
				$('#DistributorID').focus();
				return false;
			}
		}*/
	}
	
	if( $('#AddRowAmount').val() == "" ){
		alert("Please select Amount");
		$('#AddRowAmount').focus();
		return false;
	}else{
		if( isFloat($('#AddRowAmount').val()) == false ){
			alert("Amount must be integer or in decimal value.");
			$('#AddRowAmount').focus();
			return false;
		}
	}
	
	var len = $('input[name=CustomerID]').length;
	var isExists = false;
	for( var i = 0; i < len; i++ ){
		var EnteredCustomer = $('#DistributorID').val();
		var ExistingCustomer = $('input[name=CustomerID]')[i].value;
		if( EnteredCustomer == ExistingCustomer ){
			isExists = true;
			break;
		}
	}
	
	if(isExists){
		alert("Customer already exists.");
		$('#DistributorID').focus();
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
				
				var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
				
				var content = ""+
				"<tr id='TableRow"+RowMaxID+"'>"+
					"<td>"+$('#DistributorID').val()+" - "+json.DistributorName+
						"<input type='hidden' name='CustomerID' id='CustomerID' value='"+$('#DistributorID').val()+"'></td>"+
					"<td style='text-align: right; padding-right: 20px'><span id='AmountSpan"+RowMaxID+"'>"+numberWithCommas(parseFloat($('#AddRowAmount').val()).toFixed(2))+"</span>"+
						"<input type='hidden' name='Amount' id='Amount' value='"+$('#AddRowAmount').val()+"'></td>"+	
					"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeleteRow("+RowMaxID+");\">Delete</a></td>"+
				"</tr>";
				
				$("#TableBody").append(content).trigger('create');
				$('#NoItemRow').css('display', 'none');
				$('#SubmitButton').removeClass('ui-disabled');
				$('#RowMaxID').val(RowMaxID);
				
				setTotal();
				clearFields();
				
			}else{
				alert("Please enter valid Customer ID.");
				$('#DistributorID').focus();
				return false;
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
	
	return false;
	
}

function clearFields(){
	$('#DistributorID').val('');
	$('#DistributorName').val('');
	$('#isDistributorValid').val('false');
	$('#AddRowAmount').val('');
	$('#DistributorID').focus();
}

function setInstrumentAttributes(){
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "cash/GetInstrumentTypeInfoJson",
		data: {
			InstrumentID: $('#AddRowInstrumentType').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				
				if(json.CaptureInstrumentNo == "0"){
					$('#AddRowReferenceNo').textinput('disable');
					$('#AddRowReferenceNo').val('');
					$('#CaptureReferenceNo').val('false');
				}else{
					$('#AddRowReferenceNo').textinput('enable');
					$('#CaptureReferenceNo').val('true');
				}
				
				if(json.CaptureDate == "0"){
					$('#AddRowDate').textinput('disable');
					$('#AddRowDate').val('');
					$('#CaptureDate').val('false');
				}else{
					$('#AddRowDate').textinput('enable');
					$('#CaptureDate').val('true');
				}
				
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

function doPrint(CashReceiptID){
	$('#GLCashReceiptID').val( CashReceiptID );
	$('#GLCashReceiptsPrintExecute').submit();
	
}
