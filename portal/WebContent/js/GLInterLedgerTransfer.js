

$( document ).delegate("#GLInterLedgerTransferPage", "pageinit", function() {
	
	
	
});

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function FormSubmit(){
	
	if( $('#CustomerID').val() == "" ){
		alert("Please select Customer");
		$('#CustomerID').focus();
		return false;
	}else{
		if( isInteger( $('#CustomerID').val() ) == false ){
			alert("Customer ID must be an Integer.");
			$('#CustomerID').focus();
			return false;
		}else{
			if( $('#isCustomerValid').val() == 'false' ){
				alert("Please enter valid Customer ID.");
				$('#CustomerID').focus();
				return false;
			}
		}
	}
	
	if( $('#DebitAccountTypeID').val() == "0" ){
		alert("Please select Debit Account");
		$('#DebitAccountTypeID').focus();
		return false;
	}
	
	if( $('#CreditAccountTypeID').val() == "0" ){
		alert("Please select Credit Account");
		$('#CreditAccountTypeID').focus();
		return false;
	}
	
	if( $('#Amount').val() == "" ){
		alert("Please enter Amount");
		$('#Amount').focus();
		return false;
	}
	
	if( $('#Reason').val() == "" ){
		alert("Please enter Reason");
		$('#Reason').focus();
		return false;
	}
	
	$('#SubmitButton').addClass("ui-disabled");
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
	    url: "cash/GLInterLedgerTransferExecute",
	    
	    data: $('#ExecuteForm').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLInterLedgerTransfer.jsp';
	    	}else{
	    		alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
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
	$('#CustomerID').val(SAPCode);
	$('#CustomerName').val(DistributorName);
	$('#isCustomerValid').val('true');
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
	
	if( $('#CustomerID').val() == "" ){
		return false;
	}else{
		if( isInteger( $('#CustomerID').val() ) == false ){
			return false;
		}
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: $('#CustomerID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.exists == "true"){
				$('#CustomerName').val(json.DistributorName);
				$('#isCustomerValid').val('true');
			}else{
				$('#CustomerName').val('');
				$('#isCustomerValid').val('false');
			}
		},
		error:function(xhr, status){
			//alert("Server could not be reached.");
		}
		
	});
		
}



function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

