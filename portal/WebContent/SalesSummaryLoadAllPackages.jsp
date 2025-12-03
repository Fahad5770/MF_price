<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>

<script>

</script>

        	
        	
            <%
            String ClickIDD = Utilities.filterString(request.getParameter("ClickID"),1,100);
            String ClickedTitle = Utilities.filterString(request.getParameter("Title"),1, 500);
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
          <!--  <ul data-role="listview" data-inset="true"  style="font-size:10pt;" data-icon="false">
				<li data-role="list-divider" data-theme="c">
					Filter By
					<!-- <span style="width:100px; min-height:10px; b1ackground-color:green; float:right;">
						<a href="index.html" data-role="button" data-mini="true">Apply</a>
					</span> -->
				</li>
			 <!--  <span style="width:100px; min-height:10px; b1ackground-color:green; float:right; margin-top:-37px;">
					<a href="index.html" data-role="button" data-mini="true">Apply</a>
			</span>  -->	 
			<fieldset data-role="controlgroup" >
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
			
			
			<!--  <legend style="font-weight:bold; font-size:10pt;">All Packages</legend>-->
           <%
           boolean CheckBoxSelected=false;           
           long SelectedPackagesArray[] = null;
           if (session.getAttribute("SR1SelectedPackages") != null){
           	SelectedPackagesArray = (long[])session.getAttribute("SR1SelectedPackages");           	
           }
           
            ResultSet rs = s.executeQuery("select * from inventory_packages");
            
            while(rs.next()){
            	if (session.getAttribute("SR1SelectedPackages") != null){
            		for(int i=0;i<SelectedPackagesArray.length;i++)
                	{            		
              			if(rs.getLong("id")==SelectedPackagesArray[i])
              			{
              				CheckBoxSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    		
           			<input type="checkbox" name="PackagesCheckBox" id="PackageID_<%=rs.getString("id") %>" value="<%=rs.getString("id") %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabel('<%=ClickIDD%>','PackageID_<%=rs.getString("id") %>')" data-mini="true">
   					<label for="PackageID_<%=rs.getString("id") %>" ><span style="font-size:10pt; font-weight:normal;"><%=rs.getString("label") %></span></label> 
            		
				
            	<%
            	CheckBoxSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
            </fieldset>
           <!--</ul>-->
            
            
        	