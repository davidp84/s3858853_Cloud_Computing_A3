/* Jinu, “Get geo location coordinates of User,” ChillyFacts, 
* 09-Dec-2017. [Online]. Available: 
* https://chillyfacts.com/get-geo-location-coordinates-user/. 
* [Accessed: 07-May-2022]. 
*
* S. Patel, “How to pass geolocation values to variable and to SQL query in JSP,” 
* Stack Overflow, 18-Jun-2016. [Online]. Available: 
* https://stackoverflow.com/questions/37897743/how-to-pass-geolocation-values-to-variable-and-to-sql-query-in-jsp.
* [Accessed: 07-May-2022]. 
*/

// gets the coordinates of the user (if permission granted) and adds it to the hidden form fields.
//function getPosition() {
//	if (navigator.geolocation) {
//		navigator.geolocation.getCurrentPosition(function(position) {
//			document.getElementById("latitude").value = position.coords.latitude;
//			document.getElementById("longitude").value = position.coords.longitude;
////			document.getElementById("event").disabled = false;
//		}, function errorCallback(error) {
//			alert(`ERROR(${error.code}): ${error.message}`);
//		});
//	}
//}

// Method abandoned as geolocation requires HTTPS to work.


/* geocodezip and Jenson M John, “Google maps geocoding a string,” Stack Overflow, 28-Jan-2014. 
 * [Online]. Available: 
 * https://stackoverflow.com/questions/21411358/google-maps-geocoding-a-string. 
 * [Accessed: 21-May-2022]. 
 */

function getPosition() {
	var geocoder = new google.maps.Geocoder;

	geocoder.geocode({ address: document.getElementById("address").value }, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			map.setCenter(results[0].geometry.location);//center the map over the result
			//place a marker at the location
			var marker = new google.maps.Marker({
				map: map,
				position: results[0].geometry.location
			});

			document.getElementById("latitude").value = results[0].geometry.location.lat();
			document.getElementById("longitude").value = results[0].geometry.location.lng();
		} else {
			alert('Geocode was not successful for the following reason: ' + status);
		}

	});
}

var map;
// centers map on Sydney
function initMap() {
	map = new google.maps.Map(document.getElementById("map"), {
		center: { lat: -33.877, lng: 151.034 },
		zoom: 12,
	});
}


/* 
 * R. Hartmann, “How to get JSON from URL in JavaScript?,” 
 * Stack Overflow, 24-Aug-2017. [Online]. Available: 
 * https://stackoverflow.com/questions/12460378/how-to-get-json-from-url-in-javascript.
 * [Accessed: 14-May-2022]. 
 */

// api url
const api_url = "https://p37w1u2mtd.execute-api.us-east-1.amazonaws.com/prod/s3858853CCA3";

// Retireves data from Amazon Gateway API Call
var getJSON = function(url, callback) {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', url, true);
	xhr.responseType = 'json';
	xhr.onload = function() {
		var status = xhr.status;
		if (status === 200) {
			callback(null, xhr.response);
		} else {
			callback(status, xhr.response);
		}
	};
	xhr.send();
};

// Data used for Google Graph API 
var flat;
var batt;
var unknown;
var mech;
var petrol;

// Parses the data received from the API call and creates markes for the map
// and also increments the graph variables based on the event type.
getJSON(api_url, function(err, data) {
	if (err !== null) {
		alert('Something went wrong: ' + err);
	} else {
		var obj = JSON.parse(data);

		let x = 0

		flat = 0;
		batt = 0;
		unknown = 0;
		mech = 0;
		petrol = 0;

		while (typeof obj[x] !== "undefined") {
			var tempImage = getImage(obj[x].event);

			var marker = new google.maps.Marker({
				position: { lat: parseFloat(obj[x].latitude), lng: parseFloat(obj[x].longitude) },
				map: map,
				title: obj[x].event,
				icon: tempImage,
				zIndex: 10
			});

			switch (obj[x].event) {
				case "FLAT_BATTERY":
					batt++;
					break;
				case "NO_PETROL":
					petrol++;
					break;
				case "MECHANICAL":
					mech++;
					break;
				case "FLAT_TYRE":
					flat++;
					break;
				case "UNKNOWN":
					unknown++;
					break;
				default:
					break;
			}

			x++;
			drawChart();
		}

	}
});

// Creates an image variable based off event type passed.
function getImage(event) {
	var image = {
		url: "https://s3858853-a3.s3.amazonaws.com/event-images/" + event + ".png",

		// This marker is 20 pixels wide by 32 pixels high.
		//		scaledSize: new google.maps.Size(200, 320),
		scaledSize: new google.maps.Size(20, 32),
		// The origin for this image is (0, 0).
		origin: new google.maps.Point(0, 0),
		// The anchor for this image is the base of the flagpole at (0, 32).
		anchor: new google.maps.Point(10, 32),
	};

	return image;
}

// draws the chart using the variables above which are updated with each API call.
function drawChart() {

	var graphData = google.visualization.arrayToDataTable([
		['Breakdowns', 'Amount'],
		['Flat Tyre', flat],
		['No Petrol', petrol],
		['Flat Battery', batt],
		['Mechanical Issue', mech],
		['Unknown', unknown]
	]);

	var options = {
		title: 'Current Breakdown Stats'
	};

	var chart = new google.visualization.PieChart(document.getElementById('piechart'));

	chart.draw(graphData, options);
}

window.initMap = initMap;

