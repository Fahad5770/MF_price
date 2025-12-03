

$( document ).delegate("#GLAdjustedInvoicePostingPage", "pageinit", function() {
	
	
	
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
	    url: "sap/GLAdjustedInvoicePostingGetOrderInfoJson",
	    data: {
	    	SaleOrderNo: $('#OrderNo').val(),
	    	isEditCase : false
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	if( json.success == true ){
	    		
	    		$('#InvoiceNo').val( json.InvoiceNo );
		    	$('#InvoiceAmount').val( numberWithCommas(json.InvoiceAmount) );
		    	$('#InvoiceAmountVal').val( json.InvoiceAmount );
		    	
		    	$('#OrderDate').val(json.OrderDate);
		    	$('#InvoiceDate').val(json.InvoiceDate);
		    	
		    	$('#CustomerID').val( json.DistrubutorID );
		    	$('#CustomerName').val( json.DistrubutorName );
		    	
		    	$('#CurrentBalance').val( numberWithCommas(json.CurrentBalance) );
		    	
		    /*	var InvoiceAmount = parseFloat(json.InvoiceAmount);
		    	var CurrentBalance = parseFloat(json.CurrentBalance); 
		    	
		    	if( InvoiceAmount <= CurrentBalance ){ 
		    		$('#Status').val('1');
		    		$('#StatusDiv').html( '' );
		    		$('#SubmitButton').removeClass('ui-disabled');
		    	}else{
		    		$('#Status').val('0');
		    		$('#StatusDiv').html('Insufficient Balance');
		    		//$('#SubmitButton').addClass('ui-disabled');
		    	}
		    	*/
	    	}else{
	    		$('#InvoiceNo').val( '' );
		    	$('#InvoiceAmount').val( '' );
		    	$('#InvoiceAmountVal').val( '' );
		    	
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

function FormSubmit(){
	
	if( $('#OrderNo').val() == "" ){
		alert("Please enter Order No");
		$('#OrderNo').focus();
		return false;
	}
	
	
	$('#SubmitButton').addClass("ui-disabled");
	
	
	$.ajax({
	    url: "cash/GLAdjustedInvoicePostingExecute",
	    
	    data: $('#InvoicePostingExecute').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLAdjustedInvoicePosting.jsp';
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

