$(function() {
	 
	$('#Distributor_general').change(function(){
	var DistributorID=$('#Distributor_general').val();
		
		$.ajax({
		    url: "mobile/Report242SummaryDistributorInfoJson",
		    data: {
		    	DistributorID: DistributorID
		    },
		    type: "POST",
		    dataType : "json",
		    success: function( json ) {
		    	
		    	if (json.success == "true"){
		    	 	$('#DistributorName_general').val(json.DistributorName);
		    	
		    		
		    	}else{
		    		alert("Distributor ID is not valid");
		    	}
		    	
		    },
		 
	 });
	
 });
	
	
})

function getPJPName(){
	
		var PJPID=$('#PJP_ID').val();
			
			$.ajax({
			    url: "common/GetBeatPlanInfoJson",
			    data: {
			    	PJPID: PJPID
			    },
			    type: "POST",
			    dataType : "json",
			    success: function( json ) {
			    	
			    	if (json.success == "true"){
			    	 	$('#PJP_name').val(json.PJPName);
			    	
			    		
			    	}else{
			    		alert("PJP ID is not valid");
			    	}
			    	
			    },
			});
		
}

function DoRadBtnDisable(){
		var name = 'ShopClosedStatus';
		
		if($('#checkbox-h-isStatus').prop("checked") == true){
			//alert("hey1");
			$('#general_tab').find(':radio[name=' + name + ']').removeAttr("disabled");
			//$("#checkbox-h-6a").removeClass("ui-disabled");
		}
		else if($('#checkbox-h-isStatus').prop("checked") == false) {
			//alert("hey2");
			$('#general_tab').find(':radio[name=' + name + ']').attr("disabled", true);
			//$("#checkbox-h-6a").removeClass("ui-disabled");	
		}
	}




function FormSubmit(){
	
	
	var form=$('#general_tab');
	$.mobile.showPageLoadingMsg();
	$.ajax({
		url:'mobile/R242Execute',
		data:  form.serialize(), 
		type: 'post',
		dataType :'json',
		  success: function( json ) {
			  $.mobile.hidePageLoadingMsg();
			  if(json.success=="true"){
					
					alert("Data has been Saved Successfully");
					//window.location = 'ReportCenter.jsp?ReportID=162';
					$( "#CensusNameTable" ).load(window.location.href + " #CensusNameTable" );
					
				}else{
					alert("Data could not be saved.");
				}
				
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
				$.mobile.loading( 'hide');
			}
		
	});
}


function isdelete(CensusID,btnId){
	if (confirm("Are Your sure you want to delete!") == true) {
		$.mobile.showPageLoadingMsg();
		$.ajax({
			url:'mobile/R242Execute',
			data: {
				CenID: CensusID,
				btnId:btnId
			},
			type:"POST",
			dataType:"json",
			success:function(json){
				$.mobile.hidePageLoadingMsg();
				if(json.successfull == "true"){
					alert("Entry is Deleted Successfully!");
					//window.location = 'ReportCenter.jsp?ReportID=162';
					$( "#CensusNameTable" ).load(window.location.href + " #CensusNameTable" );
				}else{
					alert(json.error);
				}
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
			}
			
		});
	}
	
	
	
		
}





//coolertype
	function RemoveCoolerType(idddd){
		
					
		$('#'+idddd).remove();
	}	
				
				
	function addNewCoolerType(idd){
					
		var counter = $("#CoolerTypeRowCounterHidden").val();
		$('#summary').listview('refresh');
		var RowID = "coolType_"+counter;
		var CheckVar=0;
		
		
		var companyCTVar=$("#CoolerTypeCompanyDDL").val();
		var pacCTVar=$("#CoolerTypePackDDL").val();
		var brandCTVar=$("#CoolerTypeBrandDDL").val();
		var ValueToAddVar=companyCTVar+"-"+pacCTVar+"-"+brandCTVar;
		var InnerCounter=0;
		
		if(companyCTVar==-1 ||(pacCTVar==-1)||(brandCTVar==-1)){
			if((companyCTVar==-1)==true){
			alert("Select Company");
			}

			else if((pacCTVar==-1)==true){
				alert("Select Package");
				
			}
			else if((brandCTVar==-1)==true){
				alert("Select Quantity");
				
			}
			
		}
		
		else{
		$('#CoolerType_table').find('tr').each(function(InnerCounter) {
			
			var incompanyCTVvar= $("#co"+InnerCounter).val();
			var inpacCTVar=$("#pac"+InnerCounter).val();
			var inbrandCTVar=$("#brand"+InnerCounter).val();
			var inValueTocheckVar=incompanyCTVvar+"-"+inpacCTVar+"-"+inbrandCTVar;
		
			if(ValueToAddVar==inValueTocheckVar)	
				{	  
					alert("Value Duplicate");
					CheckVar=1;
					counter--;
					return false;
					 
				
				}
			

			else {	
					
					CheckVar=2;
				  }
			
		});
		
		if(CheckVar==2){
			var DeleteFunction = "RemoveCoolerType('"+RowID+"')";						
			var CoolTypeCompany=$("#CoolerTypeCompanyDDL option:selected").text();
			var CoolTypeBrand=$("#CoolerTypeBrandDDL option:selected").text();
			var CoolTypePack=$("#CoolerTypePackDDL option:selected").text();
			var CoolTypeCompanyid=$("#CoolerTypeCompanyDDL option:selected").val();
			var CoolTypeBrandid=$("#CoolerTypeBrandDDL option:selected").val();
			var CoolTypePackid=$("#CoolerTypePackDDL option:selected").val();
	        $('#CoolerType_table').append("<tr id='"+RowID+"'>"
	        								+"<td><label  type='text' name='cooltype_co'  id='in"+counter+"'/>"+CoolTypeCompany+"<input type='hidden' id='co"+counter+"' name='cooltype_co' value='"+CoolTypeCompanyid+"'></td>"+
	        								"<td><label type='text' name='cooltype_pack'  id='input"+counter+"'/>"+CoolTypePack+"<input type=hidden id='pac"+counter+"' name='cooltype_pack' value='"+CoolTypePackid+"'></td>"+
	        								"<td><label type='text' name='cooltype_brand'  id='inpu"+counter+"'/>"+CoolTypeBrand+"<input type=hidden id='brand"+counter+"' name='cooltype_brand' value='"+CoolTypeBrandid+"'></td>"+
	        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemoveCoolerType('"+RowID+"')>Delete</a></td>"
	        								+"</tr>").trigger('create');
		}
		
		
		InnerCounter++;
		counter++;
		$("#CoolerTypeRowCounterHidden").val(counter);
		CheckVar=0;
	}
	}		
//publicity code


	function RemovePub(idddd){
				
				
				$('#'+idddd).remove();
				}	
			function addNewPUB(idd){
				
				var counter = $("#PublicityRowCounterHidden").val();
					$('#summary').listview('refresh');
					var RowID = "pub_"+counter;
					var DeleteFunction = "Remove('"+RowID+"')";
					
					var PUBCheckVar=0;
					var PUBcompanyVar=$("#PublicityCompanyDDL").val();
					var PUBpacVar=$("#PublicityPackageDDL").val();
					var PUBbrandVar=$("#PublicityBrandDDL").val();
					var PUBValueToAddVar=PUBcompanyVar+"-"+PUBpacVar+"-"+PUBbrandVar;
					var PUBInnerCounter=0;
					
					if(PUBcompanyVar==-1 ||(PUBpacVar==-1) ||(PUBbrandVar==-1)){
						if((PUBcompanyVar==-1)==true){
							alert("Select Company");
						}
						else if((PUBpacVar==-1)){
							alert("Select Package");
						
						}
						else if((PUBbrandVar==-1)){
							alert("Select Quantity");
							
						}
					}
					
					
					
					else{
					$('#PUBLICITY_table').find('tr').each(function(PUBInnerCounter) {
						
						var PUBincompanyVar= $("#pubco"+PUBInnerCounter).val();
						var PUBinpacVar=$("#pubpac"+PUBInnerCounter).val();
						var PUBinbrandVar=$("#pubbrand"+PUBInnerCounter).val();
						var PUBinValueTocheckVar=PUBincompanyVar+"-"+PUBinpacVar+"-"+PUBinbrandVar;
					
						if(PUBValueToAddVar==PUBinValueTocheckVar)	
							{	  
								alert("Value Duplicate");
								PUBCheckVar=1;
								counter--;
								return false;
							
							}
						

						else {	
								
							PUBCheckVar=2;
							  }
						
					});
					
					if(PUBCheckVar==2){	
						//var idddd = counter;
					var PubCompany=$("#PublicityCompanyDDL option:selected").text();
					var PubBrand=$("#PublicityBrandDDL option:selected").text();
					var PubPack=$("#PublicityPackageDDL option:selected").text();
					var PubCompanyid=$("#PublicityCompanyDDL option:selected").val();
					var PubBrandid=$("#PublicityBrandDDL option:selected").val();
					var PubPackid=$("#PublicityPackageDDL option:selected").val();
			        $('#PUBLICITY_table').append("<tr id='"+RowID+"'>"
			        								+"<td><label  type='text' name='pub_co'  id='in"+counter+"'/>"+PubCompany+"<input type='hidden' id='pubco"+counter+"' name='pub_co' value='"+PubCompanyid+"'></td>"+
			        								"<td><label type='text' name='pub_pack'  id='input"+counter+"'/>"+PubPack+"<input type=hidden id='pubpac"+counter+"' name='pub_pack' value='"+PubPackid+"'></td>"+
			        								"<td><label type='text' name='pub_brand'  id='inpu"+counter+"'/>"+PubBrand+"<input type=hidden id='pubbrand"+counter+"' name='pub_brand' value='"+PubBrandid+"'></td>"+
			        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemovePub('"+RowID+"')>Delete</a></td>"
			        								+"</tr>").trigger('create');
			        
			}
			
			
					PUBInnerCounter++;
					counter++;
					$("#PublicityRowCounterHidden").val(counter);
					PUBCheckVar=0;
				}	
			

			}

//CSD 

	function Remove(idddd){
					
					
					$('#'+idddd).remove();
					}	
			
				
				
				
				//populateDash();
				populateSelect();
				
				//$(function() {
					  //var CSD_CoSelection = $('#companyddl').val();
					//if(CSD_CoSelection==5){
					//$('#companyddl').change(function(){
						//populateDash();
						//alert("populateDash");
							
				    //});
					 //}
					//else if(CSD_CoSelection!=5){
						 //populateSelect();
						 //alert("populateSelect");
					//}
				//});
				
				 $(function() {

				      $('#packddl').change(function(){
				        populateSelect();
				    });

				});

				function populateDash(){
					var csd_dash_id=['1'];
					var csd_dash_name=['-']
					
					var CSD_CoValdash = $('#companyddl').val();
					 if(CSD_CoValdash==5){
				    	 $('#packddl').html('');
				    	 $('#packddl').append('<option value="-1">Select Package </option>');
				    	 for(var i=0;i<csd_dash_id.length;i++){
				        		$('#packddl').append('<option value="'+csd_dash_id[i]+'">'+csd_dash_name[i]+'</option>');
				        		$('#brandddl').append('<option value="'+csd_dash_id[i]+'">'+csd_dash_name[i]+'</option>');
				        	}
				    	
				    }
					 
					
					
				}
				
			

				function populateSelect(){
					
					var csd_Pepsi_id=['1','2','3','4','5','6','7'];
					var csd_pepsi_name=['Pepsi','7-UP','M. Dew','Mirinda','M.Dew BP','7UP-F','Diet Pepsi'];
					
					var csd_Coke_id=['1','2','3','4','5','6','7','8','9'];
					var csd_Coke_name=['Coke','Coke Zero','Diet Coke','Sprite','Spr. Zero','Fanta','Fanta Citrus','Fanta Apple','Fanta Grape'];
					
					var csd_ColaNext_id=['1','2','3','4'];
					var csd_ColaNext_name=['Storm','Fiz-Up','Rango','Mount Dare'];
					
					var csd_Gou_id=['1','2','3','4','5','6','7','8','9','10','11','12','13'];
					var csd_Gou_name=['Gourmet Lemon','Ice Soda','Twister','Red Annar','Malta','Spark','Diet Cola','Dite lemon','Gourmet Apple','Moje Mango','Pulpy Orange','Gava','Gourmet Cola'];
					
					
					var CSD_CoVal = $('#companyddl').val();
					var CSD_PackVal = $('#packddl').val();
					var CSDcompanyVar=$("#companyddl").val();
					var CSDpacVar=$("#packddl").val();
					var CSDbrandVar=$("#brandddl").val();
					
					
					if(CSDcompanyVar==-1 && CSDpacVar==-1){
						 $('#brandddl').html('');
					        //csd_pepsi_name.forEach(function(text) {
					        	$('#brandddl').append('<option value="-1">Select Brand</option>');
					}
					 
					
				    if(CSD_CoVal==1){
				    	 $('#brandddl').html('');
				        //csd_pepsi_name.forEach(function(text) {
				        	$('#brandddl').append('<option value="-1">Select Brand</option>');
				        	
				        	for(var i=0;i<csd_Pepsi_id.length;i++){
				        		$('#brandddl').append('<option value="'+csd_Pepsi_id[i]+'">'+csd_pepsi_name[i]+'</option>');
				        	}
				        	
				            
				       // });
				    }
					
				    if(CSD_CoVal==2){
				    	 $('#brandddl').html('');
				    	 $('#brandddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<csd_Coke_id.length;i++){
			        		$('#brandddl').append('<option value="'+csd_Coke_id[i]+'">'+csd_Coke_name[i]+'</option>');
			        	}
			        	
				    }
				    if(CSD_CoVal==3){
				    	 $('#brandddl').html('');
				    	 $('#brandddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<csd_ColaNext_id.length;i++){
			        		$('#brandddl').append('<option value="'+csd_ColaNext_id[i]+'">'+csd_ColaNext_name[i]+'</option>');
			        	}
			        	
				    }
				    
				    if(CSD_CoVal==4){
				    	 $('#brandddl').html('');
				    	 $('#brandddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<csd_Gou_id.length;i++){
			        		$('#brandddl').append('<option value="'+csd_Gou_id[i]+'">'+csd_Gou_name[i]+'</option>');
			        	}
			        	
				    }
				    if(CSD_CoVal==5){
				    	
				    	 $('#brandddl').html('');
				    	 $('#brandddl').append('<option value="0"> - </option>');
				    	
				    }
				    
				 
				   
				} 
				
				
				function addNew(idd){
					
					var counter = $("#CSDRowCountHidden").val();
					$('#summary').listview('refresh');
						
					var RowID = "csd_"+counter;
							var DeleteFunction = "Remove('"+RowID+"')";
							
							var CSDCheckVar=0;
							CSDcompanyVar=$("#companyddl").val();
							CSDpacVar=$("#packddl").val();
							CSDbrandVar=$("#brandddl").val();
							var CSDValueToAddVar=CSDcompanyVar+"-"+CSDpacVar+"-"+CSDbrandVar;
							//alert("Drop down "+CSDValueToAddVar);
							
							
							if(CSDcompanyVar==-1 ||(CSDpacVar==-1 && CSDcompanyVar!=5)  ||(CSDbrandVar==-1 && CSDcompanyVar!=5)){
								if((CSDcompanyVar==-1)==true){
								alert("Select Company");
								}
								else if((CSDpacVar==-1 && CSDcompanyVar!=5 )==true){
								alert("Select Package");
								}
								else if((CSDbrandVar==-1 && CSDcompanyVar!=5)==true){
								alert("Select Brand");
								}
								
							}
							
							
							
							else{
								var CSDInnerCounter=0;
							$('#CSD_table').find('tr').each(function(CSDInnerCounter) {
								
								var CSDincompanyVar= $("#csdco"+CSDInnerCounter).val();
								var CSDinpacVar=$("#csdpac"+CSDInnerCounter).val();
								var CSDinbrandVar=$("#csdbrand"+CSDInnerCounter).val();
								var CSDinValueTocheckVar=CSDincompanyVar+"-"+CSDinpacVar+"-"+CSDinbrandVar;
								//alert("Table valuees"+CSDinValueTocheckVar);
								if(CSDValueToAddVar==CSDinValueTocheckVar)	
									
									{	  
										alert("Value Duplicate");
										CSDCheckVar=1;
										counter--;
										return false;
									
									}
								

								else {	
										
									CSDCheckVar=2;
									  }
								CSDInnerCounter++;
							});
							
							
							
								
							
							var com=$("#companyddl option:selected").text();
							var Bran=$("#brandddl option:selected").text();
							var pack=$("#packddl option:selected").text();
							var comid=$("#companyddl option:selected").val();
							var Branid=$("#brandddl option:selected").val();
							var packid=$("#packddl option:selected").val();
						
							if(CSDCheckVar==2){	
							//var idddd = counter;
						if(comid==5){
							
							Bran="-";
							pack="-";
						}
					
				        $('#CSD_table').append("<tr id='"+RowID+"'>"
				        								+"<td><label  type='text' name='csd_co'  id='in"+counter+"'/>"+com+"<input type='hidden' id='csdco"+counter+"' name='csd_co' value='"+comid+"'></td>"+
				        								"<td><label type='text' name='csd_pack'  id='input"+counter+"'/>"+pack+"<input type=hidden id='csdpac"+counter+"' name='csd_pack' value='"+packid+"'></td>"+
				        								"<td><label type='text' name='csd_brand'  id='inpu"+counter+"'/>"+Bran+"<input type=hidden id='csdbrand"+counter+"' name='csd_brand' value='"+Branid+"'></td>"+
				        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=Remove('"+RowID+"')>Delete</a></td>"
				        								+"</tr>").trigger('create');
				        
							}
				       
							
						
						counter++;
						$("#CSDRowCountHidden").val(counter);
						CSDCheckVar=0;
				
							
							}
				
				}
				

//NCB

	function RemoveNCB(idddd){
					
					
					$('#'+idddd).remove();
					}	
				
				NCBpopulateSelect();
				
				

				$(function() {

				      $('#NCBpackddl').change(function(){
				        NCBpopulateSelect();
				    });

				});

				
				
				function NCBpopulateSelect(){
					var	ncb_water_id=['1','2','3','4','5'];
					var ncb_water_name=['A.F','NESTLE','KINLEY','GOURMET','OTHERS'];
					
					var ncb_juice_id=['1','2','3','4','5','6'];
					var ncb_juice_name=['SLICE','NESTLE','SHEZAN','FRUITIEN','RANI','OTHER'];
					
					var ncb_EnergyDrink_id=['1','2','3'];
					var ncb_EnergyDrink_name=['STING','REDBULL','OTHER'];
					
					var ncb_FlavouredMilk_id=['1','2'];
					var ncb_FlavouredMilk_name=['NESTLE','OLPER'];
					
					var NCBcompanyVar=$("#NCBcompanyddl").val();
					var NCBpacVar=$("#NCBpackddl").val();
					var NCBbrandVar=$("#NCBbrandddl").val();
					
					
					var NCB_CoVal = $('#NCBcompanyddl').val();
					var NCB_PackVal = $('#NCBpackddl').val();
					
					if(NCBcompanyVar==-1 && NCBpacVar==-1){
						 $('#NCBbrandddl').html('');
					        //csd_pepsi_name.forEach(function(text) {
					        	$('#NCBbrandddl').append('<option value="-1">Select Brand</option>');
					}
					if(NCB_CoVal==1){
				    	 $('#NCBbrandddl').html('');
				        //csd_pepsi_name.forEach(function(text) {
				        	$('#NCBbrandddl').append('<option value="-1">Select Brand</option>');
				        	
				        	for(var i=0;i<ncb_water_id.length;i++){
				        		$('#NCBbrandddl').append('<option value="'+ncb_water_id[i]+'">'+ncb_water_name[i]+'</option>');
				        	}
				        	
				            
				       // });
				    }
					
				    if(NCB_CoVal==2){
				    	 $('#NCBbrandddl').html('');
				    	 $('#NCBbrandddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<ncb_juice_id.length;i++){
			        		$('#NCBbrandddl').append('<option value="'+ncb_juice_id[i]+'">'+ncb_juice_name[i]+'</option>');
			        	}
			        	
				    }
				    if(NCB_CoVal==3){
				    	 $('#NCBbrandddl').html('');
				    	 $('#NCBbrandddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<ncb_EnergyDrink_id.length;i++){
			        		$('#NCBbrandddl').append('<option value="'+ncb_EnergyDrink_id[i]+'">'+ncb_EnergyDrink_name[i]+'</option>');
			        	}
			        	
				    }
				    
				    if(NCB_CoVal==4){
				    	 $('#NCBbrandddl').html('');
				    	 $('#NCBbrandddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<ncb_FlavouredMilk_id.length;i++){
			        		$('#NCBbrandddl').append('<option value="'+ncb_FlavouredMilk_id[i]+'">'+ncb_FlavouredMilk_name[i]+'</option>');
			        	}
			        	
				    }
				    if(NCB_CoVal==5){
				    	 $('#NCBbrandddl').html('');
				    	 $('#NCBbrandddl').append('<option value="-1">Select Brand</option>');
				    	
			        	
				    } 
				} 
				
				
				function addNewNCB(idd){
					
					var counter = $("#NCBRowCounterHidden").val();
					$('#summary').listview('refresh');
							
							var RowID = "ncb_"+counter;
							var DeleteFunction = "Remove('"+RowID+"')";
							
							var NCBCheckVar=0;
							NCBcompanyVar=$("#NCBcompanyddl").val();
							NCBpacVar=$("#NCBpackddl").val();
							NCBbrandVar=$("#NCBbrandddl").val();
							var NCBValueToAddVar=NCBcompanyVar+"-"+NCBpacVar+"-"+NCBbrandVar;
							//alert("Drop down "+NCBValueToAddVar);
							var NCBInnerCounter=0;
							
							if(NCBcompanyVar==-1 ||(NCBpacVar==-1 && NCBcompanyVar!=5 ) ||(NCBbrandVar==-1 && NCBcompanyVar!=5 )){
								if((NCBcompanyVar==-1 )==true){
								alert("Select Company");
								}
								if((NCBpacVar==-1 && NCBcompanyVar!=5)==true){
								alert("Select Package");
								}
								if((NCBbrandVar==-1 && NCBcompanyVar!=5)==true){
								alert("Select Brand");
								}
							}
							
							
							
							else{
							$('#NCB_table').find('tr').each(function(NCBInnerCounter) {
								
								var ncbincompanyVar= $("#ncbco"+ NCBInnerCounter ).val();
								var ncbinpacVar=$("#ncbpac"+NCBInnerCounter).val();
								var ncbinbrandVar=$("#ncbbrand"+NCBInnerCounter).val();
								var ncbinValueTocheckVar=ncbincompanyVar+"-"+ncbinpacVar+"-"+ncbinbrandVar;
								//alert("Table valuees"+ncbinValueTocheckVar);
								if(NCBValueToAddVar==ncbinValueTocheckVar)	
									
									{	  
										alert("Value Duplicate");
										NCBCheckVar=1;
										counter--;
										return false;
									
									}
								

								else {	
										
									NCBCheckVar=2;
									  }
								
							});
							
							//alert("After Loop"+NCBCheckVar);
							var Ncbcom=$("#NCBcompanyddl option:selected").text();
							var NcbBran=$("#NCBbrandddl option:selected").text();
							var Ncbpack=$("#NCBpackddl option:selected").text();
							var Ncbcomid=$("#NCBcompanyddl option:selected").val();
							var NcbBranid=$("#NCBbrandddl option:selected").val();
							var Ncbpackid=$("#NCBpackddl option:selected").val();
							if(NCBCheckVar==2){	
								
							//var idddd = counter;
						
						if(Ncbcomid==5){
							Ncbpack="-";
							NcbBran="-";
						}
						$('#NCB_table').append("<tr id='"+RowID+"'>"
				        								+"<td><label  type='text' name='ncb_co'  id='in"+counter+"'/>"+Ncbcom+"<input type='hidden' id='ncbco"+counter+"' name='ncb_co' value='"+Ncbcomid+"'></td>"+
				        								"<td><label type='text' name='ncb_pack'  id='input"+counter+"'/>"+Ncbpack+"<input type=hidden id='ncbpac"+counter+"' name='ncb_pack' value='"+Ncbpackid+"'></td>"+
				        								"<td><label type='text' name='ncb_brand'  id='inpu"+counter+"'/>"+NcbBran+"<input type=hidden id='ncbbrand"+counter+"' name='ncb_brand' value='"+NcbBranid+"'></td>"+
				        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemoveNCB('"+RowID+"')>Delete</a></td>"
				        								+"</tr>").trigger('create');
				        
				        
						
				}

							NCBInnerCounter++;
							counter++;
							$("#NCBRowCounterHidden").val(counter);
							NCBCheckVar=0;
						}
				}				
				

//VOLCSD

function RemoveVolCSD(idddd){
					
					
					$('#'+idddd).remove();
					}	
				
				function addNewVolCSD(idd){
					
					var counter = $("#VolCSDRowCounterHidden").val();
					$('#summary').listview('refresh');
							var RowID = "volcsd_"+counter;
							var DeleteFunction = "Remove('"+RowID+"')";
							
							var VolCSDCheckVar=0;
							var VolCSDcompanyVar=$("#VolCSDcompanyddl").val();
							//alert (VolCSDcompanyVar);
							var VolCSDpacVar=$("#VolCSDpackddl").val();
							//alert ("VolCSDpacVar"+VolCSDpacVar);
							var VolCSDbrand1Var=$("#shopkeeper_volcsd").val();
							//alert ("shopkeeper_volcsd"+VolCSDbrand1Var);
							var VolCSDbrand2Var=$("#MDE_volcsd").val();
							//alert ("MDE_volcsd"+VolCSDbrand2Var);
							var VolCSDValueToAddVar=VolCSDcompanyVar+"-"+VolCSDpacVar+"-"+VolCSDbrand1Var+"-"+VolCSDbrand2Var;
							//alert("Drop down "+VolCSDValueToAddVar);
							var VolCSDInnerCounter=0;
							
							if(VolCSDcompanyVar==-1||(VolCSDpacVar==-1 && VolCSDcompanyVar!=4) ||(VolCSDbrand1Var=="" && VolCSDcompanyVar!=4)||(VolCSDbrand2Var=="" && VolCSDcompanyVar!=4)){
								if((VolCSDcompanyVar==-1)==true){
									alert("Select Company");
								}
								
								else if((VolCSDpacVar==-1 && VolCSDcompanyVar!=4)==true){
									alert("Select Package");
									
								}
								else if((VolCSDbrand1Var=="" && VolCSDcompanyVar!=4)==true){
									alert("Shopkeeper Field is Empty");
									
								}
								else if((VolCSDbrand2Var=="" && VolCSDcompanyVar!=4)==true){
									alert("MME Field is Empty");
									
								}
							}
							else{
								
							$('#VolCSD_table').find('tr').each(function(VolCSDInnerCounter) {
								
								var volcsdincompanyVar= $("#volcsdco"+ VolCSDInnerCounter ).val();
								var volcsdinpacVar=$("#volcsdpac"+VolCSDInnerCounter).val();
								var volcsdinbrand1Var=$("#volcsdbrand1"+VolCSDInnerCounter).val();
								var volcsdinbrand2Var=$("#volcsdbrand2"+VolCSDInnerCounter).val();
								var volcsdinValueTocheckVar=volcsdincompanyVar+"-"+volcsdinpacVar+"-"+volcsdinbrand1Var+"-"+volcsdinbrand2Var;
								//alert("Table valuees"+volcsdinValueTocheckVar);
								if(VolCSDValueToAddVar==volcsdinValueTocheckVar)	
									
									{	  
										alert("Value Duplicate");
										VolCSDCheckVar=1;
										counter--;
										return false;
									
									}
								

								else {	
										
									VolCSDCheckVar=2;
									  }
								
							});
							
							//alert("After Loop"+VolCSDCheckVar);
							if(VolCSDCheckVar==2){		
							
							//var idddd = counter;
						var VolCSDcom=$("#VolCSDcompanyddl option:selected").text();
						var VolCSDcomid=$("#VolCSDcompanyddl option:selected").val();
						var VolCSDpack=$("#VolCSDpackddl option:selected").text();
						var VolCSDpackid=$("#VolCSDpackddl option:selected").val();
						
						var VolCSDBran1=$('#shopkeeper_volcsd').val();
						var VolCSDBran2=$('#MDE_volcsd').val();
						
						if(VolCSDcomid==4){
							VolCSDpack="-";
							VolCSDBran1="-";
							VolCSDBran2="-";
							
						}
						
				        $('#VolCSD_table').append("<tr id='"+RowID+"'>"
				        								+"<td><label  type='text' name='volcsd_co'  id='in"+counter+"'/>"+VolCSDcom+"<input type='hidden' id='volcsdco"+counter+"' name='volcsd_co' value='"+VolCSDcomid+"'></td>"+
				        								"<td><label type='text' name='volcsd_pack'  id='input"+counter+"'/>"+VolCSDpack+"<input type=hidden id='volcsdpac"+counter+"' name='volcsd_pack' value='"+VolCSDpackid+"'></td>"+
				        								"<td><label type='text' name='volcsd_brand1'  id='inpu"+counter+"'/>"+VolCSDBran1+"<input type=hidden id='volcsdbrand1"+counter+"' name='volcsd_brand1' value='"+VolCSDBran1+"'></td>"+
				        								"<td><label type='text' name='volcsd_brand2'  id='input2"+counter+"'/>"+VolCSDBran2+"<input type=hidden id='volcsdbrand2"+counter+"' name='volcsd_brand2' value='"+VolCSDBran2+"'></td>"+
				        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemoveVolCSD('"+RowID+"')>Delete</a></td>"
				        								+"</tr>").trigger('create');
				        
						
							}
			
							VolCSDInnerCounter++;
							counter++;
							$("#VolCSDRowCounterHidden").val(counter);
							VolCSDCheckVar=0;
						}
								
							}
				

//VolJuice

	function RemoveVolJuice(idddd){
					
					
					$('#'+idddd).remove();
					}	
				
				var	volJ_pi_id=['1'];
				var volJ_pi_name=['TETRA'];
				
				var volJ_Ko_id=['1','2'];
				var volJ_Ko_name=['SSRB','PET'];
				
				var volJ_Others_id=['1','2','3','4','5'];
				var volJ_Others_name=['SSRB','NRB','PET','TETRA','CAN'];
				
				var VolJuicecompanyVar=$("#VolJuicecompanyddl").val();
			
				volJpopulateSelect();

				$(function() {

				      $('#VolJuicecompanyddl').change(function(){
				        volJpopulateSelect();
				    });

				});


				function volJpopulateSelect(){
					var	volJ_pi_id=['1'];
					var volJ_pi_name=['TETRA'];
					
					var volJ_Ko_id=['1','2'];
					var volJ_Ko_name=['SSRB','PET'];
					
					var volJ_Others_id=['1','2','3','4','5'];
					var volJ_Others_name=['SSRB','NRB','PET','TETRA','CAN'];
					
					var VolJuicecompanyVar=$("#VolJuicecompanyddl").val();
				
					
					var VolJuice_CoVal = $('#VolJuicecompanyddl').val();
					if(VolJuicecompanyVar==-1){
						 $('#VolJuicepackddl').html('');
					        //csd_pepsi_name.forEach(function(text) {
					        	$('#VolJuicepackddl').append('<option value="-1">Select Package</option>');
					}
					
					
				    if(VolJuice_CoVal==1){
				    	 $('#VolJuicepackddl').html('');
				        //csd_pepsi_name.forEach(function(text) {
				        	$('#VolJuicepackddl').append('<option value="-1">Select Brand</option>');
				        	
				        	for(var i=0;i<volJ_pi_id.length;i++){
				        		$('#VolJuicepackddl').append('<option value="'+volJ_pi_id[i]+'">'+volJ_pi_name[i]+'</option>');
				        	}
				        	
				            
				       // });
				    }
					
				    if(VolJuice_CoVal==2){
				    	 $('#VolJuicepackddl').html('');
				    	 $('#VolJuicepackddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<volJ_Ko_id.length;i++){
			        		$('#VolJuicepackddl').append('<option value="'+volJ_Ko_id[i]+'">'+volJ_Ko_name[i]+'</option>');
			        	}
			        	
				    }
				    if(VolJuice_CoVal==3){
				    	 $('#VolJuicepackddl').html('');
				    	 $('#VolJuicepackddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<volJ_Others_id.length;i++){
			        		$('#VolJuicepackddl').append('<option value="'+volJ_Others_id[i]+'">'+volJ_Others_name[i]+'</option>');
			        	}
			        	
				    }
				    
				  
				    if(VolJuice_CoVal==4){
				    	 $('#VolJuicepackddl').html('');
				    	 $('#VolJuicepackddl').append('<option value="-1">Select Brand</option>');
				    	
			        	
				    } 
				} 
				
				function addNewVolJuice(idd){
					
					var counter = $("#VolJuiceRowCounterHidden").val();
					$('#summary').listview('refresh');
						
							var RowID = "voljuice_"+counter;
							var DeleteFunction = "Remove('"+RowID+"')";
							
							var VolJuiceCheckVar=0;
							 VolJuicecompanyVar=$("#VolJuicecompanyddl").val();
							//alert (VolCSDcompanyVar);
							var VolJuicepacVar=$("#VolJuicepackddl").val();
							//alert ("VolCSDpacVar"+VolCSDpacVar);
							var VolJuicebrand1Var=$("#shopkeeper_voljuice").val();
							//alert ("shopkeeper_volcsd"+VolCSDbrand1Var);
							var VolJuicebrand2Var=$("#MDE_voljuice").val();
							//alert ("MDE_volcsd"+VolCSDbrand2Var);
							var VolJuiceValueToAddVar=VolJuicecompanyVar+"-"+VolJuicepacVar+"-"+VolJuicebrand1Var+"-"+VolJuicebrand2Var;
							//alert("Drop down "+VolJuiceValueToAddVar);
							var VolJuiceInnerCounter=0;
							
							if(VolJuicecompanyVar==-1 ||(VolJuicepacVar==-1 && VolJuicecompanyVar!=4) || (VolJuicebrand1Var=="" && VolJuicecompanyVar!=4)||(VolJuicebrand2Var=="" && VolJuicecompanyVar!=4)){
								if((VolJuicecompanyVar==-1)==true){
								alert("Select Company");
								}
								
								else if((VolJuicepacVar==-1 && VolJuicecompanyVar!=4)==true){
									alert("Select Package");
									
								}
								else if((VolJuicebrand1Var=="" && VolJuicecompanyVar!=4)==true){
									alert("Shopkeeper Field is Empty");
									
								}
								else if((VolJuicebrand2Var=="" && VolJuicecompanyVar!=4)==true){
									alert("MME Field is Empty");
									
								}
							}
							
							
							
							else {
							$('#VolJuice_table').find('tr').each(function(VolJuiceInnerCounter) {
								
								var voljuiceincompanyVar= $("#voljuiceco"+ VolJuiceInnerCounter ).val();
								var voljuiceinpacVar=$("#voljuicepac"+VolJuiceInnerCounter).val();
								var voljuiceinbrand1Var=$("#voljuicebrand1"+VolJuiceInnerCounter).val();
								var voljuiceinbrand2Var=$("#voljuicebrand2"+VolJuiceInnerCounter).val();
								var voljuiceinValueTocheckVar=voljuiceincompanyVar+"-"+voljuiceinpacVar+"-"+voljuiceinbrand1Var+"-"+voljuiceinbrand2Var;
								//alert("Table valuees"+voljuiceinValueTocheckVar);
								if(VolJuiceValueToAddVar==voljuiceinValueTocheckVar)	
									
									{	  
										alert("Value Duplicate");
										VolJuiceCheckVar=1;
										counter--;
										return false;
									
									}
								

								else {	
										
									VolJuiceCheckVar=2;
									  }
								
							});
							
							//alert("After Loop"+VolJuiceCheckVar);
							if(VolJuiceCheckVar==2){	
							
							//var idddd = counter;
						var VolJuicecom=$("#VolJuicecompanyddl option:selected").text();
						var VolJuicecomid=$("#VolJuicecompanyddl option:selected").val();
						var VolJuicepack=$("#VolJuicepackddl option:selected").text();
						var VolJuicepackid=$("#VolJuicepackddl option:selected").val();
						
						var VolJuiceBran1=$('#shopkeeper_voljuice').val();
						//alert($('#shopkeeper_voljuice').val());
						var VolJuiceBran2=$('#MDE_voljuice').val();
						if(VolJuicecomid==4){
							VolJuicepack="-";
							VolJuiceBran1="-";
							VolJuiceBran2="-";
							
						}
						
				        $('#VolJuice_table').append("<tr id='"+RowID+"'>"
				        								+"<td><label  type='text' name='voljuice_co'  id='in"+counter+"'/>"+VolJuicecom+"<input type='hidden' id='voljuiceco"+counter+"' name='voljuice_co' value='"+VolJuicecomid+"'></td>"+
				        								"<td><label type='text' name='voljuice_pack'  id='input"+counter+"'/>"+VolJuicepack+"<input type=hidden id='voljuicepac"+counter+"' name='voljuice_pack' value='"+VolJuicepackid+"'></td>"+
				        								"<td><label type='text' name='voljuice_brand1'  id='inpu"+counter+"'/>"+VolJuiceBran1+"<input type=hidden id='voljuicebrand1"+counter+"' name='voljuice_brand1' value='"+VolJuiceBran1+"'></td>"+
				        								"<td><label type='text' name='voljuice_brand2'  id='input2"+counter+"'/>"+VolJuiceBran2+"<input type=hidden id='voljuicebrand2"+counter+"' name='voljuice_brand2' value='"+VolJuiceBran2+"'></td>"+
				        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemoveVolJuice('"+RowID+"')>Delete</a></td>"
				        								+"</tr>").trigger('create');
				        
				        
							}
						
						
						
						
						
						VolJuiceInnerCounter++;
						counter++;
						$("#VolJuiceRowCounterHidden").val(counter);
						VolJuiceCheckVar=0;
				}
				
				}


//VOL DRink

			function RemoveVolDrink(idddd){
					
					
					$('#'+idddd).remove();
					}	
				
				
				
				VolDpopulateSelect();

				$(function() {

				      $('#VolDrinkcompanyddl').change(function(){
				    	  VolDpopulateSelect();
				    });

				});


				function VolDpopulateSelect(){
					var	voldrink_water_id=['1','2','3'];
					var voldrink_water_name=['PI','KO','Others'];
					
					var voldrink_energyD_id=['1','2','3'];
					var voldrink_energyD_name=['STING','REDBULL','Others'];
					
					var VolDrinkcompanyVar=$("#VolDrinkcompanyddl").val();
					
					var Voldrink_CoVal = $('#VolDrinkcompanyddl').val();
					if(VolDrinkcompanyVar==-1){
						 $('#VolDrinkpackddl').html('');
					        //csd_pepsi_name.forEach(function(text) {
					        	$('#VolDrinkpackddl').append('<option value="-1">Select Package</option>');
					}
					
				    if(Voldrink_CoVal==1){
				    	 $('#VolDrinkpackddl').html('');
				        //csd_pepsi_name.forEach(function(text) {
				        	$('#VolDrinkpackddl').append('<option value="-1">Select Brand</option>');
				        	
				        	for(var i=0;i<voldrink_water_id.length;i++){
				        		$('#VolDrinkpackddl').append('<option value="'+voldrink_water_id[i]+'">'+voldrink_water_name[i]+'</option>');
				        	}
				        	
				            
				       // });
				    }
					
				    if(Voldrink_CoVal==2){
				    	 $('#VolDrinkpackddl').html('');
				    	 $('#VolDrinkpackddl').append('<option value="-1">Select Brand</option>');
				    	for(var i=0;i<voldrink_energyD_id.length;i++){
			        		$('#VolDrinkpackddl').append('<option value="'+voldrink_energyD_id[i]+'">'+voldrink_energyD_name[i]+'</option>');
			        	}
			        	
				    }
				   
				    
				    if(Voldrink_CoVal==3){
				    	 $('#VolDrinkpackddl').html('');
				    	 $('#VolDrinkpackddl').append('<option value="-1">Select Brand</option>');
				    	
			        	
				    }
				   
				} 
				
				
				function addNewVolDrink(idd){
					
					var counter = $("#VolDrinkRowCounterHidden").val();
					$('#summary').listview('refresh');
							var RowID = "voldrink_"+counter;
							var DeleteFunction = "Remove('"+RowID+"')";
							
							var VolDrinkCheckVar=0;
							VolDrinkcompanyVar=$("#VolDrinkcompanyddl").val();
							//alert (VolCSDcompanyVar);
							var VolDrinkpacVar=$("#VolDrinkpackddl").val();
							//alert ("VolCSDpacVar"+VolCSDpacVar);
							var VolDrinkbrand1Var=$("#shopkeepervoldrink").val();
							//alert ("shopkeeper_volcsd"+VolCSDbrand1Var);
							var VolDrinkbrand2Var=$("#MDEvoldrink").val();
							//alert ("MDE_volcsd"+VolCSDbrand2Var);
							var VolDrinkValueToAddVar=VolDrinkcompanyVar+"-"+VolDrinkpacVar+"-"+VolDrinkbrand1Var+"-"+VolDrinkbrand2Var;
							//alert("Drop down "+VolDrinkValueToAddVar);
							var VolDrinkInnerCounter=0;
							
							if(VolDrinkcompanyVar==-1 || (VolDrinkpacVar==-1 && VolDrinkcompanyVar!=3)|| (VolDrinkbrand1Var==""  && VolDrinkcompanyVar!=3)||(VolDrinkbrand2Var=="" && VolDrinkcompanyVar!=3)){
								if((VolDrinkcompanyVar==-1)==true){
								alert("Select Company");
								}
								else if((VolDrinkpacVar==-1 && VolDrinkcompanyVar!=3)==true){
									alert("Select Package");
									
								}
								else if((VolDrinkbrand1Var=="" && VolDrinkcompanyVar!=3)==true){
									alert("Shopkeeper Field is Empty");
									
								}
								else if((VolDrinkbrand2Var=="" && VolDrinkcompanyVar!=3)==true){
									alert("MME Field is Empty");
									
								}
							}
							
							
							
							else{
							$('#VolDrinks_table').find('tr').each(function(VolDrinkInnerCounter) {
								
								var voldrinkincompanyVar= $("#voldrinkco"+ VolDrinkInnerCounter ).val();
								var voldrinkinpacVar=$("#voldrinkpac"+VolDrinkInnerCounter).val();
								var voldrinkinbrand1Var=$("#voldrinkbrand1"+VolDrinkInnerCounter).val();
								var voldrinkinbrand2Var=$("#voldrinkbrand2"+VolDrinkInnerCounter).val();
								var voldrinkinValueTocheckVar=voldrinkincompanyVar+"-"+voldrinkinpacVar+"-"+voldrinkinbrand1Var+"-"+voldrinkinbrand2Var;
								//alert("Table valuees"+voldrinkinValueTocheckVar);
								if(VolDrinkValueToAddVar==voldrinkinValueTocheckVar)	
									
									{	  
										alert("Value Duplicate");
										VolDrinkCheckVar=1;
										counter--;
										return false;
									
									}
								

								else {	
										
									VolDrinkCheckVar=2;
									  }
								
							});
						
							//alert("After Loop"+VolDrinkCheckVar);
							if(VolDrinkCheckVar==2){	
							
							//var idddd = counter;
						var VolDrinkcom=$("#VolDrinkcompanyddl option:selected").text();
						var VolDrinkcomid=$("#VolDrinkcompanyddl option:selected").val();
						var VolDrinkpack=$("#VolDrinkpackddl option:selected").text();
						var VolDrinkpackid=$("#VolDrinkpackddl option:selected").val();
						
						var  VolDrinkBran1=$('#shopkeepervoldrink').val();
						var  VolDrinkBran2=$('#MDEvoldrink').val();
						if(VolDrinkcomid==3){
							VolDrinkpack="-";
							VolDrinkBran1="-";
							VolDrinkBran2="-";
							
						}
						
				        $('#VolDrinks_table').append("<tr id='"+RowID+"'>"
				        								+"<td><label  type='text' name='voldrink_co'  id='in"+counter+"'/>"+VolDrinkcom+"<input type='hidden' id='voldrinkco"+counter+"' name='voldrink_co' value='"+VolDrinkcomid+"'></td>"+
				        								"<td><label type='text' name='voldrink_pack'  id='input"+counter+"'/>"+VolDrinkpack+"<input type=hidden id='voldrinkpac"+counter+"' name='voldrink_pack' value='"+VolDrinkpackid+"'></td>"+
				        								"<td><label type='text' name='voldrink_brand1'  id='inpu"+counter+"'/>"+VolDrinkBran1+"<input type=hidden id='voldrinkbrand1"+counter+"' name='voldrink_brand1' value='"+VolDrinkBran1+"'></td>"+
				        								"<td><label type='text' name='voldrink_brand2'  id='input2"+counter+"'/>"+VolDrinkBran2+"<input type=hidden id='voldrinkbrand2"+counter+"' name='voldrink_brand2' value='"+VolDrinkBran2+"'></td>"+
				        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemoveVolDrink('"+RowID+"')>Delete</a></td>"
				        								+"</tr>").trigger('create');
				        
				        
				       
				       
						
					
				}
							
							VolDrinkInnerCounter++;
							counter++;
							$("#VolDrinkRowCounterHidden").val(counter);
							VolDrinkCheckVar=0;
					}
					
				}
			

//Cooler Placement
					
					function RemoveCoolerPlace(idddd){
						
										
						$('#'+idddd).remove();
										}	
									
									
					function addNewCoolerPlace(idd){
										
					var counter = $("#CoolerPlaceRowCounterHidden").val();
									
										
					$('#summary').listview('refresh');
											
						
							
							
							var RowID = "coolplace_"+counter;
							var DeleteFunction = "RemoveCoolerPlace('"+RowID+"')";
							
							var CPCheckVar=0;
							var CPcompanyVar=$("#CoolPlaceCompanyDDL").val();
							//alert (VolCSDcompanyVar);
							var CPpacVar=$("#CoolPlacePackDDL").val();
							//alert ("VolCSDpacVar"+VolCSDpacVar);
							var CPbrand1Var=$("#CoolPlaceBrandDDL").val();
							//alert ("shopkeeper_volcsd"+VolCSDbrand1Var);
							
							var CPValueToAddVar=CPcompanyVar+"-"+CPpacVar+"-"+CPbrand1Var;
							//alert("Drop down "+CPValueToAddVar);
							var CPInnerCounter=0;
							
							if(CPcompanyVar==-1 ||(CPpacVar==-1) ||(CPbrand1Var==-1)){
								if((CPcompanyVar==-1)==true){
								alert("Select Company");
								
								}
								
								else if((CPpacVar==-1)==true){
									alert("Select Package");
								
								}
								else if((CPbrand1Var==-1==true)){
									alert("Select Brand");
								
								}
							}
						
							
							
							else{
							$('#CoolerPlace_table').find('tr').each(function(CPInnerCounter) {
								//alert("Coolerplace for loop");
								
								var cpincompanyVar= $("#clco"+ CPInnerCounter ).val();
								var cpinpacVar=$("#clpac"+CPInnerCounter).val();
								var cpinbrand1Var=$("#clbrand"+CPInnerCounter).val();
								
								var cpinValueTocheckVar=cpincompanyVar+"-"+cpinpacVar+"-"+cpinbrand1Var;
								//alert("Table valuees"+cpinValueTocheckVar);
								if(CPValueToAddVar==cpinValueTocheckVar)	
									
									{	  
										alert("Value Duplicate");
										CPCheckVar=1;
										counter--;
										return false;
									
									}
								

								else {	
										
									CPCheckVar=2;
									
									  }
								CPInnerCounter++;
								//alert("CPInnerCounter" +CPInnerCounter);
							});
							
							//alert("After Loop"+CPCheckVar);
							if(CPCheckVar==2){	
							
							
							//var idddd = counter;
						var CoolPlaceCompany=$("#CoolPlaceCompanyDDL option:selected").text();
						var CoolPlaceBrand=$("#CoolPlaceBrandDDL option:selected").text();
						var CoolPlacePack=$("#CoolPlacePackDDL option:selected").text();
						var CoolPlaceCompanyid=$("#CoolPlaceCompanyDDL option:selected").val();
						var CoolPlaceBrandid=$("#CoolPlaceBrandDDL option:selected").val();
						var CoolPlacePackid=$("#CoolPlacePackDDL option:selected").val();
				        $('#CoolerPlace_table').append("<tr id='"+RowID+"'>"
				        								+"<td><label  type='text' name='coolplace_co'  id='in"+counter+"'/>"+CoolPlaceCompany+"<input type='hidden' id='clco"+counter+"' name='coolplace_co' value='"+CoolPlaceCompanyid+"'></td>"+
				        								"<td><label type='text' name='coolplace_pack'  id='input"+counter+"'/>"+CoolPlacePack+"<input type=hidden id='clpac"+counter+"' name='coolplace_pack' value='"+CoolPlacePackid+"'></td>"+
				        								"<td><label type='text' name='coolplace_brand'  id='inpu"+counter+"'/>"+CoolPlaceBrand+"<input type=hidden id='clbrand"+counter+"' name='coolplace_brand' value='"+CoolPlaceBrandid+"'></td>"+
				        								"<td><a href='#' data-role='button' data-icon='delete' data-iconpos='notext' data-theme='c' data-ajax='false' data-inline='true' onclick=RemoveCoolerPlace('"+RowID+"')>Delete</a></td>"
				        								+"</tr>").trigger('create');
				        		
				        counter++;
						
							}
				

							
							
							
							$("#CoolerPlaceRowCounterHidden").val(counter);
							CPCheckVar=0;
					}
			}
