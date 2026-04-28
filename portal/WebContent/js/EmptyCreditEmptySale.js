

$( document ).delegate("#GLUnpostCashReceiptsPage", "pageinit", function() {
	
	
	
});

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function FormSubmit(){
	
//alert();
	
	$('#SubmitButton').addClass("ui-disabled");

	
	$.ajax({
	    url: "empty/EmptyCreditEmptySaleExecute",
	    data: $("#GLCashReceiptsForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'EmptyCreditEmptySale.jsp';
	    	}else{
	    		alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function setDateFormat(DateVal){
	if(DateVal == '' || DateVal == null ){
		return '';
	}
	var Year = DateVal.substr(0, 4);
	var Month = DateVal.substr(5, 2);
	var Day = DateVal.substr(8, 2);
	return Day+"/"+Month+"/"+Year;
}

function getEditInfoJson(){
	
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "empty/GetSAPOrderInfoEmptyJson",
		data: {
			OrderNumber: $('#OrderNumber').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			
			$.mobile.hidePageLoadingMsg();
			
			
			
			if(json.success == "true"){
				
				
				for(var i = 0 ; i < json.rows.length; i++){
					
					
					$('#DistributorID').val(json.rows[i].DistributorID);
					$('#DistributorName').val(json.rows[i].DistributorName);
					
					//alert();
					
					var content = ""+
					"<tr>"+
						"<td>"+json.rows[i].SAPCode+"<input type='hidden' name='SapCodee' id='SapCodee_"+i+"' value='"+json.rows[i].SAPCode+"'/><input type='hidden' name='DistributorIdd' id='DistributorIdd_"+i+"' value='"+json.rows[i].DistributorID+"'/><input type='hidden' name='ProductIdd' id='ProductIdd_"+i+"' value='"+json.rows[i].ProductID+"'/></td>"+
						"<td>"+json.rows[i].PackageName+"<input type='hidden' name='PackageIdd' id='PackageIdd_"+i+"' value='"+json.rows[i].PackageID+"'/></td>"+
						"<td>"+json.rows[i].BrandName+"<input type='hidden' name='BrandIdd' id='BrandIdd_"+i+"' value='"+json.rows[i].BrandID+"'/></td>"+				
						"<td>"+json.rows[i].RawCases+"<input type='hidden' name='Rawcases' id='Rawcases_"+i+"' value='"+json.rows[i].RawCases+"'/></td>"+
						"<td>"+json.rows[i].Units+"<input type='hidden' name='Unitss' id='Unitss_"+i+"' value='"+json.rows[i].Units+"'/><input type='hidden' name='LiquidInMll' id='LiquidInMll_"+i+"' value='"+json.rows[i].LiquidInML+"'/><input type='hidden' name='UnitPerSKUU' id='UnitPerSKUU_"+i+"' value='"+json.rows[i].UnitPerSKU+"'/><input type='hidden' name='TotalUnitss' id='TotalUnitss_"+i+"' value='"+json.rows[i].TotalUnits+"'/></td>"+
						
					"</tr>";
					
					$("#GLUnpostCashReceiptTableBody").append(content).trigger('create');
					$('#NoProductRow').css('display', 'none');
					
				}
				
				setTotal();
				$('#SubmitButton').removeClass('ui-disabled');
				 $('#OrderNumber').attr('readonly', true);
				
			}else{
				
				$('#DistributorID').val('');
				$('#DistributorName').val('');
				
				$('#Type').val('');
				$('#Narration').val('');
				
				var NoItemRowTR = "<tr id='NoProductRow'><td colspan='5' style='margin: 1px; padding: 0px;'><div style='width: 100%; background-color: #FFFFFF; padding: 5px;'>No details available.</div></td></tr>";
				
				$("#GLUnpostCashReceiptTableBody").html(NoItemRowTR).trigger('create');
				$('#NoProductRow').css('display', 'table-row');
				$('#InstrumentAmountTotal').val('');
				
				$('#SubmitButton').addClass('ui-disabled');
				
				alert("No record found");
				
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
		
}

function openDistributorSelectionPopup(){
	$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
		//alert("hELLO");
		lookupDistributorInit();
	} );
	$('#LookupDistributorSearch').popup("open");
}

function DistributorSearchCallBackCashReceipt(SAPCode, DistributorName){
	$('#DistributorID').val(SAPCode);
	$('#DistributorName').val(DistributorName);
	$('#isDistributorValid').val('true');
}

function setTotal(){ 
	var len = $('input[name=InstrumentAmount]').length;
	var TotalAmount = 0;
	for( var i = 0; i < len; i++ ){
		var Amount = $('input[name=InstrumentAmount]')[i].value;
		if( Amount == '' ){
			Amount = 0;
		}
		TotalAmount += parseFloat(Amount);
	}
	$('#InstrumentAmountTotal').val( numberWithCommas((TotalAmount).toFixed(2)));
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function DeleteRow(RowID){
	
	$("#"+RowID).remove();
	var RowCount = parseInt($('#RowMaxID').val());
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#SubmitButton').addClass('ui-disabled');
	}
	
	$('#RowMaxID').val(RowCount);
	setTotal();
}

function AddRow(){
	
	if( $('#AddRowInstrumentType').val() == "" ){
		alert("Please select Instrument Type");
		$('#AddRowInstrumentType').focus();
		return false;
	}
	
	if( $('#AddRowAmount').val() == "" ){
		alert("Please select Amount");
		$('#AddRowAmount').focus();
		return false;
	}else{
		if( isFloat($('#AddRowAmount').val()) == false ){
			alert("Amount must be integer or in decimal value.");
			$('#AddRowAmount').focus();
			return false;
		}
	}
	
	if( $('#CaptureReferenceNo').val() == 'true' ){
		if( $('#AddRowReferenceNo').val() == '' ){
			alert('Please enter Reference#');
			$('#AddRowReferenceNo').focus();
			return false;
		}
	}
	
	if( $('#CaptureDate').val() == 'true' ){
		if( $('#AddRowDate').val() == '' ){
			alert('Please select Date');
			$('#AddRowDate').focus();
			return false;
		}
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "cash/GetInstrumentTypeInfoJson",
		data: {
			InstrumentID: $('#AddRowInstrumentType').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				
				var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
				
				var content = ""+
				"<tr id='GLCashReceiptTR"+RowMaxID+"'>"+
					"<td>"+json.InstrumentLabel+"</td>"+
					"<td style='text-align: right; padding-right: 20px'>"+numberWithCommas(parseFloat($('#AddRowAmount').val()).toFixed(2))+
						"<input type='hidden' name='InstrumentAmount' id='InstrumentAmount' value='"+$('#AddRowAmount').val()+"'>"+
						"<input type='hidden' name='InstrumentID' id='InstrumentID' value='"+json.InstrumentID+"'>"+
						"<input type='hidden' name='InstrumentCategoryID' id='InstrumentCategoryID' value='"+json.InstrumentCategoryID+"'></td>"+
					
					"<td>"+$('#AddRowReferenceNo').val()+
						"<input type='hidden' name='InstrumentNo' id='InstrumentNo' value='"+$('#AddRowReferenceNo').val()+"'></td>"+
					
					"<td>"+setDateFormat($('#AddRowDate').val())+
						"<input type='hidden' name='InstrumentDate' id='InstrumentDate' value='"+setDateFormat($('#AddRowDate').val())+"'></td>"+
						
					"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeleteRow('GLCashReceiptTR"+RowMaxID+"');\">Delete</a></td>"+
				"</tr>";
				
				$("#GLCashReceiptTableBody").append(content).trigger('create');
				$('#NoProductRow').css('display', 'none');
				$('#SubmitButton').removeClass('ui-disabled');
				$('#RowMaxID').val(RowMaxID);
				
				setTotal();
				clearFields();
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
	return false;
	
}

function clearFields(){
	$('#AddRowInstrumentType').val('').change();
	$('#AddRowAmount').val('');
	$('#AddRowReferenceNo').val('');
	$('#AddRowDate').val('');
}

function setInstrumentAttributes(){
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "cash/GetInstrumentTypeInfoJson",
		data: {
			InstrumentID: $('#AddRowInstrumentType').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				
				if(json.CaptureInstrumentNo == "0"){
					$('#AddRowReferenceNo').textinput('disable');
					$('#AddRowReferenceNo').val('');
					$('#CaptureReferenceNo').val('false');
				}else{
					$('#AddRowReferenceNo').textinput('enable');
					$('#CaptureReferenceNo').val('true');
				}
				
				if(json.CaptureDate == "0"){
					$('#AddRowDate').textinput('disable');
					$('#AddRowDate').val('');
					$('#CaptureDate').val('false');
				}else{
					$('#AddRowDate').textinput('enable');
					$('#CaptureDate').val('true');
				}
				
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

function doPrint(CashReceiptID){
	$('#GLCashReceiptID').val( CashReceiptID );
	$('#GLCashReceiptsPrintExecute').submit();
	
}
