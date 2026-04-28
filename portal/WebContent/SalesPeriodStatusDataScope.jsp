<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
			
			<div  data-theme="c" data-content-theme="d" id="datascope">
	    		
			
			    	
			    	<div data-role="collapsible" data-collapsed="false" data-theme="c" data-content-theme="d" id="datascope" data-mini="true">
		    		<h4 id="TitleGoesHere" style="width:100%">Distributor</h4>
		    		
		    		<table border="0" width="100%" id="DistributorTable">
					<tr>
						<td style="width:20%">
							<input type="text" name="DistributorID2" id="DistributorID2" placeholder="ID" data-mini="true" onChange="getDistributorName2()"/>
						</td>
						<td style="width:50%">
							<input type="text" name="DistributorName2" id="DistributorName2" placeholder="Distributor Name" data-mini="true" readonly /> 
							<input type="hidden" name="isSecondDistCall" id="isSecondDistCall" value="0"/>
						</td>
						<td style="width:30%"><a href="#" data-role="button" data-icon="plus" data-iconpos="left" data-inline="true" onClick="AddDistributor()" data-mini="true">Add</a></td>
					</tr>
					</table>
		    		<div id="AddeddDistributor" style="width:100%">
		    		<ul data-role="listview" data-inset="true" id="populateDistributorDataUl" data-mini="true">
		    		</ul>
				</div>
				
				
				
				</div>
				
		
			