$( document ).delegate("#MRDGenerateReportPage", "pageinit", function() {
	$('#DistributorID').on('dblclick', function(e, data){
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open");
	});
});

function DistributorSearchCallBack(SAPCode, DistributorName){
	$('#DistributorID').val(SAPCode);
	$('#DistributorName').val(DistributorName);
}

function getDistributorName(){

	if(isInteger($('#DistributorID').val()) == false ){
		$('#DistributorName').val('');
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: $('#DistributorID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.exists == "true"){
				$('#DistributorName').val(json.DistributorName);
			}else{
				$('#DistributorID').val("");
				$('#DistributorName').val("");
			}			
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
	
}

function FormSubmit(){
	
	if( $('#DistributorID').val() == "" ){
		alert("Please select Distributor");
		$('#DistributorID').focus();
		return false;
	}
	
	if( $('#Date').val() == "" ){
		alert("Please enter Date");
		$('#Date').focus();
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "mrd/MRDGenerateReport",
		data: $('#MRDGenerateReportForm').serialize(),
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				alert('success');
				
				$('#OutcomeID').removeClass("ui-disabled");
				$('#Observations').removeClass("ui-disabled");
				$('#UpdateButton').button('enable');
				
				$('#SurveySummaryID').val(json.SurveySummaryID);				
				
			}else{
				alert(json.error);
			}			
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
}

function FormUpdate(){
	
	if( $('#OutcomeID').val() == "" ){
		alert("Please select Outcome");
		$('#OutcomeID').focus();
		return false;
	}
	
	if( $('#Observations').val() == "" ){
		alert("Please enter Observations");
		$('#Observations').focus();
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "mrd/MRDGenerateReportUpdateObservations",
		data: $('#MRDGenerateReportForm').serialize(),
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				alert('success');
				
			}else{
				alert(json.error);
			}			
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
	
}
