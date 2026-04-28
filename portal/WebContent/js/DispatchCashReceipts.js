

var RowCount = 0;

function enableCommentField(rowNum) {
    var commentDiv = document.getElementById('commentDynamic' + rowNum);
    if (commentDiv) {
    	
        if (!document.getElementById('comments' + rowNum)) {
           
            var input = document.createElement('input');
            input.type = 'text';
            input.id = 'comments' + rowNum;
            input.name = 'comments';
            input.className = 'comment-field';
            input.setAttribute('data-mini', 'true');
            input.placeholder = 'Type your comment here';
            input.style.marginRight = '20px';
            input.style.height = '32px';
            input.style.width = '100%';
            input.style.border = '2px lightgray solid';
            input.style.borderRadius = '20px';
     
            commentDiv.appendChild(input);
        }
    }
}



function GetAllSalesForReturn(DispatchID,VehicleTypeName,DriverName,vehicleName)
{
	$("#DispatchID").val(DispatchID); //setting dispatch id	
	$("#DisptachReturnsVehicleNumber").val(vehicleName); //setting dispatch id	
	$("#DisptachReturnsDriverName").val(DriverName); //setting dispatch id	
	$("#DisptachVehicleType").val(VehicleTypeName); //setting dispatch id	
	
	//alert(document.getElementById("ReturnsGenerrateForm"));
	document.getElementById("ReturnsGenerrateForm").submit();
}


function DispatchCashReceiptsSubmit()
{
	var remainingBalance= parseInt($('#DispatchCashReceiptsRemainingAmount').val());
	//alert(remainingBalance);
	if (remainingBalance < 0){
		alert("Cash Received can not be greater than total invoice amount");
		return false;
	}
		
	$.mobile.loading( 'show');
	

	$.ajax({
	    url: "distributor/DispatchCashReceiptsExecute",
	    data: $("#DispatchReturnMainForm" ).serialize(),
	    type: "POST",
	    dataType : "html",
	    success: function( json ) {
	    	
	    	if(json.success="true")	    		
    		{
	    		//GetAllSalesForReturn($("#DispatchID").val(),$("#DisptachReturnsVehicleNumber").val(),$("#DisptachReturnsDriverName").val(),$("#DisptachVehicleType").val())
	    		
	    		//location = 'DispatchCashReceipts.jsp?DispatchID='+$("#DispatchIDForInsertion").val()+'&VehicleName='+$("#VehicleNameForTitle").val();
	    		  $.mobile.loading( 'hide');
	    		location = "DispatchCashReceiptsMain.jsp";
	    		
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


function DispatchCashReceiptsCalculate(TotalSales)
{

	var CashReceived=0;
	if( $('#DispatchCashReceiptsCashReceived').val() == '' ){
		CashReceived = 0;
	}else{
		CashReceived = $('#DispatchCashReceiptsCashReceived').val();
	}
	var OrderCashReceived=0;
	var RemainingBalance=0;
	var TotalCashReceived=0;
	for (var i=1; i<=parseInt(TotalSales);i++)
	{

		if( $('#OrderCashReceipts_'+i).val() == '' ){
			OrderCashReceived = 0;
		}else{
			OrderCashReceived = $('#OrderCashReceipts_'+i).val();
		}
		TotalCashReceived +=parseFloat(OrderCashReceived);
	}
	
	RemainingBalance= parseFloat(CashReceived)-TotalCashReceived;
	$('#DispatchCashReceiptsCashReceived').val(parseFloat(TotalCashReceived).toFixed(2));
	$('#DispatchCashReceiptsRemainingAmount').val(parseFloat(RemainingBalance).toFixed(2));
	
}