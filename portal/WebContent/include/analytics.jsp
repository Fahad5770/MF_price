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
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();


// A/C To A/C Trf. TO 300100293940001 # 280985
// Account to Account Trasnfer - Outward

ResultSet rs = s.executeQuery("SELECT * FROM reconciliation.record where particulars like 'A/C To A/C Trf. F%'");
while(rs.next()){
	
	String description = rs.getString("particulars");
	
	String account_no = description.substring(description.indexOf("A/C To A/C Trf. TO "),description.indexOf("# "));
	
	System.out.println(account_no);
}

s.close();
c.close();
ds.dropConnection();
%>