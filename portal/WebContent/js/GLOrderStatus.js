

$( document ).delegate("#GLOrderStatusPage", "pageinit", function() {
	
	
	
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
	    url: "sap/GLOrderStatusGetOrderInfoJson",
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
	    		
	    		var InvoiceGenerated = json.InvoiceGenerated;
	    		if(InvoiceGenerated){
	    			$('#InvoiceNo').val( json.InvoiceNo );
		    		$('#InvoiceDate').val( json.InvoiceDate );
		    		
			    	$('#InvoiceAmount').val( numberWithCommas(json.InvoiceAmount) );
			    	$('#InvoiceAmountHidden').val( json.InvoiceAmount );
			    	
			    	$('#CustomerID').val( json.DistrubutorID );
			    	$('#CustomerName').val( json.DistrubutorName );
	    		}else{
	    			$('#InvoiceNo').val( 'Not Generated' );
		    		$('#InvoiceDate').val( '' );
		    		
			    	$('#InvoiceAmount').val( '' );
			    	$('#InvoiceAmountHidden').val( '' );
			    	
			    	$('#CustomerID').val( '' );
			    	$('#CustomerName').val( '' );
	    		}
	    		
	    		
		    	
		    	var SAPPosted = json.SAPPosted;
		    	if(SAPPosted){
		    		$('#SAPPosted').val('Yes');
		    	}else{
		    		$('#SAPPosted').val('No');
		    	}
		    	
		    	var SAPLifted = json.SAPLifted;
		    	if(SAPLifted){
		    		$('#SAPLifted').val('Yes');
		    	}else{
		    		$('#SAPLifted').val('No');
		    	}
		    	
		    	var PortalPosted = json.PortalPosted;
		    	if(PortalPosted){
		    		$('#PortalPosted').val('Yes');
		    		$('#PortalPostedOn').val(json.PortalPostedOn);
		    	}else{
		    		$('#PortalPosted').val('No');
		    		$('#PortalPostedOn').val('');
		    	}
		    	
		    	var PortalLifted = json.PortalLifted;
		    	if(PortalLifted){ 
		    		$('#PortalLifted').val('Yes');
		    		$('#PortalLiftedOn').val(json.PortalLiftedOn);
		    	}else{
		    		$('#PortalLifted').val('No');
		    		$('#PortalLiftedOn').val('');
		    	}
		    	
		    	
		    	
	    	}else{
	    		$('#InvoiceNo').val( '' );
		    	$('#InvoiceAmount').val( '' );
		    	$('#InvoiceAmountHidden').val( '0' );
		    	
		    	$('#CustomerID').val( '' );
		    	$('#CustomerName').val( '' );
		    	
		    	$('#SAPPosted').val('');
		    	$('#SAPLifted').val('');
	    		
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

