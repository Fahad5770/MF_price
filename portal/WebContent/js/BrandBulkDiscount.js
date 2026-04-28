
    $( document ).on( "pageinit", "#BulkDiscount", function() {     	
    	$.get('BulkDiscountDataScope.jsp?PciChannelID='+$("#PciChannelID").val()+'&UserID='+$("#UserID").val(), function(data) {   			
			
    		$("#BulkDiscountDataScope").html(data);
			  $("#BulkDiscountDataScope").trigger('create');
  		});
  		
  		
    	
    
    });
    
    function LoadPerticularBrandBulkDiscount(BulkDiscountID)
    {
    	//alert($("#isEditCase").val());
    	//window.location="SpotDiscount.jsp?Plist="+BulkDiscountid;
    	document.getElementById("BrandBulkDiscountEditID").value=BulkDiscountID;
    	document.getElementById("BrandBulkDiscountEditForm").submit();
    }
    
   

   
    



    
    