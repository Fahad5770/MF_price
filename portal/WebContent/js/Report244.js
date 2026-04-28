function is_resolved(id){
	var ID=id;
	
	$.mobile.showPageLoadingMsg();
	$("#resolved"+id).addClass("ui-disabled");
	$.ajax({
		
		url: "crm/CRMHelpDeskComplaintReportsExecute",
		data: {
			ID: ID
		},
		type:"POST",
		dataType:"json",
		 success: function(json) {
			 $.mobile.hidePageLoadingMsg();
		    if (json.success == "true"){
		    	alert("Complaint #"+json.ComplaintNO+" has been resolved");
		    }else{
		    		alert('Server could not be reached.');
		    }
		  }
		,
			
	});
	
}



 

		