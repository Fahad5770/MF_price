
var RowCount = 0;
var RowCount1 = 0;
var isAddable = true;

$( document ).delegate("#BrandExchange", "pageshow", function() {
	
	
	$.get('ProductPromotionsDataScope.jsp?productpromotionid='+$("#ProductPromotionMasterTableID").val()+'&UserID='+$("#UserID").val(), function(data) {   			
		
		$("#ProductPromotionsDataScope").html(data);
		  $("#ProductPromotionsDataScope").trigger('create');
		});
	
	setTimeout(function(){		
		$('#DistributorID2').on('dblclick', function(e, data){        		
    		$( "#LookupDistributorSearch" ).on( "popupbeforeposition", function( event, ui ) {
    			lookupDistributorInit();
    		} );
    		$('#LookupDistributorSearch').popup("open");
    		
    	});
	}, 2000);
	
	
	setTimeout(function(){
		$('#DeskSaleOutletID').on('dblclick', function(e, data){		
			$( "#LookupOutletSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupOutletInit();
			} );
			$('#LookupOutletSearch').popup("open");
					  
		});
	}, 2000);
	
	setTimeout(function(){
		$('#LiftingReportUser').on('dblclick', function(e, data){
			//alert("helo");
			$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
				lookupEmployeeInit();
			} );
			$('#LookupEmployeeSearch').popup("open");
					  
		});
	}, 2000);
});



$( document ).delegate("#BrandExchange", "pageinit", function() {
	
	$("#BrandExchangePackage").change( function( event ) {
		getBrandList( $("#BrandExchangePackage").val() );
		
	});
	
	$("#ProductPromotionPPackage").change( function( event ) {
		getBrandListPromotions( $("#ProductPromotionPPackage").val() );
		
	});
	
	
	

});


function isInteger (o) {
	
	  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
	}


function ProductPromotionsDeleteRow(RowID){
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		$('#BrandExchangeSave').addClass('ui-disabled');
	}
}





function isInteger (o) {
	
  return ! isNaN (o-0) && o != null && o.indexOf('.') == -1;
}




function removeItems(SelectID){
	
	var select = document.getElementById(SelectID);

	for (var i = 0 ; i < select.length ; i++){
		select.remove(i);
	}
}

function addItem(SelectID, Value, Text){
    // Create an Option object

    var opt = document.createElement("option");
    
    // Add an Option object to Drop Down/List Box
    document.getElementById(SelectID).options.add(opt);
    // Assign text and value to Option object
    opt.text = Text;
    opt.value = Value;
           
}

function getCoolerInjectionInfoJson(){
	
//alert();
		$.ajax({
		    url: "tot/TOTCoolerInjectionGetCoInjInfoJson",
		    data: {
		    	ProductCoolerInjectionOutletID: $("#ProductCoolerInjectionOutletID").val()
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					$("#outlet_id").val(json.outlet_id);
					$("#OutletName").val(json.outlet_name);
					$("#OutletAddress").val(json.outlet_address);
					$("#out_region_id").val(json.outlet_region_id);
					$("#OutletRegion").val(json.outlet_region_name);
					$("#out_rsm_id").val(json.outlet_rsm_id);
					$("#OutletRSM").val(json.outlet_rsm_name);
					$("#out_snd_id").val(json.outlet_snd_id);
					$("#OutletSND").val(json.outlet_snd_name);					
					
					$("#out_distributor_id").val(json.outlet_distributor_id);
					$("#OutletDistributor").val(json.outlet_distributor_name);
					$("#OutletContactName").val(json.outlet_contact_name);
					$("#OutletContactNumber").val(json.outlet_contact_number);
					$("#out_channel_id").val(json.outlet_channel_id);
					$("#OutletChannel").val(json.outlet_channel_name);
					
					$("#OutletContactNic").val(json.outlet_contact_nic);
					
					//filling beatplan check boxes
					
					$("input[type='checkbox']").attr("checked",false).checkboxradio("refresh");
					
					for(var i=0;i<json.rows.length;i++)
		    		{
						$("#BeatplanD"+json.rows[i].day_number).prop('checked', true).checkboxradio('refresh');
		    		}
					
					var ExistingTOTHTML="";
					
					for(var j=0;j<json.rows1.length;j++){
						//ExistingTOTHTML +="";
						
						ExistingTOTHTML+= "<tr >"+
												"<td style='width:25%'>"+json.rows1[j].main_asset_number+"</td>"+
												"<td style='width:25%'>"+json.rows1[j].inventory_number+"</td>"+	   							
												"<td style='width:25%'>"+json.rows1[j].tot_status+"</td>"+
												"<td style='width:25%'>"+json.rows1[j].movement_date+"</td>"+
											"</tr>";
					}
					//alert(ExistingTOTHTML)
					
		    		$("#ExistingTOTTable").html(ExistingTOTHTML);
		    	}else{
		    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	
	
}


function AddSalesVolRows(){
	
	//alert();
	
	var flag=true;
	
	if($("#SelectPCItemType").val()=="" || $("#SelectPCItemPack").val()=="" || ($("#SelectPCItemQuantity").val()==0 || $("#SelectPCItemQuantity").val()=="")){
		flag=false;
	}
	
	if(flag){
		var RowCount = $("#SalesVolPerYearRowCount").val(); //getting row count value
		
		var content = ""+
		"<tr id='SalesVolDynamicTableRows_"+RowCount+"'>"+
			"<td>"+$("#SelectPCItemType option:selected").text()+
			"<input type='hidden' name='SelectPCItemTypeDetail' value='"+$("#SelectPCItemType").val()+"' /></td>"+			
			
			
			"<td>"+$("#SelectPCItemPack option:selected").text()+"<input type='hidden' name='SelectPCItemPackDetail' value='"+$("#SelectPCItemPack").val()+"'></td>"+
			"<td>"+$("#SelectPCItemQuantity").val()+"<input type='hidden' name='SelectPCItemQuantityDetail' value='"+$("#SelectPCItemQuantity").val()+"'></td>"+
			"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"SalesVolDeleteRow('SalesVolDynamicTableRows_"+RowCount+"')\">Delete</a></td>"+
		"</tr>";
		
		$("#NoProductRow").html("");
		$("#SalesVolPerYearRow").append(content).trigger('create');
		$("#SalesVolPerYearRowCount").val(parseInt(RowCount)+1); //adding row count value and saving it
	}else{
		alert("Please select some type");
	}
}


function SalesVolDeleteRow(RowID){
	$("#"+RowID).remove();
	RowCount--;
	if( RowCount < 1){
		$('#NoProductRow').css('display', 'table-row');
		
	}
}

function LoadPackageByType(){
	
	
	$.ajax({
	    url: "tot/TOTCoolerInjectionGetPackByLRBTypeInfoJson",
	    data: {
	    	LRBTypeIDD: $("#SelectPCItemType").val()
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
			
	    	if (json.exists == "true"){
	    		var content ="";
	    		content+="<select data-mini='true' id='SelectPCItemPack' name='SelectPCItemPack'>";
	    		content+="<option value=''>Select Package</option>";
	    		
	    		for(var i=0;i<json.rows.length;i++)
	    		{
	    			content+="<option value='"+json.rows[i].package_id+"'>"+json.rows[i].package_label+"</option>";
	    		}
	    		
	    		content +="</select>";	    		
	    		
	    		$("#LRBTypePackgeFilterRow").html(content).trigger('create');
	    	}else{
	    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function AddSalesVolRows123123213213(){
	
	
	
	
	
	$.ajax({
	    url: "tot/TOTCoolerInjectionGetPackAndLrbInfoJson",
	    data: {
	    	ProductCoolerInjectionOutletID: 0
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
			
	    	if (json.exists == "true"){
	    		
	    		var RowCount = $("#SalesVolPerYearRowCount").val(); //getting row count value
	    		var content = "";
	    		
	    		content +="<tr>";
	    		
		    		
		    		
		    		
		    		content +="<td>";
		    		content+="<select data-mini='true' id='SelectPCItemPack"+RowCount+"' name='SelectPCItemPack'>";
		    		content+="<option value=''>Select Type</option>";
		    		
		    		for(var i=0;i<json.rows1.length;i++)
		    		{
		    			content+="<option value='"+json.rows1[i].lrb_id+"'>"+json.rows1[i].lrb_label+"</option>";
		    		}
		    		
		    		content +="</select>";
		    		content +="</td>";
		    		
		    		
		    		content +="<td>";
		    		content+="<select data-mini='true' id='SelectPCItemType"+RowCount+"' name='SelectPCItemType'>";
		    		content+="<option value=''>Select Package</option>";
		    		
		    		for(var i=0;i<json.rows.length;i++)
		    		{
		    			content+="<option value='"+json.rows[i].package_id+"'>"+json.rows[i].package_label+"</option>";
		    		}
		    		
		    		content +="</select>";
		    		content +="</td>";
		    		
		    		content +="<td>";
		    		content +="<input type='text' placeholder='' id='SelectPCItemQuantity"+RowCount+"' name='SelectPCItemQuantity' data-mini='true' >";
		    		content +="</td>";
		    		
		    		
		    		content +="<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>";
		    		
		    		
	    		
	    		content +="</tr>";
	    		
	    		$("#SalesVolPerYearRow").append(content).trigger('create');
	    		
				
				$("#SalesVolPerYearRowCount").val(parseInt(RowCount)+1); //adding row count value and saving it
				
				
	    		
	    	}else{
	    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});

	
	
	
	
	
		/*
		
		
		
		content +="<td>"+
		 			"<input type='text' placeholder='' id='SelectPCItemQuantity"+RowCount+"' name='SelectPCItemQuantity' data-mini='true' >"+
		 		"</td>";
		content +="<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"; */
	
	
	
	
	
	
	//add code here to add multiple rows
}





function CoolerInjectionSave()
{
	//if($("#ProductPromotionsPProductCodeIssue").val()=="")
	//{ alert();}
	//alert();
	$("#CoolerInjectionSaveBtn").addClass("ui-disabled");
	$.ajax({    		
			url: "tot/TOTCoolerInjectionExecute",    			
			    data: $("#CoolerInjectionMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			success:function(json){
				if(json.success == "true"){    				
					location = "TOTCoolerInjectionRequest.jsp";
					
				}else{
					alert(json.error);
					$("#CoolerInjectionSaveBtn").removeClass("ui-disabled");
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
			}
		});
}

function LoadPerticularProductPromotions(ProductPromotionIdd)
{	
	$("#isEditCase").val("1"); //edit case
	$.ajax({
	    url: "inventory/ProductPromotionGetProductPromotionInfoJson",
	    data: {
	    	ProductPromotionID: ProductPromotionIdd
	    },
	    type: "POST",
	    dataType : "json",
	    success: function(json) {
			
	    	if (json.exists == "true"){
	    		$("#popupDialog").popup( "close" );
	    		//loading master table
	    		$("#ProductPromotionMasterTableID").val(ProductPromotionIdd); //setting the master table id for edit case
	    		$("#ProductPromotionsLabel").val(json.Label);
	    		$("#ProductPromotionsValidFrom").val(json.ValidFrom);
	    		$("#ProductPromotionsValidTo").val(json.ValidTo);
	    		$("#ProductPromotionsIsActive").val(json.IsActive);
	    		$('#ProductPromotionsIsActive').selectmenu("refresh");
	    		//$("#ProductPromotionsIsActive").attr("selected", "selected")
	    		$('#ProductPromotionsSave').removeClass('ui-disabled');
	    		//loading sub table
	    		var content = "";
	    		$("#ProductPromotionsTableBody").html("").trigger('create');
	    		$("#ProductPromotionsTableBody1").html("").trigger('create');
	    		for(var i=0;i<json.rows.length;i++)
	    		{
		    		if(json.rows[i].TypeID == "1") //for sales
	    			{
		    			var BrandListName = "";
		    			var BrandListHiddenFields ="";
		    				
		    				for(var j=0;j<json.BrandsRows.length;j++)
	    					{
		    					//alert();
		    					if(json.rows[i].PackageID == json.BrandsRows[j].PackageID1 && json.BrandsRows[j].TypeID1 =="1")
	    						{
		    						BrandListName +=json.BrandsRows[j].BrandLabel+"<br/>";
			    					BrandListHiddenFields += "<input type='hidden' name='Brands"+json.rows[i].PackageID+"' value='"+json.BrandsRows[j].BrandID+"'>";
	    						}
	    					}
		    				//alert(BrandListName);
		    			content =
			    			"<tr id='ProductPromotionsDynamicTableRows_"+i+"'>"+
			    			"<td>"+json.rows[i].PackageLabel+
			    			"<input type='hidden' name='ProductPromotionsMainFormPackage' value='"+json.rows[i].PackageID+"' /></td>"+			
			    			
			    			"<td>"+BrandListName+
			    			BrandListHiddenFields+
			    			"<td>"+json.rows[i].RawCases+"<input type='hidden' name='ProductPromotionsMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
			    			"<td>"+json.rows[i].Units+"<input type='hidden' name='ProductPromotionsMainFormUnits' value='"+json.rows[i].Units+"'></td>"+
			    			"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"ProductPromotionsDeleteRow('ProductPromotionsDynamicTableRows_"+i+"')\">Delete</a></td>"+
			    		"</tr>";
		    			$("#ProductPromotionsTableBody").append(content).trigger('create');
	    			}
		    		else
	    			{
		    			var BrandListName = "";
		    			var BrandListHiddenFields ="";
		    				
		    				for(var j=0;j<json.BrandsRows.length;j++)
	    					{
		    					if(json.rows[i].PackageID == json.BrandsRows[j].PackageID1 && json.BrandsRows[j].TypeID1 =="2")
	    						{
		    						BrandListName +=json.BrandsRows[j].BrandLabel+"<br/>";
			    					BrandListHiddenFields += "<input type='hidden' name='PromotionBrand"+json.rows[i].PackageID+"' value='"+json.BrandsRows[j].BrandID+"'>";
	    						}
	    					}
		    				
		    			content =
				    		"<tr id='ProductPromotionsPDynamicTableRows_"+i+"'>"+
				    			"<td>"+json.rows[i].PackageLabel+
				    			"<input type='hidden' name='ProductPromotionsPMainFormPackage' value='"+json.rows[i].PackageID+"' /></td>"+			
				    			
				    			"<td>"+BrandListName+
				    			BrandListHiddenFields+
				    			"<td>"+json.rows[i].RawCases+"<input type='hidden' name='ProductPromotionsPMainFormRawCases' value='"+json.rows[i].RawCases+"'></td>"+
				    			"<td>"+json.rows[i].Units+"<input type='hidden' name='ProductPromotionsPMainFormUnits' value='"+json.rows[i].Units+"'></td>"+
				    			"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"ProductPromotionsDeleteRow('ProductPromotionsPDynamicTableRows_"+i+"')\">Delete</a></td>"+
				    		"</tr>";
		    			$("#ProductPromotionsTableBody1").append(content).trigger('create');
	    			}
	    		}
	    		
	    		
	    			
	    			$.get('ProductPromotionsDataScope.jsp?productpromotionid='+$("#ProductPromotionMasterTableID").val()+'&UserID='+$("#UserID").val(), function(data) {   			
		    			
		    			$("#ProductPromotionsDataScope").html(data);
		    			  $("#ProductPromotionsDataScope").trigger('create');
		    			});
	    			
	    		
	    		$("#ProductPromotionDeactivate").removeClass("ui-disabled");	    		
	    		$("#ProductPromotionsIsActive").addClass("ui-disabled");
	    		$("#ProductPromotionsIsActive").selectmenu('refresh', true);
	    		
				
	    	}else{
	    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    	//alert(status);
	    }
	});
}

function AddRegion()
{    	
	//adding region
	var RegionAlreadyAdded = false;    	
	$('#AddeddRegion li input').each(function(){ 
		var NewInsertedListID = $("#RegionSelect option:selected").val()+",Region";	        		
		if($(this).val().trim() == NewInsertedListID.trim())
		{
			RegionAlreadyAdded = true;		        			
		}
	});

	var FunctionName = "onClick=RemoveList('RegionDynamicallyAdded_"+$("#RegionSelect option:selected").val()+"')";
	
	if(!RegionAlreadyAdded && $("#RegionSelect option:selected").val() != "-1")
	{
		var AppendedString = "<li data-mini='true' data-icon='delete' id='RegionDynamicallyAdded_"+$("#RegionSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='RegionDynamicallyAddedHidden_"+$("#RegionSelect option:selected").val()+"' name='RegionIDhiddenfield' value='"+$("#RegionSelect option:selected").val()+",Region'/><a href='#'>"+$("#RegionSelect option:selected").text()+"</a></li>";
		
		$("#populateRegionDataUl").append(AppendedString);    	
    	$("#populateRegionDataUl").listview("refresh");
	}
}

function AddGroupDistributor()
{
	
	var GroupAlreadyAdded = false;
	$("#WarehouseeSave").removeClass('ui-disabled');
	$('#AddeddGroupDistributor li input').each(function(){ 
		var NewInsertedListID = $("#GroupDistributorSelect option:selected").val()+",GroupDistributor";	        		
		if($(this).val().trim() == NewInsertedListID.trim())
		{
			GroupAlreadyAdded = true;		        			
		}
	});

	var FunctionName = "onClick=RemoveList('GroupDistributorDynamicallyAdded_"+$("#GroupDistributorSelect option:selected").val()+"')";
	
	if(!GroupAlreadyAdded && $("#GroupDistributorSelect option:selected").val() != "-1")
	{
		var AppendedString = "<li data-mini='true' data-icon='delete' id='GroupDistributorDynamicallyAdded_"+$("#GroupDistributorSelect option:selected").val()+"' "+FunctionName+"><input type='hidden' id='GroupDistributorDynamicallyAddedHidden_"+$("#GroupDistributorSelect option:selected").val()+"' name='GroupDistributorhiddenfield' value='"+$("#GroupDistributorSelect option:selected").val()+",GroupDistributor'/><a href='#'>"+$("#GroupDistributorSelect option:selected").text()+"</a></li>";
		
		$("#populateGroupDistributorDataUl").append(AppendedString);    	
    	$("#populateGroupDistributorDataUl").listview("refresh");
	}
}

function AddDistributor()
{
	
	$("#WarehouseeSave").removeClass('ui-disabled');
	var DistributorAlreadyAdded = false;
	$('#AddeddDistributor li input').each(function(){    		
		var NewInsertedListID = $("#DistributorID2").val()+",Distributor"; //distributorid,featureid    		
		if($(this).val().trim() == NewInsertedListID.trim())
		{
			DistributorAlreadyAdded = true;    			
		}
	});
	
	var FunctionName = "onClick=RemoveList('DistributorIDDynamicallyAdded_"+$("#DistributorID2").val()+"')";
	if(!DistributorAlreadyAdded && $("#DistributorID2").val().trim()!="")
	{
		var AppendedString = "<li data-mini='true' data-icon='delete' id='DistributorIDDynamicallyAdded_"+$("#DistributorID2").val()+"' value='"+$("#DistributorID2").val()+",' "+ FunctionName+"><input type='hidden' id='DistributorIDForHiddenFieldID_"+$("#DistributorID2").val()+"' name='DistributorIDhiddenfield' value='"+$("#DistributorID2").val()+",Distributor'/><a href='#'>"+$("#DistributorID2").val()+" - "+$("#DistributorName2").val()+"</a></li>";
		$("#populateDistributorDataUl").append(AppendedString);    	
    	$("#populateDistributorDataUl").listview("refresh");
	}
}

function RemoveList(ID)
{    	
	//alert(ID);
	$("#"+ID).remove();
	$("#populateDistributorDataUl").listview("refresh");
	$("#populateRegionDataUl").listview("refresh");
	$("#populateDataUl").listview("refresh");
	$("#populateOutletDataUl").listview("refresh");
	
	$("#WarehouseeSave").removeClass('ui-disabled');
}

function DistributorSearchCallBackForUserRights(SAPCode, DistributorName){   	
	
	$('#DistributorID2').val(SAPCode);
	$('#DistributorName2').val(DistributorName);
}

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

function getOutletName(){
	//alert();
	if(isInteger($('#DeskSaleOutletID').val()) == false ){
		$('#DeskSaleOutletID').val('');
		return false;
	}
	
	$.ajax({
		
		url: "common/GetOutletBySAPCodeJSON",
		data: {
			SAPCode: $('#DeskSaleOutletID').val()
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			if(json.exists == "true"){
				$('#DeskSaleOutletName').val(json.OutletName);
			}else{
				$('#DeskSaleOutletName').val('');
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

function OutletSearchCallBackDeskSale(SAPCode, EmployeeName){
	$('#DeskSaleOutletID').val(SAPCode);
	$('#DeskSaleOutletName').val(EmployeeName);
	getOutletMasterInfo();
}

function AddOutlet()
{
	
	
	var OutletAlreadyAdded = false;
	$('#AddeddOutlet li input').each(function(){    		
		var NewInsertedListID = $("#DeskSaleOutletID").val()+",Outlet"; //distributorid,featureid    		
		if($(this).val().trim() == NewInsertedListID.trim())
		{
			OutletAlreadyAdded = true;    			
		}
	});
	
	var FunctionName = "onClick=RemoveList('OutletIDDynamicallyAdded_"+$("#DeskSaleOutletID").val()+"')";
	if(!OutletAlreadyAdded && $("#DeskSaleOutletID").val().trim()!="")
	{
		var AppendedString = "<li data-mini='true' data-icon='delete' id='OutletIDDynamicallyAdded_"+$("#DeskSaleOutletID").val()+"' value='"+$("#DeskSaleOutletID").val()+",' "+ FunctionName+"><input type='hidden' id='OutletIDForHiddenFieldID_"+$("#DeskSaleOutletID").val()+"' name='OutletIDhiddenfield' value='"+$("#DeskSaleOutletID").val()+",Outlet'/><a href='#'>"+$("#DeskSaleOutletID").val()+" - "+$("#DeskSaleOutletName").val()+"</a></li>";
		$("#populateOutletDataUl").append(AppendedString);    	
    	$("#populateOutletDataUl").listview("refresh");
	}
}

function EmployeeSearchCallBackLiftingReport(SAPCode, EmployeeName){
	
	$('#LiftingReportUser').val(SAPCode);
	$('#LiftingReportUserName').val(EmployeeName);
}

function getEmployeeName(){
	//alert();
	if( $('#LiftingReportUser').val() == "" ){
		return false;
	}
	if( isInteger($('#LiftingReportUser').val()) == false ){
		setTimeout("$('#LiftingReportUser').focus();", 100);
		return false;
	}
	
	$.ajax({
		url: "employee/GetEmployeeBySAPCodeJSON",
		data: {
			SAPCode: $('#LiftingReportUser').val()
		},
		type:"POST",
		dataType: "json",
		success: function(json){ 
			if( json.exists == 'true' ){
				$('#LiftingReportUserName').val(json.EmployeeName);
			}
		},
		error: function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
	
	
}

function getPJPName(){
	
	if( $('#ProductPromotionPJPID').val() == "" ){
		return false;
	}
	if( isInteger($('#ProductPromotionPJPID').val()) == false ){
		setTimeout("$('#ProductPromotionPJPID').focus();", 100);
		return false;
	}
	
	$.ajax({
		url: "common/GetBeatPlanInfoJson",
		data: {
			PJPID: $('#ProductPromotionPJPID').val()
		},
		type:"POST",
		dataType: "json",
		success: function(json){ 
			if( json.success == 'true' ){
				$('#ProductPromotionPJPName').val(json.PJPName);
			}
		},
		error: function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
	
	
}


function AddOrderBooker()
{
	var OrderBookerAlreadyAdded = false;
	$('#AddeddOrderBooker li input').each(function(){    		
		var NewInsertedListID = $("#LiftingReportUser").val()+",OrderBooker"; //distributorid,featureid    		
		if($(this).val().trim() == NewInsertedListID.trim())
		{
			OrderBookerAlreadyAdded = true;    			
		}
	});
	
	var FunctionName = "onClick=RemoveList('OrderBookerDynamicallyAdded_"+$("#LiftingReportUser").val()+"')";
	if(!OrderBookerAlreadyAdded && $("#LiftingReportUser").val().trim()!="")
	{
		var AppendedString = "<li data-mini='true' data-icon='delete' id='OrderBookerDynamicallyAdded_"+$("#LiftingReportUser").val()+"' value='"+$("#LiftingReportUser").val()+",' "+ FunctionName+"><input type='hidden' id='OrderBookerForHiddenFieldID_"+$("#LiftingReportUser").val()+"' name='OrderBookerhiddenfield' value='"+$("#LiftingReportUser").val()+",OrderBooker'/><a href='#'>"+$("#LiftingReportUser").val()+" - "+$("#LiftingReportUserName").val()+"</a></li>";
		$("#populateOrderBookerDataUl").append(AppendedString);    	
    	$("#populateOrderBookerDataUl").listview("refresh");
	}
}

function AddPJP()
{
	var PJPAlreadyAdded = false;
	$('#AddeddPJP li input').each(function(){    		
		var NewInsertedListID = $("#ProductPromotionPJPID").val(); //distributorid,featureid    		
		if($(this).val().trim() == NewInsertedListID.trim())
		{
			PJPAlreadyAdded = true;    			
		}
	});
	
	var FunctionName = "onClick=RemoveList('PJPDynamicallyAdded_"+$("#ProductPromotionPJPID").val()+"')";
	if(!PJPAlreadyAdded && $("#ProductPromotionPJPID").val().trim()!="")
	{
		var AppendedString = "<li data-mini='true' data-icon='delete' id='PJPDynamicallyAdded_"+$("#ProductPromotionPJPID").val()+"' value='"+$("#ProductPromotionPJPID").val()+"' "+ FunctionName+"><input type='hidden' id='PJPForHiddenFieldID_"+$("#ProductPromotionPJPID").val()+"' name='PJPhiddenfield' value='"+$("#ProductPromotionPJPID").val()+"'/><a href='#'>"+$("#ProductPromotionPJPID").val()+" - "+$("#ProductPromotionPJPName").val()+"</a></li>";
		$("#populatePJPDataUl").append(AppendedString);    	
    	$("#populatePJPDataUl").listview("refresh");
	}
}

function ProductPromotionsAdditionalRightSubmit(ProductPromotionID)
{
	//alert(ProductPromotionID);
	$("#ProductPromotionMasterTableID").val(ProductPromotionID);//setting product promotion 
	$.ajax({
	    url: "inventory/ProductPromotionsRequestAdditionalRightExecute",
	    
	    data: $("#ProductPromotionsMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){		    		
	    		window.location="ProductPromotionsRequest.jsp";
	    	}else{
				alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    } 
	});
}

function DeactivateProductPromotion()
{
	//alert($("#ProductPromotionMasterTableID").val());
	//alert($("#ProductPromotionReason").val());
	$.ajax({
		
		url: "inventory/ProductPromotionsRequestDeactivateExecute",
		data: {
			ProductPromotionID: $("#ProductPromotionMasterTableID").val(),
			DeactivatedBy:$("#UserID").val(),
			Reason:$("#ProductPromotionReason").val()
			
		},
		type:"POST",
		dataType:"json",
		success:function(json){			
			if(json.success == "true"){
				window.location="ProductPromotions.jsp";
			}
		},
		error:function(xhr, status){
			alert("Server could not be reached.");
		}
		
	});
	
}

function CheckAllCheckboxes()
{
	//alert($("#IsCheckedAllCheckboxes").val());
	if($("#IsCheckedAllCheckboxes").val()=="0") //mean no select all
	{
		$('#SpanProductPromotionsIssuePromotions input[type=checkbox]').each(function () {			
			$(this).prop("checked",true).checkboxradio("refresh");
		});
		$("#IsCheckedAllCheckboxes").val("1");
		//alert("in if");
	}
	else
	{
		$('#SpanProductPromotionsIssuePromotions input[type=checkbox]').each(function () {			
			$(this).prop("checked",false).checkboxradio("refresh");
		});
		$("#IsCheckedAllCheckboxes").val("0");
	//	alert("in else");
	}
	
}

function CheckAllCheckboxesSales()
{
	//alert($("#IsCheckedAllCheckboxes").val());
	if($("#IsCheckedAllCheckboxesSales").val()=="0") //mean no select all
	{
		$('#SpanProductPromotionsIssue input[type=checkbox]').each(function () {			
			$(this).prop("checked",true).checkboxradio("refresh");
		});
		$("#IsCheckedAllCheckboxesSales").val("1");
		//alert("in if");
	}
	else
	{
		$('#SpanProductPromotionsIssue input[type=checkbox]').each(function () {			
			$(this).prop("checked",false).checkboxradio("refresh");
		});
		$("#IsCheckedAllCheckboxesSales").val("0");
	//	alert("in else");
	}
	
}

function CalculateMarginalContribution(){
	$("#MarginalContribution").val($("#SalesSKUPrice").val()-$("#FreeSKUPriceBottles").val()-$("#VariableCost").val());
}

function CalculateNetPrice(){
	$("#netprice").val($("#SalesSKUPrice").val()-$("#FreeSKUPriceBottles").val());
}

function SetBrandHidden(bid){
	if($("#ProductPromtionBrandIDDFlag").val()=="0"){
		$("#ProductPromtionBrandIDD").val(bid);
		$("#ProductPromtionBrandIDDFlag").val("1");
	}
}

function ShowHideTable(){
	if ($('#NewOutletBox').is(":checked")) //Mean new outlet
	{
		$("#NormalOutletTable").css("display", "none");
		$("#NewOutletTable").css("display", "block");
		$("#NewOutletTable").trigger('create');
		$("#ExistingTOTTable").html("");
		
	}else{
		$("#NormalOutletTable").css("display", "block");
		$("#NewOutletTable").css("display", "none");
		$("#NormalOutletTable").trigger('create');
	}
	
	
}

function getDistributorName12(){
	
	//alert($('#OutletDistributor').val());
	$.mobile.showPageLoadingMsg();
	
	$.ajax({
		
		url: "common/GetDistributorInfoJson",
		data: {
			DistributorID: $('#OutletDistributorN').val(),
			FeatureID:269
		},
		type:"POST",
		dataType:"json",
		success:function(json){
			
			$.mobile.hidePageLoadingMsg();
			
			if(json.exists == "true"){
				$('#OutletDistributorNameN').val(json.DistributorName);
				
			}else{
				$.mobile.hidePageLoadingMsg();
				$('#OutletDistributorNameN').val('Distributor not found');
				
				
			}
		},
		error:function(xhr, status){
			$.mobile.hidePageLoadingMsg();
			alert("Server could not be reached.");
		}
		
	});
	
}

