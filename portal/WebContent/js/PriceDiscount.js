$(document).delegate(
		"#PriceDiscount",
		"pageshow",
		function() {

			// Load Scope Filters
			$.get('PriceDiscountDataScope.jsp?pricediscountid='
					+ $("#PriceDiscountMasterTableID").val() + '&UserID='
					+ $("#UserID").val(), function(data) {

				$("#PriceDiscountDataScope").html(data);
				$("#PriceDiscountDataScope").trigger('create');
			});
			// Open Distributor Search Box
			setTimeout(function() {
				$('#DistributorID2').on(
						'dblclick',
						function(e, data) {
							$("#LookupDistributorSearch").on(
									"popupbeforeposition", function(event, ui) {
										lookupDistributorInit();
									});
							$('#LookupDistributorSearch').popup("open");
						});
			}, 2000);
		});

function DistributorSearchCallBackForUserRights(SAPCode, DistributorName){	
	
	$('#DistributorID2').val(SAPCode);
	$('#DistributorName2').val(DistributorName);
}

function removeItems(SelectID){
	
	var select = document.getElementById(SelectID);

	for (var i = 0 ; i < select.length ; i++){
		select.remove(i);
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

function AddRegion() {
	// adding region
	var RegionAlreadyAdded = false;
	$('#AddeddRegion li input').each(
			function() {
				var NewInsertedListID = $("#RegionSelect option:selected")
						.val()
						+ ",Region";
				if ($(this).val().trim() == NewInsertedListID.trim()) {
					RegionAlreadyAdded = true;
				}
			});

	var FunctionName = "onClick=RemoveList('RegionDynamicallyAdded_"
			+ $("#RegionSelect option:selected").val() + "')";

	if (!RegionAlreadyAdded && $("#RegionSelect option:selected").val() != "-1") {
		var AppendedString = "<li data-mini='true' data-icon='delete' id='RegionDynamicallyAdded_"
				+ $("#RegionSelect option:selected").val()
				+ "' "
				+ FunctionName
				+ "><input type='hidden' id='RegionDynamicallyAddedHidden_"
				+ $("#RegionSelect option:selected").val()
				+ "' name='RegionIDhiddenfield' value='"
				+ $("#RegionSelect option:selected").val()
				+ ",Region'/><a href='#'>"
				+ $("#RegionSelect option:selected").text() + "</a></li>";

		$("#populateRegionDataUl").append(AppendedString);
		$("#populateRegionDataUl").listview("refresh");
	}
}

function AddChannel() {
	// adding region
	var ChannelAlreadyAdded = false;
	$('#AddeddChannel li input').each(
			function() {
				var NewInsertedListID = $("#ChannelSelect option:selected")
						.val()
						+ ",Channel";
				if ($(this).val().trim() == NewInsertedListID.trim()) {
					ChannelAlreadyAdded = true;
				}
			});

	var FunctionName = "onClick=RemoveList('ChannelDynamicallyAdded_"
			+ $("#ChannelSelect option:selected").val() + "')";

	if (!ChannelAlreadyAdded && $("#ChannelSelect option:selected").val() != "-1") {
		var AppendedString = "<li data-mini='true' data-icon='delete' id='ChannelDynamicallyAdded_"
				+ $("#ChannelSelect option:selected").val()
				+ "' "
				+ FunctionName
				+ "><input type='hidden' id='ChannelDynamicallyAddedHidden_"
				+ $("#ChannelSelect option:selected").val()
				+ "' name='ChannelIDhiddenfield' value='"
				+ $("#ChannelSelect option:selected").val()
				+ ",Channel'/><a href='#'>"
				+ $("#ChannelSelect option:selected").text() + "</a></li>";

		$("#populateChannnelDataUl").append(AppendedString);
		$("#populateChannnelDataUl").listview("refresh");
	}
}

function PriceDiscountSubmit()
{
	//if($("#ProductPromotionsPProductCodeIssue").val()=="")
	//{ alert();}
	
	$.ajax({    		
			url: "discounts/AddPriceDiscountExecute",    			
			    data: $("#PriceDiscountsMainForm" ).serialize(),
			    type: "POST",
			    dataType : "json",
			success:function(json){
				if(json.success == "true"){  
					alert(json.error);
					window.location="PriceDiscount.jsp";
					
				}else{
					alert(json.error);
					
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
			}
		});
}

function LoadPriceDiscount(PriceDiscountMasterTableID)
{	
	$("#isEditCase").val("1"); //edit case
	$.mobile.loading("show");
	$.ajax({
	    url: "discounts/PriceDiscountInfoJson",
	    data: {
	    	PriceDiscountMasterTableID: PriceDiscountMasterTableID
	    },
	    type: "POST",
	    dataType : "json",
	    success: function(json) {
	    	$.mobile.loading("hide");

			console.log("i am in....");
	    	if (json.success == "true"){
	    		$("#popupDialog").popup( "close" );
	    		//loading master table

	    		
	    		$("#PriceDiscountLabel").val(json.discount_name);
	    		$("#PriceDiscountValidFrom").val(json.valid_from);
	    		$("#PriceDiscountValidTo").val(json.valid_to);
	    		$("#PriceDiscountIsActive").val(json.is_active);
	    		$("input[name^='Discount_']").val("0");
	    		$("input[name^='CheckBox_']").prop("checked",false);
	    		$("input[name^='PercentageCheckBox_']").prop("checked",false);
	    		//$("input[name^='DiscountTypeAmount_']").prop("checked",true);
	    		
	    		// Loop through returned products and set discount values
	    		for (var i = 0; i < json.products.length; i++) {
	    		    var p = json.products[i];
	    		    var productId = p.product_id;
	    		    var discountValue = p.discount_value;
	    		    var isWithDiscount = p.is_with_tax;
	    		    var isPercentage = p.is_percentage;
	    		    console.log("percentage:"+isPercentage);
	    		    

	    		    $("input[name^='ProductCode_']").each(function () {
	    		        if ($(this).val() == productId) {
	    		            var rowIndex = $(this).attr("name").split("_")[1];
	    		            

	    		            // Set discount value
	    		            $("input[name='Discount_" + rowIndex + "']").val(discountValue);
	    		            if(isWithDiscount == 1){
	    		            	$("input[name='CheckBox_"+ rowIndex + "']").prop("checked",true);
	    		            }
	    		            if(isPercentage == 2){
	    		            	$("input[name='PercentageCheckBox_"+ rowIndex + "']").prop("checked",true);
	    		            }

	    		        }
	    		    });
	    		}
	    		
	    		
	    		
	    		$('#PriceDiscountIsActive').selectmenu("refresh");
	    		//$("#ProductPromotionsIsActive").attr("selected", "selected")
	    		$('#ProductPromotionsSave').removeClass('ui-disabled');
	    		//$('#PriceDiscountIsActive').removeClass('ui-disabled');
	    		//loading sub table
	    		var content = "";
	    		$("#ProductPromotionsTableBody").html("").trigger('create');
	    		$("#ProductPromotionsTableBody1").html("").trigger('create');
	    		/*for(var i=0;i<json.rows.length;i++)
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
	    		}*/
	    		
	    		
	    			
/*	    			$.get('ProductPromotionsDataScope.jsp?productpromotionid='+$("#ProductPromotionMasterTableID").val()+'&UserID='+$("#UserID").val(), function(data) {   			
		    			
		    			$("#ProductPromotionsDataScope").html(data);
		    			  $("#ProductPromotionsDataScope").trigger('create');
		    			});*/
	    		
	    		$.get('PriceDiscountDataScope.jsp?pricediscountid='
						+ json.product_discount_id + '&UserID='
						+ $("#UserID").val(), function(data) {

					$("#PriceDiscountDataScope").html(data);
					$("#PriceDiscountDataScope").trigger('create');
				});
	    			
	    		
	    		$("#ProductPromotionDeactivate").removeClass("ui-disabled");	    		
	    		$("#ProductPromotionsIsActive").addClass("ui-disabled");
	    		$("#ProductPromotionsIsActive").selectmenu('refresh', true);
	    		
				
	    	}else{
	    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
	    	}
	    },
	    error: function( xhr, status ) {
	    	$.mobile.loading("hide");
	    	alert("Server could not be reached.");
	    	//alert(status);
	    }
	});
}

