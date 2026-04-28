

var RowCount = 0;

$( document ).delegate("#MonthlyDiscountMain", "pageinit", function() {
	
	if(isEditCase){
		getEditInfoJson(EditID);
	}
	
	$("#MonthlyDiscountApproveButton").on( "click", function( event, ui ) {
		$("#SamplingDashboardStatus").val("1");
		SamplingDashboardSubmit();
	});
	$("#MonthlyDiscountOnHoldButton").on( "click", function( event, ui ) {
		$("#SamplingDashboardStatus").val("2");
		SamplingDashboardSubmit();
	});
	$("#MonthlyDiscountCancelledButton").on( "click", function( event, ui ) {
		$("#SamplingDashboardStatus").val("3");
		SamplingDashboardSubmit();
	});
	
});

$( document ).delegate("#MonthlyDiscountMain", "pageshow", function() {
	
	
	
	
});


function getEditInfoJson(EditIDVal){
	
	//alert("case 2");
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountListEdit.jsp?EditID='+EditIDVal, function(data) {   			
		
		
		$("#MonthlyDiscountContent").html(data);
		//$("#MonthlyDiscountContent").trigger('create');
		  
		$.mobile.loading( 'hide');
		
		  
		  $('#MonthlyDiscountApproveButton').removeClass("ui-disabled");
	
		  //$('#MonthlyDiscountOnHoldButton').removeClass("ui-disabled");
		  //$('#MonthlyDiscountCancelledButton').removeClass("ui-disabled");
	  
	})
	.fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
	
}


function SamplingDashboardSubmit(){
	
	var SamplingActionLength = $('select[name=SamplingAction]').length;
	var SelectionFlag = 0;
	for(var i = 0; i < SamplingActionLength; i++){
		if( $('select[name=SamplingAction]')[i].value == "1" ){
			SelectionFlag++;
		}
	}
	
	if(SelectionFlag == 0){
		alert("Please select atleast one Outlet");
		return false;
	}
	
	$("#SamplingDashboardApprovalForm").submit();
	
}

function SelectAllCheckboxes(CheckID){
	//if ($('input[name=CheckBoxSelectAll]').prop("checked") == true){
		$('input[name='+CheckID+']').prop( "checked", true ).checkboxradio( "refresh" );
	//}else{
		//$('input[name='+CheckID+']').prop( "checked", false ).checkboxradio( "refresh" );
	//}
}

function UnselectAllCheckboxes(CheckID){
	//if ($('input[name=CheckBoxSelectAll]').prop("checked") == true){
		//$('input[name='+CheckID+']').prop( "checked", true ).checkboxradio( "refresh" );
	//}else{
		$('input[name='+CheckID+']').prop( "checked", false ).checkboxradio( "refresh" );
	//}
}

function ChangeLabel(ID,CheckBoxID)
{
	$("#"+ID).html("Selected Packages");
	$("#"+ID).css({'font-weight':'bold'});
	
	
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
  	   
	    if($('input[name="PackagesCheckBox"]:checked').length > 0)
    	{
	    	//$("#"+ID).addClass('ui-bar-b');
	    	//$("#"+ID).removeClass('ui-btn-up-d');
	    	//alert("Length is greater than 0");
	    	 
    	}	
	    else
    	{
	    	//$("#"+ID).addClass('ui-btn-up-d');	
	    	//$("#"+ID).removeClass('ui-bar-b');
	    	//alert("Length is less than 0");
	    	$("#"+ID).html("All Packages");
	    	 
    	}
		
	   
	});
	
}

function GetTodayDate()
{
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} today = dd+'/'+mm+'/'+yyyy;
	$("#StartDate").val(today);
	$("#EndDate").val(today);
	
	$("#SelectedDateType").val("Today");
	
	AddPackagesIntoSession();
	LoadDateRange('LoadDateRangeHyperlink');
}

function GetThisWeekDate(date)
{	
	
	var lastsunday;
	
	var today = new Date();
    var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - (today.getDay()-1));       
    var lastWeekMonth = lastWeek.getMonth() + 1;
    var lastWeekDay = lastWeek.getDate();
    var lastWeekYear = lastWeek.getFullYear();
    var lastWeekDisplay =  lastWeekDay+ "/" + lastWeekMonth + "/" + lastWeekYear;
	 
   // alert(today.getDay() );
    
    // Get the this Monday
	  var monday = new Date();
	 // monday.setDate(monday.getDate() - (monday.getDay()));
	  var dd = monday.getDate();
	  var mm = monday.getMonth()+1; //January is 0!
	  var yyyy = monday.getFullYear();
	  if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} today = dd+'/'+mm+'/'+yyyy;
	  
	  $("#StartDate").val(lastWeekDisplay);
	  $("#EndDate").val(today);

		
	  $("#SelectedDateType").val("This Week");
		AddPackagesIntoSession();	  
		LoadDateRange('LoadDateRangeHyperlink');
	 // alert(lastsunday);
	  // Return array of date objects
	  //alert(sunday+" - "+monday);
}

function GetThisMonthDate()
{
	var date = new Date();
	var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
	var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
	var firstDayFormat; var lastDayFormat;
	
	if(lastDay>date) //if last month date is greater than today's date then today date will be used
	{
		lastDay=date;
	}
	
	 var dd = firstDay.getDate();
	 var mm = firstDay.getMonth()+1; //January is 0!

	 var yyyy = firstDay.getFullYear();
	 if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} firstDayFormat = dd+'/'+mm+'/'+yyyy;
	 
	 var dd1 = lastDay.getDate();
	 var mm1 = lastDay.getMonth()+1; //January is 0!

	 var yyyy1 = lastDay.getFullYear();
	 if(dd1<10){dd1='0'+dd1;} if(mm1<10){mm1='0'+mm1;} lastDayFormat = dd1+'/'+mm1+'/'+yyyy1;
	 
	 $("#StartDate").val(firstDayFormat);
	 $("#EndDate").val(lastDayFormat);
	 
	  $("#SelectedDateType").val("This Month");
		AddPackagesIntoSession();	  
		LoadDateRange('LoadDateRangeHyperlink');
	 
	 //alert();
	 //alert(lastDayFormat);
}

function GetYesterdayDate()
{
	today = new Date();
	yesterday = new Date(today);
	yesterday.setDate(today.getDate() - 1); //setDate also supports negative values, which cause the month to rollover.
	
	var dd = yesterday.getDate();
	var mm = yesterday.getMonth()+1; //January is 0!

	var yyyy = yesterday.getFullYear();
	if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} yesterday = dd+'/'+mm+'/'+yyyy;
	
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} today = dd+'/'+mm+'/'+yyyy;
	$("#StartDate").val(yesterday);
	$("#EndDate").val(yesterday);

	
	  $("#SelectedDateType").val("Yesterday");
		AddPackagesIntoSession();	  
		LoadDateRange('LoadDateRangeHyperlink');
	//alert($yesterday);
}

function GetLastWeekDate()
{
	var today = new Date();
    var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - today.getDay());       
    var lastWeekMonth = lastWeek.getMonth() + 1;
    var lastWeekDay = lastWeek.getDate();
    var lastWeekYear = lastWeek.getFullYear();

    var lastWeekDisplayEnd = lastWeekDay + "/" + lastWeekMonth + "/" + lastWeekYear;
    
    var today1 = new Date();
    var lastWeek1 = new Date(today1.getFullYear(), today1.getMonth(), today1.getDate() - ((today1.getDay()-1)+7));       
    var lastWeekMonth1 = lastWeek1.getMonth() + 1;
    var lastWeekDay1 = lastWeek1.getDate();
    var lastWeekYear1 = lastWeek1.getFullYear();

    var lastWeekDisplayStart = lastWeekDay1 + "/" + lastWeekMonth1 + "/" + lastWeekYear1;
   // alert(lastWeekDisplayStart);
    $("#StartDate").val(lastWeekDisplayStart);
	$("#EndDate").val(lastWeekDisplayEnd);    
	
	  $("#SelectedDateType").val("Last Week");
		AddPackagesIntoSession();	  
		LoadDateRange('LoadDateRangeHyperlink');	
    
}

function GetLastMonthDate()
{
	var date = new Date();
	var firstDay = new Date(date.getFullYear(), date.getMonth()-1, 1);
	var lastDay = new Date(date.getFullYear(), date.getMonth(), 0);
	var firstDayFormat; var lastDayFormat;
	
	 var dd = firstDay.getDate();
	 var mm = firstDay.getMonth()+1; //January is 0!

	 var yyyy = firstDay.getFullYear();
	 if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} firstDayFormat = dd+'/'+mm+'/'+yyyy;
	 
	 var dd1 = lastDay.getDate();
	 var mm1 = lastDay.getMonth()+1; //January is 0!

	 var yyyy1 = lastDay.getFullYear();
	 if(dd1<10){dd1='0'+dd1;} if(mm1<10){mm1='0'+mm1;} lastDayFormat = dd1+'/'+mm1+'/'+yyyy1;
	 
	// alert(lastDayFormat);
	 
	 $("#StartDate").val(firstDayFormat);
	 $("#EndDate").val(lastDayFormat);

	 
	 
	  $("#SelectedDateType").val("Last Month");
		AddPackagesIntoSession();	  
		LoadDateRange('LoadDateRangeHyperlink');	 
	 //alert(lastDayFormat);
}

function SaleSummaryLoadReport(){
	var ReportURL = $("#SalesSummaryMain #ReportURL").val();
	$.mobile.loading( 'show');
	$("#ReportSaleSummaryTD").html("<img src='images/snake-loader.gif'>");
	$.get(ReportURL, function(data) {   			
		$("#ReportSaleSummaryTD").html(data);
		  $("#ReportSaleSummaryTD").trigger('create');
		  $.mobile.loading( 'hide');
	})
	.fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
}

function DistributorSearchCallBackAtDistributorReports(SAPCode, Name){
	$('#LookupDistributorSearch').popup("close");
	 
	 $("#DashboardDistributorDistributorCode").val(SAPCode);
	 document.getElementById("DashboardDistributorFormID").submit();
}

function EmployeeSearchCallBackAtOrderBookerDashboard(SAPCode, Name){
	$('#LookupEmployeeSearch').popup("close");
	 $("#DashboardOrderBookerEmployeeCode").val(SAPCode);
	 document.getElementById("DashboardOrderBookerFormID").submit();
}

function LoadDistributors(){
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountMainDistributors.jsp', function(data) {   			
			  
			  $("#FilterBy").html(data);
			  $("#FilterBy").trigger('create');
			  $.mobile.loading( 'hide');
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	
}

function LoadRegions(){
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountMainRegions.jsp', function(data) {   			
			  
			  $("#FilterBy").html(data);
			  $("#FilterBy").trigger('create');
			  $.mobile.loading( 'hide');
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	
}


function LoadOutlets(){
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountMainOutlets.jsp', function(data) {   			
			  
			  $("#FilterBy").html(data);
			  $("#FilterBy").trigger('create');
			  $.mobile.loading( 'hide');
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	
}

function LoadPeriod(){
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountMainPeriod.jsp', function(data) {   			
			  
			  $("#FilterBy").html(data);
			  $("#FilterBy").trigger('create');
			  $.mobile.loading( 'hide');
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	
}

function LoadStatus(){
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountMainStatus.jsp', function(data) {
			  
			  $("#FilterBy").html(data);
			  $("#FilterBy").trigger('create');
			  $.mobile.loading( 'hide');
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}


function AddIntoSession(){
	
	$.mobile.loading('show');	
	
	$.ajax({
	    url: "sampling/MonthlyDiscountMainExecute",
	    data: $("#MonthlyDiscountMainForm" ).serialize(),
	    type: "POST",
	    dataType : "html",
	    success: function( html ) {
			
	    	LoadFilters();
	    	
	    	$.mobile.loading('hide');	
	    	
	    	//LoadMonthlyDiscountMainContent();
	    	
	    	//alert(html);
	    	//$('#ProductStatistics').html(html);
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function LoadMonthlyDiscountMainContent(){
	
	$.mobile.loading( 'show');	
	$.get('MonthlyDiscountList.jsp', function(data) {   			
			  
			  $("#MonthlyDiscountContent").html(data);
			  //$("#MonthlyDiscountContent").trigger('create');
			  $.mobile.loading( 'hide');
			  
			  $('#MonthlyDiscountApproveButton').removeClass("ui-disabled");
			  //$('#MonthlyDiscountOnHoldButton').removeClass("ui-disabled");
			  //$('#MonthlyDiscountCancelledButton').removeClass("ui-disabled");
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	
}
function SamplingDashboardPopup2(OutletID, month, year){
	
	$.mobile.showPageLoadingMsg();	
	$.get('SamplingDashboardDetail.jsp?OutletID='+OutletID+'&month='+month+'&year='+year, function(data) {
		  $("#SamplingDetailContent").html(data);
		  $("#SamplingDetailContent").trigger('create');
		  $("#SamplingDashboard #SamplingDetailPopup").css('overflow-y', 'scroll');
		  $.mobile.hidePageLoadingMsg();
		  
		  $("#SamplingDetailPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
		  
	});
	
}

function LoadFilters(){
	
	$.mobile.showPageLoadingMsg();
	$.get('MonthlyDiscountMainFilters.jsp', function(data) {
		
		  $("#MainFiltersDiv").html(data);
		  $("#MainFiltersDiv").trigger('create');
		  
		  $.mobile.hidePageLoadingMsg();
		  
	});
	
}