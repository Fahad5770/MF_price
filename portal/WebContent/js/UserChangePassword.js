
$( document ).delegate("#UserChangePasswordPage", "pageinit", function() {
	
});


function checkPasswordCharacters(password){
	
	var hasInteger = false;
	var hasAlphabets = false;
	
	var letters=/^[a-zA-Z]+$/;
	//var numbers=/^[0-9]+$/;
	
	for(var i = 0; i < password.length; i++){
		
		if( parseInt(password.charAt(i)) > 0 ){
			hasInteger = true;
		}
		
		if( password.charAt(i).search(letters) == 0 ){
			hasAlphabets = true;
		}
		
		if(hasInteger && hasAlphabets){
			return true;
		}
		
	}// end for
	
	if(!hasInteger || !hasAlphabets){
		return false;
	}
	
}

function FormSubmit(){
	/*
	if( $('#OldPassword').val() == "" ){
		alert("Please enter Old Password");
		$('#OldPassword').focus();
		return false;
	}
	
	if( $('#NewPassword').val() == "" ){
		alert("Please enter New Password");
		$('#NewPassword').focus();
		return false;
	}
	
	if( $('#ConfirmPassword').val() == "" ){
		alert("Please enter Confirm Password");
		$('#ConfirmPassword').focus();
		return false;
	}
	
	if($('#ConfirmPassword').val() != $('#NewPassword').val()){
		alert("Confirm Password didnt match the New Password");
		$('#ConfirmPassword').focus();
		return false;
	}
	*/
	
	var currentpass = $("#OldPassword").val();
	var newpass = $("#NewPassword").val();
	var confirmpass = $("#ConfirmPassword").val();
	
	if (currentpass.length < 2){
		alert("Please enter current password.");
		document.getElementById("CurrentPassword").focus();
		return false;
	}
	
	if (newpass.length < 8 || checkPasswordCharacters(newpass) == false ){
		alert("The password must be at least 8 characters long and contain:\n1) Atlest one number.\n2) Atleast one alphabet.");
		
		document.getElementById("NewPassword").focus();
		return false;
	}
	
	if (newpass != confirmpass){
		alert("Password doesn't match.");
		document.getElementById("ConfirmPassword").focus();
		return false;
	}
	
	
	$('#UserChangePasswordPageForm').submit();
}


function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

