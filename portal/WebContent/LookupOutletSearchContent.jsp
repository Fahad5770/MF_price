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
<style>
.tr_class_selected_outlet{

background:#cecece;

}
</style>

<script>

$(".tr_class_outlet").not('first').hover(
  function () {
    $(this).css("background","#ececec");
	$(this).css("cursor","pointer");
  }, 
  function () {
    $(this).css("background","");
  }
);

</script>
	
    <table border=0 style="width:100%">
          
            <tr>
                <th data-priority="1" nowrap="nowrap">SAP Code</th>
                <th data-priority="1" nowrap="nowrap">Name</th>
                <th data-priority="1" nowrap="nowrap">Address</th>
                <th data-priority="1" nowrap="nowrap">Owner</th>
                <th data-priority="1" nowrap="nowrap">Contact #</th>
            </tr>
          
            <%
            
            Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            int FeatureID = Utilities.parseInt(request.getParameter("OutletSearchFeatureID"));
            
            boolean IsFound = false;
            
            if(FeatureID > 0){
            
	            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
	
	            
	            Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	            
	            String DistributorQueryString = UserAccess.getDistributorQueryString(UserDistributor);
	            
	            
	            
				
	            
				long SAPCode = Utilities.parseLong(request.getParameter("OutletSearchFormSAPCode"));
				
				String Name = Utilities.filterString(request.getParameter("OutletSearchFormName"), 1, 100);
				String Address = Utilities.filterString(request.getParameter("OutletSearchFormAddress"), 1, 100);
				
				String Owner = Utilities.filterString(request.getParameter("OutletSearchFormOwner"), 1, 100);
				String Telephone = Utilities.filterString(request.getParameter("OutletSearchFormTelephone"), 1, 100);
				
				
				
				
	
				
	            String where = ""; 
	            
	            if(SAPCode > 0){
	            	where += " and id like '"+SAPCode+"%'";
	            }
	            
	            if(Name != null && Name.length() > 0){
	            	where += " and name like '%"+Name+"%' ";
	            }
	            
	            if(Address != null && Address.length() > 0){
	            	where += " and address like '%"+Address+"%'  ";
	            }
	            
	            if(Owner != null && Owner.length() > 0){
	            	where += " and cache_contact_name like '%"+Owner+"%'  ";
	            }
	            
	            if(Telephone != null && Telephone.length() > 0){
	            	where += " and cache_contact_number like '%"+Telephone+"%'  ";
	            }
	            
				if (FeatureID != 0){
					//where += " and cache_distributor_id in ("+DistributorQueryString+")  ";
				}            
	            
				//System.out.println("SELECT * FROM common_outlets where 1=1 "+where+" order by id");
				
	            ResultSet rs = s.executeQuery("SELECT * FROM common_outlets where 1=1 "+where+" order by id");
	            
	            while(rs.next()){
	            	IsFound = true;
	            	%>
	                <tr class="tr_class_outlet" onclick="lookupOutletOnSelect(<%=rs.getString("id")%>, '<%=rs.getString("name")%>'); $(this).removeClass('tr_class_outlet'); $(this).addClass('tr_class_selected_outlet');">            
		                <td style="font-weight: 100;"><%=rs.getString("id")%></td>
	                	<td style="font-weight: 100;" nowrap="nowrap"><%=WordUtils.capitalize(Utilities.truncateStringToMax(rs.getString("name"), 15))%></td>
	                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("address"), 15)%></td>
	                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("cache_contact_name"), 15)%></td>
	                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("cache_contact_number"), 15)%></td>
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
            