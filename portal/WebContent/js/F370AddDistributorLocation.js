function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}
var cityName = [];
var cid = [];
$(function() {
	$('#DistributorID').change(function(){
	var DistributorID=$('#DistributorID').val();
	$.mobile.showPageLoadingMsg();
	
		$.ajax({
		    url: "distributor/F370AddDistributorLocationInfoJson",
		    data: {
		    	DistributorID: DistributorID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	 $.mobile.hidePageLoadingMsg();
		    	if (json.success == "true"){
		    		
		    		
		    	 	$('#DistributorName').val(json.DistributorName);
		    	 	
		    	 	
		    	 	$('#Latitude').val("");
		    	 	$('#Longitude').val("");
		    	 	$('#LocationDescription').val("");
		    	 	
		    	 	var strEmpty="";
		    	 	
		    	 	$("#DistributorLocationTableBody").html(strEmpty);
		    	 	//var str ='<table style="width:80%; margin-top:35px;margin-bottom:35px;">'+
		    	 	var str ='<tr style="background: lightgray;height:40px">' +
							'<th style="text-align:left;margin-left:7.5px">Latitude</th>'+
							'<th style="text-align:left;margin-left:7.5px">Longitude</th>'+
							'<th style="text-align:left;margin-left:7.5px">Address</th>'+ 
							'<th style="text-align:left;margin-left:7.5px">Phone</th>'+
							'<th style="text-align:left;margin-left:7.5px">Location Description</th>'+
							
						'</tr>'+
		    	 		'<tr><td>&nbsp</td></tr>';
		    	 	
		    	 	
		    	 		var counter=0;
			    		for(var i = 0; i < json.rows.length; i++){
			    			
			    			
			    			//alert();
			    			var lat=json.rows[i].lat;
			    			var lng=json.rows[i].lng;
			    			var locDescription=json.rows[i].description;
			    			
			    			var address=json.rows[i].address;
			    			var phone=json.rows[i].phone;
			    			
			    		/*	var city_id=json.rows[i].city_id;
			    			var city=json.rows[i].city;*/
			    		
			    		
			    			
			    			str +="<tr id="+counter+">"+
			    				'<td><input type = "text" placeholder = "Latitude"  Read1only data-mini="true" name="Latitude1" value="'+lat+'"><input type="hidden" name="DistributorLocationMainFormLAT"  value="'+lat+'" ></td>'+
								'<td><input type = "text" placeholder = "Longitude" Read1only  data-mini="true"   name="Longitude1" value="'+lng+'"><input type="hidden" name="DistributorLocationMainFormLNG"  value="'+lng+'" ></td>'+
								'<td><input type = "text" placeholder = "Address"  Read1only    data-mini="true"   name="Address1" value="'+address+'"><input type="hidden" name="DistributorLocationMainFormAddress"  value="'+address+'" ></td>'+
								/*'<td><select id="City"  name="City1" data-mini="true">';
								for(j=0; j < json.rows1.length; j++){
									
									cityName[j]=json.rows1[j].city_name;
									cid[j]=json.rows1[j].city_id;
									
									str += '<option value='+json.rows1[j].city_id+' ';
									if(city_id == parseInt(json.rows1[j].city_id )){
										str +="selected"
									}
									str += '>'+json.rows1[j].city_name+'</option>';
			    		}
								str += '</select></td>'+*/
			    		  '<td><input type = "text" placeholder = "Phone"  Rea1donly   data-mini="true"    name="Phone1" value="'+phone+'"><input type="hidden" name="DistributorLocationMainFormPhone"  value="'+phone+'" ></td>'+
								'<td><input type = "text" placeholder = "Location Description"  data-mini="true"  Read1only   name="LocationDescription1" value="'+locDescription+'"></td>'+
								'<td><a data-icon="delete" data-theme="b"  data-mini="true" data-role="button" data-inline="true"  href="#" onClick="Deletelocation('+counter+')" aclass="ui-disabled" >Delete</a></td>'+
							
							'</tr>';
			    				
			    			counter++; 
			    		}
			    		str +="</table>";
			    		$("#DistributorLocationTableBody").append(str).trigger('create');
			    		
			    		$('#MaxRowNumber').val(counter);
						//$('#RowMaxID').val(RowMaxID);
		    	 			    	 	
		    	}else{
		    		alert("Distributor ID is not valid");
		    	}
		    	
		    },
		 
	 });
	
 });
	
	
	 
	
	
	
	
})

function Addlocation(){
		if($('#DistributorID').val() ==''){
			alert("Please enter Distributor to add location");
			return false;
		}
		
		if($('#Latitude').val() == ''){
			alert("Please enter Latitude");
			return false;
		}
		
		if($('#Longitude').val() == ''){
			alert("Please enter Longitude");
			return false;
		}
		
		if($('#LocationDescription').val() == ''){
			alert("Please enter Location Description");
			return false;
		}
		
		
		if($('#Address').val() == ''){
			alert("Please enter Address");
			return false;
		}
		
		if($('#City').val() == ''){
			alert("Please enter City");
			return false;
		}
		
		if($('#Phone').val() == ''){
			alert("Please enter Phone #");
			return false;
		}
		
		
		
		isAlreadyEntered = false;
		
		var existingLat = document.getElementsByName("DistributorLocationMainFormLAT");
		var existingLng = document.getElementsByName("DistributorLocationMainFormLNG");
		
		
		var NewLAT=AddNumberOFZerosToLATLngValue($('#Latitude').val());
		var NewLNG=AddNumberOFZerosToLATLngValue($('#Longitude').val());
		var NewAddress=$('#Address').val();
		var NewCity=$('#City').val();
		var NewPhone=$('#Phone').val();
	
		
		
		
		
		var x = 0;
		for (x = 0; x < existingLat.length; x++){
			
			
			
			
			if (existingLat[x].value == NewLAT && existingLng[x].value == NewLNG){
				isAlreadyEntered = true;
				break;
			}
		}
			 	
		    		
    	if (isAlreadyEntered !=true){
    		//	alert("$('#MaxRowNumber').val() "+$('#MaxRowNumber').val());
    				var RowMaxID = parseInt($('#MaxRowNumber').val()) + 1;
    				var str ="";
		    	 	if(RowMaxID==0){
				    		 str+='<tr >' +
									'<th style="text-align:left">Latitude</th>'+
									'<th style="text-align:left">Longitude</th>'+
									'<th style="text-align:left">Address</th>'+
									/*'<th style="text-align:left">City</th>'+*/
									'<th style="text-align:left">Phone</th>'+
									'<th style="text-align:left">Location Description</th>'+
									
								'</tr>'+
				    		 
								'<tr><td>&nbsp</td></tr>';
		    	 	}		
	    			str +="<tr id="+RowMaxID+">"+
	    				'<td><input type = "text" data-mini="true" placeholder = "Latitude"  Re1adonly   name="Latitude1" value="'+NewLAT+'"><input type="hidden" name="DistributorLocationMainFormLAT"  value="'+NewLAT+'" ></td>'+
						'<td><input type = "text" data-mini="true" placeholder = "Longitude"  Read1only     name="Longitude1" value="'+NewLNG+'"><input type="hidden" name="DistributorLocationMainFormLNG"  value="'+NewLNG+'" ></td>'+
						'<td><input type = "text" data-mini="true" placeholder = "Address"  Reado1nly     name="Address1" value="'+NewAddress+'"><input type="hidden" name="DistributorLocationMainFormAddress"  value="'+NewAddress+'" ></td>'+
						/*'<td><select  name="City1" value="'+NewCity+'" data-mini="true">';
					for(j=0; j < cityName.length; j++){
						
						str +='<option value='+cid[j]+' ';
						if( cid[j] == parseInt(NewCity)){
							str +="selected";
						}
						str +='>'+cityName[j]+'</option>';
    		}
					'<input type="hidden" name="DistributorLocationMainFormCity"  value="'+NewCity+'" ></select></td>'+*/
					'<td><input type = "text" data-mini="true" placeholder = "Phone"  Rea1donly     name="Phone1" value="'+NewPhone+'"><input type="hidden" name="DistributorLocationMainFormPhone"  value="'+NewPhone+'" ></td>'+
						'<td><input type = "text"  data-mini="true" placeholder = "Location Description"   Rea1donly    name="LocationDescription1" value="'+$('#LocationDescription').val()+'"><input type="hidden" name="DistributorLocationMainFormDes"  value="'+$('#LocationDescription').val()+'" ></td>'+
						'<td><a data-icon="delete" data-mini="true" data-theme="b"  data-mini="true" data-role="button" data-inline="true"  href="#" onClick="Deletelocation('+RowMaxID+')" aclass="ui-disabled" >Delete</a></td>'+
					
					'</tr>';
	    				
	    			//counter++;
	    	
	    		str +="</table>";
	    		$("#DistributorLocationTableBody").append(str).trigger('create');
	    		
	    		
				$('#MaxRowNumber').val(RowMaxID);
    	 			    	 	
    	}else{
    		alert("location already exists");
    	}
			    	
			 
			 
		 
}

function Deletelocation(RowID){
	
	$("#"+RowID).remove();
	/*RowCount--;
	if( RowCount < 1){
		$('#NoOutletRow').css('display', 'table-row');
		//$('#DeliveryNoteSave').addClass('ui-disabled');
	}*/
	
}


function DistributorLocationSubmit(){
	
	$.ajax({
	    url: "distributor/F370AddDistributorLocationExecute",
	    
	    data: $("#DistributorLocationForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		location = "F370AddDistributorLocation.jsp";
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



function AddNumberOFZerosToLATLngValue(ValueToAdd){
	
   var myStr = ValueToAdd;
   var res = myStr.split(".");
   var afterSplit=res[1];
   var afterSplitLength=afterSplitLength;
 
   var GetNumberOFZeros=0;
   if(afterSplitLength<19){
   	GetNumberOFZeros=18-afterSplitLength;
   }
   var AddNumberOFZeros=0;
   for(var i=0;GetNumberOFZeros>i;i++){
         
   	AddNumberOFZeros=AddNumberOFZeros+"0";
   }
   var afteraddingZeros=myStr+AddNumberOFZeros;
   return afteraddingZeros;
	
}


