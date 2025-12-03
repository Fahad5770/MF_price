
		var RowCount = 0;
		var isChangeEmployeeRequest = false;
		 
		$( document ).on( "pageinit", "#DistributorTargets", function() {
		
			
			if(isEditCase){
				getDistributorTargetInfoJson(EditID);
			}
			
			$('#DistributorID').on('dblclick', function(e, data){
				
				$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupDistributorInit();
				} );
				
				$('#LookupDistributorSearch').popup("open");
				
			});	
		
		});
		
		
		
		function DistributorTargetsSubmit(){
			
			if( $('#DistributorID').val() == "" ){
				alert("Please select Distributor");
				document.getElementById("DistributorID").focus();
				return false;
			}
			
			if( $('#Month').val() == "" ){
				alert("Please select Month");
				document.getElementById("Month").focus();
				return false;
			}
			
			if( $('#Year').val() == "" ){
				alert("Please select Year");
				document.getElementById("Year").focus();
				return false;
			}
			
			if( $('#TargetType').val() == "" ){
				alert("Please select Target Type");
				document.getElementById("TargetType").focus();
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
			
			if(EmptyFlag){
				alert('Please select atleast one Package');
				setTimeout("$('input[name=qty]')[0].focus();", 100);
				return false;
			}
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
			    url: "distributor/DistributorTargetExecute",
			    data: $("#DistributorTargetsMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	$.mobile.hidePageLoadingMsg();
			    	if (json.success == "true"){
			    		window.location="DistributorTargets.jsp"; 
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
		
		
		function showSearchContent(){
			
			$.get('DistributorTargetsSearch.jsp?DistributorID='+$('#DistributorTargetsSearchDistributorID').val(), function(data) {
				
				  $("#SearchContent").html(data);
				  $("#SearchContent").trigger('create');
				  
			});
			
			return false;
			
		}
		
		
		
		function setDistributorLookupAtDistributorTargets(SAPCode, DistributorName){
			$('#DistributorID').val(SAPCode);
			$('#DistributorName').val(DistributorName);
		}
		
		
		function getDistributorInfoJson(EditInfoJson){
			
			if( $('#DistributorID').val() == "" ){
				return false;
			}
			if( isInteger($('#DistributorID').val()) == false ){
				setTimeout("$('#DistributorID').focus();", 100);
				return false;
			}
			
			$.mobile.showPageLoadingMsg();
			$.ajax({ 
				url: "common/GetDistributorInfoJson",
				data: {
					DistributorID: $('#DistributorID').val()
				},
				type:"POST",
				dataType: "json",
				success: function(json){ 
					if( json.exists == 'true' ){
						$('#DistributorName').val(json.DistributorName);
						$('#MonthCycle').val(json.MonthCycle);
						$('#ProductGroupID').val(json.ProductGroupID);
						
						getDatePeriod();
						
						var content = "";
						
						for(var i = 0; i < json.ProductGroupRows.length; i++){
							content += "<tr>";
								content += "<td style='width: 30%'>"+json.ProductGroupRows[i].PackageLabel+"<input type='hidden' name='package_id' id='package_id' value='"+json.ProductGroupRows[i].PackageID+"' ></td>";
								content += "<td style='width: 20%'><input type='text' name='qty' id='qty_"+json.ProductGroupRows[i].PackageID+"' value='' data-mini='true' size='10' style='text-align: center' onkeyup='UpdateAnyField(this.value, "+json.ProductGroupRows[i].PackageID+")' onchange='CheckQuantity(0, "+json.ProductGroupRows[i].PackageID+")' ></td>";
								content += "<td style='width: 20%'><input type='button' value=' >> ' data-mini='true' onclick='ToggleBrands(\"brand_table_"+json.ProductGroupRows[i].PackageID+"\")' ></td>";
								content += "<td style='width: 30%; text-align: right'>";
							
									content += "<table style='width: 100%; display: none' id='brand_table_"+json.ProductGroupRows[i].PackageID+"'>";
									
										content += "<tr>";
											content += "<td nowrap style='width: 50%'>Any</td>";
											content += "<td><input type='text' name='qty_"+json.ProductGroupRows[i].PackageID+"_any' id='qty_"+json.ProductGroupRows[i].PackageID+"_any' value='' data-mini='true' style='text-align: center; width: 100px' readonly='readonly'  tabindex='-1' ></td>";
										content += "</tr>";
										
										for(var j = 0; j < json.ProductGroupRows[i].BrandRows.length; j++){
											content += "<tr>";
												content += "<td nowrap style='width: 50%'>"+json.ProductGroupRows[i].BrandRows[j].BrandLabel+"</td>";
												content += "<td><input type='text' name='qty_"+json.ProductGroupRows[i].PackageID+"' id='qty_"+json.ProductGroupRows[i].PackageID+"_"+json.ProductGroupRows[i].BrandRows[j].BrandID+"' value='' data-mini='true' style='text-align: center; width: 100px' onchange='CheckQuantity(this.id, "+json.ProductGroupRows[i].PackageID+")'  > <input type='hidden' name='brand_id_"+json.ProductGroupRows[i].PackageID+"' id='brand_id_"+json.ProductGroupRows[i].PackageID+"' value='"+json.ProductGroupRows[i].BrandRows[j].BrandID+"' > </td>";
											content += "</tr>";
										}
										
									content += "</table>";

							
								content += "</td>";
							
							
							content += "</tr>";
						}
						
						$("#DistributorTargetsTableBody").html(content).trigger('create');
						
						if(EditInfoJson != ""){
							setEditValues(EditInfoJson);
						}
						
						$.mobile.hidePageLoadingMsg();
					}else{
						$('#DistributorName').val('');
						$('#MonthCycle').val('');
						$('#ProductGroupID').val('');
						$("#DistributorTargetsTableBody").html('').trigger('create');
						$.mobile.hidePageLoadingMsg();
					}
				},
				error: function(xhr, status){
					$.mobile.hidePageLoadingMsg();
					alert("Server could not be reached.");
				}
				
			});
			return false;
		}
		
		
		function getDistributorName(){
			
			$.mobile.showPageLoadingMsg();
			$.ajax({ 
				url: "common/GetDistributorInfoJson",
				data: {
					DistributorID: $('#DistributorTargetsSearchDistributorID').val()
				},
				type:"POST",
				dataType: "json",
				success: function(json){
					$.mobile.hidePageLoadingMsg();
					if( json.exists == 'true' ){
						$('#DistributorTargetsSearchDistributorName').val(json.DistributorName);
					}
				},
				error: function(xhr, status){
					$.mobile.hidePageLoadingMsg();
					alert("Server could not be reached.");
				}
				
			});
			return false;
		}
		
		function setEditValues(json){
			var len = json.PackageRows.length;
			for(var i = 0; i < len; i++){
				$('#qty_'+json.PackageRows[i].PackageID).val(json.PackageRows[i].Quantity);
				for(var j = 0; j < json.PackageRows[i].BrandRows.length; j++ ){
					$('#qty_'+json.PackageRows[i].PackageID+'_'+json.PackageRows[i].BrandRows[j].BrandID).val(json.PackageRows[i].BrandRows[j].Quantity);
				}
				CheckQuantity(0, json.PackageRows[i].PackageID);
			}
		}
		
		function UpdateAnyField(Value, PackageID){
			$('#qty_'+PackageID+'_any').val(Value);
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
		
		function ToggleBrands(TableID){
			if($('#'+TableID).css('display')=='table'){
				$('#'+TableID).css('display', 'none');
			}else{
				$('#'+TableID).css('display', 'table');
			}
		}
		
		function getDatePeriod(){
			
			if( $('#MonthCycle').val() == "" ){
				return false;
			}
			if( $('#Month').val() == "" ){
				return false;
			}
			if( $('#Year').val() == "" ){
				return false;
			}
			
			var StartDate = new Date();
			StartDate.setDate($('#MonthCycle').val());
			StartDate.setMonth($('#Month').val() - 1);
			StartDate.setYear($('#Year').val());
			$('#StartDate').val(setDateFormat(StartDate));
			
			//var EndDate = new Date();
			//var Added30Days = StartDate.getTime() + 1000*60*60*24*30; // Offset by 30 day;
			//EndDate.setTime( Added30Days );
			StartDate.setMonth( StartDate.getMonth() + 1 );
			StartDate.setDate( StartDate.getDate() - 1 );
			$('#EndDate').val(setDateFormat(StartDate));
			
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
		
		function DistributorTargetsDatesSubmit(){
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
			    url: "distributor/DistributorTargetDatesExecute",
			    data: $("#DistributorTargetsDatesMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	$.mobile.hidePageLoadingMsg();
			    	if (json.success == "true"){
			    		window.location="DistributorTargetsDates.jsp"; 
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
		
		