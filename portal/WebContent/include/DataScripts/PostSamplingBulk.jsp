<%@page import="com.pbc.sampling.SamplingPosting"%>
<%@page import="com.pbc.util.*"%>
<%@page import="java.sql.*"%>
<% 
Datasource ds = new Datasource();
ds.createConnection();
Statement s = ds.createStatement();

ResultSet rs = s.executeQuery("select approval_id, sampling_id, outlet_id, deduction_against_advance, month from sampling_monthly_approval where deduction_against_advance != 0 and status_id = 1");
while(rs.next()){
	long ApprovalID = rs.getLong(1);
	long SamplingID = rs.getLong(2);
	long OutletID = rs.getLong(3);
	double DeductionAgainstAdvance = rs.getDouble(4);
	java.util.Date month = rs.getDate("month");
	
	SamplingPosting sp = new SamplingPosting();
	sp.postMonthlyAdjustment(ApprovalID, SamplingID, OutletID, 2062, 2, DeductionAgainstAdvance, "Monthly Adjustment for " + Utilities.getDisplayDateMonthYearFormat(month));
	sp.close();
}


s.close();
ds.dropConnection();
%>