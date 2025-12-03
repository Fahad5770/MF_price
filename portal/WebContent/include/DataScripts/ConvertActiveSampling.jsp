<%@page import="com.pbc.util.*"%>
<%@page import="java.sql.*"%>

<% 
Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();

/*
2 = 1500 ML PET
6 = 500 ML PET
*/

ResultSet rs = s.executeQuery("SELECT sp.* FROM pep.sampling_percase sp, pep.sampling s where s.sampling_id = sp.sampling_id and s.active = 1 and sp.package = 2 and sp.brand_id = 0");
while(rs.next()){
	
	
	ResultSet rs2 = s2.executeQuery("SELECT brand_id FROM pep.inventory_products where package_id = 2 and brand_id not in (12, 18)");
	while(rs2.next()){
	
		s3.executeUpdate("insert into pep.sampling_percase values('"+rs.getString(1)+"','"+rs.getString(2)+"','"+rs2.getString(1)+"','"+rs.getString(4)+"','"+rs.getString(5)+"','"+rs.getString(6)+"','"+rs.getString(7)+"','"+rs.getString(8)+"')");
	
	}
	
	
	s3.executeUpdate("delete from pep.sampling_percase where sampling_id = "+rs.getString(1)+" and package = 2 and brand_id = 0");

}
out.print("done");

s3.close();
s2.close();
s.close();

ds.dropConnection();

%>