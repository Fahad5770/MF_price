<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
//=com.pbc.util.Utilities.sendPBCEmail(new String[]{"anas.wahab@pbc.com.pk"}, null, null, "Cash Instrument Inflow", "This is a system generated email, please do not reply to it.", "DSCR1_20141130.pdf")
%>

<%

String message = "PEPSI New Schemes:\n\n";
message += "har 500ML PET pack k saath aik 500ML ki bottle muft hasil kerain.\n\n";
message += "har 1500ML PET pack k saath aik 1500ML ki bottle muft hasil kerain.\n\n";
message += "har 2250ML PET pack k saath aik 2250ML ki bottle muft hasil kerain.\n\n";
message += "har 500ML STING pack k saath do 500ML STING ki bottle muft hasil kerain.\n\n\n";
message += "ye schemes mehdood muddat k liyai hain.";

out.print(com.pbc.util.Utilities.sendSMS("923444471200", message));

%>
</body>
</html>