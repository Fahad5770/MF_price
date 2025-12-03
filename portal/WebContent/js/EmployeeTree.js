/* 
	author: zulqurnan
*/

var out = []; //for tree traversal
    var counter = 0;
    jQuery(document).ready(function() {
        $("#org").jOrgChart({
            chartElement : '#chart',
            dragAndDrop  : true
        });
        //Filter by Department
         $('#DepartmentSelectionFilter').change(function() {
        	 //alert($("#DepartmentSelectionFilter option:selected").text());
        	 $("#PleaseSelectDptID").html($("#DepartmentSelectionFilter option:selected").text());
        	 $("#FilteredEmployeeByDepartment").html("Loading..."); 
        	 showEmployeeByDepartment(this.value); //alert(this.value);        	   
        	});
         
       //Filter by Designation
         $('#DesignationSelectionFilter').change(function() {
        	 $("#PleaseSelectDesigID").html($("#DesignationSelectionFilter option:selected").text());
        	 $("#FilteredEmployeeByDepartment").html("Loading..."); 
        	 showEmployeeByDesignation(this.value); //alert(this.value);        	   
        	});
         
         //search by SAP Code
         $('#sapCode').keyup(function(){
        	 $("#FilteredEmployeeByDepartment").html("Loading..."); 
        	 searchBySAPCode($('#sapCode').val());        	    
        	});
         
         $('div.toggleDiv').click(function(){
        	 toggleEmployeelist();
        	});
        
    });
    
    function selectedColorChange(id,aid)
    {
    	//alert(id);
    	 $("#"+id).removeClass('ui-btn-up-c');
         $("#"+id).addClass('ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-b');
         $("#"+aid).removeClass('grayColorDesig');
         $("#"+aid).addClass('grayColorDesigSelected');       
       
         //toggle bit
         if($('#ToggleBitLi').val() == "1")
         { 
         	$("#"+id).addClass('ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c');
         }
         else
         { $('#ToggleBitLi').val("0"); }
    }
    
    function addNodeToList(SapCode,Name,Department,Designation,DeptID,DesigID)
    {
    	//var LiID = this.id; 
    	//alert(LiID);
    	$("#chart").html(" ");    	
    	var Title = '<li><a href="#" id="'+SapCode+'#'+DeptID+'#'+DesigID+'" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-hover-b ui-btn-up-b"><span class="ui-btn-inner"><span class="ui-btn-text" style="font-size:10px;">'+SapCode+'<br/>'+Name+'<br/>'+Department+'<br/>'+Designation+'</span></span></a></li>';
    	$("#main_child_ul").append(Title);
    	$("#org").jOrgChart({
            chartElement : '#chart',
            dragAndDrop  : true
        });
    }
    
    function processOneLi(node) {       
    	//alert("hello");
        var aNode = node.children("a:first");
        var retVal = {
            "title": aNode.attr("title"),
            //"url": aNode.attr("href"),
            //"name": aNode.attr('id')
            "name" : aNode.attr('id'),
            "parentID" : aNode.text(),
            "designation": aNode.text(),
            "department": aNode.text()
        };

        node.find("> .children > li").each(function() {
            if (!retVal.hasOwnProperty("children")) {
                retVal.children = [];
            }
            retVal.children.push(processOneLi($(this)));
        });

        return retVal;
    }
    
    function addClass() //add 'class' for base condition for recursive function
    {
    	$('#org ul').each(function(i)
		{
    		$(this).attr("class","children");
		});
    }
    
    
    function jsonOutput()
    {
    	//alert();
    	out = [];
    	addClass();
    	$("#htmlid").html($("#org").html());    	
        $("#Orgger > ul > li").each(function() {
            out.push(processOneLi($(this)));
            
        });
        
    	//console.log("got the following JSON from your HTML:", JSON.stringify(out));
        $("#JSONString").val(JSON.stringify(out));
    }
    
    function toggleEmployeelist()
    {
    	//alert("hello 22");
    	$("#EmployeeListDiv").show();
    	/*if($("#ToggleBit").val() == "0") //already visible - hide it
    	{
    		alert("in ifs");
    		$("#EmployeeListDiv").hide();
    		$("#ToggleBit").val("1");//change the bit
    		$("#Orgger").width("80%");
    	}
    	else
    	{
    		alert("in else");
    		$("#EmployeeListDiv").show();
    		$("#ToggleBit").val("0");//change the bit
    		$("#Orgger").width(700);
    	}*/
    	
    }
    
    function showEmployeeByDepartment(DepartmentID)
    {    	 
    	var DesigID="-1";
    	//if($("#DesignationSelectionFilter option:selected").val() != "-1") //mean if designation is also selected
    	//{ DesigID = $("#DesignationSelectionFilter option:selected").val(); }
    	/*$.get('EmployeeByDeparment.jsp?DepartmentID='+DepartmentID+'&DesignationID='+DesigID, function(data) {   			
   			  $("#FilteredEmployeeByDepartment").html(data);  			  
   		}); */
    	
    	$.get('EmployeeTreeEmployeeSearchByDeparment.jsp?DepartmentID='+DepartmentID, function(data) {   			
 			  $("#FilteredEmployeeByDepartment").html(data);  			  
 		}); 
    }
    
    function showEmployeeByDesignation(DesignationID)
    {    	 
    	var DeptID="-1"; 
    	if($("#DepartmentSelectionFilter option:selected").val() != "-1") // mean if department is also selected
    	{ DeptID = $("#DepartmentSelectionFilter option:selected").val(); }
    	$.get('EmployeeByDesignation.jsp?DesignationID='+DesignationID+'&DepartmentID='+DeptID, function(data) {   			
   			  $("#FilteredEmployeeByDepartment").html(data);    			  
   		});   		
    }
    
    function searchBySAPCode(SAPCode)
    {
		$.get('EmployeeTreeEmployeeSearchBySAPCode.jsp?SAPCode='+SAPCode, function(data) {   			
			  $("#FilteredEmployeeByDepartment").html(data);  			  
		});   		
		
    }
    
    function JSONToObject()
    {    	
    	jsonOutput();
    	//alert( $("#JSONString").val());
    	$("#JSONToJava").submit();
    }
    
    function AddQuickEmployeeTree(){
		
		
		$.ajax({
		    url: "employee/QuickAddEmployeeTree",
		    
		    data: $("#QuickAddEmployeeTreeForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if(json.SapCode == "-1")
	    		{
		    		alert("No such Employee exist");
	    		}
		    	else
	    		{
		    		addNodeToList(json.SapCode,json.Name,json.Department,json.Designation,json.DeptID,json.DesigID);
	    		}
		    	
		    	
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
		
	}
    
    