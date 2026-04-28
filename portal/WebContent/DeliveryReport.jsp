<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/DeliveryReport.js?1=123"></script>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(40, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>

<div data-role="header" data-id="DeliveryHeader" data-theme="d">
    <h1>DELIVERY REPORT</h1>
    <a href="DeliveryReportMain.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    
</div>

<div data-role="content" data-theme="d">
<!-- <ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li> -->

	
<table style="width: 100%;" cellpadding="0" cellspacing="0">
	<tr>		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			<thead>
		    <tr style="font-size: 12px;">		    	
				<th data-priority="2" style="text-align:left;">Distributor</th>
				<th data-priority="2" style="text-align:left;">Payment Method</th>
				<th data-priority="3" style="text-align:left;">Sale Order #</th>
				<th data-priority="4" style="text-align:left;">Vehicle #</th>
				<th data-priority="1" style="text-align:left;">Remarks</th>
				<th data-priority="persist" style="text-align:left;">Date</th>
				<th data-priority="persist" style="text-align:left;">User</th>
				<th data-priority="persist" style="text-align:left;">Status</th>
				<th data-priority="persist" style="text-align:left;">Invoice #</th>
				<th data-priority="persist"></th>
		    </tr>
		  </thead>

            <% 
            DeliveryNote bean = new DeliveryNote();
            
            String params = request.getParameter("params");
            
            int WarehouseID = Utilities.parseInt(request.getParameter("LiftingReportMainWarehouseID"));
            int Region = Utilities.parseInt(request.getParameter("LiftingReportMainRegion"));
            long Distributor = Utilities.parseLong(request.getParameter("LiftingReportMainDistributor"));
            long InvoiceNumber = Utilities.parseLong(request.getParameter("LiftingReportMainInvoiceNumber"));
            int PaymentMethod = Utilities.parseInt(request.getParameter("LiftingReportMainPaymentMethod"));
            String SaleOrderNumber = Utilities.filterString(request.getParameter("LiftingReportMainSaleOrderNumber"), 1, 100);
            String VehicleNumber = Utilities.filterString(request.getParameter("LiftingReportMainVehicleNumber"), 1, 100);
            int VehicleType = Utilities.parseInt(request.getParameter("LiftingReportMainVehicleType"));
            String Remakrs = Utilities.filterString(request.getParameter("LiftingReportMainRemakrs"), 1, 100);
            String BatchCode = Utilities.filterString(request.getParameter("LiftingReportBatchCode"), 1, 100);
            long UserSAPCode = Utilities.parseLong(request.getParameter("LiftingReportUser"));
            
            String Status = Utilities.filterString(request.getParameter("LiftingReportStatus"), 1, 100);
            
            int FromDateHour = Utilities.parseInt(request.getParameter("LiftingReportMainFromDateHour"));
            int FromDateMinutes = Utilities.parseInt(request.getParameter("LiftingReportMainFromDateMinutes"));
            Date FromDate = Utilities.parseDateTime(request.getParameter("LiftingReportMainFromDate"), FromDateHour, FromDateMinutes);
            
            int ToDateHour = Utilities.parseInt(request.getParameter("LiftingReportMainToDateHour"));
            int ToDateMinutes = Utilities.parseInt(request.getParameter("LiftingReportMainToDateMinutes"));
            Date ToDate = Utilities.parseDateTime(request.getParameter("LiftingReportMainToDate"), ToDateHour, ToDateMinutes);
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            DocumentHeader list[] = bean.getDocumentList(FromDate, ToDate, Distributor, SaleOrderNumber, VehicleNumber, Remakrs, PaymentMethod, BatchCode, Status, UserSAPCode, InvoiceNumber, VehicleType, Region, WarehouseID,SessionUserID);
            
            for( DocumentHeader item: list ){
            	
            	%>
            	
            	
            	<tr style="font-size: 12px;">					
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= item.DISTRIBUTOR_ID %> - <%= item.DISTRIBUTOR_NAME %></td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec">
					<%
						if( item.PAYMENT_METHOD_LABEL != null && item.PAYMENT_METHOD_LABEL.length() > 0 ){
							out.print(item.PAYMENT_METHOD_LABEL);
						}	
					%>
					 </td>
					 
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%
							if (item.SALE_ORDER_NUMBER !=null &&  !item.SALE_ORDER_NUMBER.isEmpty()){
								out.print(item.SALE_ORDER_NUMBER);
							}
						%>
					</td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%
							if (item.VEHICLE_NUMBER !=null &&  !item.VEHICLE_NUMBER.isEmpty()){
								out.print(item.VEHICLE_NUMBER);
							}
						%></td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= item.REMARKS %></td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= Utilities.getDisplayDateFormat(item.CREATED_ON) %></td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= item.CREATED_BY %></td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= item.STATUS %></td>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%= item.INVOICE_NUMBER %></td>
					<%
					if(Utilities.isAuthorized(34, SessionUserID) == true){
						
					
					%>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a data-role="button" data-mini="true" data-theme="c" id="DeliveryNoteButton" onClick="OpenDeliveryNoteVoucher(<%=item.DOCUMENT_ID%>)">View</a></td>
					<% 
					}else{
					%>
					<td style="text-align:left; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><a data-role="button" data-mini="true" data-theme="c" id="DeliveryNoteButton" onClick="OpenDeliveryNoteVoucherPrint(<%=item.DOCUMENT_ID%>)">View</a></td>
					<%
					}
					%>
		    	</tr>
            	
				
            	<%
            	
            }
            
            bean.close();
            %>
            </table>
		</td>
	</tr>
		<form method="post" action="DeliveryNote.jsp" id="DeliveryReportViewForm" target="_blank" >
		<input type="hidden" name="DeliveryID" id="DeliveryIDHiddenID"/>
		</form>  
		
		<form method="post" action="DeliveryNotePrintWithoutHeader.jsp" id="DeliveryReportPrintForm"  target="_blank">
		<input type="hidden" name="DeliveryID" id="DeliveryIDHiddenIDForPrint"/>
		</form>  
        </table>