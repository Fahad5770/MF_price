<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%=com.pbc.util.Utilities.sendPBCEmail(new String[]{"anas.wahab@pbc.com.pk"}, null, null, "Cash Instrument Inflow", "This is a system generated email, please do not reply to it.", "DSCR1_20141130.pdf")
%>
</body>
</html>