/*

var RowCount = 0;




function GetAllSalesForReturn(DispatchID,VehicleTypeName,DriverName,vehicleName)
{
	$("#DispatchID").val(DispatchID); //setting dispatch id	
	$("#DisptachReturnsVehicleNumber").val(vehicleName); //setting dispatch id	
	$("#DisptachReturnsDriverName").val(DriverName); //setting dispatch id	
	$("#DisptachVehicleType").val(VehicleTypeName); //setting dispatch id	
	
	//alert(document.getElementById("ReturnsGenerrateForm"));
	document.getElementById("ReturnsGenerrateForm").submit();
}





function DispatchBalanceCashReceiptsSubmit()
{
	var InvoiceAmountTotal= parseInt($('#InvoiceAmountTotal').val());
	var cashReceivedTotal= parseInt($('#CashReceivedTotal').val());
	//alert(InvoiceAmountTotal);
	//alert(cashReceivedTotal);
	
	if (InvoiceAmountTotal <cashReceivedTotal){
		alert("Remaining Balance Should be equal to Zero");
		return false;
	}
	if(InvoiceAmountTotal == cashReceivedTotal){
		 $("#FullyAdjusted").val("1");
	}else{
		$("#FullyAdjusted").val("0");
	}
	//return false;
	//	//alert(remainingBalance);

	//	var MaxQuantity=document.getElementsByName("maxQuantityAllowed");
	//    var enteredQuantity=document.getElementsByName("DispatchBalanceCashReceiptsMainFormRawCases");
	//    var RecordLength=MaxQuantity.length;
	//    for(var i=0;i<RecordLength;i++ ){
	//    	if(parseInt(MaxQuantity[i].value)<parseInt(enteredQuantity[i].value)){
	//    		document.getElementById(enteredQuantity[i].id).focus();
	//    		alert("Quantity should be less than "+MaxQuantity[i].value);
	//    		return false;
	//    	}
	//    }
	//		
		$.mobile.loading( 'show');
		
	
		$.ajax({
		    url: "distributor/DispatchBalanceCashReceiptsExecute",
		    data: $("#DispatchBalanceMainForm" ).serialize(),
		    type: "POST",
		    dataType : "html",
		    success: function( json ) {
		    	
		    	if(json.success="true")	    		
	    		{
		    		//GetAllSalesForReturn($("#DispatchID").val(),$("#DisptachReturnsVehicleNumber").val(),$("#DisptachReturnsDriverName").val(),$("#DisptachVehicleType").val())
		    		
		    		//location = 'DispatchBalanceCashReceipts.jsp?DispatchID='+$("#DispatchIDForInsertion").val()+'&VehicleName='+$("#VehicleNameForTitle").val();
		    		  $.mobile.loading( 'hide');
		    		location = "DispatchBalanceCashReceiptsMain.jsp";
		    		
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

function AddRow(DispatchID,SalesID,OutletID,InvoiceAmount,RemainingBalance,OutletName){
	//alert(document.getElementById("checkb"+SalesID).checked);
	if(document.getElementById("checkb"+SalesID).checked==true){
		$('#myTable').append(
			'<tr id='+SalesID+'><td><input type="hidden" name="DispatchID" id="DispatchID" value="'+DispatchID+'"/><input type="hidden" name="SalesID" id="SalesID" value="'+SalesID+'"/>'+SalesID+'</td><td><input type="hidden" name="OutletID" id="OutletID" value="'+OutletID+'"/><input type="hidden" name="InvoiceAmount1" id="InvoiceAmount1" value="'+InvoiceAmount+'"/>'+OutletID+' - '+OutletName+'</td><td>'+InvoiceAmount+'</td><td>'+RemainingBalance+'</td><td><input type="text" data-mini="true" name="CashReceived1"  id="CashReceived1" onChange="CashReceivedTotalCalculate()" value='+RemainingBalance+' ></td><td><input type="text" id="comments" name="comments" data-mini="true" placeholder="Type your comment here"></td></tr>');
	}else{
		$("input[value='"+InvoiceAmount1+"']").remove();
		
		$('#'+SalesID).remove();
	}
	var sum = 0;
    $("input[id *= 'CashReceived1']").each(function(){
    	//alert($(this).val());
        sum += +$(this).val();
    });
    $("#CashReceivedTotal").val(sum);
    
    var sum1 = 0;
    $("input[id *= 'InvoiceAmount1']").each(function(){
    	//alert($(this).val());
        sum1 += +$(this).val();
    });
    $("#InvoiceAmountTotal").val(sum1);
}

function CashReceivedTotalCalculate(){
	var sum = 0;
    $("input[id *= 'CashReceived1']").each(function(){
    	//alert($(this).maxVal());
        sum += +$(this).val();
    });
    $("#CashReceivedTotal").val(sum);
}
*/

var RowCount = 0;
 
// Function to handle form submission for sales return
function GetAllSalesForReturn(DispatchID, VehicleTypeName, DriverName, vehicleName) {
    $("#DispatchID").val(DispatchID); 
    $("#DisptachReturnsVehicleNumber").val(vehicleName); 	
    $("#DisptachReturnsDriverName").val(DriverName);	
    $("#DisptachVehicleType").val(VehicleTypeName);
 
    // Submit the form
    document.getElementById("ReturnsGenerrateForm").submit();
}
 
// Function to submit dispatch balance cash receipts
function DispatchBalanceCashReceiptsSubmit() {
    // Parse integer values safely
    var InvoiceAmountTotal = parseInt($('#InvoiceAmountTotal').val()) || 0;
    var cashReceivedTotal = parseInt($('#CashReceivedTotal').val()) || 0;
 
    // Validate that the cash received does not exceed the invoice amount
    if (InvoiceAmountTotal < cashReceivedTotal) {
        alert("Remaining Balance Should be equal to Zero");
        return false;
    }
 
    // Set the FullyAdjusted value based on the comparison
    $("#FullyAdjusted").val(InvoiceAmountTotal === cashReceivedTotal ? "1" : "0");
 
    // Show loading indicator
    $.mobile.loading('show');
 
    // AJAX call to submit the form data
    $.ajax({
        url: "distributor/DispatchBalanceCashReceiptsExecute",
        data: $("#DispatchBalanceMainForm").serialize(),
        type: "POST",
        dataType: "json",
        success: function(response) {
            console.log("AJAX Success Response:", response); // Debugging log
            if (response.success === "true") {	    		
                $.mobile.loading('hide');
                // Redirect to the main page upon success
                window.location.href = "DispatchBalanceCashReceiptsMain.jsp";
            } else {
                alert(response.message || "Server could not process the request.");
                $.mobile.loading('hide');
            }
        },
        error: function(xhr, status, error) {
            console.error("AJAX Error:", status, error); // Detailed error log
            alert("Server could not be reached.");
            $.mobile.loading('hide');
        }
    });
 
    return false; 
}
 
// Function to add or remove a row in the table based on checkbox state
function AddRow(DispatchID, SalesID, OutletID, InvoiceAmount, RemainingBalance, OutletName) {
    var checkbox = document.getElementById("checkb" + SalesID);
    if (checkbox && checkbox.checked === true) {
        // Ensure unique IDs by appending SalesID
        var rowId = "row_" + SalesID;
        var dispatchId = "DispatchID_" + SalesID;
        var salesId = "SalesID_" + SalesID;
        var outletId = "OutletID_" + SalesID;
        var invoiceAmountId = "InvoiceAmount1_" + SalesID;
        var cashReceivedId = "CashReceived1_" + SalesID;
        var commentsId = "comments_" + SalesID;
 
        // Append a new row with unique IDs and a toggle button for comments
        $('#myTable').append(
            '<tr id="' + rowId + '">' +
                '<td>' +
                    '<input type="hidden" name="DispatchID" id="' + dispatchId + '" value="' + DispatchID + '"/>' +
                    '<input type="hidden" name="SalesID" id="' + salesId + '" value="' + SalesID + '"/>' +
                    SalesID +
                '</td>' +
                '<td>' +
                    '<input type="hidden" name="OutletID" id="' + outletId + '" value="' + OutletID + '"/>' +
                    '<input type="hidden" name="InvoiceAmount1" id="' + invoiceAmountId + '" value="' + InvoiceAmount + '"/>' +
                    OutletID + ' - ' + OutletName +
                '</td>' +
                '<td>' + InvoiceAmount + '</td>' +
                '<td>' + RemainingBalance + '</td>' +
                '<td>' +
                    '<input type="text" data-mini="true" name="CashReceived1" id="' + cashReceivedId + '" onChange="CashReceivedTotalCalculate()" value="' + RemainingBalance + '" >' +
                '</td>' +
                '<td>' +
                    '<button type="button" onclick="toggleCommentInput(this)" data-mini="true">Add Comment</button>' +
                    '<input type="text" id="' + commentsId + '" name="comments" data-mini="true" placeholder="Type your comment here" style="display:none; margin-top:5px;" />' +
                '</td>' +
            '</tr>'
        );
    } else {
        // Remove the row based on SalesID
        var rowId = "row_" + SalesID;
        $('#' + rowId).remove();
    }
    // Recalculate totals after adding/removing a row
    calculateTotals();
}
 
// Function to toggle the visibility of the comment input field
function toggleCommentInput(button) {
    $(button).hide(); // Hide the "Add Comment" button
    $(button).siblings('input[name="comments"]').show().focus(); // Show and focus the comment input
}
 
// Function to recalculate totals for Cash Received and Invoice Amount
function CashReceivedTotalCalculate(){
    calculateTotals();
}
 
// Helper function to calculate and update total values
function calculateTotals() {
    var sumCashReceived = 0;
    $("input[name='CashReceived1']").each(function() {
        var val = parseFloat($(this).val());
        if (!isNaN(val)) {
            sumCashReceived += val;
        }
    });
    $("#CashReceivedTotal").val(sumCashReceived);
 
    var sumInvoiceAmount = 0;
    $("input[name='InvoiceAmount1']").each(function() {
        var val = parseFloat($(this).val());
        if (!isNaN(val)) {
            sumInvoiceAmount += val;
        }
    });
    $("#InvoiceAmountTotal").val(sumInvoiceAmount);
}
