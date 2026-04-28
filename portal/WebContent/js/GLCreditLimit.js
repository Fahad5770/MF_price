

$( document ).delegate("#GLCreditLimitPage", "pageinit", function() {
	
	
	
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
	
	if( $('#CreditLimit').val() == "" ){
		alert("Please enter Credit Limit");
		$('#CreditLimit').focus();
		return false;
	}else{
		if( isFloat( $('#CreditLimit').val() ) == false ){
			alert("Credit Limit must be a number.");
			$('#CreditLimit').focus();
			return false;
		}
	}
	
	if( $('#ValidFrom').val() == "" ){
		alert("Please enter Valid From");
		$('#ValidFrom').focus();
		return false;
	}
	
	if( $('#ValidTo').val() == "" ){
		alert("Please enter Valid To");
		$('#ValidTo').focus();
		return false;
	}
	
	$('#ValidFromDate').val(setDateFormat($('#ValidFrom').val()));
	$('#ValidToDate').val(setDateFormat($('#ValidTo').val()));
	
	$('#SubmitButton').addClass("ui-disabled");
	
	$.ajax({
	    url: "cash/GLCreditLimitExecute",
	    
	    data: $('#GLCreditLimitExecute').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLCreditLimit.jsp';
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
	
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: $('#CustomerID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
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

