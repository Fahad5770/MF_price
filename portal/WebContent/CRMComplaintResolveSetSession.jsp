<%@page import="com.pbc.util.Utilities"%>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 142;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

long ComplaintID = Utilities.parseLong(request.getParameter("ComplaintID"));

session.setAttribute("45039483932_SR1SelectedComplaintID", ComplaintID);

out.print("success");
%>