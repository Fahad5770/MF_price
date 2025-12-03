
var DocumentType = 0;

$( document ).delegate("#SamplingCreditSlipReceiving", "pageshow", function() {
	
	document.getElementById("SamplingCreditSlipReceivingBarcode").focus();
		
});


function setSubmitButton(status, DocumentTypeValue){
	
		DocumentType = DocumentTypeValue;
	
	  if( status == true ){
		  $('#SamplingCreditSlipReceivingSave').removeClass("ui-disabled");
	  }else{
		  $('#SamplingCreditSlipReceivingSave').addClass("ui-disabled");
		  $("#SearchContent").html("Barcode does not exist or already received.");
	  }
}

function showSearchContent(){
	if( $('#SamplingCreditSlipReceivingBarcode').val() != '' ){
		$.mobile.showPageLoadingMsg();
		$.get('SamplingCreditSlipReceivingView.jsp?Barcode='+$('#SamplingCreditSlipReceivingBarcode').val(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#SearchContent").html(data);
		});
	}
	document.getElementById("SamplingCreditSlipReceivingBarcode").focus();
	return false;

}

function SamplingCreditSlipReceivingSubmit(){
	
	
	
	$('#SamplingCreditSlipReceivingSave').addClass("ui-disabled");
	
	$.ajax({
	    url: "sampling/CreditSlipReceivingExecute",
	    
	    data: {
	    	
	    	Barcode : $('#SamplingCreditSlipReceivingBarcode').val(),
	    	DocumentType : DocumentType
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		$('#SamplingCreditSlipReceivingBarcode').val('');
	    		$("#SearchContent").html('');
	    		document.getElementById("SamplingCreditSlipReceivingBarcode").focus();
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}
