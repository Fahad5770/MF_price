
function GetClientInfo()
{

	
	
	if($("#CustomerID").val() !=""){
		$("#DispatchReturnsSave").removeClass('ui-disabled')
		$.mobile.loading( 'show');
		$.ajax({
		    url: "cash/GLCreditLimitInfoJson",
		    data: $("#ReturnMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if(json.success=="true")	    		
	    		{
		    		
					
		    		$("#ValidTo").val(json.valid_to);
		    		$("#ValidFrom").val(json.valid_from);
		    		$("#CreditLimit").val(json.credit_limit);
		    		$("#CustomerName").val(json.customer_name);
		    		$.mobile.loading( 'hide');
		    		
	    		}
		    	else
	    		{
		    		
		    		alert(json.error);
		    		//alert("Server could not be reached.");
		    		  $.mobile.loading( 'hide');
	    		}
		    	
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	//alert(json.error);
		    	  $.mobile.loading( 'hide');
		    }
		});
	}else{
		$("#DispatchReturnsSave").addClass('ui-disabled')
		//document.getElementById('DispatchReturnsVehicleNumber').focus();
		$("#ValidTo").val("");
		$("#ValidFrom").val("");
		$("#CreditLimit").val("");
		$("#CustomerName").val("");
		$.mobile.loading( 'hide');
	}
	

}
function ProductionReceiptSubmit()
{
	
	
	if($("#CustomerID").val() !="" && $("#Deactivation_Remarks").val() !=""){
		
		$.mobile.loading( 'show');
		$.ajax({
		    url: "cash/GLCreditLimitDeactiveExecute",
		    data: $("#ReturnMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if(json.success=="true")	    		
	    		{
		    		window.location.href="F397DeactivateCreditLimit.jsp"
		    		$.mobile.loading( 'hide');
	    		}
		    	else
	    		{
		    		
		    		alert(json.error);
		    		//alert("Server could not be reached.");
		    		  $.mobile.loading( 'hide');
	    		}
		    	
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	//alert(json.error);
		    	  $.mobile.loading( 'hide');
		    }
		});
	}else{
		//document.getElementById('DispatchReturnsVehicleNumber').focus();
		alert("Please select all fields.");
	}
	
}

 
