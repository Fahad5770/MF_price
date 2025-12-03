
	  $( document ).on( "pageinit", "#InventryMain", function() {  
	    	$("#DistributorID").parent().css("width","100%");
	    	$("#DistributorName").parent().css("width","74%");
	    	$("#InventoryID").children().css("width","100%");
	    });
	
	function getInventoryInfoJson(){
    	/*alert($('#InventoryID').val());
    	if(isInteger($('#InventoryID').val()) == false || $('#InventoryID').val() == "" ){
    		$('#InventoryID').val('');
    		return false;
    	}*/
    	
    	$.ajax({
    		
    		url: "inventory/GetInventryInfoJSON",
    		data: {
    			InventryID: $('#InventoryID').val()
    		},
    		type:"POST",
    		dataType:"json",
    		success:function(json){
    			if(json.exists == "true"){
    				$("#DistributorID").val(json.DistributorId);
    				$("#DistributorName").val(json.DistributorName);
    				$("#IsAdjusted").val(json.Adjusted);
    				$("#IsBlocked").val(json.Blocked);
    				$("#DispatchCreatedOn").val(json.Created_on);
    			}else if(json.exists == "false"){
    				alert("Inventry doesn't exist");
    			}
    		},
    		error:function(xhr, status){
    			alert("Server could not be reached.");
    		}
    		
    	});
    	
    }
	
	function FormSubmit(){
	$.ajax({
    		
    		url: "inventory/SalesDispatchExecute",
    		data: {
    			InventryID: $('#InventoryID').val()
    		},
    		type:"POST",
    		dataType:"json",
    		success:function(json){
    			if(json.success == "true"){
    				alert("Dipatch open Succeccfully");
    				window.location="InventorySalesDispatchUnlock.jsp";    
    			}else if(json.success == "false"){
    				alert("Inventry doesn't exist");
    			}
    		},
    		error:function(xhr, status){
    			alert("Server could not be reached.");
    		}
    		
    	});
	}
