function adduser(){
	var userid= $('#userid').val();
	var newpwd= $('#newpwd').val();
	var confirmpwd= $('#confirmpwd').val();
	var firstname= $('#firstname').val();
	var lastname= $('#lastname').val();
	var displayname= $('#displayname').val();
	var designation= $('#designation').val();
	var dept= $('#dept').val();
	var email= $('#email').val();
	var distributor= $('#distributor').val();
	var pwdlen=newpwd.length;
	if(userid!="" &&   newpwd!="" &&   confirmpwd!="" &&   firstname!="" &&   lastname!="" && displayname!=""  && designation!=""  && dept!=""   && email!=""   && distributor!="" ){
		if(newpwd!=confirmpwd){
			alert("New-password and confirm-password does not match");
			return false;
		}
		else if(pwdlen<5)
			{
			alert("New-password length should be more than 5");
			return false;
			}
		
		else{
			if(email.match(/^\w+([-+.'][^\s]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/)){
				$.ajax({
				    url: "employee/DistributorPortalUserAddExecute",
				    data: {
				    	UserID:userid ,
				    	NewPassword:newpwd ,
				    	ConfirmPass:confirmpwd ,
				    	FisrtName:firstname ,
				    	LastName:lastname ,
				    	DisplayName:displayname ,
				    	Designation:designation ,
				    	Dept:dept ,
				    	Email:email ,
				    	Distributor:distributor
				    	//UserID:UserID
				    },
				    type: "POST",
				    dataType : "json",
				    success: function( json ) {
				    	if (json.isExist1 == "true"){
				    		alert(json.Msg);
				    		
				    		window.location="AddDistributorPortalUser.jsp";
				    	}else if(json.isExist1 == "false"){
				    		alert(json.Msg);
				    	}
				    },
				    error: function( xhr, status ) {
				    	alert("Server could not be reached.");
				    }
				});
			}
			else{
				alert("Email Error");			
			}
		}
	}
	else {
		alert("All fields must be filled properly");			
	return false;
	}
	
}