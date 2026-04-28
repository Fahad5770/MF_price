
var RowCount = 0;
var Shell250MLCount = 0;
var Shell1000MLCount = 0;
var isAddable = true;
var SAPPackagesArray = new Array();
 

$( document ).delegate("#DeliveryNote", "pageshow", function() {
	//document.getElementById('DeliveryNoteSAPOrderNo').focus();
	
	
	//showSearchContent();
	
});

$( document ).delegate("#DeliveryNote", "pageinit", function() {
	
	$('#DeliveryNoteProductCode').on('dblclick', function(e, data){
		
		$( "#LookupProductSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupProductInit();
		} );
		$('#LookupProductSearch').popup("open");
				  
	});	
	
	setTimeout('$("#DeliveryNotePaymentMethod").change(function(event){	DeliveryNoteUpdateChangeStatus(); });', 1000);
	
	$('#DeliveryNoteProductCode').on("dblclick", function(e, data){
		$('#autocomplete_product').html('');
		$( "#popupSearch" ).popup( "open", { positionTo: "#DeliveryNoteProductCode" } );
		$( "#popupSearch input" ).focus();
	});
	
	$( "#autocomplete_product" ).on( "listviewbeforefilter", function ( e, data ) {
        var $ul = $( this ),
            $input = $( data.input ),
            value = $input.val(),
            html = "";
        $ul.html( "" );
        if ( value && value.length > 1 ) {
            $ul.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
            $ul.listview( "refresh" );
            $.ajax({
                url: "common/GetProductBySearchJSON",
                dataType: "jsonp",
                crossDomain: true,
                data: {
                    q: $input.val()
                }
            
            })
            .then( function ( response ) {
            	
                $.each( response.rows, function ( i, val ) {
                    html += "<li><a href='#' data-ajax='false' onclick=\"setProduct("+val.SapCode+")\">" + val.PackageName + " " + val.BrandName + " ("+val.SapCode+")</a></li>";
                });
                $ul.html( html );
                $ul.listview( "refresh" );
                $ul.trigger( "updatelayout");
                
                
            });
        }
    });
	
	$("#FromDateSpan").click( function( event ) {
		document.getElementById("DeliveryNoteFromDate").disabled = false;
		document.getElementById("DeliveryNoteToDate").disabled = false;
		$("#DeliveryNoteFromDate").focus();
		
	});	
	$("#ToDateSpan").on( "click", function( event, ui ) {
		document.getElementById("DeliveryNoteFromDate").disabled = false;
		document.getElementById("DeliveryNoteToDate").disabled = false;
		$("#DeliveryNoteToDate").focus();
	});	

});
 
function searchProductCode(){
	
	if( $('#PackageSearch').val() != "" && $('#BrandSearch').val() != "" ){
		
		$.ajax({
		    url: "common/GetProductBySearchJSON",
		    data: {
		    	PackageID: $('#PackageSearch').val(),
		    	BrandID: $('#BrandSearch').val()
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		
		    		$('#DeliveryNoteProductCode').val(json.SapCode);
		    		$( "#popupSearch" ).popup( "close" );
		    		getProductInfoJson(json.SapCode); 
		    		
		    		
		    		setTimeout("$('#DeliveryNoteRawCases').focus();", 100);
		    	}else{
		    		//alert('Server could not be reached.');
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
}

function setProduct(SapCode){
	$('#DeliveryNoteProductCode').val(SapCode);
	$('#autocomplete_product').html('');
	$( "#popupSearch" ).popup( "close" );
	getProductInfoJson(SapCode); 
}

function populateBrand(){
	if( $('#PackageSearch').val() != "" ){
		$.ajax({
		    url: "common/GetBrandListJson",
		    data: {
		    	PackageID: $('#PackageSearch').val()
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		var str = "<option value=''>Select Brand</option>";
		    		for(var i = 0; i < json.rows.length; i++){
		    			str += "<option value='"+json.rows[i].id+"'>"+json.rows[i].label+"</option>";
		    		}
		    		$('#BrandSearch').html(str).change();
		    		
		    	}else{
		    		//alert('Server could not be reached.');
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
}

function DeliveryNotePopulateShellStatus(val){
	if(val == 1){
		$('#ShellStatusTable').css('display', 'block');
	}else{
		$('#ShellStatusTable').css('display', 'none');
	}
}

function DeliveryNoteUpdateChangeStatus(){
	var ProductCodeArray = document.getElementsByName("ProductCode");
	if(isAddable == true && ProductCodeArray.length > 0){
		$('#DeliveryNoteSave').removeClass('ui-disabled');
	}
	
	if( $('#DeliveryNoteVehicleType').val() == 2 ){
		$('#DeliveryNoteFreightAmount').removeClass('ui-disabled');
		$('#DeliveryNoteFreightContractorID').removeClass('ui-disabled').change();
	}else{
		$('#DeliveryNoteFreightAmount').val("");
		$('#DeliveryNoteFreightContractorID').val("");
		$('#DeliveryNoteFreightAmount').addClass('ui-disabled');
		$('#DeliveryNoteFreightContractorID').addClass('ui-disabled').change();
	}
	
}

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}


function DeliveryNoteSetDistributor(DistributorID){
	
	if(isInteger(DistributorID) == false){
		$("#DeliveryNoteDistributorName").html("ID must be an integer value");
		document.getElementById('DeliveryNoteDistributorID').focus();
		return false;
	}
		if(isInteger(DistributorID) == false){
		$("#DeliveryNoteDistributorName3").html("ID must be an integer value");
		document.getElementById('DeliveryNoteDistributorID4').focus();
		return false;
	}
	
	if (DistributorID != ""){
		$.ajax({
		    url: "common/GetDistributorInfoJson",
		    data: {
		    	DistributorID: DistributorID,
		    	DistributorID: DistributorID3,
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		$("#DeliveryNoteDistributorName").html(json.DistributorName);
		    		$("#DeliveryNoteDistributorName3").html(json.DistributorName3);
		    		//$("#SamplingReceiving #DistributorID").val(DistributorID);
		    	}else{
		    		$("#DeliveryNoteDistributorName").html("Invalid ID");
		    		$('#DeliveryNoteDistributorID').val('');
		    			$('#DeliveryNoteDistributorID4').val('');
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}

function DeliveryNoteRollBack(EditID){
	
	if (EditID != ""){
		$.ajax({
		    url: "inventory/DeliveryNoteRollBack",
		    data: {
		    	EditID: EditID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		
		    		window.location='DeliveryNote.jsp';
		    		
		    	}else{
		    		alert("Server could not be reached.from server");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}

function DeliveryNoteDeleteRow(RowID){
	
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#DeliveryNoteSave').addClass('ui-disabled');
	}
	DeliveryNoteUpdateProductStatistics();
	
}



function DeliveryNoteAddProduct(){
	
	
	
	
	if( $('#DeliveryNoteProductCode').val() == "" ){
		document.getElementById('DeliveryNoteProductCode').focus();
		return false;
	}else{
		var value = $('#DeliveryNoteProductCode').val();
		if(isInteger(value) == false){
			document.getElementById('DeliveryNoteProductCode').focus();
			return false;
		}
	}
	
	if( ( $('#DeliveryNoteRawCases').val() == "" || $('#DeliveryNoteRawCases').val() == "0" ) && ( $('#DeliveryNoteUnits').val() == "" || $('#DeliveryNoteUnits').val() == "0" ) ){
		document.getElementById('DeliveryNoteRawCases').focus();
		return false;
	}else{
		var value_raw_cases = $('#DeliveryNoteRawCases').val();
		if(value_raw_cases!= "" && isInteger(value_raw_cases) == false){
			document.getElementById('DeliveryNoteRawCases').focus();
			return false;
		}
		
		var value_units = $('#DeliveryNoteUnits').val();
		if(value_units != "" && isInteger(value_units) == false){
			document.getElementById('DeliveryNoteUnits').focus();
			return false;
		}
		
	}
	
	
	
	
	
	$('#DeliveryNoteMainFormSAPOrderNo').val($('#DeliveryNoteSAPOrderNo').val());
	$('#DeliveryNoteMainFormVehicleNo').val($('#DeliveryNoteVehicleNo').val());
	
	$('#DeliveryNoteMainFormRemarks').val($('#DeliveryNoteRemarks').val());
	
	$("#DeliveryNoteMainForm").css("visibility","visible");

	var val = $('#DeliveryNoteProductCode').val();
	
	//barcode check
	
	if($("#DeliveryNoteBatchCode").val()!=""){ //if someone enters barcode
		if($("#DeliveryNoteBatchCode").val().length<8){
			alert("Batch Code should be 8 characters long");
			return false;
		}
	}
	
	
	
	
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
		    		
		    		/*if(verifyPackage(json.PackageID) == false ){
		    			alert('Package is not listed.');
		    			document.getElementById('DeliveryNoteProductCode').focus();
		    			return false;
		    		}*/
		    		
		    		var ValDeliveryNoteRawCases = $('#DeliveryNoteRawCases').val();
	    			if( ValDeliveryNoteRawCases == '' ){
	    				ValDeliveryNoteRawCases = '0';
	    			}
					
					var ShellType = '';
					
					if( json.ShellProductID == "109" ){
						ShellType = '250';
						Shell250MLCount += parseInt(ValDeliveryNoteRawCases); 
					}
					
					if( json.ShellProductID == "110" ){
						ShellType = '1000';
						Shell1000MLCount += parseInt(ValDeliveryNoteRawCases); 
					}
					
		    		var isAlreadyEntered = false;
		    		
		    		/*var enteredBatchCode = $('#DeliveryNoteBatchCode').val();
		    		//alert('enteredBatchCode = '+enteredBatchCode);
		    		
		    		var existing = document.getElementsByName("ProductCode");
		    		var existingBatchCode = document.getElementsByName("DeliveryNoteMainFormBatchCode");
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
		    		*/
		    		if (isAlreadyEntered != true){
		    			
		    			var ValDeliveryNoteUnits = $('#DeliveryNoteUnits').val();
		    			if( ValDeliveryNoteUnits == '' ){
		    				ValDeliveryNoteUnits = '0';
		    			}
		    			
		    			
		    			/*if( parseInt(ValDeliveryNoteUnits) >= parseInt(json.UnitPerSKU) ){
		    				alert('Bottles should be less than units per SKU');
		    				document.getElementById('DeliveryNoteUnits').focus();
		    				return false;
		    			}*/
		    			
		    			var OldOrderedQuantity = $('#package_ordrd_'+json.PackageID).html();
		    			if(OldOrderedQuantity == null || parseInt(OldOrderedQuantity) == NaN){
		    				OldOrderedQuantity = '0';
		    			}
		    			var OldOrderedRawCases = OldOrderedQuantity.split('/')[0];
		    			var OldOrderedUnits = OldOrderedQuantity.split('/')[1];
		    			
		    			if(OldOrderedUnits == null || parseInt(OldOrderedUnits) == NaN){
		    				OldOrderedUnits = 0;
		    			}
		    			
		    			var OldQuantity = $('#package_dlvry_'+json.PackageID).html();
		    			if(OldQuantity == null || parseInt(OldQuantity) == NaN){
		    				OldQuantity = '0';
		    			}
		    			var OldRawCases = OldQuantity.split('/')[0];
		    			var OldUnits = OldQuantity.split('/')[1];
		    			
		    			if(OldUnits == null || parseInt(OldUnits) == NaN){
		    				OldUnits = 0;
		    			}
		    			
		    			var TotalRawCases = parseInt(ValDeliveryNoteRawCases) + parseInt(OldRawCases);
		    			var TotalUnits = parseInt(ValDeliveryNoteUnits) + parseInt(OldUnits);
		    			
		    			//alert(ValDeliveryNoteUnits+ ", " + OldUnits);
		    			
		    			/*if(( parseInt(TotalRawCases) > parseInt(OldOrderedRawCases) ) || ( parseInt(TotalUnits) > parseInt(OldOrderedUnits) )){
		    				alert('Delivery Quantity should not be greater than Ordered Quantity');
		    				document.getElementById('DeliveryNoteProductCode').focus();
		    				return false;
		    			}
		    			*/
		    			/*if($('#DeliveryNoteIsPartial').val() == 0){
		    				if(( parseInt(TotalRawCases) != parseInt(OldOrderedRawCases) ) || ( parseInt(TotalUnits) != parseInt(OldOrderedUnits) )){
			    				alert('Quantity must be equal in Full Delivery');
			    				document.getElementById('DeliveryNoteProductCode').focus();
			    				return false;
			    			}
		    			}*/
		    			
		    			
		    			var UnitFormatter = '';
		    			if(TotalUnits > 0){
		    				UnitFormatter = '/'+TotalUnits;
		    			}
		    			$('#package_dlvry_'+json.PackageID).html(TotalRawCases+''+UnitFormatter);
		    			
		    			var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
		    			
				    	var content = ""+
						"<tr id='DeliveryNote"+RowMaxID+"'>"+
							"<td>"+$('#DeliveryNoteProductCode').val()+"<input type='hidden' name='DeliveryNoteMainFormUnitPerSKU' value='"+json.UnitPerSKU+"'></td>"+	
							"<td>"+json.PackageLabel+"<input type='hidden' name='PackageID' value='"+json.PackageID+"' ><input type='hidden' name='DeliveryNoteMainFormLiquidInML' value='"+json.LiquidInML+"'></td>"+						
							"<td>"+json.BrandLabel+"<input type='hidden' id='ProductCode' name='ProductCode' value='"+val+"'><input type='hidden' name='ProductID' value='"+json.ProductID+"'></td>"+
							"<td>"+ValDeliveryNoteRawCases+"<input type='hidden' name='DeliveryNoteMainFormRawCases' value='"+ValDeliveryNoteRawCases+"'></td>"+
							"<td>"+ValDeliveryNoteUnits+"<input type='hidden' name='DeliveryNoteMainFormUnits' value='"+ValDeliveryNoteUnits+"'></td>"+
							"<td>"+$('#DeliveryNoteBatchCode').val()+"<input type='hidden' name='DeliveryNoteMainFormBatchCode' value='"+$('#DeliveryNoteBatchCode').val()+"'></td>"+
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeliveryNoteDeleteRow('DeliveryNote"+RowMaxID+"'); UpdatePackageDeliveryQuantities("+json.PackageID+", "+ValDeliveryNoteRawCases+", "+ValDeliveryNoteUnits+"); UpdateShellCounter('"+ValDeliveryNoteRawCases+"', '"+ShellType+"')\">Delete</a></td>"+
						"</tr>";
						
						$("#DeliveryNoteTableBody").append(content).trigger('create');
						
						RowCount++;
						
						$('#NoProductRow').css('display', 'none');
						
						$('#DeliveryNoteProductCode').val('');
						$('#DeliveryNoteRawCases').val('');
						$('#DeliveryNoteUnits').val('');
						$('#DeliveryNoteBatchCode').val('');
						$('#ProductInfoSpan').html('');
						
						$('#250MLShellQuantity').html(Shell250MLCount);
						$('#1000MLShellQuantity').html(Shell1000MLCount);
						
						$('#DeliveryNoteProductCode').focus();
						$('#DeliveryNoteSave').removeClass('ui-disabled');
						
						$('#RowMaxID').val(RowMaxID);
						
						
						DeliveryNoteUpdateProductStatistics();
					
		    		}else{
		    			document.getElementById('DeliveryNoteProductCode').focus();
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

function DeliveryNoteUpdateProductStatistics(){
	$.ajax({
	    url: "inventory/GetDeilveryNoteProductStatistics",
	    data: $("#DeliveryNoteMainForm" ).serialize(),
	    type: "POST",
	    dataType : "html",
	    success: function( html ) {
			
	    	//alert(html);
	    	$('#ProductStatistics').html(html);
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function UpdatePackageDeliveryQuantities(PackageID, RawCases, Units){
	
	var OldQuantity = $('#package_dlvry_'+PackageID).html();
	var OldRawCases = OldQuantity.split('/')[0];
	var OldUnits = OldQuantity.split('/')[1];
	
	var UpdatedRawCases = parseInt(OldRawCases) - parseInt(RawCases);
	var UpdatedUnits = parseInt(OldUnits) - parseInt(Units);
	
	var UnitFormatter = '';	
	if(UpdatedUnits > 0){
		UnitFormatter = '/'+UpdatedUnits;
	}
	
	 $('#package_dlvry_'+PackageID).html(UpdatedRawCases+UnitFormatter);
}

function verifyPackage(PackageID){
	
	for(var i = 0; i < SAPPackagesArray.length; i++){
		if( SAPPackagesArray[i] == PackageID ){
			return true;
		}
	}
	return false;
}

function SetProductInfo( ShellCode, RawCases ){
	$('#DeliveryNoteProductCode').val(ShellCode);
	$('#DeliveryNoteRawCases').val(RawCases);
	$('#DeliveryNoteRawCases').focus();
	$('#DeliveryNoteUnits').attr('disabled', 'disabled');
	$('#DeliveryNoteBatchCode').attr('disabled', 'disabled');
	getProductInfoJson(ShellCode);
}

function UpdateShellCounter( RawCases, ShellType ){
	
	if( ShellType != "" && ShellType == "250" ){
		Shell250MLCount = Shell250MLCount - parseInt(RawCases);
		$('#250MLShellQuantity').html(Shell250MLCount);
	}else if( ShellType != "" && ShellType == "1000" ){
		Shell1000MLCount = Shell1000MLCount - parseInt(RawCases);
		$('#1000MLShellQuantity').html(Shell1000MLCount);
	}
}

function DeliveryNoteSubmit1(){
	
	
	
	
	if( $('#DeliveryNoteDistributorID2').val() == "" ){
		document.getElementById('DeliveryNoteDistributorID2').focus();
		return false;
	}
	
	if( $('#DeliveryNoteDistributorID3').val() == "" ){
		println("Test=============>"+document.getElementById('DeliveryNoteDistributorID3'));
		document.getElementById('DeliveryNoteDistributorID3').focus();
		return false;
	}
	
	
	//Commented on 3rd of April 2019
	
	/*if($("#rawcases1").val()==""){
		document.getElementById('rawcases1').focus();
		return false;
	}
	
	if($("#bottles1").val()==""){
		document.getElementById('bottles1').focus();
		return false;
	}
	
	if($("#batchcode1").val()==""){
		document.getElementById('batchcode1').focus();
		return false;
	}*/
	
	//Added on 3rd of April 2019 start
	
	var EntriesCounter=0;
	
	var NoOfRows=$("#NumberOFRows").val();
	
	for(var j=1;j<NoOfRows;j++){
		
		var checkifRawCaesFilled=0;
		var checkifBottlesFilled=0;
		var checkifBatchFilled=0;
		
		
		//getting the values of All fields in particluar ROW
		if($("#rawcases"+j).val()!=""){
			
			checkifRawCaesFilled=1;  //getting the values of RawCases in particluar ROW
		}
		
		if($("#bottles"+j).val()!=""){
			checkifBottlesFilled=1;  //getting the values of Bottles in particluar ROW
		}
		
		if($("#batchcode"+j).val()!=""){
			checkifBatchFilled=1;  //getting the values of BatchCode in particluar ROW
		}
		
		
		 
		var isValidEntry=0;
		//checking the values of Bottles & BatchCode whether if Rawcase is filled in particluar ROW
		if(checkifRawCaesFilled==1){
			if(checkifBottlesFilled==1 && checkifBatchFilled==1){
				isValidEntry++;
				
			}else {
				if(checkifBottlesFilled==0){
					document.getElementById('bottles'+j).focus();
					return false;
				}else if(checkifBatchFilled==0){
					document.getElementById('batchcode'+j).focus();
					return false;
				}
				
				
			}
			
			
		}
		
		//checking the values of Rawcase & BatchCode whether if  Bottles is filled in particluar ROW
		if(checkifBottlesFilled==1){
			if(checkifRawCaesFilled==1 && checkifBatchFilled==1){
				isValidEntry++;
				
			}else {
				if(checkifRawCaesFilled==0){
					document.getElementById('rawcases'+j).focus();
					return false;
				}else if(checkifBatchFilled==0){
					document.getElementById('batchcode'+j).focus();
					return false;
				}
				
				
			}
			
		}
		
		//checking the values of Rawcase & Bottles whether if  batchcode is filled in particluar ROW
		if(checkifBatchFilled==1){
			if(checkifRawCaesFilled==1 && checkifBottlesFilled==1){
				isValidEntry++;
				
			}else {
				if(checkifRawCaesFilled==0){
					document.getElementById('rawcases'+j).focus();
					return false;
				}else if(checkifBottlesFilled==0){
					document.getElementById('bottles'+j).focus();
					return false;
				}
				
				
			}
			
			
		}
		
		
		//if  isValidEntry==3 then consider the row fine
		if(isValidEntry==3){
			EntriesCounter++;
		}
		
		
	}
	
	//if EntriesCounter>3 then allow for further processing
	if(EntriesCounter>0){
	
	//Added on 3rd of April 2019 End
	
	
	var isShell250MLAdded = false;
	var isShell1000MLAdded = false;
	
	/*if(Shell250MLCount > 0){
		var ProductCodeArray = $("input[name=ProductCode]");
		for(var i = 0 ; i < ProductCodeArray.length; i++){
			if( ProductCodeArray[i].value == 5012 ){
				isShell250MLAdded = true;
			}
		}
		
		if(isShell250MLAdded==false){
			alert("Please enter 250ML Shell");
			$('#DeliveryNoteProductCode').focus();
			return false;
		}
		
	}*/
	
	/*if(Shell1000MLCount > 0){
		var ProductCodeArray = $("input[name=ProductCode]");
		for(var i = 0 ; i < ProductCodeArray.length; i++){
			if( ProductCodeArray[i].value == 5014 ){
				isShell1000MLAdded = true;
			}
		}
		
		if(isShell1000MLAdded==false){
			alert("Please enter 1000ML Shell");
			$('#DeliveryNoteProductCode').focus();
			return false;
		} 
		
	}*/
	  
	//////////////////////////////////////////////////////////////////////////////////////
	
		var IsPartialVal = $('#DeliveryNoteIsPartial').val();
		/*if( IsPartialVal == 0 ){
			for(i = 0; i < SAPPackagesArray.length; i++){
				
				var OrderedQty = $('#package_ordrd_'+SAPPackagesArray[i]).html();
				var OrderedRawCases = OrderedQty.split('/')[0];
				var OrderedUnits = OrderedQty.split('/')[1];
				
				var DeliveryQty = $('#package_dlvry_'+SAPPackagesArray[i]).html();
				var DeliveryRawCases = DeliveryQty.split('/')[0];
				var DeliveryUnits = DeliveryQty.split('/')[1];
				
				if( (OrderedRawCases != DeliveryRawCases) || (OrderedUnits != DeliveryUnits) ){
					alert("Quantity must be equal in Full Delivery");
					document.getElementById('DeliveryNoteProductCode').focus();
					return false;
				}
				
			}
		}*/
		
		//////////////////////////////////////////////////////////////////////////////////////
		
		
		$("#DeliveryNoteDistributorID").val($("#DeliveryNoteDistributorID2").val());
		$("#DeliveryNoteVehicleNo").val($("#DeliveryNoteVehicleNo2").val());
		$("#DeliveryNoteRemarks").val($("#DeliveryNoteRemarks2").val());
			$("#DeliveryNoteDistributorID4").val($("#DeliveryNoteDistributorID3").val());
		//alert("Going...");
		
		$('#DeliveryNoteSave').addClass("ui-disabled");
		$.ajax({
		    url: "inventory/StockTransferExecute",
		    
		    data: $("#DeliveryNoteMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
			alert(json.error);
		    		location = "StockTransferNote.jsp?DeliveryIDPrint="+json.DeliveryID;
		    	}else{
					alert(json.error);
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	
	}else {
		alert("Please Enter atleast one Record!.");
	}
	
}

function showSearchContent(){
	
	$("#SearchContent").focus();
	
	document.getElementById("DeliveryNoteFromDate").disabled = "disabled";
	document.getElementById("DeliveryNoteToDate").disabled = "disabled";
	
	var WarehouseID = "";
	
	if($('#DeliveryNoteWarehouseID').val() != ""){
		WarehouseID = $('#DeliveryNoteWarehouseID').val();
	}
	
	
	//DeliveryNoteWarehouseID
	
	$.get('DeliveryNoteSearch.jsp?FromDate='+$('#DeliveryNoteFromDate').val()+'&ToDate='+$('#DeliveryNoteToDate').val()+'&DeliveryNoteWarehouseID='+WarehouseID, function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function getDeliveryNoteInfoJson(EditID){
	
	if (EditID != ""){
		$.ajax({
		    url: "inventory/GetDeliveryNoteInfoJson",
		    data: {
		    	EditID: EditID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					
		    		$('#DeliveryNotePrint').removeClass('ui-disabled');
		    		
		    		$('#DeliveryNoteInvoiceNo').val(json.InvoiceNo);
		    		
		    		$("#DeliveryNoteIsPartial").val(json.IsPartial);
		    		$("#DeliveryNoteIsPartial").change();
		    		
		    		$("#DeliveryNoteDistributorID").val(json.DistributorID+"-"+json.DistributorName);
		    		$("#DeliveryNoteDistributorID4").val(json.DistributorID+"-"+json.DistributorName3);
					//$("#DeliveryNoteDistributorName").html(json.DistributorName);
					
					$("#DeliveryNotePaymentMethod").val(json.PaymentMethod);
					
					$('#DeliveryNotePaymentMethod').change();
					
		    		$("#DeliveryNoteSAPOrderNo").val(json.SAPOrderNo);
		    		
		    		$("#DeliveryNoteSAPOrderNo").attr('readonly', true);
		    		
					$("#DeliveryNoteVehicleNo").val(json.VehicleNo);
					
					$("#DeliveryNoteVehicleType").val(json.VehicleType);
					$("#DeliveryNoteVehicleType").change();
					
					$("#DeliveryNoteRemarks").val(json.Remarks);
					
					
					//if(json.PalletizeID!=""){
						$("#DeliveryNoteIsPalatize").val(json.PalletizeID);
					//}else{
						//$("#DeliveryNoteIsPalatize").val(3);
					//}
					
					
					
					
					if( json.IsDelivered == "1" ){
						$('#DeliveryNoteSave').addClass('ui-disabled');
						$('#DeliveryNoteRollBack').removeClass('ui-disabled');
						isAddable = false;
					}else{
						if( json.isTodaysVoucher == 'false' ){
							$('#DeliveryNoteSave').addClass('ui-disabled');
							isAddable = false;
						}else{
							//$('#DeliveryNoteSave').removeClass('ui-disabled');
							isAddable = true;
						}
					}
					
					//alert('rows length = '+json.rows.length);
					
					$("#DeliveryNoteMainForm").css("visibility","visible");
					
					for( var i = 0; i < json.rows.length; i++ ){
						var ShellType = '';
						
						if( json.rows[i].ShellProductID == "109" ){
							ShellType = '250';
							Shell250MLCount += parseInt(json.rows[i].RawCases);
							$('#250MLShellQuantity').html(Shell250MLCount);
						}
						if( json.rows[i].ShellProductID == "110" ){
							ShellType = '1000';
							Shell1000MLCount += parseInt(json.rows[i].RawCases);
							$('#1000MLShellQuantity').html(Shell1000MLCount);
						}
						
						var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
						
						var content = ""+
							"<tr id='DeliveryNote"+RowMaxID+"'>"+
								"<td>"+json.rows[i].SAPCode+"<input type='hidden' name='DeliveryNoteMainFormUnitPerSKU' value='"+json.rows[i].UnitPerSKU+"'></td>"+	
								"<td>"+json.rows[i].PackageName+"<input type='hidden' name='PackageID' value='"+json.rows[i].PackageID+"' ><input type='hidden' name='DeliveryNoteMainFormLiquidInML' value='"+json.rows[i].LiquidInML+"'></td>"+						
								"<td>"+json.rows[i].BrandName+"<input type='hidden' id='ProductCode' name='ProductCode' value='"+json.rows[i].SAPCode+"'><input type='hidden' name='ProductID' value='"+json.rows[i].ProductID+"'></td>"+
								"<td>"+json.rows[i].RawCases+"<input type='hidden' name='DeliveryNoteMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
								"<td>"+json.rows[i].Units+"<input type='hidden' name='DeliveryNoteMainFormUnits' value='"+json.rows[i].Units+"'></td>"+
								"<td>"+json.rows[i].BatchCode+"<input type='hidden' name='DeliveryNoteMainFormBatchCode' value='"+json.rows[i].BatchCode+"'></td>"+
								"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeliveryNoteDeleteRow('DeliveryNote"+RowMaxID+"'); UpdatePackageDeliveryQuantities("+json.rows[i].PackageID+", "+json.rows[i].RawCases+", "+json.rows[i].Units+"); UpdateShellCounter('"+json.rows[i].RawCases+"', '"+ShellType+"')\">Delete</a></td>"+
							"</tr>";
							
						$("#DeliveryNoteTableBody").append(content).trigger('create');
						//$('#DeliveryNoteSave').removeClass('ui-disabled');
						RowCount++;
						$('#NoProductRow').css('display', 'none');
						$('#RowMaxID').val(RowMaxID);
						
						
	
					}
					DeliveryNoteUpdateProductStatistics();
					DeliveryNoteGetSaleOrderStatus(json.SAPOrderNo, true);
					
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

function DeliveryNoteGetSaleOrderStatus(SaleOrderNo, isEditCase){
	//alert("SaleOrderNo = "+SaleOrderNo);
	$.mobile.showPageLoadingMsg();	
	$.ajax({
	    url: "sap/GetSalesOrderStatusJson",
	    data: {
	    	SaleOrderNo: SaleOrderNo,
	    	isEditCase : isEditCase
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) { 
	    	
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	var TableContent = '<table data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">';
	    	TableContent += '<thead>';
	    	TableContent += '<tr class="ui-bar-c">';
	    	TableContent += '<th data-priority="1" style="width:10%">Package</th>';
	    	TableContent += '<th data-priority="1" style="width:10%; text-align:right">Ordered Quantity</th>';
	    	TableContent += '<th data-priority="1" style="width:10%; text-align:right">Delivery Quantity</th>';
	    	TableContent += '</tr>';
	    	TableContent += '</thead>';
	    	
	    	
	    	if(json.error == '1'){
	    		alert('Failed to connect SAP server.');
	    		document.getElementById("DeliveryNoteSAPOrderNo").focus();
	    		return false;
	    	}else if(json.error == '2'){
	    		alert('Sale Order already exists.');
	    		document.getElementById("DeliveryNoteSAPOrderNo").focus();
	    		return false;
	    	}else{
	    		
	    		if (json.exists == "true"){
					
	    			if(json.InvoiceNo == "0"){
	    				alert("Invoice does not exists against this order.");
	    				return false;
	    			}
	    			
	    			$('#DeliveryNoteInvoiceNo').val(json.InvoiceNo);
	    			$('#DeliveryNoteInvoiceAmount').val(json.InvoiceAmount);
	    			//alert($('#DeliveryNoteInvoiceAmount').val());
	    			
	    			$('#DeliveryNoteDistributorID').val(json.DistrubutorName);
	    			$('#DeliveryNoteDistributorID4').val(json.DistrubutorName);
    				
    				$('#DeliveryNoteMainFormDistributorID').val(json.DistrubutorID);
	    			
	    			if(!isEditCase){
	    				
	    				var NoProductContentStr = '<tr id="NoProductRow"><td colspan="7" style="margin: 1px; padding: 0px;">';
	    				NoProductContentStr += '<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div></td></tr>';
	    				
	    				$("#DeliveryNoteTableBody").html(NoProductContentStr);
	    				
	    				$('#250MLShellQuantity').html('0');
			    		$('#1000MLShellQuantity').html('0');
			    		Shell250MLCount = 0;
		    			Shell1000MLCount = 0;
			    		SAPPackagesArray = [];
	    			}
	    			
					for(var i = 0; i < json.rows.length; i++){
						
						//if(json.rows[i].CategoryID == 1){
							TableContent += '<tr>';
							
							TableContent += '<td>'+json.rows[i].PackageName+'</td>';
							
							var QuantityUnits = '';
							if(json.rows[i].QuantityUnits > 0){
								QuantityUnits = '/'+json.rows[i].QuantityUnits;
							}
							
							
							SAPPackagesArray[i] = json.rows[i].PackageID;
							var ShellVal = 0;
							if(json.rows[i].CategoryID == 3 && isEditCase != true){
								ShellVal = json.rows[i].QuantityRawCases;
							}
							
							TableContent += '<td style="text-align:right" id="package_ordrd_'+json.rows[i].PackageID+'">'+json.rows[i].QuantityRawCases + QuantityUnits+'</td>';
							TableContent += '<td style="text-align:right" id="package_dlvry_'+json.rows[i].PackageID+'">'+ShellVal+'</td>';
							
							TableContent += '</tr>';
						//}else{
							if(json.rows[i].CategoryID == 3){
								if(!isEditCase){
									
									var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
									
									var content = ""+
										"<tr id='DeliveryNote"+RowMaxID+"'>"+
											"<td>"+json.rows[i].ProductSAPCode+"<input type='hidden' name='DeliveryNoteMainFormUnitPerSKU' value='1'></td>"+	
											"<td>"+json.rows[i].PackageName+"<input type='hidden' name='PackageID' value='"+json.rows[i].PackageID+"' ><input type='hidden' name='DeliveryNoteMainFormLiquidInML' value='0'></td>"+						
											"<td>"+json.rows[i].BrandName+"<input type='hidden' id='ProductCode' name='ProductCode' value='"+json.rows[i].ProductSAPCode+"'><input type='hidden' name='ProductID' value='"+json.rows[i].ProductID+"'></td>"+
											"<td>"+json.rows[i].QuantityRawCases+"<input type='hidden' name='DeliveryNoteMainFormRawCases' value='"+json.rows[i].QuantityRawCases+"'></td>"+
											"<td>"+json.rows[i].QuantityUnits+"<input type='hidden' name='DeliveryNoteMainFormUnits' value='"+json.rows[i].QuantityUnits+"'></td>"+
											"<td>&nbsp;<input type='hidden' name='DeliveryNoteMainFormBatchCode' value=''></td>"+
											"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeliveryNoteDeleteRow('DeliveryNote"+RowMaxID+"'); UpdatePackageDeliveryQuantities("+json.rows[i].PackageID+", "+json.rows[i].QuantityRawCases+", 0); UpdateShellCounter('"+json.rows[i].QuantityRawCases+"', '')\">Delete</a></td>"+
										"</tr>";
										
									$("#DeliveryNoteTableBody").append(content).trigger('create');
									RowCount++;
									$('#NoProductRow').css('display', 'none');
									
									
									$('#RowMaxID').val(RowMaxID);
								}
							}
							
						//}// end else
						
					}
					
					TableContent += ' </table>';
					$('#DeliveryNotePackageStatusTable').html(TableContent);
					//$('#DeliveryNotePackageStatusTable').refresh();
					//$('#ProductInfoSpan').html(json.PackageLabel+" - "+json.BrandLabel);
					
					if(isEditCase){
						
						var PackageID = document.getElementsByName("PackageID");
	    				var FormRawCases = document.getElementsByName("DeliveryNoteMainFormRawCases");
	    				var FormUnits = document.getElementsByName("DeliveryNoteMainFormUnits");
			    		
			    		for (x = 0; x < PackageID.length; x++){
			    			
			    			var OldQty = $('#package_dlvry_'+PackageID[x].value).html();
			    			
			    			if(OldQty != null){
			    				//alert('package_dlvry_'+PackageID[x].value+' = '+OldQty);
			    				
				    			var OldRawCases = OldQty.split('/')[0];
				    			var OldUnits = OldQty.split('/')[1];
				    			
				    			if(OldUnits == null || parseInt(OldUnits) == NaN){
				    				OldUnits = 0;
				    			}
			    				
				    			var DBRawCases = FormRawCases[x].value;
				    			var DBUnits = FormUnits[x].value;
				    			
				    			var TotalRawCases = parseInt(DBRawCases) + parseInt(OldRawCases);
				    			var TotalUnits = parseInt(DBUnits) + parseInt(OldUnits);
				    			
				    			var UnitFormatter = '';
				    			if(TotalUnits > 0){
				    				UnitFormatter = '/'+TotalUnits;
				    			}
				    			$('#package_dlvry_'+PackageID[x].value).html(TotalRawCases+''+UnitFormatter);
				    			
			    			}else{
			    				//alert('package_dlvry_'+PackageID[x].value+' = '+OldQty+'=> null value');
			    			}
			    			
			    		}
	    				
					}
					
					DeliveryNoteUpdateProductStatistics();
					
		    	}else{
		    		alert('Invalid Sale Order Number');
		    		$.mobile.hidePageLoadingMsg();
		    		document.getElementById("DeliveryNoteSAPOrderNo").focus();
		    	}
	    		
	    	}
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.hidePageLoadingMsg();
	    	document.getElementById("DeliveryNoteSAPOrderNo").focus();
	    }
	});
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
					alert(json.PackageLabel+" "+json.BrandLabel);
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
function DeliveryNoteDelete()
{
	var r=confirm("Are you sure you want to delete?");
	if (r==true)
	  {	  
		$.ajax({
		    url: "inventory/DeilveryNoteDeleteExecute",
		    
		    data: $("#DeliveryNoteMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		location = "DeliveryNote.jsp";
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
	
	
}

function ProductSearchCallBack(SAPCode, ProductName){
	$('#DeliveryNoteProductCode').val(SAPCode);
	$('#ProductInfoSpan').html(ProductName);
	setTimeout("$('#DeliveryNoteRawCases').focus();", 100);
}