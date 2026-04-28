<%@page import="com.pbc.util.*"%>
<%@page import="java.sql.*"%>
<% 
try{
	Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
}catch (Exception e){
	e.printStackTrace();
}
Connection oc = DriverManager.getConnection("jdbc:odbc:sampling","","");
Statement os = oc.createStatement();

Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();


ResultSet rs = os.executeQuery("select * from [sheet1$]");
while(rs.next()){
	
	long OutletID = rs.getLong(1);
	java.util.Date ValidFrom = rs.getDate(2);
	java.util.Date ValidTo = rs.getDate(3);
	double CompanyShare = rs.getDouble(4);
	double DedTerm = rs.getDouble(6);

	//out.println(ValidFrom+"<br>");
	
	out.println("update sampling set fixed_company_share = "+CompanyShare+", fixed_deduction_term = "+DedTerm+", fixed_company_share_offpeak = "+CompanyShare+", fixed_deduction_term_offpeak = "+DedTerm+", fixed_valid_from = '"+ValidFrom+"', fixed_valid_to = '"+ValidTo+"' where outlet_id = "+OutletID+" and active = 1;<br>");

}

os.close();
oc.close();

s.close();
ds.dropConnection();
%>