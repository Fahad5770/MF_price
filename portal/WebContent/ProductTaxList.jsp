<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>

<jsp:useBean id="bean" class="com.pbc.inventory.DeliveryNote" scope="page"/>
<jsp:setProperty name="bean" property="*"/>

<%
    long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

    if(Utilities.isAuthorized(490, SessionUserID) == false){
        response.sendRedirect("AccessDenied.jsp");
    }

    Datasource ds = new Datasource();
    ds.createConnection();

    Statement s = ds.createStatement();
    Statement s1 = ds.createStatement();
%>

<html>
<head>
    <jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
    <script src="js/lookups.js"></script>    
</head>

<body>

<script>
function SaveProductTaxList() {
    // Make the AJAX call
        $.mobile.showPageLoadingMsg();
    $.ajax({
        url: "product/ProductTaxListExecute",
        method: "POST",
        data: $("#PriceTaxList").serialize(),
        dataType: "json",
        success: function(response) {
        	$.mobile.hidePageLoadingMsg();
         //   console.log('Success:', response);
            alert("Product Tax Saved Successfully .. !");
            window.location='ProductTaxList.jsp';
            
        },
        error: function(xhr, status, error) {
        	 $.mobile.hidePageLoadingMsg();
           // console.error('Error:', error);
            alert("An error occured .. !");
        }
    });
   
}
</script>

    <div data-role="page" id="PriceList" data-url="PriceList" data-theme="d">
        <jsp:include page="Header2.jsp">
            <jsp:param value="Product Sales Tax" name="title"/>
        </jsp:include>
        
        <div data-role="content" data-theme="d">
            <form id="PriceTaxList">    
                <ul data-role="listview" data-inset="false" data-divider-theme="c">
                    <li>
                        <input type="hidden" name="UserID" id="UserID" value="<%=SessionUserID%>"/>
                        <input type="hidden" name="FeatureIDFOrWhole" id="FeatureIDFOrWhole" value="51"/>
                    </li>
                    <div></div>
                    <li data-role="list-divider">Product Sales Tax</li>
                    <li>   
                        <table border="0" width="100%">
                            <tr>
                                <td>Product Code</td>
                                <td>Package Name</td>
                                <td>Brand Name</td>
                                <td>Register Filer</td>
                                <td>Register Non-Filer</td>
                                <td>UnRegister Filer</td>
                                <td>UnRegister Non-Filer</td>
                            </tr>
                            
                            <%
                            
                                ResultSet rs = s.executeQuery("select * from inventory_products_view");
                                while(rs.next()) {
                                	
                                    long productId =rs.getLong("product_id");
                                    int productCode = rs.getInt("sap_code");
                                    double registerFiler = 0;
                                	double registerNonFiler = 0;
                                	double unregisterFiler = 0;
                                	double unregisterNonFiler = 0;
                                    ResultSet rsDetails = s1.executeQuery("select * from inventory_product_tax_rates where product_id = "+productId+" ");
                                   
                                    if(rsDetails.first()) {
                                    	
                                    	 registerFiler = rsDetails.getDouble("register_filer");
                                    	 registerNonFiler = rsDetails.getDouble("register_non_filer");
                                    	 unregisterFiler = rsDetails.getDouble("unregister_filer");
                                    	 unregisterNonFiler = rsDetails.getDouble("unregister_non_filer");
                                    }
                            %>
                            
                            <tr>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="Product Code" id="PriceLTaxSapCode" name="PriceLTaxSapCode" data-mini="true" value="<%=productCode%>" readonly="readonly" tabindex="-1">
                                    <input type="hidden" name="PriceListProductCode" id="PriceListProductCode" value="<%=productCode%>"/>
                                </td>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="Product Name" id="PriceTaxPackageName" name="PriceTaxPackageName" data-mini="true" value="<%=rs.getString("package_label")%>" readonly="readonly" tabindex="-1">
                                </td>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="Brand Name" id="PriceTaxBrandName" name="PriceTaxBrandName" data-mini="true" value="<%=rs.getString("brand_label")%>" readonly="readonly" tabindex="-1">
                                </td>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="Register Filer" id="PriceTaxRegisterFiler" name="PriceTaxRegisterFiler" data-mini="true" value="<%=registerFiler%>" tabindex="-1">
                                </td>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="Register Non-Filer" id="PriceTaxRegisterNonFiler" name="PriceTaxRegisterNonFiler" data-mini="true" value="<%=registerNonFiler%>" tabindex="-1">
                                </td>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="UnRegister Filer" id="PriceTaxUnRegisterFiler" name="PriceTaxUnRegisterFiler" data-mini="true" value="<%=unregisterFiler%>" tabindex="-1">
                                </td>
                                <td valign="top" style="width:14%">
                                    <input type="text" placeholder="UnRegister Non-Filer" id="PriceTaxUnRegisterNonFiler" name="PriceTaxUnRegisterNonFiler" data-mini="true" value="<%=unregisterNonFiler%>" tabindex="-1">
                                </td>
                            </tr>
                            <%
                                    
                                }
                            %>
                        </table> 
                        </form> 
                    </li>
                </ul>
            </form>
            
            <div>
                <table style="width: 100%;">
                    <tr>
                        <td>
                            <a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DeliveryNoteSave" href="#" onClick="SaveProductTaxList();">Save</a>
                            <button data-icon="check" data-theme="b" data-inline="true" id="DeliveryNoteReset" onClick="javascript:window.location='ProductTaxList.jsp'">Reset</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</body>
</html>

<%
    s.close();
    ds.dropConnection();
    bean.close();
%>
