<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.Date"%>

<%

Datasource ds = new Datasource();
ds.createConnection();
ds.startTransaction();
Connection c = ds.getConnection();
Statement s = c.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
Statement s2 = c.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
Statement s3 = c.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);

String r = request.getParameter("r");

try{

	ResultSet rs = s.executeQuery("SELECT created_by FROM pep.workflow_requests where request_id="+r);
	if(rs.first()){		
		ResultSet rs2 = s2.executeQuery("SELECT distinct tdm_id FROM pep.distributor_beat_plan_view where assigned_to="+rs.getString(1));
		if(rs2.first()){
			s3.executeUpdate("update workflow_requests set current_userid="+rs2.getString(1)+" where request_id="+r);
			s3.executeUpdate("update workflow_requests_steps set user_id="+rs2.getString(1)+" where request_id="+r+" and completed_on is null");
		}
	}
	
	ds.commit();
	s3.close();
	s2.close();
	s.close();
	c.close();
	
}catch(Exception e){
	ds.rollback();
	out.print(e);
}finally{
	ds.dropConnection();
}

%>