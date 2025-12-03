<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
			
<%

Datasource ds = new Datasource();	
ds.createConnection();
Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();

ResultSet rs = s.executeQuery("SELECT id, distributor_id, outlet_id, created_by FROM inventory_sales_adjusted where beat_plan_id is null");
while(rs.next()){
	ResultSet rs2 = s2.executeQuery("SELECT id as beat_plan_id FROM pep.distributor_beat_plan_view where assigned_to = "+rs.getString("created_by"));
	if(rs2.first()){
		//System.out.println("update inventory_sales_adjusted set beat_plan_id="+rs2.getString("beat_plan_id")+" where id="+rs.getString("id"));
		s3.executeUpdate("update inventory_sales_adjusted set beat_plan_id="+rs2.getString("beat_plan_id")+" where id="+rs.getString("id"));
	}
}

s3.close();
s2.close();
s.close();
ds.dropConnection();

%>		
			