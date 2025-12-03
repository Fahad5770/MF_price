
var RowCount = 0;
var isAddable = true;

$( document ).delegate("#DamagedStock", "pageshow", function() {
	document.getElementById('DamagedStockRemarks').focus();
	
	//showSearchContent();
	
});

$( document ).delegate("#DamagedStock", "pageinit", function() {
	
	$("#FromDateSpan").click( function( event ) {
		document.getElementById("DamagedStockFromDate").disabled = false;
		document.getElementById("DamagedStockToDate").disabled = false;
		$("#DamagedStockFromDate").focus();
		
	});	
	$("#ToDateSpan").on( "click", function( event, ui ) {
		document.getElementById("DamagedStockFromDate").disabled = false;
		document.getElementById("DamagedStockToDate").disabled = false;
		$("#DamagedStockToDate").focus();
	});	

});




function DamagedStockDeleteRow(RowID){
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#DamagedStockSave').addClass('ui-disabled');
	}
}

function isInteger (o) {
	
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
	}

function DamagedStockAddProduct(){
	
	if( isAddable == false ){
		return false;
	}
	
	if( $('#DamagedStockProductCode').val() == "" ){
		document.getElementById('DamagedStockProductCode').focus();
		return false;
	}else{
		var value = $('#DamagedStockProductCode').val();
		if(isInteger(value) == false){
			document.getElementById('DamagedStockProductCode').focus();
			return false;
		}
	}
	
	if( ( $('#DamagedStockRawCases').val() == "" || $('#DamagedStockRawCases').val() == "0" ) && ( $('#DamagedStockUnits').val() == "" || $('#DamagedStockUnits').val() == "0" ) ){
		document.getElementById('DamagedStockRawCases').focus();
		return false;
	}else{
		var value_raw_cases = $('#DamagedStockRawCases').val();
		if(value_raw_cases!= "" && isInteger(value_raw_cases) == false){
			document.getElementById('DamagedStockRawCases').focus();
			return false;
		}
		
		var value_units = $('#DamagedStockUnits').val();
		if(value_units != "" && isInteger(value_units) == false){
			document.getElementById('DamagedStockUnits').focus();
			return false;
		}
		
	}
	
	
	
	if( $('#DamagedStockType').val() == "" ){
		document.getElementById('DamagedStockType').focus();
		return false;
	}
	
	
	$('#DamagedStockMainFormRemarks').val($('#DamagedStockRemarks').val());
	
	$("#DamagedStockMainForm").css("visibility","visible");

	var val = $('#DamagedStockProductCode').val();
	
	if (val != "" && val.length > 0){
		$.mobile.showPageLoadingMsg();	
		$.ajax({
		    url: "common/GetProductInfo",
		    data: {
		    	ProductCode: val
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
				$.mobile.hidePageLoadingMsg();
				
		    	if (json.exists == "true"){
		    		var isAlreadyEntered = false;
		    		
		    		var enteredBatchCode = $('#DamagedStockBatchCode').val();
		    		//alert('enteredBatchCode = '+enteredBatchCode);
		    		
		    		var existing = document.getElementsByName("ProductCode");
		    		var existingBatchCode = document.getElementsByName("DamagedStockMainFormBatchCode");
		    		for (x = 0; x < existing.length; x++){
		    			//alert('existingBatchCode = '+existingBatchCode[x]);
		    		}
		    		var x = 0;
		    		for (x = 0; x < existing.length; x++){
		    			if (existing[x].value == json.ProductCode && existingBatchCode[x].value ==  enteredBatchCode ){
		    				isAlreadyEntered = true;
		    				break;
		    			}
		    		}
		    		
		    		if (isAlreadyEntered != true){
		    			
		    			var ValDamagedStockRawCases = $('#DamagedStockRawCases').val();
		    			if( ValDamagedStockRawCases == '' ){
		    				ValDamagedStockRawCases = '0';
		    			}
		    			
		    			var ValDamagedStockUnits = $('#DamagedStockUnits').val();
		    			if( ValDamagedStockUnits == '' ){
		    				ValDamagedStockUnits = '0';
		    			}
		    			
		    			if( parseInt(ValDamagedStockUnits) >= parseInt(json.UnitPerSKU) ){
		    				alert('Bottles should be less than units per SKU');
		    				return false;
		    			}
		    			
				    	var content = ""+
						"<tr id='DamagedStock"+val+"'>"+
							"<td>"+$('#DamagedStockProductCode').val()+"<input type='hidden' name='DamagedStockMainFormUnitPerSKU' value='"+json.UnitPerSKU+"'></td>"+	
							"<td>"+json.PackageLabel+"<input type='hidden' name='DamagedStockMainFormLiquidInML' value='"+json.LiquidInML+"'></td>"+						
							"<td>"+json.BrandLabel+"<input type='hidden' name='ProductCode' value='"+val+"'><input type='hidden' name='ProductID' value='"+json.ProductID+"'></td>"+
							"<td>"+ValDamagedStockRawCases+"<input type='hidden' name='DamagedStockMainFormRawCases' value='"+ValDamagedStockRawCases+"'></td>"+
							"<td>"+ValDamagedStockUnits+"<input type='hidden' name='DamagedStockMainFormUnits' value='"+ValDamagedStockUnits+"'></td>"+
							"<td>"+$('#DamagedStockBatchCode').val()+"<input type='hidden' name='DamagedStockMainFormBatchCode' value='"+$('#DamagedStockBatchCode').val()+"'></td>"+
							"<td>"+$('#DamagedStockType option:selected').text()+"<input type='hidden' name='DamagedStockMainFormType' value='"+$('#DamagedStockType').val()+"'></td>"+
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DamagedStockDeleteRow('DamagedStock"+val+"')\">Delete</a></td>"+
						"</tr>";
						
						$("#DamagedStockTableBody").append(content).trigger('create');
						
						RowCount++;
						
						$('#NoProductRow').css('display', 'none');
						
						$('#DamagedStockProductCode').val('');
						$('#DamagedStockRawCases').val('');
						$('#DamagedStockUnits').val('');
						$('#DamagedStockBatchCode').val('');
						$('#DamagedStockType').val('');
						$('#DamagedStockType').change();
						$('#ProductInfoSpan').html('');
						$('#DamagedStockProductCode').focus();
						$('#DamagedStockSave').removeClass('ui-disabled');
						
					
		    		}
					
		    	}else{
		    		alert("Invalid Product Code");
					$('#DeliveryNoteProductCode').focus();
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
	
	
	//$("#SamplingReceivingBarcodeField").val("");
	
	return false;
}

function DamagedStockSubmit(){
	
	$('#DamagedStockMainFormRemarks').val($('#DamagedStockRemarks').val());
	
	$('#DamagedStockSave').addClass("ui-disabled");
	$.ajax({
	    url: "inventory/DamagedStockExecute",
	    
	    data: $("#DamagedStockMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "DamagedStock.jsp";
	    	}else{
				alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function showSearchContent(){
	
	$("#SearchContent").focus();
	
	document.getElementById("DamagedStockFromDate").disabled = "disabled";
	document.getElementById("DamagedStockToDate").disabled = "disabled";
	
	$.get('DamagedStockSearch.jsp?FromDate='+$('#DamagedStockFromDate').val()+'&ToDate='+$('#DamagedStockToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function getDamagedStockInfoJson(EditID){
	
	if (EditID != ""){
		$.ajax({
		    url: "inventory/GetDamagedStockInfoJson",
		    data: {
		    	EditID: EditID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					
					$("#DamagedStockRemarks").val(json.Remarks);
					
					//alert('rows length = '+json.rows.length);
					if( json.isTodaysVoucher == 'false' ){
						$('#DamagedStockSave').addClass('ui-disabled');
						isAddable = false;
					}else{
						$('#DamagedStockSave').removeClass('ui-disabled');
						isAddable = true;
					}
					
					
					
					$("#DamagedStockMainForm").css("visibility","visible");
					
					for( var i = 0; i < json.rows.length; i++ ){
						var content = ""+
							"<tr id='DamagedStock"+json.rows[i].ProductID+"'>"+
								"<td>"+json.rows[i].SAPCode+"<input type='hidden' name='DamagedStockMainFormUnitPerSKU' value='"+json.rows[i].UnitPerSKU+"'></td>"+	
								"<td>"+json.rows[i].PackageName+"<input type='hidden' name='DamagedStockMainFormLiquidInML' value='"+json.rows[i].LiquidInML+"'></td>"+						
								"<td>"+json.rows[i].BrandName+"<input type='hidden' name='ProductCode' value='"+json.rows[i].SAPCode+"'><input type='hidden' name='ProductID' value='"+json.rows[i].ProductID+"'></td>"+
								"<td>"+json.rows[i].RawCases+"<input type='hidden' name='DamagedStockMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
								"<td>"+json.rows[i].Units+"<input type='hidden' name='DamagedStockMainFormUnits' value='"+json.rows[i].Units+"'></td>"+
								"<td>"+json.rows[i].BatchCode+"<input type='hidden' name='DamagedStockMainFormBatchCode' value='"+json.rows[i].BatchCode+"'></td>"+
								"<td>"+json.rows[i].DamageTypeName+"<input type='hidden' name='DamagedStockMainFormType' value='"+json.rows[i].TypeID+"'></td>"+
								"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DamagedStockDeleteRow('DamagedStock"+json.rows[i].ProductID+"')\">Delete</a></td>"+
							"</tr>";
							
						$("#DamagedStockTableBody").append(content).trigger('create');
						
						RowCount++;
						$('#NoProductRow').css('display', 'none');
						
	
					}
					
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
					$('#DamagedStockType').html(json.DamageStockTypeOptions);
					$('#DamagedStockType').refresh();
					
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