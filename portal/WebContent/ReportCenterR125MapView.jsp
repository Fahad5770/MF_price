
<style>
   #map-canvas {
     height: 100%;
     margin: 0px;
     padding: 0px
   }
</style>
    
<script>
var OrderLat = document.getElementById("OrderLocation_"+OrderID).value.split(',')[0];
var OrderLng = document.getElementById("OrderLocation_"+OrderID).value.split(',')[1];

var OutletLat = document.getElementById("OutletLocation_"+OrderID).value.split(',')[0];
var OutletLng = document.getElementById("OutletLocation_"+OrderID).value.split(',')[1];

//document.getElementById("tmp").innerHTML = "("+OrderLat+","+OrderLng+") - ("+OutletLat+","+OutletLng+")";

	var map;
	
	function initialize() {
		
	  var mapOptions = {
	    zoom: 13,
	    center: new google.maps.LatLng(OrderLat,OrderLng)
	  };
	  map = new google.maps.Map(document.getElementById('map-canvas'),
	      mapOptions);
	  
	  var markers = new Array();
	  
	  markers[0] = new google.maps.Marker({
	      position: new google.maps.LatLng(OutletLat , OutletLng),
	      map: map,
	      title: ''+OutletInfo,
	      icon: 'images/markers/letter_o.png'
	  });
	  
	  markers[1] = new google.maps.Marker({
	      position: new google.maps.LatLng(OrderLat , OrderLng),
	      map: map,
	      title: ''+OrderBookerInfo,
	      icon: 'images/markers/truck.png'
	  });
	  
	  
	}
	
	//google.maps.event.addDomListener(window, 'load', initialize);
	initialize();
</script>
<div id="tmp"></div>
<div id="map-canvas"></div>
