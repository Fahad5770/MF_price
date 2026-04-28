<%@page import="com.pbc.util.Utilities"%>
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
String message = "Mian Umer Karyana Store\\n";
message += "ABC Bazar, ABC Market, FSD. 03005676683\\n";
message += "Pepsi Cooler Selling Gourmet\\n";
message += "B Brands\\n";
message += "Brand Shortage\\n";

String pictures[] = new String[2];
pictures[0] = "CRM_252812814112943_1407824905724.jpg";
pictures[1] = "CRM_2657247141580_1406196416711.jpg";


Utilities.sendWhatsApp("923008406444", message, pictures);
%>

</body>
</html>