
    $( document ).on( "pageinit", "#UserRight", function() {
    	
    	
    	//$("#SaleTypeID").find('2').attr("selected", "selected");    	
    	

    	$('#DistributorID').on('dblclick', function(e, data){
    		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
    			lookupDistributorInit();
    		} );
    		$('#LookupDistributorSearch').popup("open");
    		$("#isSecondDistCall").val("0");
    	});
    	
    	
    	
    	
    	$("#DistributroIDTD").hide();
    	$("#DistributroNameTD").hide();    	
    	$("#SaleTypeID").change(function() {
    		if($('#SaleTypeID option:selected').val() == "2")  //secodary
    		{
    			$("#DistributroIDTD").show();
    	    	$("#DistributroNameTD").show(); 
    	    	
    	    	if($("#isEditForSelection").val() == "1")
    	    	{
    	    		$("#SapCode").val("");
        	    	$("#FirstName").val("");
        			$("#LastName").val("");
        			$("#DisplayName").val("");
        			$("#Email").val("");        		    			
        			$("#Disignation").val("");
        			$("#Department").val("");
        			$("#DistributorID").val("");
        			$("#DistributorName").val("");
        			
        			$("#isEdit").val("1"); //changing the bit for insertion        		    			
        			$('#ForCheckBoxID input:checkbox').each(function() {          		    	    	   
        	    	    $("#FeatureCheckbox"+this.value).attr("checked",false).checkboxradio("refresh");
        	    	    //alert("reseting");
        	    	});
        		
        			
        			
    	    	}
    	    	
    		}
    		else
			{    			
    			$("#DistributroIDTD").hide();
    	    	$("#DistributroNameTD").hide(); 
			}
    		/*$('input[name="FeatureCheckbox"]:checked').each(function() {
    			   alert(this.value);
    			});*/
    	  });
    	
    	$('#SapCode').bind("change", function(e, data){
    		$("#populateDataUl").html("");
    		LoadEditUserForm(this.value);
    	});
    	
        
    });
    
    function UserSearch()
    {
    	$( "#LookupUserSearch" ).on( "popupbeforeposition", function( event, ui ) {
    		lookupUserInit();
		} );
		$('#LookupUserSearch').popup("open");
    }
    
    
    function DistributorSearchCallBackForUserRights(SAPCode, DistributorName){
    	if($("#isSecondDistCall").val()=="0") //normal case
		{
    		$('#DistributorID').val(SAPCode);
        	$('#DistributorName').val(DistributorName);
		}
    	else if($("#isSecondDistCall").val()=="1") //2nd case
		{
    		$('#DistributorID2').val(SAPCode);
        	$('#DistributorName2').val(DistributorName);
		}
    	
    }
    
    function UserRightAddUser()
    {
    	
    }
    
    function UserRightSubmit()
    { //alert($("#isEdit").val());
    	//alert("Function of sumbission is called ");
    	var isValidflag = true;
    	if( $('#SapCode').val() == "" ){
    		document.getElementById('SapCode').focus();
    		isValidflag = false;
    	}else{
    		var value = $('#SapCode').val();
    		if(isInteger(value) == false){
    			document.getElementById('SapCode').focus();
    			isValidflag = false;
    		}
    	}
    	
    	
    	
    	if( $('#FirstName').val() == "" ){
    		document.getElementById('FirstName').focus();
    		isValidflag = false;
    	}
    	
    	if( $('#LastName').val() == "" ){
    		document.getElementById('LastName').focus();
    		isValidflag = false;
    	}
    	
    	if( $('#DisplayName').val() == "" ){
    		document.getElementById('DisplayName').focus();
    		isValidflag = false;
    	}
    	
    	if( $('#Email').val() == "" ){
    		document.getElementById('Email').focus();
    		isValidflag = false;
    	}
    	else
		{
    		var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    	    if(!pattern.test($('#Email').val()))
    	    {
    	    	document.getElementById('Email').focus();
    	    	isValidflag = false;
    	    }
		}
    	
    	if( $('#Disignation').val() == "" ){
    		document.getElementById('Disignation').focus();
    		isValidflag = false;
    	}
    	
    	if( $('#Department').val() == "" ){
    		document.getElementById('Department').focus();
    		isValidflag = false;
    	}
    	
    	if($('#SaleTypeID option:selected').val() == "-1") //if type is not selected
		{
    		isValidflag = false;
    		document.getElementById('SaleTypeID').focus();    		
		}
    	else if($('#SaleTypeID option:selected').val() == "2") //if type is secondary then check the distributor id
		{
    		if( $('#DistributorID').val() == "" || $('#DistributorID').val() == "0" ){
        		document.getElementById('DistributorID').focus();
        		isValidflag = false;
        	}
		}
    	
    	if( $('#payroll_id').val() == "" ){
    		document.getElementById('payroll_id').focus();
    		isValidflag = false;
    	}
    	
    	if($("#isEdit").val()=="1") //for insertion
		{
    		if( $('#password').val() == "" ){
        		document.getElementById('password').focus();
        		isValidflag = false;
        	}
		}
    	if($('#password').val().length > 0){
    		if ($('#password').val().length < 8 || checkPasswordCharacters($('#password').val()) == false ){
        		alert("The password must be at least 8 characters long and contain:\n1) Atlest one number.\n2) Atleast one alphabet.");
        		document.getElementById("password").focus();
        		return false;
        	}
    	}
    	
    	
        	if(isValidflag)
    		{		
        		$.ajax({
        		    url: "employee/UserAdminExecute",
        		    
        		    data: $("#UserRightForm" ).serialize(),
        		    type: "POST",
        		    dataType : "json",
        		    success: function( json ) {
        		    	if (json.success == "true"){
        		    		location = "UserAdmin.jsp";
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
    }
    
    function checkPasswordCharacters(password){
    	
    	var hasInteger = false;
    	var hasAlphabets = false;
    	
    	var letters=/^[a-zA-Z]+$/;
    	//var numbers=/^[0-9]+$/;
    	
    	for(var i = 0; i < password.length; i++){
    		
    		if( parseInt(password.charAt(i)) > 0 ){
    			hasInteger = true;
    		}
    		
    		if( password.charAt(i).search(letters) == 0 ){
    			hasAlphabets = true;
    		}
    		
    		if(hasInteger && hasAlphabets){
    			return true;
    		}
    		
    	}// end for
    	
    	if(!hasInteger || !hasAlphabets){
    		return false;
    	}
    	
    }
    
    function UserAdditionalRightSubmit()
    {
    	if($("#SapCode").val()!="")
    	{
    		$.ajax({
    		    url: "employee/UserAdminAdditionalRightExecute",
    		    
    		    data: $("#UserRightForm" ).serialize(),
    		    type: "POST",
    		    dataType : "json",
    		    success: function( json ) {
    		    	if (json.success == "true"){
    		    		//location = "UserRight.jsp";
    		    		$("#WarehouseeSave").addClass('ui-disabled');
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
    	else
		{
    		alert("Please Select User ID");
		}
    	
    }
    
    function LoadEditUserForm(SapCode)
    { 
    	//alert(SapCode+"Hello");
    	$("#isEdit").val("0");
    	flag = true;
    	var value = SapCode;
    	if(isInteger(value) == false){
		document.getElementById('SapCode').focus();		
		flag = false;
		
    	}
    	if(flag == true)
		{
    		//alert("hello I am in if");
    		$.ajax({
    		    url: "employee/UserAdminExecute",
    		    
    		    data: $("#UserRightForm" ).serialize(),
    		    type: "POST",
    		    dataType : "json",
    		    success: function( json ) {
    		    	if (json.success == "true"){
    		    		//alert(json.isExist);
    		    		if(json.isExist=="true") //its mean record exist in db and populate the form and launch the edit case
    	    			{
    		    			
    		    			$('#ForCheckBoxID input:checkbox').each(function() {
     		    	    	   
    		    	    	    $("#FeatureCheckbox"+this.value).attr("checked",false).checkboxradio("refresh");
    		    	    	    //alert("reseting");
    		    	    	});
    		    			
    		    			$("#isEdit").val("2");  //changing the flag for eidt case
    		    			$("#isEditForSelection").val("0");//chaning the flag to 0 bec we found data and it is edit case 
    		    			
    		    			//enable additional rights buttons   		    			
    		    			//alert(json.UserActive);

    		    			$("#SapCode").val(json.SapCode);
    		    			$("#password").val();
    		    			$("#FirstName").val(json.FirstName);
    		    			$("#LastName").val(json.LastName);
    		    			$("#DisplayName").val(json.DisplayName);
    		    			$("#Email").val(json.Email);
    		    			$("#Disignation").val(json.Designation);
    		    			$("#Department").val(json.Department);
    		    			//alert(json.TypeID);
    		    			$("#SaleTypeID").val(json.TypeID).change();
    		    			$("#SaleTypeID").val(json.TypeID).change();
    		    			$("#DistributorID").val(json.DistributorID);
							$("#payrollId").val(json.PayrollId);
							$("#UserRightUserBA").val(json.BrandAmbassador);

    		    			$("#DistributorName").val(json.DistributorName);
    		    			$("#UserRightUserActive").val(json.UserActive).change();
    		    			
    		    			$("#UserRightDistributorGrp").val(json.DefaultGroupID).change();
    		    			
    		    			
    		    			for( var i = 0; i < json.rows.length; i++ ){
    		    				//alert("FeatureCheckbox"+json.rows[i].FeatureId);
    		    				//alert(json.rows[i].FeatureId);
    		    				//$("#FeatureCheckbox"+json.rows[i].FeatureId).attr("checked",true).checkboxradio("refresh");
    		    				$('#FeatureCheckbox'+json.rows[i].FeatureId).prop('checked', true).checkboxradio("refresh");
    		    				
    		    				//if($("#checkSurfaceEnvironment-1").prop('checked') == true)
    		    				//alert($("#FeatureCheckbox"+json.rows[i].FeatureId)).;
    		    				
    		    			}
    		    			
    		    			$('#AdditionalRightDiv a').each(function(){
    		    				$("#"+$(this).attr('id')).removeClass('ui-disabled');
    		    				//alert($(this).attr('id'));
    		    	    	});
    		    			
    		    		}
    		    		else
    	    			{   		    			
    		    			if($('#SaleTypeID option:selected').val()=="1") //only for primary case
    		    			{
    		    				$("#isEditForSelection").val("1"); //changing the flag to 1 to get the user info against employee lookup to load the data
    		    				LoadUserFormForInsertion($("#SapCode").val());
    		    			}  
    		    			else
		    				{
        		    			$("#FirstName").val("");
        		    			$("#LastName").val("");
        		    			$("#DisplayName").val("");
        		    			$("#Email").val("");        		    			
        		    			$("#Disignation").val("");
        		    			$("#Department").val("");
        		    			
        		    			$("#DistributorID").val("");
        	        			$("#DistributorName").val("");
        		    			
        		    			$("#isEdit").val("1"); //changing the bit for insertion        		    			
        		    			$('#ForCheckBoxID input:checkbox').each(function() {          		    	    	   
        		    	    	    $("#FeatureCheckbox"+this.value).attr("checked",false).checkboxradio("refresh");
        		    	    	    //alert("reseting");
        		    	    	});
		    				}
    		    			$("#isEdit").val("1"); //changing the bit for insertion
    		    		} 
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
    }
    
    function LoadUserFormForInsertion(SapCode)
    { 
    	
    	var value = SapCode;
    	//alert("Function calliing "+value);
    	if(isInteger(value) == false){
		document.getElementById('SapCode').focus();
		flag = false;
		
    	}
    	var value = SapCode;
    	if(flag == true)
		{
    	
    		//alert("hello I am in if");
    		$.ajax({
    		    url: "employee/UserAdminExecute",
    		    
    		    data: $("#UserRightForm" ).serialize(),
    		    type: "POST",
    		    dataType : "json",
    		    success: function( json ) {
    		    	if (json.success == "true"){
    		    		//alert(json.isExist);
    		    		if(json.isExist1=="true") //its mean record exist in db and populate the form and launch the edit case
    	    			{
    		    			
    		    			//alert(Usman);
    		    			$("#SapCode").val(json.SapCode);
    		    			
    		    			$("#FirstName").val(json.FirstName);
    		    			$("#LastName").val(json.LastName);
    		    			$("#DisplayName").val(json.DisplayName);
    		    			$("#Email").val("");
    		    			
    		    			$("#Disignation").val(json.Designation);
    		    			$("#Department").val(json.Department);
    		    			
    		    			$("#isEdit").val("1"); //changing the bit for insertion
    		    			
    		    			$('#ForCheckBoxID input:checkbox').each(function() {
      		    	    	   
    		    	    	    $("#FeatureCheckbox"+this.value).attr("checked",false).checkboxradio("refresh");
    		    	    	    //alert("reseting");
    		    	    	});
    		    			
    		    			$('#AdditionalRightDiv a').each(function(){
    		    				
    		    				$("#"+$(this).attr('id')).addClass('ui-disabled');
    		    				//alert($(this).attr('id'));
    		    	    	});
    		    			
    		    			
    		    		}
    		    		
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
    }
    
    
    
    function UserRightsUserEdit(SAPCode, EmployeeName){    	
    	$("#isEdit").val("0");
    	$("#SapCode").val(SAPCode).change();
    	//LoadEditUserForm(SAPCode);
    	
    }
    
    function EnableCheckBoxes()
    {
    	if($("#DoNotSpecify").prop('checked') == true){
    		//alert();
    		//enabling the checkboxes
    		$("#Warehouse").attr("checked",false).checkboxradio("refresh");
    		$("#Region").attr("checked",false).checkboxradio("refresh");
    		$("#Distributor").attr("checked",false).checkboxradio("refresh");
    		
    		$('#Warehouse').prop('disabled', true).checkboxradio("refresh");
        	$('#Region').prop('disabled', true).checkboxradio("refresh");
        	$('#Distributor').prop('disabled', true).checkboxradio("refresh");
        	
        	$("#LoadDataDrpDwn").hide();
        	$("#LoadDataDrpDwnRegion").hide();
        	$("#DistributorTable").hide();
        	$("#LoadWareHouseToggle").val("0");
        	$("#LoadRegionToggle").val("0");
        	$("#LoadDistributoriToggle").val("0");        	
        	
    	}
    	else
		{
    		//enabling the checkboxes
        	$('#Warehouse').prop('disabled', false).checkboxradio("refresh");
        	$('#Region').prop('disabled', false).checkboxradio("refresh");
        	$('#Distributor').prop('disabled', false).checkboxradio("refresh");
		}
    	
    }
    
    function SetTitle(title,FeatureID)
    {
    	//alert(FeatureID);
    	
    	$('#FeatureIDFOrWhole').val(FeatureID);
    	
    	//$("#datascope").show();
    	//alert($('#TitleGoesHere span.ui-btn-text'));
    	if($("#SapCode").val()!="")
		{
    		$.get('UserRightAdminDataScope.jsp?title='+title+'&featureid='+FeatureID+'&UserID='+$("#SapCode").val(), function(data) {   			
  			  $("#LoadDataDrpDwn").html(data);
  			  $("#LoadDataDrpDwn").trigger('create');
    		});
      	
    		$("#WarehouseeSave").removeClass('ui-disabled');
      		//alert();
      	
      		setTimeout(function(){
      			
  	    		$('#DistributorID2').on('dblclick', function(e, data){
  	    			
  	    			$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
  	        			lookupDistributorInit();
  	        		} );
  	        		$('#LookupDistributorSearch').popup("open");
  	        		$("#isSecondDistCall").val("1");  //setting the flag for second distributor call function
  	        		
  	        		
  	    		});	
  	    		$("#populateDataUl").listview("refresh");
      		}, 2000);
		}
    	else
		{
    		alert("Please Select User");
		}
    	
    	
    	
    	
    		
    		
    	

    	//$('#TitleGoesHere span.ui-btn-text').html(title)
    }
    
    function LoadWareHouse()
    {
    	//alert($("#LoadWareHouseToggle").val());
    	
    		$.get('GetAllWarehouse.jsp', function(data) {   			
  			  $("#LoadDataDrpDwn").html(data);
  			  $("#LoadDataDrpDwn").trigger('create');
    		});
    		
		if($("#LoadWareHouseToggle").val()=="0")
		{
			$("#LoadDataDrpDwn").show();
			$("#LoadWareHouseToggle").val("1");
		}
    	else
		{
    		$("#LoadDataDrpDwn").hide();
    		$("#LoadWareHouseToggle").val("0");
		}
    }
        
    function AddWareHouse()
    {
    	//alert($("#WarehouseSelect").val());
    	//alert($("#WarehouseSelect option:selected").text());
    	//alert("Ware house "+$("#WarehouseSelect option:selected").val()+" - "+$('#FeatureIDFOrWhole').val());
    	$("#WarehouseeSave").removeClass('ui-disabled');
    	var WarehouseAlreadyAdded = false;
    	$('#populateDataUl li input').each(function(){    		
    		var NewInsertedListID = $("#WarehouseSelect option:selected").val()+","+$('#FeatureIDFOrWhole').val()+",Warehouse";
    		
    		//if($("#WarehouseSelect option:selected").text().trim() == $(this).text().trim())
    		//alert($('input[name="Warehouse"]:checked').val());
    		if($(this).val().trim() == NewInsertedListID.trim())
    		{
    			WarehouseAlreadyAdded = true;
    			//alert('matched');
			}
    	});
    	//alert($('#FeatureIDFOrWhole').val());
    	var FunctionName = "onClick=RemoveList('WarehouseDynamicallyAdded_"+$("#WarehouseSelect option:selected").val()+"')";
    	if(!WarehouseAlreadyAdded && $("#WarehouseSelect option:selected").val() != "-1")
		{
    		var AppendedString = "<li data-mini='true' data-icon='delete' id='WarehouseDynamicallyAdded_"+$("#WarehouseSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' name='Warehousehiddenfield' value='"+$("#WarehouseSelect option:selected").val()+","+$('#FeatureIDFOrWhole').val()+",Warehouse'/><a href='#'>"+$("#WarehouseSelect option:selected").text()+"</a></li>";
    		//alert(AppendedString);
    		$("#populateDataUl").append(AppendedString);    	
        	$("#populateDataUl").listview("refresh");
		}
	
    	
    	
    }
    
    function LoadRegion()
    {
    	$.get('GetAllRegion.jsp', function(data) {   			
			  $("#LoadDataDrpDwnRegion").html(data);
			  $("#LoadDataDrpDwnRegion").trigger('create');
		}); 
    	
    	if($("#LoadRegionToggle").val()=="0")
		{
			$("#LoadDataDrpDwnRegion").show();
			$("#LoadRegionToggle").val("1");
		}
    	else
		{
    		$("#LoadDataDrpDwnRegion").hide();
    		$("#LoadRegionToggle").val("0");
		}
    	
    }
    
    function AddRegion()
    {
    	//alert($("#WarehouseSelect").val());
    	//alert($("#WarehouseSelect option:selected").text());
    	//alert("Region "+$("#RegionSelect option:selected").val()+" - "+$('#FeatureIDFOrWhole').val());
    	var RegionAlreadyAdded = false;
    	$("#WarehouseeSave").removeClass('ui-disabled');
    	$('#AddeddRegion li input').each(function(){ 
    		var NewInsertedListID = $("#RegionSelect option:selected").val()+","+$('#FeatureIDFOrWhole').val()+",Region";    		
    		//alert(NewInsertedListID);
    		if($(this).val().trim() == NewInsertedListID.trim())
    		{
    			RegionAlreadyAdded = true;
    			//alert('matched');
			}
    	});

    	var FunctionName = "onClick=RemoveList('RegionDynamicallyAdded_"+$("#RegionSelect option:selected").val()+"')";
    	
    	if(!RegionAlreadyAdded && $("#RegionSelect option:selected").val() != "-1")
		{
    		var AppendedString = "<li data-mini='true' data-icon='delete' id='RegionDynamicallyAdded_"+$("#RegionSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='RegionDynamicallyAddedHidden_"+$("#RegionSelect option:selected").val()+"' name='RegionIDhiddenfield' value='"+$("#RegionSelect option:selected").val()+","+$('#FeatureIDFOrWhole').val()+",Region'/><a href='#'>"+$("#RegionSelect option:selected").text()+"</a></li>";
    		
    		$("#populateRegionDataUl").append(AppendedString);    	
        	$("#populateRegionDataUl").listview("refresh");
		}
    	
    	
    }
    
    function LoadDistributor()
    {
    	
    	if($("#LoadDistributoriToggle").val()=="0")
		{
			$("#DistributorTable").show();
			$("#LoadDistributoriToggle").val("1");
		}
    	else
		{
    		$("#DistributorTable").hide();
    		$("#LoadDistributoriToggle").val("0");
		}
    	
    }
    
    function AddDistributor()
    {
    	//alert("Distributor "+$("#DistributorID2").val()+" - "+$('#FeatureIDFOrWhole').val());
    	$("#WarehouseeSave").removeClass('ui-disabled');
    	var DistributorAlreadyAdded = false;
    	$('#AddeddDistributor li input').each(function(){
    		//alert($(this).text().trim()+"-"+$("#DistributorName2").val());
    		
    		//alert($("#DistributorIDForHiddenFieldID_"+$("#DistributorID2").val()).val());
    		//alert($(this).val());
    		var NewInsertedListID = $("#DistributorID2").val()+","+$('#FeatureIDFOrWhole').val()+",Distributor"; //distributorid,featureid
    		//if($("#DistributorName2").val().trim() == $(this).text().trim() && $("#DistributorID2").val().trim() != $('#FeatureIDFOrWhole').val().trim()) //the same distributor with same feature id is not allowd for second time
			//alert($(this).val().trim()+" - "+NewInsertedListID);
    		if($(this).val().trim() == NewInsertedListID.trim())
    		{
    			DistributorAlreadyAdded = true;
    			//alert('matched');
			}
    	});
    	
    	var FunctionName = "onClick=RemoveList('DistributorIDDynamicallyAdded_"+$("#DistributorID2").val()+"')";
    	if(!DistributorAlreadyAdded && $("#DistributorID2").val().trim()!="")
		{
    		var AppendedString = "<li data-mini='true' data-icon='delete' id='DistributorIDDynamicallyAdded_"+$("#DistributorID2").val()+"' value='"+$("#DistributorID2").val()+","+$('#FeatureIDFOrWhole').val()+"' "+ FunctionName+"><input type='hidden' id='DistributorIDForHiddenFieldID_"+$("#DistributorID2").val()+"' name='DistributorIDhiddenfield' value='"+$("#DistributorID2").val()+","+$('#FeatureIDFOrWhole').val()+",Distributor'/><a href='#'>"+$("#DistributorID2").val()+" - "+$("#DistributorName2").val()+"</a></li>";
    		$("#populateDistributorDataUl").append(AppendedString);    	
        	$("#populateDistributorDataUl").listview("refresh");
		}
    	
    }
    
    function AddGroupDistributor()
    {
    	
    	var GroupAlreadyAdded = false;
    	$("#WarehouseeSave").removeClass('ui-disabled');
    	$('#AddeddGroupDistributor li input').each(function(){ 
    		var NewInsertedListID = $("#GroupDistributorSelect option:selected").val()+","+$('#FeatureIDFOrWhole').val()+",GroupDistributor";    		
    		//alert(NewInsertedListID);
    		if($(this).val().trim() == NewInsertedListID.trim())
    		{
    			GroupAlreadyAdded = true;
    			//alert('matched');
			}
    	});

    	var FunctionName = "onClick=RemoveList('GroupDistributorDynamicallyAdded_"+$("#GroupDistributorSelect option:selected").val()+"')";
    	
    	if(!GroupAlreadyAdded && $("#GroupDistributorSelect option:selected").val() != "-1")
		{
    		var AppendedString = "<li data-mini='true' data-icon='delete' id='GroupDistributorDynamicallyAdded_"+$("#GroupDistributorSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='GroupDistributorDynamicallyAddedHidden_"+$("#GroupDistributorSelect option:selected").val()+"' name='GroupDistributorhiddenfield' value='"+$("#GroupDistributorSelect option:selected").val()+","+$('#FeatureIDFOrWhole').val()+",GroupDistributor'/><a href='#'>"+$("#GroupDistributorSelect option:selected").text()+"</a></li>";
    		
    		$("#populateGroupDistributorDataUl").append(AppendedString);    	
        	$("#populateGroupDistributorDataUl").listview("refresh");
		}
    	
    	
    }  
    
    function RemoveList(ID)
    {
    	$("#"+ID).remove();
    	$("#populateDistributorDataUl").listview("refresh");
    	$("#populateRegionDataUl").listview("refresh");
    	$("#populateDataUl").listview("refresh");
    	$("#WarehouseeSave").removeClass('ui-disabled');
    }
    
    function getDistributorName(){
    	//alert();
    	if(isInteger($('#DistributorID').val()) == false ){
    		$('#DistributorID').val('');
    		return false;
    	}
    	
    	$.ajax({
    		
    		url: "common/GetDistributorInfoJson",
    		data: {
    			DistributorID: $('#DistributorID').val()
    		},
    		type:"POST",
    		dataType:"json",
    		success:function(json){
    			if(json.exists == "true"){
    				$('#DistributorName').val(json.DistributorName);
    			}else{
    				$('#DistributorName').val('');
    			}
    		},
    		error:function(xhr, status){
    			alert("Server could not be reached.");
    		}
    		
    	});
    	
    }
    
    function getDistributorName2(){
    	//alert();
    	if(isInteger($('#DistributorID2').val()) == false ){
    		$('#DistributorID2').val('');
    		return false;
    	}
    	
    	$.ajax({
    		
    		url: "common/GetDistributorInfoJson",
    		data: {
    			DistributorID: $('#DistributorID2').val()
    		},
    		type:"POST",
    		dataType:"json",
    		success:function(json){
    			if(json.exists == "true"){
    				$('#DistributorName2').val(json.DistributorName);
    			}else{
    				$('#DistributorName2').val('');
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
    
    
    
    
    
    