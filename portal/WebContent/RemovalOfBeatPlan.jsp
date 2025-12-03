<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Date, java.sql.Connection, java.sql.Statement, java.sql.ResultSet, java.io.*" %>
<%@ page import="com.pbc.util.Utilities, com.pbc.util.Datasource" %>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>

<%
    String PageID = "InvoiceStatus";
    String sessionUser = (String) session.getAttribute("UserID");
    
    if (sessionUser == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    long SessionUserID = Long.parseLong(sessionUser);
    int FeatureID = 361;

    if (!Utilities.isAuthorized(FeatureID, SessionUserID)) {
        response.sendRedirect("AccessDenied.jsp");
        return;
    }

    boolean isEditCase = false;
    long EditID = Utilities.parseLong(request.getParameter("OrderBookerTargetID"));
    if (EditID > 0) {
        isEditCase = true;
    }

    Datasource ds = new Datasource();
    ds.createConnection();
    Connection c = ds.getConnection();
    Statement s = c.createStatement();
%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->

    <script>
        var isEditCase = <%=isEditCase%>;
        var EditID = <%=EditID%>;
    </script>

    <link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
    <script src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>				
    <script src="js/OrderBookerTargets.js"></script>
    <script src="js/lookups.js"></script>

    <style>
        .radio_style {
            display: block;
            width: 80px;
            height: 50px;
            background-repeat: no-repeat;
            background-position: -231px 0px;
        }
        .ui-table-reflow.ui-responsive {
            display: block;
        }
    </style>

   <script>
    $(document).delegate("#PJPUploader", "pageinit", function () {
        $(".WorkflowAttachButton").on("click", function () {
            $("#processType1").val($("#processType option:selected").val());
            $("#popup_workflow_attach").popup("open", { transition: "slidedown", positionTo: "window" });
        });

        var manualuploader = $('#manual-fine-uploader').fineUploader({
            autoUpload: false,
            text: {
                uploadButton: '<i class="icon-plus icon-white"></i> Select Files'
            },
            request: {
                endpoint: 'employee/RemovalOfBeatPlan',
                paramsInBody: true,
                params: {
                    RequestID: 123,
                    processType: function () {
                        return $("#processType1").val();
                    }
                }
            }
        })
        .on('complete', function (event, id, name, responseJSON) {
            if (responseJSON.Success === 'true') {
                alert(responseJSON.Message || "Beat Plan removed successfully");
            } else {
                alert(responseJSON.Message || "Could not process the file");
            }
            window.location = "RemovalOfBeatPlan.jsp";
        });

        $('#triggerUpload').click(function () {
            manualuploader.fineUploader('uploadStoredFiles');
        });
    });
</script>

</head>

<body>
<div data-role="page" id="PJPUploader" data-url="PJPUploader" data-theme="d">

    <jsp:include page="Header2.jsp">
        <jsp:param value="Removal of Beat Plan" name="title"/>
    </jsp:include>

    <div data-role="content" data-theme="d">
        <form action="test2.jsp" name="DistributorTargetsMainForm" id="DistributorTargetsMainForm">
            <ul data-role="listview" data-inset="false" data-divider-theme="c">
                <li>
                    <input type="hidden" name="EditID" id="EditID" value="<%=EditID%>">
                    <table style="width:50%;margin-top:1.5%;" border="0">
                        <tr>
                            <td style="width:20px; font-weight:normal;">Select CSV file to Remove Beat Plan</td>
                            <td style="width:30%; font-weight:normal;">
                                <a href="#" style="width:250px;" 
                                   data-role="button" data-icon="arrow-u" data-corners="false" data-theme="b"  
                                   class="WorkflowAttachButton">Select File</a>
                            </td>
                        </tr>
                    </table>
                </li>
            </ul>   
        </form> 
    </div>

    <!-- File upload popup -->
    <div data-role="popup" id="popup_workflow_attach" data-overlay-theme="a" data-theme="c" 
         data-dismissible="false" style="max-width:600px;min-width:500px;max-height:500px;" class="ui-corner-all">
        <div data-role="header" data-theme="b" class="ui-corner-top">
            <h1>Attach Files</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
            <div id="manual-fine-uploader"></div>
            <br>
            <input type="hidden" id="processType1" />
            <button id="triggerUpload" data-role="button" data-inline="true" data-theme="b">Attach</button>
            <a href="#<%= PageID %>" data-role="button" data-inline="true" data-theme="c">Close</a>
        </div>
    </div> 

    <!-- Footer -->
    <div data-role="footer" data-position="fixed" data-theme="b">
        <table style="width: 100%;">
            <tr><td></td></tr>
        </table>
    </div>

    <jsp:include page="LookupEmployeeSearchPopup.jsp"> 
        <jsp:param value="EmployeeSearchCallBack" name="CallBack" />
    </jsp:include>

    <!-- Search Popup -->
    <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" 
         data-dismissible="true" style="min-width:700px; overflow-y:auto; min-height:600px; max-height:600px" 
         class="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
            <form data-ajax="false" id="DistributorTargetsSearch" onsubmit="return showSearchContent()">
                <table>
                    <tr>
                        <td><input type="text" name="SearchEmployeeID" id="SearchEmployeeID" 
                                   data-mini="true" placeholder="Employee ID" onchange="getOrderBookerName22()" size="12"></td>
                        <td><input type="text" name="SearchEmployeeName" id="SearchEmployeeName" 
                                   data-mini="true" placeholder="Employee Name" readonly="readonly"></td>
                        <td><input type="submit" value="Search" data-mini="true"></td>
                    </tr>
                </table>
            </form>
            <div id="SearchContent" style="padding: 5px"></div>
        </div>
    </div>
</div>
</body>
</html>

<%
    s.close();
    ds.dropConnection();
%>
