
		var RowCount = 0;
		var isChangeEmployeeRequest = false;
		 
		var UserType = '';
		
		$( document ).on( "pageinit", "#BeatPlanCreate", function() {
			
			$('#OutletID').on('dblclick', function(e, data){
				
				$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
					lookupOutletInit();
				} );
				$('#LookupOutletSearch').popup("open");
						  
			});
			
		$('#BeatPlanCreateDistributorID').on('dblclick', function(e, data){
			$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupDistributorInit();
			} );
				$('#LookupDistributorSearch').popup("open");
			});	
		
		$('#BeatPlanCreateEmployeeID').on('dblclick', function(e, data){
			UserType = 'Employee';
			$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupEmployeeInit();
			} );
				$('#LookupEmployeeSearch').popup("open");
			});	
		
		
		$('#BeatPlanCreateSMID').on('dblclick', function(e, data){
			
			UserType = 'SM';
			
			$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupEmployeeInit();
			} );
				$('#LookupEmployeeSearch').popup("open");
			});	
		
		$('#BeatPlanCreateTDMID').on('dblclick', function(e, data){
			
			UserType = 'TDM';
			
			$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupEmployeeInit();
			} );
				$('#LookupEmployeeSearch').popup("open");
			});	
		
		$('#BeatPlanCreateASMID').on('dblclick', function(e, data){
			
			UserType = 'ASM';
			
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
		

		function VerifyIsalternative(Tag)
		{  var outletid=Tag.value;
		   var outletdays=0;
		
			
			  if (document.getElementById("sunday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  if (document.getElementById("monday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  if (document.getElementById("tuesday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  
			  if (document.getElementById("wednesday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  if (document.getElementById("thursday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  if (document.getElementById("friday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  if (document.getElementById("saturday"+outletid).checked) 
			  {
				  outletdays++;
			  }
			  
			  if(outletdays==1)
			  {$("#Isalternative"+outletid).removeAttr("disabled");}
			  else
			  {  
				  
			      if($("#Isalternative"+outletid).is(":checked"))
			      {
	               // alert("In Case of Multiple Days or No Day Selected, Alternative is not Allowed");
	                $("#Isalternative"+outletid).prop("checked", false);
	              }
				  $("#Isalternative"+outletid).attr("disabled", true);
			  }
		  
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
					"<td valign='middle' style='color:green'>"+"Active</td>"+
					"<input type=hidden name='hibernation' value='"+$('#OutletID').val()+"-1"+"' >"+
					"<td nowrap='nowrap'>"+
					
					"<fieldset data-role='controlgroup' data-type='horizontal'>"+
	
		                "<input onclick='VerifyIsalternative(this);' type='checkbox'  name='sunday' id='sunday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label for='sunday"+$('#OutletID').val()+"' class='radio_style'>&#10003;</label>"+
		                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='monday' id='monday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label for='monday"+$('#OutletID').val()+"' class='radio_style'>&#10003;</label>"+
		                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='tuesday' id='tuesday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label for='tuesday"+$('#OutletID').val()+"' class='radio_style'>&#10003; </label>"+
		                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='wednesday' id='wednesday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label for='wednesday"+$('#OutletID').val()+"' class='radio_style'>&#10003; </label>"+
		                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='thursday' id='thursday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label for='thursday"+$('#OutletID').val()+"' class='radio_style'>&#10003; </label>"+
		                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='friday' id='friday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label  for='friday"+$('#OutletID').val()+"' class='radio_style'>&#10003; </label>"+
		                "<input onclick='VerifyIsalternative(this);' type='checkbox' name='saturday' id='saturday"+$('#OutletID').val()+"' value='"+$('#OutletID').val()+"'>"+
		                "<label for='saturday"+$('#OutletID').val()+"' class='radio_style'>&#10003; </label>"+
		                
					"</fieldset></td>"+
					"<td><input type='checkbox'  style='position: absolute !important;top:-18px !important;height:80px !important;width:20px !important;' name='Isalternative' value='"+$('#OutletID').val()+"' id='Isalternative"+$('#OutletID').val()+"'></td>"+
					
					"<td valign='middle'><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"BeatPlanDeleteRow('BeatPlan"+RowMaxID+"');\">Delete</a></td>"+
				"</tr>";
				
				$("#BeatPlanCreateTableBody").append(content).trigger('create');
				
				 RowCount++;
				 $("#Isalternative"+$('#OutletID').val()).attr("disabled", true);
				
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
		
		function BeatPlanCreateSubmit(){
			
			if( $('#BeatPlanCreateOutletGroupName').val() == "" ){
				alert("Please enter PJP");
				document.getElementById("BeatPlanCreateOutletGroupName").focus();
				return false;
			}
			
			if( $('#BeatPlanCreateDistributorID').val() == "" ){
				alert("Please select Distributor");
				enableDistributorSelection();
				document.getElementById("BeatPlanCreateDistributorID").focus();
				return false;
			}
			
			$('#BeatPlanCreateMainFormOutletGroupName').val( $('#BeatPlanCreateOutletGroupName').val() );
			$('#DistributorID').val( $('#BeatPlanCreateDistributorID').val() );
			
			if( $('#BeatPlanCreateEmployeeID').val() != "" ){
				$('#OrderBookerID').val( $('#BeatPlanCreateEmployeeID').val() );
			}
			
			if( $('#BeatPlanCreateSMID').val() != "" ){
				$('#SMID').val( $('#BeatPlanCreateSMID').val() );
			}
			
			if( $('#BeatPlanCreateTDMID').val() != "" ){
				$('#TDMID').val( $('#BeatPlanCreateTDMID').val() );
			}
			
			if( $('#BeatPlanCreateASMID').val() != "" ){
				$('#ASMID').val( $('#BeatPlanCreateASMID').val() );
			}
			
			$.mobile.showPageLoadingMsg();
			$.ajax({
			    url: "outlet/BeatPlanCreateExecute",
			    data: $("#BeatPlanCreateMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	$.mobile.hidePageLoadingMsg();
			    	if (json.success == "true"){
			    		location = "BeatPlanListDistributor.jsp";
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
		
		
		function getBeatPlanInfoJson(EditID){
						
			$.ajax({
			    url: "outlet/GetOutletBeatPlanInfoJson",
			    data: {
			    	EditID: EditID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.exists == "true"){
			    		
			    		$('#BeatPlanCreateOutletGroupName').val(json.PJPName);
			    		$('#BeatPlanCreateDistributorID').val(json.DistributorID);
			    		$('#BeatPlanCreateDistributorName').val(json.DistributorName);
			    		
			    		$('#BeatPlanCreateEmployeeID').val(json.OrderBookerID);
			    		$('#BeatPlanCreateEmployeeName').val(json.OrderBookerName);
			    		
			    		$('#BeatPlanCreateSMID').val(json.SMID);
			    		$('#BeatPlanCreateSMName').val(json.SMName);
			    		
			    		$('#BeatPlanCreateTDMID').val(json.TDMID);
			    		$('#BeatPlanCreateTDMName').val(json.TDMName);
			    		
			    		$('#BeatPlanCreateASMID').val(json.ASMID);
			    		$('#BeatPlanCreateASMName').val(json.ASMName);
			    		
			    		RowCount = json.content_rows;
			    		
			    		if(json.content_rows > 0){
			    			setTimeout("$('#NoOutletRow').css('display', 'none')", 500);
			    		}
			    		
			    		$("#BeatPlanCreateTableBody").append(json.content).trigger('create');
			    		
						$('#RowMaxID').val(json.content_rows);
			    		
						var OutletIDublicate = [];
			    		for( var i = 0; i < json.rows.length; i++ )
			    		{		    			
			    		
			    			if(OutletIDublicate.includes('#Isalternative'+json.rows[i].OutletID))
			    			{
			    				$('#Isalternative'+json.rows[i].OutletID).attr("disabled", true);
			    			}
			    		
			    			OutletIDublicate.push('#Isalternative'+json.rows[i].OutletID)
			    			
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
		
		function getEmployeeName(ElementID, ElementName, ElementValue){
			if( ElementValue == "" ){
				return false;
			}
			if( isInteger(ElementValue) == false ){
				setTimeout("$('#"+ElementID+"').focus();", 100);
				return false;
			}
			
			$.ajax({
				url: "employee/GetEmployeeBySAPCodeJSON",
				data: {
					SAPCode: ElementValue
				},
				type:"POST",
				dataType: "json",
				success: function(json){ 
					if( json.exists == 'true' ){
						$('#'+ElementName).val(json.EmployeeName);
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
		
		function getDistributorName(){
			if( $('#BeatPlanCreateDistributorID').val() == "" ){
				return false;
			}
			if( isInteger($('#BeatPlanCreateDistributorID').val()) == false ){
				setTimeout("$('#BeatPlanCreateDistributorID').focus();", 100);
				return false;
			}
			
			$.ajax({ 
				url: "common/GetDistributorInfoJson",
				data: {
					DistributorID: $('#BeatPlanCreateDistributorID').val()
				},
				type:"POST",
				dataType: "json",
				success: function(json){ 
					if( json.exists == 'true' ){
						$('#BeatPlanCreateDistributorName').val(json.DistributorName);
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
		
		function setDistributorLookupAtBeatPlanCreate(SAPCode, DistributorName){
			$('#BeatPlanCreateDistributorID').val(SAPCode);
			$('#BeatPlanCreateDistributorName').val(DistributorName);
			$('#DistributorID').val( SAPCode );
		}
		
		function setEmployeeLookupAtBeatPlanCreate(SAPCode, DistributorName){
			$('#BeatPlanCreate'+UserType+'ID').val(SAPCode);
			$('#BeatPlanCreate'+UserType+'Name').val(DistributorName);
			$('#OrderBookerID').val( SAPCode );
		}
		
		function enableEmployeeSelection(){
			$('#BeatPlanCreateEmployeeID').removeClass('ui-disabled');
			$('#BeatPlanCreateEmployeeName').removeClass('ui-disabled');
			document.getElementById("BeatPlanCreateEmployeeID").focus();
		}
		
		function enableSMSelection(){
			$('#BeatPlanCreateSMID').removeClass('ui-disabled');
			$('#BeatPlanCreateSMName').removeClass('ui-disabled');
			document.getElementById("BeatPlanCreateSMID").focus();
		}
		
		function enableTDMSelection(){
			$('#BeatPlanCreateTDMID').removeClass('ui-disabled');
			$('#BeatPlanCreateTDMName').removeClass('ui-disabled');
			document.getElementById("BeatPlanCreateTDMID").focus();
		}
		
		function enableASMSelection(){
			$('#BeatPlanCreateASMID').removeClass('ui-disabled');
			$('#BeatPlanCreateASMName').removeClass('ui-disabled');
			document.getElementById("BeatPlanCreateASMID").focus();
		}
		
		function enableDistributorSelection(){
			$('#BeatPlanCreateDistributorID').removeClass('ui-disabled');
			$('#BeatPlanCreateDistributorName').removeClass('ui-disabled');
			document.getElementById("BeatPlanCreateDistributorID").focus();
		}
		
		
		