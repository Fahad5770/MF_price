
var RowCount = 0;

$( document ).delegate("#PhysicalStockAdjustment", "pageinit", function() {
	
	$('#PhysicalStockAdjustmentProductCode').on('dblclick', function(e, data){
		
		$( "#LookupProductSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupProductInit();
		} );
		$('#LookupProductSearch').popup("open");
				  
	});	
	
});

function PhysicalStockAdjustmentAddProduct(){	

	//alert($("#DispatchID").val());
	//alert("hello");
	if( $('#PhysicalStockAdjustmentProductCode').val() == "" ){
		document.getElementById('PhysicalStockAdjustmentProductCode').focus();
		return false;
	}else{
		var value = $('#PhysicalStockAdjustmentProductCode').val();
		if(isInteger(value) == false){
			document.getElementById('PhysicalStockAdjustmentProductCode').focus();
			return false;
		}
	}
	
	if( ( $('#PhysicalStockAdjustmentRawCases').val() == "" ) && ( $('#PhysicalStockAdjustmentUnits').val() == "" ) ){
		document.getElementById('PhysicalStockAdjustmentRawCases').focus();
		return false;
	}else{
		var value_raw_cases = $('#PhysicalStockAdjustmentRawCases').val();
		if(value_raw_cases!= "" && isInteger(value_raw_cases) == false){
			document.getElementById('PhysicalStockAdjustmentRawCases').focus();
			return false;
		}
		
		var value_units = $('#PhysicalStockAdjustmentUnits').val();
		if(value_units != "" && isInteger(value_units) == false){
			document.getElementById('PhysicalStockAdjustmentUnits').focus();
			return false;
		}
		
	}

	var val = $('#PhysicalStockAdjustmentProductCode').val();
	
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
				
		    	$.mobile.loading( 'hide');
				
		    	if (json.exists == "true"){
		    		
		    		var ValDeskSaleRawCases = $('#PhysicalStockAdjustmentRawCases').val();
	    			if( ValDeskSaleRawCases == '' ){
	    				ValDeskSaleRawCases = '0';
	    			}
					
		    		var isAlreadyEntered = false;		    		
		    		
		    		if (isAlreadyEntered != true){
		    			
		    			var ValDeskSaleUnits = $('#PhysicalStockAdjustmentUnits').val();
		    			if( ValDeskSaleUnits == '' ){
		    				ValDeskSaleUnits = '0';
		    			}		    			
		    			
		    			if( parseInt(ValDeskSaleUnits) >= parseInt(json.UnitPerSKU) ){
		    				alert('Bottles should be less than units per SKU');
		    				document.getElementById('PhysicalStockAdjustmentUnits').focus();
		    				return false;
		    			}
		    			
		    			//DispatchReturnsMainFormProductName
		    			var AddedProductArray = new Array();
		    			var IsAlreadyProductAdded = false;
		    			AddedProductArray = document.getElementsByName("PhysicalStockAdjustmentMainFormProductCode");
		    			
		    			for(var i=0;i<AddedProductArray.length;i++)
		    			{
		    				if(AddedProductArray[i].value==$('#PhysicalStockAdjustmentProductCode').val())
		    				{
		    					IsAlreadyProductAdded = true;
		    					break;
		    				}
		    			}
		    			
		    			var TotalUnits = (parseInt(ValDeskSaleRawCases)*parseInt(json.UnitPerSKU))+parseInt(ValDeskSaleUnits);
		    			
		    			//if(!IsAlreadyProductAdded && IsAlreadyProductDispatchReturnSummaryAdded && !IsAlreadyTotalUnitsDispatchReturnSummaryAdded)
		    			
		    			$.ajax({
		    			    url: "inventory/PhysicalStockAdjustmentValidate",
		    			    
		    			    data: {
		    			    	DistID : $('#PhysicalStockAdjustmentDistributor').val()
		    			    },
		    			    type: "POST",
		    			    dataType : "json",
		    			    success: function( json2 ) {
		    			    	if (json2.success == "true"){
		    			    		//$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
		    			    		//flag = true;
		    			    		
				    				if(!IsAlreadyProductAdded)
				    				{
					    				
					    				var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
					    				
						    			//setting TotalUnits in hidden field to use it in summary
								    	var content = ""+
										"<tr id='PhysicalStockAdjustment"+RowMaxID+"'>"+
											"<td>"+$('#PhysicalStockAdjustmentProductCode').val()+"<input type='hidden' name='PhysicalStockAdjustmentMainFormUnitPerSKU' value='"+json.UnitPerSKU+"'><input type='hidden' name='PhysicalStockAdjustmentMainFormProductCode' value='"+$('#PhysicalStockAdjustmentProductCode').val()+"'></td>"+	
											"<td>"+json.PackageLabel+"<input type='hidden' name='PhysicalStockAdjustmentMainFormPackageLabel' value='"+json.PackageLabel+"' ><input type='hidden' name='PackageID' value='"+json.PackageID+"' ><input type='hidden' name='PhysicalStockAdjustmentMainFormLiquidInML' value='"+json.LiquidInML+"'><input type='hidden' name='TotalUnitsCalculatedForSummary' value='"+TotalUnits+"'></td>"+						
											"<td>"+json.BrandLabel+"<input type='hidden' name='PhysicalStockAdjustmentMainFormBrandLabel' value='"+json.BrandLabel+"' ><input type='hidden' name='BrandID' value='"+json.BrandID+"' ><input type='hidden' id='ProductCode' name='ProductCode' value='"+val+"'><input type='hidden' name='ProductID' value='"+json.ProductID+"'></td>"+
											"<td>"+ValDeskSaleRawCases+"<input type='hidden' name='PhysicalStockAdjustmentMainFormRawCases' value='"+ValDeskSaleRawCases+"'></td>"+
											"<td>"+ValDeskSaleUnits+"<input type='hidden' name='PhysicalStockAdjustmentMainFormUnits' value='"+ValDeskSaleUnits+"'></td>"+							
											"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"PhysicalStockAdjustmentDeleteRow('PhysicalStockAdjustment"+RowMaxID+"');\">Delete</a></td>"+
										"</tr>";
								    	
										$("#PhysicalStockAdjustmentTableBody").append(content).trigger('create');
										
										RowCount++;
										//alert(RowCount);
										$('#NoProductRow').css('display', 'none');
										
										$('#PhysicalStockAdjustmentProductCode').val('');
										$('#PhysicalStockAdjustmentRawCases').val('');
										$('#PhysicalStockAdjustmentUnits').val('');
										
										$('#ProductInfoSpan').html('');					
										
										
										$('#PhysicalStockAdjustmentProductCode').focus();
										
											$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
										
										
										
										$('#RowMaxID').val(RowMaxID);
				    				}
				    				else
				    				{
				    					alert("Product already added");
				    				}
		    			    		
		    			    		
		    			    	}else{
		    			    		//$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
		    						alert(json.error);
		    			    	}
		    			    },
		    			    error: function( xhr, status ) {
		    			    	alert("Server could not be reached.");
		    			    }
		    			});
		    			/*
			    		if(CheckPendingDispatch()){
			    			
		    			}
		    			*/
		    			
					
		    		}else{
		    			document.getElementById('PhysicalStockAdjustmentProductCode').focus();
		    		}
					
		    	}else{
		    		alert("Invalid Product Code");
					$('#PhysicalStockAdjustmentProductCode').focus();
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

function PhysicalStockAdjustmentDeleteRow(RowID){
	
	$("#"+RowID).remove();
	RowCount--;
	$('#RowMaxID').val(RowCount); //decreasing the row count
	
	if(RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#PhysicalStockAdjustmentSave').addClass("ui-disabled");
	}else{
		$('#PhysicalStockAdjustmentSave').removeClass("ui-disabled");
	}
	
}

function PhysicalStockAdjustmentSubmit(){
	
	if( $('#PhysicalStockAdjustmentDistributor').val() == '0' ){
		alert('Please select Distributor');
		document.getElementById("PhysicalStockAdjustmentDistributor").focus();
		return false;
	}
	
	
	$('#PhysicalStockAdjustmentDistributorID').val( $('#PhysicalStockAdjustmentDistributor').val() );
	
	$('#PhysicalStockAdjustmentSave').addClass("ui-disabled");
	$.ajax({
	    url: "inventory/PhysicalStockAdjustmentExecute",
	    
	    data: $("#PhysicalStockAdjustmentMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "PhysicalStockAdjustment.jsp";
	    	}else{
	    		$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
				alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}


function PhysicalStockAdjustmentReset(){
	location = "PhysicalStockAdjustment.jsp";
}

function PhysicalStockAdjustmentGetEditInfoJson(EditIDVal){
	$('#PhysicalStockAdjustmentSave').addClass("ui-disabled");
	$.ajax({
	    url: "inventory/GetPhysicalStockAdjustmentInfoJson",
	    
	    data: {
	    	EditID : EditIDVal
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		
	    		for(var i = 0; i < json.rows.length; i++){
	    		
	    			var content = content + ""+
					"<tr id='PhysicalStockAdjustment"+(i+1)+"'>"+
						"<td>"+json.rows[i].SAPCode+"<input type='hidden' name='PhysicalStockAdjustmentMainFormUnitPerSKU' value='"+json.rows[i].UnitPerSKU+"'><input type='hidden' name='PhysicalStockAdjustmentMainFormProductCode' value='"+json.rows[i].SAPCode+"'></td>"+	
						"<td>"+json.rows[i].PackageLabel+"<input type='hidden' name='PhysicalStockAdjustmentMainFormPackageLabel' value='"+json.rows[i].PackageLabel+"' ><input type='hidden' name='PackageID' value='"+json.rows[i].PackageID+"' ><input type='hidden' name='PhysicalStockAdjustmentMainFormLiquidInML' value='"+json.rows[i].LiquidInML+"'><input type='hidden' name='TotalUnitsCalculatedForSummary' value=''></td>"+						
						"<td>"+json.rows[i].BrandLabel+"<input type='hidden' name='PhysicalStockAdjustmentMainFormBrandLabel' value='"+json.rows[i].BrandLabel+"' ><input type='hidden' name='BrandID' value='"+json.rows[i].BrandID+"' ><input type='hidden' id='ProductCode' name='ProductCode' value='"+json.rows[i].SAPCode+"'><input type='hidden' name='ProductID' value='"+json.rows[i].ProductID+"'></td>"+
						"<td>"+json.rows[i].RawCases+"<input type='hidden' name='PhysicalStockAdjustmentMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
						"<td>"+json.rows[i].Units+"<input type='hidden' name='PhysicalStockAdjustmentMainFormUnits' value='"+json.rows[i].Units+"'></td>"+							
						"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"PhysicalStockAdjustmentDeleteRow('PhysicalStockAdjustment"+(i+1)+"');\">Delete</a></td>"+
					"</tr>";
	    			
	    			RowCount++;
	    			
	    			
	    		}
	    		$("#PhysicalStockAdjustmentTableBody").append(content).trigger('create');
				
				
				
				//alert(RowCount);
				$('#NoProductRow').css('display', 'none');
				
				
				
				$('#PhysicalStockAdjustmentProductCode').focus();
				$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
				
				$('#RowMaxID').val(RowCount);
	    		
	    		
	    	}else{
	    		$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
				alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function showSearchContent(){
	
	$("#SearchContent").focus();
	
	document.getElementById("DeliveryNoteFromDate").disabled = "disabled";
	document.getElementById("DeliveryNoteToDate").disabled = "disabled";
	
	$.get('PhysicalStockAdjustmentSearch.jsp?FromDate='+$('#DeliveryNoteFromDate').val()+'&ToDate='+$('#DeliveryNoteToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function getProductInfoJson(ProductID){

	if (ProductID != ""){
		$.ajax({
		    url: "common/GetProductInfo",
		    data: {
		    	ProductCode: ProductID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					//alert(json.PackageLabel+" "+json.BrandLabel);
					$('#ProductInfoSpan').html(json.PackageLabel+" - "+json.BrandLabel);
					
		    	}else{
		    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}


function ProductSearchCallBack(SAPCode, ProductName){
	$('#PhysicalStockAdjustmentProductCode').val(SAPCode);
	$('#ProductInfoSpan').html(ProductName);
	
}

function CheckPendingDispatch(){
	if( $('#PhysicalStockAdjustmentDistributor').val() == '0' ){
		$('#PhysicalStockAdjustmentSave').addClass('ui-disabled');
		return false;
	}
	//alert($('#PhysicalStockAdjustmentDistributor').val());
	var flag=false;
	$.ajax({
	    url: "inventory/PhysicalStockAdjustmentValidate",
	    
	    data: {
	    	DistID : $('#PhysicalStockAdjustmentDistributor').val()
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		$('#PhysicalStockAdjustmentSave').removeClass('ui-disabled');
	    		flag = true;
	    	}else{
	    		$('#PhysicalStockAdjustmentSave').addClass('ui-disabled');
				alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
	return flag;
}