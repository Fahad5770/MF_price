
		var RowCount = 0;
		var isChangeEmployeeRequest = false;
		 
		$( document ).on( "pageinit", "#SamplingPlannedSalesPage", function() {
		
			$('#OutletID').on('dblclick', function(e, data){
				
				$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupOutletInit();
				} );
				$('#LookupOutletSearch').popup("open");
				
			});	
		
		});
		
		
		
		function FormSubmit(){
			
			if( $('#OutletID').val() == "" ){
				setTimeout("$('#OutletID').focus();", 100);
				return false;
			}
			if( isInteger($('#OutletID').val()) == false ){
				setTimeout("$('#OutletID').focus();", 100);
				$('#OutletName').val('');
				return false;
			}
			
			var len = $('input[name=qty]').length;
			var EmptyFlag = true;
			for(var i = 0; i < len; i++){
				if($('input[name=qty]')[i].value != ""){
					EmptyFlag = false;
					break;
				}
			}
			
			var len2 = $('input[name=qty_t2]').length;
			var EmptyFlag2 = true;
			for(var i = 0; i < len2; i++){
				if($('input[name=qty_t2]')[i].value != ""){
					EmptyFlag2 = false;
					break;
				}
			}
			
			if(EmptyFlag && EmptyFlag2){
				alert('Please select atleast one Package');
				setTimeout("$('input[name=qty]')[0].focus();", 100);
				return false;
			}
			
			
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
			    url: "sampling/SamplingPlannedSalesExecute",
			    data: $("#MainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	$.mobile.hidePageLoadingMsg();
			    	if (json.success == "true"){
			    		window.location="SamplingPlannedSales.jsp"; 
			    	}else{
						alert(json.error);
			    		//alert("Server could not be reached.");
			    	}
			    },
			    error: function( xhr, status ) {
			    	$.mobile.hidePageLoadingMsg();
			    	alert("Server could not be reached.");
			    }
			});
			
		}
		
		function getDistributorTargetInfoJson(EditID){
			
			$.ajax({
			    url: "distributor/DistributorTargetEditInfoJson",
			    data: {
			    	EditID: EditID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.success == "true"){
			    		
			    		$('#DistributorID').val(json.DistributorID);
			    		$('#Month').val(json.Month).change();
			    		$('#Year').val(json.Year).change();
			    		$('#StartDate').val(json.StartDate);
			    		$('#EndDate').val(json.EndDate);
			    		
			    		$('#TargetType').val(json.TargetTypeID).change();
			    		
			    		getDistributorInfoJson(json);
			    		
			    	}else{
			    		alert(json.error);
			    	}
			    	
			    },
			    error: function( xhr, status ) {
			    	alert("Server could not be reached.");
			    }
			});
		}
		
		
		
		function OutletSearchCallBackSamplingPlannedSale(SAPCode, OutletName){
			
			$('#OutletID').val(SAPCode);
			$('#OutletName').val(OutletName);
			getOutletInfoJson();
		}
		
		
		
		function getOutletInfoJson(){
			
			if( $('#OutletID').val() == "" ){
				return false;
			}
			if( isInteger($('#OutletID').val()) == false ){
				setTimeout("$('#OutletID').focus();", 100);
				$('#OutletName').val('');
				return false;
			}
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
				url: "sampling/SamplingPlannedSalesGetOutletInfoJson",
				data: {
					OutletID: $('#OutletID').val(),
				},
				type:"POST",
				dataType: "json",
				success: function(json){
					$.mobile.hidePageLoadingMsg();
					if( json.success == "true" ){
						$('#OutletName').val(json.OutletName);
						$('#StartDate').val(json.ActivatedOn);
						$('#EndDate').val(json.EndDate);
						
						$('#RequestID').val(json.RequestID);
						$('#ActivatedOn').val(json.ActivatedOn);
						
					}else{
						$('#OutletName').val('');
					}
				},
				error: function(xhr, status){
					$.mobile.hidePageLoadingMsg();
					alert("Server could not be reached.");
				}
				
			});
			
			
		}
		
		function UpdateAnyField(Value, PackageID){
			$('#qty_'+PackageID+'_any').val(Value);
		}
		
		function UpdateAnyField2(Value, PackageID){
			$('#qty_'+PackageID+'_any_t2').val(Value);
		}
		
		function CheckQuantity(thisID, PackageID){
			
			var len = $('input[name=qty_'+PackageID+']').length;
			var total_package_qty = parseInt($('#qty_'+PackageID).val());
			
			var total = 0;
			for(var i = 0; i < len; i++){
				var BrandQty = $('input[name=qty_'+PackageID+']')[i].value;
				
				if(BrandQty == ""){
					BrandQty = "0";
				}
				
				total += parseInt(BrandQty);
			}
			//alert(total_package_qty);
			var any_val = total_package_qty - total;
			if(any_val < 0){
				alert('Brand Quantity should no be greater than Package Quantity');
				document.getElementById("qty_"+PackageID).focus();
				$('#'+thisID).val('0');
				return false;
			}
			$('#qty_'+PackageID+'_any').val( any_val );
			
			
		}
		
		function CheckQuantity2(thisID, PackageID){
			
			var len = $('input[name=qty_'+PackageID+'_t2]').length;	// brands qty length
			var total_package_qty = parseInt($('#qty_'+PackageID+'_t2').val()); // current package qty
			
			var total = 0;
			for(var i = 0; i < len; i++){
				var BrandQty = $('input[name=qty_'+PackageID+'_t2]')[i].value;
				
				if(BrandQty == ""){
					BrandQty = "0";
				}
				
				total += parseInt(BrandQty);
			}
			//alert(total_package_qty);
			var any_val = total_package_qty - total;
			if(any_val < 0){
				alert('Brand Quantity should no be greater than Package Quantity');
				document.getElementById("qty_"+PackageID+"_t2").focus();
				$('#'+thisID).val('0');
				return false;
			}
			
			$('#qty_'+PackageID+'_any_t2').val( any_val );
			
			
		}
		 
		function ToggleBrands(TableID){
			if($('#'+TableID).css('display')=='table'){
				$('#'+TableID).css('display', 'none');
			}else{
				$('#'+TableID).css('display', 'table');
			}
		}
		
		
		
		function setDateFormat(DateObj){
			
			var Date = DateObj.getDate();
			var Month = (DateObj.getMonth()+1);
			var Year = DateObj.getFullYear();
			
			if(Date < 10){
				Date = "0"+Date;
			}
			
			if(Month < 10){
				Month = "0"+Month;
			}
			
			return Date+"/"+Month+"/"+Year;
			
		}
		
		