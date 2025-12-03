
var EmployeeIDFieldID = 0;
var EmployeeNameFieldID = 0;

function getDistributorsListByRegion(){
	
	$.mobile.loading( 'show');
	
	$.get('ManageDistributorsListDistributors.jsp?region_id='+$('#region').val(), function(data) {   			
		  $("#DistributorsDIV").html(data);		 
		  //$('#DistributorsDIV').trigger("create");
		  $.mobile.loading( 'hide');
	})
	.fail(function() {			
		alert("Server could not be reached.");
		$.mobile.loading( 'hide');
	  }) ;
}

function getEmployeeName(EmployeeIDVal, ContainerID){
	
	$.mobile.loading( 'show');
	
	$.ajax({
	    url: "common/GetUserInfoJson",
	    data: {
	    	UserID: EmployeeIDVal
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.exists == "true"){
	    		
	    		$('#'+ContainerID).val(json.DistributorName);
	    		
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
}

function ManageDistributorSubmit(){
	$.mobile.loading( 'show');
	
	$.ajax({
	    url: "distributor/ManageDistributorExecute",
	    data: $('#ManageDistributorsForm').serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		window.location = 'ManageDistributors.jsp';
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading( 'hide');
	    	alert("Server could not be reached.");
	    }
	});
	
}

function OpenEmployeeSearchPopup(EmployeeIDFieldIDParam, EmployeeNameFieldIDParam){
	
	EmployeeIDFieldID = EmployeeIDFieldIDParam;
	EmployeeNameFieldID = EmployeeNameFieldIDParam;
	
	$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupEmployeeInit();
	} );
	$('#LookupEmployeeSearch').popup("open");
	
}

function EmployeeSearchCallBackManageDistributor(SAPCode, EmployeeName){
	$('#'+EmployeeIDFieldID).val(SAPCode);
	$('#'+EmployeeNameFieldID).val(EmployeeName);
}
