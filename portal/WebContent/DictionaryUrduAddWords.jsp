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
<script src="js/DictionaryUrduAddWords.js"></script>

<%
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 88;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();


%>


<div data-role="page" id="DictionaryUrduAddWordsMain" data-url="DictionaryUrduAddWordsMain" data-theme="d">

    
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Dictionary Urdu Add Words" name="title"/>
    </jsp:include>

    
    <div data-role="content" data-theme="d">

	<div class="ui-grid-a">
    	<div class="ui-block-a" style="width:20%;">
    		<div class="ui-bar ui-bar-c1" style=" min-height:200px; font-size:10pt; margin-top:-5px;">
    			
    			
    			
    			<ul data-role="listview" data-inset="true" data-theme="d" style="font-size:10pt">
    				<li data-icon="arrow-r" data-theme="d" data-role="list-divider">Most Common Words</li>
    				<%
    					ResultSet rs1 = s.executeQuery("SELECT name,count(name) as counter FROM common_dictionary_urdu_words where name not in (select english from common_dictionary_urdu)  group by name having count(name)>1 order by counter desc");
    					while(rs1.next()){
    				%>
    				
    				<li><a href="#" style="font-size:10pt" onClick="SetCommonWord('<%=rs1.getString("name") %>')"><%=rs1.getString("name") %>(<%=rs1.getLong("counter") %>)</a></li>
    				<%
    					}
    				%>
    			</ul>
    		</div>
    	</div>    	
    	<div class="ui-block-b" style="width:80%;">
    		<div class="ui-bar ui-bar-c1" style="min-height:60px">
    			<form id="DictionaryUrduAddWordsForm" data-ajax="false" >
	    			<table border="0" style="width:100%">
	    				<tr>
	    					<td style="width:40%">
	    						<input type="text" name="EnglishWord" id="EnglishWord" placeholder="English Word" data-mini="true" style="font-family: Tahoma; font-size: 16pt;"/>
	    					</td>
	    					<td style="width:40%">
	    						<input type="text" name="UrduWord" id="UrduWord" placeholder="Urdu Word" data-mini="true" onkeydown="return changeToUrduinput();" style="font-family: Tahoma; font-size: 16pt; text-align: right;"/>
	    						<input type="hidden" name="UrduWordHidden" id="UrduWordHidden"/>
	    						<input type="hidden" name="DictionaryIsEditFlag" id="DictionaryIsEditFlag" value="0"/>
	    						<input type="hidden" name="DictionaryWordID" id="DictionaryWordID"/>
	    					</td>
	    					<td style="width:10%"><a href="#" data-role="button" data-mini="true" o1nClick ="UrduDictionaryWords()" onClick="AddWords()">Save</a></td>
	    					<td style="width:10%"><a href="#" data-role="button" data-mini="true"  onClick="Reset()">Reset</a></td>
	    				</tr>
	    				<tr>
	    					<td colspan="4">
		    					<br/>
		    					<ul data-role="listview" data-filter="true" data-filter-placeholder="Search word" data-inset="true" id="AllWordsULlist" data-mini="true">
		    						<%
		    						int count=0;
		    						ResultSet rs = s.executeQuery("select * from common_dictionary_urdu");
		    						while(rs.next()){		
		    						%>
		    						<li><a href="#" style="font-size:16pt;font-family: Tahoma;font-weight:normal;" onClick="EditCase(<%=rs.getString("id") %>,'<%=rs.getString("english") %>','<%=rs.getString("urdu_unicode") %>')"><%=rs.getString("english") %> - <%=rs.getString("urdu_unicode") %></a></li>
		    						<%
		    						count++;
		    						}
		    						%>
								</ul>
								<%
								if(count==0){ //mean no record
		    						%>
		    						No Record exists.
								    <%} %>
	    					</td>
	    				</tr>
	    			</table>
    			</form>
    		</div>
    	</div>
	</div><!-- /grid-a -->

	

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


