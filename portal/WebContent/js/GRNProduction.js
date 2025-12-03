
$( document ).delegate("#GRNProduction", "pageshow", function() {
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
	//alert();
	if( $('#GRNBarcode').val() != '' ){
		//checking for if is_already received =1 
		
		
		$.ajax({
		    url: "inventory/ValidateGRNProductionJson",
		    
		    data: {
		    	
		    	GRNBarcode : $('#GRNBarcode').val()
		    	
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.get('ReceiptFromProductionPrinting.jsp?GRNProBarCode='+$('#GRNBarcode').val(), function(data) {
		    			$.mobile.hidePageLoadingMsg();
		    			$("#SearchContent").html(data);
		    		});
		    	}else{
		    		//alert("This GRN has already been sumbitted.");
		    		$("#SearchContent").html("This GRN has already been sumbitted.");
		    		$("#GRNSave").addClass("ui-disabled");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
		
	}
	//document.getElementById("GRNBarcode").val("");
	document.getElementById("GRNBarcode").focus();
	$("#GRNSave").removeClass("ui-disabled");
	return false;

}

function GRNSubmit(){
	
	$('#GRNSave').addClass("ui-disabled");
	
	$.ajax({
	    url: "inventory/GRNProductionExecute",
	    
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