

$( document ).delegate("#GLCashPayment", "pageinit", function() {
	
	
	
});

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function FormSubmit(){
	
	if( $('#DistributorID').val() == "" ){
		alert("Please select Customer");
		$('#DistributorID').focus();
		return false;
	}else{
		if( isInteger( $('#DistributorID').val() ) == false ){
			alert("Customer ID must be an Integer.");
			$('#DistributorID').focus();
			return false;
		}else{
			if( $('#isDistributorValid').val() == 'false' ){
				alert("Please enter valid Customer ID.");
				$('#DistributorID').focus();
				return false;
			}
		}
	}
	
	if( $('#BankAccountID').val() == "" ){
		alert("Please select Bank");
		$('#BankAccountID').focus();
		return false;
	}
	
	var len = $('input[name=InstrumentAmount]').length;
	var isEmpty = true;
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=InstrumentAmount]')[i].value;
		if( Amount != '' && Amount != '0' ){
			isEmpty = false;
			break;
		}
	}
	
	if( isEmpty ){
		alert("Atleast one Instrument is mandatory.");
		$('input[name=InstrumentAmount]')[0].focus();
		return false;
	}
	
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=InstrumentAmount]')[i].value;
		if( isFloat(Amount) == false ){
			alert("Amount must be integer or in decimal value.");
			$('input[name=InstrumentAmount]')[i].focus();
			return false;
		}
	}
	
	
	$('#SubmitButton').addClass("ui-disabled");
	
	$('#DistributorIDSubmitForm').val( $('#DistributorID').val() );
	$('#BankAccountIDSubmitForm').val( $('#BankAccountID').val() );
	$('#NarrationSubmitForm').val( $('#Narration').val() );
	
	$.ajax({
	    url: "cash/GLChequeReturnExecute",
	    
	    data: $('#GLCashPaymentForm').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLChequeReturn.jsp';
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
	var len = $('input[name=InstrumentAmount]').length;
	var TotalAmount = 0;
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=InstrumentAmount]')[i].value;
		if( Amount == '' ){
			Amount = 0;
		}
		TotalAmount += parseFloat(Amount);
	}
	$('#InstrumentAmountTotal').val( numberWithCommas((TotalAmount).toFixed(2)));
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function DeleteRow(RowID){
	
	$("#"+RowID).remove();
	var RowCount = parseInt($('#RowMaxID').val());
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#SubmitButton').addClass('ui-disabled');
	}
	
	$('#RowMaxID').val(RowCount);
	setTotal();
}

function AddRow(){
	
	if( $('#AddRowInstrumentType').val() == "" ){
		alert("Please select Instrument Type");
		$('#AddRowInstrumentType').focus();
		return false;
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
	
	if( $('#CaptureReferenceNo').val() == 'true' ){
		if( $('#AddRowReferenceNo').val() == '' ){
			alert('Please enter Reference#');
			$('#AddRowReferenceNo').focus();
			return false;
		}
	}
	
	if( $('#CaptureDate').val() == 'true' ){
		if( $('#AddRowDate').val() == '' ){
			alert('Please select Date');
			$('#AddRowDate').focus();
			return false;
		}
	}
	
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
				
				var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
				
				var content = ""+
				"<tr id='GLCashReceiptTR"+RowMaxID+"'>"+
					"<td>"+json.InstrumentLabel+"</td>"+
					"<td style='text-align: right; padding-right: 20px'>"+numberWithCommas(parseFloat($('#AddRowAmount').val()).toFixed(2))+
						"<input type='hidden' name='InstrumentAmount' id='InstrumentAmount' value='"+$('#AddRowAmount').val()+"'>"+
						"<input type='hidden' name='InstrumentID' id='InstrumentID' value='"+json.InstrumentID+"'>"+
						"<input type='hidden' name='InstrumentCategoryID' id='InstrumentCategoryID' value='"+json.InstrumentCategoryID+"'></td>"+
						"<input type='hidden' name='InstrumentSerialNo' id='InstrumentSerialNo' value='0'></td>"+
					
					"<td>"+$('#AddRowReferenceNo').val()+
						"<input type='hidden' name='InstrumentNo' id='InstrumentNo' value='"+$('#AddRowReferenceNo').val()+"'></td>"+
					
					"<td>"+setDateFormat($('#AddRowDate').val())+
						"<input type='hidden' name='InstrumentDate' id='InstrumentDate' value='"+setDateFormat($('#AddRowDate').val())+"'></td>"+
						
					"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeleteRow('GLCashReceiptTR"+RowMaxID+"');\">Delete</a></td>"+
				"</tr>";
				
				$("#GLCashReceiptTableBody").append(content).trigger('create');
				$('#NoProductRow').css('display', 'none');
				$('#SubmitButton').removeClass('ui-disabled');
				$('#RowMaxID').val(RowMaxID);
				
				setTotal();
				clearFields();
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
	return false;
	
}

function AutoAddRowCheque(InstrumentLabel, Amount, InstrumentID, InstrumenCategoryID, ReferenceNo, InstrumentDate, SerialNo, CustomerID){
	
	var len = $('input[name=InstrumentSerialNo]').length;
	for( var i = 0 ; i < len ; i++ ){
		if( $('input[name=InstrumentSerialNo]')[i].value == SerialNo ){ 
			return false;
		}
	}
	//return false;
	
	var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
	
	if( RowMaxID > 1 ){
		return false;
	}
	
	var content = ""+
	"<tr id='GLCashReceiptTR"+RowMaxID+"'>"+
		"<td>"+InstrumentLabel+"</td>"+
		"<td style='text-align: right; padding-right: 20px'>"+numberWithCommas(parseFloat(Amount).toFixed(2))+
			"<input type='hidden' name='InstrumentAmount' id='InstrumentAmount' value='"+Amount+"'>"+
			"<input type='hidden' name='InstrumentID' id='InstrumentID' value='"+InstrumentID+"'>"+
			"<input type='hidden' name='InstrumentCategoryID' id='InstrumentCategoryID' value='"+InstrumenCategoryID+"'></td>"+
			"<input type='hidden' name='InstrumentSerialNo' id='InstrumentSerialNo' value='"+SerialNo+"'></td>"+
			
		"<td>"+ReferenceNo+
			"<input type='hidden' name='InstrumentNo' id='InstrumentNo' value='"+ReferenceNo+"'></td>"+
		
		"<td>"+setDateFormat(InstrumentDate)+
			"<input type='hidden' name='InstrumentDate' id='InstrumentDate' value='"+setDateFormat(InstrumentDate)+"'></td>"+
			
		"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeleteRow('GLCashReceiptTR"+RowMaxID+"');\">Delete</a></td>"+
	"</tr>";
	
	$("#GLCashReceiptTableBody").append(content).trigger('create');
	$('#NoProductRow').css('display', 'none');
	$('#SubmitButton').removeClass('ui-disabled');
	$('#RowMaxID').val(RowMaxID);
	setTotal();
	
	$('#DistributorID').val(CustomerID);
	getDistributorName();
}


function clearFields(){
	$('#AddRowInstrumentType').val('').change();
	$('#AddRowAmount').val('');
	$('#AddRowReferenceNo').val('');
	$('#AddRowDate').val('');
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
