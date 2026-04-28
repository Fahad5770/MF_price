<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>

<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

ResultSet rs = s.executeQuery("SELECT outlet_id, count(distinct id) ct FROM pep.distributor_beat_plan_schedule group by outlet_id having ct != 1");
while(rs.next()){
	out.println(rs.getString(1)+" "+rs.getString(2)+"<br>");
}

s.close();
c.close();
ds.dropConnection();
%>