
var RowCount = 0;
var Shell250MLCount = 0;
var Shell1000MLCount = 0;
var isAddable = true;
var UnitPrice = 0;
var SAPPackagesArray = new Array();



$( document ).delegate("#DeskSale", "pageshow", function() {
	
	//showSearchContent();
	
});

$( document ).delegate("#DeskSale", "pageinit", function() {
	
	if (isEditCase == true){
		getOrderInvoicingEditInfoJson(EditID);
	}
	
	$('#DeskSaleProductCode').on('dblclick', function(e, data){
		
		$( "#LookupProductSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupProductInit();
		} );
		$('#LookupProductSearch').popup("open");
				  
	});	
	
	
	
	setTimeout('$("#DeskSalePaymentMethod").change(function(event){	DeskSaleUpdateChangeStatus(); });', 1000);
	
	$('#DeskSaleProductCode').on("dblclick", function(e, data){
		$('#autocomplete_product').html('');
		$( "#popupSearch" ).popup( "open", { positionTo: "#DeskSaleProductCode" } );
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
		document.getElementById("DeskSaleFromDate").disabled = false;
		document.getElementById("DeskSaleToDate").disabled = false;
		$("#DeskSaleFromDate").focus();
		
	});	
	$("#ToDateSpan").on( "click", function( event, ui ) {
		document.getElementById("DeskSaleFromDate").disabled = false;
		document.getElementById("DeskSaleToDate").disabled = false;
		$("#DeskSaleToDate").focus();
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
		    		
		    		$('#DeskSaleProductCode').val(json.SapCode);
		    		$( "#popupSearch" ).popup( "close" );
		    		getProductInfoJson(json.SapCode); 
		    		
		    		
		    		setTimeout("$('#DeskSaleRawCases').focus();", 100);
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
	$('#DeskSaleProductCode').val(SapCode);
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

function DeskSalePopulateShellStatus(val){
	if(val == 1){
		$('#ShellStatusTable').css('display', 'block');
	}else{
		$('#ShellStatusTable').css('display', 'none');
	}
}

function DeskSaleUpdateChangeStatus(){
	var ProductCodeArray = document.getElementsByName("ProductCode");
	if(isAddable == true && ProductCodeArray.length > 0){
		$('#DeskSaleSave').removeClass('ui-disabled');
	}
}

function isInteger (o) {
	  return ! isNaN (o-0) && o != null ;
}


function DeskSaleSetDistributor(DistributorID){
	
	if(isInteger(DistributorID) == false){
		$("#DeskSaleDistributorName").html("ID must be an integer value");
		document.getElementById('DeskSaleDistributorID').focus();
		return false;
	}
	
	
	if (DistributorID != ""){
		$.ajax({
		    url: "common/GetDistributorInfoJson",
		    data: {
		    	DistributorID: DistributorID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		$("#DeskSaleDistributorName").html(json.DistributorName);
		    		//$("#SamplingReceiving #DistributorID").val(DistributorID);
		    	}else{
		    		$("#DeskSaleDistributorName").html("Invalid ID");
		    		$('#DeskSaleDistributorID').val('');
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}

function DeskSaleRollBack(EditID){
	
	if (EditID != ""){
		$.ajax({
		    url: "inventory/DeskSaleRollBack",
		    data: {
		    	EditID: EditID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.exists == "true"){
		    		
		    		window.location='DeskSale.jsp';
		    		
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

function DeskSaleDeleteRow(RowID){
	
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#DeskSaleSave').addClass('ui-disabled');
	}
	
	updateDeskSalePromotions();
	
	setTimeout(function(){
		updateInvoiceSummary();
	}, 500);
	
	
}



function DeskSaleAddProduct(){
	
	if( $('#DeskSaleOutletID').val() == '' ){
		alert('Please select Outlet before adding products');
		document.getElementById('DeskSaleOutletID').focus();
		return false;
	}else{
		var value = $('#DeskSaleOutletID').val();
		if(isInteger(value) == false){
			document.getElementById('DeskSaleOutletID').focus();
			return false;
		}
	}
	
	if( isAddable == false ){
		return false;
	}
	
	if( $('#DeskSaleProductCode').val() == "" ){
		document.getElementById('DeskSaleProductCode').focus();
		return false;
	}else{
		var value = $('#DeskSaleProductCode').val();
		if(isInteger(value) == false){
			document.getElementById('DeskSaleProductCode').focus();
			return false;
		}
	}
	
	if( ( $('#DeskSaleRawCases').val() == "" || $('#DeskSaleRawCases').val() == "0" ) && ( $('#DeskSaleUnits').val() == "" || $('#DeskSaleUnits').val() == "0" ) ){
		document.getElementById('DeskSaleRawCases').focus();
		return false;
	}else{
		var value_raw_cases = $('#DeskSaleRawCases').val();
		if(value_raw_cases!= "" && isInteger(value_raw_cases) == false){
			document.getElementById('DeskSaleRawCases').focus();
			return false;
		}
		
		var value_units = $('#DeskSaleUnits').val();
		if(value_units != "" && isInteger(value_units) == false){
			document.getElementById('DeskSaleUnits').focus();
			return false;
		}
		
	}
	
	
	var value = $('#DeskSaleDiscount').val();
	if(isInteger(value) == false){
		document.getElementById('DeskSaleDiscount').focus();
		return false;
	}
	
	
	var ValProductCode = $('#DeskSaleProductCode').val();
	if( $('#DeskSaleBatchCode').val() == "" && !( ValProductCode == '5012' || ValProductCode == '5014' ) ){
		document.getElementById('DeskSaleBatchCode').focus();
		return false;
	}
	
	
	
	$('#DeskSaleMainFormSAPOrderNo').val($('#DeskSaleSAPOrderNo').val());
	$('#DeskSaleMainFormVehicleNo').val($('#DeskSaleVehicleNo').val());
	
	$('#DeskSaleMainFormRemarks').val($('#DeskSaleRemarks').val());
	
	$("#DeskSaleMainForm").css("visibility","visible");

	var val = $('#DeskSaleProductCode').val();
	
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
		    		
		    		var ValDeskSaleRawCases = $('#DeskSaleRawCases').val();
	    			if( ValDeskSaleRawCases == '' ){
	    				ValDeskSaleRawCases = '0';
	    			}
					
					var ShellType = '';
					
					
		    		var isAlreadyEntered = false;
		    		
		    		
		    		if (isAlreadyEntered != true){
		    			
		    			var ValDeskSaleUnits = $('#DeskSaleUnits').val();
		    			if( ValDeskSaleUnits == '' ){
		    				ValDeskSaleUnits = '0';
		    			}
		    			
		    			
		    			if( parseInt(ValDeskSaleUnits) >= parseInt(json.UnitPerSKU) ){
		    				alert('Bottles should be less than units per SKU');
		    				document.getElementById('DeskSaleUnits').focus();
		    				return false;
		    			}
		    			
		    			if( $('#DeskSaleDiscount').val() == '' ){
		    				$('#DeskSaleDiscount').val('0');
		    			}
		    			
		    			var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
		    			
				    	var content = ""+
						"<tr id='DeskSale"+RowMaxID+"'>"+
							"<td>"+$('#DeskSaleProductCode').val()+"<input type='hidden' name='DeskSaleMainFormUnitPerSKU' value='"+json.UnitPerSKU+"'></td>"+	
							"<td>"+json.PackageLabel+"<input type='hidden' name='DeskSaleMainFormPackageLabel' value='"+json.PackageLabel+"' ><input type='hidden' name='PackageID' value='"+json.PackageID+"' ><input type='hidden' name='DeskSaleMainFormLiquidInML' value='"+json.LiquidInML+"'></td>"+						
							"<td>"+json.BrandLabel+"<input type='hidden' name='DeskSaleMainFormBrandLabel' value='"+json.BrandLabel+"' ><input type='hidden' name='BrandID' value='"+json.BrandID+"' ><input type='hidden' id='ProductCode' name='ProductCode' value='"+val+"'><input type='hidden' name='ProductID' value='"+json.ProductID+"'></td>"+
							"<td>"+ValDeskSaleRawCases+"<input type='hidden' name='DeskSaleMainFormRawCases' value='"+ValDeskSaleRawCases+"'></td>"+
							"<td>"+ValDeskSaleUnits+"<input type='hidden' name='DeskSaleMainFormUnits' value='"+ValDeskSaleUnits+"'></td>"+
							"<td>"+formatInCommas($('#DeskSaleRate').val())+"<input type='hidden' name='DeskSaleMainFormRateHidden' value='"+$('#DeskSaleRate').val()+"'><input type='hidden' name='DeskSaleMainFormUnitRateHidden' value='"+$('#DeskSaleUnitRate').val()+"'></td>"+
							"<td>"+formatInCommas($('#DeskSaleAmount').val())+"<input type='hidden' id='DeskSaleMainFormAmount' name='DeskSaleMainFormAmount' value='"+$('#DeskSaleAmount').val()+"'></td>"+
							"<td>"+$('#DeskSaleDiscount').val()+"<input type='hidden' name='DeskSaleMainFormDiscount' value='"+$('#DeskSaleDiscount').val()+"'></td>"+
							"<td>"+formatInCommas($('#DeskSaleNetAmount').val())+"<input type='hidden' id='DeskSaleMainFormNetAmount' name='DeskSaleMainFormNetAmount' value='"+$('#DeskSaleNetAmount').val()+"'></td>"+
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeskSaleDeleteRow('DeskSale"+RowMaxID+"');\">Delete</a></td>"+
						"</tr>";
				    	
						$("#DeskSaleTableBody").append(content).trigger('create');
						
						RowCount++;
						
						$('#NoProductRow').css('display', 'none');
						
						$('#DeskSaleProductCode').val('');
						$('#DeskSaleRawCases').val('');
						$('#DeskSaleUnits').val('');
						$('#DeskSaleBatchCode').val('');
						$('#DeskSaleRate').val('');
						
						$('#DeskSaleAmount').val('');
						$('#DeskSaleDiscount').val('');
						$('#DeskSaleNetAmount').val('');
						
						$('#ProductInfoSpan').html('');
						
						$('#250MLShellQuantity').html(Shell250MLCount);
						$('#1000MLShellQuantity').html(Shell1000MLCount);
						
						$('#DeskSaleProductCode').focus();
						$('#DeskSaleSave').removeClass('ui-disabled');
						
						$('#RowMaxID').val(RowMaxID);
						
						$('#DeskSaleOutletID').attr('readonly', 'readonly');
						$('#DeskSaleOutletID').unbind('dblclick');
						
						$('#DeskSaleSave').addClass('ui-disabled');
						
						updateDeskSalePromotions();
						
						
						
						
					
		    		}else{
		    			document.getElementById('DeskSaleProductCode').focus();
		    		}
					
		    	}else{
		    		alert("Invalid Product Code");
					$('#DeskSaleProductCode').focus();
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

function updateDeskSalePromotions(){
	
	$.get('DeskSalePromotions.jsp?'+$('#DeskSaleMainForm').serialize(), function(data) {
		
		  $("#DeskSalePromotionsDiv").html(data);
		  $("#DeskSalePromotionsDiv").trigger('create');
		  
		  setTimeout(function(){
				updateInvoiceSummary();
				 $('#DeskSaleSave').removeClass('ui-disabled');
			}, 500);
		  
		 
		  
		  
	});
	
}

function updateInvoiceSummary(){
	
	var ArraySize = $("input[name=ProductCode]").length;
	var TotalAmount = 0;
	for(var i = 0; i < ArraySize; i++ ){
		TotalAmount += parseFloat($("input[name=DeskSaleMainFormNetAmount]")[i].value);
	}
	/*
	var PromotionsArraySize = $("input[name=PromotionsProductID]").length;
	var PromotionsTotalAmount = 0;
	for(var i = 0; i < PromotionsArraySize; i++ ){
		PromotionsTotalAmount += parseFloat($("input[name=PromotionsAmount]")[i].value);
	}
	
	
	
	
	var PromotionsTotalAmountExSalesTax = parseFloat("" + parseFloat((PromotionsTotalAmount / SalesTaxPercent) * 100).toFixed(2)); 
	
	var TotalAmountExSalesTax = parseFloat("" + parseFloat((TotalAmount / SalesTaxPercent) * 100).toFixed(2)); 
	*/
	
	$('#DeskSaleMainFormTotalInvoiceAmount').val(parseFloat(TotalAmount).toFixed(2));
	$('#DeskSaleMainFormTotalInvoiceAmountField').val(formatInCommas(parseFloat(TotalAmount).toFixed(2)));
	
	/*
	var SalesTaxAmount = TotalAmount - TotalAmountExSalesTax;
	
	$('#DeskSaleMainFormSalesTax').val(parseFloat(SalesTaxAmount).toFixed(2));
	$('#DeskSaleMainFormSalesTaxField').val(formatInCommas(parseFloat(SalesTaxAmount).toFixed(2)));
	
	
	var PromotionsWDHTaxAmount = parseFloat("" + parseFloat(( PromotionsTotalAmountExSalesTax * WHDTaxPercent ) / 100).toFixed(2));
	*/
	var WDHTaxAmount = parseFloat("" + parseFloat(( TotalAmount * WHDTaxPercent ) / 100).toFixed(2));
	
	
	//alert(WDHTaxAmount + "/" + PromotionsWDHTaxAmount);
	
	//WDHTaxAmount = parseFloat(WDHTaxAmount) + parseFloat(PromotionsWDHTaxAmount);
	
	$('#DeskSaleMainFormWithHoldingTax').val(parseFloat(WDHTaxAmount).toFixed(2));
	$('#DeskSaleMainFormWithHoldingTaxField').val(formatInCommas(parseFloat(WDHTaxAmount).toFixed(2)));
	
	var TotalInvoiceAmount = parseFloat(TotalAmount) + parseFloat(WDHTaxAmount);
	$('#DeskSaleMainFormTotalAmount').val(parseFloat(TotalInvoiceAmount).toFixed(2));
	$('#DeskSaleMainFormTotalAmountField').val( formatInCommas( parseFloat(TotalInvoiceAmount).toFixed(2) ) );
	
	var Discount;
	if( $('#DeskSaleMainFormTotalDiscountField').val() == '' ){
		Discount = 0;
		$('#DeskSaleMainFormTotalDiscount').val('0');
	}else{
		
		var value = $('#DeskSaleMainFormTotalDiscountField').val();
		if(isInteger(value) == false){
			Discount = 0;
			$('#DeskSaleMainFormTotalDiscountField').val('0');
		}else{
			Discount = parseFloat($('#DeskSaleMainFormTotalDiscountField').val());
			$('#DeskSaleMainFormTotalDiscount').val($('#DeskSaleMainFormTotalDiscountField').val());
		}
	}
	
	var NetAmount = parseFloat("" + parseFloat( TotalInvoiceAmount - Discount ).toFixed(2));
	$('#DeskSaleMainFormFinalNetAmount').val(NetAmount);
	$('#DeskSaleMainFormFinalNetAmountField').val( formatInCommas( NetAmount ) );
	
	var DecimalIndex = (NetAmount+"").indexOf(".");
	var FractionAmount = 0;
	if (DecimalIndex != -1){
		FractionAmount = parseInt( (NetAmount+"").substring( DecimalIndex+1 ) );
	}
	var FinalAmountRounded = 0;
	if( FractionAmount != 0 ){
		FinalAmountRounded = parseInt( (NetAmount+"").substring( 0, DecimalIndex ) ) + 1;
		$('#DeskSaleMainFormFinalNetAmountRoundedField').val( formatInCommas( FinalAmountRounded ) );
		$('#DeskSaleMainFormFinalNetAmountRounded').val( FinalAmountRounded );
		$('#DeskSaleMainFormAdjustment').val( parseFloat(FinalAmountRounded - NetAmount).toFixed(2) );
		
	}else{
		$('#DeskSaleMainFormFinalNetAmountRoundedField').val( formatInCommas( NetAmount.toFixed(0) ) );
		$('#DeskSaleMainFormFinalNetAmountRounded').val( NetAmount.toFixed(0) );
		$('#DeskSaleMainFormAdjustment').val(0);
		
	}

}

function DeskSaleUpdateProductStatistics(){
	$.ajax({
	    url: "inventory/GetDeilveryNoteProductStatistics",
	    data: $("#DeskSaleMainForm" ).serialize(),
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
	$('#DeskSaleProductCode').val(ShellCode);
	$('#DeskSaleRawCases').val(RawCases);
	$('#DeskSaleRawCases').focus();
	$('#DeskSaleUnits').attr('disabled', 'disabled');
	$('#DeskSaleBatchCode').attr('disabled', 'disabled');
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

function OrderInvoicingEditSubmit(){
	
	if( $('#DeskSaleOutletID').val() == "" ){
		document.getElementById('DeskSaleOutletID').focus();
		return false;
	}else{
		var value = $('#DeskSaleOutletID').val();
		if(isInteger(value) == false){
			document.getElementById('DeskSaleOutletID').focus();
			return false;
		}
	}
	
	
	$('#DeskSaleOutledIDHidden').val($('#DeskSaleOutletID').val());
	$('#DeskSaleDistributorNameHidden').val( $('#DeskSaleDistributorName').val() );
	
	$('#DeskSaleSave').addClass("ui-disabled");
	$.ajax({
	    url: "mobile/OrderInvoicingEditExecute",
	    
	    data: $("#DeskSaleMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "OrderInvoicing.jsp";
	    	}else{
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
	
	document.getElementById("DeskSaleFromDate").disabled = "disabled";
	document.getElementById("DeskSaleToDate").disabled = "disabled";
	
	$.get('DeskSaleSearch.jsp?FromDate='+$('#DeskSaleFromDate').val()+'&ToDate='+$('#DeskSaleToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function getOrderInvoicingEditInfoJson(ParamOrderID){
	
	if (ParamOrderID != ""){
		$.ajax({
		    url: "mobile/GetOrderInvoicingEditInfoJson",
		    data: {
		    	OrderID: ParamOrderID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					
		    		$('#DeskSaleOutletID').val(json.OutletID);
		    		$('#DeskSaleOutletID').attr('readonly', 'readonly');
	    			$('#DeskSaleOutletID').unbind('dblclick');
					
		    		getOutletName();
					getOutletMasterInfo();
					
					
					
					$('#DeskSaleCreatedByHidden').val(json.CreatedBy);
					
					$('#DeskSaleMainFormTotalInvoiceAmountField').val(formatInCommas(parseFloat(json.InvoiceAmount).toFixed(2)));
					$('#DeskSaleMainFormSalesTaxField').val(formatInCommas(parseFloat(json.SalesTaxAmount).toFixed(2)));
					$('#DeskSaleMainFormWithHoldingTaxField').val(formatInCommas(parseFloat(json.WHTaxAmount).toFixed(2)));
					$('#DeskSaleMainFormTotalAmountField').val(formatInCommas(parseFloat(json.TotalAmount).toFixed(2)));
					$('#DeskSaleMainFormTotalDiscountField').val(formatInCommas(parseFloat(json.Discount).toFixed(2)));
					$('#DeskSaleMainFormFinalNetAmountRoundedField').val(formatInCommas(json.NetAmount));
					
					$('#DeskSaleMainFormTotalInvoiceAmount').val(parseFloat(json.InvoiceAmount).toFixed(2));
					$('#DeskSaleMainFormSalesTax').val(parseFloat(json.SalesTaxAmount).toFixed(2));
					$('#DeskSaleMainFormWithHoldingTax').val(parseFloat(json.WHTaxAmount).toFixed(2));
					$('#DeskSaleMainFormTotalAmount').val(parseFloat(json.TotalAmount).toFixed(2));
					$('#DeskSaleMainFormTotalDiscount').val(parseFloat(json.Discount).toFixed(2));
					$('#DeskSaleMainFormAdjustment').val(parseFloat(json.FractionAdjustment).toFixed(2));
					$('#DeskSaleMainFormFinalNetAmountRounded').val(json.NetAmount);
					
					//$('#DeskSaleOutletID').val(json.OutletID);
					//$('#DeskSaleOutletID').val(json.OutletID);
					
				var DeskSalePromotionsTable = "<table data-role='table' data-mode='reflow' class='ui-body-d table-stripe ui-responsive' style='font-size: 10pt; width:100%'>";
    		
	    		DeskSalePromotionsTable += "<thead>";
	    		DeskSalePromotionsTable += "<tr class='ui-bar-c'>";
		    		DeskSalePromotionsTable += "<th data-priority='1' style='width:10%'>Product Code</th>";
					DeskSalePromotionsTable += "<th data-priority='1' style='width:10%'>Package</th>";
					DeskSalePromotionsTable += "<th data-priority='1' style='width:10%'>Brand</th>";
					DeskSalePromotionsTable += "<th data-priority='1' style='width:10%; text-align:right'>Raw Cases</th>";
					DeskSalePromotionsTable += "<th data-priority='1' style='width:10%; text-align:right'>Bottles</th>";
					DeskSalePromotionsTable += "<th data-priority='1' style='width:10%; text-align:right'>Rate</th>";
					DeskSalePromotionsTable += "<th data-priority='1' style='width:10%; text-align:right'>Amount</th>";
				DeskSalePromotionsTable += "</tr>";
				DeskSalePromotionsTable += "</thead>";
				
				var is_promotion = false;
				
					for( var i = 0; i < json.rows.length; i++ ){
						
						if( json.rows[i].IsPromotion == '0' ){
							
							var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
							
							var content = ""+
							"<tr id='DeskSale"+RowMaxID+"'>"+
								"<td>"+json.rows[i].SAPCode+"<input type='hidden' name='DeskSaleMainFormUnitPerSKU' value='"+json.rows[i].UnitPerSKU+"'></td>"+	
								"<td>"+json.rows[i].PackageName+"<input type='hidden' name='DeskSaleMainFormPackageLabel' value='"+json.rows[i].PackageName+"' ><input type='hidden' name='PackageID' value='"+json.rows[i].PackageID+"' ><input type='hidden' name='DeskSaleMainFormLiquidInML' value='"+json.rows[i].LiquidInML+"'></td>"+						
								"<td>"+json.rows[i].BrandName+"<input type='hidden' name='DeskSaleMainFormBrandLabel' value='"+json.rows[i].BrandName+"' ><input type='hidden' name='BrandID' value='"+json.rows[i].BrandID+"' ><input type='hidden' id='ProductCode' name='ProductCode' value='"+json.rows[i].SAPCode+"'><input type='hidden' name='ProductID' value='"+json.rows[i].ProductID+"'></td>"+
								"<td>"+json.rows[i].RawCases+"<input type='hidden' name='DeskSaleMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
								"<td>"+json.rows[i].Units+"<input type='hidden' name='DeskSaleMainFormUnits' value='"+json.rows[i].Units+"'></td>"+
								"<td>"+formatInCommas(json.rows[i].RateRawCases)+"<input type='hidden' name='DeskSaleMainFormRateHidden' value='"+json.rows[i].RateRawCases+"'><input type='hidden' name='DeskSaleMainFormUnitRateHidden' value='"+json.rows[i].RateUnits+"'></td>"+
								"<td>"+formatInCommas(json.rows[i].TotalAmount)+"<input type='hidden' id='DeskSaleMainFormAmount' name='DeskSaleMainFormAmount' value='"+json.rows[i].TotalAmount+"'></td>"+
								"<td>"+json.rows[i].Discount+"<input type='hidden' name='DeskSaleMainFormDiscount' value='"+json.rows[i].Discount+"'></td>"+
								"<td>"+formatInCommas(json.rows[i].TotalAmount)+"<input type='hidden' id='DeskSaleMainFormNetAmount' name='DeskSaleMainFormNetAmount' value='"+json.rows[i].TotalAmount+"'></td>"+
								"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeskSaleDeleteRow('DeskSale"+RowMaxID+"');\">Delete</a></td>"+
							"</tr>";
					    	
							$("#DeskSaleTableBody").append(content).trigger('create');
							
							RowCount++;
							$('#NoProductRow').css('display', 'none');
							$('#RowMaxID').val(RowMaxID);
							$('#DeskSaleSave').removeClass('ui-disabled');
						
						}else{
							
							is_promotion = true;
							
							DeskSalePromotionsTable += "<tr>";
					    		DeskSalePromotionsTable += "<td style='width:10%'>"+json.rows[i].SAPCode+"</td>";
								DeskSalePromotionsTable += "<td style='width:10%'>"+json.rows[i].PackageName+"</td>";
								DeskSalePromotionsTable += "<td style='width:10%'>"+json.rows[i].BrandName+"</td>";
								DeskSalePromotionsTable += "<td style='width:10%; text-align:right'>"+json.rows[i].RawCases+"</td>";
								DeskSalePromotionsTable += "<td style='width:10%; text-align:right'>"+json.rows[i].Units+"</td>";
								DeskSalePromotionsTable += "<td style='width:10%; text-align:right'>"+formatInCommas(json.rows[i].RateRawCases)+"</td>";
								DeskSalePromotionsTable += "<td style='width:10%; text-align:right'>"+formatInCommas(json.rows[i].TotalAmount);
								DeskSalePromotionsTable += "<input type='hidden' name='PromotionsProductCode' value='"+json.rows[i].SAPCode+"' > "+
								
								"<input type='hidden' name='PromotionID' value='"+json.rows[i].PromotionID+"' > "+
								
								"<input type='hidden' name='PromotionsPackageID' value='"+json.rows[i].PackageID+"' > "+
								"<input type='hidden' name='PromotionsBrandID' value='"+json.rows[i].BrandID+"' > "+
								
								"<input type='hidden' name='PromotionsProductID' value='"+json.rows[i].ProductID+"' > "+
								"<input type='hidden' name='PromotionsUnitPerSKU' value='"+json.rows[i].UnitPerSKU+"' > "+
								"<input type='hidden' name='PromotionsLiquidInML' value='"+json.rows[i].LiquidInML+"' > "+
								
								"<input type='hidden' name='PromotionsRawCases' value='"+json.rows[i].RawCases+"' > "+
								"<input type='hidden' name='PromotionsUnits' value='"+json.rows[i].Units+"' > "+
								"<input type='hidden' name='PromotionsRateRawCase' value='"+json.rows[i].RateRawCases+"' > "+
								"<input type='hidden' name='PromotionsRateUnit' value='"+json.rows[i].RateUnits+"' > "+
								"<input type='hidden' name='PromotionsAmount' value='"+json.rows[i].TotalAmount+"' > "+"</td>";
							DeskSalePromotionsTable += "</tr>";
							
						}
	
					}
					
					DeskSalePromotionsTable += "</table>";
					
					if( is_promotion ){
						$("#DeskSalePromotionsDiv").append(DeskSalePromotionsTable).trigger('create');
					}
					
		    	}else{
		    		//$("#DeskSaleDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}

function DeskSaleGetSaleOrderStatus(SaleOrderNo, isEditCase){
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
	    		document.getElementById("DeskSaleSAPOrderNo").focus();
	    		return false;
	    	}else if(json.error == '2'){
	    		alert('Sale Order already exists.');
	    		document.getElementById("DeskSaleSAPOrderNo").focus();
	    		return false;
	    	}else{
	    		
	    		if (json.exists == "true"){
					
	    			if(json.InvoiceNo == "0"){
	    				alert("Invoice does not exists against this order.");
	    				return false;
	    			}
	    			
	    			$('#DeskSaleInvoiceNo').val(json.InvoiceNo);
	    			$('#DeskSaleInvoiceAmount').val(json.InvoiceAmount);
	    			//alert($('#DeskSaleInvoiceAmount').val());
	    			
	    			$('#DeskSaleDistributorID').val(json.DistrubutorName);
    				
    				$('#DeskSaleMainFormDistributorID').val(json.DistrubutorID);
	    			
	    			if(!isEditCase){
	    				
	    				var NoProductContentStr = '<tr id="NoProductRow"><td colspan="7" style="margin: 1px; padding: 0px;">';
	    				NoProductContentStr += '<div style="width: 100%; background-color: #FFFFFF; padding: 5px;">No products added.</div></td></tr>';
	    				
	    				$("#DeskSaleTableBody").html(NoProductContentStr);
	    				
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
										"<tr id='DeskSale"+RowMaxID+"'>"+
											"<td>"+json.rows[i].ProductSAPCode+"<input type='hidden' name='DeskSaleMainFormUnitPerSKU' value='1'></td>"+	
											"<td>"+json.rows[i].PackageName+"<input type='hidden' name='PackageID' value='"+json.rows[i].PackageID+"' ><input type='hidden' name='DeskSaleMainFormLiquidInML' value='0'></td>"+						
											"<td>"+json.rows[i].BrandName+"<input type='hidden' id='ProductCode' name='ProductCode' value='"+json.rows[i].ProductSAPCode+"'><input type='hidden' name='ProductID' value='"+json.rows[i].ProductID+"'></td>"+
											"<td>"+json.rows[i].QuantityRawCases+"<input type='hidden' name='DeskSaleMainFormRawCases' value='"+json.rows[i].QuantityRawCases+"'></td>"+
											"<td>"+json.rows[i].QuantityUnits+"<input type='hidden' name='DeskSaleMainFormUnits' value='"+json.rows[i].QuantityUnits+"'></td>"+
											"<td>&nbsp;<input type='hidden' name='DeskSaleMainFormBatchCode' value=''></td>"+
											"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeskSaleDeleteRow('DeskSale"+RowMaxID+"'); UpdatePackageDeliveryQuantities("+json.rows[i].PackageID+", "+json.rows[i].QuantityRawCases+", 0); UpdateShellCounter('"+json.rows[i].QuantityRawCases+"', '')\">Delete</a></td>"+
										"</tr>";
										
									$("#DeskSaleTableBody").append(content).trigger('create');
									RowCount++;
									$('#NoProductRow').css('display', 'none');
									
									
									$('#RowMaxID').val(RowMaxID);
								}
							}
							
						//}// end else
						
					}
					
					TableContent += ' </table>';
					$('#DeskSalePackageStatusTable').html(TableContent);
					//$('#DeskSalePackageStatusTable').refresh();
					//$('#ProductInfoSpan').html(json.PackageLabel+" - "+json.BrandLabel);
					
					if(isEditCase){
						
						var PackageID = document.getElementsByName("PackageID");
	    				var FormRawCases = document.getElementsByName("DeskSaleMainFormRawCases");
	    				var FormUnits = document.getElementsByName("DeskSaleMainFormUnits");
			    		
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
					
					DeskSaleUpdateProductStatistics();
					
		    	}else{
		    		alert('Invalid Sale Order Number');
		    		$.mobile.hidePageLoadingMsg();
		    		document.getElementById("DeskSaleSAPOrderNo").focus();
		    	}
	    		
	    	}
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.hidePageLoadingMsg();
	    	document.getElementById("DeskSaleSAPOrderNo").focus();
	    }
	});
}

function getProductInfoJson(ProductID){
	
	if( $('#DeskSaleOutletID').val() == '' ){
		//alert('Please select Outlet before adding products');
		document.getElementById("DeskSaleOutletID").focus();
		return false;
	}else{
		var value = $('#DeskSaleOutletID').val();
		if(isInteger(value) == false){
			document.getElementById('DeskSaleOutletID').focus();
			return false;
		}
	}
	
	if( ProductID == '5012' || ProductID == '5014' ){
		$('#DeskSaleUnits').attr('disabled', 'disabled');
		$('#DeskSaleBatchCode').attr('disabled', 'disabled');
	}else{
		$('#DeskSaleUnits').attr('disabled', false);
		$('#DeskSaleBatchCode').attr('disabled', false);
	}
	
	if (ProductID != ""){
		$.ajax({
		    url: "common/GetProductInfo",
		    data: {
		    	ProductCode: ProductID,
		    	OutletID: $('#DeskSaleOutletID').val()
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					//alert(json.PackageLabel+" "+json.BrandLabel);
					$('#ProductInfoSpan').html(json.PackageLabel+" - "+json.BrandLabel);
					$('#DeskSaleRate').val(json.RawCasePrice);
					
					UnitPrice = parseFloat(json.UnitPrice);
					$('#DeskSaleUnitRate').val(json.UnitPrice);
					
		    	}else{
		    		//$("#DeskSaleDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}
function DeskSaleDelete()
{
	var r=confirm("Are you sure you want to delete?");
	if (r==true)
	  {	  
		$.ajax({
		    url: "inventory/DeilveryNoteDeleteExecute",
		    
		    data: $("#DeskSaleMainForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		location = "DeskSale.jsp";
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
	$('#DeskSaleProductCode').val(SAPCode);
	$('#ProductInfoSpan').html(ProductName);
	setTimeout("$('#DeskSaleRawCases').focus();", 100);
}

function OutletSearchCallBackDeskSale(SAPCode, EmployeeName){
	$('#DeskSaleOutletID').val(SAPCode);
	$('#DeskSaleOutletName').val(EmployeeName);
	getOutletMasterInfo();
}

function getOutletName(){
	//alert();
	if(isInteger($('#DeskSaleOutletID').val()) == false ){
		$('#DeskSaleOutletID').val('');
		return false;
	}
	
	$.ajax({
		
		url: "common/GetOutletBySAPCodeJSON",
		data: {
			SAPCode: $('#DeskSaleOutletID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#DeskSaleOutletName').val(json.OutletName);
				$('#DeskSaleOutledIDHidden').val( $('#DeskSaleOutletID').val() );
			}else{
				$('#DeskSaleOutletName').val('');
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

function getOutletMasterInfo()
{
	$.ajax({
		
		url: "sampling/GetOutletInfoJson",
		data: {
			OutletID: $('#DeskSaleOutletID').val(),
			FeatureID:50
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$("#DeskSaleDistributorName").val(json.distributor_id+" - "+json.distributor_name);	
				$('#DeskSaleDistributorIDHidden').val(json.distributor_id);
				$("#OutletAddress").val(json.address+" ("+json.owner_name+", "+json.owner_tele+")");
				
				getDistributorInfoJson(json.distributor_id);
				
			}else{
				
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
}

function getDistributorInfoJson(DistributorIDParam)
{
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: DistributorIDParam
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#DeskSaleRegionIDHidden').val(json.RegionID);
				
			}else{
				
			}
		},
		error:function(xhr, status){
			//alert("Server could not be reached.");
		}
		
	});
}

function DeskSaleCalculate(){
	var RawCases;
	if( $('#DeskSaleRawCases').val() == '' ){
		RawCases = 0;
	}else{
		RawCases = $('#DeskSaleRawCases').val();
	}
	
	var Units;
	if( $('#DeskSaleUnits').val() == '' ){
		Units = 0;
	}else{
		Units = $('#DeskSaleUnits').val();
	}
	
	var Rate;
	if( $('#DeskSaleRate').val() == '' ){
		Rate = 0;
	}else{
		Rate = $('#DeskSaleRate').val();
	}
	
	var Discount;
	if( $('#DeskSaleDiscount').val() == '' ){
		Discount = 0;
	}else{
		Discount = $('#DeskSaleDiscount').val();
	}
	
	var Amount = (RawCases * Rate) + ( Units * UnitPrice );
	var NetAmount = Amount - Discount;
	

	
	if( Amount > 0 ){
		$('#DeskSaleAmount').val(parseFloat(Amount).toFixed(2));
	}else{
		$('#DeskSaleAmount').val('');
	}
	
	if( NetAmount > 0 ){
		$('#DeskSaleNetAmount').val(parseFloat(NetAmount).toFixed(2));
	}else{
		$('#DeskSaleNetAmount').val('');
	}
	
	
}

function formatInCommas(yourNumber) {
    //Seperates the components of the number
    var n= yourNumber.toString().split(".");
    //Comma-fies the first part
    n[0] = n[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    //Combines the two sections
    return n.join(".");
}


function getProductPrice(PackageIDParam, BrandIDParam, OutletIDParam, counter){
	
	$.ajax({
	    url: "common/GetProductInfo",
	    data: {
	    	PackageID: PackageIDParam,
	    	BrandID: BrandIDParam,
	    	OutletID: OutletIDParam
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
			
	    	if (json.exists == "true"){
	    		
	    		$('#ProdcutCodeSpan'+counter).html(json.ProductCode);
	    		
	    		var RateRawCase = parseFloat(json.RawCasePrice).toFixed(2);
	    		var RateUnit = parseFloat(json.UnitPrice);
	    		
	    		var RawCases =  $("input[name=RawCasePromotionHidden]")[counter].value;
	    		var Units =  $("input[name=UnitPromotionHidden]")[counter].value;
	    		
	    		var AmountRawCase = parseFloat(""+parseFloat(parseFloat(RawCases) * RateRawCase).toFixed(2));
	    		var AmountUnit = parseFloat(""+parseFloat(parseFloat(Units) * RateUnit).toFixed(2));
	    		
	    		var TotalAmount = parseFloat(parseFloat(AmountRawCase) + parseFloat(AmountUnit)).toFixed(2);
	    		
	    		$('#RateSpan'+counter).html(RateRawCase);
	    		$('#AmountSpan'+counter).html(formatInCommas(TotalAmount));
	    		
	    		$("input[name=PromotionsProductID]")[counter].value = json.ProductID;
	    		$("input[name=PromotionsRawCases]")[counter].value = RawCases;
	    		$("input[name=PromotionsUnits]")[counter].value = Units;
	    		$("input[name=PromotionsRateRawCase]")[counter].value = RateRawCase;
	    		$("input[name=PromotionsRateUnit]")[counter].value = RateUnit;
	    		$("input[name=PromotionsUnitPerSKU]")[counter].value = json.UnitPerSKU;
	    		$("input[name=PromotionsLiquidInML]")[counter].value = json.LiquidInML;
	    		$("input[name=PromotionsAmount]")[counter].value = TotalAmount;
				
	    		setTimeout(function(){
	    			updateInvoiceSummary();
	    		}, 500);
	    		
	    	}else{
	    		//$("#DeskSaleDistributorName").val("Invalid ID");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
	
}
