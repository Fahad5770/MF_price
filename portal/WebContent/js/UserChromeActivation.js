
$( document ).delegate("#UserChromeActivation", "pageshow", function() {
	
});

function showSearchContent(){
	//alert();
	if( $('#SapCode').val() != '' ){
		$.mobile.showPageLoadingMsg();
		$.get('UserChromeActivationLoadData.jsp?SapCode='+$('#SapCode').val(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#SearchContent").html(data);
			
		});
	}
	document.getElementById("SapCode").focus();
	return false;

}

function SubmitProcess(PID,BID){
	
	$.ajax({
	    url: "employee/UserActivateChromeExecute",
	    
	    data: {
	    	
	    	ProcessID : PID,
	    	BrowserID : BID
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		window.location='UserChromeActivation.jsp';
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

