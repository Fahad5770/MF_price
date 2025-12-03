
    $( document ).on( "pageinit", "#AddDistributor", function() {    	
    	
		$.get('DistributorGroupsFiltered.jsp?GroupID='+GlobalGroupID, function(data) {   			
		  $("#FilteredGroupedDistributors").html(data);
		  $("#FilteredGroupedDistributors").trigger('create');
		  $("#FilteredGroupedDistributors").trigger('refresh');

	});
    });
    
    function AddDistributor(DistributorID)
    {    	
    	
    	if($("#isSelected_"+DistributorID).val()=="0")
    	{
    		$("#DistributorLi_"+DistributorID).buttonMarkup({theme: 'b'});
    		$("#isSelected_"+DistributorID).val("1");
    		$("#AddDistributorForm").append("<input type='hidden' name='DistributorIdsName' value='"+DistributorID+"'/>");
    		var SelectedDistributorCounter = $("#SelectedDistributorCounter").val(); 
    		var SelectedDistributorCounter1 = parseInt(SelectedDistributorCounter)+1;  //increment the counter
    		$("#SelectedDistributorCounter").val(SelectedDistributorCounter1);
    	}
    	else
    	{
    		$("#DistributorLi_"+DistributorID).buttonMarkup({theme: 'c'});
    		$("#isSelected_"+DistributorID).val("0");    		
    		$('#AddDistributorForm input').each(function() {
    			if($(this).val()==DistributorID)
				{
    				(this).remove();
    				var SelectedDistributorCounter = $("#SelectedDistributorCounter").val(); 
    	    		var SelectedDistributorCounter1 = parseInt(SelectedDistributorCounter)-1;  //increment the counter
    	    		$("#SelectedDistributorCounter").val(SelectedDistributorCounter1);
    			
				}
    			   
    			});
    	}
    	
    }
    
    function AddGroups(GroupID)
    {
    	if($("#SelectedDistributorCounter").val()=="0") //mean no selected distributor - so run filter case
		{
    		$("#isFilteredBtnClicked").val("1");//setting to 1 because filtered case
    		$("#TempFilteredGrpID").val(GroupID);
    		GlobalGroupID = GroupID;
    		$.get('DistributorGroupsFiltered.jsp?GroupID='+GroupID, function(data) {   			
    			  $("#FilteredGroupedDistributors").html(data);
    			  $("#FilteredGroupedDistributors").trigger('create');
      		});
		}
    	else
		{
    		
    		$("#isDeleteCase").val("0"); //setting flag for Insertion case
    		
        	$("#AddDistributorForm").append("<input type='hidden' name='GroupIDName' value='"+GroupID+"'/>"); //adding group id 
        	$.ajax({
        		
        		url: "employee/DistributorGroupsExecute",
        			
        		    data: $("#AddDistributorForm" ).serialize(),
        		    type: "POST",
        		    dataType : "json",
        		success:function(json){
        			if(json.success == "true"){  
        				//alert($("#TempFilteredGrpID").val());
        				//GlobalGroupID = $("#TempFilteredGrpID").val();
        				window.location='DistributorGroups.jsp?GroupID='+GlobalGroupID;
        				//$("#TempFilteredGrpID").val(GroupID); //changing the group id
        				
        			}else{
        				
        			}
        		},
        		error:function(xhr, status){
        			alert("Server could not be reached.");
        		}
        		
        	});
		}
    	
    	
    }
    
    function DeleteGroup(DistributorId,GroupId)
    {
    	$("#isDeleteCase").val("1"); //setting flag for delete case\
    	
    	
    	$.ajax({
    		
    		url: "employee/DistributorGroupsExecute",
    		data: {
    			DistID: DistributorId,
    			GrpID: GroupId,
    			isDeleteCase:$("#isDeleteCase").val()
    			
    		},
    		type:"POST",
    		dataType:"json",
    		success:function(json){
    			if(json.deletesuccess == "true"){
    				window.location='DistributorGroups.jsp?GroupID='+GlobalGroupID;
    			}
    		},
    		error:function(xhr, status){
    			alert("Server could not be reached.");
    		}
    		
    	});
    }
    
    function AddDistributorNew()
    {    	
    	$("#isDeleteCase").val("2"); //setting flag for insertion case
    	var GroupNameEmptyFlag = false;
    	if( $('#AddDistributorNameTxtBx').val() == "" ){
    		document.getElementById('AddDistributorNameTxtBx').focus();
    		GroupNameEmptyFlag = true;
    	}
    	if(!GroupNameEmptyFlag)
    	{
    		$.ajax({
        		
        		url: "employee/DistributorGroupsExecute",
        		data: {
        			AddDistributorNameTxtBx: $("#AddDistributorNameTxtBx").val(),    			
        			isDeleteCase:$("#isDeleteCase").val()
        			
        		},
        		type:"POST",
        		dataType:"json",
        		success:function(json){
        			if(json.insertionsuccess == "true"){   				
        				window.location='DistributorGroups.jsp?GroupID='+GlobalGroupID;    				
        				
        			}else{
        				
        			}
        		},
        		error:function(xhr, status){
        			alert("Server could not be reached.");
        		}
        		
        	});
    	}
    	
    }
    
    
    
    