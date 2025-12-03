<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>

<style>
.ui-btn {
text-align: left
}
</style>

<script>

</script>

        	
        	
            <%
            
            long UniqueVoID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
            
            String ClickedTitle = Utilities.filterString(request.getParameter("Title"),1, 500);
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
         
<fieldset data-role="controlgroup" data-corners="true" style="margin-left: 0px; margin-top: -10px; padding-top: 0px;">
			<input type="button" name="Apply" id="Apply" data-theme="a" value="Channels" style="text-align: left" data-mini="true">
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
			
			<!--  <legend style="font-weight:bold; font-size:10pt;">All Packages</legend>-->
           <%
           boolean CheckBoxSelected=false;           
           long SelectedChannelsArray[] = null;
           if (session.getAttribute(UniqueVoID+"_SR1SelectedChannels") != null){
        	   SelectedChannelsArray = (long[])session.getAttribute(UniqueVoID+"_SR1SelectedChannels");           	
           }
           
            ResultSet rs = s.executeQuery("select id, label from pci_sub_channel");
            
            while(rs.next()){
            	if (session.getAttribute(UniqueVoID+"_SR1SelectedChannels") != null){
            		for(int i=0;i<SelectedChannelsArray.length;i++)
                	{            		
              			if(rs.getLong("id")==SelectedChannelsArray[i])
              			{
              				CheckBoxSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    	
            		<input type="checkbox" name="ChannelsCheckBox" id="ChannelID_<%=rs.getString("id") %>" value="<%=rs.getString("id") %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabelChannels('<%=ClickIDD%>')" data-mini="true">
   					<label for="ChannelID_<%=rs.getString("id") %>" ><span style="font-size:9pt; font-weight:normal;"><%=rs.getString("label") %></span></label> 
				
            	<%
            	CheckBoxSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true" data-iconpos="right">
            </fieldset>
           <!--</ul>-->
            
            
        	