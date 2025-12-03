
		var RowCount = 0;
		 
		$( document ).on( "pageinit", "#EmployeeProductGroup", function() {
			
			$('#selectAll').change(function(){
				$('select[name="IsSelected"]').val($('#selectAll').val()).slider('refresh');
			});
			
			$.get('EmployeeProductGroupSearch.jsp', function(data) {
				
				  $("#SearchContent").html(data);
				  $("#SearchContent").trigger('create');
				  
			});
			
			if (GlobalEditID != 0){
				getEmployeeProductGroupInfoJson(GlobalEditID);
			}
		}); 
		
		function EmployeeProductGroupSubmit(){
			
			if($('#EmployeeProductGroupName').val() == ''){
				document.getElementById("EmployeeProductGroupName").focus();
				return false;
			}
			
			var flag = 0;
			
			var IsSelected = document.getElementsByName("IsSelected");
			for(x = 0; x < IsSelected.length; x++){
				if(IsSelected[x].value == 'on'){
					flag++;
				}
			}
			
			if(flag == 0){
				alert("Please select atleast one product.");
				return false;
			}
			
			$('#EmployeeProductGroupFormName').val($('#EmployeeProductGroupName').val());
			
			$.ajax({
			    url: "employee/EmployeeProductGroupExecute",
			    
			    data: $("#EmployeeProductGroupForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.success == "true"){
			    		location = "EmployeeProductGroups.jsp";
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
		
		function getEmployeeProductGroupInfoJson(EditID){
			
			$.ajax({
			    url: "employee/GetEmployeeProductGroupInfoJson",
			    data: {
			    	EditID: EditID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.exists == "true"){
			    		
			    		$('#EmployeeProductGroupName').val(json.GroupName);
			    		
			    		for(var i = 0; i < json.rows.length; i++){
			    			$('#IsSelected'+json.rows[i].ProductID).val('on').slider('refresh');
			    		}
			    		
			    		//location = "EmployeeDashboard.jsp?EmployeeCode="+$('#EmployeeCode').val();
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
		
		function EmployeeProductGroupReset(){
			window.location = 'EmployeeProductGroups.jsp';
		}
		
		
		
		