
    $( document ).on( "pageinit", "#PriceList", function() {     	
    	$.get('PriceListDataScope.jsp?pricelistid='+$("#PriceListIDForWhole").val()+'&UserID='+$("#UserID").val(), function(data) {   			
			
    		$("#PriceListDataScope").html(data);
			  $("#PriceListDataScope").trigger('create');
  		});
    	
    	setTimeout(function(){
    		$('#DistributorID2').on('dblclick', function(e, data){        		
        		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
        			lookupDistributorInit();
        		} );
        		$('#LookupDistributorSearch').popup("open");
        		$("#isSecondDistCall").val("0");
        	});
    	}, 2000);
    });
    
    function SavePriceList()
    {
    	
    	var isEmpty = false;
    	if($("#PriceListLabel").val()=="")
    	{
    		isEmpty = true;
    		document.getElementById('PriceListLabel').focus();
    	}
    	if($("#PriceListValidFrom").val()=="")
    	{
    		isEmpty = true;
    		document.getElementById('PriceListValidFrom').focus();
    	}
    	if($("#PriceListValidTo").val()=="")
    	{
    		isEmpty = true;
    		document.getElementById('PriceListValidTo').focus();
    	}
    	
    	if(!isEmpty)
    	{ 			
			$.ajax({    		
	    		url: "inventory/PriceListExecute",    			
	    		    data: $("#PriceListForm" ).serialize(),
	    		    type: "POST",
	    		    dataType : "json",
	    		success:function(json){
	    			if(json.success == "true"){    				
	    				PriceListAdditionalRightSubmit(json.price_list_id); 				
	    			}else{
	    				alert(json.error);
	    				
	    			}
	    		},
	    		error:function(xhr, status){
	    			alert("Server could not be reached.");
	    		}
	    	});
    	}	
    }
    
    function PriceListAdditionalRightSubmit(PriceList)
    {
    	$("#PriceListIDForWhole").val(PriceList);//setting price list
    	$.ajax({
		    url: "inventory/PriceListAdditionalRightExecute",
		    
		    data: $("#PriceListForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){		    		
		    		window.location="PriceList.jsp";
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
    
    function FillBottles(id,obj)
    {
    	UnitPerSku = $("#PriceListUPSku_"+id).val();
    	bottles = obj.value/UnitPerSku;
    	$("#PriceListBottle_"+id).val(parseFloat(bottles).toFixed(2));    	
    	//alert(parseFloat(bottles).toFixed(2));
    }
    
    function LoadPerticularPriceList(PriceListID)
    {
    	//alert($("#isEditCase").val());
    	//window.location="PriceList.jsp?Plist="+PriceListID;
    	document.getElementById("PriceListEditID").value=PriceListID;
    	document.getElementById("PriceListEditForm").submit();
    }
    
    function AddRegion()
    {    	
    	$.ajax({
		    url: "inventory/PriceListGetActivePriceListInfoJson",
		    
		    data: {
    			RegionID: $("#RegionSelect option:selected").val(),
    			DistributorGroupID: 0,
    			DistributorID:0,
    			PJPID:0
    		},
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.region_exist != "true"){		    		
		    		//adding region
		    		var RegionAlreadyAdded = false;    	
		        	$('#AddeddRegion li input').each(function(){ 
		        		var NewInsertedListID = $("#RegionSelect option:selected").val()+",Region";	        		
		        		if($(this).val().trim() == NewInsertedListID.trim())
		        		{
		        			RegionAlreadyAdded = true;		        			
		    			}
		        	});

		        	var FunctionName = "onClick=RemoveList('RegionDynamicallyAdded_"+$("#RegionSelect option:selected").val()+"')";
		        	
		        	if(!RegionAlreadyAdded && $("#RegionSelect option:selected").val() != "-1")
		    		{
		        		var AppendedString = "<li data-mini='true' data-icon='delete' id='RegionDynamicallyAdded_"+$("#RegionSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='RegionDynamicallyAddedHidden_"+$("#RegionSelect option:selected").val()+"' name='RegionIDhiddenfield' value='"+$("#RegionSelect option:selected").val()+",Region'/><a href='#'>"+$("#RegionSelect option:selected").text()+"</a></li>";
		        		
		        		$("#populateRegionDataUl").append(AppendedString);    	
		            	$("#populateRegionDataUl").listview("refresh");
		    		}
		    	}else{
					alert($("#RegionSelect option:selected").text()+' already has active price list "'+json.price_list_name+'"');
		    		//alert(json.error);
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
    }
    
    function AddGroupDistributor()
    {
    	
    	$.ajax({
		    url: "inventory/PriceListGetActivePriceListInfoJson",
		    
		    data: {
    			RegionID: 0,
    			DistributorGroupID: $("#GroupDistributorSelect option:selected").val(),
    			DistributorID:0,
    			PJPID:0
    		},
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.region_exist != "true"){		    		
		    		var GroupAlreadyAdded = false;
		        	$("#WarehouseeSave").removeClass('ui-disabled');
		        	$('#AddeddGroupDistributor li input').each(function(){ 
		        		var NewInsertedListID = $("#GroupDistributorSelect option:selected").val()+",GroupDistributor";	        		
		        		if($(this).val().trim() == NewInsertedListID.trim())
		        		{
		        			GroupAlreadyAdded = true;		        			
		    			}
		        	});

		        	var FunctionName = "onClick=RemoveList('GroupDistributorDynamicallyAdded_"+$("#GroupDistributorSelect option:selected").val()+"')";
		        	
		        	if(!GroupAlreadyAdded && $("#GroupDistributorSelect option:selected").val() != "-1")
		    		{
		        		var AppendedString = "<li data-mini='true' data-icon='delete' id='GroupDistributorDynamicallyAdded_"+$("#GroupDistributorSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='GroupDistributorDynamicallyAddedHidden_"+$("#GroupDistributorSelect option:selected").val()+"' name='GroupDistributorhiddenfield' value='"+$("#GroupDistributorSelect option:selected").val()+",GroupDistributor'/><a href='#'>"+$("#GroupDistributorSelect option:selected").text()+"</a></li>";
		        		
		        		$("#populateGroupDistributorDataUl").append(AppendedString);    	
		            	$("#populateGroupDistributorDataUl").listview("refresh");
		    		}
		    	}else{
					alert($("#GroupDistributorSelect option:selected").text()+' already has active price list "'+json.price_list_name+'"');
		    		//alert(json.error);
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
    }
    
    function AddDistributor()
    {
    	
    	$.ajax({
		    url: "inventory/PriceListGetActivePriceListInfoJson",
		    
		    data: {
    			RegionID: 0,
    			DistributorGroupID: 0,
    			DistributorID:$("#DistributorID2").val(),
    			PJPID:0
    		},
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.region_exist != "true"){		    		
		    		$("#WarehouseeSave").removeClass('ui-disabled');
		        	var DistributorAlreadyAdded = false;
		        	$('#AddeddDistributor li input').each(function(){    		
		        		var NewInsertedListID = $("#DistributorID2").val()+",Distributor"; //distributorid,featureid    		
		        		if($(this).val().trim() == NewInsertedListID.trim())
		        		{
		        			DistributorAlreadyAdded = true;    			
		    			}
		        	});
		        	
		        	var FunctionName = "onClick=RemoveList('DistributorIDDynamicallyAdded_"+$("#DistributorID2").val()+"')";
		        	if(!DistributorAlreadyAdded && $("#DistributorID2").val().trim()!="")
		    		{
		        		var AppendedString = "<li data-mini='true' data-icon='delete' id='DistributorIDDynamicallyAdded_"+$("#DistributorID2").val()+"' value='"+$("#DistributorID2").val()+",' "+ FunctionName+"><input type='hidden' id='DistributorIDForHiddenFieldID_"+$("#DistributorID2").val()+"' name='DistributorIDhiddenfield' value='"+$("#DistributorID2").val()+",Distributor'/><a href='#'>"+$("#DistributorID2").val()+" - "+$("#DistributorName2").val()+"</a></li>";
		        		$("#populateDistributorDataUl").append(AppendedString);    	
		            	$("#populateDistributorDataUl").listview("refresh");
		    		}
		    	}else{
					alert($("#DistributorID2").val()+" - "+$("#DistributorName2").val()+' already has active price list "'+json.price_list_name+'"');
		    		//alert(json.error);
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
    }
    
    function RemoveList(ID)
    {    	
    	$("#"+ID).remove();
    	$("#populateDistributorDataUl").listview("refresh");
    	$("#populateRegionDataUl").listview("refresh");
    	$("#populateDataUl").listview("refresh");
    	$("#WarehouseeSave").removeClass('ui-disabled');
    }
    
    function DistributorSearchCallBackForUserRights(SAPCode, DistributorName){   	
    	
		$('#DistributorID2').val(SAPCode);
		$('#DistributorName2').val(DistributorName);
    }
    
    function getDistributorName2(){
    	//alert();
    	if(isInteger($('#DistributorID2').val()) == false ){
    		$('#DistributorID2').val('');
    		return false;
    	}
    	
    	$.ajax({
    		
    		url: "common/GetDistributorInfoJson",
    		data: {
    			DistributorID: $('#DistributorID2').val()
    		},
    		type:"POST",
    		dataType:"json",
    		success:function(json){
    			if(json.exists == "true"){
    				$('#DistributorName2').val(json.DistributorName);
    			}else{
    				$('#DistributorName2').val('');
    			}
    		},
    		error:function(xhr, status){
    			alert("Server could not be reached.");
    		}
    		
    	});
    	
    }
    
function getPJPName(){
    	
    	if( $('#PJPID').val() == "" ){
    		return false;
    	}
    	if( isInteger($('#PJPID').val()) == false ){
    		setTimeout("$('#PJPID').focus();", 100);
    		return false;
    	}
    	
    	$.ajax({
    		url: "common/GetBeatPlanInfoJson",
    		data: {
    			PJPID: $('#PJPID').val()
    		},
    		type:"POST",
    		dataType: "json",
    		success: function(json){ 
    			if( json.success == 'true' ){
    				$('#PJPName').val(json.PJPName);
    			}
    		},
    		error: function(xhr, status){
    			alert("Server could not be reached.");
    		}
    		
    	});
    	
    	
    	
    }


function AddPJP()
{
	
	$.ajax({
	    url: "inventory/PriceListGetActivePriceListInfoJson",
	    
	    data: {
			RegionID: 0,
			DistributorGroupID: 0,
			DistributorID:0,
			PJPID:$("#PJPID").val()
		},
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.region_exist != "true"){		    		
	    		$("#WarehouseeSave").removeClass('ui-disabled');
	        	var PJPAlreadyAdded = false;
	        	$('#AddeddPJP li input').each(function(){    		
	        		var NewInsertedListID = $("#PJPID").val()+",PJP"; //distributorid,featureid    		
	        		if($(this).val().trim() == NewInsertedListID.trim())
	        		{
	        			PJPAlreadyAdded = true;    			
	    			}
	        	});
	        	
	        	var FunctionName = "onClick=RemoveList('PJPIDDynamicallyAdded_"+$("#PJPID").val()+"')";
	        	if(!PJPAlreadyAdded && $("#PJPID").val().trim()!="")
	    		{
	        		var AppendedString = "<li data-mini='true' data-icon='delete' id='PJPIDDynamicallyAdded_"+$("#PJPID").val()+"' value='"+$("#PJPID").val()+",' "+ FunctionName+"><input type='hidden' id='PJPIDhiddenfield_"+$("#PJPID").val()+"' name='PJPIDhiddenfield' value='"+$("#PJPID").val()+",PJP'/><a href='#'>"+$("#PJPID").val()+" - "+$("#PJPName").val()+"</a></li>";
	        		$("#populatePJPDataUl").append(AppendedString);    	
	            	$("#populatePJPDataUl").listview("refresh");
	    		}
	    	}else{
				alert($("#PJPID").val()+" - "+$("#PJPName").val()+' already has active price list "'+json.price_list_name+'"');
	    		//alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}
    
    