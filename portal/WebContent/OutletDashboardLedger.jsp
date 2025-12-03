<%@page import="com.pbc.outlet.LedgerTransaction"%>
<%@page import="com.pbc.outlet.Advance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}
%>

<div data-role="page" id="OutletDashboardLedger" data-url="OutletDashboardLedger" data-theme="d">

    <jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=bean.OUTLET.NAME %>" name="title"/>
    	<jsp:param value="2" name="tab"/>
    	<jsp:param value="<%=bean.OUTLET.ID%>" name="OutletID"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d">
        
<table data-role="table" id="table-custom-2" data-mode="columntoggle" class="ui-body-d ui-shadow table-stripe ui-responsive" data-column-btn-theme="c" data-column-btn-text="Export to Excel" data-column-popup-theme="a" style="font-size: 10pt;">
         <thead>
           <tr class="ui-bar-d">
             <th>Date</th>
             <th>Posting ID</th>
             <th>Posted By</th>             
             <th>Transaction Type</th>
             <th>Remarks</th>
             <th>Debit</th>
             <th>Credit</th>
             <th>Balance</th>
           </tr>
         </thead>
         <tbody>
         <%
        LedgerTransaction ledger[] = bean.getLedger();
        
        for (int i = (ledger.length-1); i >= 0; i--){
        %>
           <tr>
             <td><%=Utilities.getDisplayDateFormat(ledger[i].DATE) %></th>
             <td><%=ledger[i].POSTING_ID %></td>
             <td><%=ledger[i].POSTED_BY_NAME %> (<%=ledger[i].POSTED_BY%>)</td>
             <td><%=ledger[i].POSTING_TYPE_LABEL %></td>
             <td><%=ledger[i].REMARKS %></td>
             <td><%=Utilities.getDisplayCurrencyFormat(ledger[i].DEBIT)%></td>
             <td><%=Utilities.getDisplayCurrencyFormat(ledger[i].CREDIT)%></td>
             <td><%=Utilities.getDisplayCurrencyFormat(ledger[i].BALANCE)%></td>
           </tr>
		<%
        }
		%>
         </tbody>
       </table>        
    </div><!-- /content -->

    <jsp:include page="Footer1.jsp" /> <!-- /footer -->

</div>

<%
bean.close();
%>
</body>
</html>