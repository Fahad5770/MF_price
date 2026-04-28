

var RowCount = 0;
$( document ).delegate("#Synchronization", "pageinit", function() {
});

function ShowDate(SyncActionValue){
	$("#SyncActionValue").val(SyncActionValue);
	if(SyncActionValue=="1"){//highlight distributors
		
		$("#title").html("Distributors");
		$("#SyncBtnTD").removeClass("ui-disabled");
		$("#LogOutputMsg").html("");
		
	}else if(SyncActionValue=="2"){//highlight distributors
		
		$("#title").html("Employees");
		$("#LogOutputMsg").html("");
		$("#SyncBtnTD").removeClass("ui-disabled");
		
	}else if(SyncActionValue=="3"){//highlight distributors
		
		$("#title").html("Outlets");
		$("#LogOutputMsg").html("");
		$("#SyncBtnTD").removeClass("ui-disabled");
		
	}else if(SyncActionValue=="4"){//highlight distributors
		
		$("#title").html("Sales");
		$("#StartDate").val("");
		$("#EndDate").val("");
		$("#StartDate").attr("placeholder", "Month");
		$("#EndDate").attr("placeholder", "Year");
		$("#LogOutputMsg").html("");
		$("#SyncBtnTD").removeClass("ui-disabled");
		
	}else if(SyncActionValue=="5"){//highlight distributors
		
		$("#title").html("Sales");
		$("#StartDate").val("");
		$("#EndDate").val("");
		$("#StartDate").attr("placeholder", "From (DD/MM/YYYY)");
		$("#EndDate").attr("placeholder", "To (DD/MM/YYYY)");
		
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!

		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = dd+'/'+mm+'/'+yyyy;
		$("#EndDate").val(today);
		
		
		$("#LogOutputMsg").html("");
		
	}else if(SyncActionValue=="6"){//highlight distributors
		
		$("#title").html("Order Booker Statistics");
		$("#StartDate").val("");
		$("#EndDate").val("");
		$("#StartDate").attr("placeholder", "From (DD/MM/YYYY)");
		$("#EndDate").attr("placeholder", "To (DD/MM/YYYY)");
		$("#LogOutputMsg").html("");
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!

		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = dd+'/'+mm+'/'+yyyy;
		$("#EndDate").val(today);
	}else if(SyncActionValue=="7") {
		
		$("#title").html("Reconnect SAP");
		$("#LogOutputMsg").html("");
		$("#SyncBtnTD").removeClass("ui-disabled");
	}else if(SyncActionValue=="8"){//Promotions Cache
		
		$("#title").html("Promotions Cache");		
		$("#LogOutputMsg").html("");
		$("#SyncBtnTD").removeClass("ui-disabled");
		
	}else if(SyncActionValue=="9"){//Promotions Cache
		
		$("#title").html("Hand to Hand Discount");		
		$("#LogOutputMsg").html("");
		$("#SyncBtnTD").removeClass("ui-disabled");
		
	}else if(SyncActionValue=="10"){//
		
		$("#title").html("Orders and Invoices");
		$("#StartDate").val("");
		$("#EndDate").val("");
		$("#StartDate").attr("placeholder", "From (DD/MM/YYYY)");
		$("#EndDate").attr("placeholder", "To (DD/MM/YYYY)");
		
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!

		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = dd+'/'+mm+'/'+yyyy;
		$("#EndDate").val(today);
		
		
		$("#LogOutputMsg").html("");
		
	}
	
	
	
	if(SyncActionValue=="1" || SyncActionValue=="2" || SyncActionValue=="3" || SyncActionValue=="7" || SyncActionValue=="8" || SyncActionValue=="9"){
		
		$("#StartDateTD").addClass("ui-disabled");
		$("#EndDateTD").addClass("ui-disabled");
		
		
	}else{
		
		$("#StartDateTD").removeClass("ui-disabled");
		$("#EndDateTD").removeClass("ui-disabled");
		
	}
	
	
}

function Synchronize(){
	var DateFlag=false;
	var ActionValue = $("#SyncActionValue").val();
	if(ActionValue=="1"){//Distributors
		$("#SyncInProgressStatus").html("In Progress...");	
		$("#SyncBtnTD").addClass("ui-disabled");	
		$.ajax({			
			//url: "util/BiBatch",
			url: "sap/SyncExecuteShell",
			data: {
				CommandID: 1						
    		},
			type:"POST",
			dataType:"json",
			success:function(json){
				if(json.success == "true"){
					$("#LogOutputMsg").html(json.logmsg);
					$("#SyncInProgressStatus").html("Done.");
					$("#SyncBtnTD").removeClass("ui-disabled");
				}else{
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
				$("#SyncInProgressStatus").html("The request could not be completed.");
			}
		});
		
	}else if(ActionValue=="2"){//Employees
		$("#SyncInProgressStatus").html("In Progress...");	
		$("#SyncBtnTD").addClass("ui-disabled");	
		$.ajax({			
			//url: "util/BiBatch",
			url: "sap/SyncExecuteShell",
			data: {
				CommandID: 2						
    		},
			type:"POST",
			dataType:"json",
			success:function(json){
				if(json.success == "true"){
					$("#LogOutputMsg").html(json.logmsg);
					$("#SyncInProgressStatus").html("Done.");
					$("#SyncBtnTD").removeClass("ui-disabled");
				}else{
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
				$("#SyncInProgressStatus").html("The request could not be completed.");
			}
		});
	}else if(ActionValue=="3"){//Outlets
		$("#SyncInProgressStatus").html("In Progress...");	
		$("#SyncBtnTD").addClass("ui-disabled");	
		$.ajax({			
			//url: "util/BiBatch",
			url: "sap/SyncExecuteShell",
			data: {
				CommandID: 3						
    		},
			type:"POST",
			dataType:"json",
			success:function(json){
				if(json.success == "true"){
					$("#LogOutputMsg").html(json.logmsg);
					$("#SyncInProgressStatus").html("Done.");
					$("#SyncBtnTD").removeClass("ui-disabled");
				}else{
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
				$("#SyncInProgressStatus").html("The request could not be completed.");
			}
		});
		
	}else if(ActionValue=="4"){//Sales Sap
		var DateFlag=false;
		$("#SyncBtnTD").addClass("ui-disabled");
		if($("#StartDate").val()!="" && $("#EndDate").val()!=""){
			DateFlag=true;
		}else{
			alert("Please Enter Valid Month and year");
			document.getElementById('StartDate').focus();
		}
		//$("#SyncBtnTD").addClass("ui-disabled");	
		
		if(DateFlag){
			$("#SyncInProgressStatus").html("In Progress...");	
			$.ajax({			
				//url: "util/BiBatch",
				url: "sap/SyncExecuteShell",
				data: {
					CommandID: 4,
					Month: $("#StartDate").val(),
					Year: $("#EndDate").val()    
	    		},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.success == "true"){
						$("#LogOutputMsg").html(json.logmsg);
						$("#SyncInProgressStatus").html("Done.");
						$("#SyncBtnTD").removeClass("ui-disabled");
					}else{
						$("#SyncInProgressStatus").html("The request could not be completed.");
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			});	
		}
		
		
		
	}else if(ActionValue=="5"){//Sales Sampling
		
		$("#SyncBtnTD").addClass("ui-disabled");
		$("#SyncInProgressStatus").html("In Progress...");
		DateFlag=false;
		//var StartDate1 = $("#StartDate").val();
		//var EndDate1 = $("#EndDate").val();
		if($("#StartDate").val()!="" && $("#EndDate").val()!=""){
			DateFlag=true;
		}else{
			alert("Please Enter Valid Date Range");
			document.getElementById('StartDate').focus();
		}
		if(DateFlag){
			$("#SyncBtnTD").addClass("ui-disabled");	
			$.ajax({			
				//url: "util/BiBatch",
				url: "sap/SyncSalesInternal",
				data: {
					StartDate1: $("#StartDate").val(),
					EndDate1: $("#EndDate").val()    			
	    		},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.success == "true"){
						$("#SyncInProgressStatus").html("Done.");
						$("#SyncBtnTD").removeClass("ui-disabled");
					}else{
						$("#SyncInProgressStatus").html("The request could not be completed.");
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			});
		}
	}else if(ActionValue=="6"){//Order Booker Ranking
		DateFlag=false;
		$("#SyncBtnTD").addClass("ui-disabled");
		$("#SyncInProgressStatus").html("In Progress...");
		//var StartDate1 = $("#StartDate").val();
		//var EndDate1 = $("#EndDate").val();
		if($("#StartDate").val()!="" && $("#EndDate").val()!=""){
			DateFlag=true;
		}else{
			alert("Please Enter Valid Date Range");
			document.getElementById('StartDate').focus();
		}
		if(DateFlag){
		
			$.ajax({
				url: "bi/BiOrderBookerStatistics",
				//url: "util/BiRuntime",
				data: {
					StartDate1: $("#StartDate").val(),
					EndDate1: $("#EndDate").val()    			
	    		},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.success == "true"){
						$("#SyncInProgressStatus").html("Done.");
						$("#SyncBtnTD").removeClass("ui-disabled");
					}else{
						alert("Server could not be reached.");
						$("#SyncInProgressStatus").html("The request could not be completed.");
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			});
		}
	}else if(ActionValue=="7"){//Reconnect SAP
		$("#SyncInProgressStatus").html("In Progress...");	
		$("#SyncBtnTD").addClass("ui-disabled");	
		$.ajax({			
			//url: "util/BiBatch",
			url: "sap/Connect",			
			type:"POST",
			dataType:"html",
			success:function(json){				
					$("#LogOutputMsg").html(json);
					$("#SyncInProgressStatus").html("Done.");
					$("#SyncBtnTD").removeClass("ui-disabled");
				
			},
			error:function(xhr, status){
				alert("Server could not be reached."+xhr.responseText);
				$("#SyncInProgressStatus").html("The request could not be completed.");
			}
		});
		
	}else if(ActionValue=="8"){//Promotions Cache		
				
		$("#SyncInProgressStatus").html("In Progress...");	
		$("#SyncBtnTD").addClass("ui-disabled");	
		$.ajax({
				url: "bi/PromotionsCache",
				//url: "util/BiRuntime",
				data: {
					StartDate1: $("#StartDate").val(),
					EndDate1: $("#EndDate").val()    			
	    		},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.success == "true"){
						$("#SyncInProgressStatus").html("Done.");
						$("#SyncBtnTD").removeClass("ui-disabled");
					}else{
						alert("Server could not be reached.");
						$("#SyncInProgressStatus").html("The request could not be completed.");
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			});
		
	}else if(ActionValue=="9"){//Promotions Cache		
		//alert();		
		$("#SyncInProgressStatus").html("In Progress...");	
		$("#SyncBtnTD").addClass("ui-disabled");	
		$.ajax({
				url: "bi/HandToHandDiscount",
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.success == "true"){
						$("#SyncInProgressStatus").html("Done.");
						$("#SyncBtnTD").removeClass("ui-disabled");
					}else{
						alert("Server could not be reached.15");
						$("#SyncInProgressStatus").html("The request could not be completed.");
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			});
		
	}else if(ActionValue=="10"){
		var DateFlag=false;
		$("#SyncBtnTD").addClass("ui-disabled");
		if($("#StartDate").val()!="" && $("#EndDate").val()!=""){
			DateFlag=true;
		}else{
			alert("Please Enter Valid Month and year");
			document.getElementById('StartDate').focus();
		}
		//$("#SyncBtnTD").addClass("ui-disabled");	
		
		if(DateFlag){
			$("#SyncInProgressStatus").html("In Progress...");	
			$.ajax({			
				//url: "util/BiBatch",
				url: "sap/SyncSalesOrdersAndInvoices",
				data: {
					
					StartDate: $("#StartDate").val(),
					EndDate: $("#EndDate").val()    
	    		},
				type:"POST",
				dataType:"json",
				success:function(json){
					if(json.success == "true"){
						$("#LogOutputMsg").html(json.logmsg);
						$("#SyncInProgressStatus").html("Done.");
						$("#SyncBtnTD").removeClass("ui-disabled");
					}else{
						$("#SyncInProgressStatus").html("The request could not be completed.");
					}
				},
				error:function(xhr, status){
					alert("Server could not be reached.");
					$("#SyncInProgressStatus").html("The request could not be completed.");
				}
			});	
		}
		
		
		
	}
	
	//alert($("#SyncActionValue").val());
}
function EnableStartBtn(){
	$("#SyncBtnTD").removeClass("ui-disabled");	
}
