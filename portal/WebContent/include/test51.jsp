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
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

String r = request.getParameter("r");

try{
	/*
	ResultSet rs = s.executeQuery("SELECT created_by FROM pep.workflow_requests where request_id="+r);
	if(rs.first()){		
		ResultSet rs2 = s2.executeQuery("SELECT distinct rsm_id FROM pep.distributor_beat_plan_view where assigned_to="+rs.getString(1));
		if(rs2.first()){
			s3.executeUpdate("update workflow_requests set current_userid="+rs2.getString(1)+" where request_id="+r);
			s3.executeUpdate("update workflow_requests_steps set user_id="+rs2.getString(1)+" where request_id="+r+" and completed_on is null");
		}
	}
	*/
	
	
	
	ResultSet rs = s.executeQuery("SELECT dt.id, dtp.quantity FROM pep.distributor_targets dt join distributor_targets_packages dtp on dt.id = dtp.id where month = 9 and year = 2016 and package_id = 3");
	while(rs.next()){
		
		s3.executeUpdate("insert into distributor_targets_packages_brands values ("+rs.getString(1)+",3,1,"+rs.getString(2)+")");
		
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