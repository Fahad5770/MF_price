<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.UserAccess"%>

<%
Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();
Statement s2 = ds.createStatement();

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

%>
    
    <div data-role="header" data-theme="b" data-position="fixed">
		<div>
	        <div data-role="controlgroup" data-type="horizontal" style="float: left; display: inline; padding-left: 10px; padding-right: 5px;" data-corners="true">
				<a href="#menupanel" data-role="button" data-inline="true" data-mini="true" data-icon="bars" data-theme="a"  data-iconpos="notext">Menu</a>
			</div>
			
			
	        <div data-role="controlgroup" data-type="horizontal" data-corners="true" style="float: left; display: inline; padding-left: 10px; padding-right: 5px;">
				<a href="#PopupDashboard" data-rel="popup" data-role="button" data-icon="arrow-d" data-theme="a" data-transition="pop" data-position-to="#TESTID">Dashboards</a>
				
			</div>
			<div id="TESTID" style="float: left; margin-left:0px;"></div>
			<%
			if(UserAccess.isAuthorized(99, SessionUserID, request) == true){
				%>
				
	        <div data-role="controlgroup" data-type="horizontal" data-corners="true" style="float: left; display: inline; padding-left: 10px; padding-right: 5px;">
				<a href="ReportCenterMain.jsp?<%=Math.random() %><%=Math.random() %>=<%=Math.random() %><%=Math.random() %>" data-ajax="false" data-role="button" data-icon="grid" data-theme="a">Report Center</a>
				
			</div>				
				<%
			}
			%>
			
			
			
			<div data-role="controlgroup" data-type="horizontal" data-corners="true" style="float: right; padding-left: 10px; padding-right: 5px;">
			
			    <button data-role="button" data-icon="gear" data-theme="b" id="ChangePasswordButton">Change Password</button>
			    <a href="index.jsp?signout=1&<%=Math.random() %><%=Math.random() %>=<%=Math.random() %><%=Math.random() %>" data-ajax="false" data-role="button" data-inline="true" data-mini="true" data-icon="minus" data-theme="b" >Signout</a>
			</div>
		</div>
    </div><!-- /header -->
    
    <div data-role="popup" id="PopupDashboard" data-theme="none">
        <ul data-role="listview" data-inset="true" style="min-width:210px;" data-theme="c">
             
	      	<%
	      	ResultSet rs2 = s2.executeQuery("select feature_name, url, ajax_call from features where group_id = 4 and active = 1 and feature_id in (select feature_id from user_access where user_id = "+SessionUserID+") order by sort_order");
	      	while(rs2.next()){
	      		%>
	      		<li data-icon="arrow-r"><a href="<%=rs2.getString(2) %>"  <%if (rs2.getInt(3) == 0){out.print("data-ajax=\"false\"");} %>><%=rs2.getString(1) %></a></li>
	      		<%
	      	}
	      	%>            
        </ul>
    </div>
<%
s2.close();
s.close();
ds.dropConnection();
%>
    