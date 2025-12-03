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


ResultSet rs = s.executeQuery("SELECT id, distributor_id, outlet_id FROM inventory_sales_invoices where beat_plan_id is null");
while(rs.next()){
	ResultSet rs2 = s2.executeQuery("SELECT dbp.id as beat_plan_id FROM distributor_beat_plan dbp, distributor_beat_plan_schedule dbps where dbp.id=dbps.id and dbps.outlet_id="+rs.getString("outlet_id"));
	if(rs2.first()){
		//System.out.println("update inventory_sales_invoices set beat_plan_id="+rs2.getString("beat_plan_id")+" where id="+rs.getString("id"));
		s3.executeUpdate("update inventory_sales_invoices set beat_plan_id="+rs2.getString("beat_plan_id")+" where id="+rs.getString("id"));
	}
}


s3.close();
s2.close();
s.close();
ds.dropConnection();

%>		
			