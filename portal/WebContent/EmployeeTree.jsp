<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.sql.ResultSet"%>
<jsp:useBean id="empTree" class="com.pbc.employee.EmployeeTree" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee List</title> 
	<meta name="viewport" content="width=device-width, initial-scale=1"> 

	<!--  Tree files -->
	
	<link rel="stylesheet" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" href="css/jquery.jOrgChart.css"/>
    <link rel="stylesheet" href="css/custom.css"/>
    <link href="css/prettify.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="js/prettify.js"></script>
    
    
    
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.2.1/jquery.mobile-1.2.1.min.css" />
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
	<ascript src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
	<ascript src="http://code.jquery.com/mobile/1.2.1/jquery.mobile-1.2.1.min.js"></script>
    <!-- jQuery includes -->
    
    <!--  Jquery popup files -->
    
    <link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
	<script type="text/javascript" src="js/script.js"></script>
    
    <!-- ///////////////////////// -->
    
    
    <script src="js/jquery.jOrgChart.js"></script>
    <script src="js/EmployeeTree.js"></script>

   
	
	<!--  ///////////////////////////////////////////////////// -->
	
<style type="text/css">
 
   .grayColorDesig
   {
   	 color:gray;
   }
   .grayColorDesigSelected
   {
     color:white; 
   }
    a
   {
   }
   a:hover
   {
   	text-decoration:none;
   }
   </style>	
	
</head>
<body onload="prettyPrint();">
<div >

	

<!-- <label><a href="javascript:jsonOutput()">JSON</a></label> --> 
<!-- <label><a href="javascript:testtt()">test</a></label> -->
<!-- <label><a href="javascript:html_output()">HTML</a></label>-->

 <!--  <label id="htmlid">

</label>-->
<div style="width:700px; min-height:50px; float:right; b1ackground-color:red;">
	<div style="width:700px; min-height:50px; b1ackground-color:red; float:left;">
				
				<div style="float:left; width:250px; ">	         
		         <form method="post" id="QuickAddEmployeeTreeForm">
		         <input type="text" name="quickAddSapCode" id="quickAddSapCode" value=""  placeholder="Search by SAP Code" class="ui-input-text ui-body-null ui-corner-all ui-shadow-inset ui-body-c" style="height:30px;">
				 </form> 
				</div>
				<div style="width:70px;;float:left; margin-left:10px; " id="tesssssttttt" >
				  <a href="javascript:AddQuickEmployeeTree()" title="Quick Add Employee By Entering SAP Code">
				  <div  class="ui-btn ui-shadow ui-btn-corner-all ui-submit ui-btn-up-c" style="height:30px;">
							<span class="ui-btn-inner ui-btn-corner-all" style="margin-top:-2px;"><span class="ui-btn-text" style="font-size:11px;">Add</span></span>
							<input type="button" value="Submit Button" class="ui-btn-hidden" >
						</div>
				  </a>
				  
				  </div>
				  <a href="#" class="topopup"  onClick="toggleEmployeelist()" title="Select Employee From List">
					<div style="float:left;  width:180px; margin-right:10px; margin-left:10px;" >
					<div  class="ui-btn ui-shadow ui-btn-corner-all ui-submit ui-btn-up-c " style="height:30px;">
									<span class="ui-btn-inner ui-btn-corner-all" style="margin-top:-3px;"><span class="ui-btn-text" style="font-size:13px;">Select Employee
					</span></span>
										<input type="submit" value="Submit Button" class="ui-btn-hidden" >
									</div>
					</div></a>
					<a href="#" title="Save the Tree"><div style="float:left;  width:100px;">
					<form method="post" action="employee/EmployeeTreeToJSON" id="JSONToJava" onclick="JSONToObject();">
						<input type="hidden" name="JSONString" id="JSONString"/>
						<div  class="ui-btn ui-shadow ui-btn-corner-all ui-submit ui-btn-up-c" style="height:30px;">
							<span class="ui-btn-inner ui-btn-corner-all" style="margin-top:-2px;"><span class="ui-btn-text" style="font-size:13px;">Save</span></span>
							<input type="submit" value="Submit Button" class="ui-btn-hidden" >
						</div>
					</form></a>
					</div>
	</div>

</div>

<div style="float:right; width:500px; height:50px; margin:10px; ">
</div>



	<div data-role="content" style="min-height:500px;">	
		<div data-role="content" style="width:100%; b1order:1px solid #3e6790; min-height:420px; float:left;" id="Orgger">
		
		 <!--<ul  id="org">
		    <li><a href="#" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-e ui-btn-up-e"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">3214657890sdfsdf<br/>Root<br/>SAP<br/>SSE</span></span></a>
		        <ul id="main_child_ul" class="children">
		      	<li>
		      		<a href="#" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">1</span></span></a>
					<ul class="children">
						<li><a href="#" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">1</span></span></a></li>
						<li><a href="#" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">1</span></span></a></li>
					</ul>			      	
		      	</li>
		      	<li><a href="#" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">2</span></span></a></li>
		      	<li><a href="#" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">3</span></span></a></li>
		     
		      </ul> 
		    </li>
		     
   		</ul>--> 
   		<%
		       String x=empTree.Operator();
		       out.println(x);
		       %>
   		           
    
    <div id="chart" class="orgChart"></div>
    
    <script>
        jQuery(document).ready(function() {
            
            /* Custom jQuery for the example */
            $("#show-list").click(function(e){
                e.preventDefault();
                
                $('#list-html').toggle('fast', function(){
                    if($(this).is(':visible')){
                        $('#show-list').text('Hide underlying list.');
                        $(".topbar").fadeTo('fast',0.9);
                    }else{
                        $('#show-list').text('Show underlying list.');
                        $(".topbar").fadeTo('fast',1);                  
                    }
                });
            });
            
            $('#list-html').text($('#org').html());
            
            $("#org").bind("DOMSubtreeModified", function() {
                $('#list-html').text('');
                
                $('#list-html').text($('#org').html());
                
                prettyPrint();                
            });
        });
    </script>
		
		</div>
		
		
		
    <div id="toPopup"> 
    	
        <div class="close"></div>
       	<span class="ecs_tooltip">Press Esc to close <span class="arrow"></span></span>
		<div id="PopupContent"> <!--your content start-->
           <div data-role="content" style="width:100%; height:400px;float:left; overflow:scroll; b1order:1px solid #3e6790;" id="EmployeeListDiv">
			<input type="hidden" id="ToggleBit" value="0"/>
			<input type="hidden" id="ToggleBitLi" value="0"/>
			
			<div data-role="fieldcontain" class="ui-hide-label" style="margin-left:10px;">	         
	         <input type="email" name="sapCode" id="sapCode" value=""  placeholder="Search by SAP Code" class="ui-input-text ui-body-null ui-corner-all ui-shadow-inset ui-body-c" style="height:40px; width:100%">
			</div>
			
			<div class="ui-select" style="margin-left:10px;">
				<div class="ui-btn ui-btn-icon-right ui-btn-corner-all ui-shadow ui-btn-up-c" >
					<span class="ui-btn-inner ui-btn-corner-all">
						<span class="ui-btn-text" id="PleaseSelectDptID">Please Select Department</span>
							<span class="ui-icon ui-icon-arrow-d ui-icon-shadow"></span></span>
							<%			
								ResultSet DepartmentRS = empTree.loadAllDepartments();								
							%>
							<select name="DepartmentSelectionFilter" id="DepartmentSelectionFilter">
								<option value="-1">Select Department</option>
								<% 
									while (DepartmentRS.next()){									
								%>
								<option value="<%=DepartmentRS.getString("orgeh") %>"><%=DepartmentRS.getString("orgtx") %></option>
								<% } %>
								
							</select>
				</div>
		    </div>
		    
		   
			
			
			<%
			//ResultSet rs = null;
			ResultSet rs = empTree.getAllEmployees();
			int counter=0;
			%>
			<div id="FilteredEmployeeByDepartment">
				<ul data-role="listview" id="employee_list" class="ui-listview" >
					
				</ul>
			</div>	
			
			
			
			<%			
			empTree.close();
			%>
			
			
			<!--<ul data-role="listview" id="employee_list" class="ui-listview">
				<li id="li_id_1" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="1" class="ui-link-inherit">Acura</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_2" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="2" class="ui-link-inherit">Audi</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_3" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="3" class="ui-link-inherit">BMW</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_4" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="4" class="ui-link-inherit">Cadillac</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_5" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="5" class="ui-link-inherit">Ferrari</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_6" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="6" class="ui-link-inherit">Acura1</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_7" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="7" class="ui-link-inherit">Audi1</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_8" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="8" class="ui-link-inherit">BMW1</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_9" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="9" class="ui-link-inherit">Cadillac1</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_10" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="10" class="ui-link-inherit">Ferrari1</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_11" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="11" class="ui-link-inherit">Acura2</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_12" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="12" class="ui-link-inherit">Audi2</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_13" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="13" class="ui-link-inherit">BMW2</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_14" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="14" class="ui-link-inherit">Cadillac2</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
				<li id="li_id_15" class="ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li ui-li-last"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="#" id="15" class="ui-link-inherit">Ferrari2</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
			</ul>-->
			
			</div>
			
        </div> <!--your content end-->
    
    </div> <!--toPopup end-->
    
	<div class="loader"></div>
   	<div id="backgroundPopup"></div>
		
		
			
	</div><!-- /content -->
	
	<div data-role="footer" data-theme="b">
		<h1></h1>
	</div><!-- /header -->

</div><!-- /page -->
</body>
</html>