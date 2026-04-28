
$( document ).delegate("#FDAComplaintFormPage", "pageinit", function() {
	$('#ComplaintType').val(ComplaintType).change();
	$('#ConcernedDirectorate').val(ConcernedDirectorate).change();
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
	
	if( $('#FirstName').val() == "" ){
		alert("Please enter First Name");
		$('#FirstName').focus();
		return false;
	}
	
	if( $('#ComplaintType').val() == "" ){
		alert("Please select Complaint Type");
		$('#ComplaintType').focus();
		return false;
	}
	
	if( $('#ContactNo').val() == "" ){
		alert("Please enter Contact No");
		$('#ContactNo').focus();
		return false;
	}
	
	$('#FDAComplaintFormExecute').submit();
	
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
