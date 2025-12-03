
$( document ).delegate("#DispatchMain", "pageinit", function() {	
	
	$( ".ClassDispatchSalesSelect" ).on( "slidestop", function( event ) { GenerateSunnary(this.id)} )
	//$( ".ClassDispatchSalesSelect" ).on( "slidestop", function( event ) { GenerateSunnary(this.id)} )
	
	$("#DispatchMainDistributorID").parent().css("width","100%");
	
	$('#DispatchMainDistributorID').on('dblclick', function(e, data){
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open");
	});
	
	//Fixing the Summary
	
	$(window).scroll(function() {	       
	       
	    $('#DispatchSalesSummary').css({'position':'fixed','top':'125pt'});
	       
	    });
	
	
});

function DistributorSearchCallBack(SAPCode, DistributorName){
		$('#DispatchMainDistributorID').val(SAPCode);
		$('#DispatchMainDistributorName').val(DistributorName);
}

function getDistributorName(){
	if(isInteger($('#DispatchMainDistributorID').val()) == false ){
		$('#DispatchMainDistributorName').val('');
		return false;
	}
	
	var disID = $('#DispatchMainDistributorID').val();
	$.mobile.loading( 'show');
			$.ajax({
				
				url: "common/GetDistributorInfoJson",
				data: {
					DistributorID: disID
				},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.exists == "true"){
						$('#DispatchMainDistributorName').val(json.DistributorName);
						$('#DispatchMainDistributorID').val(disID);
						$.mobile.loading( 'hide');
						
					}else{
						$('#DispatchMainDistributorID').val("");
						$('#DispatchMainDistributorName').val("");
						$.mobile.loading( 'hide');
					}
					
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$.mobile.loading( 'hide');
				}
			});
}

function isInteger (o) {
	  return ! isNaN (o-0) && o != null ;
}

function GenerateDispatchSales()
{
	document.getElementById("DispatchGenerrateForm").submit();
}

function GenerateSunnary(id)
{

	
		var fdata = $("#DispatchGenerateSalesForm" ).serialize();

		$.mobile.loading( 'show');
		
		
		/*
		$.get('DispatchSalesSummary.jsp?'+data, function(data) {   			
			  $("#DispatchSalesSummary").html(data);		 
			  $('#DispatchSalesSummary').trigger("create");
			  $.mobile.loading( 'hide');
		})
		.fail(function() {			
			alert("Server could not be reached.");
			$.mobile.loading( 'hide');
		  }) ;
	*/
		
		$.ajax({
		    url: "DispatchSalesSummary.jsp",
		    data: fdata,
		    type: "POST",
		    success: function( hdata ) {
				  $("#DispatchSalesSummary").html(hdata);		 
				  $('#DispatchSalesSummary').trigger("create");
				  $.mobile.loading( 'hide');		    	
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	$.mobile.loading( 'hide');
		    }
		});		
	
}

function GetDriverNameByVehicleID()
{
	//alert($("#DispatchVehicleSelect").val());
	var VehiID = $("#DispatchVehicleSelect").val();
	$.mobile.loading( 'show');
	$.ajax({
		
		url: "common/GetDistributorVehicleInfoJson",
		data: {
			DistributorVehicleID: VehiID
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
			$("#DistributorDriverName").val(json.DistributorDriverName);
			$.mobile.loading( 'hide');
			}
			else
			{
				$("#DistributorDriverName").val("");
				$.mobile.loading( 'hide');
			}
			
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
			$.mobile.loading( 'hide');
		}
	});
}

function ShowVehicleType()
{
	var IDLabel = $("#DispatchVehicleSelect").val().split(",");
	$("#VehicleType").val(IDLabel[1]);// setting the vehicle type
}

function DispatchSalesSubmit()
{
	//alert();
	var IsValid = false;
	var PackageArray = new Array();
	var SalesSummaryArray = new Array();
	var AnySalesSelected = false;
	var ByVehicleSpotSelling = false;
	SalesSummaryArray = document.getElementsByName("DispatchSalesSelect");
	if($('#DispatchVehicleType').val() == "" ){		
		document.getElementById('DispatchVehicleType').focus();  
		IsValid = true;
	}
	if($("#DispatchVehicleType").val()=="1")//only check for vehicle - in case of by hand no need to validate
	{
//		/alert("By Vechicle ");
		if($('#DispatchVehicleSelect').val() == "" ){		
			document.getElementById('DispatchVehicleSelect').focus();  
			IsValid = true;
		}
		if($('#DistributorDriverName').val() == "" ){		
			document.getElementById('DistributorDriverName').focus();  
			IsValid = true;
		}
	}
	for(var i=0;i<SalesSummaryArray.length;i++)
	{
		if(SalesSummaryArray[i].value=="1")
		{
			AnySalesSelected = true;
			break;
		}
	}
	if($("#SpotSellingCheckBx").is(":checked")){ //if spot selling checkbox is checked then no need to check the invoices list		
		if($("#DispatchVehicleType").val()=="1"){ //only by vehicle is allowed
			AnySalesSelected = true; //to bypass the check
			$("#SpotSellingCheckBxHidden").val("1"); //setting valeu to 1 for spot selling - by default it is 0
			ByVehicleSpotSelling = true;
		}else{
			document.getElementById('DispatchVehicleType').focus();
			ByVehicleSpotSelling = false;			
			//alert();
		}
		
		
	}else{
		ByVehicleSpotSelling = true;
	}
	if(IsValid==false && AnySalesSelected && ByVehicleSpotSelling)
	{		
		$("#LiftingReportGenerateButtonTD").hide();
		$.mobile.loading( 'show');
		$.ajax({
		    url: "distributor/DispatchSalesExecute",
		    
		    data: $("#DispatchGenerateSalesForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.mobile.loading( 'hide');
		    		location = "Dispatch.jsp";
		    		
		    	}else{
					alert(json.error);
					$.mobile.loading( 'hide');
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	$.mobile.loading( 'hide');
		    }
		});
	}
	
}

function DispatchDeleteubmit()
{
	//alert();
	var IsValid = false;
	var PackageArray = new Array();
	var SalesSummaryArray = new Array();
	var AnySalesSelected = false;
	SalesSummaryArray = document.getElementsByName("DispatchSalesSelect");
	if($('#DispatchVehicleType').val() == "" ){		
		document.getElementById('DispatchVehicleType').focus();  
		IsValid = true;
	}
	if($("#DispatchVehicleType")==1)//only check for vehicle - in case of by hand no need to validate
	{
		if($('#DispatchVehicleSelect').val() == "" ){		
			document.getElementById('DispatchVehicleSelect').focus();  
			IsValid = true;
		}
		if($('#DistributorDriverName').val() == "" ){		
			document.getElementById('DistributorDriverName').focus();  
			IsValid = true;
		}
	}
	for(var i=0;i<SalesSummaryArray.length;i++)
	{
		if(SalesSummaryArray[i].value=="1")
		{
			AnySalesSelected = true;
			break;
		}
	}	
	if(AnySalesSelected)
	{		
		if (confirm("Are you sure you want to delete selected invoice(s)?")){
		$("#LiftingReportGenerateButtonTD").hide();
		$.mobile.loading( 'show');
		$.ajax({
		    url: "inventory/DispatchSalesDeleteExecute",
		    
		    data: $("#DispatchGenerateSalesForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.mobile.loading( 'hide');
		    		location = "Dispatch.jsp";
		    		
		    	}else{
					alert(json.error);
					$.mobile.loading( 'hide');
		    		//alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    	$.mobile.loading( 'hide');
		    }
		});
		}
	}
	
}

function DispatchSalesUpdate(){
	
	if($('#DispatchVehicleType').val() == ""){
		alert('Please select Dispatch Type');
		document.getElementById("DispatchVehicleType").focus();
		return false;
	}
	
	if($('#DispatchVehicleSelect').val() == ""){
		alert('Please select Vehicle');
		document.getElementById("DispatchVehicleSelect").focus();
		return false;
	}
	
	if($('#DistributorDriverName').val() == ""){
		alert('Please select Driver');
		document.getElementById("DistributorDriverName").focus();
		return false;
	}
	
	$.mobile.loading( 'show');
	$.ajax({
	    url: "distributor/DispatchSalesExecuteUpdate",
	    
	    data: $("#DispatchGenerateSalesForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	$.mobile.loading( 'hide');
	    	if (json.success == "true"){
	    		location = "Dispatch.jsp";
	    	}else{
				alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.loading( 'hide');
	    }
	});
}

function DisableByHand()
{
	if($("#DispatchVehicleType").val()=="2") //by hand then disable other two drop downs and reset them
	{
		//alert();
		$("#DispatchVehicleSelectTD").addClass("ui-disabled");
		$("#DistributorDriverNameTD").addClass("ui-disabled");
		$('#DispatchVehicleSelectTD').trigger("create");
		$("#VehicleType").val("");
		$("#DispatchVehicleSelect").val("");
		$("#DispatchVehicleSelect").selectmenu('refresh');
		$("#DistributorDriverName").val("");
		$("#DistributorDriverName").selectmenu('refresh');
	}
	else
	{
		$("#DispatchVehicleSelectTD").removeClass("ui-disabled");
		$("#DistributorDriverNameTD").removeClass("ui-disabled");
		$('#DispatchVehicleSelectTD').trigger("create");
	}
}

function showSearchContent(){
	
	$("#SearchContent").focus();
	
	//document.getElementById("DispatchSaleFromDate").disabled = "disabled";
	//document.getElementById("DispatchSaleToDate").disabled = "disabled";
	
	$.get('DispatchSaleSearch.jsp?FromDate='+$('#DispatchSaleFromDate').val()+'&ToDate='+$('#DispatchSaleToDate').val(), function(data) {		
		  $("#SearchContent").html(data);
		  $("#SearchContent").trigger('create');		  
	})
	.fail(function() {
			$.mobile.loading( 'hide');
		  }) ;
	
	return false;

}

function DeleteDispatchSales()
{
	$.mobile.loading( 'show');
	$.ajax({
	    url: "distributor/DispatchSalesExecute",
	    
	    data: $("#DispatchGenerateSalesForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		$.mobile.loading( 'hide');
	    		location = "Dispatch.jsp";
	    	}else{
				alert(json.error);
				$.mobile.loading( 'hide');
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	$.mobile.loading( 'hide');
	    	
	    }
	});
}

function SpotSellingCheckBoxClick(){
	if($("#SpotSellingCheckBx").is(":checked")){
		$("#DispatchInvoicesList").addClass("ui-disabled");
	}else{
		$("#DispatchInvoicesList").removeClass("ui-disabled");
	}
	
	
}