    <div data-role="popup" id="LookupUserSearch" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" > 
        
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search User</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			<div class="ui-grid-a">
                <div class="ui-block-a" style="width:30%">
                    <form data-ajax="false" name="LookupUserSearchForm" id="LookupUserSearchForm" onSubmit="return lookupUserShowSearchContent()">
                    
                    <ul data-role="listview" data-inset="true">
                    <li data-role="list-divider" data-theme="d">Filter by</li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="UserSearchFormUserID" id="UserSearchFormUserID" value="" placeholder="User ID" data-mini="true" onChange="lookupUserSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="UserSearchFormFirstName" id="UserSearchFormFirstName" value="" placeholder="First Name" data-mini="true" onChange="lookupUserSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="UserSearchFormLastName" id="UserSearchFormLastName" value="" placeholder="Last Name" data-mini="true" onChange="lookupUserSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="UserSearchFormEmail" id="UserSearchFormEmail" placeholder="Email" data-mini="true" onChange="lookupUserSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="UserSearchFormDistributorID" id="UserSearchFormDistributorID" placeholder="Distributor ID" data-mini="true" onChange="lookupUserSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <button data-role="button" data-icon="search" id="UserSearchFormButtonID" data-theme="c" data-inline="true" data-corners="true" data-mini="true">Search</button>
                        </li>
                        
                    </ul>
                    <input type="hidden" id="lookupUserCallBack" value="<%=request.getParameter("CallBack")%>" />
                </form>
                </div>
                <div class="ui-block-b" style="width:70%; padding-left:20px">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider" data-theme="d">Search Results</li>
                        <li>
                            <div id="LookupUserSearchContent" style="padding-left:10px; overflow:auto">&nbsp;</div>
                        </li>
                    </ul>
                </div>
            </div><!-- /grid-a -->
            
        </div>
    </div>