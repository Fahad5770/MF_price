<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>


        	
<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

String SQL = "SELECT distinct outlet_id FROM sampling_monthly_approval where distributor_id is null order by outlet_id"; 
         
ResultSet rs = s.executeQuery(SQL);

while(rs.next()){
	//out.println( "select * from sampling_monthly_approval where outlet_id = "+rs.getString("outlet_id")+" and distributor_id is not null order by approval_id <br>" );
	ResultSet rs2 = s2.executeQuery("select * from sampling_monthly_approval where outlet_id = "+rs.getString("outlet_id")+" and distributor_id is not null order by approval_id");
	if(rs2.first()){
		
		//out.println("===>update sampling_monthly_approval set distributor_id = "+rs2.getString("distributor_id")+" where distributor_id is null and outlet_id = "+rs.getString("outlet_id")+"<br>");
		s3.executeUpdate("update sampling_monthly_approval set distributor_id = "+rs2.getString("distributor_id")+" where distributor_id is null and outlet_id = "+rs.getString("outlet_id"));
		
		//out.print(rs2.getString("outlet_id")+"<br>");
	}else{
		out.print(rs.getString("outlet_id")+"<br>");
		
		ResultSet rs3 = s4.executeQuery("select customer_id from outletmaster where outlet_id = "+rs.getString("outlet_id"));
		while(rs3.next()){
			s3.executeUpdate("update sampling_monthly_approval set distributor_id = "+rs3.getString("customer_id")+" where distributor_id is null and outlet_id = "+rs.getString("outlet_id"));
		}
		
	}
}

s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>