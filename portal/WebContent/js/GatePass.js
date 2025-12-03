
$( document ).delegate("#GatePass", "pageshow", function() {
	document.getElementById("GatePassBarcode").focus();
	showDeliveredGatePassQueue();
	setInterval(showDeliveredGatePassQueue, 60000);	
});


function setSubmitButton(status){
	  if( status == true ){
		  $('#GatePassSave').removeClass("ui-disabled");
	  }else{
		  $('#GatePassSave').addClass("ui-disabled");
		  $("#SearchContent").html("Barcode doesn'nt exists.");
	  }
}

function showSearchContent(){
	if( $('#GatePassBarcode').val() != '' ){
		$.mobile.showPageLoadingMsg();
		$.get('DeliveryNotePrint.jsp?Document=GatePass&Barcode='+$('#GatePassBarcode').val(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#SearchContent").html(data);
			
		});
	}
	document.getElementById("GatePassBarcode").focus();
	return false;

}

function GatePassSubmit(){
	
	$('#GatePassSave').addClass("ui-disabled");
	
	$.ajax({
	    url: "inventory/GatePassExecute",
	    
	    data: {
	    	
	    	GatePassBarcode : $('#GatePassBarcode').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		$('#GatePassBarcode').val('');
	    		$("#SearchContent").html('');
	    		document.getElementById("GatePassBarcode").focus();
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function showDeliveredGatePassQueue(){
	//alert();
		//$.mobile.showPageLoadingMsg();
		$.get('GatePassQueue.jsp', function(data) {
			//$.mobile.hidePageLoadingMsg();
			//alert(data);
			$("#DeliveredGatePassQueue").html(data);
			$("#DeliveredGatePassQueue").trigger('create');
			
		});
	return false;

}