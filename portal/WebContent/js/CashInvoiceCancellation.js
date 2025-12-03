
$( document ).delegate("#CashInvoiceCancellation", "pageshow", function() {
	
	document.getElementById("InvoiceNo").focus();
		
});

function showSearchContent(){
	if( $('#InvoiceNo').val() != '' ){
		$.mobile.showPageLoadingMsg();
		$.get('CashInvoiceCancellationGetInvoice.jsp?InvoiceNo='+$('#InvoiceNo').val(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#SearchContent").html(data).trigger('create');
		});
	}
	document.getElementById("InvoiceNo").focus();
	return false;

}

function doCancel(){
	
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
	    url: "cash/CashInvoiceCancellationExecute",
	    
	    data: {
	    	
	    	InvoiceNo : $('#InvoiceNo').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	if (json.success == "true"){
	    		window.location = 'CashInvoiceCancellation.jsp';
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
}

function doActive(){
	
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
	    url: "cash/CashInvoiceCancellationExecute",
	    
	    data: {
	    	
	    	InvoiceNo : $('#InvoiceNo').val(),
	    	isActive: 'true'
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	if (json.success == "true"){
	    		window.location = 'CashInvoiceCancellation.jsp';
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
}
