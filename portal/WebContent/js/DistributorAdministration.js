$(document).delegate("#DistributorAdministration", "pageinit", function() {

	if (DistributorIDGlobal != 0) {
		$("#DistributorID").val(DistributorIDGlobal);
		LoadDistributor();
	}

	$('#DeskSaleOutletID').on('dblclick', function(e, data) {
		$("#LookupOutletSearch").on("popupbeforeposition", function(event, ui) {
			lookupOutletInit();
		});
		$('#LookupOutletSearch').popup("open");

	});

});

function LoadDistributor() {
	DistributorID = $("#DistributorID").val();

	$.ajax({
		url: "distributor/GetDistributorByIDJson",
		data: {
			DistributorIDD: DistributorID,
			FeatureIDD: $("#FeatureID").val()
		},
		type: "POST",
		dataType: "json",
		success: function(json) {
			if (json.success == "true") {
				$("#DistributorName").val(json.DistributorName);
				$("#DistributorCity").val(json.DistributorCity);
				$("#DistributorAddress").val(json.DistributorAddress);

				//	$("#DistributorRegion").val(json.DistributorRegion);
				$("#DistributorRegion").val(json.DistributorRegion);
				$('#DistributorRegion').selectmenu('refresh', true);

				$("#DistributorRoute").val(json.DistributorRoute);
				$("#DistributorContactNo").val(json.DistributorContact);
				$("#SelectProductGroupID").val(json.DistributorProductGroupID);
				$('#SelectProductGroupID').selectmenu('refresh', true);

				$("#SelectDistributorTypeID").val(json.DistributorTypeID);
				$('#SelectDistributorTypeID').selectmenu('refresh', true);

				$("#DistributorMonthCycle").val(json.DistributorMonthCycle);
				$("#DeskSaleOutletID").val(json.DistributorDeskSaleOutletID);
				$("#DeskSaleOutletName").val(json.DistributorDeskSaleOutletName);




			} else {
				alert("Invalid Distributor ID");
			}
		},
		error: function(xhr, status) {
			alert("Server could not be reached.");
		}
	});
}

function DistributorAdministrationSubmit() {
	DistributorID = $("#DistributorID").val();
	MonthCycle = $("#DistributorMonthCycle").val();

	if (DistributorID == "") {
		alert("Please Enter Distributor ID");
		document.getElementById('DistributorID').focus();
		return false;
	}

	if (isInteger(DistributorID) == false) {
		alert("Please Enter Valid Distributor ID");
		document.getElementById('DistributorID').focus();
		return false;
	}
	if (isInteger(MonthCycle) == false) {
		alert("Please Enter Valid Month Cycle");
		document.getElementById('DistributorMonthCycle').focus();
		return false;
	}

	if (MonthCycle < 1 || MonthCycle > 28) {
		alert("Please enter Month Cycle between 1 to 28");
		document.getElementById('DistributorMonthCycle').focus();
		return false;
	}


	$.ajax({
		url: "distributor/DistributorAdministrationExecute",
		data: $("#DistributorAdministrationForm").serialize(),
		type: "POST",
		dataType: "json",
		success: function(json) {
			if (json.success == "true") {
				location = "DistributorAdministration.jsp";
			} else {
				//alert('Server could not be reached.');
			}
		},
		error: function(xhr, status) {
			alert("Server could not be reached.");
		}
	});


}

function getOutletName() {
	//alert();
	if (isInteger($('#DeskSaleOutletID').val()) == false) {
		$('#DeskSaleOutletID').val('');
		return false;
	}

	$.mobile.showPageLoadingMsg();

	$.ajax({

		url: "common/GetOutletBySAPCodeJSON",
		data: {
			SAPCode: $('#DeskSaleOutletID').val(),
			FeatureID: 75
		},
		type: "POST",
		dataType: "json",
		success: function(json) {



			if (json.exists == "true") {
				$('#DeskSaleOutletName').val(json.OutletName);

				$.mobile.hidePageLoadingMsg();
			} else {
				$.mobile.hidePageLoadingMsg();
				$('#DeskSaleOutletName').val('Outlet not found');
			}
		},
		error: function(xhr, status) {
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}

	});

}

function OutletSearchCallBackDeskSale(SAPCode, EmployeeName) {
	$('#DeskSaleOutletID').val(SAPCode);
	$('#DeskSaleOutletName').val(EmployeeName);
}

function isInteger(o) {

	return !isNaN(o - 0) && o != null && o.indexOf('.') == -1;
} 
