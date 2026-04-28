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
	
	String PromotionName="";
	ResultSet rs1 = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestIDVal);
	while(rs1.next()){
		PromotionName = rs1.getString("label");
	}
	
	String HTMLEmailCOO = WorkflowEmail.getPromotionRequestHTMLWithActionButtonsCOO(RequestIDVal);
	Utilities.sendPBCHTMLEmail(new String[]{"atiq.baloch@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Promotion Request | "+PromotionName+" | ID#"+RequestIDVal, HTMLEmailCOO, null);
	
	
	
	//Utilities.sendPBCHTMLEmail(new String[]{"omerfk@pbc.com.pk"}, null, new String[]{"anas.wahab@pbc.com.pk"}, "Promotion Request | "+PromotionName+" | ID#"+RequestIDVal, HTMLEmail, null);
	
	ds.dropConnection();
	
}catch(Exception e){out.print(e);}

%>