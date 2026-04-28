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

<script>

</script>

        	
        	
            <%
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
         <fieldset data-role="controlgroup" >
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
			
           <%
           boolean CheckBoxSelected=false;           
           long SelectedPackagesArray[] = null;
           if (session.getAttribute("SR1SelectedDistributors") != null){
           	SelectedPackagesArray = (long[])session.getAttribute("SR1SelectedDistributors");           	
           }
            long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
            Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 67);
            
           for(int x=0;x<UserDistributor.length;x++){
            	
        	   if (session.getAttribute("SR1SelectedDistributors") != null){
            		for(int i=0;i<SelectedPackagesArray.length;i++)
                	{            		
              			if(UserDistributor[x].DISTRIBUTOR_ID==SelectedPackagesArray[i])
              			{
              				CheckBoxSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    		
           			<input type="checkbox" name="DistributorCheckBox" id="DistributorID_<%=UserDistributor[x].DISTRIBUTOR_ID %>" value="<%=UserDistributor[x].DISTRIBUTOR_ID %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabelDistributor('<%=ClickIDD%>')" data-mini="true">
   					<label for="DistributorID_<%=UserDistributor[x].DISTRIBUTOR_ID %>" ><span style="font-size:10pt; font-weight:normal;"><%=UserDistributor[x].DISTRIBUTOR_NAME %></span></label> 
            		
				
            	<%
            	CheckBoxSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
            </fieldset>
           
            
            
        	