<div data-role="header" data-theme="c" data-position="fixed">
	<h1 style="font-size: 14pt"><%=request.getParameter("title") %></h1>
	<a href="<%=request.getParameter("BackURL") %>" data-role="button" data-theme="d" data-inline="true" data-ajax="false" data-icon="back">Back</a>
    <a data-theme="c" data-transition="flow" data-inline="true" data-ajax="false" data-corners="false" ><%=request.getParameter("HeaderValue")%></a>
    
</div><!-- /header -->