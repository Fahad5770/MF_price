<%@page import="com.pbc.util.Utilities"%>
<%
String title = request.getParameter("title");
int tab = Utilities.parseInt(request.getParameter("tab"));
long DistributorCode = Utilities.parseLong(request.getParameter("DistributorCode"));
%>
<div data-role="header" data-id="BeatPlanHeader" data-theme="d">
    <h1><%=title%></h1>
    <a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
    <a href="javascript:changeRegionForDashboard()" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back" >Change Region</a>
    <div data-role="navbar" >
        <ul>
            <li><a href="#" data-ajax="false" data-transition="none" >Statistics</a></li>
			<li><a href="#" data-ajax="false" data-transition="none" >Distributors</a></li>
			<li><a href="#" data-ajax="false" data-transition="none" >Sales</a></li>
			<li><a href="#" data-ajax="false" data-transition="none" >Discounts</a></li>
			<li><a href="#" data-ajax="false" data-transition="none" >Promotions</a></li>
        </ul>
    </div><!-- /navbar -->
</div><!-- /header -->