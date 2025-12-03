var RowCount = 0;
var RowCount1 = 0;
var isAddable = true;

$(document).delegate("#BrandExchange", "pageshow", function() {

	$('#BrandPriceListSave').removeClass('ui-disabled');

	$("#BrandExchangePackage").change(function(event) {
		getBrandList($("#BrandExchangePackage").val());

	});

});

function getBrandList(PackageID) {
	$.mobile.showPageLoadingMsg();

	if (PackageID != "") {
		$
				.ajax({
					url : "inventory/GetBrandListJson",
					data : {
						PackageID : PackageID
					},
					type : "POST",
					dataType : "json",
					success : function(json) {

						if (json.exists == "true") {

							var regex = new RegExp('select_id', 'g');

							var BrandSelectLis = "";
							var x = 0;
							for (var i = 0; i < json.rows.length; i++) {

								BrandSelectLis += '<input type="checkbox" name="PromotionBrandList" id="PromotionBrandList_'
										+ x
										+ '" value="'
										+ json.rows[i].id
										+ '" style="width:80%">'
										+ '<label for="PromotionBrandList_'
										+ x
										+ '" >'
										+ json.rows[i].label
										+ ' </label>';
								x++;
							}

							$('#SpanProductBaePriceList').html(BrandSelectLis);
							$('#SpanProductBaePriceList').trigger('create');

							$.mobile.hidePageLoadingMsg();

						} else {

						}
					},
					error : function(xhr, status) {
						alert("Server could not be reached.");
					}
				});
	}

}
function isInteger(o) {

	return !isNaN(o - 0) && o != null && o.indexOf('.') == -1;
}

function AddSalesToTable() {

	if ($('#ProductPromotionsRawCases').val() != "") {

		var BrandArray = new Array();
		BrandArray = document.getElementsByName("PromotionBrandList");
		if (BrandArray.length > 0) {
			$('#ProductPromotionsSave').removeClass('ui-disabled');

		}

	}

}

function BasePriceListSubmit() {
	if ($('#BasePriceListValidFrom').val() == "") {
		alert("Please Select Start Date");
	} else {

		{

			{
				console.log($("#ProductPromotionsMainForm").serialize());
				$.ajax({
					url : "inventory/BasePriceListExecute",
					data : $("#ProductPromotionsMainForm").serialize(),
					type : "POST",
					dataType : "json",
					success : function(json) {
						if (json.success == "true") {
							alert("Data Saved Successfully");
							window.location = "BasePriceList.jsp";

						} else {
							alert(json.error);

						}
					},
					error : function(xhr, status) {
						alert("Server could  not be reached.");
					}
				});

			}

		}

	}

}
