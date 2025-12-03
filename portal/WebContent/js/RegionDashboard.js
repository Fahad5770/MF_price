function changeRegionForDashboard(){
	$( "#LookupRegionSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupRegionInit();
	} );
	$('#LookupRegionSearch').popup("open");
}

function RegionSearchCallBack(SAPCode, RegionName){
	window.location = "RegionDashboard.jsp?RegionCode="+SAPCode;
}