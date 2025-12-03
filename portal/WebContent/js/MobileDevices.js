
		var RowCount = 0;
		var isChangeEmployeeRequest = false;
		 
		$( document ).on( "pageinit", "#MobileDevices", function() {
		
			$('#MobileDevicesAssignToID').on('dblclick', function(e, data){
				
				$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupEmployeeInit();
				} );
				
				$('#LookupEmployeeSearch').popup("open");
				
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
				
				$("#BeatPlanCreateTableBody").append(content).trigger('create');
				
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
		
		function MobileDevicesSubmit(){
			
			if( $('#MobileDevicesDeviceID').val() == "" ){
				alert("Please enter Device ID");
				document.getElementById("MobileDevicesDeviceID").focus();
				return false;
			}
			
			if( $('#MobileDevicesAssignToID').val() == "" ){
				alert("Please select User");
				document.getElementById("MobileDevicesAssignToID").focus();
				return false;
			}
			
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
			    url: "mobile/MobileDevicesExecute",
			    data: $("#MobileDevicesMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	$.mobile.hidePageLoadingMsg();
			    	if (json.success == "true"){
			    		window.location="MobileDevices.jsp";
			    	}else{
						alert(json.error);
			    		//alert("Server could not be reached.");
			    	}
			    },
			    error: function( xhr, status ) {
			    	$.mobile.hidePageLoadingMsg();
			    	alert("Server could not be reached.");
			    }
			});
			
		}
		
		
		function MobileDevicesSetActive(ActiveValue){
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
			    url: "mobile/MobileDevicesExecute",
			    data: {
			    	RecordID: EditID,
			    	SetActive: ActiveValue
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	$.mobile.hidePageLoadingMsg();
			    	if (json.success == "true"){
			    		
			    		if(ActiveValue == '1'){
			    			$('#ActivateButtonSpan').css("display", "none");
			    			$('#DeActivateButtonSpan').css("display", "block");
			    		}else{
			    			$('#DeActivateButtonSpan').css("display", "none");
			    			$('#ActivateButtonSpan').css("display", "block");
			    			
			    		}
			    		
			    		//window.location="MobileDevices.jsp";
			    	}else{
						alert(json.error);
			    		//alert("Server could not be reached.");
			    	}
			    },
			    error: function( xhr, status ) {
			    	$.mobile.hidePageLoadingMsg();
			    	alert("Server could not be reached.");
			    }
			});
			
		}
		
		
		
		
		function getMobileDeviceInfoJson(EditID){
			
			$.ajax({
			    url: "mobile/MobileDevicesEditInfoJson",
			    data: {
			    	EditID: EditID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.success == "true"){
			    		//window.location="MobileDevices.jsp";
			    		$('#MobileDevicesDeviceID').val(json.DeviceID);
			    		$('#MobileDevicesAssignToID').val(json.IssuedTo);
			    		
			    		$('#MobileDevicesMobileNo').val(json.MobileNo);
			    		
			    		getEmployeeName();
			    		if(json.IsActive == '1'){
			    			$('#DeActivateButtonSpan').css("display", "block");
			    		}else{
			    			$('#ActivateButtonSpan').css("display", "block");
			    		}
			    	}else{
			    		alert(json.error);
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
			if( $('#MobileDevicesAssignToID').val() == "" ){
				return false;
			}
			if( isInteger($('#MobileDevicesAssignToID').val()) == false ){
				setTimeout("$('#MobileDevicesAssignToID').focus();", 100);
				return false;
			}
			
			$.ajax({
				url: "employee/GetEmployeeBySAPCodeJSON",
				data: {
					SAPCode: $('#MobileDevicesAssignToID').val()
				},
				type:"POST",
				dataType: "json",
				success: function(json){ 
					if( json.exists == 'true' ){
						$('#MobileDevicesAssignTo').val(json.EmployeeName);
					}
				},
				error: function(xhr, status){
					alert("Server could not be reached.");
				}
				
			});
			
			
			
		}
		
		function setEmployeeLookupAtMobileDevices(SAPCode, EmployeeName){
			$('#MobileDevicesAssignToID').val(SAPCode);
			$('#MobileDevicesAssignTo').val(EmployeeName);
		}
		
		