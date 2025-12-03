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
			/*
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			Statement s2 = c.createStatement();
			Statement s3 = c.createStatement();
			
            ResultSet rs = s.executeQuery("SELECT * FROM mobile_order where beat_plan_id is null and unedited_order_id is not null order by id");
            while( rs.next() ){
            	
            	//System.out.println("select id from distributor_beat_plan_view where assigned_to = "+rs.getString("created_by")+" and distributor_id = "+rs.getString("distributor_id")+" and outlet_id = "+rs.getString("outlet_id")+" limit 1");
            	
            	ResultSet rs2 = s2.executeQuery("select id from distributor_beat_plan_view where distributor_id = "+rs.getString("distributor_id")+" and outlet_id = "+rs.getString("outlet_id")+" limit 1");
            	if( rs2.first() ){
            		
            		s3.executeUpdate("update mobile_order set beat_plan_id = "+rs2.getString("id")+" where id = " + rs.getString("id") );
            		s3.executeUpdate("update inventory_sales_invoices set beat_plan_id = "+rs2.getString("id")+" where order_id = " + rs.getString("id") );
            		s3.executeUpdate("update inventory_sales_adjusted set beat_plan_id = "+rs2.getString("id")+" where order_id = " + rs.getString("id") );
            		
            	}
            }
            
            s3.close();
            s2.close();
            s.close();
            c.close();
            ds.dropConnection();
            */
           
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			Statement s2 = c.createStatement();
			Statement s3 = c.createStatement();
			
            ResultSet rs = s.executeQuery("SELECT step_id, action_id, user_id, request_id, count(sequence_id) ct FROM pep.workflow_requests_steps group by step_id, action_id, user_id, request_id having ct > 1");
            while( rs.next() ){
				s3.executeUpdate("delete from workflow_requests_steps where step_id = "+rs.getString(1)+" and action_id = "+rs.getString(2)+" and user_id = "+rs.getString(3)+" and request_id = "+ rs.getString(4)+" limit 1");
            }
            
            s3.close();
            s2.close();
            s.close();
            c.close();
            ds.dropConnection();
            
            %>
        	