

$( document ).on( "pageinit", "#EmployeeDashboard", function() {
	
	setTimeout("$('#EmployeeDashboardSapCode').focus();", 100);
	
	$('#EmployeeDashboardSapCode').on('dblclick', function(e, data){
		
		$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupEmployeeInit();
		} );
		$('#LookupEmployeeSearch').popup("open");
				  
	});
    
});	


function getEmployeeName(){
	
	if( $('#EmployeeDashboardSapCode').val() == "" ){
		return false;
	}
	if( isInteger($('#EmployeeDashboardSapCode').val()) == false ){
		setTimeout("$('#EmployeeDashboardSapCode').focus();", 100);
		return false;
	}
	
	$.ajax({
		url: "employee/GetEmployeeBySAPCodeJSON",
		data: {
			SAPCode: $('#EmployeeDashboardSapCode').val()
		},
		type:"POST",
		dataType: "json",
		success: function(json){ 
			if( json.exists == 'true' ){
				$('#EmployeeDashboardName').val(json.EmployeeName);
				$('#EmployeeDashboardSubmitButton').removeClass('ui-disabled');
			}
		},
		error: function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

function EmployeeDashboardSubmit(){
	window.location = "EmployeeDashboard.jsp?EmployeeCode="+$('#EmployeeDashboardSapCode').val();
}

function setEmployeeLookup(SAPCode, Name){
	$('#EmployeeDashboardSapCode').val(SAPCode);
	$('#EmployeeDashboardName').val(Name);
	$('#EmployeeDashboardSubmitButton').removeClass("ui-disabled");
}