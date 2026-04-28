
    $( document ).on( "pageinit", "#DistributorEmployeesMain", function() {
    });
    
    function DistributorEmployeeSubmit()
    {
    	var IsValid = false;
    	if($('#DistributorEmpName').val() == "" ){
    		document.getElementById('DistributorEmpName').focus();  
			IsValid = true;
		}
    	if($('#DistributorDeptName').val() == "" ){
    		document.getElementById('DistributorDeptName').focus();  
			IsValid = true;
		}
    	if($('#DistributorEmpDesignation').val() == "" ){
    		document.getElementById('DistributorEmpDesignation').focus();  
			IsValid = true;
		}
    	if($('#DistributorEmpNIC').val() == "" ){
    		document.getElementById('DistributorEmpNIC').focus();  
			IsValid = true;
		}
    	if(isInteger($('#DistributorEmpNIC').val()) == false ){
    		document.getElementById('DistributorEmpNIC').focus();  
			IsValid = true;
		}
    	if(!IsValid)
		{
    		$.ajax({    		
    			url: "distributor/DistributorEmployeeExecute",    			
    			    data: $("#DistributorEmpForm" ).serialize(),
    			    type: "POST",
    			    dataType : "json",
    			success:function(json){
    				if(json.success == "true"){    				
    					window.location="DistributorEmployees.jsp";    					
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
    
    function LoadPerticularDistributorEmp(ID)
    {
    	//alert(ID);
    	$("#DistEmpID").val(ID);
    	//window.location="DistributorEmployees.jsp"; 
    	document.getElementById("DistEmpEditForm").submit();
    	
    	
    }
    
    function isInteger (o) {
    	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
    }