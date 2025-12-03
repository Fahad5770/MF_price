
    $( document ).on( "pageinit", "#BulkDiscount", function() {     	
    	$.get('BulkDiscountDataScope.jsp?PciChannelID='+$("#PciChannelID").val()+'&UserID='+$("#UserID").val(), function(data) {   			
			
    		$("#BulkDiscountDataScope").html(data);
			  $("#BulkDiscountDataScope").trigger('create');
  		});
    	
    
    });
    
    
    function SaveBulkDiscount()
    {
    	
    	var isEmpty = false;
    	if($("#BulkDiscountLabel").val()=="")
    	{
    		isEmpty = true;
    		document.getElementById('BulkDiscountLabel').focus();
    	}
    	if($("#BulkDiscountValidFrom").val()=="")
    	{
    		isEmpty = true;
    		document.getElementById('BulkDiscountValidFrom').focus();
    	}
    	if($("#BulkDiscountValidTo").val()=="")
    	{
    		isEmpty = true;
    		document.getElementById('BulkDiscountValidTo').focus();
    	}
    	
    	if(!isEmpty)
    	{ 			
			$.ajax({    		
	    		url: "inventory/BulkDiscountExecute",    			
	    		    data: $("#BulkDiscountForm" ).serialize(),
	    		    type: "POST",
	    		    dataType : "json",
	    		success:function(json){
	    			if(json.success == "true"){    	
	    				//window.location="SpotDiscount.jsp";
	    				BulkDiscountAdditionalRightExecute(json.spot_discount_id); 				
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
    
    function BulkDiscountAdditionalRightExecute(BulkDiscount)
    {
    	$("#BulkDiscountIDForWhole").val(BulkDiscount);//setting price list
    	$.ajax({
		    url: "inventory/BulkDiscountAdditionalRightExecute",
		    
		    data: $("#BulkDiscountForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){		    		
		    		window.location="BulkDiscount.jsp";
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
    	UnitPerSku = $("#Discountper_<%=i"+id).val();
    	bottles = obj.value/UnitPerSku;
    	$("#Discountper"+id).val(parseFloat(bottles).toFixed(2));    	
    	//alert(parseFloat(bottles).toFixed(2));
    }
    
    function LoadPerticularBulkDiscount2(BulkDiscountID)
    {
    	//alert($("#isEditCase").val());
    	//window.location="SpotDiscount.jsp?Plist="+BulkDiscountid;
    	document.getElementById("BulkDiscountEditID").value=BulkDiscountID;
    	document.getElementById("BulkDiscountEditForm").submit();
    }
    
   function AddPCI()
    {    	
    	$.ajax({
		    url: "inventory/BulkDiscountGetActiveSpotDiscountInfoJson",
		    
		    data: {
    			label: $("#PCISelect option:selected").val(),
    		
    		},
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.region_exist != "true"){		    		
		    		//adding region
		    		var RegionAlreadyAdded = false;    	
		        	$('#Addeddlabel li input').each(function(){ 
		        		var NewInsertedListID = $("#PCISelect option:selected").val()+",label";	        		
		        		if($(this).val().trim() == NewInsertedListID.trim())
		        		{
		        			RegionAlreadyAdded = true;		        			
		    			}
		        	});

		        	var FunctionName = "onClick=RemoveList('PCIDynamicallyAdded_"+$("#PCISelect option:selected").val()+"')";
		        	
		        	if(!RegionAlreadyAdded && $("#PCISelect option:selected").val() != "-1")
		    		{
		        		var AppendedString = "<li data-mini='true' data-icon='delete' id='PCIDynamicallyAdded_"+$("#PCISelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='RegionDynamicallyAddedHidden_"+$("#PCISelect option:selected").val()+"' name='PCIIDhiddenfield' value='"+$("#PCISelect option:selected").val()+",label'/><a href='#'>"+$("#PCISelect option:selected").text()+"</a></li>";
		        		
		        		$("#populatePCIDataUl").append(AppendedString);    	
		            	$("#populatePCIDataUl").listview("refresh");
		    		}
		    	}else{
					alert($("#PCISelect option:selected").text()+' already has active price list "'+json.spot_discount_name+'"');
		    		//alert(json.error);
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
    }

    function addField(n)
{
    var td = n.parentNode.parentNode.cloneNode(true);
    document.getElementById('tbl').appendChild(td);
}
       function RemoveList(ID)
    {    	
    	$("#"+ID).remove();
    	$("#populateDistributorDataUl").listview("refresh");
    	$("#populatePCIDataUl").listview("refresh");
    	$("#populateDataUl").listview("refresh");
    	$("#WarehouseeSave").removeClass('ui-disabled');
    }
    	
    
   

   
    



    
    