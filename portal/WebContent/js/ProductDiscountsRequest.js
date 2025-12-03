
var RowCount = 0;
var RowCount1 = 0;
var isAddable = true;

$( document ).delegate("#BrandExchange", "pageshow", function() {
	
	
	$.get('ProductDiscountsDataScope.jsp?productpromotionid='+$("#ProductPromotionMasterTableID").val()+'&UserID='+$("#UserID").val(), function(data) {   			
		
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


function getBrandList(PackageID){
	$.mobile.showPageLoadingMsg();
	
	if (PackageID != ""){
		$.ajax({
		    url: "inventory/ProductDiscountGetBrandListJson",
		    data: {
		    	PackageID: PackageID,
		    	IsLiquidOnly: 'true'
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if (json.exists == "true"){
		    		
		    		var regex = new RegExp('select_id', 'g');
		    		
		    		
		    		
		    		//var BrandSelectLis="<select id='ProductPromotionsProductCodeIssue' name='ProductPromotionsProductCodeIssue' data-mini='true' ><option value=''>Select Brand</option><option value='0'>Any</option>";
		    		//for(var i=0;i<json.rows.length;i++)
		    		//{
		    		//	BrandSelectLis+="<option value='"+json.rows[i].id+"'>"+json.rows[i].label+"</option>";
		    		//}
					var BrandSelectLis="";
					var x=0;
		    		for(var i=0;i<json.rows.length;i++)
		    		{
		    			
		    			BrandSelectLis+='<input type="checkbox" name="PromotionBrandList" id="PromotionBrandList_'+x+'" value="'+json.rows[i].id+'" style="width:80%">'+
						'<label for="PromotionBrandList_'+x+'" >'+json.rows[i].label+' </label>';
		    			x++;
		    		}
		    		BrandSelectLis +='<input type="checkbox" name="PromotionBrandList12f" id="PromotionBrandList_12f" value="" onClick="CheckAllCheckboxesSales()">'+
				    '<label for="PromotionBrandList_12f" >Select All</label>';
		    		//alert(BrandSelectLis);
		    		$('#SpanProductPromotionsIssue').html(BrandSelectLis);
		    		$('#SpanProductPromotionsIssue').trigger('create');
		    		//$("input[type='checkbox']").attr("checked",false).checkboxradio("refresh");
		    		
		    		//$("#SpanProductPromotionsIssue").checkboxradio("refresh");
			    	$.mobile.hidePageLoadingMsg();
					
		    	}else{
		    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}

function getBrandListPromotions(PackageID){
	$.mobile.showPageLoadingMsg();
	
	if (PackageID != ""){
		$.ajax({
		    url: "common/GetBrandListJson",
		    data: {
		    	PackageID: PackageID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if (json.exists == "true"){
		    		
		    		var regex = new RegExp('select_id', 'g');
		    		
		    		//alert(json.rows[0].label);
		    		//var BrandSelectLis="<select id='ProductPromotionsPProductCodeIssue' name='ProductPromotionsPProductCodeIssue' data-mini='true' ><option value=''>Select Brand</option>";
		    		//for(var i=0;i<json.rows.length;i++)
		    		//{
		    			//BrandSelectLis+="<option value='"+json.rows[i].id+"'>"+json.rows[i].label+"</option>";
		    		//}
		    		var BrandSelectLis="";
					var x=0;
		    		for(var i=0;i<json.rows.length;i++)
		    		{
		    			
		    			BrandSelectLis+='<input type="checkbox" name="PromotionPBrandList" id="PromotionPBrandList_'+x+'" value="'+json.rows[i].id+'" style="width:80%">'+
						'<label for="PromotionPBrandList_'+x+'" >'+json.rows[i].label+' </label>';
		    			x++;
		    		}
		    		BrandSelectLis +='<input type="checkbox" name="PromotionPBrandList12f" id="PromotionPBrandList_12f" value="" onClick="CheckAllCheckboxes()">'+
				    '<label for="PromotionPBrandList_12f" >Select All</label>';
		    		
		    		//$('#SpanProductPromotionsIssuePromotions').html(json.SelectOptions.replace(regex, 'ProductPromotionsPProductCodeIssue'));
		    		$('#SpanProductPromotionsIssuePromotions').html(BrandSelectLis);
		    		
		    		$("#SpanProductPromotionsIssuePromotions").trigger('create');
			    	$.mobile.hidePageLoadingMsg();
					
		    	}else{
		    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
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

function getProductInfoJson(ProductID){
	
	if (ProductID != ""){
		$.ajax({
		    url: "common/GetProductInfo",
		    data: {
		    	ProductCode: ProductID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.exists == "true"){
					//alert(json.PackageLabel+" "+json.BrandLabel);
					$('#ProductInfoSpan').html(json.PackageLabel+" - "+json.BrandLabel);
					$('#BrandExchangeType').html(json.DamageStockTypeOptions);
					$('#BrandExchangeType').refresh();
					
		    	}else{
		    		//$("#DeliveryNoteDistributorName").val("Invalid ID");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
	}
	
}

function AddSalesToTable()
{
	var IsValid = false;
	if( $('#BrandExchangePackage').val() == "" ){
		document.getElementById('BrandExchangePackage').focus();
		IsValid = true;
	}
	if( $('#ProductPromotionsProductCodeIssue').val() == "" ){
		document.getElementById('ProductPromotionsProductCodeIssue').focus();
		IsValid = true;
	}
	
	
	
	var PackageArray = new Array();
	var BrandArray = new Array();
	PackageArray = document.getElementsByName("ProductPromotionsMainFormPackage");
	BrandArray = document.getElementsByName("ProductPromotionsMainFormProductIDIssue");
	if(PackageArray.length>0 && BrandArray.length>0)
	{
		for(var i=0;i<PackageArray.length;i++)
		{
			//alert(PackageArray[i].value);
			if(($("#BrandExchangePackage option:selected").val() == PackageArray[i].value) && ($("#ProductPromotionsProductCodeIssue option:selected").val() == BrandArray[i].value)) //if same package and same brand already exist
			{
				IsValid = true;
			}
		}
		
	}
	
	
	
	if(!IsValid)
	{
		var BrandListName = "";
		var BrandListHiddenFields ="";
		$('#SpanProductPromotionsIssue input[type=checkbox]').each(function () {
		    //sList += "(" + $(this).val() + "-" + (this.checked ? "checked" : "not checked") + ")";
			//alert($(this).val());
			if(this.checked && $('label[for="'+this.id+'"]').text().trim()!="Select All")
			{
				//BrandList +="";
				//alert(this.id);
				//alert($('label[for="'+this.id+'"]').text());
				BrandListName +=$('label[for="'+this.id+'"]').text().trim()+"<br/>";
				BrandListHiddenFields += "<input type='hidden' name='Brands"+$("#BrandExchangePackage").val()+"_"+RowCount+"' value='"+$(this).val()+"'>";
				//BrandListHiddenFields += "<input type='hidden' name='Brands"+$("#BrandExchangePackage").val()+"' value='"+$(this).val()+"'>";
			}
		});
		
		var content = ""+
		"<tr id='ProductPromotionsDynamicTableRows_"+RowCount+"' style='width:100%'>"+
			"<td>"+$("#BrandExchangePackage option:selected").text()+
			"<input type='hidden' name='ProductPromotionsMainFormPackage' value='"+$("#BrandExchangePackage").val()+"' /></td>"+			
			
			"<td>"+BrandListName+
			BrandListHiddenFields+"</td>"+
			"<td><input type='text' placeholder='Quantity' id='EstimatedSalesVolumeRawCases"+RowCount+"' name='Quantity' data-mini='true'/></td>"+
			"<td><input type='text' placeholder='Distributor Rate' id='SalesSKUPrice"+RowCount+"' name='DiscountRate' data-mini='true' onKeyup='CalculateMarginalContribution("+RowCount+");' /></td>"+
			"<td><input type='text' placeholder='Selling Price' id='FreeSKUPriceBottles"+RowCount+"' name='SellingPrice' data-mini='true' onKeyup='CalculateMarginalContribution("+RowCount+");' /></td>"+
			"<td><input type='text' placeholder='Variable Cost + Taxes' id='VariableCost"+RowCount+"' name='VariableCost' data-mini='true' onKeyup='CalculateMarginalContribution("+RowCount+")' readonly/></td>"+
			"<td><input type='text' placeholder='Other Cost' id='OtherCost"+RowCount+"' name='OtherCost' data-mini='true'/></td>"+
			"<td><input type='text' placeholder='Promotion Cost' id='PromotionCost"+RowCount+"' name='PromotionCost' data-mini='true'  /></td>"+
			"<td><input type='text' placeholder='Marginal Contribution' id='MarginalContribution"+RowCount+"' name='MarginalContribution' data-mini='true' class='ui-disabled' /></td>"+
			
			"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"ProductPromotionsDeleteRow('ProductPromotionsDynamicTableRows_"+RowCount+"')\">Delete</a></td>"+
		"</tr>";
		
		$("#ProductPromotionsTableBody").append(content).trigger('create');
		
		RowCount++;
		
		$('#NoProductRow').css('display', 'none');
		
		$('#BrandExchangePackage').val('');
		$('#ProductPromotionsProductCodeIssue').val('');
		$('#ProductPromotionsRawCases').val('');
		$('#ProductPromotionsUnits').val('');
		
		$('#BrandExchangePackage').selectmenu("refresh");
		$('#ProductPromotionsProductCodeIssue').selectmenu("refresh");	
		
		
		$('#ProductPromotionsSave').removeClass('ui-disabled');
		$('#SpanProductPromotionsIssue').html("");
		
		
	}
	
}

function AddSalesToTablePromotions()
{
	//alert();
	var IsValid = false;
	var IsAnyChecked = false;
	
	$('#SpanProductPromotionsIssuePromotions input[type=checkbox]').each(function () {
		if(this.checked)
		{
			IsAnyChecked = true;
		}
	});
	
	if(!IsAnyChecked)
	{IsValid = true;}
	
	
	if( $('#ProductPromotionPPackage').val() == "" ){
		document.getElementById('ProductPromotionPPackage').focus();
		IsValid = true;
	}
	
	
	if( $('#ProductPromotionsPRawCases').val() == "" && $('#ProductPromotionsPUnits').val() == ""){
		document.getElementById('ProductPromotionsPRawCases').focus();
		IsValid = true;
	}
	
	var PackageArray = new Array();
	var BrandArray = new Array();
	PackageArray = document.getElementsByName("ProductPromotionsPMainFormPackage");
	BrandArray = document.getElementsByName("ProductPromotionsPMainFormProductIDIssue");
	if(PackageArray.length>0 && BrandArray.length>0)
	{
		for(var i=0;i<PackageArray.length;i++)
		{
			//alert(PackageArray[i].value);
			if(($("#ProductPromotionPPackage option:selected").val() == PackageArray[i].value) && ($("#ProductPromotionsPProductCodeIssue option:selected").val() == BrandArray[i].value)) //if same package and same brand already exist
			{
				IsValid = true;
			}
		}
		
	}
	
	
	
	if(!IsValid)
	{

		var BrandListName = "";
		var BrandListHiddenFields ="";
		$('#SpanProductPromotionsIssuePromotions input[type=checkbox]').each(function () {		    
			if(this.checked && $('label[for="'+this.id+'"]').text().trim()!="Select All")
			{
				BrandListName +=$('label[for="'+this.id+'"]').text().trim()+"<br/>";
				BrandListHiddenFields += "<input type='hidden' name='PromotionBrand"+$("#ProductPromotionPPackage").val()+"' value='"+$(this).val()+"'>";
			}
		});
		
		var content = ""+
		"<tr id='ProductPromotionsPDynamicTableRows_"+RowCount1+"'>"+
			"<td>"+$("#ProductPromotionPPackage option:selected").text()+
			"<input type='hidden' name='ProductPromotionsPMainFormPackage' value='"+$("#ProductPromotionPPackage").val()+"' /></td>"+			
			
			"<td>"+BrandListName+
			BrandListHiddenFields+
			
			"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"ProductPromotionsDeleteRow('ProductPromotionsPDynamicTableRows_"+RowCount1+"')\">Delete</a></td>"+
		"</tr>";
		
		$("#ProductPromotionsTableBody1").append(content).trigger('create');
		
		RowCount1++;
		
		$('#NoProductRow1').css('display', 'none');
		
		$('#ProductPromotionPPackage').val('');
		$('#ProductPromotionsPProductCodeIssue').val('');
		$('#ProductPromotionsPRawCases').val('');
		$('#ProductPromotionsPUnits').val('');
		
		$('#ProductPromotionPPackage').selectmenu("refresh");
		$('#ProductPromotionsPProductCodeIssue').selectmenu("refresh");
		
		$('#SpanProductPromotionsIssuePromotions').html("");
	}
	
}

function ProductPromotionsSubmit()
{
	//if($("#ProductPromotionsPProductCodeIssue").val()=="")
	//{ alert();}
	//alert();
	
	
	if($("#DistributorID2").val().trim()!=""){
	
		$("#ProductPromotionsSave").addClass("ui-disabled");
		$.ajax({    		
				url: "inventory/ProductDiscountsRequestExecute",    			
				    data: $("#ProductPromotionsMainForm" ).serialize(),
				    type: "POST",
				    dataType : "json",
				success:function(json){
					if(json.success == "true"){    				
						//window.location="ProductPromotions.jsp";
						ProductPromotionsAdditionalRightSubmit(json.product_promotion_id);
						
					}else{
						alert(json.error);
						
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
				}
			});
	}else{
		alert("Please select distributor");
	}
}

function LoadPerticularProductPromotions(ProductPromotionIdd)
{	
	$("#isEditCase").val("1"); //edit case
	$.ajax({
	    url: "inventory/ProductDiscountGetProductDiscountInfoJson",
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
	    		//$('#ProductPromotionsSave').removeClass('ui-disabled');
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
	//$("#ProductPromotionsSave").removeClass('ui-disabled');
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
	    url: "inventory/ProductDiscountsRequestAdditionalRightExecute",
	    
	    data: $("#ProductPromotionsMainForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){		    		
	    		window.location="ProductDiscountsRequest.jsp";
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
		
		url: "inventory/ProductDiscountsDeactivateExecute",
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

function CalculateMarginalContribution(RowID){
	//alert(RowID);
	//$("#MarginalContribution"+RowID).val($("#FreeSKUPriceBottles"+RowID).val()-$("#VariableCost"+RowID).val());
}

function CalculateNetPrice(){
	$("#netprice"+RowID).val($("#SalesSKUPrice"+RowID).val()-$("#FreeSKUPriceBottles"+RowID).val());
}

function CustomValidation(){
	if($("#ProductPromotionsLabel").val()!="" && $("#ProductPromotionsValidFrom").val()!="" && $("#ProductPromotionsValidTo").val()!=""){
		$('#ProductPromotionsSave').removeClass('ui-disabled');
	}
}

