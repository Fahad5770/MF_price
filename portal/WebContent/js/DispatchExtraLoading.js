

var RowCount = 0;
$( document ).delegate("#DispatchReturnsMainInner", "pageinit", function() {
	
	var data1 = $("#DispatchExtraLoadingMainForm" ).serialize() +'&DispatchID='+$("#DispatchIDForInsertion").val()+"&VehicleName="+$("#VehicleNameForTitle").val();
	
	$.mobile.loading( 'show');
	/*
	$.get('DispatchExtraSummary.jsp?'+data1+'&DispatchID='+$("#DispatchIDForInsertion").val()+"&VehicleName="+$("#VehicleNameForTitle").val(), function(data) {   			
		  $("#DispatchReturnsSummary").html(data);
		  $("#DispatchReturnsSummary").trigger('create');
		 
		  $.mobile.loading( 'hide');
	})
	.fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
	*/
	$.ajax({
	    url: "DispatchExtraSummary.jsp",
	    data: data1,
	    type: "POST",
	    dataType : "html",
	    success: function( htmlres ) {
	    	
			  $("#DispatchReturnsSummary").html(htmlres);
			  $("#DispatchReturnsSummary").trigger('create');
			  $.mobile.loading( 'hide');
			  
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.loading( 'hide');
	    }
	});
	
	
	$('#DispatchReturnsProductCode').on('dblclick', function(e, data){
		
		$( "#LookupProductSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupProductInit();
		} );
		$('#LookupProductSearch').popup("open");
				  
	});	
	
	if( parseInt($('#RowMaxID').val()) >= 1){		
		$("#NoLiquidReqTd").addClass("ui-disabled");
	}
	
	
});

function LiquidReturns()
{
	document.getElementById("ReturnsGenerrateForm").submit();
}
function EmptyReturns()
{
	document.getElementById("EmptyGenerrateForm").submit();
}


function GetAllSalesForReturn(DispatchID,VehicleTypeName,DriverName,vehicleName)
{
	$("#DispatchID").val(DispatchID); //setting dispatch id	
	$("#DisptachReturnsVehicleNumber").val(vehicleName); //setting dispatch id	
	$("#DisptachReturnsDriverName").val(DriverName); //setting dispatch id	
	$("#DisptachVehicleType").val(VehicleTypeName); //setting dispatch id	
	
	
	document.getElementById("ReturnsGenerrateForm").submit();
}

function DispatchReturnsAddProduct(){	

	//alert($("#DispatchID").val());
	//alert("hello");
	if( $('#DispatchReturnsProductCode').val() == "" ){
		document.getElementById('DispatchReturnsProductCode').focus();
		return false;
	}else{
		var value = $('#DispatchReturnsProductCode').val();
		if(isInteger(value) == false){
			document.getElementById('DispatchReturnsProductCode').focus();
			return false;
		}
	}
	
	if( ( $('#DispatchReturnsRawCases').val() == "" || $('#DispatchReturnsRawCases').val() == "0" ) && ( $('#DispatchReturnsUnits').val() == "" || $('#DispatchReturnsUnits').val() == "0" ) ){
		document.getElementById('DispatchReturnsRawCases').focus();
		return false;
	}else{
		var value_raw_cases = $('#DispatchReturnsRawCases').val();
		if(value_raw_cases!= "" && isInteger(value_raw_cases) == false){
			document.getElementById('DispatchReturnsRawCases').focus();
			return false;
		}
		
		var value_units = $('#DispatchReturnsUnits').val();
		if(value_units != "" && isInteger(value_units) == false){
			document.getElementById('DispatchReturnsUnits').focus();
			return false;
		}
		
	}
	
	
	
	
	$("#DispatchExtraLoadingMainForm").css("visibility","visible");

	var val = $('#DispatchReturnsProductCode').val();
	
	if (val != "" && val.length > 0){
		
		$.mobile.loading( 'show');
		$.ajax({ 
		    url: "common/GetProductInfo",
		    data: {
		    	ProductCode: val
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	$.mobile.loading( 'show');
				
		    	if (json.exists == "true"){
		    		
		    		var ValDeskSaleRawCases = $('#DispatchReturnsRawCases').val();
	    			if( ValDeskSaleRawCases == '' ){
	    				ValDeskSaleRawCases = '0';
	    			}
					
					var ShellType = '';
					
					
		    		var isAlreadyEntered = false;		    		
		    		
		    		if (isAlreadyEntered != true){
		    			
		    			var ValDeskSaleUnits = $('#DispatchReturnsUnits').val();
		    			if( ValDeskSaleUnits == '' ){
		    				ValDeskSaleUnits = '0';
		    			}		    			
		    			
		    			if( parseInt(ValDeskSaleUnits) >= parseInt(json.UnitPerSKU) ){
		    				alert('Bottles should be less than units per SKU');
		    				document.getElementById('DispatchReturnsUnits').focus();
		    				return false;
		    			}
		    			
		    			//DispatchReturnsMainFormProductName
		    			var AddedProductArray = new Array();
		    			var IsAlreadyProductAdded = false;
		    			AddedProductArray = document.getElementsByName("DispatchReturnsMainFormProductName");
		    			
		    			for(var i=0;i<AddedProductArray.length;i++)
		    			{
		    				if(AddedProductArray[i].value==$('#DispatchReturnsProductCode').val())
		    				{
		    					IsAlreadyProductAdded = true;
		    					break;
		    				}
		    			}
		    			
		    			var TotalUnits = (parseInt(ValDeskSaleRawCases)*parseInt(json.UnitPerSKU))+parseInt(ValDeskSaleUnits);
		    			
		    			var AddedProductInDispatchReturnsSummaryArray = new Array();
		    			var IsAlreadyProductDispatchReturnSummaryAdded = false;
		    			AddedProductInDispatchReturnsSummaryArray = document.getElementsByName("DispatchReturnsSummaryProductSapCode");
		    			
		    			for(var i=0;i<AddedProductInDispatchReturnsSummaryArray.length;i++)
		    			{
		    				if(AddedProductInDispatchReturnsSummaryArray[i].value==$('#DispatchReturnsProductCode').val())
		    				{
		    					IsAlreadyProductDispatchReturnSummaryAdded = true;
		    					break;
		    				}
		    			}
		    			
		    			//checking for total units - error if entered units are more
		    			
		    			var AddedTotalUnitsInDispatchReturnsSummaryArray = new Array();
		    			var ProductUnitsArray = new Array();
		    			var IsAlreadyTotalUnitsDispatchReturnSummaryAdded = false;
		    			AddedTotalUnitsInDispatchReturnsSummaryArray = document.getElementsByName("DispatchReturnsSummaryTotalUnitsHidden");		    			
		    			for(var i=0;i<AddedTotalUnitsInDispatchReturnsSummaryArray.length;i++)
		    			{
		    				//alert("Already Stored units "+AddedTotalUnitsInDispatchReturnsSummaryArray[i].value+" New Entered Units "+TotalUnits+"|"+AddedProductInDispatchReturnsSummaryArray[i].value);		    				
		    				ProductUnitsArray=AddedTotalUnitsInDispatchReturnsSummaryArray[i].value.split("|");
		    				//alert(ProductUnitsArray[0]);		    				
		    				if(ProductUnitsArray[0]==$('#DispatchReturnsProductCode').val()) //mean same product now check the quantity
	    					{
		    					if(ProductUnitsArray[1]<TotalUnits)
	    						{
		    						IsAlreadyTotalUnitsDispatchReturnSummaryAdded = true;
			    					break;
	    						}
	    					}
		    			}
		    			
		    			//if(!IsAlreadyProductAdded && IsAlreadyProductDispatchReturnSummaryAdded && !IsAlreadyTotalUnitsDispatchReturnSummaryAdded)
		    			if(!IsAlreadyProductAdded)
	    				{
		    				
		    				var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
			    			//setting TotalUnits in hidden field to use it in summary
					    	var content = ""+
							"<tr id='DeskSale"+RowMaxID+"'>"+
								"<td>"+$('#DispatchReturnsProductCode').val()+"<input type='hidden' name='DispatchReturnsMainFormUnitPerSKU' value='"+json.UnitPerSKU+"'><input type='hidden' name='DispatchReturnsMainFormProductName' value='"+$('#DispatchReturnsProductCode').val()+"'></td>"+	
								"<td>"+json.PackageLabel+"<input type='hidden' name='DispatchReturnsMainFormPackageLabel' value='"+json.PackageLabel+"' ><input type='hidden' name='PackageID' value='"+json.PackageID+"' ><input type='hidden' name='DispatchReturnsMainFormLiquidInML' value='"+json.LiquidInML+"'><input type='hidden' name='TotalUnitsCalculatedForSummary' value='"+TotalUnits+"'></td>"+						
								"<td>"+json.BrandLabel+"<input type='hidden' name='DispatchReturnsMainFormBrandLabel' value='"+json.BrandLabel+"' ><input type='hidden' name='BrandID' value='"+json.BrandID+"' ><input type='hidden' id='ProductCode' name='ProductCode' value='"+val+"'><input type='hidden' name='ProductID' value='"+json.ProductID+"'></td>"+
								"<td>"+ValDeskSaleRawCases+"<input type='hidden' name='DispatchReturnsMainFormRawCases' value='"+ValDeskSaleRawCases+"'></td>"+
								"<td>"+ValDeskSaleUnits+"<input type='hidden' name='DispatchReturnsMainFormUnits' value='"+ValDeskSaleUnits+"'></td>"+							
								"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeskSaleDeleteRow('DeskSale"+RowMaxID+"');\">Delete</a></td>"+
							"</tr>";
					    	$.mobile.loading( 'show');
							$("#DispatchReturnsTableBody").append(content).trigger('create');
							
							var data1 = $("#DispatchExtraLoadingMainForm" ).serialize();
							$.get('DispatchExtraSummary.jsp?'+data1+'&DispatchID='+$("#DispatchIDForInsertion").val()+"&VehicleName="+$("#VehicleNameForTitle").val(), function(data) {   			
								  $("#DispatchReturnsSummary").html(data);
								  $("#DispatchReturnsSummary").trigger('create');
							});
							
							
							
							$("#NoLiquidReqTd").addClass("ui-disabled");
							$.mobile.loading( 'hide');
							
							
							RowCount++;
							//alert(RowCount);
							$('#NoProductRow').css('display', 'none');
							
							$('#DispatchReturnsProductCode').val('');
							$('#DispatchReturnsRawCases').val('');
							$('#DispatchReturnsUnits').val('');
							
							$('#ProductInfoSpan').html('');					
							
							
							$('#DispatchReturnsProductCode').focus();
							$('#DispatchReturnsSave').removeClass('ui-disabled');
							
							$('#RowMaxID').val(RowMaxID);
	    				}
		    			else
		    				{
		    					alert("Product already added");
		    					$.mobile.loading( 'hide');
		    				}
		    			
					
		    		}else{
		    			document.getElementById('DeskSaleProductCode').focus();
		    			  $.mobile.loading( 'hide');
		    		}
					
		    	}else{
		    		alert("Invalid Product Code");
					$('#DeskSaleProductCode').focus();
					  $.mobile.loading( 'hide');
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	  $.mobile.loading( 'hide');
		    }
		});
		
	}
	
	
	//$("#SamplingReceivingBarcodeField").val("");
	
	return false;
}

function DeskSaleDeleteRow(RowID){	
	$.mobile.loading( 'show');
	$("#"+RowID).remove();
	RowCount = $('#RowMaxID').val()-1;
	
	//alert(RowCount);
	$('#RowMaxID').val(RowCount); //decreasing the row count
	$("#DispatchReturnsSaveTD").removeClass("ui-disabled");
	$("#DispatchReturnsSave").removeClass("ui-disabled");
	
	
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#DeskSaleSave').addClass('ui-disabled');
		$("#NoLiquidReqTd").removeClass("ui-disabled");
		$("#DispatchReturnsSave").addClass("ui-disabled");
	}
	
	var data1 = $("#DispatchExtraLoadingMainForm" ).serialize();
	$.get('DispatchExtraSummary.jsp?'+data1+'&DispatchID='+$("#DispatchIDForInsertion").val()+"&VehicleName="+$("#VehicleNameForTitle").val(), function(data) {   			
		  $("#DispatchReturnsSummary").html(data);
		  $("#DispatchReturnsSummary").trigger('create');
		  $.mobile.loading( 'hide');
	})
	 .fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
}

function DispatchReturnsSubmit()
{
	//alert();
	$.mobile.loading( 'show');
	$.ajax({
	    url: "distributor/DispatchExtraLoadingExecute",
	    data: $("#DispatchExtraLoadingMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	if(json.success=="true")	    		
    		{
	    		location = "DispatchExtraLoadingMain.jsp";
	    		  $.mobile.loading( 'hide');
    		}
	    	else
    		{
	    		alert(json.error);
	    		  $.mobile.loading( 'hide');
    		}
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	  $.mobile.loading( 'hide');
	    }
	});
}

function NoLiquRequired15()
{
	if($("#NoLiquRequiredToggleBit").val()==0)
	{
		$('#DispatchReturnsSave').removeClass('ui-disabled');
		$("#NoLiquRequiredToggleBit").val("1");
		$("#AddDispatchReturnsBtnTD").addClass("ui-disabled");		
	}
	else
	{		
		$('#DispatchReturnsSave').addClass('ui-disabled');
		$("#NoLiquRequiredToggleBit").val("0");
		$("#AddDispatchReturnsBtnTD").removeClass("ui-disabled");
	}

	
}


function ProductSearchCallBack(SAPCode, ProductName){
	$('#DispatchReturnsProductCode').val(SAPCode);
	setTimeout(function(){
		document.getElementById("DispatchReturnsRawCases").focus();
	}, 100);
}