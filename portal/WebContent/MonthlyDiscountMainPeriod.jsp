<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.outlet.Outlet"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.util.UserAccess"%>
<style>
.ui-btn {
text-align: left
}
</style>
<script>

</script>

        <%
        
        String OutletQueryString = "";
        if(session.getAttribute("MonthlyDiscountOutlets") != null){
        	OutletQueryString = (String) session.getAttribute("MonthlyDiscountOutlets");
        }
        
        int month = 0;
        if(session.getAttribute("MonthlyDiscountMonth") != null){
        	month = (Integer) session.getAttribute("MonthlyDiscountMonth");
        }

        int year = 0;
        if(session.getAttribute("MonthlyDiscountYear") != null){
        	year = (Integer) session.getAttribute("MonthlyDiscountYear");
        }
        
        %>   
           <input type="hidden" name="FilterType" id="FilterType" value="4" >
           
        
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
			
			<li data-role="list-divider" data-theme="a">Period</li>
			
			<li>
	  			<select name="month" id="month" data-mini="true">
		           <option value="1">January</option>
		           <option value="2">February</option>
		           <option value="3">March</option>
		           <option value="4">April</option>
		           <option value="5">May</option>
		           <option value="6">June</option>
		           <option value="7">July</option>
		           <option value="8">August</option>
		           <option value="9">September</option>
		           <option value="10">October</option>
		           <option value="11">November</option>
		           <option value="12">December</option>
	           </select>
	           
	  		</li>
	  		<li>
	  		
	  			<select name="year" id="year" data-mini="true">
	            <%
	            int cur_year = Utilities.getYearByDate(new java.util.Date());
	            
	            for (int i = 2012; i < (cur_year+2); i++){
	            %>
	            	<option value="<%=i%>" <%if (i == cur_year){out.print("selected");} %>><%=i %></option>
	            <%
	            }
	            %>

	            </select>
	  		
	  		</li>
	  		
	  			<script>
	           
	           		$('#month').val(<%=month%>).change();
	           		$('#year').val(<%=year%>).change();
	           
	           </script>
	  		
	  			<!-- <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddIntoSession()" data-mini="true" data-iconpos="right"> -->
	  			
	  			<li data-icon="check" >
     				<a href="#" onClick="AddIntoSession()" data-iconpos="left" data-mini="true" style="font-size:10pt">Apply Preference</a>
     			</li>
  			</ul> 
           	
            
           
            
            
        	