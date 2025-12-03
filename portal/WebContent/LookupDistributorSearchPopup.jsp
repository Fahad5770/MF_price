    <%@page import="com.pbc.util.Utilities"%>
    <div data-role="popup" id="LookupDistributorSearch" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" > 
        
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search Distributor</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			<div class="ui-grid-a">
                <div class="ui-block-a" style="width:30%">
                    <form data-ajax="false" name="LookupDistributorSearchForm" id="LookupDistributorSearchForm" onSubmit="return lookupDistributorShowSearchContent()">
                    
                    <ul data-role="listview" data-inset="true">
                    <li data-role="list-divider" data-theme="d">Filter by</li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="DistributorSearchFormSAPCode" id="DistributorSearchFormSAPCode" value="" placeholder="Distributor ID" data-mini="true" onChange="lookupDistributorSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="DistributorSearchFormName" id="DistributorSearchFormName" value="" placeholder="Distributor Name" data-mini="true" onChange="lookupDistributorSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="DistributorSearchFormCity" id="DistributorSearchFormCity" value="" placeholder="City" data-mini="true" onChange="lookupDistributorSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="DistributorSearchFormAddress" id="DistributorSearchFormAddress" value="" placeholder="Address" data-mini="true" onChange="lookupDistributorSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="DistributorSearchFormContact" id="DistributorSearchFormContact" value="" placeholder="Contact #" data-mini="true" onChange="lookupDistributorSetChangeFlag()" >
                        </li>
                        
                        
                        <li data-role="fieldcontain">
                            <button data-role="button" data-icon="search" id="DistributorSearchFormButton" data-theme="c" data-inline="true" data-corners="true" data-mini="true">Search</button>
                        </li>
                        
                    </ul>
                    <input type="hidden" id="lookupDistributorCallBack" value="<%=request.getParameter("CallBack")%>" />
                    <input type="hidden" name="DistributorSearchFeatureID" id="DistributorSearchFeatureID" value="<%=Utilities.parseInt(request.getParameter("DistributorSearchFeatureID"))%>" />
                </form>
                </div>
                <div class="ui-block-b" style="width:70%; padding-left:20px">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider" data-theme="d">Search Results</li>
                        <li>
                            <div id="LookupDistributorSearchContent" style="padding-left:10px; overflow:auto">&nbsp;</div>
                        </li>
                    </ul>
                </div>
            </div><!-- /grid-a -->
            
        </div>
    </div>