$( document ).delegate("#MRDOutletsFormPage", "pageinit", function() {
	$('#DeleteButton').addClass("ui-disabled");
});

function getOutletInfoJson(){

	if(isInteger($('#OutletID').val()) == false ){
		$('#OutletID').val('');
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "mrd/GetOutletInfoJSON",
		data: {
			OutletID: $('#OutletID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				$('#OutletName').val(json.OutletName);
				$('#BusinessType').val(json.BusinessTypeLabel);
				$('#Address').val(json.Address);
				$('#Region').val(json.Region);
				$('#Lat').val(json.Lat);
				$('#Lng').val(json.Lng);
				$('#GPSAccuracy').val(json.GpsAccuracy);
				$('#Comments').val(json.Comments);
				$('#Pepsi').val(json.Pepsi);
				$('#Coke').val(json.Coke);
				$('#Gourmet').val(json.Gourmet);
				$('#ContactPerson').val(json.ContactPerson);
				$('#Phone').val(json.Phone);
				$('#UserOutletID').val(json.UserOutletID);
				
				$('#DeleteButton').removeClass("ui-disabled");
			}else{
				//alert('else');
				$('#DeleteButton').addClass("ui-disabled");
			}			
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
	
}

function FormSubmit(){
	
	if( $('#OutletID').val() == "" ){
		alert("Please enter OutletID");
		$('#OutletID').focus();
		return false;
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "mrd/MRDOutletsExecute",
		data: $('#MRDOutletsForm').serialize(),
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				window.location='MRDOutlets.jsp';
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

function DeleteSubmit(){
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		
		url: "mrd/MRDOutletsDeleteOutlet",
		data: {
			OutletID: $('#OutletID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			$.mobile.hidePageLoadingMsg();
			if(json.success == "true"){
				window.location='MRDOutlets.jsp';
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

