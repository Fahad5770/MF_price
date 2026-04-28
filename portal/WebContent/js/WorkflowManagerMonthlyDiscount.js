
$( document ).delegate("#MonthlyDiscount", "pageinit", function() {
	initPage("#MonthlyDiscount");
});
$( document ).delegate("#MonthlyDiscountApproval", "pageinit", function() {
	initPage("#MonthlyDiscountApproval");
});

function initPage(pageid){
	
	

	getEditInfoJson(RequestID);

	setWorkflowEvents(pageid);
	showAttachments(pageid);
	showChatContent(pageid);
/*	
	$.ajax({
	    url: "common/GetLiquidPackageListJson",
	    data: {
	        NoParam: 1
	    },
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	
	    	INVENTORY_PACKAGE_JSON = json;
	    	
	    	$.ajax({
	    	    url: "common/GetBrandListJson",
	    	    data: {
	    	        NoParam: 1
	    	    },
	    	    type: "POST",
	    	    dataType : "json",
	    	    success: function( json2 ) {
	    	    	INVENTORY_BRAND_JSON = json2;
	    	    	
	    	    	setSamplingFormEvents(pageid);
	    	    	setWorkflowEvents(pageid);
	    	    	showAttachments(pageid);
	    	    	showChatContent(pageid);
	    	    	
	    	    },
	    	    error: function( xhr, status ) {
	    	        alert( "Sorry, the server did not respond." );
	    	    }
	    	});
	    	
	    	
	    	
	    },
	    error: function( xhr, status ) {
	        alert( "Sorry, the server did not respond." );
	    }
	});
	*/
}


function setWorkflowEvents(pageid){

    var manualuploader = $(pageid + ' #manual-fine-uploader').fineUploader({
        autoUpload: false,
        text: {
        	uploadButton: '<i class="icon-plus icon-white"></i> Select Files'
        },
        request: {
        	endpoint: 'util/UploadFile',
        	paramsInBody: true,
            params: {
            	RequestID: $(pageid + " #RequestID").val(),
            	description: function(){
            		return $(pageid + " #WorkflowAttachmentDescription").val();
            	}
            }
        }
    }).on('error', function(event, id, name, reason) {

    })
    .on('complete', function(event, id, name, responseJSON){
    	showAttachments(pageid);
    	$.mobile.changePage(pageid);
    	
    });
   
    $(pageid + ' #triggerUpload').click(function() {
    	manualuploader.fineUploader('uploadStoredFiles');
    });
      
	$(pageid + " .WorkflowAttachButton").on( "click", function( event, ui ) {
		$( pageid +" #popup_workflow_attach" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});
	
	$(pageid + " .WorkflowConversationButton").on( "click", function( event, ui ) {
		$( pageid +" #popup_workflow_conversation" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});
	
	$(pageid + " .WorkflowDeclineButton").on( "click", function( event, ui ) {
		$( pageid +" #popup_workflow_decline" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});

	$(pageid + " .WorkflowDeclineConfirmButton").on( "click", function( event, ui ) {
		
		$.ajax({
		    url: "workflow/DeclineRequestExecute",
		    data: $(pageid + " .WorkflowForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	if (json.success == "true"){
		    		$.mobile.changePage("WorkflowManager.jsp?requestID="+json.RequestID+"&processID=1&SecondPage=1");
		    	}else{
		    		$( pageid + " #AJAXErrorMessage" ).html(json.error);
		    		setTimeout(function() {
			    		$( pageid + " #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
		    		}, 2000);
		    	}
		    },
		    error: function( xhr, status ) {
		    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
	    		setTimeout(function() {
		    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	    		}, 2000);
		    }
		});
		
	});
	
	
	$(pageid + " .WorkflowSubmitButton").on( "click", function( event, ui ) {
		
		//$(pageid+ ' .WorkflowForm').submit();
		
		var SamplingActionLength = $('select[name=SamplingAction]').length;
		var SelectionFlag = 0;
		for(var i = 0; i < SamplingActionLength; i++){
			if( $('select[name=SamplingAction]')[i].value == "1" ){
				SelectionFlag++;
			}
		}
		
		if(SelectionFlag == 0){
			alert("Please select atleast one Outlet");
			return false;
		}
		
		$( pageid +" #popup_workflow_forward" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	});
	
	$(pageid +" .WorkflowSubmitConfirm").on("click", function( event, ui ) {
		event.preventDefault();
		$(pageid + " #WorkflowStepRemarks").val($( pageid + " #WorkflowStepRemarksPopup").val());
		
		$.mobile.changePage(pageid);
		
		$.ajax({
		    url: "sampling/MonthlyDiscountRequestExecute",
		    data: $("#SamplingDashboardApprovalForm" ).serialize(),
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	//alert('success');
		    	window.location="WorkflowManagerMonthlyDiscount.jsp?requestID="+json.RequestID+"&processID=1&SecondPage=1";
		    	//$.mobile.changePage("WorkflowManagerMonthlyDiscount.jsp?requestID="+json.RequestID+"&processID=1&SecondPage=1");
		    },
		    error: function( xhr, status ) {
		    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
	    		setTimeout(function() {
		    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
	    		}, 2000);
		    }
		});
	});		
}


function showAttachments(pageid){
	
	if (parseFloat($(pageid + " #RequestID").val()) > 0){
		$.get('WorkflowManagerAttachments.jsp?RequestID='+$(pageid + " #RequestID").val(), function(data) {
			  $(pageid + " #WorkflowAttachmentsBar").html(data);
			  $(pageid + " #WorkflowAttachmentsBar").trigger('create');
			  
		});    	
	}
}


function showChatContent(pageid){
	 
	if (parseFloat($(pageid + " #RequestID").val()) > 0){
	
		
		$.get('WorkflowManagerChat.jsp?RequestID='+$(pageid + " #RequestID").val()+'&PageID='+pageid, function(data) {
			  $(pageid + " #WorkflowChatContent").html(data);
			  $(pageid + " #WorkflowChatContent").trigger('create');
			  
			  var ChatPreview = $(pageid + " #WorkflowChatLastMessage").val();
			  
			  if (ChatPreview.length > 5){
				  $(pageid + " #WorkflowChatPreview").text(ChatPreview);
				  $(pageid + " #WorkflowChatPreviewContainer").css("display","block");
			  }
			  
			  
			  
				$(pageid + " #WorkflowChatSendNewButton").on( "click", function( event, ui ) {
					$.ajax({
					    url: "workflow/WorkflowChatExecute",
					    data: $(pageid + " #WorkflowChatSendNewForm" ).serialize(),
					    type: "POST",
					    dataType : "json",
					    success: function( json ) {
					    	if (json.success == "true"){
					    		showChatContent(pageid);
					    	}else{
					    		$( pageid + " #AJAXErrorMessage" ).html(json.error);
					    		setTimeout(function() {
						    		$( pageid + " #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
					    		}, 2000);
					    	}
					    },
					    error: function( xhr, status ) {
					    	$( pageid + " #AJAXErrorMessage" ).html("Unknown Error");
				    		setTimeout(function() {
					    		$( pageid +" #AJAXErrorPopup" ).popup( "open", { transition: "slidedown",positionTo: "window" } );
				    		}, 2000);
					    }
					});
				});
		});    	
	}
	
	
	
}


