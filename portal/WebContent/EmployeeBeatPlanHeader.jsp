<%@page import="com.pbc.util.Utilities"%>
<%
String title = request.getParameter("title");
int tab = Utilities.parseInt(request.getParameter("tab"));
int EmployeeCode = Utilities.parseInt(request.getParameter("EmployeeCode"));
%>
<div data-role="header" data-id="BeatPlanHeader" data-theme="d">
    <h1><%=title%></h1>
    <a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
    <a href="javascript:changeEmployeeForDashboard()" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back" >Change Order Booker</a>
    <div data-role="navbar" >
        <ul>
        
            <li><a href="EmployeeBeatPlan.jsp?EmployeeCode=<%=EmployeeCode%>" data-ajax="false" data-transition="none" <%if (tab == 1){%>class="ui-btn-active ui-state-persist"<%} %> >Beat Plan</a></li>
			<li><a href="EmployeeProductSpecification.jsp?EmployeeCode=<%=EmployeeCode%>" data-ajax="false" data-transition="none" <%if (tab == 2){%>class="ui-btn-active ui-state-persist"<%} %>>Product Specification</a></li>
			<li><a href="#" data-ajax="false" data-transition="none" >Profile</a></li>
			<li><a href="#" data-ajax="false" data-transition="none" >Performance</a></li>
			
        </ul>
    </div><!-- /navbar -->
</div><!-- /header -->