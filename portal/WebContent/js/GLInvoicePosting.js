

$( document ).delegate("#GLInvoicePostingPage", "pageinit", function() {
	
	
	
});

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function GetSaleOrderStatus(){
	//alert("SaleOrderNo = "+SaleOrderNo);
	$.mobile.showPageLoadingMsg();	
	$.ajax({
	    url: "sap/GLInvoicePostingGetOrderInfoJson",
	    data: {
	    	SaleOrderNo: $('#OrderNo').val(),
	    	isEditCase : false
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	if( json.success == true ){
	    		
	    		$('#OrderDate').val( json.OrderDate );
	    		
	    		$('#InvoiceNo').val( json.InvoiceNo );
	    		$('#InvoiceDate').val( json.InvoiceDate );
	    		
		    	$('#InvoiceAmount').val( numberWithCommas(json.InvoiceAmount) );
		    	$('#InvoiceAmountHidden').val( json.InvoiceAmount );
		    	
		    	$('#CustomerID').val( json.DistrubutorID );
		    	$('#CustomerName').val( json.DistrubutorName );
		    	
		    	$('#CurrentBalance').val( numberWithCommas(json.CurrentBalance) );
		    	$('#AdvanceAgainstOrder').val( numberWithCommas(json.AdvanceAgainstOrder) );
		    	$('#AdvanceAgainstOrderHidden').val( json.AdvanceAgainstOrder );
		    	
		    	
		    	
		    	var InvoiceAmount = parseFloat(json.InvoiceAmount);
		    	var CurrentBalance = parseFloat(json.CurrentBalance); 
		    	var AdvanceAgainstOrder = parseFloat(json.AdvanceAgainstOrder); 
		    	

		    	
		    	if( InvoiceAmount <=  ( CurrentBalance + AdvanceAgainstOrder ) ){ 
		    		$('#Status').val('1');
		    		$('#StatusDiv').html( '' );
		    		$('#SubmitButton').removeClass('ui-disabled');
		    	}else{
		    		$('#Status').val('0');
		    		$('#StatusDiv').html('Insufficient Balance');
		    		//$('#SubmitButton').addClass('ui-disabled');
		    	}
		    	
	    	}else{
	    		$('#InvoiceNo').val( '' );
		    	$('#InvoiceAmount').val( '' );
		    	$('#InvoiceAmountHidden').val( '0' );
		    	
		    	$('#CustomerID').val( '' );
		    	$('#CustomerName').val( '' );
		    	
		    	$('#CurrentBalance').val( '' );
		    	$('#AdvanceAgainstOrder').val( '' );
		    	$('#AdvanceAgainstOrderHidden').val( '0' );
		    	
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

function FormSubmit(){
	
	if( $('#OrderNo').val() == "" ){
		alert("Please enter Order No");
		$('#OrderNo').focus();
		return false;
	}
	
	
	$('#SubmitButton').addClass("ui-disabled");
	
	
	$.ajax({
	    url: "cash/GLInvoicePostingExecute",
	    
	    data: $('#InvoicePostingExecute').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLInvoicePosting.jsp';
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



function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

