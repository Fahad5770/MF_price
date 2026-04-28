function getSub(id){
	var numid=id;
	var CatID=$('#Cat_'+numid).val()
	//alert(numid);
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "crm/CrmHelpDeskComplaintInfoJson",
		data: {
			CatID: CatID
		},
		type:"POST",
		dataType:"json",
		 success: function(json) {
			 $.mobile.hidePageLoadingMsg();
		    	if (json.exists == "true"){
		    		var display=0;
		    		if(CatID==4){
		    			display="display:none";
		    		}
		    		var str = "<fieldset data-role='controlgroup' data-mini='true' style="+display+" data-iconpos='right'><legend>Sub Category:</legend>";
		    		for(var i = 0; i < json.rows.length; i++){
		    			var SubId=json.rows[i].id;
		    			var Sublabel=json.rows[i].label;
		    			var checkFlag=null;
		    			if(SubId==7 || SubId==13 || SubId==1){
		    			checkFlag="checked='checked'";
		    				}
		    			str += "<input type='radio' name='subcategory' id='subcat_"+SubId+"' value='"+SubId+"'"+checkFlag+" ><label for='subcat_"+SubId+"'>"+Sublabel+"</label>";
		    		
		    		}
		    		
		    		$("#SubCat").html(str);
		    		$("#CRMRegCOm").trigger('create');
		    	}else{
		    		alert('Server could not be reached.');
		    }
		    }
		,
			
	});
	
}


function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}


function submitForm(){
	
	if($('#Asset_code').val() =="" ||  $('#outletID').val() == ""){
		if($('#Asset_code').val() ==""){
			
			if($('#outletID').val() == "" ){
				alert("Enter Asset Code OR Outlet ID");
				return false;
			}
			else{ 
				if( isInteger( $('#outletID').val() ) == false ){
							alert("Enter a valid outlet ID");
							$('#outletID').focus();
							return false;
				}
					
			}
		}
	}
	
	//if($('#Asset_code').val() !="" ){
		//if( isInteger( $('#Asset_code').val() ) == false ){
			//alert("Enter a valid Asset Code");
			//$('#Asset_code').focus();
			//return false;
		//}
	//}
	
	//Outlet ID 
	//if($("#outlet_name").val()=="Not Found" && $("#outletID").val()!="Not Found"){
		//alert("Enter Valid outlet ID Name");
		//$('#outletID').focus();
		//$('#outletID').val("Not Found");
		//return false;
	//}else if($("#outlet_name").val()=="Not Found" &&  $("#outletID").val()=="" &&  $("#Asset_code").val()=="" ){
		//alert("Enter Valid outlet ID Name");
		//$('#outletID').focus();
		//$('#outletID').val("Not Found");
	//}
	
	//Contact Person Name
	if($("#PersonContactName").val()==""){
		alert("Enter Contact Person Name");
		$('#PersonContactName').focus();
		return false;
	}
	
	//Contact Person Number
	if( $('#ContactNo').val() == "" ){
		alert("Enter contact number");
		$('#ContactNo').focus();
		return false;
	}else{
		if( isInteger( $('#ContactNo').val() ) == false ){
			alert("Contact Number must be an Integer.");
			$('#ContactNo').focus();
			return false;
		}
	}
	
	if($("#Description").val()==""){
		alert("Enter Description");
		$('#Description').focus();
		return false;
	}
	
	//var contact=$("#ContactNo").val();
	//var numbers= /^[0-9]+$/;
	//if(!contact.value.match(numbers) ){
		//alert("Enter valid contact person number");
	//}
	$("#SubmitButton").addClass("ui-disabled");
	$("#ResetButton").addClass("ui-disabled");
	$("#ViewButton").addClass("ui-disabled");
	//$('#SubmitButton').attr('disabled', 'disabled');
	//$('#ResetButton').attr('disabled', 'disabled');
	//$('#ViewButton').attr('disabled', 'disabled');
	
	$.mobile.showPageLoadingMsg();
	//alert("hey");
	$.ajax({
		url:"crm/CRMHelpDeskComplaintExecute",
		data:$('#CrmRegisterComplaintForm').serialize(),
		type:"POST",
		dataType:"json",
		success: function(json){
			$.mobile.hidePageLoadingMsg();
			$('#SubmitButton').removeClass("ui-disabled");
			$('#ResetButton').removeClass("ui-disabled");
			$('#ViewButton').removeClass("ui-disabled");
			
			if(json.success=="true"){
				alert("Complaint Registered Successfully! Your complaint Number is "+json.ComplaintNO);
				window.location = 'CRMHelpDeskComplaint.jsp';
				$.mobile.hidePageLoadingMsg();
			}else{
		    		alert('Server could not be reached.');
		    }
			
		}
	});
	
}


function getOutletName(){
	
	var id=$('#outletID').val();
	$.ajax({
		url: "crm/CrmHelpDeskComplaintInfoJson",
		data: {
			id:id
			
		},
		type:"POST",
		dataType:"json",
		 success: function(json) {
			 $.mobile.hidePageLoadingMsg();
		    	if (json.exists == "true"){
		    		
		    		$('#outlet_name').val(json.name);
		    	}else{
		    		alert('Server could not be reached.');
		    }
		 },
	});
}

function getOutletNamebyAssetCode(){

	
	var asset=$('#Asset_code').val();
	var flagForquery=1;
	
	
	$.ajax({
		url: "crm/CrmHelpDeskComplaintInfoJson",
		data: { asset : asset,flagForquery : flagForquery },
		type:"POST",
		dataType:"json",
		 success: function(json) {
			 $.mobile.hidePageLoadingMsg();
		    	if (json.exists == "true"){
		    		
		    		$('#outletID').val(json.outlet_id);
		    		$('#outlet_name').val(json.name);
		    	}else{
		    		alert('Server could not be reached.');
		    }
		 },
	});
}

	
 

		