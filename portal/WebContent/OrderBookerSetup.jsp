<%@page import="com.pbc.util.Utilities"%>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 80;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

%>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>

<script>

$( document ).on( "pageshow", "#OrderBookerSetup", function() {
		
	setTimeout(function(){

		$( "#LookupEmployeeSearch" ).on( "popupbeforeposition", function( event, ui ) {
			lookupEmployeeInit();
		} );
		$('#LookupEmployeeSearch').popup("open");
		
	}, 500);
	
	
});

function setEmployeeLookupAtOrderBookerSetup(SAPCode, Name){
	window.location='EmployeeProductSpecification.jsp?EmployeeCode='+SAPCode;
}

</script>

<div data-role="page" id="OrderBookerSetup" data-url="OrderBookerSetup" data-theme="d">
&nbsp;

	<jsp:include page="Header2.jsp" >
    	<jsp:param value="Order Booker Setup" name="title"/>
    </jsp:include>

    
    <div data-role="content" data-theme="d">
    &nbsp;
    </div>
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    &nbsp;
    </div>



	<jsp:include page="LookupEmployeeSearchPopup.jsp" >
	 	<jsp:param value="setEmployeeLookupAtOrderBookerSetup" name="CallBack" />
	 </jsp:include><!-- Include Employee Search -->

</div>