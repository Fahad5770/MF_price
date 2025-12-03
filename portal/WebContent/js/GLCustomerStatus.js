

$( document ).delegate("#GLCustomerStatusPage", "pageinit", function() {
	
	
	
});

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function GetSaleOrderStatus(){
	//alert("SaleOrderNo = "+SaleOrderNo);
	
	if( $('#CustomerID').val() == "" ){
		
		return false;
	}else{
		if( isInteger($('#CustomerID').val()) == false ){
			$('#CustomerID').val('');
			$('#CustomerName').val('');
			$('#CurrentBalance').val('');
			return false;
		}
	}
	
	$.mobile.showPageLoadingMsg();	
	$.ajax({
	    url: "sap/GLCustomerStatusGetCustomerInfoJson",
	    data: {
	    	CustomerID: $('#CustomerID').val(),
	    	isEditCase : false
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	if( json.success == true ){
	    		
	    		
		    	
		    	//$('#CustomerID').val( json.DistrubutorID );
		    	$('#CustomerName').val( json.CustomerName );
		    	
		    	$('#LedgerBalance').val( numberWithCommas(json.LedgerBalance) ); 
				$('#SecurityBalance').val( numberWithCommas(json.SecurityBalance) );
				$('#CreditLimit').val( numberWithCommas(json.CreditLimit) );
				$('#AvailableLimit').val( numberWithCommas(json.AvaliableBalance) );
		    	
		    	
	    	}else{
	    		$('#InvoiceNo').val( '' );
		    	$('#InvoiceAmount').val( '' );
		    	$('#InvoiceAmountHidden').val( '0' );
		    	
		    	$('#CustomerID').val( '' );
		    	$('#CustomerName').val( '' );
		    	
		    	$('#CurrentBalance').val( '' );
		    	
		    	$('#Status').val('0');
	    		$('#StatusDiv').html( '' );
	    		$('#SubmitButton').addClass('ui-disabled');
	    		
	    		alert(json.error);
	    	}

	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.hidePageLoadingMsg();
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



function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

