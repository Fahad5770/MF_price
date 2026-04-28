    <%@page import="com.pbc.util.Utilities"%>
<div data-role="popup" id="LookupOutletSearch" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" > 
        
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search Outlet</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			<div class="ui-grid-a">
                <div class="ui-block-a" style="width:30%">
                    <form data-ajax="false" name="LookupOutletSearchForm" id="LookupOutletSearchForm" onSubmit="return lookupOutletShowSearchContent()">
                    
                    <ul data-role="listview" data-inset="true">
                    <li data-role="list-divider" data-theme="d">Filter by</li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="OutletSearchFormSAPCode" id="OutletSearchFormSAPCode" value="" placeholder="Outlet ID" data-mini="true" onChange="lookupOutletSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="OutletSearchFormName" id="OutletSearchFormName" value="" placeholder="Outlet Name" data-mini="true" onChange="lookupOutletSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="OutletSearchFormAddress" id="OutletSearchFormAddress" value="" placeholder="Address" data-mini="true" onChange="lookupOutletSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="OutletSearchFormOwner" id="OutletSearchFormOwner" placeholder="Owner" data-mini="true" onChange="lookupOutletSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="OutletSearchFormTelephone" id="OutletSearchFormTelephone" placeholder="Telephone" data-mini="true" onChange="lookupOutletSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                        	<label for="OutletSearchMultiSelect" style="width:120px">Multi-Selection :</label>
							<select name="OutletSearchMultiSelect" id="OutletSearchMultiSelect" data-role="slider" data-mini="true">
							    <option value="off" selected="">No</option>
							    <option value="on">Yes</option>
							</select>
						</li>
                        
                        <li data-role="fieldcontain">
                            <button data-role="button" data-icon="search" id="OutletSearchFormButton" data-theme="c" data-inline="true" data-corners="true" data-mini="true">Search</button>
                        </li>
                        
                    </ul>
                    <input type="hidden" id="lookupOutletCallBack" value="<%=request.getParameter("CallBack")%>" />
                    <input type="hidden" name="OutletSearchFeatureID" id="OutletSearchFeatureID" value="<%=Utilities.parseInt(request.getParameter("OutletSearchFeatureID"))%>" />
                </form>
                </div>
                <div class="ui-block-b" style="width:70%; padding-left:20px">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider" data-theme="d">Search Results</li>
                        <li>
                            <div id="LookupOutletSearchContent" style="padding-left:10px; overflow:auto">&nbsp;</div>
                        </li>
                    </ul>
                </div>
            </div><!-- /grid-a -->
            
        </div>
    </div>