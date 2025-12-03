<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%
Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();


long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
System.out.println("SessionUserID "+SessionUserID);

%>
	<div data-role="panel" id="menupanel" data-display="push" data-position="left" data-position-fixed="true" data-theme="a">
		<ul data-role="listview" data-theme="d" class="nav-search" data-filter="true" data-filter-placeholder="Search..." data-filter-theme="a">
            <!-- <li data-icon="delete" data-theme="a"><%=request.getParameter("title") %></li>  -->
            <%
            System.out.println("select group_id, group_name from feature_groups where active = 1 and group_id != 4 and group_id in (select distinct fe.group_id from user_access ua, features fe where ua.feature_id = fe.feature_id and ua.user_id = "+SessionUserID+" and fe.visible = 1) order by sort_order");
            ResultSet rs = s.executeQuery("select group_id, group_name from feature_groups where active = 1 and group_id != 4 and group_id in (select distinct fe.group_id from user_access ua, features fe where ua.feature_id = fe.feature_id and ua.user_id = "+SessionUserID+" and fe.visible = 1) order by sort_order");
            while(rs.next()){
            %>
            <li data-icon="arrow-r" data-theme="b"><%=rs.getString(2) %></li>
            	<%
            	System.out.println("select feature_name, url, ajax_call, feature_id from features where group_id = "+rs.getString(1)+" and active = 1 and visible = 1 and feature_id in (select feature_id from user_access where user_id = "+SessionUserID+") order by sort_order");
            	ResultSet rs2 = s2.executeQuery("select feature_name, url, ajax_call, feature_id from features where group_id = "+rs.getString(1)+" and active = 1 and visible = 1 and feature_id in (select feature_id from user_access where user_id = "+SessionUserID+") order by sort_order");
            	while(rs2.next()){
            		String url = rs2.getString(2);
            		if (url.indexOf("?") == -1){
            			url = url + "?"+Math.random()+"="+Math.random();
            		}else{
            			url = url + "&"+Math.random()+"="+Math.random();
            		}
            		%>
            		<li data-icon="arrow-r"><a href="<%=url %>" data-transition="flow" <%if (rs2.getInt(3) == 0){out.print("data-ajax=\"false\"");} %>>F<%=rs2.getString("feature_id") %> - <%=rs2.getString(1) %></a></li>
            		<%
            	}
            	%>
            <%
            }
            %>
            <li data-icon="delete" data-theme="b"><a href="#" data-rel="close">Close menu</a></li>
        </ul>
	</div><!-- /Left Navigation Panel -->
<%
s2.close();
s.close();

ds.dropConnection();
%>