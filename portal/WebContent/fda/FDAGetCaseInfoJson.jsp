<%@page import="org.json.simple.JSONObject"%>
<%@page import="com.sap.conn.jco.ext.SessionException.Type"%>
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
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>

<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

response.setContentType("json");
JSONObject json = new JSONObject();           

int CaseNo = Utilities.parseInt(request.getParameter("CaseNo"));
ResultSet rs = s.executeQuery("select *, (SELECT label FROM fda_complaints_types where id=type_id) type_label from fda_complaints where case_no="+CaseNo);
if(rs.first()){
	json.put("success", "true");
	
	json.put("FirstName", rs.getString("first_name"));
	json.put("LastName", rs.getString("last_name"));
	json.put("TypeLabel", rs.getString("type_label"));
	json.put("StatusID", rs.getString("status_id"));
	json.put("DateOfEntry", Utilities.getDisplayDateFormat(Utilities.parseDateYYYYMMDD(rs.getString("date_of_entry"))));
	
}else{
	json.put("success", "false");
	json.put("error", "No Case found for the ID: "+CaseNo);
}

s.close();
c.close();
ds.dropConnection();


out.print(json);


%>