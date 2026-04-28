<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.Date"%>

<%
aa
Datasource ds = new Datasource();
ds.createConnection();
//ds.startTransaction();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

try{

	/*
	ResultSet rs = s.executeQuery("SELECT id, created_on_date, distributor_id, order_id, order_created_on_date FROM inventory_sales_adjusted where id > 2689139 order by id");
	while(rs.next()){
		
		String OrderCreatedOnDate = null; 
		if (rs.getString("order_created_on_date") == null){
			OrderCreatedOnDate = null;
		}else{
			OrderCreatedOnDate = "'"+rs.getString("order_created_on_date")+"'";
		}
		
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_created_on_date='"+rs.getString("created_on_date")+"', cache_distributor_id="+rs.getString("distributor_id")+", cache_order_id="+rs.getString("order_id")+", cache_order_created_on_date="+OrderCreatedOnDate+" where id="+rs.getLong("id"));
	}
	*/
	
	/*
	ResultSet rs2 = s.executeQuery("SELECT product_id, package_id, brand_id FROM inventory_products_view where category_id=1");
	while(rs2.next()){
		
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_package_id="+rs2.getInt("package_id")+", cache_brand_id="+rs2.getInt("brand_id")+" where product_id="+rs2.getInt("product_id"));
	}
	*/
	//ds.commit();

	ResultSet rs = s.executeQuery("SELECT id, booked_by FROM inventory_sales_adjusted order by id");
	while(rs.next()){
		s2.executeUpdate("update inventory_sales_adjusted_products set cache_booked_by="+rs.getString("booked_by")+" where id="+rs.getLong("id"));
	}
	
	
	s2.close();
	s.close();
	c.close();
	
}catch(Exception e){
	//ds.rollback();
	out.print(e);
}finally{
	ds.dropConnection();
}

%>