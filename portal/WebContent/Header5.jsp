<div data-role="header" data-theme="c" data-position="fixed">
	
	<% 
	String BackURL = request.getParameter("BackURL");
	if(session.getAttribute("isMobileSession") != null){ 
		BackURL = "MobileReportCenterMain.jsp?"+Math.random()+"="+Math.random();
	}
	%>
	
			<a href="<%=BackURL%>" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
			
				<h1 style="font-size: 11pt;text-align:center;"><span style="font-size: 11pt"><%=request.getParameter("title") %></span></h1>
			
				<a href="#" data-role="button" data-icon="star" data-iconpos="notext" onClick="ShowHideFilter();"></a>
		
    
</div><!-- /header -->