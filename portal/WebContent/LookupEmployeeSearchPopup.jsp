    <div data-role="popup" id="LookupEmployeeSearch" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" > 
        
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search Employee</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			<div class="ui-grid-a">
                <div class="ui-block-a" style="width:30%">
                    <form data-ajax="false" name="LookupEmployeeSearchForm" id="LookupEmployeeSearchForm" onSubmit="return lookupEmployeeShowSearchContent()">
                    
                    <ul data-role="listview" data-inset="true">
                    <li data-role="list-divider" data-theme="d">Filter by</li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="EmployeeDashboardSearchFormSAPCode" id="EmployeeDashboardSearchFormSAPCode" value="" placeholder="Employee ID" data-mini="true" onChange="lookupEmployeeSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="EmployeeDashboardSearchFormFirstName" id="EmployeeDashboardSearchFormFirstName" value="" placeholder="First Name" data-mini="true" onChange="lookupEmployeeSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="EmployeeDashboardSearchFormLastName" id="EmployeeDashboardSearchFormLastName" value="" placeholder="Last Name" data-mini="true" onChange="lookupEmployeeSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="EmployeeDashboardSearchFormDepartment" id="EmployeeDashboardSearchFormDepartment" placeholder="Department" data-mini="true" onChange="lookupEmployeeSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <input type="text" name="EmployeeDashboardSearchFormDesignation" id="EmployeeDashboardSearchFormDesignation" placeholder="Designation" data-mini="true" onChange="lookupEmployeeSetChangeFlag()" >
                        </li>
                        
                        <li data-role="fieldcontain">
                            <button data-role="button" data-icon="search" id="EmployeeDashboardSearchFormButton" data-theme="c" data-inline="true" data-corners="true" data-mini="true">Search</button>
                        </li>
                        
                    </ul>
                    <input type="hidden" id="lookupEmployeeCallBack" value="<%=request.getParameter("CallBack")%>" />
                </form>
                </div>
                <div class="ui-block-b" style="width:70%; padding-left:20px">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider" data-theme="d">Search Results</li>
                        <li>
                            <div id="LookupEmployeeSearchContent" style="padding-left:10px; overflow:auto">&nbsp;</div>
                        </li>
                    </ul>
                </div>
            </div><!-- /grid-a -->
            
        </div>
    </div>