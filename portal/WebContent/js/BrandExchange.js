
var RowCount = 0;
var isAddable = true;

$( document ).delegate("#BrandExchange", "pageshow", function() {
	document.getElementById('BrandExchangeRemarks').focus();
	
	//showSearchContent();
	
});



$( document ).delegate("#BrandExchange", "pageinit", function() {
	
	$("#BrandExchangePackage").change( function( event ) {
		getBrandList( $("#BrandExchangePackage").val() );
		
	});
	
	$("#FromDateSpan").click( function( event ) {
		document.getElementById("BrandExchangeFromDate").disabled = false;
		document.getElementById("BrandExchangeToDate").disabled = false;
		$("#BrandExchangeFromDate").focus();
		
	});	
	$("#ToDateSpan").on( "click", function( event, ui ) {
		document.getElementById("BrandExchangeFromDate").disabled = false;
		document.getElementById("BrandExchangeToDate").disabled = false;
		$("#BrandExchangeToDate").focus();
	});	

});


function isInteger (o) {
	
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
	}


function BrandExchangeDeleteRow(RowID){
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#BrandExchangeSave').addClass('ui-disabled');
	}
}

function isInteger (o) {
	
  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}


function BrandExchangeAddProduct(){
	
	if( isAddable == false ){
		return false;
	}
	
	if( $('#BrandExchangePackage').val() == "" ){
		document.getElementById('BrandExchangePackage').focus();
		return false;
	}
	
	if( $('#BrandExchangeProductCodeIssue').val() == "" ){
		document.getElementById('BrandExchangeProductCodeIssue').focus();
		return false;
	}
	
	if( $('#BrandExchangeProductCodeReceive').val() == "" ){
		document.getElementById('BrandExchangeProductCodeReceive').focus();
		return false;
	}
	
	if( $('#BrandExchangeProductCodeIssue').val() == $('#BrandExchangeProductCodeReceive').val() ){
		alert("Exchange brand must be different");
		document.getElementById('BrandExchangeProductCodeReceive').focus();
		return false;
	}
	
	if( ( $('#BrandExchangeRawCases').val() == "" || $('#BrandExchangeRawCases').val() == "0" ) && ( $('#BrandExchangeUnits').val() == "" || $('#BrandExchangeUnits').val() == "0" ) ){
		document.getElementById('BrandExchangeRawCases').focus();
		return false;
	}else{
		var value_raw_cases = $('#BrandExchangeRawCases').val();
		if(value_raw_cases!= "" && isInteger(value_raw_cases) == false){
			document.getElementById('BrandExchangeRawCases').focus();
			return false;
		}
		
		var value_units = $('#BrandExchangeUnits').val();
		if(value_units != "" && isInteger(value_units) == false){
			document.getElementById('BrandExchangeUnits').focus();
			return false;
		}
		
	}
		
	$("#BrandExchangeMainForm").css("visibility","visible");
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
	    url: "inventory/GetBrandExchangeInfo",
	    data: {
	    	PackageID: $('#BrandExchangePackage').val(),
	    	BrandIssue: $("#BrandExchangeProductCodeIssue").val(),
	    	BrandReceive: $('#BrandExchangeProductCodeReceive').val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
			
			$.mobile.hidePageLoadingMsg();
			
	    	if (json.exists == "true"){
	    			
    			var ValBrandExchangeRawCases = $('#BrandExchangeRawCases').val();
    			if( ValBrandExchangeRawCases == '' ){
    				ValBrandExchangeRawCases = '0';
    			}
    			
    			var ValBrandExchangeUnits = $('#BrandExchangeUnits').val();
    			if( ValBrandExchangeUnits == '' ){
    				ValBrandExchangeUnits = '0';
    			}
    			
    			if( parseInt(ValBrandExchangeUnits) >= parseInt(json.UnitPerSKUReceive) ){
    				alert('Bottles should be less than units per SKU');
    				document.getElementById('BrandExchangeUnits').focus();
    				return false;
    			}
    			
    			//var val = $("#BrandExchangeProductCodeIssue").val()+$("#BrandExchangeProductCodeReceive").val();
    			var val = json.ProductCodeIssue+json.ProductCodeReceive;
    			
    			var content = ""+
    			"<tr id='BrandExchange"+val+"'>"+
    				"<td>"+$("#BrandExchangePackage option:selected").text()+
    				"<input type='hidden' name='BrandExchangeMainFormPackage' value='"+$("#BrandExchangePackage").val()+"' /></td>"+
    				"<input type='hidden' name='BrandExchangeMainFormLiquidInML' value='"+json.LiquidInML+"'></td>"+
    				
    				"<td>"+$("#BrandExchangeProductCodeIssue option:selected").text()+
						"<input type='hidden' name='BrandExchangeMainFormProductIDIssue' value='"+json.ProductCodeIssue+"'>"+
						"<input type='hidden' name='BrandExchangeMainFormUnitPerSKUIssue' value='"+json.UnitPerSKUIssue+"'>"+
						
    				"<td>"+$("#BrandExchangeProductCodeReceive option:selected").text()+
	    				"<input type='hidden' name='BrandExchangeMainFormProductIDReceive' value='"+json.ProductCodeReceive+"'>"+
	    				"<input type='hidden' name='BrandExchangeMainFormUnitPerSKUReceive' value='"+json.UnitPerSKUReceive+"'>"+
    				
    				"<td>"+ValBrandExchangeRawCases+"<input type='hidden' name='BrandExchangeMainFormRawCases' value='"+ValBrandExchangeRawCases+"'></td>"+
    				"<td>"+ValBrandExchangeUnits+"<input type='hidden' name='BrandExchangeMainFormUnits' value='"+ValBrandExchangeUnits+"'></td>"+
    				"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"BrandExchangeDeleteRow('BrandExchange"+val+"')\">Delete</a></td>"+
    			"</tr>";
    			
    			$("#BrandExchangeTableBody").append(content).trigger('create');
    			
    			RowCount++;
    			
    			$('#NoProductRow').css('display', 'none');
    			
    			$('#BrandExchangePackage').val('');
    			$('#BrandExchangeProductCodeIssue').val('');
    			$('#BrandExchangeProductCodeReceive').val('');
    			$('#BrandExchangeRawCases').val('');
    			$('#BrandExchangeUnits').val('');
    			
    			$('#BrandExchangePackage').selectmenu("refresh");
    			$('#BrandExchangeProductCodeIssue').selectmenu("refresh");
    			$('#BrandExchangeProductCodeReceive').selectmenu("refresh");
    			
    			$('#BrandExchangeProductCode').focus();
    			$('#BrandExchangeSave').removeClass('ui-disabled');
	    			
	    		
	    			
	    	}else{	// json exists false
	    		alert("Server could not be reached.");
	    	}
	    		
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	    	

	
	//$("#SamplingReceivingBarcodeField").val("");
	
	return false;
}

function BrandExchangeSubmit(){
	
	$('#BrandExchangeMainFormRemarks').val($('#BrandExchangeRemarks').val());
	
	$('#BrandExchangeSave').addClass("ui-disabled");
	$.ajax({
	    url: "inventory/BrandExchangeExecute",
	    
	    data: $("#BrandExchangeMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "BrandExchange.jsp";
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
	
	document.getElementById("BrandExchangeFromDate").disabled = "disabled";
	document.getElementById("BrandExchangeToDate").disabled = "disabled";
	
	$.get('BrandExchangeSearch.jsp?FromDate='+$('#BrandExchangeFromDate').val()+'&ToDate='+$('#BrandExchangeToDate').val(), function(data) {
		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');
		  
	});
	return false;

}

function getBrandExchangeInfoJson(EditID){
	
	if (EditID != ""){
		$.ajax({
		    url: "inventory/GetBrandExchangeInfoJson",
		    data: {
		    	EditID: EditID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					
					$("#BrandExchangeRemarks").val(json.Remarks);
					
					//alert('rows length = '+json.rows.length);
					
					if( json.isTodaysVoucher == 'false' ){
						$('#BrandExchangeSave').addClass('ui-disabled');
						isAddable = false;
					}else{
						$('#BrandExchangeSave').removeClass('ui-disabled');
						isAddable = true;
					}
					
					$("#BrandExchangeMainForm").css("visibility","visible");
					
					for( var i = 0; i < json.rows.length; i++ ){
						var val = json.rows[i].ProductIDIssued+json.rows[i].ProductIDReceived;
						var content = ""+
		    			"<tr id='BrandExchange"+val+"'>"+
		    				"<td>"+json.rows[i].PackageName+
		    					"<input type='hidden' name='BrandExchangeMainFormPackage' value='"+json.rows[i].PackageID+"' /></td>"+
		    					"<input type='hidden' name='BrandExchangeMainFormLiquidInML' value='"+json.rows[i].LiquidInML+"' /></td>"+
		    				
		    				"<td>"+json.rows[i].BrandNameIssued+
								"<input type='hidden' name='BrandExchangeMainFormProductIDIssue' value='"+json.rows[i].ProductIDIssued+"'>"+
								"<input type='hidden' name='BrandExchangeMainFormUnitPerSKUIssue' value='"+json.rows[i].UnitPerSKUIssued+"'>"+
		    						
		    				"<td>"+json.rows[i].BrandNameReceived+
			    				"<input type='hidden' name='BrandExchangeMainFormProductIDReceive' value='"+json.rows[i].ProductIDReceived+"'>"+
			    				"<input type='hidden' name='BrandExchangeMainFormUnitPerSKUReceive' value='"+json.rows[i].UnitPerSKUReceived+"'>"+
		    				
		    				"<td>"+json.rows[i].RawCases+"<input type='hidden' name='BrandExchangeMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
		    				"<td>"+json.rows[i].Units+"<input type='hidden' name='BrandExchangeMainFormUnits' value='"+json.rows[i].Units+"'></td>"+
		    				"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"BrandExchangeDeleteRow('BrandExchange"+val+"')\">Delete</a></td>"+
		    			"</tr>";
							
						$("#BrandExchangeTableBody").append(content).trigger('create');
						
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


function getBrandList(PackageID){
	$.mobile.showPageLoadingMsg();
	
	if (PackageID != ""){
		$.ajax({
		    url: "common/GetBrandListJson",
		    data: {
		    	PackageID: PackageID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if (json.exists == "true"){
		    		
		    		var regex = new RegExp('select_id', 'g');
		    		
		    		$('#SpanBrandExchangeProductCodeIssue').html(json.SelectOptions.replace(regex, 'BrandExchangeProductCodeIssue'));
		    		$("#SpanBrandExchangeProductCodeIssue").trigger('create');
		    		
		    		$('#SpanBrandExchangeProductCodeReceive').html(json.SelectOptions.replace(regex, 'BrandExchangeProductCodeReceive').replace("Select Brand", 'Exchange To'));
		    		$("#SpanBrandExchangeProductCodeReceive").trigger('create');
			    	
			    	$.mobile.hidePageLoadingMsg();
					
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

function removeItems(SelectID){
	
	var select = document.getElementById(SelectID);

	for (var i = 0 ; i < select.length ; i++){
		select.remove(i);
	}
}

function addItem(SelectID, Value, Text){
    // Create an Option object

    var opt = document.createElement("option");
    
    // Add an Option object to Drop Down/List Box
    document.getElementById(SelectID).options.add(opt);
    // Assign text and value to Option object
    opt.text = Text;
    opt.value = Value;
           
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
					$('#BrandExchangeType').html(json.DamageStockTypeOptions);
					$('#BrandExchangeType').refresh();
					
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