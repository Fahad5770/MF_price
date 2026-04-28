<%@ page import="com.pbc.util.Datasource"%>
<%@ page import="com.pbc.util.Utilities"%>
<%@ page import="com.pbc.workflow.*"%>
<%@ page import="java.sql.*"%>
<%

long RequestIDVal = Utilities.parseLong(request.getParameter("RequestID"));

Datasource ds = new Datasource();

try {
	
	
	ds.createConnection();
	
	Statement s = ds.createStatement();
	
	String HTMLEmailCOO = WorkflowEmail.getDiscountRequestHTMLWithActionButtonsCOO(RequestIDVal);
	
	
	String DistributorName ="";
	ResultSet rs = s.executeQuery("SELECT distributor_id,(select name from common_distributors cd where isdrd.distributor_id=cd.distributor_id) name FROM inventory_sales_discounts_request isdr join inventory_sales_discounts_request_distributors isdrd on isdr.id=isdrd.product_promotion_id where isdr.request_id="+RequestIDVal);
	if(rs.first()){
		DistributorName = rs.getString("name");
	}

	Utilities.sendPBCHTMLEmail(new String[]{"atiq.baloch@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Upfront Discount Request | "+DistributorName+" | ID#"+RequestIDVal, HTMLEmailCOO, null);
	
	ds.dropConnection();
	
}catch(Exception e){out.print(e);}

%>