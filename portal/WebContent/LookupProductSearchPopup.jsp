<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

String PackageOptions = "<option value=''>Select Package</option>";
ResultSet rs = s.executeQuery("select id, label from inventory_packages order by label");
while(rs.next()){
	PackageOptions += "<option value='"+rs.getString("id")+"'>"+rs.getString("label")+"</option>";
}

String BrandOptions = "<option value=''>Select Brand</option>";
ResultSet rs2 = s.executeQuery("select id, label from inventory_brands order by label");
while(rs2.next()){
	BrandOptions += "<option value='"+rs2.getString("id")+"'>"+rs2.getString("label")+"</option>";
}

%>
    <div data-role="popup" id="LookupProductSearch" data-overlay-theme="a" data-theme="d" data-dismissible="true" class="ui-corner-all" > 
        
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search Product</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
			<div class="ui-grid-a">
                <div class="ui-block-a" style="width:30%">
                    <form data-ajax="false" name="LookupProductSearchForm" id="LookupProductSearchForm" onSubmit="return lookupProductShowSearchContent()">
                    
                    <ul data-role="listview" data-inset="true">
                    <li data-role="list-divider" data-theme="d">Filter by</li>
                        
                        <li data-role="fieldcontain">
                            <select name="ProductSearchFormPackage" id="ProductSearchFormPackage" data-mini="true" onChange="lookupProductSetChangeFlag()" >
                            	<%=PackageOptions%>
                            </select>
                        </li>
                        
                        <li data-role="fieldcontain">
                            <select name="ProductSearchFormBrand" id="ProductSearchFormBrand" data-mini="true" onChange="lookupProductSetChangeFlag()" >
                            	<%=BrandOptions%>
                            </select>
                        </li>
                        
                        <li data-role="fieldcontain">
                            <button data-role="button" data-icon="search" id="ProductSearchFormButton" data-theme="c" data-inline="true" data-corners="true" data-mini="true">Search</button>
                        </li>
                        
                    </ul>
                    <input type="hidden" id="lookupProductCallBack" value="<%=request.getParameter("CallBack")%>" />
                </form>
                </div>
                <div class="ui-block-b" style="width:70%; padding-left:20px">
                    <ul data-role="listview" data-inset="true">
                        <li data-role="list-divider" data-theme="d">Search Results</li>
                        <li>
                            <div id="LookupProductSearchContent" style="padding-left:10px; overflow:auto">&nbsp;</div>
                        </li>
                    </ul>
                </div>
            </div><!-- /grid-a -->
            
        </div>
    </div>
    
<%
s.close();
c.close();
ds.dropConnection();
%>