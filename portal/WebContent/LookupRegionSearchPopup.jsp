    <div data-role="popup" id="LookupRegionSearch" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" > 
        
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Regions</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
        	<input type="hidden" id="lookupRegionCallBack" value="<%=request.getParameter("CallBack")%>" />
			<div id="LookupRegionSearchContent" style="padding-left:10px; overflow:auto">&nbsp;</div>
        </div>
    </div>