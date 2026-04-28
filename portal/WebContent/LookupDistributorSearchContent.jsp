<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<script>

$(".tr_class_distributor").not('first').hover(
  function () {
    $(this).css("background","#ececec");
	$(this).css("cursor","pointer");
  }, 
  function () {
    $(this).css("background","");
  }
);

</script>
	
    <table border="0" style="width:100%">
          
            <tr>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">SAP Code</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Name</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Address</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">City</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Contact #</th>
            </tr>
          
            <%
            
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            
            
            boolean IsFound = false;
            int FeatureID = Utilities.parseInt(request.getParameter("DistributorSearchFeatureID"));
            
            if(FeatureID > 0){
            	
            
	            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
	            
				Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
				
	            
	            String DistributorQueryString = UserAccess.getDistributorQueryString(UserDistributor);
	            
	
				
	            
				long SAPCode = Utilities.parseLong(request.getParameter("DistributorSearchFormSAPCode"));
				
				String Name = Utilities.filterString(request.getParameter("DistributorSearchFormName"), 1, 100);
				String City = Utilities.filterString(request.getParameter("DistributorSearchFormCity"), 1, 100);
				String Address = Utilities.filterString(request.getParameter("DistributorSearchFormAddress"), 1, 100);
				String Contact = Utilities.filterString(request.getParameter("DistributorSearchFormContact"), 1, 100);
				
				String where = ""; 
	            
				if(SAPCode > 0){
					where += " and distributor_id like '"+SAPCode+"%' ";
				}
				
	            if(Name != null && Name.length() > 0){
	            	where += " and name like '%"+Name+"%' ";
	            }
	            if(City != null && City.length() > 0){
	            	where += " and city like '%"+City+"%' ";
	            }
	            if(Address != null && Address.length() > 0){
	            	where += " and address like '%"+Address+"%' ";
	            }
	            if(Contact != null && Contact.length() > 0){
	            	where += " and contact_no like '%"+Contact+"%' ";
	            }
	            
	            if (FeatureID != 0){
					where += " and distributor_id in ("+DistributorQueryString+")  ";
				}   
	            
	            
	            
	            ResultSet rs = s.executeQuery("SELECT * FROM common_distributors where 1=1 "+where+" order by name");
	            
	            while(rs.next()){
	            	IsFound = true;
	            	%>
	                <tr class="tr_class_distributor" onclick="lookupDistributorOnSelect(<%=rs.getString("distributor_id")%>, '<%=rs.getString("name")%>')">            
		                <td style="font-weight: 100;"><%=rs.getString("distributor_id")%></td>
	                	<td style="font-weight: 100;" nowrap="nowrap"><%=WordUtils.capitalize(Utilities.truncateStringToMax(rs.getString("name"), 15))%></td>
	                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("address"), 15)%></td>
	                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("city"), 15)%></td>
	                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("contact_no"), 15)%></td>
	                </tr>
	            	<%
	            	
	            }
            
            }
            
			if( IsFound == false ){
				%>
                <tr>
                	<td colspan="4" style="text-align:center">No record found.</td>
                </tr>
            	<%
			}
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</table>
            