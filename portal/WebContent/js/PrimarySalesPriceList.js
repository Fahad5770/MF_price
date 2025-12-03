
$( document ).delegate("#GatePass", "pageshow", function() {
	document.getElementById("GatePassBarcode").focus();
	showDeliveredGatePassQueue();
	setInterval(showDeliveredGatePassQueue, 60000);	
});


function setSubmitButton(status){
	  if( status == true ){
		  $('#GatePassSave').removeClass("ui-disabled");
	  }else{
		  $('#GatePassSave').addClass("ui-disabled");
		  $("#SearchContent").html("Barcode doesn'nt exists.");
	  }
}

function showSearchContent(){
	if( $('#GatePassBarcode').val() != '' ){
		$.mobile.showPageLoadingMsg();
		$.get('DeliveryNotePrint.jsp?Document=GatePass&Barcode='+$('#GatePassBarcode').val(), function(data) {
			$.mobile.hidePageLoadingMsg();
			$("#SearchContent").html(data);
			
		});
	}
	document.getElementById("GatePassBarcode").focus();
	return false;

}

function PriceListSubmit(){
	
	$('#GatePassSave').addClass("ui-disabled");
	
	$.ajax({
	    url: "primarysales/PriceListExecute",
	    data: $("#PriceListForm" ).serialize(),	   
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "PrimarySalesPriceList.jsp";
	    		
	    	}else{
	    		alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function showDeliveredGatePassQueue(){
	//alert();
		//$.mobile.showPageLoadingMsg();
		$.get('GatePassQueue.jsp', function(data) {
			//$.mobile.hidePageLoadingMsg();
			//alert(data);
			$("#DeliveredGatePassQueue").html(data);
			$("#DeliveredGatePassQueue").trigger('create');
			
		});
	return false;

}

function AddPriceRows(){
	var html="";
	
	html+="<tr>"+
				"<td style='width:25%;'>"+
					"<input type='text' placeholder='Distributor ID' id='DistributorID' name='DistributorID' data-mini='true'>"+
				"</td>"+
				"<td  style='width:25%;'>"+
					"<input type='text' placeholder='Product Code' id='ProductCode' name='ProductCode' data-mini='true'>"+
				"</td>"+
				"<td  style='width:25%;'>"+
					"<input type='text' placeholder='Price' id='Price' name='Price' data-mini='true'>"+
				"</td>"+
				"<td  style='width:25%;'>&nbsp;</td>"+
		  "</tr>";
		$("#PriceListAddRows").append(html);
		$("#PriceListAddRows").trigger('create');
		return false;
}