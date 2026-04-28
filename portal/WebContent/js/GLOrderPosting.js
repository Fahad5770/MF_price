var VirtualDistributorID = 200769;

$( document ).delegate("#GLOrderPostingPage", "pageinit", function() {
	
	
	
});

function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

function isFloat (o) {
	  return ! isNaN (o-0) && o != null;
}

function GetSaleOrderStatus(){
	//alert("SaleOrderNo = "+SaleOrderNo);
	$('#SubmitButton').addClass('ui-disabled');
	$.mobile.showPageLoadingMsg();	
	$.ajax({
	    url: "sap/GLOrderPostingGetOrderInfoJson",
	    data: {
	    	SaleOrderNo: $('#OrderNo').val(),
	    	isEditCase : false
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.hidePageLoadingMsg();
	    	
	    	if( json.success == true ){
	    		
	    		$('#OrderDate').val( json.OrderDate );
	    		$('#EntryDate').val( json.EntryDate );
	    		
	    		//$('#InvoiceNo').val( json.InvoiceNo );
	    		//$('#InvoiceDate').val( json.InvoiceDate );
	    		
		    	$('#InvoiceAmount').val( numberWithCommas(json.OrderAmount) );
		    	$('#InvoiceAmountHidden').val( json.OrderAmount );
		    	
		    	$('#CustomerID').val( json.DistrubutorID );
		    	$('#CustomerName').val( json.DistrubutorName );
		    	
		    	$('#CurrentBalance').val( numberWithCommas(json.CurrentBalance) );
		    	$('#CreditLimit').val( numberWithCommas(json.CreditLimit) );
		    	
		    	var InvoiceAmount = parseFloat(json.OrderAmount);
		    	var CurrentBalance = parseFloat(json.CurrentBalance); 
		    	var CreditLimit = parseFloat(json.CreditLimit);
		    	
		    	if( InvoiceAmount <= (CurrentBalance+CreditLimit) ){ 
		    		$('#Status').val('1');
		    		$('#StatusDiv').html( '' );
		    		$('#SubmitButton').removeClass('ui-disabled');
		    	}else{
		    		$('#Status').val('0');
		    		$('#StatusDiv').html('Insufficient Balance');
		    		//$('#SubmitButton').addClass('ui-disabled');
		    	}
		    	$('#PromotionsTable').empty().trigger('create');
		    	var len = json.PromotionRows.length;

		    	var ApprovalFieldAttribute = "";
		    	if(parseInt($('#CustomerID').val()) == VirtualDistributorID){
		    		//ApprovalFieldAttribute = " disabled='disabled'";
		    	}
		    	
		    	for(var i = 0; i < len; i++){
		    		
		    		var POSNR = json.PromotionRows[i].POSNR;
		    		var MATNR = json.PromotionRows[i].MATNR;
		    		var ARKTX = json.PromotionRows[i].ARKTX;
		    		var VRKME = json.PromotionRows[i].VRKME;
		    		var KWMENG = json.PromotionRows[i].KWMENG;
		    		
		    		var ProductID = json.PromotionRows[i].ProductID;
		    		var RawCases = json.PromotionRows[i].RawCases;
		    		var Units = json.PromotionRows[i].Units;
		    		var TotalUnits = json.PromotionRows[i].TotalUnits;
		    		
		    		var content = '<tr>';
		    				content += '<td>'+POSNR+'<input type=hidden name=POSNR value='+POSNR+'></td>';
		    				content += '<td>'+MATNR+
		    								'<input type=hidden name=MATNR value="'+MATNR+'">'+
		    								'<input type=hidden name=VRKME value="'+VRKME+'">'+
		    								'<input type=hidden name=ProductID value="'+ProductID+'"></td>';
		    				content += '<td>'+ARKTX+'<input type=hidden name=ARKTX value="'+ARKTX+'"></td>';
		    				//content += '<td>'+VRKME+'<input type=hidden name=VRKME value="'+VRKME+'"></td>';
		    				content += '<td>'+RawCases+
		    								'<input type=hidden name=KWMENG value="'+KWMENG+'">'+
		    								'<input type=hidden name=RawCases value="'+RawCases+'"></td>';
		    				content += '<td>'+Units+
							'<input type=hidden name=Units value="'+Units+'">'+
							'<input type=hidden name=TotalUnits value="'+TotalUnits+'"></td>';
		    				
		    				content += '<td><input type=text data-mini=true name=ApprovalID id=ApprovalID'+i+' '+ApprovalFieldAttribute+'  ></td>';
		    			content += '</tr>';
		    		
		    		$('#PromotionsTable').append(content).trigger('create');
		    		
		    	}
		    	
	    	}else{
	    		$('#OrderDate').val( '' );
	    		$('#EntryDate').val( '' );
	    		$('#InvoiceNo').val( '' );
		    	$('#InvoiceAmount').val( '' );
		    	$('#InvoiceAmountHidden').val( '0' );
		    	
		    	$('#CustomerID').val( '' );
		    	$('#CustomerName').val( '' );
		    	
		    	$('#CurrentBalance').val( '' );
		    	$('#CreditLimit').val( '' );
		    	
		    	$('#Status').val('0');
	    		$('#StatusDiv').html( '' );
	    		$('#SubmitButton').addClass('ui-disabled');
	    		
	    		$('#PromotionsTable').empty().trigger('create');
	    		
	    		
	    		alert(json.error);
	    	}

	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.hidePageLoadingMsg();
	    }
	    
	});
}

function FormValidation(){
	
	if( $('#OrderNo').val() == "" ){
		alert("Please enter Order No");
		$('#OrderNo').focus();
		return false;
	}

	if(parseInt($('#CustomerID').val()) != VirtualDistributorID){
		
		var len = $('input[name=ApprovalID]').length;
		if(len > 0){
			var isPromotionEmpty = false;
			var CurRequestID = 0;
			for(var i = 0; i < len; i++){
				if( $('input[name=ApprovalID]')[i].value == "" ){
					CurRequestID = $('input[name=ApprovalID]')[i].id;
					isPromotionEmpty = true;
					break;
				}
			}
			
			if(isPromotionEmpty){
				
				alert('Please enter Approval ID for Promotions');
				$('#'+CurRequestID).focus();
				return false
			}
		}
		
	}
	
	
	$('#SubmitButton').addClass("ui-disabled");
	
	
	$.ajax({
	    url: "cash/GLOrderPostingValidate",
	    
	    data: $('#OrderPostingExecute').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		FormSubmit();
	    	}else{
	    		alert(json.error);
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
	
}

function FormSubmit(){
	/*
	if( $('#OrderNo').val() == "" ){
		alert("Please enter Order No");
		$('#OrderNo').focus();
		return false;
	}
	
	var len = $('input[name=ApprovalID]').length;
	if(len > 0){
		var isPromotionEmpty = false;
		var CurRequestID = 0;
		for(var i = 0; i < len; i++){
			if( $('input[name=ApprovalID]')[i].value == "" ){
				CurRequestID = $('input[name=ApprovalID]')[i].id;
				isPromotionEmpty = true;
				break;
			}
		}
		
		if(isPromotionEmpty){
			alert('Please enter Promotion');
			$('#'+CurRequestID).focus();
			return false
		}
	}
	*/
	
	$('#SubmitButton').addClass("ui-disabled");
	
	
	$.ajax({
	    url: "cash/GLOrderPostingExecute",
	    
	    data: $('#OrderPostingExecute').serialize(), 
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$('#SubmitButton').removeClass("ui-disabled");
	    	if (json.success == "true"){
	    		//alert("true");
	    		window.location = 'GLOrderPosting.jsp';
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
	if(DateVal == ''){
		return '';
	}
	var Year = DateVal.substr(0, 4);
	var Month = DateVal.substr(5, 2);
	var Day = DateVal.substr(8, 2);
	return Day+"/"+Month+"/"+Year;
}

function getDistributorName(){
	
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: $('#DistributorID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#DistributorName').val(json.DistributorName);
				$('#isDistributorValid').val('true');
			}else{
				$('#DistributorName').val('');
				$('#isDistributorValid').val('false');
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
		
}



function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

