<%
if (session == null || session.getAttribute("UserID") == null){
	response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
}
%>