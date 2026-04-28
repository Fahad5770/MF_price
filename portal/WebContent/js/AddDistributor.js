/**
 * 03-02-2020
 */
$(function(){		
		$("#AddDistributor").click(function(){
			
			/* Get Data */
			var id = $("#distributor_id").val();
			var name = $("#distributor_name").val();
			var city = $("#city").val();
			var region = $("#region").val();
			var product = $("#product").val();
			
			/* Flag */
			var flag = false;
			
			/*Validation Rules*/
			var id_regex = /^[0-9]+$/;
			
			
			/* Validation */
			if(city < 1)
				{
				flag = true;
				alert("Select City");
				}
			if(region < 1)
			{
			flag = true;
			alert("Select Region");
			}
			if(product < 1)
			{
			flag = true;
			alert("Select Productb Group");
			}
			if(address == "")
			{
			flag = true;
			alert("Enter Address");
			}
			if(id_regex.test(id) == "" || id == "")
				{
				flag = true;
				alert("ID Should be in numeric");
				}
			
			 
			if(flag == false){
				$.ajax({
				    url: "distributor/AddDistributorExecute",
				    data: $("#add_distributor" ).serialize(),
				    type: "POST",
				    dataType : "json",
				    success: function( json ) {
				    	if (json.success == "true"){
				    	 	alert("Distributor Add Successfully");
				    	 	window.location.href="AddDistributor.jsp";
				    		
				    	}else{
				    		alert("Distributor Already Exists");
				    	}
				    	
				    },
				    error : function(){
				    	alert("Server could not be reached");
				    },
				 
			 });
			}
		});
		
	});  