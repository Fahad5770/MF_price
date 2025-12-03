<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>


<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/Synchronization.js?4=4"></script>





<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 81;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();

%>



<div data-role="page" id="Synchronization" data-url="Synchronization" data-theme="d">

    
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Synchronization" name="title"/>

    		

    </jsp:include>

    
    <div data-role="content" data-theme="d">

	<form id="ReturnsGenerrateForm" name="ReturnsGenerrateForm" action="DispatchReturns.jsp" method="POST" data-ajax="false">		 		
		 		<div class="ui-grid-a">
				    <div class="ui-block-a">
				    	<div class="ui-bar" style="min-height:60px">
				    		<ul data-role="listview" data-inset="true" data-mini="true" id="test">			   
								    <li data-role="list-divider" data-theme="c">
									Synchronization (SAP)
								     </li>
						    		
					        		<li data-icon="arrow-r" id="list_id_1">
					        			<a href="" onClick="ShowDate(1)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Distributors</td>
					        						
					        					</tr>
					        				</table>
					        			</a>
					        		</li>		
				        			
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(2)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Employees</td>
					        						
					        					</tr>
					        				</table>
					        			</a>
					        		</li>		
				        			
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(3)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Outlets</td>
					        						
					        					</tr>
					        				</table>
					        			</a>
					        		</li>		
				        			
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(4)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Sales</td>
					        						
					        					</tr>
					        				</table>
					        			</a>
					        		</li>
					        		
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(10)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Orders and Invoices</td>
					        						
					        					</tr>
					        				</table>
					        			</a>
					        		</li>
					        		
					        		
					        			
								    <li data-role="list-divider" data-theme="c">
									Internal Batch Processes
								     </li>				        			
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(5)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Sales</td>
					        						
					        					</tr>
					        				</table>
					        			</a>
					        		</li>
					        		
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(6)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Order Booker Statistics</td>
					        					</tr>
					        				</table>
					        			</a>
					        		</li>
					        		
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(8)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Promotions Cache</td>
					        					</tr>
					        				</table>
					        			</a>
					        		</li>
					        		<li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(9)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Hand to Hand Discount</td>
					        					</tr>
					        				</table>
					        			</a>
					        		</li>
					        		
					        		
					        		 <li data-role="list-divider" data-theme="c">
									Utilities
								     </li>
								     <li data-icon="arrow-r">
					        			<a href="" onClick="ShowDate(7)">
					        				<table style="width:100%;font-size:10pt;">
					        					<tr>
					        						<td style="width:50%">Reconnect SAP	</td>
					        						
					        					</tr> 
					        				</table>
					        			</a>
					        		</li>	
			    				</a>
			  			 	</li>
			    <input type="hidden" name="SyncActionValue" id="SyncActionValue" value=""/>
							</ul>
				    	</div>
				    </div>
				    <div class="ui-block-b">
				    	<div class="ui-bar" style="min-height:300px">
				    		<ul data-role="listview" data-inset="true" data-mini="true">
			   				<li data-role="list-divider" data-theme="c"><h3 style="font-size:10pt;">&nbsp;<span id="title"></span></h3></li>
							    <li style="background-color:#fff">
							    	<table border="0" style="width: 50%; font-size:10pt;">
								    		
								    		<tr>
									        	<td><h3 style="font-size:10pt;">Status</h3></td>
									        	
							        		</tr>
							        	
							        		<tr>							        		
							        		<td class="ui-disabled" id="StartDateTD"><input type="text" name="StartDate" id="StartDate" placeholder="From (DD/MM/YYYY)" onChange="EnableStartBtn()"/></td>
							        		</tr>
							        		<tr>							        		
							        		<td class="ui-disabled" id="EndDateTD"><input type="text" name="EndDate" id="EndDate" placeholder="To (DD/MM/YYYY)" onChange="EnableStartBtn()"/></td>
							        		</tr>
							        		<tr>							        		
							        		<td>
							        			<table style="width:100%; b1order:1px solid green;">
							        				<tr>
							        					<td style="width:20%"><h3 style="font-size:10pt;">Progress</h3></td>
							        					<td style="width:80%"><span id="SyncInProgressStatus" style="font-size:10pt;"></span></td>
							        				</tr>
							        			</table>
							        		</td>
							        		</tr>
							        		<tr>
							        		<td id="SyncBtnTD" class="ui-disabled"><input type="button"  value="Start" data-theme="c" onClick="Synchronize();"/></td>							        		
							        		</tr>
							        		
							        		
							        	</table>
							     </li>
				        	
							    	</ul>
							    	
							    	<ul data-role="listview" data-inset="true" data-mini="true">
			   							<li data-role="list-divider" data-theme="c"><h3 style="font-size:10pt;">Log</h3></li>
									    <li style="background-color:#fff">
									    <span id="LogOutputMsg" style="font-size:10pt;font-weight:normal"></span>
									    </li>				        	
							   		</ul>
				    	</div>
				    </div>
				</div><!-- /grid-a -->
		 		
		 		
		 		
		 		<table style="width:100%">
			    	<tr>
			    		<td style="width:50%">
			    		
			    		
			    		
			    		
			    		
			    		
			    		</td>
			    		<td style="width:50%">
			    		
			    		
			    
			
			    		
			    		
			    		
			    		
			    		</td>
			    	</tr>
			    
			    </table>
    		
    			
    			
    			
    			
    		
    		
    		
    		
    		
		
	</form>


	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<!-- <button data-icon="check" data-theme="a" data-inline="true" id="RetursGenerateButton" onClick="GetAllSalesForReturn()">Generate Returns</button>
		 -->
	</div>    	
    </div>
	

</div>

</body>
</html>



<%
s.close();
ds.dropConnection();
%>