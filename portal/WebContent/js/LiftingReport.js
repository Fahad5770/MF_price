
$( document ).delegate("#LiftingReportMain", "pageinit", function() {

	$("#LiftingReportMainDistributor").parent().css("width","100%");
	$("#LiftingReportMainDistributorName").parent().css("width","100%");
	$("#LiftingReportUser").parent().css("width","100%");
	$("#LiftingReportUserName").parent().css("width","100%");
	$("#BrandSearchTD").children().css("width","100%");
	$("#PackageSearchTD").children().css("width","100%");
	
	//if this user has only one distributor then it must be selected
	
	if(DistributorIds.length==1)
	{
		$('#LiftingReportMainDistributor').val(DistributorIds[0]);
		getDistributorName();
	}
	
	
	$('#LiftingReportMainDistributor').on('dblclick', function(e, data){
		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupDistributorInit();
		} );
		$('#LookupDistributorSearch').popup("open");
	});
	
	$("#LiftingReportGenerateButton").on ("click", function( event, ui ) {
		//var params = $('#LiftingReportGenerrateForm').serialize();
		
		//window.open("SamplingDashboardXLS.jsp?"+params,"_blank");
		
		//$('#LiftingReportGenerrateForm').attr("action", "LiftingReport.jsp?params="+encodeURIComponent(params));
		
		$('#LiftingReportGenerrateForm').submit();
	});
	
	$('#LiftingReportUser').on('dblclick', function(e, data){
		//alert("helo");
		$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupEmployeeInit();
		} );
		$('#LookupEmployeeSearch').popup("open");
				  
	});
	
	
	
});

function OpenDeliveryNoteVoucher(ID){
	window.location='DeliveryNote.jsp?DeliveryID='+ID;
}

function DistributorSearchCallBack(SAPCode, DistributorName){
	
	for(var i=0;i<DistributorIds.length;i++)
	{
		
		$('#LiftingReportMainDistributor').val(SAPCode);
		$('#LiftingReportMainDistributorName').val(DistributorName);
		//alert($('#LiftingReportMainDistributor').val()+"-"+DistributorIds[i]);
		
		if($('#LiftingReportMainDistributor').val() != DistributorIds[i])
		{
			$('#LiftingReportMainDistributor').val("");
			$('#LiftingReportMainDistributorName').val("");
		}
		else
		{
			break;
		}
		
	}
}

function getDistributorName(){
	
	//alert(DistributorIds.length);
	if(isInteger($('#LiftingReportMainDistributor').val()) == false ){
		$('#LiftingReportMainDistributorName').val('');
		return false;
	}
	var isFound = false;
	var disID = $('#LiftingReportMainDistributor').val();
	for(var i=0;i<DistributorIds.length;i++)
	{
		//alert(DistributorIds[i]);
		if(isFound)
		{ break; }
		//alert(DistributorIds.length);
		if(disID == DistributorIds[i])
		{ //alert(DistributorIds[i]);
			isFound = true;
			$.ajax({
				
				url: "common/GetDistributorInfoJson",
				data: {
					DistributorID: disID
				},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.exists == "true"){
						$('#LiftingReportMainDistributorName').val(json.DistributorName);
						$('#LiftingReportMainDistributor').val(disID);
						
					}else{
						$('#LiftingReportMainDistributor').val("");
						$('#LiftingReportMainDistributorName').val("");
					}
					
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
				}
				
			});
		}
		else
		{
			$('#LiftingReportMainDistributor').val("");
			$('#LiftingReportMainDistributorName').val("");
		}
	}
	
	
	
}

function EmployeeSearchCallBackLiftingReport(SAPCode, EmployeeName){
	
			$('#LiftingReportUser').val(SAPCode);
			$('#LiftingReportUserName').val(EmployeeName);
		
		
	
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