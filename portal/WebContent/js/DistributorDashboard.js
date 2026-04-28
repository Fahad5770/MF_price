function changeDistributorForDashboard(){
	$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupDistributorInit();
	});	
	$('#LookupDistributorSearch').popup("open");
	
}

function DistributorSearchCallBack(SAPCode, OutletName){
	window.location = "DistributorDashboard.jsp?DistributorCode="+SAPCode;
}

$( document ).delegate("#DistributorDashboard", "pageshow", function() {
	//alert($("#ActivePageIndex").val());
	jQuery.mobile.changePage($( '#'+$("#ActivePageIndex").val() ).attr("href"), {transition: "none"});
});