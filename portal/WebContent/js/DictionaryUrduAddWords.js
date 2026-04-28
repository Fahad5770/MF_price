$( document ).delegate("#DictionaryUrduAddWordsMain", "pageinit", function() {

});

function AddWords(){
	///alert($("#UrduWordHidden").val());
	
	var urduVal =document.getElementById("UrduWord").value; 
	//alert(urduVal.length);
	var urduValConcat="";
	var x  =0;
	for (x = 0; x < urduVal.length; x++){
		urduValConcat += "&#"+urduVal.charCodeAt(x)+";"; //converting to unicode bec we are storing unicodes in db
	}
	document.getElementById("UrduWordHidden").value = urduValConcat;
	
	$.ajax({
	    url: "dictionary/DictionaryUrduAddWordsExecute",
	    
	    data: $("#DictionaryUrduAddWordsForm" ).serialize(),
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		LoadAllWords();
	    		$("#EnglishWord").val("");
	    		$("#UrduWord").val("");
	    		$("#DictionaryIsEditFlag").val(0); //reseting flag to 0 for insertion case by default
	    	}else{
				alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function  LoadAllWords(){//
	$.ajax({
	    url: "dictionary/DictionaryUrduAddWordsJson",
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		$("#AllWordsULlist").html("");
	    		
	    		$("#AllWordsULlist").append(json.jsonstring);
    			$('#AllWordsULlist').listview('refresh');
	    		/*for(var i =0; i<json.rows.length;i++){
	    			//alert(json.rows[i].ID);
	    			$("#AllWordsULlist").append("<li><a href='#'>"+json.rows[i].English+" - "+json.rows[i].Urdu+"</a></li>");
	    			$('#AllWordsULlist').listview('refresh');
	    		}*/
	    	}else{
				alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function UrduDictionaryWords(){
	$.ajax({
	    url: "dictionary/DictionaryUrduWordsExecute",    
	    
	    type: "POST",
	    dataType : "json",
	    success: function( json ) {
	    	if (json.success == "true"){
	    		alert("Done");
	    	}else{
				alert(json.error);
	    		//alert("Server could not be reached.");
	    	}
	    },
	    error: function( xhr, status ) {
	    	alert("Server could not be reached.");
	    }
	});
}

function SetCommonWord(word){
	$("#EnglishWord").val(word);
}

function EditCase(ID,Eng,Urdu){
	//alert(ID);
	$("#EnglishWord").val(Eng);
	$("#UrduWord").val(Urdu);
	$("#DictionaryIsEditFlag").val(1); //setting is edit flag = 1 for edit case
	$("#DictionaryWordID").val(ID);
}

function changeToUrduinput(){
	
	//document.getElementById("log").innerHTML += event.keyCode;
	
	
	if (event.shiftKey){
		if (event.keyCode == 72){ // h
			document.getElementById("UrduWord").value += String.fromCharCode(1607);
			document.getElementById("UrduWordHidden").value += "&#1607;";
			return false;
		}else
		if (event.keyCode == 73){ // i
			document.getElementById("UrduWord").value += String.fromCharCode(1592);
			document.getElementById("UrduWordHidden").value += "&#1592;";
			return false;
		}else
		if (event.keyCode == 75){ // k
			document.getElementById("UrduWord").value += String.fromCharCode(1582);//'&#1582;';
			document.getElementById("UrduWordHidden").value += "&#1582;";
			return false;
		}else
		if (event.keyCode == 83){ // s
			document.getElementById("UrduWord").value += String.fromCharCode(1588);//'&#1588;';
			document.getElementById("UrduWordHidden").value += "&#1588;";
			return false;
		}else
		if (event.keyCode == 65){ // a
			document.getElementById("UrduWord").value += String.fromCharCode(1570);//'&#1570;';
			document.getElementById("UrduWordHidden").value += "&#1570;";
			return false;
		}else
		if (event.keyCode == 68){ // d
			document.getElementById("UrduWord").value += String.fromCharCode(1672);//'&#1672;';
			document.getElementById("UrduWordHidden").value += "&#1672;";
			return false;
		}else
		if (event.keyCode == 84){ // t
			document.getElementById("UrduWord").value += String.fromCharCode(1657);//'&#1657;';
			document.getElementById("UrduWordHidden").value += "&#1657;";
			return false;
		}
		if (event.keyCode == 82){ // r
			document.getElementById("UrduWord").value += String.fromCharCode(1681);//'&#1681;';
			document.getElementById("UrduWordHidden").value += "&#1681;";
			return false;
		}else
		if (event.keyCode == 78){ // n
			document.getElementById("UrduWord").value += String.fromCharCode(64414);//'&#64414;';
			document.getElementById("UrduWordHidden").value += "&#64414;";
			return false;
		}else
		if (event.keyCode == 89){ // y
			document.getElementById("UrduWord").value += String.fromCharCode(64431);//'&#64431;';
			document.getElementById("UrduWordHidden").value += "&#64431;";
			return false;
		}else
		if (event.keyCode == 69){ // e
			document.getElementById("UrduWord").value += String.fromCharCode(1594);//'&#1594;';
			document.getElementById("UrduWordHidden").value += "&#1594;";
			return false;
		}else
		if (event.keyCode == 67){ // c
			document.getElementById("UrduWord").value += String.fromCharCode(1579);//'&#1579;';
			document.getElementById("UrduWordHidden").value += "&#1579;";
			return false;
		}
	}
	if (event.keyCode == 08){ // backspace
		//document.getElementById("UrduWord").value="";
		//document.getElementById("UrduWordHidden").value="";		
		//return false;
	}
	if (event.keyCode == 32){ // space
		//document.getElementById("UrduWord").value += '&#32;';
		//return false;
	}else
	if (event.keyCode == 65){ // a
		//event.keyCode = String.fromCharCode(1575);
		//String.fromCharCode(1575);
		document.getElementById("UrduWordHidden").value += "&#1575;";
		document.getElementById("UrduWord").value += String.fromCharCode(1575);		
		return false;
	}else
	if (event.keyCode == 66){ // b
		document.getElementById("UrduWord").value += String.fromCharCode(1576);//'&#1576;';
		document.getElementById("UrduWordHidden").value += "&#1576;";
		return false;
	}else
	if (event.keyCode == 67){ // c
		if (!event.ctrlKey){
			document.getElementById("UrduWord").value += String.fromCharCode(1670);//'&#1670;';
			document.getElementById("UrduWordHidden").value += "&#1670;";
			return false;
		}
	}else
	if (event.keyCode == 68){ // d
		document.getElementById("UrduWord").value += String.fromCharCode(1583);//'&#1583;';
		document.getElementById("UrduWordHidden").value += "&#1583;";
		return false;
	}else
	if (event.keyCode == 69){ // e
		document.getElementById("UrduWord").value += String.fromCharCode(1593);//'&#1593;';
		document.getElementById("UrduWordHidden").value += "&#1593;";
		return false;
	}else
	if (event.keyCode == 70){ // f
		document.getElementById("UrduWord").value += String.fromCharCode(1601);//'&#1601;';
		document.getElementById("UrduWordHidden").value += "&#1601;";
		return false;
	}else
	if (event.keyCode == 71){ // g
		document.getElementById("UrduWord").value += String.fromCharCode(1711);//'&#1711;';
		document.getElementById("UrduWordHidden").value += "&#1711;";
		return false;
	}else
	if (event.keyCode == 72){ // h
		document.getElementById("UrduWord").value += String.fromCharCode(1581);//'&#1581;';
		document.getElementById("UrduWordHidden").value += "&#1581;";
		return false;
	}else
	if (event.keyCode == 73){ // i
		document.getElementById("UrduWord").value += String.fromCharCode(1591);//'&#1591;';
		document.getElementById("UrduWordHidden").value += "&#1591;";
		return false;
	}else
	if (event.keyCode == 74){ // j
		document.getElementById("UrduWord").value += String.fromCharCode(1580);//'&#1580;';
		document.getElementById("UrduWordHidden").value += "&#1580;";
		return false;
	}else
	if (event.keyCode == 75){ // k
		document.getElementById("UrduWord").value += String.fromCharCode(1705);//'&#1705;';
		document.getElementById("UrduWordHidden").value += "&#1705;";
		return false;
	}else
	if (event.keyCode == 76){ // l
		document.getElementById("UrduWord").value += String.fromCharCode(1604);//'&#1604;';
		document.getElementById("UrduWordHidden").value += "&#1604;";
		return false;
	}else
	if (event.keyCode == 77){ // m
		document.getElementById("UrduWord").value += String.fromCharCode(1605);//'&#1605;';
		document.getElementById("UrduWordHidden").value += "&#1605;";
		return false;
	}else
	if (event.keyCode == 78){ // n
		document.getElementById("UrduWord").value += String.fromCharCode(1606);
		document.getElementById("UrduWordHidden").value += "&#1606;";
		return false;
	}else
	if (event.keyCode == 79){ // o
		document.getElementById("UrduWord").value += String.fromCharCode(1589);//'&#1589;';
		document.getElementById("UrduWordHidden").value += "&#1589;";
		return false;
	}else
	if (event.keyCode == 80){ // p
		document.getElementById("UrduWord").value += String.fromCharCode(1662);//'&#1662;';
		document.getElementById("UrduWordHidden").value += "&#1662;";
		return false;
	}else
	if (event.keyCode == 81){ // q
		document.getElementById("UrduWord").value += String.fromCharCode(1602);//'&#1602;';
		document.getElementById("UrduWordHidden").value += "&#1602;";
		return false;
	}else
	if (event.keyCode == 82){ // r
		document.getElementById("UrduWord").value += String.fromCharCode(1585);//'&#1585;';
		document.getElementById("UrduWordHidden").value += "&#1585;";
		return false;
	}else
	if (event.keyCode == 83){ // s
		if (!event.altKey){
			document.getElementById("UrduWord").value += String.fromCharCode(1587);
			document.getElementById("UrduWordHidden").value += "&#1587;";
			return false;
		}
	}else
	if (event.keyCode == 84){ // t
		document.getElementById("UrduWord").value += String.fromCharCode(1578);//'&#1578;';
		document.getElementById("UrduWordHidden").value += "&#1578;";
		return false;
	}else
	if (event.keyCode == 85){ // u
		document.getElementById("UrduWord").value += String.fromCharCode(1569);//'&#1569;';
		document.getElementById("UrduWordHidden").value += "&#1569;";
		return false;
	}else
	if (event.keyCode == 86){ // v
		if (!event.ctrlKey){
			document.getElementById("UrduWord").value += String.fromCharCode(1608);//'&#1608;';
			document.getElementById("UrduWordHidden").value += "&#1608;";
			return false;
		}
	}else
	if (event.keyCode == 87){ // w
		document.getElementById("UrduWord").value += String.fromCharCode(1574);//'&#1574;';
		document.getElementById("UrduWordHidden").value += "&#1574;";
		return false;
	}else
	if (event.keyCode == 88){ // x		
		if (!event.ctrlKey){
			document.getElementById("UrduWord").value += String.fromCharCode(1729);
			document.getElementById("UrduWordHidden").value += "&#1729;";
			return false;
		}
	}else
	if (event.keyCode == 89){ // y
		document.getElementById("UrduWord").value += String.fromCharCode(1610);//'&#1610;';
		document.getElementById("UrduWordHidden").value += "&#1610;";
		return false;
	}else
	if (event.keyCode == 90){ // z
		if (!event.ctrlKey){
			document.getElementById("UrduWord").value += String.fromCharCode(1586);//'&#1586;';
			document.getElementById("UrduWordHidden").value += "&#1586;";
			return false;
		}
	}
	//alert(document.getElementById("UrduWord").value);
}

function Reset(){
	$("#EnglishWord").val("");
	$("#UrduWord").val("");
	$("#DictionaryIsEditFlag").val(0); //reseting flag to 0 for insertion case by default
}