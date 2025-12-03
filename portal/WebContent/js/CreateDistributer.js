function addCity() {
	$("#LookupUserSearch").on("popupbeforeposition", function(event, ui) {
		lookupUserInit();
	});
	$('#LookupUserSearch').popup("open");

}

function CreateDistributor() {
	var pattern = new RegExp(/^[0-9]+$/);
	var isValidflag = true;
	var errorMsg = "";

	if ($('#DistributorID').val() == "") {
		document.getElementById('DistributorID').focus();
		errorMsg = "Please Add Distributor ID";
		isValidflag = false;
	} else if (!pattern.test($('#DistributorID').val())) {
		document.getElementById('DistributorID').focus();
		errorMsg = "Please Add Distributor ID in numbers";
		isValidflag = false;
	} else if ($('#DistributorName').val() == "") {
		document.getElementById('DistributorName').focus();
		errorMsg = "Please Add Distributor Name";
		isValidflag = false;
	} else if ($('#Address').val() == "") {
		document.getElementById('Address').focus();
		errorMsg = "Please Add Distributor Address";
		isValidflag = false;
	} else if ($('#region').val() == "0") {
		document.getElementById('region').focus();
		errorMsg = "Please Select Region";
		isValidflag = false;
	} else if ($('#city').val() == "0") {
		document.getElementById('city').focus();
		errorMsg = "Please Select City";
		isValidflag = false;
	}


	if (!isValidflag) {
		alert(errorMsg);
	} else {

		$.ajax({
			url: "distributor/DistributorCreateExecute",

			data: {
				DistributorID: $('#DistributorID').val(),
				DistributorName: $('#DistributorName').val(),
				Address: $('#Address').val(),
				region: $('#region').val(),
				city: $('#city').val(),
				cityLabel: $("#city option:selected").text()

			},
			type: "POST",
			dataType: "json",
			success: function(json) {
				if (json.success == "true") {
					alert("Distributor created.");
					location = "CreateDistributor.jsp";
				} else {
					alert(json.error);
					//alert("Server could not be reached.");
				}
			},
			error: function(xhr, status) {
				alert("Server could not be reached.");
			}
		});
	}
}


