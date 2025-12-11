<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page" />
<jsp:setProperty name="bean" property="*" />

<%
String PageID = "ProductTax";

if (session.getAttribute("UserID") == null) {
	response.sendRedirect("index.jsp");
	return;
}

String UserID = (String) session.getAttribute("UserID");
long SessionUserID = Long.parseLong(UserID);
int FeatureID = 509;

if (!Utilities.isAuthorized(FeatureID, SessionUserID)) {
	response.sendRedirect("AccessDenied.jsp");
	return;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="include/StandaloneSrc.jsp" />

    <!-- FineUploader CSS/JS -->
    <link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
    <script src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js"></script>

    <!-- Custom JS -->
    <script src="js/OrderBookerTargets.js"></script>
    <script src="js/lookups.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script>
        $(document).ready(function() {

            // Load taxes on product selection
            $("#Product").change(function() {
                var productId = $(this).val();
                if (productId === "") return;
                $.ajax({
                    url: "inventory/AddSalesTax",
                    type: "GET",
                    data: { product_id: productId },
                    dataType: "json",
                    success: function(data) {
                        // SALES TAX
                        $("input[name='sales_tax_reg_filer']").val(data.sales_tax_reg_filer || '0.0');
                        $("input[name='sales_tax_reg_nonfiler']").val(data.sales_tax_reg_nonfiler || '0.0');
                        $("input[name='sales_tax_unreg_filer']").val(data.sales_tax_unreg_filer || '0.0');
                        $("input[name='sales_tax_unreg_nonfiler']").val(data.sales_tax_unreg_nonfiler || '0.0');

                        // INCOME TAX
                        $("input[name='income_tax_reg_filer']").val(data.income_tax_reg_filer || '0.0');
                        $("input[name='income_tax_reg_nonfiler']").val(data.income_tax_reg_nonfiler || '0.0');
                        $("input[name='income_tax_unreg_filer']").val(data.income_tax_unreg_filer || '0.0');
                        $("input[name='income_tax_unreg_nonfiler']").val(data.income_tax_unreg_nonfiler || '0.0');
                    },
                    error: function(xhr, status, error) {
                        console.log(xhr.responseText);
                        alert("Failed to load tax values.");
                    }
                });
            });

            // Save button click
            $("#ProductTaxSave").click(function(e) {
                e.preventDefault();

                var productId = $("#Product").val();
                if (productId === "") {
                    alert("Please select a product first!");
                    return;
                }

                var formData = $("#DistributorTargetsMainForm").serialize();

                $.ajax({
                    url: "inventory/AddSalesTax",
                    type: "POST",
                    data: formData,
                    dataType: "json",
                    success: function(resp) {

                        if (resp.status === "success") {
                            alert(resp.message);
                            location.reload(); // reload page
                        } else {
                            alert("Error: " + resp.message);
                        }
                    },
                    error: function(xhr, status, error) {
                        console.log(xhr.responseText);
                        alert("An error occurred while saving tax data.");
                    }
                });
            });

        });
    </script>
</head>

<body>
    <div data-role="page" id="PSRTargets" data-theme="d">

        <jsp:include page="Header2.jsp">
            <jsp:param name="title" value="Add Product Tax" />
        </jsp:include>

        <div data-role="content" data-theme="d">
            <form id="DistributorTargetsMainForm" name="DistributorTargetsMainForm">
                <ul data-role="listview" data-inset="false">
                    <li>
                        <input type="hidden" name="EditID" id="EditID">

                        <table style="width: 570px;">
                            <tr>
                                <td>
                                    <select name="Product" id="Product" style="width: 250px;">
                                        <option value="">Select Product</option>
                                        <%
                                            ResultSet rs = s.executeQuery(
                                                "SELECT brand_label, package_label, product_id FROM inventory_products_view WHERE is_visible = 1 ORDER BY brand_label, package_label"
                                            );
                                            while (rs.next()) {
                                                int productId = rs.getInt("product_id");
                                                String label = rs.getString("brand_label") + " - " + rs.getString("package_label");
                                        %>
                                        <option value="<%=productId%>"><%=label%></option>
                                        <%
                                            }
                                            rs.close();
                                        %>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <br><br>

                        <table>
                            <tr>
                                <th></th>
                                <th>Sales Tax</th>
                                <th>Income Tax</th>
                            </tr>
                            <tr>
                                <td>Registered & Filer</td>
                                <td><input type="text" name="sales_tax_reg_filer" value="0.0"></td>
                                <td><input type="text" name="income_tax_reg_filer" value="0.0"></td>
                            </tr>
                            <tr>
                                <td>Registered & Non-Filer</td>
                                <td><input type="text" name="sales_tax_reg_nonfiler" value="0.0"></td>
                                <td><input type="text" name="income_tax_reg_nonfiler" value="0.0"></td>
                            </tr>
                            <tr>
                                <td>Unregistered & Filer</td>
                                <td><input type="text" name="sales_tax_unreg_filer" value="0.0"></td>
                                <td><input type="text" name="income_tax_unreg_filer" value="0.0"></td>
                            </tr>
                            <tr>
                                <td>Unregistered & Non-Filer</td>
                                <td><input type="text" name="sales_tax_unreg_nonfiler" value="0.0"></td>
                                <td><input type="text" name="income_tax_unreg_nonfiler" value="0.0"></td>
                            </tr>
                        </table>
                    </li>
                </ul>
            </form>

            <div data-role="footer" data-position="fixed" data-theme="b">
                <div>
                    <table style="width: 100%;">
                        <tr>
                            <td>
                                <a data-icon="check" data-theme="a" data-role="button"
                                   data-inline="true" id="ProductTaxSave" href="#">Save</a>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

        </div>
    </div>

    <%
        ds.dropConnection();
    %>
</body>
</html>
