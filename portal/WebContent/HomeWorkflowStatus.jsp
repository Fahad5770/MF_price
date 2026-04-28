<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
		

    <%
    long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
    int days = Integer.parseInt(request.getParameter("days"));
    
    
    int TypeID = Integer.parseInt(request.getParameter("TypeID")); 
    System.out.println("TypeID "+TypeID);
    
    Workflow workflow = new Workflow();
    
    if (TypeID == 1){
	    WorkflowDocument items[] = workflow.getActionAwaited(SessionUserID);
	    if (items.length > 0){
	    %>

	    <ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search workflow requests...">
	    <li data-role="list-divider" data-theme="d">Action Awaited<span class="ui-li-count"><%if (items.length > 0){%><%=items.length %><%}else{%>There are no action items<%}%></span></li>
	    <%
	    }
	    for (int i = 0; i < items.length; i++){
	    	String url = "";
	    	boolean isAjax = false;
	    	if (items[i].PROCESS_ID == 1){
	    		url = "WorkflowManagerRegisterOutlet.jsp?requestID="+items[i].REQUEST_ID+"&processID=1";
	    	//	isAjax = true;
	    	}else if (items[i].PROCESS_ID == 2){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerMonthlyDiscount.jsp?processID=2&requestID="+items[i].REQUEST_ID;
	    	
	    	}else if (items[i].PROCESS_ID == 3){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerPromotionRequest.jsp?processID=3&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 4){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 5){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerCreditLimitRequest.jsp?processID=5&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 6){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerTOTCoolerInjectionRequest.jsp?processID=6&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 8){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerPerCaseDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 9){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerEmptyCreditLimitRequest.jsp?processID=9&requestID="+items[i].REQUEST_ID;
	    		
	    	}

	    	//System.out.println("Here");
	    %> 
	    <li><a href="<%=url %>" data-transition="flow" <%if (isAjax == false){ %>data-ajax="false"<%} %> >
	        <h2><%=items[i].PROCESS_NAME%><span style="font-weight: 400; font-size: 13px;"> for <%=items[i].CURRENT_STEP.ACTION_LABEL%></span></h2>
	        <%=workflow.getRequestPreview(items[i].PROCESS_ID, items[i].REQUEST_ID) %>
	        <p class="ui-li-aside"><span style="font-weight: 400; font-size: 13px">Request ID: <%=items[i].REQUEST_ID%><br><br><%=items[i].LAST_STEP.ACTION_LABEL_PAST%> by <%=items[i].LAST_STEP.USER_DISPLAY_NAME%><br><%=Utilities.getDisplayDateTimeFormat(items[i].CREATED_ON)%></span></p>
	    </a></li> 
	    <%
		}
    }
    if (TypeID == 2){
	    WorkflowDocument items[] = workflow.getInProcess(SessionUserID, days);
	    if (items.length > 0){
	    %>
	    <ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search workflow requests...">
	    <li data-role="list-divider" data-theme="d">In Process<span class="ui-li-count"><%if (items.length > 0){%><%=items.length %><%}else{%>There are no documents in process that need your attention<%}%></span></li>
	    <%
	    }
	    for (int i = 0; i < items.length; i++){
	    	
	    	String url = "";
	    	boolean isAjax = false;
	    	if (items[i].PROCESS_ID == 1){
	    		url = "WorkflowManagerRegisterOutlet.jsp?requestID="+items[i].REQUEST_ID+"&processID=1";
	    		//isAjax = true;
	    	}else if (items[i].PROCESS_ID == 2){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerMonthlyDiscount.jsp?processID=2&requestID="+items[i].REQUEST_ID;
	    	
	    	}else if (items[i].PROCESS_ID == 3){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerPromotionRequest.jsp?processID=3&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 4){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 5){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerCreditLimitRequest.jsp?processID=5&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 6){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerTOTCoolerInjectionRequest.jsp?processID=6&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 8){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerPerCaseDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 9){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerEmptyCreditLimitRequest.jsp?processID=9&requestID="+items[i].REQUEST_ID;
	    		
	    	}


	    %> 
	    <li><a href="<%=url %>" data-transition="flow" <%if (isAjax == false){ %>data-ajax="false"<%} %> >
	        <h2><%=items[i].PROCESS_NAME%><span style="font-weight: 400; font-size: 13px;"> sent to <%=items[i].CURRENT_STEP.USER_DISPLAY_NAME%> for <%=items[i].CURRENT_STEP.ACTION_LABEL%></span></h2>
	        <%=workflow.getRequestPreview(items[i].PROCESS_ID, items[i].REQUEST_ID) %>
	        <p class="ui-li-aside"><span style="font-weight: 400; font-size: 13px">Request ID: <%=items[i].REQUEST_ID%><br><br><%=items[i].LAST_STEP.ACTION_LABEL_PAST%> by <%=items[i].LAST_STEP.USER_DISPLAY_NAME%><br><%=Utilities.getDisplayDateTimeFormat(items[i].CREATED_ON)%></span></p>
	    </a></li> 
	    <%

		}
    }
    if (TypeID == 3){
	    WorkflowDocument items[] = workflow.getRequested(SessionUserID, days);
	    if (items.length > 0){
	    %>
	    <ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search workflow requests...">
	    <li data-role="list-divider" data-theme="d">Requested<span class="ui-li-count"><%if (items.length > 0){%><%=items.length %><%}else{%>You havn't initiated any process<%}%></span></li>
	    <%
	    }
	    for (int i = 0; i < items.length; i++){
	    	
	    	String url = "";
	    	boolean isAjax = false;
	    	if (items[i].PROCESS_ID == 1){
	    		url = "WorkflowManagerRegisterOutlet.jsp?requestID="+items[i].REQUEST_ID+"&processID=1";
	    		//isAjax = true;
	    	}else if (items[i].PROCESS_ID == 2){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerMonthlyDiscount.jsp?processID=2&requestID="+items[i].REQUEST_ID;
	    	
	    	}else if (items[i].PROCESS_ID == 3){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerPromotionRequest.jsp?processID=3&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 4){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 5){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerCreditLimitRequest.jsp?processID=5&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 6){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerTOTCoolerInjectionRequest.jsp?processID=6&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 8){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerPerCaseDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
	    		
	    	}else if (items[i].PROCESS_ID == 9){
	    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
	    		url = "WorkflowManagerEmptyCreditLimitRequest.jsp?processID=9&requestID="+items[i].REQUEST_ID;
	    		
	    	}


	    %> 
	    <li><a href="<%=url %>" data-transition="flow" <%if (isAjax == false){ %>data-ajax="false"<%} %> >
	        <h2><%=items[i].PROCESS_NAME%><span style="font-weight: 400; font-size: 13px;"> sent to <%=items[i].CURRENT_STEP.USER_DISPLAY_NAME%> for <%=items[i].CURRENT_STEP.ACTION_LABEL%></span></h2>
	        <%=workflow.getRequestPreview(items[i].PROCESS_ID, items[i].REQUEST_ID) %>
	        <p class="ui-li-aside"><span style="font-weight: 400; font-size: 13px">Request ID: <%=items[i].REQUEST_ID%><br><br><%=items[i].LAST_STEP.ACTION_LABEL_PAST%> by <%=items[i].LAST_STEP.USER_DISPLAY_NAME%><br><%=Utilities.getDisplayDateTimeFormat(items[i].CREATED_ON)%></span></p>
	    </a></li> 
	    <%

		}
    }
	    if (TypeID == 4){
		    WorkflowDocument items[] = workflow.getCompleted(SessionUserID, days);
		    if (items.length > 0){
		    %>
		    <ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search workflow requests...">
		    <li data-role="list-divider" data-theme="d">Completed<span class="ui-li-count"><%if (items.length > 0){%><%=items.length %><%}else{%>There are no completed requests.<%}%></span></li>
		    <%
		    }
		    for (int i = 0; i < items.length; i++){
		    	String url = "";
		    	boolean isAjax = false;
		    	if (items[i].PROCESS_ID == 1){
		    		url = "WorkflowManagerRegisterOutlet.jsp?requestID="+items[i].REQUEST_ID+"&processID=1";
		    	//	isAjax = true;
		    	}else if (items[i].PROCESS_ID == 2){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerMonthlyDiscount.jsp?processID=2&requestID="+items[i].REQUEST_ID;
		    	
		    	}else if (items[i].PROCESS_ID == 3){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerPromotionRequest.jsp?processID=3&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 4){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 5){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerCreditLimitRequest.jsp?processID=5&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 6){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerTOTCoolerInjectionRequest.jsp?processID=6&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 8){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerPerCaseDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 9){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerEmptyCreditLimitRequest.jsp?processID=9&requestID="+items[i].REQUEST_ID;
		    		
		    	}

		    %> 
		    <li><a href="<%=url %>" data-transition="flow" <%if (isAjax == false){ %>data-ajax="false"<%} %> >
		        <h2><%=items[i].PROCESS_NAME%></h2>
		        <%=workflow.getRequestPreview(items[i].PROCESS_ID, items[i].REQUEST_ID) %>
		        <p class="ui-li-aside"><span style="font-weight: 400; font-size: 13px">Request ID: <%=items[i].REQUEST_ID%><br><br><%=items[i].CURRENT_STEP.ACTION_LABEL_PAST%> by <%=items[i].CURRENT_STEP.USER_DISPLAY_NAME%><br><%=Utilities.getDisplayDateTimeFormat(items[i].CURRENT_STEP.COMPLETED_ON)%></span></p>
		    </a></li> 
		    <%
			}
	    }
	    if (TypeID == 5){
		    WorkflowDocument items[] = workflow.getDeclined(SessionUserID, days);
		    if (items.length > 0){
		    %>
		    <ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search workflow requests...">
		    <li data-role="list-divider" data-theme="d">Declined<span class="ui-li-count"><%if (items.length > 0){%><%=items.length %><%}else{%>There are no declined requests.<%}%></span></li>
		    <%
		    }
		    for (int i = 0; i < items.length; i++){
		    	
		    	String url = "";
		    	boolean isAjax = false;
		    	if (items[i].PROCESS_ID == 1){
		    		url = "WorkflowManagerRegisterOutlet.jsp?requestID="+items[i].REQUEST_ID+"&processID=1";
		    	//	isAjax = true;
		    	}else if (items[i].PROCESS_ID == 2){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerMonthlyDiscount.jsp?processID=2&requestID="+items[i].REQUEST_ID;
		    	
		    	}else if (items[i].PROCESS_ID == 3){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerPromotionRequest.jsp?processID=3&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 4){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 5){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerCreditLimitRequest.jsp?processID=5&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 6){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerTOTCoolerInjectionRequest.jsp?processID=6&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 8){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerPerCaseDiscountRequest.jsp?processID=4&requestID="+items[i].REQUEST_ID;
		    		
		    	}else if (items[i].PROCESS_ID == 9){
		    		//url = "MonthlyDiscountMain.jsp?RequestID="+items[i].REQUEST_ID;
		    		url = "WorkflowManagerEmptyCreditLimitRequest.jsp?processID=9&requestID="+items[i].REQUEST_ID;
		    		
		    	}

		    	
		    %> 
		    <li><a href="<%=url %>" data-transition="flow" <%if (isAjax == false){ %>data-ajax="false"<%} %> >
		        <h2><%=items[i].PROCESS_NAME%></h2>
		        <%=workflow.getRequestPreview(items[i].PROCESS_ID, items[i].REQUEST_ID) %>
		        <p class="ui-li-aside"><span style="font-weight: 400; font-size: 13px">Request ID: <%=items[i].REQUEST_ID%><br><br><%=items[i].CURRENT_STEP.ACTION_LABEL_PAST%> by <%=items[i].CURRENT_STEP.USER_DISPLAY_NAME%><br><%=Utilities.getDisplayDateTimeFormat(items[i].CURRENT_STEP.COMPLETED_ON)%></span></p>
		    </a></li> 
		    <%
			}
	    }

    workflow.close();
    %>
</ul>