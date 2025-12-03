
		var RowCount = 0;
		
		$( document ).on( "pageinit", "#ProductSpecification", function() {
			
			$( "#autocomplete_employee_search" ).on( "listviewbeforefilter", function ( e, data ) {
		        var $ul = $( this ),
		            $input = $( data.input ),
		            value = $input.val(),
		            html = "";
		        $ul.html( "" );
		        
		        if(value == ""){
		        	$('#EmployeeNameSpan_search').html('');
					$('#EmployeeID_search').val('');
		        }
		        
		        if ( value && value.length > 1 ) {
		            $ul.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
		            $ul.listview( "refresh" );
		            $.ajax({
		                url: "employee/GetEmployeeBySearchJSON",
		                dataType: "jsonp",
		                crossDomain: true,
		                data: {
		                    q: $input.val()
		                }
		            
		            })
		            .then( function ( response ) {
		            	
		                $.each( response.rows, function ( i, val ) {
		                    html += "<li><a href='#' data-ajax='false' onclick=\"setEmployeeSearch("+val.EmployeeCode+", '"+val.FirstName + " " + val.LastName+"')\">" + val.FirstName + " " + val.LastName + " ("+val.EmployeeCode+")</a></li>";
		                });
		                $ul.html( html );
		                $ul.listview( "refresh" );
		                $ul.trigger( "updatelayout");
		                
		                
		            });
		        }
		    });

		});
		
		function setEmployee(EmployeeID, EmployeeName){
			$('#EmployeeNameSpan2').html(EmployeeName);
			$('#EmployeeID2').val(EmployeeID);
			$( "#autocomplete_employee" ).html('');
		}
		
		function setEmployeeSearch(EmployeeID, EmployeeName){
			$('#EmployeeNameSpan_search').html(EmployeeName);
			$('#EmployeeID_search').val(EmployeeID);
			$( "#autocomplete_employee_search" ).html('');
		}
		
		function DisplayProductList(val){
			$.get('EmployeeProductGroupList.jsp?EmployeeProductGroupID='+val, function(data) {
				
				  $("#ProductList").html(data);
				  $("#ProductList").trigger('create');
				  
			});
		}
		
		
		function ProductSpecificationSubmit(){
			 
			$.ajax({
			    url: "employee/ProductSpecificationExecute",
			    
			    data: $("#ProductSpecificationForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.success == "true"){
			    		location = "EmployeeProductSpecification.jsp?EmployeeCode="+$('#EmployeeCode').val();
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
		
		
		function getEmployeeProductSpecificationInfoJson(EditID){
			
			$.ajax({
			    url: "employee/GetEmployeeProductSpecificationInfoJson",
			    data: {
			    	EditID: EditID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.exists == "true"){
			    		$('#EmployeeNameSpan2').html(json.EmployeeName);
			    		$('#EmployeeID2').val(json.EmployeeID);
			    		$('#EmployeeProductGroup').val(json.GroupID).change();
			    	}else{
			    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
			    	}
			    	
			    },
			    error: function( xhr, status ) {
			    	alert("Server could not be reached.");
			    }
			});
		}
		
		
		function showSearchContent(){
			
			$.get('EmployeeProductSepcificationSearch.jsp?EmployeeID='+$("#EmployeeID_search").val()+'&DashboardEmployeeCode='+$('#EmployeeCode').val(), function(data) {
				
				  $("#SearchContent").html(data);
				  $("#SearchContent").trigger('create');
				  
			});
			return false;

		}
		
		function changeEmployee(){
			
		}
		
		function ProductSpecificationReset(){
			window.location='EmployeeProductSpecification.jsp?EmployeeCode='+$('#EmployeeCode').val();
		}
		
		function changeEmployeeForDashboard(){
			
			$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupEmployeeInit();
			} );
			$('#LookupEmployeeSearch').popup("open");
		}
		
		function EmployeeSearchCallBack(SAPCode, EmployeeName){
			window.location = 'EmployeeDashboard.jsp?EmployeeCode='+SAPCode;
		}
		