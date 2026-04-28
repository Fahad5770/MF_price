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
            
            
            Datasource ds = new Datasource();
            ds.createConnection();
            Statement s = ds.createStatement();        
           %>
           
           
           
         
			<fieldset data-role="controlgroup" >
			<input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
			
			
			<!--  <legend style="font-weight:bold; font-size:10pt;">All Packages</legend>-->
           <%
           boolean CheckBoxSelected=false;           
           long SelectedBrandArray[] = null;
           if (session.getAttribute("SR1SelectedBrands") != null){
        	   SelectedBrandArray = (long[])session.getAttribute("SR1SelectedBrands");           	
           }
           
            ResultSet rs = s.executeQuery("select * from inventory_brands");
            
            while(rs.next()){
            	if (session.getAttribute("SR1SelectedBrands") != null){
            		for(int i=0;i<SelectedBrandArray.length;i++)
                	{            		
              			if(rs.getLong("id")==SelectedBrandArray[i])
              			{
              				CheckBoxSelected = true;            			
              			}            		
                	}
            	}
            	
            	%>    		
           			<input type="checkbox" name="BrandsCheckBox" id="BrandID_<%=rs.getString("id") %>" value="<%=rs.getString("id") %>" <%if(CheckBoxSelected){ %>checked<%} %> onClick="ChangeLabelBrands('<%=ClickIDD%>','PackageID_<%=rs.getString("id") %>')" data-mini="true">
   					<label for="BrandID_<%=rs.getString("id") %>" ><span style="font-size:10pt; font-weight:normal;"><%=rs.getString("label") %></span></label> 
            		
				
            	<%
            	CheckBoxSelected=false;
            }
            s.close();            
            ds.dropConnection();
            %>
            <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddPackagesIntoSession()" data-mini="true">
            </fieldset>
           <!--</ul>-->
            
            
        	