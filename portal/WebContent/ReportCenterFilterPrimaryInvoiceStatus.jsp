<%@page import="org.apache.commons.lang3.time.DateUtils"%>
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
.ui-btn {
text-align: left
}
</style>
<script>

</script>

            <%
            long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
         
			<fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Invoice Status" style="text-align: left" data-mini="true">
			
           <%
          
           
          
            
            
            boolean IsPrimaryInvoiceStatusSelected=false;
            String SelectedPrimaryInvoiceStatusArray[] = null;
            if (session.getAttribute(UniqueVoID+"_SR1SelectedPrimaryInvoiceStatus") != null){
            	SelectedPrimaryInvoiceStatusArray = (String[])session.getAttribute(UniqueVoID+"_SR1SelectedPrimaryInvoiceStatus");           	
            }
            
            boolean LiftedChecked = false;
            boolean NotLiftedChecked = false;
            boolean PaidChecked = false;
            boolean UnPaidChecked = false;
            
            if (session.getAttribute(UniqueVoID+"_SR1SelectedPrimaryInvoiceStatus") != null){
            	for(int i=0;i<SelectedPrimaryInvoiceStatusArray.length;i++){
                	if(SelectedPrimaryInvoiceStatusArray[i].equals("Lifted")){
                		LiftedChecked = true;
                	}
                	if(SelectedPrimaryInvoiceStatusArray[i].equals("NotLifted")){
                		NotLiftedChecked = true;
                	}
                	if(SelectedPrimaryInvoiceStatusArray[i].equals("Paid")){
                		PaidChecked = true;
                	}
                	if(SelectedPrimaryInvoiceStatusArray[i].equals("UnPaid")){
                		UnPaidChecked = true;
                	}
                }
                	
            }
            
            
            	
            	%>    		
           			<input type="checkbox" name="PrimaryInvoiceStatus" id="PrimaryInvoiceStatus_1" value="Lifted" <%if(LiftedChecked){ %>checked<%} %> data-mini="true">
   					<label for="PrimaryInvoiceStatus_1" ><span style="font-size:9pt; font-weight:normal;">Lifted</span></label> 
            		
            		<input type="checkbox" name="PrimaryInvoiceStatus" id="PrimaryInvoiceStatus_2" value="Not Lifted" <%if(NotLiftedChecked){ %>checked<%} %> data-mini="true">
   					<label for="PrimaryInvoiceStatus_2" ><span style="font-size:9pt; font-weight:normal;">Not Lifted</span></label> 
            		
            		<!-- 
            		<input type="checkbox" name="PrimaryInvoiceStatus" id="PrimaryInvoiceStatus_3" value="Paid" <%if(PaidChecked){ %>checked<%} %> data-mini="true">
   					<label for="PrimaryInvoiceStatus_3" ><span style="font-size:9pt; font-weight:normal;">Paid</span></label> 
            		
            		
            		<input type="checkbox" name="PrimaryInvoiceStatus" id="PrimaryInvoiceStatus_4" value="UnPaid" <%if(UnPaidChecked){ %>checked<%} %> data-mini="true">
   					<label for="PrimaryInvoiceStatus_4" ><span style="font-size:9pt; font-weight:normal;">UnPaid</span></label> 
            		 -->
            		
				
            	<%
            	
            
            
           
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	