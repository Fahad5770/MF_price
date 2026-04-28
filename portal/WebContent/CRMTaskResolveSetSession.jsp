<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 148;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long ComplaintID = Utilities.parseLong(request.getParameter("ComplaintID"));

session.setAttribute("45039481234_SR1SelectedComplaintID", ComplaintID);

out.print("success");

%>