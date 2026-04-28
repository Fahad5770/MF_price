function isInteger (o) {
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}

$(function() {
	 
	$('#DistributionID').change(function(){
	var DistributorID=$('#DistributionID').val();
		
		$.ajax({
		    url: "mobile/Report242SummaryDistributorInfoJson",
		    data: {
		    	DistributorID: DistributorID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if (json.success == "true"){
		    	 	$('#DistributionName').val(json.DistributorName);
		    	
		    		
		    	}else{
		    		alert("Distributor ID is not valid");
		    	}
		    	
		    },
		 
	 });
	
 });
	
	
});

function getAgencyName(){

		var DistributorID=$('#AgencyID').val();
			
			$.ajax({
			    url: "mobile/Report242SummaryDistributorInfoJson",
			    data: {
			    	DistributorID: DistributorID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.success == "true"){
			    	 	$('#AgencyName').val(json.DistributorName);
			    	
			    		
			    	}else{
			    		alert("Agency ID is not valid");
			    	}
			    	
			    },
			 
		 });
		
	
		
		
}

function submitForm(){
	if( $('#Date').val()=="" ){
		//alert("Enter Date");
		$('#Date').focus();
		return false;
	}
	/*if($('#Requestedby').val()==false ){
			alert("Enter a value for Requested By");
			$('#Requestedby').focus();
			return false;
	}else{
		if( isInteger( $('#Requestedby').val() )==false ){
			alert("Enter a numeric value for Requested By");
			$('#Requestedby').focus();
			return false;
		}  S
	}*/
	
	//Contact Person Number
/*	if( $('#ContactNo').val()=="" ){
		alert("Enter contact number");
		$('#ContactNo').focus();
		return false;
	}*/  
	//if( $('#SAPcode').val() == false ){
			//alert("Eneter Sap-Code");
			//$('#SAPcode').focus();
			//return false;
		//}
	
	
	/*if($("#Requestfor").val()==""){
		alert("Enter Description of Request For");
		$('#Requestfor').focus();
		return false;
	}*/
	
	/*if($('#AgencyID').val()  == false ){
		alert("Enter a valid Agency ID");
		$('#AgencyID').focus();
		return false;
}else{
	if( isInteger( $('#AgencyID').val() ) == false ){
		alert("Enter a numeric value for Distributor ID");
		$('#AgencyID').focus();
		return false;
	}
}*/
	/*if( $('#AgencyName').val()=="" ){
		alert("Enter Agency name");
		$('#AgencyName').focus();
		return false;
	}
	
	*/
	
	if( $('#OutletName').val()=="" ){
		alert("Enter Outlet Name");
		$('#OutletName').focus();
		return false;
	}
	if( $('#OutletAddress').val()==false ){
			alert("Eneter Outlet Address");
			$('#OutletAddress').focus();
			return false;
		}
	
	
	
	if( $('#OwnerName').val()=="" ){
		alert("Enter Owner Name");
		$('#OwnerName').focus();
		return false;
	}
	if($('#OutletContactNo').val()==false ){
			alert("Enter outlet Contact Number");
			$('#OutletContactNo').focus();
			return false;
		}
	
	
	if( $('#CNICNo').val() == "" ){
		alert("Enter CNIC number");
		$('#CNICNo').focus();
		return false;
	}
	
	
/*	if($("#VehicleNo").val()==""){
		alert("Enter Vehicle Number");
		$('#VehicleNo').focus();
		return false;
	}*/
	if( $('#BeatPlan').val()==-1 ){
		alert("Select Beat Plan");
		$('#BeatPlan').focus();
		return false;
	}
	
	if($('#Subchannels').val()==-1 ){
		alert("Select Sub channel");
		$('#Subchannels').focus();
		return false;
	}
	if($('#vpoclassification').val()==-1 ){
		alert("Select Sub Vpo Classification");
		$('#vpoclassification').focus();
		return false;
	}
	
	if($('#RequesterRemarks').val()==false ){
			alert("Enter Request Remarks");
			$('#RequesterRemarks').focus();
			return false;
		}
	if($('#commoncategory').val()==-1 ){
		alert("Select Category");
		$('#commoncategory').focus();
		return false;
	}

	//Contact Person Number
	//if( $('#SNDRemarks').val() == "" ){
		//alert("Enter SND Remarks");
		//$('#SNDRemarks').focus();
		//return false;
	//}
	/*if( $('#RMID').val()==false ){   
			alert("Enter RM ID ");  
			$('#RMID').focus();
			return false;
	}else{
		if( isInteger( $('#RMID').val() )==false ){
			alert("Enter a numeric value for RM ID");
			$('#RMID').focus();
			return false;
		}
	}*/
	
	
	//if($("#RouteAreaID").val()==""){
		//alert("Enter Route Area ID");
		//$('#RouteAreaID').focus();
		//return false;
	//}else{
		//if( isInteger( $('#RouteAreaID').val() )==false ){
			//alert("Enter a numeric value for Route Area ID");
			//$('#RouteAreaID').focus();
			//return false;
		//}
	//}
	if( $('#DistributionID').val()==false){
		alert("Enter Distributor ID");
		$('#DistributionID').focus();
		return false;
	}else{
		if( isInteger( $('#DistributionID').val() )==false ){
			alert("Enter a numeric value for Distributor ID");
			$('#DistributionID').focus();
			return false;
		}
	}

	if($('#PJPID :checked').length == 0)
	{
	    alert ( "Please select at least one PJP" );
	    return false;
	}

//if($("#DistributionName").val()==""){
	//alert("Enter Distributor Name");
	//$('#DistributionName').focus();
	//return false;
//}


	
	
	//var contact=$("#ContactNo").val();
	//var numbers= /^[0-9]+$/;
	//if(!contact.value.match(numbers) ){
		//alert("Enter valid contact person number");
	//}
	$("#SubmitButton").addClass("ui-disabled");
	$("#ResetButton").addClass("ui-disabled");
	$("#ViewButton").addClass("ui-disabled");
	
	
	$.mobile.showPageLoadingMsg();
	//alert("hey");
	$.ajax({
		url:"outlet/AddOutletExecute",
		data:$('#AddOutletForm').serialize(),
		type:"POST",
		dataType:"json",
		success: function(json){
			$.mobile.hidePageLoadingMsg();
			//$('#SubmitButton').removeClass("ui-disabled");
			//$('#ResetButton').removeClass("ui-disabled");
			//$('#ViewButton').removeClass("ui-disabled");
			
			if(json.success=="true"){
				$.mobile.hidePageLoadingMsg();
				alert("Data has been Saved Successfully! New Outlet ID is "+json.newoutletID);
				window.location = 'NewOutletRequest.jsp';
				
			}else{
				alert("Data could not be saved. "+json.error);
			}
			
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
			$.mobile.loading( 'hide');
		}
	});
	
}


function foucsnext(e){
	
		 var $canfocus = $(':focusable');
	        var index = $canfocus.index(document.activeElement) + 1;
	        if (index >= $canfocus.length) index = 0;
	        $canfocus.eq(index).focus();
	        
	   
}

function CheckCnicDuplication(){
	$.mobile.showPageLoadingMsg();
	$("#SubmitButton").addClass("ui-disabled");
	var EnteredCnic=$('#CNICNo').val();
	
	var regexCnic = /^[0-9]+$/;
	//alert(EnteredCnic);
	if((regexCnic.test(EnteredCnic)) == true){
	$.ajax({
		url:"outlet/GetOutletCnicJson",
		data:{
			EnteredCnic:EnteredCnic
		},
		type:"POST",
		dataType:"json",
		success: function(json){
			$.mobile.hidePageLoadingMsg();	
			if(json.success=="true"){
			alert("Outlet already exists against this CNIC ");
			}else{
				//alert();
				$('#SubmitButton').removeClass("ui-disabled");
			}
			
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
			$.mobile.loading( 'hide');
		}
	});
	}else
		{
		alert("Cnic should be in numeric");
		//$('#SubmitButton').removeClass("ui-disabled");
		}

}	
 

		