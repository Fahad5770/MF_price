
$( document ).delegate("#GRN", "pageshow", function() {
	document.getElementById("GRNBarcode").focus();
});

function setSubmitButton(status){
	  if( status == true ){
		  $('#GRNSave').removeClass("ui-disabled");
	  }else{
		  $('#GRNSave').addClass("ui-disabled");
	  }
}

function showSearchContent(){
	if( $('#GRNBarcode').val() != '' ){
		//alert();
		$.ajax({
		    url: "inventory/ValidateGRNJson",
		    
		    data: $("#GRNForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.mobile.showPageLoadingMsg();
		    		$.get('DeliveryNotePrint.jsp?Document=GRN&Barcode='+$('#GRNBarcode').val(), function(data) {
		    			$.mobile.hidePageLoadingMsg();
		    			$("#SearchContent").html(data);
		    		});
		    	}
		    	else if(json.success == "not allowed")
		    	{
		    		alert("Access denied for this GRN");
		    		$("#GRNSave").addClass( "ui-disabled" );
		    		$("#SearchContent").html("");
		    	}
		    	else{
					alert(json.error);
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
	document.getElementById("GRNBarcode").focus();
	return false;

}

function GRNSubmit(){
	
	$('#GRNSave').addClass("ui-disabled");
	
	$.ajax({
	    url: "inventory/GRNExecute",
	    
	    data: {
	    	
	    	GRNBarcode : $('#GRNBarcode').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		$('#GRNBarcode').val('');
	    		$("#SearchContent").html('');
	    		document.getElementById("GRNBarcode").focus();
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}