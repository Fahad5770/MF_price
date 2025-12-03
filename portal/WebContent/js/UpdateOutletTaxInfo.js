
	$( document ).delegate("#OutletAdministration", "pageinit", function() {
		
		$("#SamplingDeactivationSaveButton").on ("click", function( event, ui ) {
			$('#SamplingDeactivationForm').submit();
		});
		
		$('#DistributorID2').on('dblclick', function(e, data){	
			//alert('abc');
			$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
				//alert("hELLO");
				lookupDistributorInit();
			} );
			$('#LookupDistributorSearch').popup("open");
		});

		
		var pageid = "#OutletAdministration";
		
		$(pageid + ' #OutletID').change(function() {
			$.ajax({
			    url: "outlet/GetOutletInfoJson",
			    data: {
			        OutletID: this.value,
			        FeatureID: FeatureIDValue
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.success == "true"){
			    		$('#ContactDivContent').html("");
			    		//$('#ContactDivContent').append(ContactDivContent).trigger('create')
			    		
			    		$(pageid + ' #OutletName').val(json.OutletName);
			    		
			    		$(pageid + ' #address').val(json.Address);
			    		$(pageid + ' #region').val(json.RegionName);
			    		$(pageid + ' #ChannelID').val(json.ChannelID).change();			    		
			    		$(pageid + ' #NfcTagID').val(json.NFCTagID);
			    		$(pageid + ' #SAPCustomerID').val(json.SAPCustomerID);	
			    		$(pageid + ' #SegmentID').val(json.SegmentID).change();
			    		$(pageid + ' #AgreedDailyAverageSales').val(json.AgreedDailyAvgSales);
			    		$(pageid + ' #VPOClassifications').val(json.VPOClassificationsID).change();
			    		if(json.accountnumberbankalfalah != "null"){
			    			$(pageid + ' #Anumber').val(json.accountnumberbankalfalah);
			    		}
			    		//$(pageid + ' #Anumber').val(json.accountnumberbankalfalah).change();
			    		$(pageid + ' #discountdisbursement').val(json.DiscountDisbursementID).change();
			    		
			    		
			    		
			    		$(pageid + ' #PrimaryDistributorName').val(json.PrimaryDistributorID+' - '+json.PrimaryDistributorName);
			    		
			    		if(json.SMSNumber != "0"){
			    			$(pageid + ' #smsnumber').val(json.SMSNumber);
			    		}else{
			    			//$("#SMSDID").css("display","none");
			    		}
	
						///Added by awais on 24/02/2021
						
			    		if(json.isFiler=="1"){
			    			$('#isFilerID').prop('checked', true).checkboxradio('refresh');
			    		}
	
			    		///End patch

			    		var lat = json.Lat;
			    		var lng = json.Lng;
			    		
			    		if (parseFloat(lat) > 1){
			    			$(pageid + ' #OutletMap').attr("src","http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C"+lat+","+lng+"&sensor=false");
			    		}else{
			    			$(pageid + ' #OutletMap').attr("src","images/GPSUnavailable.png");
			    		}
			    		
			    		
			    		var ContactDivContent = '<table class="table-stripe GridWithoutBorder" style="font-size: 10pt; width:100%; margin-top:10px">';
			    		ContactDivContent += '<thead><tr class="ui-bar-d">';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">Contact Name</th>';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">Number</th>';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">NIC</th>';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">Relation</th>';
			    		ContactDivContent += '</tr></thead>';
			    		
						
			    		var length = json.ContactRows.length;
			    		for(var i = 0; i < length; i++){
			    			//alert("ContactName = "+json.ContactRows[i].ContactName);
			    			ContactDivContent += '<tr>';
				    			ContactDivContent += '<td style="width: 40%" nowrap>'+json.ContactRows[i].ContactName+'</td>';
				    			ContactDivContent += '<td style="width: 20%" nowrap>'+json.ContactRows[i].ContactNumber+'</td>';
				    			ContactDivContent += '<td style="width: 20%" nowrap>'+json.ContactRows[i].ContactNIC+'</td>';
				    			ContactDivContent += '<td style="width: 20%" nowrap>'+json.ContactRows[i].ContactRelation+'</td>';
			    			ContactDivContent += '</tr>';
			    		}
			    		
			    		ContactDivContent += '</table>';
			    		$('#ContactDivContent').append(ContactDivContent).trigger('create')
			    		
			    		/*var length = json.DistributorRows.length;
			    		for(var i = 0; i < length; i++){
			    			$("#populateGroupDistributorDataUl").append("<li data-mini='true' data-icon='delete' id='DistributorLI_"+json.DistributorRows[i].DistributorID+"' onclick='RemoveList(this.id)'><input type='hidden' name='OutletAdministrationDistributorID' value='"+json.DistributorRows[i].DistributorID+"'/><a href='#'>"+json.DistributorRows[i].DistributorID+" - "+json.DistributorRows[i].DistributorName+"</a></li>");
			    		}
			    		$("#populateGroupDistributorDataUl").listview("refresh");
			    		
			    		*/
			    		
			    		var PJPHtml = "<table class='table-stripe GridWithoutBorder' style='font-size: 10pt; width:100%; margin-top:10px'>" +
			    						"<thead>" +
				    						"<tr class='ui-bar-d'>" +
				    							"<th>PJP</th>" +
				    							"<th>Distributor</th>" +
				    							"<th>Product Group</th>" +
				    						"</tr>" +
			    						"</thead>";
			    		var length = json.PJPRows.length;
			    		for(var i = 0; i < length; i++){
			    			PJPHtml += "<tr>" +
				    						"<td><a href='#' onclick='BeatPlanRedirect("+json.PJPRows[i].PJPID+", "+json.PJPRows[i].DistributorID+")'>"+json.PJPRows[i].PJPID+" - "+json.PJPRows[i].PJPName+"</a></td>" +
				    						"<td>"+json.PJPRows[i].DistributorID+" - "+json.PJPRows[i].DistributorName+"</td>" +
				    						"<td><a href='#' onclick='DistributorAdministrationRedirect("+json.PJPRows[i].DistributorID+")'>"+json.PJPRows[i].ProductGroupID+" - "+json.PJPRows[i].ProductGroupName+"</a></td>" +
			    						"</tr>";
			    			
			    		}
			    		PJPHtml += "</table>";
			    		$("#PJPContent").html(PJPHtml);
			    		
			    		$("#OutletAdministrationSaveButton").removeClass("ui-disabled");
			    		
			    	}else{
			    		
			    		$(pageid + ' #address').val("");
			    		$(pageid + ' #region').val("");
			    		
			    		$(pageid + ' #OutletName').val("Outlet doesn't exist.");
			    		$(pageid + ' #OutletID').focus();
			    		$(pageid + ' #OutletID').select();
			    		
			    		$("#OutletAdministrationSaveButton").addClass("ui-disabled");
			    	}
			    },
			    error: function( xhr, status ) {
			        alert( "Sorry, the server did not respond." );
			    }
			});
			
		});	

	});
	
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
	
	function OutletAdministrationAddDistributor(){
		
		var length = document.getElementsByName('OutletAdministrationDistributorID').length;
		for(var i = 0; i < length; i++){
			if(document.getElementsByName('OutletAdministrationDistributorID')[i].value == $('#DistributorID2').val()){
				return false;	// avoid duplication
			}
		}
		
		if( $('#DistributorID2').val() != '' && $('#DistributorName2').val() != '' ){
			$("#populateGroupDistributorDataUl").append("<li data-mini='true' data-icon='delete' id='DistributorLI_"+$('#DistributorID2').val()+"' onclick='RemoveList(this.id)'><input type='hidden' name='OutletAdministrationDistributorID' value='"+$('#DistributorID2').val()+"'/><a href='#'>"+$('#DistributorID2').val()+" - "+$('#DistributorName2').val()+"</a></li>");    	
	    	$("#populateGroupDistributorDataUl").listview("refresh");
	    	
	    	$('#DistributorID2').val('');
	    	$('#DistributorName2').val('');
	    	
		}
	}
	
	function RemoveList(ID)
    {
    	$("#"+ID).remove();
    	$("#populateGroupDistributorDataUl").listview("refresh");
    	
    }
	
	function DistributorSearchCallBackOutletAdministration(SAPCode, DistributorName){
		$('#DistributorID2').val(SAPCode);
		$('#DistributorName2').val(DistributorName);
	}
	
	function OutletAdministrationSubmit(){
		var SMSNumber = $("#smsnumber").val();
		/*if($('#discountdisbursement').val()==0){
			alert("Please Select Discount Disbursement");
			$('#discountdisbursement').focus();
			return false;
		}*/
		
		//Added by awais on 24/02/2021
		if($("#isFilerID").is(":checked")){
			
			$("#isFilerID").val("1");
		}
		//end patch
		if(isInteger(SMSNumber) && (SMSNumber.length==12 || SMSNumber.length==0)){
			$.mobile.showPageLoadingMsg();	
			$.ajax({
	    		
	    		url: "outlet/OutletAdministrationExecute",
	    		data: $("#OutletAdministrationForm" ).serialize(),
	    		type:"POST",
	    		dataType:"json",
	    		success:function(json){
	    			$.mobile.hidePageLoadingMsg();
	    			if(json.success == "true"){
	    				window.location='OutletAdministration.jsp';
	    			}else{
	    				alert(json.error);
	    			}
	    		},
	    		error:function(xhr, status){
	    			alert("Server could not be reached.");
	    		}
	    		
	    	});
		}else{
			alert("Please enter valid 12 digit number");
		}
		
		
	}
	
	function isInteger (o) {
		  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
	}

	function BeatPlanRedirect(BeatPlanID, DistributorID){
		//alert('BeatPlanID = '+BeatPlanID);
		$('#BeatPlanID').val(BeatPlanID);
		$('#DistributorID').val(DistributorID);
		
		$('#PJPForm').submit();
		
	}
	
	function DistributorAdministrationRedirect(DistributorID){
		$('#DistributorFormDistributorID').val(DistributorID);
		
		$('#DistributorForm').submit();
		
	}
	
	function populateAvgSale(val){
		
		if(val == "5"){
			$('#AgreedDailyAverageSales').removeClass("ui-disabled");			
		}else{
			$('#AgreedDailyAverageSales').addClass("ui-disabled");
			$('#AgreedDailyAverageSales').val('');
		}
		
	}
	
	function populateCaptiveBySegment(val){
		/*
		if(val == "5"){ // captive
			if( $('#ChannelID').val() != "8" ){ //marriage hall
				$('#ChannelID').val("9").change(); // captive
			}			
		}else{
			//$('#ChannelID').val("").change();
		}
		*/
	}
	
	function populateCaptiveByChannel(val){
		/*
		if(val == "9" || val == "8"){
			$('#SegmentID').val("5").change();
		}else{
			//$('#SegmentID').val("").change();
		}
		*/
	}
	
	$( document ).delegate("#OutletTaxinfo", "pageinit", function() {
		
		$("#SamplingDeactivationSaveButton").on ("click", function( event, ui ) {
			$('#SamplingDeactivationForm').submit();
		});
		
		$('#DistributorID2').on('dblclick', function(e, data){	
			//alert('abc');
			$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
				//alert("hELLO");
				lookupDistributorInit();
			} );
			$('#LookupDistributorSearch').popup("open");
		});

		
		var pageid = "#OutletTaxinfo";
		
		$(pageid + ' #OutletID').change(function() {
			$.ajax({
			    url: "outlet/GetOutletInfoJsonforTax",
			    data: {
			        OutletID: this.value,
			        FeatureID: FeatureIDValue
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	if (json.success == "true"){
			    		$('#ContactDivContent').html("");
			    		//$('#ContactDivContent').append(ContactDivContent).trigger('create')
			    		
			    		$(pageid + ' #OutletName').val(json.OutletName);
			    		
			    		$(pageid + ' #address').val(json.Address);
			    		$(pageid + ' #region').val(json.RegionName);
			    		$(pageid + ' #ChannelID').val(json.ChannelID).change();			    		
			    		$(pageid + ' #NfcTagID').val(json.NFCTagID);
			    		$(pageid + ' #SAPCustomerID').val(json.SAPCustomerID);	
			    		$(pageid + ' #SegmentID').val(json.SegmentID).change();
			    		$(pageid + ' #AgreedDailyAverageSales').val(json.AgreedDailyAvgSales);
			    		$(pageid + ' #VPOClassifications').val(json.VPOClassificationsID).change();
			    		$(pageid + ' #StnID').val(json.StnID);
			    		$(pageid + ' #NtnID').val(json.NtnID);

			    		if(json.accountnumberbankalfalah != "null"){
			    			$(pageid + ' #Anumber').val(json.accountnumberbankalfalah);
			    		}
			    		//$(pageid + ' #Anumber').val(json.accountnumberbankalfalah).change();
			    		$(pageid + ' #discountdisbursement').val(json.DiscountDisbursementID).change();
			    		
			    		
			    		
			    		$(pageid + ' #PrimaryDistributorName').val(json.PrimaryDistributorID+' - '+json.PrimaryDistributorName);
			    		
			    		if(json.SMSNumber != "0"){
			    			$(pageid + ' #smsnumber').val(json.SMSNumber);
			    		}else{
			    			//$("#SMSDID").css("display","none");
			    		}
	
						///Added by awais on 24/02/2021
						
			    		if(json.isFiler=="1"){
			    			$('#isFilerID').prop('checked', true).checkboxradio('refresh');
			    		}
						if(json.isRegister=="1"){
			    			$('#isRegisterID').prop('checked', true).checkboxradio('refresh');
			    		}
			    		///End patch

			    		var lat = json.Lat;
			    		var lng = json.Lng;
			    		
			    		if (parseFloat(lat) > 1){
			    			$(pageid + ' #OutletMap').attr("src","http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=14&size=285x220&markers=color:blue%7Clabel:S%7C"+lat+","+lng+"&sensor=false");
			    		}else{
			    			$(pageid + ' #OutletMap').attr("src","images/GPSUnavailable.png");
			    		}
			    		
			    		
			    		var ContactDivContent = '<table class="table-stripe GridWithoutBorder" style="font-size: 10pt; width:100%; margin-top:10px">';
			    		ContactDivContent += '<thead><tr class="ui-bar-d">';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">Contact Name</th>';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">Number</th>';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">NIC</th>';
				    		ContactDivContent += '<th data-priority="1" style="width:10%">Relation</th>';
			    		ContactDivContent += '</tr></thead>';
			    		
						
			    		var length = json.ContactRows.length;
			    		for(var i = 0; i < length; i++){
			    			//alert("ContactName = "+json.ContactRows[i].ContactName);
			    			ContactDivContent += '<tr>';
				    			ContactDivContent += '<td style="width: 40%" nowrap>'+json.ContactRows[i].ContactName+'</td>';
				    			ContactDivContent += '<td style="width: 20%" nowrap>'+json.ContactRows[i].ContactNumber+'</td>';
				    			ContactDivContent += '<td style="width: 20%" nowrap>'+json.ContactRows[i].ContactNIC+'</td>';
				    			ContactDivContent += '<td style="width: 20%" nowrap>'+json.ContactRows[i].ContactRelation+'</td>';
			    			ContactDivContent += '</tr>';
			    		}
			    		
			    		ContactDivContent += '</table>';
			    		$('#ContactDivContent').append(ContactDivContent).trigger('create')
			    		
			    		/*var length = json.DistributorRows.length;
			    		for(var i = 0; i < length; i++){
			    			$("#populateGroupDistributorDataUl").append("<li data-mini='true' data-icon='delete' id='DistributorLI_"+json.DistributorRows[i].DistributorID+"' onclick='RemoveList(this.id)'><input type='hidden' name='OutletAdministrationDistributorID' value='"+json.DistributorRows[i].DistributorID+"'/><a href='#'>"+json.DistributorRows[i].DistributorID+" - "+json.DistributorRows[i].DistributorName+"</a></li>");
			    		}
			    		$("#populateGroupDistributorDataUl").listview("refresh");
			    		
			    		*/
			    		
			    		var PJPHtml = "<table class='table-stripe GridWithoutBorder' style='font-size: 10pt; width:100%; margin-top:10px'>" +
			    						"<thead>" +
				    						"<tr class='ui-bar-d'>" +
				    							"<th>PJP</th>" +
				    							"<th>Distributor</th>" +
				    							"<th>Product Group</th>" +
				    						"</tr>" +
			    						"</thead>";
			    		var length = json.PJPRows.length;
			    		for(var i = 0; i < length; i++){
			    			PJPHtml += "<tr>" +
				    						"<td><a href='#' onclick='BeatPlanRedirect("+json.PJPRows[i].PJPID+", "+json.PJPRows[i].DistributorID+")'>"+json.PJPRows[i].PJPID+" - "+json.PJPRows[i].PJPName+"</a></td>" +
				    						"<td>"+json.PJPRows[i].DistributorID+" - "+json.PJPRows[i].DistributorName+"</td>" +
				    						"<td><a href='#' onclick='DistributorAdministrationRedirect("+json.PJPRows[i].DistributorID+")'>"+json.PJPRows[i].ProductGroupID+" - "+json.PJPRows[i].ProductGroupName+"</a></td>" +
			    						"</tr>";
			    			
			    		}
			    		PJPHtml += "</table>";
			    		$("#PJPContent").html(PJPHtml);
			    		
			    		$("#OutletAdministrationSaveButton").removeClass("ui-disabled");
			    		
			    	}else{
			    		
			    		$(pageid + ' #address').val("");
			    		$(pageid + ' #region').val("");
			    		
			    		$(pageid + ' #OutletName').val("Outlet doesn't exist.");
			    		$(pageid + ' #OutletID').focus();
			    		$(pageid + ' #OutletID').select();
			    		
			    		$("#OutletAdministrationSaveButton").addClass("ui-disabled");
			    	}
			    },
			    error: function( xhr, status ) {
			        alert( "Sorry, the server did not respond." );
			    }
			});
			
		});	

	});
		function OutletTaxSubmit(){
		var SMSNumber = $("#smsnumber").val();
		/*if($('#discountdisbursement').val()==0){
			alert("Please Select Discount Disbursement");
			$('#discountdisbursement').focus();
			return false;
		}*/
		
		//Added by awais on 24/02/2021
		if($("#isFilerID").is(":checked")){
			
			$("#isFilerID").val("1");
		}
		if($("#isRegisterID").is(":checked")){
			
			$("#isRegisterID").val("1");
		}
		//end patch
		//if(isInteger(SMSNumber) && (SMSNumber.length==12 || SMSNumber.length==0)){
			$.mobile.showPageLoadingMsg();	
			$.ajax({
	    		
	    		url: "outlet/UpdateOutletTaxInfoExecute",
	    		data: $("#OutletAdministrationForm" ).serialize(),
	    		type:"POST",
	    		dataType:"json",
	    		success:function(json){
	    			$.mobile.hidePageLoadingMsg();
	    			if(json.success == "true"){
	    				window.location='UpdateOutletTaxInfo.jsp';
	    			}else{
	    				alert(json.error);
	    			}
	    		},
	    		error:function(xhr, status){
	    			alert("Server could not be reached.");
	    		}
	    		
	    	});
	/*	}else{
			alert("Please enter valid 12 digit number");
		}*/
		
		
	}