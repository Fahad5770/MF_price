
$( document ).delegate("#FDAChangeComplaintStatusPage", "pageinit", function() {
	
});


function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function Submit(){
	if( $('#CaseNo').val() == "" ){
		alert("Please enter Case No");
		$('#CaseNo').focus();
		return false;
	}
	
	if( $('#StatusID').val() == "" ){
		alert("Please select Status");
		$('#StatusID').focus();
		return false;
	}
	
	$('#FDAChangeComplaintStatusExecute').submit();
	
	return false;
	
}

function showSearchContent(){
	
	$("#SearchContent").focus();
	
	//document.getElementById("FromDate").disabled = "disabled";
	//document.getElementById("ToDate").disabled = "disabled";
	
	$.get('FDAComplaintSearch.jsp?FromDate='+$('#FromDate').val()+'&ToDate='+$('#ToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function showCaseInfo(){
	
	$.ajax({
	    url: "FDAGetCaseInfoJson.jsp",
	    data: {
	    	CaseNo: $('#CaseNo').val()
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if(json.success == "true"){
	    		$('#StatusID').val(json.StatusID).change();
	    		
	    		$('#FirstName').val(json.FirstName);
	    		$('#LastName').val(json.LastName);
	    		$('#Type').val(json.TypeLabel);
	    		$('#DateofEntry').val(json.DateOfEntry);
	    		
	    	}else{
	    		$('#StatusID').val('').change();
	    		
	    		$('#FirstName').val('');
	    		$('#LastName').val('');
	    		$('#Type').val('');
	    		$('#DateofEntry').val('');
	    		
	    		alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
}
