

var RowCount = 0;
$(document).delegate("#DispatchReturnsMain", "pageinit", function() {
	//alert($("#DispatchID").val());
	//alert($("#DispatchID").val());
	$("#LoadDispatchAdjustmentSummaryDIV").html("<img src='images/snake-loader.gif'>");
	$.get('DispatchAdjustmentSummary.jsp?dispatchid=' + $("#DispatchID").val(), function(data) {
		$("#LoadDispatchAdjustmentSummaryDIV").html(data);
		$("#LoadDispatchAdjustmentSummaryDIV").trigger('create');

		var DispatchedTotalUnits = new Array();
		var AdjustedTotalUnits = new Array();
		var IsSameFlag = false;

		DispatchedTotalUnits = document.getElementsByName("DispatchedTotalUnits");
		AdjustedTotalUnits = document.getElementsByName("AdjustedTotalUnits");


		for (var i = 0; i < DispatchedTotalUnits.length; i++) {
			if (DispatchedTotalUnits[i].value != AdjustedTotalUnits[i].value) {
				IsSameFlag = true; break;
			}

		}

		/*if(!IsSameFlag) //if Dispatch and Adjusted are same
		{
			$("#SaveAdjustmentButtonDIV").removeClass("ui-disabled");
		}*/
	});

	//loadDifferenceSummary();
	/*setTimeout(
			  function() 
			  {
				  alert($("#DispatchID").val());
			  }, 5000);*/
});

function loadDifferenceSummary() {
	$("#LoadDispatchAdjustmentDifferenceDIV").html("<img src='images/snake-loader.gif'>");
	$.get('DispatchAdjustmentReconciliationSummary.jsp?dispatchid=' + $("#DispatchID").val(), function(data) {
		$("#LoadDispatchAdjustmentDifferenceDIV").html(data);
		$("#LoadDispatchAdjustmentDifferenceDIV").trigger('create');



		if (document.getElementsByName("ReconciliationSummaryProducts").length > 0) {

			$('#SaveAdjustmentButtonDIV').addClass("ui-disabled");
		} else {

			$('#SaveAdjustmentButtonDIV').removeClass("ui-disabled");
		}

	});

}


function GetAllSalesForAdjustment(id) {
	//alert(id);
	$("#DispatchID").val(id);
	document.getElementById("DispatchGenerrateForm").submit();




}

function DispatchAdjustmentAddProduct() {

	//alert($("#DispatchID").val());

	//alert($("#DispatchIDHidden").val());

	if ($('#DispatchAdjustmentProductCode').val() == "") {
		document.getElementById('DispatchAdjustmentProductCode').focus();
		return false;
	} else {
		var value = $('#DispatchAdjustmentProductCode').val();
		if (isInteger(value) == false) {
			document.getElementById('DispatchAdjustmentProductCode').focus();
			return false;
		}
	}

	if (($('#DispatchAdjustmentRawCases').val() == "" || $('#DispatchAdjustmentRawCases').val() == "0") && ($('#DispatchAdjustmentUnits').val() == "" || $('#DispatchAdjustmentUnits').val() == "0")) {
		document.getElementById('DispatchAdjustmentRawCases').focus();
		return false;
	} else {
		var value_raw_cases = $('#DispatchAdjustmentRawCases').val();
		if (value_raw_cases != "" && isInteger(value_raw_cases) == false) {
			document.getElementById('DispatchAdjustmentRawCases').focus();
			return false;
		}

		var value_units = $('#DispatchAdjustmentUnits').val();
		if (value_units != "" && isInteger(value_units) == false) {
			document.getElementById('DispatchAdjustmentUnits').focus();
			return false;
		}

	}




	$("#DispatchAdjustmentForm").css("visibility", "visible");

	var val = $('#DispatchAdjustmentProductCode').val();

	if (val != "" && val.length > 0) {

		$.mobile.showPageLoadingMsg();
		$.ajax({
			url: "common/GetProductInfo",
			data: {
				ProductCode: val
			},
			type: "POST",
			dataType: "json",
			success: function(json) {

				$.mobile.hidePageLoadingMsg();

				if (json.exists == "true") {

					var ValDeskSaleRawCases = $('#DispatchAdjustmentRawCases').val();
					if (ValDeskSaleRawCases == '') {
						ValDeskSaleRawCases = '0';
					}

					var ShellType = '';


					var isAlreadyEntered = false;


					if (isAlreadyEntered != true) {

						var ValDeskSaleUnits = $('#DispatchAdjustmentUnits').val();
						if (ValDeskSaleUnits == '') {
							ValDeskSaleUnits = '0';
						}


						if (parseInt(ValDeskSaleUnits) >= parseInt(json.UnitPerSKU)) {
							alert('Bottles should be less than units per SKU');
							document.getElementById('DDispatchAdjustmentUnits').focus();
							return false;
						}



						var RowMaxID = parseInt($('#RowMaxID').val()) + 1;

						/*var content = ""+
						"<tr id='DeskSale"+RowMaxID+"'>"+
							"<td>"+$('#DispatchReturnsProductCode').val()+"<input type='hidden' name='DispatchReturnsMainFormUnitPerSKU' value='"+json.UnitPerSKU+"'></td>"+	
							"<td>"+json.PackageLabel+"<input type='hidden' name='DispatchReturnsMainFormPackageLabel' value='"+json.PackageLabel+"' ><input type='hidden' name='PackageID' value='"+json.PackageID+"' ><input type='hidden' name='DispatchReturnsMainFormLiquidInML' value='"+json.LiquidInML+"'></td>"+						
							"<td>"+json.BrandLabel+"<input type='hidden' name='DispatchReturnsMainFormBrandLabel' value='"+json.BrandLabel+"' ><input type='hidden' name='BrandID' value='"+json.BrandID+"' ><input type='hidden' id='ProductCode' name='ProductCode' value='"+val+"'><input type='hidden' name='ProductID' value='"+json.ProductID+"'></td>"+
							"<td>"+ValDeskSaleRawCases+"<input type='hidden' name='DispatchReturnsMainFormRawCases' value='"+ValDeskSaleRawCases+"'></td>"+
							"<td>"+ValDeskSaleUnits+"<input type='hidden' name='DispatchReturnsMainFormUnits' value='"+ValDeskSaleUnits+"'></td>"+							
							"<td><a data-role='button' data-mini='true' data-icon='delete' data-iconpos='notext' data-inline='true' onClick=\"DeskSaleDeleteRow('DeskSale"+RowMaxID+"');\">Delete</a></td>"+
						"</tr>";
						
						$("#DispatchReturnsTableBody").append(content).trigger('create');*/

						RowCount++;

						//setting the flag to insert record in db
						$("#isAddClicked").val("1");

						$.ajax({

							url: "distributor/DispatchAdjustmentExecute",
							data: {
								DispatchID: $("#DispatchIDHidden").val(),
								OutletID: $("#OutletIDHidden").val(),
								InvoiceID: $("#InvoiceIDHidden").val(),
								ProductID: json.ProductID,
								RawCases: ValDeskSaleRawCases,
								Units: ValDeskSaleUnits,
								LiquInMl: json.LiquidInML,
								UnitPerSKU: json.UnitPerSKU,
								IsAddClicked: $("#isAddClicked").val()

							},
							type: "POST",
							dataType: "json",
							success: function(json) {
								if (json.success == "true") {
									$.mobile.loading('show');
									$.get('DispatchAdjustmentInvoiceSummary.jsp?invoiceid=' + $("#InvoiceIDHidden").val() + "&outletid=" + $("#OutletIDHidden").val() + "&dispatchid=" + $("#DispatchIDHidden").val(), function(data) {
										$("#LoadDispatchAdjustmentInvoiceSummaryDIV").html(data);
										$("#LoadDispatchAdjustmentInvoiceSummaryDIV").trigger('create');


										$.get('DispatchAdjustmentAddProduct.jsp?outletid=' + $("#OutletIDHidden").val() + '&dispatchid=' + $("#DispatchIDHidden").val() + '&invoiceid=' + $("#InvoiceIDHidden").val(),

											function(data) {
												$("#DispatchAdjustmentAddProductDIV").html(data);
												$("#DispatchAdjustmentAddProductDIV").trigger('create');

												$.get('DispatchAdjustmentSummary.jsp?dispatchid=' + $("#DispatchIDHidden").val(), function(data) {
													$("#LoadDispatchAdjustmentSummaryDIV").html(data);
													$("#LoadDispatchAdjustmentSummaryDIV").trigger('create');

													//			  loadDifferenceSummary();

													var DispatchedTotalUnits = new Array();
													var AdjustedTotalUnits = new Array();
													var IsSameFlag = false;

													DispatchedTotalUnits = document.getElementsByName("DispatchedTotalUnits");
													AdjustedTotalUnits = document.getElementsByName("AdjustedTotalUnits");


													for (var i = 0; i < DispatchedTotalUnits.length; i++) {
														if (DispatchedTotalUnits[i].value != AdjustedTotalUnits[i].value) {
															IsSameFlag = true; break;
														}

													}

													if (!IsSameFlag) //if Dispatch and Adjusted are same
													{
														$("#SaveAdjustmentButtonDIV").removeClass("ui-disabled");
													}
												});

											});
										$.get('DispatchAdjustmentBrandDiscount.jsp?invoiceid=' + $("#InvoiceIDHidden").val(), function(data) {
											$("#LoadDispatchAdjustmentInvoiceBrandDIV").html(data);
											$("#LoadDispatchAdjustmentInvoiceBrandDIV").trigger('create');
										});

										$.mobile.loading('hide');
									});

								} else {
									//alert("Server could not be reached");
									alert(json.error);
									$.mobile.loading('hide');

								}
							},
							error: function(xhr, status) {
								alert("Server could not be reached.");
								$.mobile.loading('hide');
							}

						});

					} else {
						document.getElementById('DispatchAdjustmentProductCode').focus();
						$.mobile.loading('hide');
					}

				} else {
					alert("Invalid Product Code");
					$('#DispatchAdjustmentProductCode').focus();
					$.mobile.loading('hide');
				}
			},
			error: function(xhr, status) {
				alert("Server could not be reached.");
				$.mobile.loading('hide');
			}
		});

	}


	//$("#SamplingReceivingBarcodeField").val("");

	return false;
}

function DispatchAdjustmentAddProductLoadPage(outletid, dispatchid, invoiceid, OutletCompName) {
	$("#DispatchIDForLastUse").val(dispatchid);
	$("#OutletNamePutHere").html(OutletCompName);

	$.mobile.loading('show');

	$("#LoadDispatchAdjustmentInvoiceSummaryDIV").html("<img src='images/snake-loader.gif'>");
	$.get('DispatchAdjustmentInvoiceSummary.jsp?invoiceid=' + invoiceid + "&outletid=" + outletid + "&dispatchid=" + dispatchid, function(data) {


		$("#LoadDispatchAdjustmentInvoiceSummaryDIV").html(data);
		$("#LoadDispatchAdjustmentInvoiceSummaryDIV").trigger('create');

		$("#DispatchAdjustmentAddProductDIV").html("<img src='images/snake-loader.gif'>");
		$.get('DispatchAdjustmentAddProduct.jsp?outletid=' + outletid + '&dispatchid=' + dispatchid + '&invoiceid=' + invoiceid, function(data) {
			$("#DispatchAdjustmentAddProductDIV").html(data);
			$("#DispatchAdjustmentAddProductDIV").trigger('create');

			$("#LoadDispatchAdjustmentSummaryDIV").html("<img src='images/snake-loader.gif'>");
			$.get('DispatchAdjustmentSummary.jsp?dispatchid=' + dispatchid, function(data) {
				$("#LoadDispatchAdjustmentSummaryDIV").html(data);
				$("#LoadDispatchAdjustmentSummaryDIV").trigger('create');

				//  loadDifferenceSummary();

				var DispatchedTotalUnits = new Array();
				var AdjustedTotalUnits = new Array();
				var IsSameFlag = false;

				DispatchedTotalUnits = document.getElementsByName("DispatchedTotalUnits");
				AdjustedTotalUnits = document.getElementsByName("AdjustedTotalUnits");


				for (var i = 0; i < DispatchedTotalUnits.length; i++) {
					if (DispatchedTotalUnits[i].value != AdjustedTotalUnits[i].value) {
						IsSameFlag = true; break;
					}

				}

				if (!IsSameFlag) //if Dispatch and Adjusted are same
				{
					//	$("#SaveAdjustmentButtonDIV").removeClass("ui-disabled");
				}
				//calling invoice summary

				$("#LoadDispatchAdjustmentInvoiceBrandDIV").html("<img src='images/snake-loader.gif'>");
				$.get('DispatchAdjustmentBrandDiscount.jsp?invoiceid=' + invoiceid, function(data) {
					$("#LoadDispatchAdjustmentInvoiceBrandDIV").html(data);
					$("#LoadDispatchAdjustmentInvoiceBrandDIV").trigger('create');
				}).fail(function() {
					$.mobile.loading('hide');
				});

			})
				.fail(function() {
					$.mobile.loading('hide');
				});
		})

			.fail(function() {
				$.mobile.loading('hide');
			});

		$("#InvoiceHeadingID").hide();
		$.mobile.loading('hide');
	})
		.fail(function() {
			$.mobile.loading('hide');
		});



}

function DispatchAdjustmentDeleteRow(rowid, dispatchid, outletid, productid, invoiceid, ispromotion, quantity) {
	$("#" + rowid).remove();
	//alert(invoiceid);
	//alert($("#isDelete").val());
	//alert();
	$("#isEditCase").val("2"); //setting the is delete flag to 1

	$.ajax({

		url: "distributor/DispatchAdjustmentExecute",
		data: {
			DispatchID: dispatchid,
			OutletID: outletid,
			ProductID: productid,
			InvoiceID: invoiceid,
			isEditCase: $("#isEditCase").val(),
			isPromotion: ispromotion,
			quantity: quantity
		},
		type: "POST",
		dataType: "json",
		success: function(json) {
			if (json.success == "true") {
				$.mobile.loading('show');
				$("#LoadDispatchAdjustmentInvoiceSummaryDIV").html("<img src='images/snake-loader.gif'>");
				$.get('DispatchAdjustmentInvoiceSummary.jsp?invoiceid=' + invoiceid + "&outletid=" + outletid + "&dispatchid=" + dispatchid, function(data) {
					$("#LoadDispatchAdjustmentInvoiceSummaryDIV").html(data);
					$("#LoadDispatchAdjustmentInvoiceSummaryDIV").trigger('create');

					$("#DispatchAdjustmentAddProductDIV").html("<img src='images/snake-loader.gif'>");
					$.get('DispatchAdjustmentAddProduct.jsp?outletid=' + outletid + '&dispatchid=' + dispatchid + '&invoiceid=' + invoiceid, function(data) {
						$("#DispatchAdjustmentAddProductDIV").html(data);
						$("#DispatchAdjustmentAddProductDIV").trigger('create');

						$("#LoadDispatchAdjustmentSummaryDIV").html("<img src='images/snake-loader.gif'>");
						$.get('DispatchAdjustmentSummary.jsp?dispatchid=' + dispatchid, function(data) {
							$("#LoadDispatchAdjustmentSummaryDIV").html(data);
							$("#LoadDispatchAdjustmentSummaryDIV").trigger('create');

							//  loadDifferenceSummary();

							var DispatchedTotalUnits = new Array();
							var AdjustedTotalUnits = new Array();
							var IsSameFlag = false;

							DispatchedTotalUnits = document.getElementsByName("DispatchedTotalUnits");
							AdjustedTotalUnits = document.getElementsByName("AdjustedTotalUnits");


							for (var i = 0; i < DispatchedTotalUnits.length; i++) {
								if (DispatchedTotalUnits[i].value != AdjustedTotalUnits[i].value) {
									IsSameFlag = true; break;
								}

							}

							if (!IsSameFlag) //if Dispatch and Adjusted are same
							{
								//$("#SaveAdjustmentButtonDIV").removeClass("ui-disabled");
							}
							else {
								$("#SaveAdjustmentButtonDIV").addClass("ui-disabled");
							}

							$("#LoadDispatchAdjustmentInvoiceBrandDIV").html("<img src='images/snake-loader.gif'>");
							$.get('DispatchAdjustmentBrandDiscount.jsp?invoiceid=' + invoiceid, function(data) {
								$("#LoadDispatchAdjustmentInvoiceBrandDIV").html(data);
								$("#LoadDispatchAdjustmentInvoiceBrandDIV").trigger('create');
							});

						});
					});
					$.mobile.loading('hide');
				});






			} else {
				alert("Server could not be reached");
				$.mobile.loading('hide');

			}
		},
		error: function(xhr, status) {
			alert("Server could not be reached.");
			$.mobile.loading('hide');
		}

	});

	var DispatchedTotalUnits1 = document.getElementsByName("DispatchedTotalUnits");
	if (DispatchedTotalUnits1.length == 0) {
		$("#SaveAdjustmentButtonDIV").addClass("ui-disabled");
	}

}


function SaveDispatchAdjustmentInDB() {


	$.mobile.loading('show');
	$.ajax({

		url: "distributor/DispatchAdjustmentUpdateExecute",
		data: {
			DispatchID: $("#DispatchIDForLastUse").val()
		},
		type: "POST",
		dataType: "json",
		success: function(json) {
			if (json.success == "true") {
				location = "DispatchAdjustmentMain.jsp";
				$.mobile.loading('hide');

			} else {
				alert("Server could not be reached");
				$.mobile.loading('hide');

			}
		},
		error: function(xhr, status) {
			alert("Server could not be reached.");
			$.mobile.loading('hide');
		}

	});
}

function changeTab(PageNo) {
	var url_str = "";
	if (PageNo == '1') {
		url_str = "DispatchAdjustment.jsp";
	} else {
		url_str = "DeskSaleExtraLoad.jsp";
	}

	document.getElementById("PassParams").action = url_str;
	document.getElementById("PassParams").submit();

}