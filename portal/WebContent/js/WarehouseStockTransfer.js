

var RowCount = 0;
$( document ).delegate("#DispatchReturnsMainInner", "pageinit", function() {
	
		
});




function GetAllSalesForReturn(DispatchID,VehicleTypeName,DriverName,vehicleName)
{
	$("#DispatchID").val(DispatchID); //setting dispatch id	
	$("#DisptachReturnsVehicleNumber").val(vehicleName); //setting dispatch id	
	$("#DisptachReturnsDriverName").val(DriverName); //setting dispatch id	
	$("#DisptachVehicleType").val(VehicleTypeName); //setting dispatch id	
	
	
	document.getElementById("ReturnsGenerrateForm").submit();
}

function DispatchReturnsAddProduct(){	

	//alert();
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
	
	
	
	
	$("#DispatchReturnMainForm").css("visibility","visible");

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
		    				$.mobile.loading( 'hide');
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
							$("#DispatchReturnsSummary").html("<img src='images/snake-loader.gif'>");
							var data1 = $("#DispatchReturnMainForm" ).serialize();
							
							
							
							
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
		    				
		    				
	    				}else{
	    					alert("Product alredy added");
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
	}
	$("#DispatchReturnsSummary").html("<img src='images/snake-loader.gif'>");
	var data1 = $("#DispatchReturnMainForm" ).serialize();
	$.get('DispatchReturnsSummary.jsp?'+data1+'&DispatchID='+$("#DispatchIDForInsertion").val()+"&VehicleName="+$("#VehicleNameForTitle").val(), function(data) {   			
		  $("#DispatchReturnsSummary").html(data);
		  $("#DispatchReturnsSummary").trigger('create');
		  $.mobile.loading( 'hide');
	})
	 .fail(function() {
		  $.mobile.loading( 'hide');
		  alert("Server could not be reached");
	  }) ;
	
	
}

function ProductionReceiptSubmit()
{
	//alert($("#DispatchReturnsVehicleNumber").val());
	//alert($('#WareHouseID').val());
	if($("#DispatchReturnsVehicleNumber").val() !=""){
		if( $("#WareHouseID").val() !="-1"){
			
			$("#DispatchReturnsVehicleNumberhidden").val($("#DispatchReturnsVehicleNumber").val());
			$("#WarehouseNumberhidden").val($("#WareHouseID").val());
			$.mobile.loading( 'show');
			$.ajax({
			    url: "inventory/WarehouseStockTransferExecute",
			    data: $("#DispatchReturnMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if(json.success=="true")	    		
		    		{
			    		
			    		location = "WarehouseStockTransfer.jsp?ID="+json.MasterTableID;
			    		//$("#ReceiptFromProductionPrint").css('display','block');
			    		//$("#ReceiptFromProductionMasterTableID").val(json.MasterTableID);
			    		$.mobile.loading( 'hide');
		    		}
			    	else
		    		{
			    		//alert("hello");
			    		alert(json.error);
			    		//alert("Server could not be reached.");
			    		  $.mobile.loading( 'hide');
		    		}
			    	
			    },
			    error: function( xhr, status ) {
			    	alert("Server could not be reached.");
			    	//alert(json.error);
			    	  $.mobile.loading( 'hide');
			    }
			});
			
		}else{
			document.getElementById('WareHouseID').focus();
		}
	}else{
		document.getElementById('DispatchReturnsVehicleNumber').focus();
		//alert();
	}
	
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

function LoadBrands(PackageID1){
	$.mobile.loading( 'show');
	$.ajax({
	    url: "common/GetBrandListJson",
	    data: {
	    	PackageID: PackageID1,
	    	IsLiquidOnly:true
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	if(json.exists="true")	    		
    		{
	    		$("#ReceiptFromProdBrandULList").html("");
	    		var list = "";
	    		list = "<ul data-role='listview' data-inset='true'  style='font-size:10pt; margin-top:-10px;' data-icon='false'><li data-role='list-divider' data-theme='a'>Brands</li>";
				    			
	    		for(var i=0; i<json.rows.length;i++){
	    			list +="<li><a href='#' style='font-size:10pt; font-weight:normal;' onClick='LoadProductCode("+PackageID1+","+json.rows[i].id+")'>"+json.rows[i].label+"</a></li>"
	    		}
	    		list +="</ul>";
	    		//alert(list);
	    		$("#ReceiptFromProdBrandULList").html(list);
	    		$("#ReceiptFromProdBrandULList").trigger('create');
	    		 $.mobile.loading( 'hide');
    		}
	    	else
    		{
	    		alert("Server could not be reached.");
	    		  $.mobile.loading( 'hide');
    		}
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	  $.mobile.loading( 'hide');
	    }
	});
}

function LoadProductCode(PackageID1,BrandID1){	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "common/GetProductInfo",
	    data: {
	    	PackageID: PackageID1,
	    	BrandID: BrandID1
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	if(json.exists="true")	    		
    		{
	    		$("#DispatchReturnsProductCode").val(json.ProductCode);
	    		document.getElementById('DispatchReturnsRawCases').focus();
	    		 $.mobile.loading( 'hide');
    		}
	    	else
    		{
	    		alert("Server could not be reached.");
	    		  $.mobile.loading( 'hide');
    		}
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	  $.mobile.loading( 'hide');
	    }
	});
}

function showSearchContent(){
	
	
	$("#SearchContent").focus();
	
	//document.getElementById("ReceiptFromProductionFromDate").disabled = "disabled";
	//document.getElementById("ReceiptFromProductionToDate").disabled = "disabled";
	
	$.get('WarehouseStockTransferSearch.jsp?FromDate='+$('#ReceiptFromProductionFromDate').val()+'&ToDate='+$('#ReceiptFromProductionToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function LoadEditCase(ReceiptProductionID,CreatedOn){
	//alert(ReceiptProductionID);
	//alert(CreatedOn);
	$("#ProductionReceiptID").val(ReceiptProductionID);
	$("#CreatedOnEditCase").val(CreatedOn);
	//alert($("#CreatedOnEditCase").val());
	document.getElementById("ReceiptFromProductionSearchForm").submit();
}

function ProductionReceiptPrint(){
	document.getElementById("ReceiptFromProductionPrintingForm").submit();
}

function EnableSave(){
	$("#DispatchReturnsSave").removeClass("ui-disabled");
}

