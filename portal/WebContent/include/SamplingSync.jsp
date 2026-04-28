<%@page import="org.omg.CORBA.portable.RemarshalException"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:include page="StandaloneSrc.jsp" /> <!-- JQM Base -->
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();



%>

<div data-role="header" data-id="LiftingHeader" data-theme="d">
    <h1>Sync</h1>
   
    
</div>

<div data-role="content" data-theme="d">

<%
ResultSet rs = s.executeQuery("SELECT * FROM sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and sp.package = 2 and curdate() between sp.valid_from and sp.valid_to and sp.brand_id in (0,1)");

while(rs.next()){
	s2.executeUpdate("insert ignore into sampling_percase(sampling_id,package,brand_id,agency_share,company_share,deduction_term,hand_to_hand,valid_from,valid_to) values("+rs.getLong("sampling_id")+",24,1,"+rs.getDouble("agency_share")+","+rs.getDouble("company_share")+","+rs.getDouble("deduction_term")+",0,"+Utilities.getSQLDate(rs.getDate("valid_from"))+","+Utilities.getSQLDate(rs.getDate("valid_to"))+")");
	
	
	
	
	//out.println("insert ignore into sampling_percase(sampling_id,package,brand_id,agency_share,company_share,deduction_term,hand_to_hand,valid_from,valid_to) values("+rs.getLong("sampling_id")+",24,1,"+rs.getDouble("agency_share")+","+rs.getDouble("company_share")+","+rs.getDouble("deduction_term")+",0,"+Utilities.getSQLDate(rs.getDate("valid_from"))+","+Utilities.getSQLDate(rs.getDate("valid_to"))+")");
}

%>
<p>Successfully Inserted</p>

	



</div>