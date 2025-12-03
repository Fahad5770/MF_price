
    $( document ).on( "pageinit", "#DistributorVehiclesMain", function() {    
    	$("#DriverIDTD").children().css("width","100%");
    });
    
    function DistributorVehicleSubmit()
    {
    	var IsValid = true;
    	var patt=/[^0-9a-zA-Z]/
    		if($("#VehicleNum").val()=="")
        	{//invalid
            	IsValid = false;
            	document.getElementById('VehicleNum').focus();
            	//alert("Not valid");
        	}
    		if($("#VehicleNum").val().match(patt))
        	{//invalid
            	IsValid = false;
            	document.getElementById('VehicleNum').focus();
            	//alert("Not valid");
        	}    	
	    	
	    	if($("#VehiclesTypeIDSelect").val() == "")
	    	{
	        	IsValid = false;
	        	document.getElementById('VehiclesTypeIDSelect').focus();        	
	    	}
	    	if($("#DistributorDriverID").val() == "")
	    	{
	        	IsValid = false;
	        	document.getElementById('DistributorDriverID').focus();        	
	    	}
	    	

	        	if(IsValid)
	    		{
	        		$.ajax({    		
	        			url: "distributor/DistributorVehicleExecute",    			
	        			    data: $("#DistributorVehicleForm" ).serialize(),
	        			    type: "POST",
	        			    dataType : "json",
	        			success:function(json){
	        				if(json.success == "true"){    				
	        					window.location="DistributorVehicles.jsp";    					
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
	
	
    
    function LoadPerticularDistributorVehicle(ID)
    {
    	//alert(ID);
    	$("#DistVehID").val(ID);
    	//window.location="DistributorEmployees.jsp"; 
    	document.getElementById("DistVehEditForm").submit();
    	
    	
    }
    
    function GetDistributorEmployeeName()
    {
    	
    		//alert();
    		if(isInteger($('#DistributorDriverID').val()) == false ){
    			$('#DistributorDriverID').val('');
    			return false;
    		}    		
    		$.ajax({
    			
    			url: "distributor/GetDistributorEmployeeInfoJson",
    			data: {
    				DistributorDriverID: $('#DistributorDriverID').val()
    			},
    			type:"POST",
    			dataType:"json",
    			success:function(json){
    				if(json.exists == "true"){
    					$('#DistributorEmpName').val(json.DistributorDriverName);
    				}else{
    					$('#DistributorEmpName').val('');
    				}
    			},
    			error:function(xhr, status){
    				alert("Server could not be reached.");
    			}
    			
    		});
    		
    	
    }
    
    function isInteger (o) {
  	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
  }