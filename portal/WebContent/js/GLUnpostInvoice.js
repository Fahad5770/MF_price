

$( document ).delegate("#GLUnpostInvoicePage", "pageinit", function() {
	
	
	
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
	    url: "sap/GLUnpostInvoiceGetOrderInfoJson",
	    data: {
	    	SaleOrderNo: $('#OrderNo').val(),
	    	isEditCase : false
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	if( json.success == true ){
	    		
	    		if(json.isLifted){
	    			
	    			$('#OrderDate').val( '' );
		    		$('#InvoiceNo').val( '' );
		    		$('#InvoiceDate').val( '' );
			    	$('#InvoiceAmount').val( '' );
			    	$('#InvoiceAmountHidden').val( '0' );
			    	
			    	$('#CustomerID').val( '' );
			    	$('#CustomerName').val( '' );
			    	
			    	$('#CurrentBalance').val( '' );
			    	
			    	$('#Status').val('0');
		    		$('#StatusDiv').html( '' );
		    		$('#SubmitButton').addClass('ui-disabled');
		    		
		    		alert("Invoice is Lifted");
	    			
	    			return false;
	    		}
	    		
	    		$('#OrderDate').val( json.OrderDate );
	    		
	    		$('#InvoiceNo').val( json.InvoiceNo );
	    		$('#InvoiceDate').val( json.InvoiceDate );
	    		
		    	$('#InvoiceAmount').val( numberWithCommas(json.InvoiceAmount) );
		    	$('#InvoiceAmountHidden').val( json.InvoiceAmount );
		    	
		    	$('#CustomerID').val( json.CustomerID );
		    	$('#CustomerName').val( json.CustomerName );
		    	
		    	$('#CurrentBalance').val( numberWithCommas(json.CurrentBalance) );
		    	
		    	
		    	
	    	}else{
	    		$('#OrderDate').val( '' );
	    		$('#InvoiceNo').val( '' );
	    		$('#InvoiceDate').val( '' );
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

function FormSubmit(){
	
	if( $('#OrderNo').val() == "" ){
		alert("Please enter Order No");
		$('#OrderNo').focus();
		return false;
	}
	
	
	$('#SubmitButton').addClass("ui-disabled");
	
	
	$.ajax({
	    url: "cash/GLUnpostInvoiceExecute",
	    
	    data: $('#UnpostInvoiceExecute').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLUnpostInvoice.jsp';
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

