function changeOutletForDashboard(){
	$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
		lookupOutletInit();
	} );
	$('#LookupOutletSearch').popup("open");
}

function OutletSearchCallBack(SAPCode, OutletName){
	window.location = "OutletDashboard.jsp?outletID="+SAPCode;
}