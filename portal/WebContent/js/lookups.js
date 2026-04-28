
// search employee

var lookupEmployeeIsUpdated = false;


function lookupEmployeeInit(){
	var maxWidth = ($( window ).width() - 200) + "px";
	var maxHeight = ($( window ).height() - 250) + "px";
	var maxHeight2 = ($( window ).height() - 400) + "px";
	$("#LookupEmployeeSearch").css("width", maxWidth);
	$("#LookupEmployeeSearch").css("height", maxHeight);
	$("#LookupEmployeeSearchContent").css("height", maxHeight2);
	
	setTimeout(function(){$('#EmployeeDashboardSearchFormSAPCode').focus();},'1000');

}

function lookupEmployeeSetChangeFlag(){
	lookupEmployeeIsUpdated = true;
}

function lookupEmployeeShowSearchContent(){
	
	if(lookupEmployeeIsUpdated){
		
		$.mobile.showPageLoadingMsg();	
		
		$.get('LookupEmployeeSearchContent.jsp?1=1&'+$('#LookupEmployeeSearchForm').serialize(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#LookupEmployeeSearchContent").html(data);
			$("#LookupEmployeeSearchContent").trigger('create');
			lookupEmployeeIsUpdated = false;  
		});
		
	}
	return false;
}

function lookupEmployeeOnSelect(SAPCode, EmployeeName){
	$('#LookupEmployeeSearch').popup("close");
	var CallBack = $('#lookupEmployeeCallBack').val();
	eval(CallBack+"("+SAPCode+",'"+EmployeeName+"')");
}


// search outlet

var lookupOutletIsUpdated = false;
 
function lookupOutletInit(){
	var maxWidth = ($( window ).width() - 50) + "px";
	var maxHeight = ($( window ).height() - 250) + "px";
	var maxHeight2 = ($( window ).height() - 400) + "px";
	$("#LookupOutletSearch").css("width", maxWidth);
	$("#LookupOutletSearch").css("height", maxHeight);
	$("#LookupOutletSearchContent").css("height", maxHeight2);

	setTimeout(function(){$('#OutletSearchFormSAPCode').focus();},'1000');
}

function lookupOutletSetChangeFlag(){
	lookupOutletIsUpdated = true;
}

function lookupOutletShowSearchContent(){
	
	if(lookupOutletIsUpdated){
		
		$.mobile.showPageLoadingMsg();
		
		$.get('LookupOutletSearchContent.jsp?1=1&'+$('#LookupOutletSearchForm').serialize(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#LookupOutletSearchContent").html(data);
			$("#LookupOutletSearchContent").trigger('create');
			lookupOutletIsUpdated = false;
		});
		
	}
	return false;
}

function lookupOutletOnSelect(SAPCode, OutletName){
	if($('#OutletSearchMultiSelect').val() == "off"){
		$('#LookupOutletSearch').popup("close");
	}
	var CallBack = $('#lookupOutletCallBack').val();
	eval(CallBack+"("+SAPCode+",'"+OutletName+"')");
}


//search distributor

var lookupDistributorIsUpdated = false;
 
function lookupDistributorInit(){
	var maxWidth = ($( window ).width() - 50) + "px";
	var maxHeight = ($( window ).height() - 250) + "px";
	var maxHeight2 = ($( window ).height() - 400) + "px";
	$("#LookupDistributorSearch").css("width", maxWidth);
	$("#LookupDistributorSearch").css("height", maxHeight);
	$("#LookupDistributorSearchContent").css("height", maxHeight2);
	
	setTimeout(function(){$('#DistributorSearchFormSAPCode').focus();},'1000');

}

function lookupDistributorSetChangeFlag(){
	lookupDistributorIsUpdated = true;
}

function lookupDistributorShowSearchContent(){
	
	if(lookupDistributorIsUpdated){
		
		$.mobile.showPageLoadingMsg();
		
		$.get('LookupDistributorSearchContent.jsp?1=1&'+$('#LookupDistributorSearchForm').serialize(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#LookupDistributorSearchContent").html(data);
			$("#LookupDistributorSearchContent").trigger('create');
			lookupDistributorIsUpdated = false;
		});
		
	}
	return false;
}

function lookupDistributorOnSelect(SAPCode, DistributorName){
	
	$('#LookupDistributorSearch').popup("close");
	
	var CallBack = $('#lookupDistributorCallBack').val();
	eval(CallBack+"("+SAPCode+",'"+DistributorName+"')");
}



//search product

var lookupProductIsUpdated = false;
 
function lookupProductInit(){
	var maxWidth = ($( window ).width() - 50) + "px";
	var maxHeight = ($( window ).height() - 250) + "px";
	var maxHeight2 = ($( window ).height() - 400) + "px";
	$("#LookupProductSearch").css("width", maxWidth);
	$("#LookupProductSearch").css("height", maxHeight);
	$("#LookupProductSearchContent").css("height", maxHeight2);

}

function lookupProductSetChangeFlag(){
	lookupProductIsUpdated = true;
}

function lookupProductShowSearchContent(){
	
	if(lookupProductIsUpdated){
		
		$.mobile.showPageLoadingMsg();
		
		$.get('LookupProductSearchContent.jsp?1=1&'+$('#LookupProductSearchForm').serialize(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#LookupProductSearchContent").html(data);
			$("#LookupProductSearchContent").trigger('create');
			lookupProductIsUpdated = false;
		});
		
	}
	return false;
}

function lookupProductOnSelect(SAPCode, ProductName){
	
	$('#LookupProductSearch').popup("close");
	
	var CallBack = $('#lookupProductCallBack').val();
	eval(CallBack+"("+SAPCode+",'"+ProductName+"')");
}

//search region

var lookupRegionIsUpdated = true;
 
function lookupRegionInit(){
	//var maxWidth = ($( window ).width() - 700) + "px";
	//var maxHeight = ($( window ).height() - 250) + "px";
	//var maxHeight2 = ($( window ).height() - 300) + "px";
	$("#LookupRegionSearch").css("width", "400px");
	$("#LookupRegionSearch").css("height", "430px");
	
	$("#LookupRegionSearchContent").css("height", "370");
	lookupRegionShowSearchContent();

}

function lookupRegionSetChangeFlag(){
	lookupRegionIsUpdated = true;
}

function lookupRegionShowSearchContent(){
	
	if(lookupRegionIsUpdated){
		
		$.mobile.showPageLoadingMsg();
		
		$.get('LookupRegionSearchContent.jsp?', function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#LookupRegionSearchContent").html(data);
			$("#LookupRegionSearchContent").trigger('create');
			lookupRegionIsUpdated = true;
		});
		
	}
	return false;
}

function lookupRegionOnSelect(SAPCode, RegionName){
	
	$('#LookupRegionSearch').popup("close");
	
	var CallBack = $('#lookupRegionCallBack').val();
	eval(CallBack+"("+SAPCode+",'"+RegionName+"')");
}


// search user

var lookupUserIsUpdated = false;

function lookupUserSetChangeFlag(){
	//alert();
	lookupUserIsUpdated = true;
}

function lookupUserInit(){
	var maxWidth = ($( window ).width() - 50) + "px";
	var maxHeight = ($( window ).height() - 250) + "px";
	var maxHeight2 = ($( window ).height() - 400) + "px";
	$("#LookupUserSearch").css("width", maxWidth);
	$("#LookupUserSearch").css("height", maxHeight);
	$("#LookupUserSearchContent").css("height", maxHeight2);
	setTimeout(function(){$('#UserSearchFormUserID').focus();},'1000');
}

function lookupUserShowSearchContent(){
	
	if(lookupUserIsUpdated){
		
		$.mobile.showPageLoadingMsg();	
		
		$.get('LookupUserSearchContent.jsp?1=1&'+$('#LookupUserSearchForm').serialize(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#LookupUserSearchContent").html(data);
			$("#LookupUserSearchContent").trigger('create');
			lookupUserIsUpdated = false;  
		});
		
	}
	return false;
}

function lookupUserOnSelect(SAPCode, EmployeeName){
	$('#LookupUserSearch').popup("close");
	var CallBack = $('#lookupUserCallBack').val();
	eval(CallBack+"("+SAPCode+",'"+EmployeeName+"')");
}