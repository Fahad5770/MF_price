

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
	$.get('ReportCenterFilter.jsp?ReportID='+$("#ReportID").val()+'&UniqueSessionID='+$("#UniqueSessionID").val(), function(data) {   			
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
			   if($("#IsFromDistributorSelectedSession").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllFromDistributorsHyperlink").html("Selected From Distributors");
			  }
			   if($("#IsToDistributorSelectedSession").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllToDistributorsHyperlink").html("Selected To Distributors");
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
			  if($("#IsDistributorOrderStatusSessionSet").val()=="1") //DistributorID is selected
			  {
				$("#LoadAllDistributorOrderStatusHyperlink").html("Selected Distributor Order Status");
			  }
			  if($("#IsUserEmployeeSessionSet").val()=="1") // Departments is selected
			  {
					
					 $("#LoadAllUserEmployeesHyperlink").html("Selected Users");
			  }	
			  if($("#IsUserEmployeeWithTextSessionSet").val()=="1") // Departments is selected
			  {
					
					 $("#LoadAllUserEmployeesWithTextHyperlink").html("Selected Filter by Id");
			  }
			  if($("#IsChannelSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllChannelsHyperlink").html("Selected Channels");
			  }
			   if($("#IsCitySessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllCityHyperlink").html("Selected Channels");
			  }
			   if($("#IsSKUSessionSet").val()=="1") //mean order booker is selected
			  {
				$("#LoadAllSKUHyperlink").html("Selected SKUs");
			  }
			  
			  
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
	
	var DefaultFilter = $("#DefaultFilter").val();
	eval(DefaultFilter);
	
	//SaleSummaryLoadReport();
	
	
});

function LoadAllPackages(ID)
{
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("1"); //1 for Package
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterPackages.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllPackagesEmpty(ID)
{
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("1"); //1 for Package
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterPackagesEmpty.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$.get('ReportCenterFilterBrands.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}
function LoadAllBrandsEmpty(ID)
{
	$("#FilterType").val("2"); //2 for Brand
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterBrandsEmpty.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("3"); //3 for Distributors
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDistributors.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}
function LoadAllFromDistributors(ID)
{
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("54"); //3 for Distributors
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterFromDistributors.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllToDistributors(ID)
{
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("55"); //3 for Distributors
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterToDistributors.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}



function LoadAllPalletizes(ID)
{
	//alert(ID);
	$("#FilterType").val("45"); //45 for Palletize Type	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterPallatize.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllHauContractors(ID){
	//alert(ID);
	$("#FilterType").val("46"); //45 for Palletize Type	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterHaulageContractors.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$.get('ReportCenterFilterOrderBookers.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$.get('ReportCenterFilterVehicles.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$.get('ReportCenterFilterEmployees.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$.get('ReportCenterFilterOutlets.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllOutletsManual(ID)
{
	
	$("#FilterType").val("13"); //13 for Outlet 1 => Manual Entry of Outlet
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterOutletsManual.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) { 
		
				$("#LoadAllSearchResultsDIV").html(data);
				$('#OutletIDManual').on('dblclick', function(e, data){		
					//alert();
					$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
						lookupOutletInit();
					});
					$('#LookupOutletSearch').popup("open");
				});
				
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllAssetNumber(ID)
{
	
	$("#FilterType").val("34"); //34 for Asset Number
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterAssetNumber.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) { 
		
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
	//$( "#t12Colps" ).collapsible({ collapsed: true });
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("9"); //9 for PJP
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterPJP.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterHOD.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllCensusUser(ID)
{
	//alert(ID);
	$("#FilterType").val("44"); //44 for CensusUser	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterCensusUser.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllEmptyLossType(ID)
{
	//alert(ID);
	$("#FilterType").val("39"); //39 for EmptyLossType	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterEmptyLossType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllEmptyReceiptType(ID)
{
	//alert(ID);
	$("#FilterType").val("40"); //40 for Empty Receipt Type	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterEmptyReceiptType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}


function LoadAllGTMCategorys(ID)
{
	//alert(ID);
	$("#FilterType").val("38"); //38 for GTM Category	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterGTMCategory.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllEmptyReasons(ID)
{
	//alert(ID);
	$("#FilterType").val("36"); //10 for HOD	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterEmptyReason.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllMovementTypes(ID)
{
	//alert(ID);
	$("#FilterType").val("37"); //37 for MovementType	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterMovementType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}


function LoadAllSamplingCreditSlipTypes(ID)
{
	//alert(ID);
	$("#FilterType").val("35"); //35 for Sampling Credit Slip Types	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterSamplingCreditSlipTypes.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllPrimaryInvoiceStatus(ID)
{
	//alert(ID);
	$("#FilterType").val("30"); //30 for Primary Invoice Status	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterPrimaryInvoiceStatus.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllGlEmployees(ID)
{
	//alert(ID);
	$("#FilterType").val("29"); //29 for Gl Employee	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterGlEmployee.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}



function LoadAllRSM(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("11"); //10 for RSM
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterRSM.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}


function LoadAllLiftingType(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("43"); //43 for Lifting Type
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterLiftingType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllSM(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("20"); //20 for SM
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterSM.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllTDM(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("21"); //21 for TDM
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterTDM.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllASM(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("22"); //22 for ASM
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterASM.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllOutletType(ID)
{
	
	$("#FilterType").val("16"); //16 for Outlet Type
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterOutletType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllDiscountType(ID)
{
	
	$("#FilterType").val("32"); //32 for Discount Type
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDiscountType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllWarehouse(ID)
{
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("12"); //12 for Warehouse
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterWarehouse.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllRegion(ID)
{
	//alert(ID);
	$("#FilterType").val("25"); //25 for Region	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterRegion.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadCashInstruments(ID)
{
	//alert(ID);
	$("#FilterType").val("28"); //28 for Transaction Account	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterCashInstruments.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadCashInstrumentsMultiple(ID)
{
	//alert(ID);
	$("#FilterType").val("31"); //31 for Cash Instruments Multiple	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterCashInstrumentsMultiple.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllAccountType(ID)
{
	//alert(ID);
	$("#FilterType").val("26"); //26 for Account Type	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterAccountType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("5"); //5 for Date Range
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDateRange.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadDate(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("5"); //5 for Date Range
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDate.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadDateRangeNew(ID)
{
	//alert(ID);
	//$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("14"); //14 for Date Range New
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDateRangeNew.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadDaysRange(ID)
{
	//alert(ID);
	//$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("15"); //15 for Days
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDaysRange.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadComplaintID(ID)
{
	//alert(ID);
	//$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("17"); //15 for Complaint ID
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterComplaintID.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadCustomerID(ID)
{
	//alert(ID);
	//$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("27"); //27 for Customer ID
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterCustomerID.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadDistributorID(ID)
{
	
	$("#FilterType").val("33"); //33 for Distributor ID
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDistributorID.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadPlantID(ID)
{
	
	$("#FilterType").val("42"); //42 for Plant ID
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterPlantID.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadComplaintType(ID)
{
	//alert(ID);
	//$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("18"); //15 for Complaint Type
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterComplaintType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadComplaintStatus(ID)
{
	
	$("#FilterType").val("19"); //19 for Complaint Status
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterComplaintStatus.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllSalesType(ID)
{
	//alert(ID);
	$("#FilterType").val("23"); //23 for Sales Type	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterSalesType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
function ChangeFromLabelDistributor(ID)
{
	$("#"+ID).html("Selected Distributors");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="FromDistributorCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All From Distributors");
	    	 
    	}
		
	   
	});
}
function ChangeToLabelDistributor(ID)
{
	$("#"+ID).html("Selected Distributors");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="ToDistributorCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All TO Distributors");
	    	 
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

function LoadAllUserEmployees(ID)
{
	$("#FilterType").val("49"); //7 for Employees
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterUserEmployee.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}


function LoadRequestID(ID)
{
 //alert(ID);
 //$( "#t12Colps" ).trigger( "collapse" );
 $("#FilterType").val("49"); //49 for Request ID
 $.mobile.loading( 'show'); 
 $.get('ReportCenterFilterRequestID.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {      
     $("#LoadAllSearchResultsDIV").html(data);
     $("#LoadAllSearchResultsDIV").trigger('create');
     $.mobile.loading( 'hide');
  })
  .fail(function() {
     $.mobile.loading( 'hide');
     alert("Server could not be reached");
    }) ;
}

function LoadAllUserEmployeesWithText(ID)
{
	$("#FilterType").val("50"); //7 for Employees
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterUserEmployeeWithText.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function AddPackagesIntoSession()
{
	$.mobile.loading( 'show');	
	$.ajax({
    		url: "reports/ReportCenterExecute",		    
		    data: $("#SalesSummaryMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
    		success:function(json){
    			$.mobile.loading( 'hide');
    			if(json.success == "true"){
    				//SaleSummaryLoadReport();
    				$.mobile.loading( 'show');	
    				$.get('ReportCenterFilter.jsp?ReportID='+$("#ReportID").val()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
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
    						    if($("#IsFromDistributorSelectedSession").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllFromDistributorsHyperlink").html("Selected From Distributors");
    						  }
    						    if($("#IsToDistributorSelectedSession").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllToDistributorsHyperlink").html("Selected To Distributors");
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
    							if($("#IsSessionSet").val()=="1") //Palletize Type  is selected
      						  {
      							$("#LoadAllPalletizeHyperlink").html("Selected Palletize Type");
      						  }
      						  if($("#IsSessionSet").val()=="1") //HauContractor Type  is selected
      						  {
      							$("#LoadAllHauContractorHyperlink").html("Selected Contractor");
      						  }
      						  if($("#IsDistributorOrderStatusSessionSet").val()=="1") //Distributor Order Status is selected
    						  {
      							$("#LoadAllDistributorOrderStatusHyperlink").html("Selected Order Status");
      						  }
      						  if($("#IsChannelSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllChannelsHyperlink").html("Selected Channels");
    						  }
    						  if($("#IsCitySessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllCityHyperlink").html("Selected Towns");
    						  }
    						   if($("#IsSKUSessionSet").val()=="1") //mean order booker is selected
    						  {
    							$("#LoadAllSKUHyperlink").html("Selected SKUs");
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

function GetTodayDate1()
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
	LoadDate('LoadDateRangeHyperlink1');
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

function GetYesterdayDate1()
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
		LoadDate('LoadDateRangeHyperlink1');
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

function ShowHideFilter(){
	
	if($("#FilterShowHideFlag").val()=="0"){
		$("#LoadSalesSummaryFilterBy").css("display", "none");
		$("#LoadAllSearchResultsDIV").css("display", "none");
		$("#FilterTDID").css('width','0%');
		$("#ReportSaleSummaryTD").css('width','100%');
		$("#FilterShowHideFlag").val("1");
	}else{
		$("#LoadSalesSummaryFilterBy").css("display", "block");
		$("#LoadAllSearchResultsDIV").css("display", "block");
		$("#FilterTDID").css('width','15%');
		$("#ReportSaleSummaryTD").css('width','68%');
		$("#FilterShowHideFlag").val("0");
	}
	
	
}

function OutletSearchCallBackReporCenter(SAPCode, EmployeeName){
	$('#OutletIDManual').val(SAPCode);
	$('#OutletNameManual').val(EmployeeName);
	//$('#DeskSaleOutledIDHidden').val( SAPCode );
	//getOutletMasterInfo();
}

function getOutletName(){
	//alert();
	if(isInteger($('#OutletIDManual').val()) == false ){
		$('#OutletIDManual').val('');
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "common/GetOutletBySAPCodeJSON",
		data: {
			SAPCode: $('#OutletIDManual').val(),
			FeatureID:116
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#OutletNameManual').val(json.OutletName);
				$.mobile.hidePageLoadingMsg();
			}else{
				$.mobile.hidePageLoadingMsg();
				$('#OutletNameManual').val('Outlet not found');
				
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
	
}

function LoadAllDistributorsLifting(ID)
{
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("3"); //3 for Distributors
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDistributorsLifting.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function ResolveComplaint(){
	var ComplaintID = $("#SelectedComplaintIDHidden").val();
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "crm/ComplaintResolveExecute",
		data: {
			ComplaintID1: ComplaintID,
			Remarks: $("#ComplaintRemarks").val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.success == "true"){				
				$.mobile.hidePageLoadingMsg();
				location = "ReportCenter.jsp?ReportID=49";
			}else{
				alert("Server could not be reached");
				$.mobile.hidePageLoadingMsg();
				location = "ReportCenter.jsp?ReportID=49";
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
			location = "ReportCenter.jsp?ReportID=49";			
		}
		
	});
	
}

function ResolveComplaintTOT(){
	var ComplaintID = $("#SelectedComplaintIDHidden").val();
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "crm/ComplaintResolveExecute",
		data: {
			ComplaintID1: ComplaintID,
			Remarks: $("#ComplaintRemarks1").val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.success == "true"){				
				$.mobile.hidePageLoadingMsg();
				location = "ReportCenter.jsp?ReportID=50";
			}else{
				alert("Server could not be reached");
				$.mobile.hidePageLoadingMsg();
				location = "ReportCenter.jsp?ReportID=50";
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
			location = "ReportCenter.jsp?ReportID=50";			
		}
		
	});
	
}

function SetComplaintID(ComplaintID){
	$("#SelectedComplaintIDHidden").val(ComplaintID);
}

function ApplyAllFiltersAtOnce(){
	if($("#SearchId").val()=="")
	{alert("Please Enter Feature/Report ID");
	return;}else
		{
	SaleSummaryLoadReport();}
}

function PaymentPosting(ID,InstromentID,AccountName,WarehouseID,AccountNumber,Amount){
		
$.mobile.showPageLoadingMsg();
	//alert($("#UniqueVoucherID").val());
	$.ajax({
		
		url: "cash/GLPaymentPostingExecute",
		data: {
			SerialNo: ID,
			PostingType: $("input[name=PostingOptions]:checked").val(),
			InstrumentID1:InstromentID,
			UVID:$("#UniqueVoucherID_"+ID).val(),
			AccountName1:AccountName,
			WarehouseID1:WarehouseID,
			AccountNumber1:AccountNumber,
			Amount1:Amount
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.success == "true"){				
				$("#PostingOptionsID_"+ID).checkboxradio('disable');
				$("#PostingOptionsIDD_"+ID).checkboxradio('disable');
				$.mobile.hidePageLoadingMsg();
				//location = "ReportCenter.jsp?ReportID=66";
			}else{
				alert("Server could not be reached");
				$.mobile.hidePageLoadingMsg();
				
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
			//location = "ReportCenter.jsp?ReportID=50";			
		}
		
	});
}

function R146CustomerIDLoadDistSearch()
{	
		
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open");
	
}
function R146DistributorSearchCallBack(SAPCode, DistributorName){
	
		$('#CustomerID').val(SAPCode);
		//$('#LiftingReportMainDistributorName').val(DistributorName);
	
}



function R164DistributorIDLoadDistSearch()
{	
		
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open");
	
}
function R164DistributorSearchCallBack(SAPCode, DistributorName){
	
	$('#DistributorID').val(SAPCode);
	//$('#LiftingReportMainDistributorName').val(DistributorName);

}
function R164LoadPerCaseView(SamplingID){
	//alert(SamplingID);
	//
	$.get('ReportCenterR164PerCaseView.jsp?SampleID='+SamplingID+'&UniqueSessionID='+$("#UniqueSessionID").val(), function(data) {   			
		  $("#SearchContent34").html(data);
		  $("#SearchContent34").trigger('create');
		  
	})
	.fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
}



function R172LoadPlannedPackagesQuantity(RequestID, SamplingID){
	//alert(SamplingID);
	//
	$.get('ReportCenterR172LoadPlannedPackagesQuantity.jsp?RequestID='+RequestID+'&SamplingID='+SamplingID+'&UniqueSessionID='+$("#UniqueSessionID").val(), function(data) {   			
		  $("#SearchContent35").html(data);
		  $("#SearchContent35").trigger('create');
		  
	})
	.fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
}

function LoadDateTimeRange(ID)
{
	//alert(ID);
	//$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("41"); //41 for DateTime Range
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDateTimeRange.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function GetTodayDateTime()
{
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} today = dd+'/'+mm+'/'+yyyy;
	$("#StartDateTime").val(today);
	
	var today1 = new Date();
	
	$("#EndDateTime").val(today1.getDate());
	
	$("#SelectedDateType").val("Today");
	
	AddPackagesIntoSession();
	LoadDateTimeRange('LoadDateRangeHyperlink');
}



function GetThisWeekDateTime(date)
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
	  var dd = monday.getDate()+1;
	  var mm = monday.getMonth()+1; //January is 0!
	  var yyyy = monday.getFullYear();
	  
	 
	  
	  if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} today = dd+'/'+mm+'/'+yyyy;
	  
	  $("#StartDateTime").val(lastWeekDisplay);
	  $("#EndDateTime").val(today);

		
	  $("#SelectedDateType").val("This Week");
		AddPackagesIntoSession();	  
		LoadDateTimeRange('LoadDateRangeHyperlink');
	 // alert(lastsunday);
	  // Return array of date objects
	  //alert(sunday+" - "+monday);
}

function GetThisMonthDateTime()
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
	 
	 var dd1 = lastDay.getDate()+1;
	 var mm1 = lastDay.getMonth()+1; //January is 0!

	 var yyyy1 = lastDay.getFullYear();
	 if(dd1<10){dd1='0'+dd1;} if(mm1<10){mm1='0'+mm1;} lastDayFormat = dd1+'/'+mm1+'/'+yyyy1;
	 
	 $("#StartDateTime").val(firstDayFormat);
	 $("#EndDateTime").val(lastDayFormat);
	 
	  $("#SelectedDateType").val("This Month");
		AddPackagesIntoSession();	  
		LoadDateTimeRange('LoadDateRangeHyperlink');
	 
	 //alert();
	 //alert(lastDayFormat);
}

function GetYesterdayDateTime()
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
	$("#StartDateTime").val(yesterday);
	$("#EndDateTime").val(today);

	
	  $("#SelectedDateType").val("Yesterday");
		AddPackagesIntoSession();	  
		LoadDateTimeRange('LoadDateRangeHyperlink');
	//alert($yesterday);
}

function GetYesterdayDate1Time()
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
	$("#StartDateTime").val(yesterday);
	$("#EndDateTime").val(yesterday);

	
	  $("#SelectedDateType").val("Yesterday");
		AddPackagesIntoSession();	  
		LoadDate('LoadDateRangeHyperlink1');
	//alert($yesterday);
}

function GetLastWeekDateTime()
{
	var today = new Date();
    var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate());       
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
    $("#StartDateTime").val(lastWeekDisplayStart);
	$("#EndDateTime").val(lastWeekDisplayEnd);    
	
	  $("#SelectedDateType").val("Last Week");
		AddPackagesIntoSession();	  
		LoadDateTimeRange('LoadDateRangeHyperlink');	
    
}

function GetLastMonthDateTime()
{
	var date = new Date();
	var firstDay = new Date(date.getFullYear(), date.getMonth()-1, 1);
	var lastDay = new Date(date.getFullYear(), date.getMonth(), 0);
	var firstDayFormat; var lastDayFormat;
	
	 var dd = firstDay.getDate();
	 var mm = firstDay.getMonth()+1; //January is 0!

	 var yyyy = firstDay.getFullYear();
	 if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} firstDayFormat = dd+'/'+mm+'/'+yyyy;
	 
	 var dd1 = lastDay.getDate()+1;
	 var mm1 = lastDay.getMonth()+1; //January is 0!

	 var yyyy1 = lastDay.getFullYear();
	 if(dd1<10){dd1='0'+dd1;} if(mm1<10){mm1='0'+mm1;} lastDayFormat = dd1+'/'+mm1+'/'+yyyy1;
	 
	// alert(lastDayFormat);
	 
	 $("#StartDateTime").val(firstDayFormat);
	 $("#EndDateTime").val(lastDayFormat);

	 
	 
	  $("#SelectedDateType").val("Last Month");
		AddPackagesIntoSession();	  
		LoadDateTimeRange('LoadDateRangeHyperlink');	 
	 //alert(lastDayFormat);
}

function LoadAllDistributorType(ID)
{
	//alert(ID);
	$( "#t12Colps" ).trigger( "collapse" );
	$("#FilterType").val("47"); //47 for Distributor type by nasir
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDistributorType.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllDistributorOrderStatus(ID)
{
	//alert(ID);
	$("#FilterType").val("48"); //48 for Distributor Order Status	
	$( "#t12Colps" ).trigger( "collapse" );	
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterDistributorOrderStatus.jsp?ClickID='+ID+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function LoadAllChannels(ID)
{
	$("#FilterType").val("51"); //2 for Brand
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterChannels.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function ChangeLabelChannels(ID,CheckBoxID)
{
	$("#"+ID).html("Selected Channels");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="ChannelsCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All Channels");
	    	 
    	}
		
	   
	});
}

function LoadAllCity(ID)
{
	$("#FilterType").val("52"); // 52 for City
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterCity.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function ChangeLabelCity(ID,CheckBoxID)
{
	$("#"+ID).html("Selected Towns");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="CityCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All Towns");
	    	 
    	}
		
	   
	});
}

function LoadAllSKUs(ID)
{
	$("#FilterType").val("53"); // 53 for SKU
	$.mobile.loading( 'show');	
	$.get('ReportCenterFilterSKU.jsp?ClickID='+ID+"&Title="+$("#"+ID).html()+'&UniqueSessionID='+$("#UniqueSessionID").val()+'&rand='+Math.random(), function(data) {   			
			  $("#LoadAllSearchResultsDIV").html(data);
			  $("#LoadAllSearchResultsDIV").trigger('create');
			  $.mobile.loading( 'hide');
		})
		.fail(function() {
			  $.mobile.loading( 'hide');
			  alert("Server could not be reached");
		  }) ;
}

function ChangeLabelSKU(ID,CheckBoxID)
{
	$("#"+ID).html("Selected SKUs");
	$("#"+ID).css({'font-weight':'bold'});
	$('#LoadAllSearchResultsDIV input:checkbox').each(function() {
	  	   
	    if($('input[name="SKUCheckBox"]:checked').length > 0)
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
	    	$("#"+ID).html("All SKUs");
	    	 
    	}
		
	   
	});
}

