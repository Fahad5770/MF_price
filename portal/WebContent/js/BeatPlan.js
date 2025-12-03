
		var RowCount = 0;
		var isChangeEmployeeRequest = false;
		 
		$( document ).on( "pageinit", "#EmployeeBeatPlan", function() {
			
			$('#EmployeeSapCode').on('dblclick', function(e, data){
				isChangeEmployeeRequest = false;
				$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupEmployeeInit();
				} );
				$('#LookupEmployeeSearch').popup("open");
						  
			});
			
			$('#OutletID').on('dblclick', function(e, data){
				
				$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupOutletInit();
				} );
				$('#LookupOutletSearch').popup("open");
						  
			});
			
			$( "#autocomplete_search" ).on( "listviewbeforefilter", function ( e, data ) {
				
		        var $ul = $( this ),
		            $input = $( data.input ),
		            value = $input.val(),
		            html = "";
		        $ul.html( "" );
		        if(value == ""){
		        	$('#EmployeeNameSearch').html('');
					$('#EmployeeIDSearch').val('');
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
			
			
			$( "#autocomplete" ).on( "listviewbeforefilter", function ( e, data ) {
		        var $ul = $( this ),
		            $input = $( data.input ),
		            value = $input.val(),
		            html = "";
		        $ul.html( "" );
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
		                    html += "<li><a href='#' data-ajax='false' onclick=\"changeEmployeeFields("+val.EmployeeCode+", '"+val.FirstName + " " + val.LastName+"')\">" + val.FirstName + " " + val.LastName + " ("+val.EmployeeCode+")</a></li>";
		                });
		                $ul.html( html );
		                $ul.listview( "refresh" );
		                $ul.trigger( "updatelayout");
		                
		                
		            });
		        }
		    });
			
			$( "#autocomplete_outlet_search" ).on( "listviewbeforefilter", function ( e, data ) {
		        var $ul = $( this ),
		            $input = $( data.input ),
		            value = $input.val(),
		            html = "";
		        $ul.html( "" );
		        
		        if(value == ""){
		        	$('#OutletNameSearch').html('');
					$('#OutletIDSearch').val('');
		        }
		        
		        if ( value && value.length > 1 ) {
		            $ul.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
		            $ul.listview( "refresh" );
		            $.ajax({
		                url: "common/GetOutletsBySearchJSON",
		                dataType: "jsonp",
		                crossDomain: true,
		                data: {
		                    q: $input.val()
		                }
		            
		            })
		            .then( function ( response ) {
		            	
		                $.each( response.rows, function ( i, val ) {
		                    html += "<li><a href='#' data-ajax='false' onclick=\"setOutletSearch("+val.OutletID+", '"+val.OutletName+" "+val.OutletType+"')\">" + val.OutletName + " " + val.OutletType + "("+val.OutletID+")<font size=1><br>" + val.region_name_short + ", " + val.DistributorName + "<br>"+ val.address +"</font></a></li>";
		                });
		                $ul.html( html );
		                $ul.listview( "refresh" );
		                $ul.trigger( "updatelayout");
		                
		                
		            });
		        }
		    });
			
			
			$( "#autocomplete2" ).on( "listviewbeforefilter", function ( e, data ) {
		        var $ul = $( this ),
		            $input = $( data.input ),
		            value = $input.val(),
		            html = "";
		        $ul.html( "" );
		        if ( value && value.length > 1 ) {
		            $ul.html( "<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>" );
		            $ul.listview( "refresh" );
		            $.ajax({
		                url: "common/GetOutletsBySearchJSON",
		                dataType: "jsonp",
		                crossDomain: true,
		                data: {
		                    q: $input.val()
		                }
		            
		            })
		            .then( function ( response ) {
		            	
		                $.each( response.rows, function ( i, val ) {
		                    html += "<li><a href='#' data-ajax='false' onclick=\"setOutlet("+val.OutletID+", '"+val.OutletName+" "+val.OutletType+"')\">" + val.OutletName + " " + val.OutletType + "("+val.OutletID+")<font size=1><br>" + val.region_name_short + ", " + val.DistributorName + "<br>"+ val.address +"</font></a></li>";
		                });
		                $ul.html( html );
		                $ul.listview( "refresh" );
		                $ul.trigger( "updatelayout");
		                
		                
		            });
		        }
		    });
			
		
		});
		
		function changeEmployeeFields(EmployeeID, EmployeeName){
			$('#ChangeEmployeeName').val(EmployeeName);
			$('#ChangeEmployeeID').val(EmployeeID);
			$( "#autocomplete" ).html('');
		}
		
		
		function setEmployeeSearch(EmployeeID, EmployeeName){
			$('#EmployeeNameSearch').html(EmployeeName);
			$('#EmployeeIDSearch').val(EmployeeID);
			$( "#autocomplete_search" ).html('');
		}
		
		function setOutlet(OutletID, OutletName){
			$('#OutletNameSpan').html(OutletName);
			$('#OutletName').val(OutletName);
			$('#OutletID').val(OutletID);
			$( "#autocomplete2" ).html('');
		}
		
		function setOutletSearch(OutletID, OutletName){
			$('#OutletNameSearch').html(OutletName);
			$('#OutletIDSearch').val(OutletID);
			$( "#autocomplete_outlet_search" ).html('');
		}
		
		
		
		function BealPlanAddOutlet(){
			
			if($('#OutletID').val() == ''){
				return false;
			}
			
			isAlreadyEntered = false;
			
			var existing = document.getElementsByName("EmployeeBeatPlanMainFormOutletID");
			var x = 0;
    		for (x = 0; x < existing.length; x++){
    			if (existing[x].value == $('#OutletID').val()){
    				isAlreadyEntered = true;
    				break;
    			}
    		}
			
    		if(isAlreadyEntered != true){
    		
				var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
				
				var content = ""+
				"<tr id='BeatPlan"+RowMaxID+"'>"+
					"<td valign='middle'>"+$('#OutletID').val()+" - "+$('#OutletName').val()+"<input type='hidden' name='EmployeeBeatPlanMainFormOutletID' id='EmployeeBeatPlanMainFormOutletID' value='"+$('#OutletID').val()+"' ></td>"+
					"<td nowrap='nowrap'>"+
					
					"<fieldset data-role='controlgroup' data-type='horizontal'>"+
	
		                "<input type='checkbox' name='sunday' id='sunday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='sunday' class='radio_style'>&#10003;</label>"+
		                "<input type='checkbox' name='monday' id='monday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='monday' class='radio_style'>&#10003;</label>"+
		                "<input type='checkbox' name='tuesday' id='tuesday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='tuesday' class='radio_style'>&#10003; </label>"+
		                "<input type='checkbox' name='wednesday' id='wednesday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='wednesday' class='radio_style'>&#10003; </label>"+
		                "<input type='checkbox' name='thursday' id='thursday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='thursday' class='radio_style'>&#10003; </label>"+
		                "<input type='checkbox' name='friday' id='friday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='friday' class='radio_style'>&#10003; </label>"+
		                "<input type='checkbox' name='saturday' id='saturday' value='"+$('#OutletID').val()+"'>"+
		                "<label for='saturday' class='radio_style'>&#10003; </label>"+
		                
					"</fieldset></td>"+
					
					"<td valign='middle'><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"BeatPlanDeleteRow('BeatPlan"+RowMaxID+"');\">Delete</a></td>"+
				"</tr>";
				
				$("#EmployeeBeatPlanTableBody").append(content).trigger('create');
				
				RowCount++;
				
				$('#NoOutletRow').css('display', 'none');
				$('#RowMaxID').val(RowMaxID);
				
				$('#OutletID').val('');
				$('#OutletName').val('');
				
    		}
			
			return false;
		}
		
		function BeatPlanDeleteRow(RowID){
			
			$("#"+RowID).remove();
			RowCount--;
			if( RowCount < 1){
				$('#NoOutletRow').css('display', 'table-row');
				//$('#DeliveryNoteSave').addClass('ui-disabled');
			}
			
		}
		
		function BeatPlanSubmit(){
			$('#EmployeeBeatPlanMainFormAssignTo').val($('#EmployeeSapCode').val());
			
			$.ajax({
			    url: "employee/BeatPlanExecute",
			    
			    data: $("#EmployeeBeatPlanMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.success == "true"){
			    		location = "EmployeeBeatPlan.jsp?EmployeeCode="+$('#EmployeeCode').val();
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
		
		
		function getBeatPlanInfoJson(EditID){
			
			$.ajax({
			    url: "employee/GetBeatPlanInfoJson",
			    data: {
			    	EditID: EditID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.exists == "true"){
			    		$('#EmployeeSapCode').val(json.AssignedTo);
			    		$('#EmployeeBeatPlanMainFormAssignTo').val(json.AssignedTo);
			    		$('#EmployeeName').html(json.CRName);
			    		
			    		for( var i = 0; i < json.rows_distinct.length; i++ ){
			    			
			    			var RowMaxID = parseInt($('#RowMaxID').val()) + 1;
							
							var content = ""+
							"<tr id='BeatPlan"+RowMaxID+"'>"+
								"<td valign='middle'>"+json.rows_distinct[i].OutletID+" - "+json.rows_distinct[i].OutletName+"<input type='hidden' name='EmployeeBeatPlanMainFormOutletID' id='EmployeeBeatPlanMainFormOutletID' value='"+json.rows_distinct[i].OutletID+"' ></td>"+
								"<td nowrap='nowrap'>"+
								
								"<fieldset data-role='controlgroup' data-type='horizontal'>"+
				
					                "<input type='checkbox' name='sunday' id='sunday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'  >"+
					                "<label for='sunday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003;</label>"+
					                "<input type='checkbox' name='monday' id='monday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'>"+
					                "<label for='monday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003;</label>"+
					                "<input type='checkbox' name='tuesday' id='tuesday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'>"+
					                "<label for='tuesday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003; </label>"+
					                "<input type='checkbox' name='wednesday' id='wednesday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'>"+
					                "<label for='wednesday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003; </label>"+
					                "<input type='checkbox' name='thursday' id='thursday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'>"+
					                "<label for='thursday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003; </label>"+
					                "<input type='checkbox' name='friday' id='friday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'>"+
					                "<label for='friday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003; </label>"+
					                "<input type='checkbox' name='saturday' id='saturday"+json.rows_distinct[i].OutletID+"' value='"+json.rows_distinct[i].OutletID+"'>"+
					                "<label for='saturday"+json.rows_distinct[i].OutletID+"' class='radio_style'>&#10003; </label>"+
					                
								
								"</fieldset></td>"+
								
								"<td valign='middle'><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"BeatPlanDeleteRow('BeatPlan"+RowMaxID+"');\">Delete</a></td>"+
							"</tr>";
							
							$("#EmployeeBeatPlanTableBody").append(content).trigger('create');
							
							RowCount++;
							
							$('#NoOutletRow').css('display', 'none');
							$('#RowMaxID').val(RowMaxID);
			    			
			    			
			    		}
			    		 
			    		for( var i = 0; i < json.rows.length; i++ ){
			    			
			    			$('#'+json.rows[i].DayName.toLowerCase()+""+json.rows[i].OutletID).prop("checked", true).checkboxradio("refresh");
			    		
			    		}
			    		
			    		
			    		
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
			
			$("#EmployeeBeatPlanSearchContent").focus();
			
			$.get('BeatPlanSearch.jsp?EmployeeID='+$('#EmployeeIDSearch').val()+"&OutletID="+$('#OutletIDSearch').val()+"&DashboardEmployeeCode="+$('#EmployeeCode').val(), function(data) {
				
				  $("#EmployeeBeatPlanSearchContent").html(data);
				  $("#EmployeeBeatPlanSearchContent").trigger('create');
				  
			});
			return false;

		}
		
		function BeatPlanReset(){
			window.location='EmployeeBeatPlan.jsp?EmployeeCode='+$('#EmployeeCode').val();
		}
		
		function getEmployeeName(){
			if( $('#EmployeeSapCode').val() == "" ){
				return false;
			}
			if( isInteger($('#EmployeeSapCode').val()) == false ){
				setTimeout("$('#EmployeeSapCode').focus();", 100);
				return false;
			}
			
			$.ajax({
				url: "employee/GetEmployeeBySAPCodeJSON",
				data: {
					SAPCode: $('#EmployeeSapCode').val()
				},
				type:"POST",
				dataType: "json",
				success: function(json){ 
					if( json.exists == 'true' ){
						$('#EmployeeName').val(json.EmployeeName);
					}
				},
				error: function(xhr, status){
					alert("Server could not be reached.");
				}
				
			});
			
			
			
		}
		
		function getOutletName(AddOutlet){
			if( $('#OutletID').val() == "" ){
				return false;
			}
			if( isInteger($('#OutletID').val()) == false ){
				setTimeout("$('#OutletID').focus();", 100);
				return false;
			}
			
			$.ajax({ 
				url: "common/GetOutletBySAPCodeJSON",
				data: {
					SAPCode: $('#OutletID').val()
				},
				type:"POST",
				dataType: "json",
				success: function(json){ 
					if( json.exists == 'true' ){
						$('#OutletName').val(json.OutletName);
						if(AddOutlet){
							BealPlanAddOutlet();
						}
						
					}
				},
				error: function(xhr, status){
					alert("Server could not be reached.");
				}
				
			});
			return false;
		}
		
		function changeEmployeeForDashboard(){
			isChangeEmployeeRequest = true;
			$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupEmployeeInit();
			} );
			$('#LookupEmployeeSearch').popup("open");
		}
		
		function EmployeeSearchCallBack(SAPCode, EmployeeName){
			if(isChangeEmployeeRequest){
				window.location = 'EmployeeBeatPlan.jsp?EmployeeCode='+SAPCode;
			}
			$('#EmployeeSapCode').val(SAPCode);
			$('#EmployeeName').val(EmployeeName);
		}
		
		function OutletSearchCallBack(SAPCode, OutletName){
			$('#OutletID').val(SAPCode);
			$('#OutletName').val(OutletName);
			BealPlanAddOutlet();
		}
		
		function enableEmployeeSelection(){
			$('#EmployeeSapCode').removeClass('ui-disabled');
			$('#EmployeeName').removeClass('ui-disabled');
		}