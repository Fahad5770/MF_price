
var RowCount = 0;
var Shell250MLCount = 0;
var Shell1000MLCount = 0;
var isAddable = true;
var SAPPackagesArray = new Array();
 

$( document ).delegate("#DeliveryNote", "pageshow", function() {
	document.getElementById('DeliveryNoteSAPOrderNo').focus();
	
	
	//showSearchContent();
	
});

$( document ).delegate("#DeliveryNote", "pageinit", function() {
	
	
	
	
	
	
	

});
 


function GetSalesOrderInfo(){
	//alert("SaleOrderNo = "+$("#OrderNo").val());
	
	$('#alreadyGenerated').addClass('ui-disabled');
	
	$.mobile.showPageLoadingMsg();	
	$.ajax({
	    url: "sap/DraftOutletLoadExecute",
	    data: {
	    	SaleOrderNo: $("#OrderNo").val(),
	    	UniqueVoucherID:$("#UniqueVoucherID").val()
	    	
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) { 
	    	
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	//if(json.error == '1'){
	    		//alert('Failed to connect SAP server.');
	    		//document.getElementById("DeliveryNoteSAPOrderNo").focus();
	    	//	return false;
	    	//}else if(json.error == '2'){
	    		//alert('Sale Order already exists.');
	    		//document.getElementById("DeliveryNoteSAPOrderNo").focus();
	    		//return false;
	    	//}else{
	    		
	    		//if (json.exists == "true"){
					
	    			//if(json.InvoiceNo == "0"){
	    				//alert("Invoice does not exists against this order.");
	    				//return false;
	    			//}
	    			
	    			if(json.success=="true"){
	    	    		//alert("Successfully Inserted!!");
	    				var url = "DraftOutLoadPrint.jsp?DraftID="+$("#OrderNo").val();
	    				window.open(url,'_blank');
	    				window.location = "DraftOutLoad.jsp";
	    	    		
	    	    	}else if(json.success=="false"){
	    	    		$('#alreadyGenerated').removeClass('ui-disabled');
	    	    		//alert("There was error in printing, Duplicate entry");
	    	    	}
	    		//}
	    	//}
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.hidePageLoadingMsg();
	    	document.getElementById("DeliveryNoteSAPOrderNo").focus();
	    }
	});
}


function ViewAlreadyPrint(){
	//alert("SaleOrderNo = "+$("#OrderNo").val());
	
	
	
	$.mobile.showPageLoadingMsg();	
	
	
	var url = "DraftOutLoadPrint.jsp?DraftID="+$("#OrderNo").val();
	window.open(url,'_blank');
	window.location = "DraftOutLoad.jsp";
}





