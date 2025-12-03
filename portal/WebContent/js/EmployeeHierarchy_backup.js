
var GlobalCount=0;
var chart="";
var data="";
var firstselectionFlag=true;
google.load('visualization', '1', {packages:['orgchart']});
google.setOnLoadCallback(drawChart);		    		

var DefaultLevel = "1";





$( document ).delegate("#EmployeeHierarchyMain", "pageshow", function() {
	
	if ($('#SessionUserID').val() == "2062" || $('#SessionUserID').val() == "2381"){
		DefaultLevel = "1";
	}else{
		DefaultLevel = "3";
	}
	
	getEmployeeTreeJSON(DefaultLevel);
	
	$( "#AddUnderTxtBx" ).on( "dblclick", function( event, ui ) {
		$( "#LookupUserSearch" ).on( "popupbeforeposition", function( event, ui ) {
    		lookupUserInit();
		} );
		$('#LookupUserSearch').popup("open");
	} );
	
	
	$("#chart_div").swipe( { swipeStatus:swipe2, allowPageScroll:"none" } );
	
	 //$("#chart_div").swipe( { swipeLeft:swipe1, swipeRight:swipe2, allowPageScroll:"none"} );
	
});

var lastPosition = 0;

function swipe2(event, phase, direction, distance){
	//$("#scrolltest").text(event +" "+ phase + " " + direction + " " + distance);
	

	if (phase == "start"){
		
		lastPosition = $("#chart_div").css("left");
		lastPosition = lastPosition.replace("px","");
		if (isNaN(lastPosition)){
			lastPosition = 0;
		}
		
		//alert("s "+lastPosition);
		
	}
	if (phase == "end"){
		lastPosition = $("#chart_div").css("left");
		lastPosition = lastPosition.replace("px","");
		if (isNaN(lastPosition)){
			lastPosition = 0;
		}	
		//alert("e "+lastPosition);
	}
	
	if (phase == "move"){
		
		if (direction == "left"){
			

			var moveTo = parseFloat(distance);

			if (isNaN(moveTo)){
				moveTo = 0;
			}
			moveTo = parseFloat(lastPosition) - parseFloat(moveTo);
			
			$("#scrolltest").text(event +" "+ phase + " " + direction + " " + distance );
			$("#chart_div").css("left", moveTo);	
			
			
		}
		if (direction == "right"){
			

			var moveTo = parseFloat(distance);

			if (isNaN(moveTo)){
				moveTo = 0;
			}
			moveTo = parseFloat(lastPosition) + parseFloat(moveTo);
			
			$("#scrolltest").text(event +" "+ phase + " " + direction + " " + distance );
			$("#chart_div").css("left", moveTo);	
			
			
		}

	}


}
function swipe1(event, phase, direction, distance){
	//$("#scrolltest").text("1"+event +" "+ phase + " " + direction + " " + distance);
}

function getEmployeeTreeJSON(forlevelid){	
	 $.mobile.loading( 'show');
	//alert("a");
		$.ajax({
		    url: "employee/EmployeeHierarchyJSON",		    
		    data: {
				ForLevelID1:forlevelid
			},
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
				
		    	if (json.success == "true"){
		    		//alert();	
		    		//childs1 = chart.getChildrenIndexes(0);
		    		//alert(childs1.length);  
		    		data.removeRows(0,data.getNumberOfRows());
		    		var LevelNName="";
		    		/*data.addRows([
		    		        	    [{v:'Zulqarnan', f:'Zulqarnan'}, 'Mike', '']
		    		        	  ]);*/
		    		  //chart.draw(data, {allowHtml:true,allowCollapse:true});	
		    		//alert(json.rows1.length);  
		    		var TempNodeHeadID="";
		    		if(json.rows1.length>0){//mean level view
		    			var rowcounter=0;
		    			for(var ii = 0; ii < json.rows.length; ii++){
		    				for( var j = 0; j < json.rows1.length; j++ ){
				    			 //alert(json.rows1[j].ID);
		    					if( json.rows[ii].CurrentReportingTo == json.rows1[j].ID)
	    						{
		    						TempNodeHeadID=json.rows1[j].ID;
		    						if(json.rows1[j].LevelName!=null){
					    				 LevelNName = json.rows1[j].LevelName;
					    			 }
					    			 else{
					    				 LevelNName="";
					    			 }
					    			//alert(json.rows[ii].ID);
		    						if(json.rows[ii].CurrentReportingTo=="1"){ //means root element - Reporting to no one
					    				data.addRows([
							    		        	   [{v:json.rows1[j].ID, f:'<div style="font-size: 10px;"><b>Company</b><br>CO'+'<input type="hidden" name="nodeid_'+json.rows1[j].ID+'" id="nodeid_'+json.rows1[j].ID+'" value="'+json.rows1[j].ID+'"/><input type="hidden" name="nodename_'+json.rows1[j].ID+'" id="nodename_'+json.rows1[j].ID+'" value="'+json.rows1[j].DisplayName+'"/><input type="hidden" name="nodedesignation_'+json.rows1[j].ID+'" id="nodedesignation_'+json.rows1[j].ID+'" value="'+json.rows1[j].Designation+'"/><input type="hidden" name="nodenodehirlevel_'+json.rows1[j].ID+'" id="nodehirlevel_'+json.rows1[j].ID+'" value="'+LevelNName+'"/></div>'}, '', '']
							    		        	  ]);
					    				data.setRowProperty(rowcounter, 'style', 'border:1px solid '+json.rows1[j].BorderNodeColor+'; background:'+json.rows1[j].NodeColor);
					    				rowcounter++;
		    						}
		    						else{
		    							data.addRows([
							    		        	    [{v:json.rows1[j].ID, f:'<div style="font-size: 10px;"><b>'+json.rows1[j].DisplayName+'</b><br>('+json.rows1[j].ID+')<br/>'+json.rows1[j].Designation+'<br>'+LevelNName+'<input type="hidden" name="nodeid_'+json.rows1[j].ID+'" id="nodeid_'+json.rows1[j].ID+'" value="'+json.rows1[j].ID+'"/><input type="hidden" name="nodename_'+json.rows1[j].ID+'" id="nodename_'+json.rows1[j].ID+'" value="'+json.rows1[j].DisplayName+'"/><input type="hidden" name="nodedesignation_'+json.rows1[j].ID+'" id="nodedesignation_'+json.rows1[j].ID+'" value="'+json.rows1[j].Designation+'"/><input type="hidden" name="nodenodehirlevel_'+json.rows1[j].ID+'" id="nodehirlevel_'+json.rows1[j].ID+'" value="'+LevelNName+'"/></div>'}, '', '']
							    		        	  ]);
					    				data.setRowProperty(rowcounter, 'style', 'border:1px solid '+json.rows1[j].BorderNodeColor+'; background:'+json.rows1[j].NodeColor);
					    				rowcounter++;
		    						}
		    						
		    						
					    				
	    						}
		    					
				    			
				    		}
		    				if(json.rows[ii].LevelName!=null){
			    				 LevelNName = json.rows[ii].LevelName;
			    			 }
			    			 else{
			    				 LevelNName="";
			    			 }
			    			if(json.rows[ii].ID=="1"){ //means root element - Reporting to no one
			    				data.addRows([
					    		        	   [{v:json.rows[ii].ID, f:'<div style="font-size: 10px;"><b>Company</b><br>CO'+'<input type="hidden" name="nodeid_'+json.rows[ii].ID+'" id="nodeid_'+json.rows[ii].ID+'" value="'+json.rows[ii].ID+'"/><input type="hidden" name="nodename_'+json.rows[ii].ID+'" id="nodename_'+json.rows[ii].ID+'" value="'+json.rows[ii].DisplayName+'"/><input type="hidden" name="nodedesignation_'+json.rows[ii].ID+'" id="nodedesignation_'+json.rows[ii].ID+'" value="'+json.rows[ii].Designation+'"/><input type="hidden" name="nodenodehirlevel_'+json.rows[ii].ID+'" id="nodehirlevel_'+json.rows[ii].ID+'" value="'+LevelNName+'"/></div>'}, '', '']
					    		        	  ]);
			    				data.setRowProperty(rowcounter, 'style', 'border:1px solid '+json.rows[ii].BorderNodeColor+'; background:'+json.rows[ii].NodeColor);
		    				}
			    			else{
			    				data.addRows([
					    		        	    [{v:json.rows[ii].ID, f:'<div style="font-size: 10px;"><b>'+json.rows[ii].DisplayName+'</b><br>('+json.rows[ii].ID+')<br/>'+json.rows[ii].Designation+'<br>'+LevelNName+'<input type="hidden" name="nodeid_'+json.rows[ii].ID+'" id="nodeid_'+json.rows[ii].ID+'" value="'+json.rows[ii].ID+'"/><input type="hidden" name="nodename_'+json.rows[ii].ID+'" id="nodename_'+json.rows[ii].ID+'" value="'+json.rows[ii].DisplayName+'"/><input type="hidden" name="nodedesignation_'+json.rows[ii].ID+'" id="nodedesignation_'+json.rows[ii].ID+'" value="'+json.rows[ii].Designation+'"/><input type="hidden" name="nodenodehirlevel_'+json.rows[ii].ID+'" id="nodehirlevel_'+json.rows[ii].ID+'" value="'+LevelNName+'"/></div>'}, json.rows[ii].CurrentReportingTo, '']
					    		        	  ]);			    				
			    				data.setRowProperty(rowcounter, 'style', 'border:1px solid '+json.rows[ii].BorderNodeColor+'; background:'+json.rows[ii].NodeColor);	
			    				
			    				
			    			}
			    			rowcounter++;
		    			}
		    			
		    			
		    		}
		    		else{
		    		
		    		
		    			for( var i = 0; i < json.rows.length; i++ ){
			    			 if(json.rows[i].LevelName!=null){
			    				 LevelNName = json.rows[i].LevelName;
			    			 }
			    			 else{
			    				 LevelNName="";
			    			 }
			    			if(json.rows[i].ID=="1"){ //means root element - Reporting to no one
			    				data.addRows([
					    		        	   [{v:json.rows[i].ID, f:'<div style="font-size: 10px;"><b>Company</b><br>CO'+'<input type="hidden" name="nodeid_'+json.rows[i].ID+'" id="nodeid_'+json.rows[i].ID+'" value="'+json.rows[i].ID+'"/><input type="hidden" name="nodename_'+json.rows[i].ID+'" id="nodename_'+json.rows[i].ID+'" value="'+json.rows[i].DisplayName+'"/><input type="hidden" name="nodedesignation_'+json.rows[i].ID+'" id="nodedesignation_'+json.rows[i].ID+'" value="'+json.rows[i].Designation+'"/><input type="hidden" name="nodenodehirlevel_'+json.rows[i].ID+'" id="nodehirlevel_'+json.rows[i].ID+'" value="'+LevelNName+'"/></div>'}, '', '']
					    		        	  ]);
			    				data.setRowProperty(i, 'style', 'border:1px solid '+json.rows[i].BorderNodeColor+'; background:'+json.rows[i].NodeColor);
		    				}
			    			else{
			    				data.addRows([
					    		        	    [{v:json.rows[i].ID, f:'<div style="font-size: 10px;"><b>'+json.rows[i].DisplayName+'</b><br>('+json.rows[i].ID+')<br/>'+json.rows[i].Designation+'<br>'+LevelNName+'<input type="hidden" name="nodeid_'+json.rows[i].ID+'" id="nodeid_'+json.rows[i].ID+'" value="'+json.rows[i].ID+'"/><input type="hidden" name="nodename_'+json.rows[i].ID+'" id="nodename_'+json.rows[i].ID+'" value="'+json.rows[i].DisplayName+'"/><input type="hidden" name="nodedesignation_'+json.rows[i].ID+'" id="nodedesignation_'+json.rows[i].ID+'" value="'+json.rows[i].Designation+'"/><input type="hidden" name="nodenodehirlevel_'+json.rows[i].ID+'" id="nodehirlevel_'+json.rows[i].ID+'" value="'+LevelNName+'"/></div>'}, json.rows[i].CurrentReportingTo, '']
					    		        	  ]);			    				
			    				data.setRowProperty(i, 'style', 'border:1px solid '+json.rows[i].BorderNodeColor+'; background:'+json.rows[i].NodeColor);	
			    				
			    				
			    			}
			    			
			    		}
		    		
		    		}
		    		
		    		
		    		google.visualization.events.addListener(chart, 'select', selectHandler);
		    		chart.draw(data, {allowHtml:true,allowCollapse:true});
		    		
		    		 $.mobile.loading( 'hide');
		    		//alert();
		    		
		    	}else{
		    		alert("Server could not be reached.");
		    	}
		    },
		    error: function( xhr, status ) {
		    	alert("Server could not be reached.");
		    }
		});
}
function drawChart() {
	//alert();
	  data = new google.visualization.DataTable();
	  data.addColumn('string', 'Name');
	  data.addColumn('string', 'Manager');
	  data.addColumn('string', 'ToolTip');
	  data.addRows([
	        	    [{v:'1', f:'System'}, '', 'The President']	        	    
	        	  ]);
	  
	  chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
	  chart.draw(data, {allowHtml:true,allowCollapse:true});
	  //google.visualization.events.addListener(chart, 'select', selectHandler);

	}

function selectHandler(e){
	//alert();
	//alert("sdasd");
	 var selection = chart.getSelection(); //returns the array of selected items 	 
 	 var item = selection[0]; 	  
 	 var str1 = data.getFormattedValue(item.row, 0); 	
 	 var id = data.getValue(item.row, 0); 	 
 		
 	/* 
    var allIndexes = chart.getChildrenIndexes(item.row); //getting children of given node
  	 
  	 //alert(allIndexes[1]);
  	var str2 = data.getValue(allIndexes[0], 0);
 	 alert(str2);*/
 	 
 	 if((document.getElementById('MoveUnderRadio-1').checked==false  && document.getElementById('MoveUnderRadio-3').checked==false && document.getElementById('MoveUnderRadio-4').checked==false)){
	 		//if(id!="1"){//if not root
	 	if(id=="1"){
	 		$('#MoveUnderRadio-1_label').addClass("ui-disabled");
	 		$('#MoveUnderRadio-2_label').addClass("ui-disabled");
	 		$('#MoveUnderRadio-3_label').addClass("ui-disabled");
	 		$('#MoveUnderRadio-5_label').addClass("ui-disabled");
	 		
	 	}
	 	else{
	 		$('#MoveUnderRadio-1_label').removeClass("ui-disabled");
	 		$('#MoveUnderRadio-2_label').removeClass("ui-disabled");
	 		$('#MoveUnderRadio-3_label').removeClass("ui-disabled");
	 		$('#MoveUnderRadio-5_label').removeClass("ui-disabled");
	 	}
 		 
 		 var LevelNNamme="";
	 			if($("#nodehirlevel_"+id).val()!=""){
	 				LevelNNamme="("+$("#nodehirlevel_"+id).val()+")";
	 			}
	 			else{
	 				LevelNNamme="";
	 			}
	 			var display = "<b>" + $("#nodeid_"+id).val()+" - "+$("#nodename_"+id).val() + "</b><br>" + $("#nodedesignation_"+id).val()+" "+LevelNNamme;
	 			$("#ParentSelectedNode").html(display);
	 	 	 		 			
	 			$("#SelectAction").removeClass("ui-disabled");
	 			$( "#SelectAction" ).selectmenu( "refresh" );
	 			
	 			//setting hidden fields
	 	 	 	$("#DispSapCode").val($("#nodeid_"+id).val());
	 	 	 	$("#DispDisplayname").val($("#nodename_"+id).val());
	 	 	 	$("#DispDesignation").val($("#nodedesignation_"+id).val());
	 	 	 	$("#ActionList").css("display", "block");

	 		//}
 		}
 		else{
 			ChangeTxtMoveUnder();
 		}
 		
 		if((document.getElementById('MoveUnderRadio-1').checked==true || document.getElementById('MoveUnderRadio-2').checked==true || document.getElementById('MoveUnderRadio-3').checked==true || document.getElementById('MoveUnderRadio-4').checked==true) && ($("#DispSapCode").val()!="") && ($("DispSapCodeMoveUnder").val()!="")){ //if drop down is selected && Some Parent is selected && Some Child is Selected
 			$("#ButtonApplyID").removeClass("ui-disabled"); 			
 		}
 		else{
 			
 		}
}

function ShowHideDIVMoveUnder(){
	 if($('#SelectAction').val()!="0"){		
		$('#ActionMoveUnderID').css("display", "block");
	}
	else{		
		$('#ActionMoveUnderID').css("display", "none");		
	}
}

function ResetFields(){
	$("#DesignationMoveUnder").html("Not Selected");	
	$("#ParentSelectedNode").html("Not Selected");	
	$('#ActionMoveUnderID').css("display", "none");
	$('#ActionList').css("display", "none");
	$('#ReportingLevelControlAddUnder').css("display","none");
	
	$("#MoveUnderRadio-1").prop('checked', false);
	$("#MoveUnderRadio-1").checkboxradio("refresh");
	$("#MoveUnderRadio-2").prop('checked', false);
	$("#MoveUnderRadio-2").checkboxradio("refresh");
	$("#MoveUnderRadio-3").prop('checked', false);
	$("#MoveUnderRadio-3").checkboxradio("refresh");
	$("#MoveUnderRadio-4").prop('checked', false);
	$("#MoveUnderRadio-4").checkboxradio("refresh");
	$("#MoveUnderRadio-5").prop('checked', false);
	$("#MoveUnderRadio-5").checkboxradio("refresh");
	$("#ButtonApplyID").addClass("ui-disabled");
	$("#AddUnderTableID").css("display", "none");
	$("#ReportingLevelControl").css("display", "none");
	
	//reseting level radio list
	$("input:radio[name='MoveReportingLevelRadio']").each(function() { 
		$("#"+this.id).prop('checked', false);
		$("#"+this.id).checkboxradio("refresh");		
	});
	$("input:radio[name='MoveReportingLevelRadioAddUnder']").each(function() { 
		$("#"+this.id).prop('checked', false);
		$("#"+this.id).checkboxradio("refresh");		
	});
	
	
	
	$("#DispSapCode").val("");
	$("#DispDisplayname").val("");
	$("#DispDesignation").val("");
	$("#DispSapCodeMoveUnder").val("");
	$("#DispDisplaynameMoveUnder").val("");
	$("#DispDesignationMoveUnder").val("");
	$("#AddUnderTxtBx").val("");
}
function SelectAction(){
	var display = "Not Selected";
	$("#DesignationMoveUnder").html(display);
	$('#ActionMoveUnderID').css("display", "block");
}

function ChangeTxtMoveUnder(){	
	//alert();
		
	
	
	
	var selection = chart.getSelection(); //returns the array of selected items 	 
	 	 var item = selection[0]; 	  
	 	 var str1 = data.getFormattedValue(item.row, 0); 	
	 	 var id = data.getValue(item.row, 0);
	 	if(document.getElementById('MoveUnderRadio-2').checked==false){ //for other than remove controll show the Move under div
	 		$('#ActionMoveUnderID').css("display", "block");
	 	}
	 	
	 	
	 	if(id=="1"){
	 		$('#MoveUnderRadio-1_label').addClass("ui-disabled");
	 		$('#MoveUnderRadio-2_label').addClass("ui-disabled");
	 		$('#MoveUnderRadio-3_label').addClass("ui-disabled");
	 		$('#MoveUnderRadio-5_label').addClass("ui-disabled");
	 		
	 	}
	 	else{
	 		$('#MoveUnderRadio-1_label').removeClass("ui-disabled");
	 		$('#MoveUnderRadio-2_label').removeClass("ui-disabled");
	 		$('#MoveUnderRadio-3_label').removeClass("ui-disabled");
	 		$('#MoveUnderRadio-5_label').removeClass("ui-disabled");
	 	}
	 	
	 	
	 	
	 	var LevelNNamme="";
			if($("#nodehirlevel_"+id).val()!=""){
				LevelNNamme="("+$("#nodehirlevel_"+id).val()+")";
			}
			else{
				LevelNNamme="";
			}
	 	
		//if(id!="1"){//if not root
				 var display = "<b>" + $("#nodeid_"+id).val()+" - "+$("#nodename_"+id).val() + "</b><br>" + $("#nodedesignation_"+id).val()+" "+LevelNNamme;
				$("#DesignationMoveUnder").html(display);
		 	 	
		 	 	//setting hidden fields
		 	 	$("#DispSapCodeMoveUnder").val($("#nodeid_"+id).val());
		 	 	$("#DispDisplaynameMoveUnder").val($("#nodename_"+id).val());
		 	 	$("#DispDesignationMoveUnder").val($("#nodedesignation_"+id).val());
		 	// }  
	
}

function ApplyChanges(){
	var ChildSapCode=$("#DispSapCode").val();
	var ParentSapCode=$("#DispSapCodeMoveUnder").val();
	var ActionValue = $("input:radio[name='MoveUnderRadio']:checked").val();
	var ReportingLevelID = $("input:radio[name='MoveReportingLevelRadio']:checked").val();
	var ReportingLevelIDAddUnder = $("input:radio[name='MoveReportingLevelRadioAddUnder']:checked").val();
	//alert(ReportingLevel);
	var AddUnerValue=$("#AddUnderTxtBx").val();
	
	
		$.ajax({		
			url: "employee/EmployeeHierarchyExecute",
			data: {
				ParentSapCode1 : ParentSapCode,
				ChildSapCode1 : ChildSapCode,
				ActionValue1 : ActionValue,
				AddUnerValue1 : AddUnerValue,
				ReportingLevelID1:ReportingLevelID,
				ReportingLevelIDAddUnder1:ReportingLevelIDAddUnder
			},
			type:"POST",
			dataType:"json",
			success:function(json){
				if(json.success == "true"){
					getEmployeeTreeJSON(DefaultLevel);
					ResetFields();
					 
				}else{
					alert(json.error);
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
			}			
		});		
	
	
}

function AddUnder(){
	if(document.getElementById('MoveUnderRadio-4').checked==true){
		$("#AddUnderTableID").css("display", "block");
		$("#DesignationMoveUnder").css("display", "none");
		$("#SelectNodeToMoveID").html("Enter Employee ID");
		$("#ButtonApplyID").addClass("ui-disabled");
		$('#ReportingLevelControl').css("display", "none");
		$('#ReportingLevelControlAddUnder').css("display","block");
	}
	else if(document.getElementById('MoveUnderRadio-2').checked==true){ //for remove
		$('#ActionMoveUnderID').css("display", "none");
	    $("#ButtonApplyID").removeClass("ui-disabled");
	    $('#ReportingLevelControl').css("display", "none");
	    $('#ReportingLevelControlAddUnder').css("display","none");
	}
	else if(document.getElementById('MoveUnderRadio-5').checked==true){ //for Reporting Level
		$('#ActionMoveUnderID').css("display", "none");
		$('#ReportingLevelControl').css("display", "block");
		$('#ReportingLevelControlAddUnder').css("display","none");
	}
	else{
		$("#AddUnderTableID").css("display", "none");
		$("#DesignationMoveUnder").html("Not Selected");
		$("#DesignationMoveUnder").css("display", "block");
		$("#SelectNodeToMoveID").html("Select node to move");
		$("#ButtonApplyID").addClass("ui-disabled");
		$('#ReportingLevelControl').css("display", "none");
		$('#ReportingLevelControlAddUnder').css("display","none");
	}
}

function EnableSaveBtn(){	
	if($("#AddUnderTxtBx").val()!="" && AddReportingLevel()){
		$("#ButtonApplyID").removeClass("ui-disabled");
	}
	else{
		$("#ButtonApplyID").addClass("ui-disabled");
	}
	
}

function SelectReportingLevel(){
	
		$("#ButtonApplyID").removeClass("ui-disabled");
	
}

function SeeCompanyLevel(levelid){
	getEmployeeTreeJSON(levelid);
	//alert();
}

function UserRightsCallBack(SAPCode, EmployeeName){  	
	
	$("#AddUnderTxtBx").val(SAPCode).change();
	//LoadEditUserForm(SAPCode);
	
}

function AddReportingLevel(){
	var AddUnerValuesFlag=false;
	//alert();
	$("input:radio[name='MoveReportingLevelRadioAddUnder']").each(function() { 
		if($("#"+this.id).is(':checked')){
			AddUnerValuesFlag=true;
		}
	});
	return AddUnerValuesFlag;
}