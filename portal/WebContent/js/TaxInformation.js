$(document).on("click", "#SalesTaxSubmit", function () {
    $.ajax({
        url: "product/SalesTaxServlet",
        type: "POST",
        data: {
            FR: $("#SalesFR").val(),
            FUR: $("#SalesFUR").val(),
            NFR: $("#SalesNFR").val(),
            NFUR: $("#SalesNFUR").val(),
            created_by: $("#UserID").val()
        },
        success: function () {
            alert("Sales Tax Saved");
            location.reload(); // simpler & safer
        },
        error: function () {
            alert("Error saving Sales Tax");
        }
    });
});


$(document).on("click", "#IncomeTaxSubmit", function () {
    $.ajax({
        url: "product/IncomeTaxServlet",
        type: "POST",
        data: {
            FR: $("#IncomeFR").val(),
            FUR: $("#IncomeFUR").val(),
            NFR: $("#IncomeNFR").val(),
            NFUR: $("#IncomeNFUR").val(),
            created_by: $("#UserID").val()
        },
        success: function () {
            alert("Income Tax Saved");
            location.reload();
        },
        error: function () {
            alert("Error saving Income Tax");
        }
    });
});