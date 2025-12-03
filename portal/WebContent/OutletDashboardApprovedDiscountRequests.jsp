<%@page import="com.pbc.outlet.LedgerTransaction"%>
<%@page import="com.pbc.outlet.Advance"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.UserAccess"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 31;
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

%>

<div data-role="page" id="OutletDashboardLedger" data-url="OutletDashboardLedger" data-theme="d">

    <jsp:include page="OutletDashboardHeader.jsp" >
    	<jsp:param value="<%=bean.OUTLET.NAME %>" name="title"/>
    	<jsp:param value="2" name="tab"/>
    	<jsp:param value="<%=bean.OUTLET.ID%>" name="OutletID"/>
    </jsp:include>
     <!-- /header -->
    
    <div data-role="content" data-theme="d">
        
<table border="0" data-role="table" id="table-custom-2" class="ui-body-d ui-shadow table-stripe ui-responsive" data-column-btn-theme="c" data-column-popup-theme="a" style="font-size: 10pt;">
         <thead>
           <tr class="ui-bar-d">
             <th>Request ID</th>
             <th>Advance Company Share</th>
             <th>Fixed Company Share</th>             
             <th>Fixed Deduction Term</th>
             <th>Fixed Valid From</th>
             <th>Fixed Valid To</th>
             
             
             <th>Fixed Company Share Offpeak</th>
             <th>Fixed Deduction Term Offpeak</th>
             <th>Activated On</th>
             <th>Deactivated On</th>
             <th>Deactivation Timestamp</th>
             <th>Active</th>
           </tr>
         </thead>
         <tbody>
         	<%
         		ResultSet rs = s.executeQuery("SELECT request_id, sampling_id, advance_company_share, fixed_company_share, fixed_deduction_term, fixed_valid_from, fixed_valid_to,  active, activated_on, deactivated_on, fixed_company_share_offpeak, fixed_deduction_term_offpeak, deactivation_timestamp, ( SELECT count(sampling_id) FROM sampling_percase where sampling_id=sampling.sampling_id ) per_case_rows FROM sampling where outlet_id = "+bean.OUTLET.ID+" order by activated_on desc, active desc");
         		while( rs.next() ){
         			
         			int PerCaseRows = rs.getInt("per_case_rows");
         			
         			%>
         			<tr>
						<td><%=rs.getString("request_id")%></td>
						<td><%=rs.getString("advance_company_share")%></td>
						<td><%=rs.getString("fixed_company_share")%></td>
						<td><%=rs.getString("fixed_deduction_term")%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getDate("fixed_valid_from"))%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getDate("fixed_valid_to"))%></td>
						
						
						<td><%=rs.getString("fixed_company_share_offpeak")%></td>
						<td><%=rs.getString("fixed_deduction_term_offpeak")%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getTimestamp("activated_on"))%></td>
						<td><%=Utilities.getDisplayDateFormat(rs.getTimestamp("deactivated_on"))%></td>
						<td><% if( rs.getTimestamp("deactivation_timestamp") != null ){ out.print(Utilities.getDisplayDateFormat(rs.getTimestamp("deactivation_timestamp"))+" "+Utilities.getDisplayTimeFormat(rs.getTimestamp("deactivation_timestamp"))); } %></td>
						<td <% if( rs.getString("active").equals("1") ){%> style="background: #000; color: #fff;" <% } %> ><%=rs.getString("active")%></td>
         			</tr>
         			<% if( PerCaseRows > 0 ){ %>
         			<tr>
         				<td colspan="4">&nbsp;</td>
         				<td colspan="8" >
         					<div style="width: 100%;">
         					<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="a" data-count-theme="c">
         						<li data-role="list-divider">Sampling Detail </li>
         						<li>
		         					<table align="center" style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0">
		         					
		         						<thead>
		         						<tr>
			         						<th>Package</th>
			         						<th>Brand</th>
			         						<th>Company Share</th>
			         						<th>Hand to Hand</th>
			         						<th>Deduction Term</th>
			         						<th>Valid From</th>
		         							<th>Valid To</th>
		         						</tr>
		         						</thead>
		         						<%
		         						ResultSet rs2 = s2.executeQuery("SELECT *, ( SELECT label FROM inventory_packages where id=sampling_percase.package ) package_label, ( SELECT label FROM inventory_brands where id=sampling_percase.brand_id ) brand_label FROM sampling_percase where sampling_id="+rs.getString("sampling_id"));
		         						while( rs2.next() ){
		         							%>
		         							<tr>
				         						<td><%=rs2.getString("package_label")%></td>
				         						<td><% if( rs2.getString("brand_label") != null ){ out.print( rs2.getString("brand_label") ); }else{ out.print("All"); } %></td>
				         						<td><%=rs2.getString("company_share")%></td>
				         						<td><%=rs2.getString("hand_to_hand")%></td>
				         						<td><%=rs2.getString("deduction_term")%></td>
				         						<td><%=Utilities.getDisplayDateFormat(rs2.getDate("valid_from"))%></td>
			         							<td><%=Utilities.getDisplayDateFormat(rs2.getDate("valid_to"))%></td>
			         						</tr>
		         							<%
		         						}
		         						%>
		         					</table>
	         					</li>
         					</ul>
         					</div>
         				</td>
         			</tr>
         			<%
         			} // end if
         		}
         	%>
         	
         </tbody>
       </table>        
    </div><!-- /content -->

    <jsp:include page="Footer1.jsp" /> <!-- /footer -->

</div>

<%
bean.close();
s2.close();
s.close();
ds.dropConnection();
%>
</body>
</html>