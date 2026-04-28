
$( document ).delegate("#ProductPromotionsDeactivationPage", "pageshow", function() {
	
	document.getElementById("RequestID").focus();
		
});


function GetPromotionLabel(){
	$.mobile.showPageLoadingMsg();
	$.ajax({
	    url: "inventory/ProductPromotionsDeactivationGetPromotionInfoJson",
	    
	    data: {
	    	
	    	RequestID : $('#RequestID').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();	
	    	if (json.success == "true"){
	    		$('#Description').val(json.PromotionLabel);
	    		$('#Submit').removeAttr("disabled");
	    	}else{
	    		$('#Submit').attr("disabled", "disabled");
	    		alert(json.error); 
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function FormSubmit(){
	
	if($('#RequestID').val() == ""){
		document.getElementById("RequestID").focus();
		return false;
	}
	
	$('#Submit').attr("disabled", "disabled");
	
	$.mobile.showPageLoadingMsg();	
	
	$.ajax({
	    url: "inventory/ProductPromotionsDeactivationExecute",
	    
	    data: {
	    	
	    	RequestID : $('#RequestID').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();	
	    	if (json.success == "true"){
	    		alert("Promotion De-activated Successfully");
	    		$('#RequestID').val('');
	    		$('#Description').val('');
	    		document.getElementById("RequestID").focus();
	    		$('#Submit').removeAttr("disabled");
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}
