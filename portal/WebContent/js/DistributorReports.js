

var RowCount = 0;

$( document ).delegate("#SalesSummaryMain", "pageshow", function() {
	$('#ChangeDistributor').on('click', function(e, data){
		
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open", {transition:"pop"});
				  
	});	
	
	$('#ChangeOrderBooker').on('click', function(e, data){
		
		$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupEmployeeInit();
		} );
		$('#LookupEmployeeSearch').popup("open", {transition:"pop"});
				  
	});	
	
});

$( document ).delegate("#SalesSummaryMain", "pageshow", function() {
	
	$.mobile.loading( 'show');	
	$.get('DistributorReportsFilter.jsp?ReportID='+$("#ReportID").val(), function(data) {   			
			  $("#LoadSalesSummaryFilterBy").html(data);
			  $("#LoadSalesSummaryFilterBy").trigger('create');
			  $.mobile.loading( 'hide');
			  if($("#IsOrderBookerSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllOrderBookersHyperlink").html("Selected Order Bookers");
			  }
			  if($("#IsDistributorSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllDistributorsHyperlink").html("Selected Distributors");
			  }
			  if($("#IsBrandSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllBrandsHyperlink").html("Selected Brands");
			  }
			  if($("#IsPackageSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllPackagesHyperlink").html("Selected Packages");
			  }
			  if($("#IsDateRangeSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadDateRangeHyperlink").html("Selected Date Range");
			  }
			  
			  
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	var DefaultFilter = $("#DefaultFilter").val();
	eval(DefaultFilter);
	
	SaleSummaryLoadReport();
	
	
});

function LoadAllPackages(ID)
{
	$("#FilterType").val("1"); //1 for Package
	$.mobile.loading( 'show');	
	$.get('DistributorReportsPackages.jsp?ClickID='+ID+"&Title="+$("#"+ID).html(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllBrands(ID)
{
	$("#FilterType").val("2"); //2 for Brand
	$.mobile.loading( 'show');	
	$.get('DistributorReportsBrands.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllDistributors(ID)
{
	$("#FilterType").val("3"); //3 for Distributors
	$.mobile.loading( 'show');	
	$.get('DistributorReportsDistributors.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllOrderBookers(ID)
{
	$("#FilterType").val("4"); //4 for Order Bookers
	$.mobile.loading( 'show');	
	$.get('DistributorReportsOrderBookers.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllVehicles(ID)
{
	$("#FilterType").val("6"); //6 for Vehicles
	$.mobile.loading( 'show');	
	$.get('DistributorReportsVehicles.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}
function LoadAllEmployees(ID)
{
	$("#FilterType").val("7"); //7 for Employees
	$.mobile.loading( 'show');	
	$.get('DistributorReportsEmployees.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}
function LoadAllOutlets(ID)
{
	$("#FilterType").val("8"); //7 for Employees
	$.mobile.loading( 'show');	
	$.get('DistributorReportsOutlets.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllPJPs(ID)
{
	//alert(ID);
	$("#FilterType").val("9"); //9 for PJP
	$.mobile.loading( 'show');	
	$.get('DistributorReportsPJP.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllHODs(ID)
{
	//alert(ID);
	$("#FilterType").val("10"); //10 for HOD
	$.mobile.loading( 'show');	
	$.get('DistributorReportsHOD.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}



function LoadDateRange(ID)
{
	//alert(ID);
	$("#FilterType").val("5"); //5 for Date Range
	$.mobile.loading( 'show');	
	$.get('DistributorReportsDateRange.jsp?ClickID='+ID, function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
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

function ChangeLabelBrands(ID,CheckBoxID)
{
	$("#"+ID).html("Selected Brands");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="BrandsCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All Brands");
	    	 
    	}
		
	   
	});
}

function ChangeLabelDistributor(ID)
{
	$("#"+ID).html("Selected Distributors");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="DistributorCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All Distributors");
	    	 
    	}
		
	   
	});
}

function ChangeLabelOrderBooker(ID)
{
	$("#"+ID).html("Selected Order Bookers");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="OrderBookerCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All Order Bookers");
	    	 
    	}
		
	   
	});
}


function AddPackagesIntoSession()
{
	$.mobile.loading( 'show');	
	$.ajax({
    		url: "reports/DistributorReportsExecute",		    
		    data: $("#SalesSummaryMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
    		success:function(json){
    			$.mobile.loading( 'hide');
    			if(json.success == "true"){
    				SaleSummaryLoadReport();
    				$.mobile.loading( 'show');	
    				$.get('DistributorReportsFilter.jsp?ReportID='+$("#ReportID").val(), function(data) {   			
    						  $("#LoadSalesSummaryFilterBy").html(data);
    						  $("#LoadSalesSummaryFilterBy").trigger('create');
    						  $.mobile.loading( 'hide');
    						  if($("#IsOrderBookerSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllOrderBookersHyperlink").html("Selected Order Bookers");
    						  }
    						  if($("#IsDistributorSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllDistributorsHyperlink").html("Selected Distributors");
    						  }
    						  if($("#IsBrandSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllBrandsHyperlink").html("Selected Brands");
    						  }
    						  if($("#IsPackageSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllPackagesHyperlink").html("Selected Packages");
    						  }
    						  if($("#IsDateRangeSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadDateRangeHyperlink").html("Selected Date Range");
    						  }
    					})
    					.fail(function() {
    						  $.mobile.loading( 'hide');
    						  alert("Server could not be reached");
    					  }) ;
    			}else{
    				
    			}
    		},
    		error:function(xhr, status){
    			alert("Server could not be reached.");
    			$.mobile.loading( 'hide');
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
