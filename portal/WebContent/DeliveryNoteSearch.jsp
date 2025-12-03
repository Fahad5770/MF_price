<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>


        	<ul data-role="listview" data-inset="true"> 
        	
            <%
            DeliveryNote bean = new DeliveryNote();
            Date FromDate = Utilities.parseDate(request.getParameter("FromDate"));
            Date ToDate = Utilities.parseDateTime(request.getParameter("ToDate"), 23, 59);
            
            int WarehouseID = Utilities.parseInt(request.getParameter("DeliveryNoteWarehouseID"));
            
            Date LastDate = Utilities.parseDate("01/01/1997");
            
            DocumentHeader list[] = bean.getDocumentList(FromDate, ToDate, 0, "", "", "", 0, "", "", 0, 0, 0, 0, WarehouseID,0);
            boolean isFound = false;
            for( DocumentHeader item: list ){
            	isFound = true;
            	%>
            	
            	<% 
            	if(!DateUtils.isSameDay(LastDate, item.CREATED_ON) ){
            		%>
            		<li data-role="list-divider"><%=Utilities.getDisplayFullDateFormat(item.CREATED_ON)%></li>
            		<%
            	}
            	%>
            	<li><a data-ajax="false" href="DeliveryNote.jsp?DeliveryID=<%=item.DOCUMENT_ID%>">
					<span style="font-size: 10pt; font-weight: 400;"><%=item.DISTRIBUTOR_NAME%><br><%=item.REMARKS%></span>
					<span class="ui-li-count"><%=Utilities.getDisplayTimeFormat(item.CREATED_ON)%></span>
					</a>
				</li>
				
            	<%
            	LastDate = item.CREATED_ON;
            }
            
            if(isFound == false){
            	%>
            		<li>No result found.</li>
            	<%
            }
            
            bean.close();
            %>
        	</ul>