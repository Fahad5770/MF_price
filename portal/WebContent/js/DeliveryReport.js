
$( document ).delegate("#DeliveryReportMain", "pageinit", function() {
	
	$("#LiftingReportMainDistributor").parent().css("width","100%");
	$("#LiftingReportMainDistributorName").parent().css("width","100%");
	
	$("#LiftingReportUser").parent().css("width","100%");
	$("#LiftingReportUserName").parent().css("width","100%");
	
	$("#DeliveryReportGenerateButton").on ("click", function( event, ui ) {
		//var params = $('#LiftingReportGenerrateForm').serialize();
		
		//window.open("SamplingDashboardXLS.jsp?"+params,"_blank");
		
		//$('#LiftingReportGenerrateForm').attr("action", "LiftingReport.jsp?params="+encodeURIComponent(params));
		
		$('#DeliveryReportGenerrateForm').submit();
	});
	
	$('#LiftingReportUser').on('dblclick', function(e, data){		
		$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupEmployeeInit();
		} );
		$('#LookupEmployeeSearch').popup("open");
				  
	});
	
	$('#LiftingReportMainDistributor').on('dblclick', function(e, data){	
		//alert();
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			//alert("hELLO");
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open");
	});
	
	//if this user has only one distributor then it must be selected
	
	//alert('DistributorIds.length = '+DistributorIds.length);
	
	if(DistributorIds.length==1)
	{
		$('#LiftingReportMainDistributor').val(DistributorIds[0]);
		getDistributorName();
	}
	
	
});

function OpenDeliveryNoteVoucher(ID){
	//window.location='DeliveryNote.jsp?DeliveryID='+ID;
	$("#DeliveryIDHiddenID").val(ID);
	document.getElementById("DeliveryReportViewForm").submit();
	
}
function OpenDeliveryNoteVoucherPrint(ID){
	$("#DeliveryIDHiddenIDForPrint").val(ID);
	document.getElementById("DeliveryReportPrintForm").submit();
	//window.location='DeliveryNotePrintWithoutHeader.jsp?DeliveryID='+ID;
}

function EmployeeSearchCallBackDeliveryReport(SAPCode, EmployeeName){
	$('#LiftingReportUser').val(SAPCode);
	$('#LiftingReportUserName').val(EmployeeName);
}

function DistributorSearchCallBackDeliveryRpt(SAPCode, DistributorName){
	
	
	for(var i=0;i<DistributorIds.length;i++)
	{
		$('#LiftingReportMainDistributor').val(SAPCode);
		$('#LiftingReportMainDistributorName').val(DistributorName);
		
		if($('#LiftingReportMainDistributor').val() != DistributorIds[i])
		{
			$('#LiftingReportMainDistributor').val("");
			$('#LiftingReportMainDistributorName').val("");
		}
	}
}

function getDistributorName(){
	
	if(isInteger($('#LiftingReportMainDistributor').val()) == false ){
		$('#LiftingReportMainDistributorName').val('');
		return false;
	}
	
	var isMatched = false;
	
	for(var i=0;i<DistributorIds.length;i++)
	{
		if($('#LiftingReportMainDistributor').val() == DistributorIds[i])
		{
			isMatched = true;
			
			$.ajax({
				
				url: "common/GetDistributorInfoJson",
				data: {
					DistributorID: $('#LiftingReportMainDistributor').val()
				},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.exists == "true"){
						$('#LiftingReportMainDistributorName').val(json.DistributorName);
					}else{
						$('#LiftingReportMainDistributorName').val('');
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
				}
				
			});
			
		}
	}
	
	if(isMatched == false){
		$('#LiftingReportMainDistributor').val("");
		$('#LiftingReportMainDistributorName').val("");
	}
	
}

function getUserName(){
	//alert();
	if(isInteger($('#LiftingReportUser').val()) == false ){
		$('#LiftingReportUserName').val('');
		return false;
	}
	
	$.ajax({
		
		url: "common/GetUserInfoJson",
		data: {
			UserID: $('#LiftingReportUser').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#LiftingReportUserName').val(json.DistributorName);
			}else{
				$('#LiftingReportUserName').val('');
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}