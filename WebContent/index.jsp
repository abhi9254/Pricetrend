<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PriceTrend</title>
<script>
	function getDetails() {
		document.getElementById("added_message").innerHTML = "";
		document.getElementById("product_details_amazon").innerHTML = "";
		var url = document.getElementById("product_url").value;
		var x = new XMLHttpRequest()
		x.open("GET", "ajax.jsp?url=" + url, true)
		x.send(null)
		x.onreadystatechange = function() {
			if (x.readyState == 4) {
				document.getElementById("product_details_amazon").innerHTML = x.responseText;
			}
		}
	}

	function addToMyProducts() {
		var url = document.getElementById("product_url").value;
		//var price = document.getElementById("product_price").value;
		var y = new XMLHttpRequest();
		y.open("GET", "ajax.jsp?url=" + url + "&price=" + "0", true);
		y.send(null);

		y.onreadystatechange = function() {
			if (y.readyState == 4) {
				document.getElementById("added_message").innerHTML = "";
				document.getElementById("added_message").innerHTML = y.responseText;
			}
		}

	}

	function start_updater() {
		document.getElementById("added_message").innerHTML = "";
		var z = new XMLHttpRequest();
		z.open("GET", "ajax.jsp?updater=start", true);
		z.send(null);

		z.onreadystatechange = function() {
			if (z.readyState == 4) {
				document.getElementById("added_message").innerHTML = z.responseText;
			}
		}
	}

	function start_crawler() {
		document.getElementById("added_message").innerHTML = "Started scraper";
		var z = new XMLHttpRequest();
		z.open("GET", "ajax.jsp?crawler=start", true);
		z.send(null);

		z.onreadystatechange = function() {
			if (z.readyState == 4) {
				document.getElementById("added_message").innerHTML = z.responseText;
			}
		}
	}

	function start_linker() {
		document.getElementById("added_message").innerHTML = "Started linker";
		var z = new XMLHttpRequest();
		z.open("GET", "ajax.jsp?linker=start", true);
		z.send(null);

		z.onreadystatechange = function() {
			if (z.readyState == 4) {
				document.getElementById("added_message").innerHTML = z.responseText;
			}
		}
	}
</script>
</head>
<body>

	<li><a href="">Home</a> <a href="wishlist.jsp">Wishlist</a> <a
		href="profile.jsp">Account</a></li>
	<li><a onclick="start_updater()">Update</a></li>
	<li><a onclick="start_crawler()">Crawl</a></li>
	<li><a onclick="start_linker()">Link</a></li>
	<br>

	<div>
		<input type="text" id="product_url" placeholder="Product url"
			style="width: 800px; height: 20px" />
		<div>Drop Item here</div>
		<br>
		<button onclick="addToMyProducts()">Add</button>
		<label id="added_message"></label>
		<button onclick="getDetails()">Check now</button>
		<button>Cancel</button>
	</div>
	<br>
	<br>
	<div>
		<div>Price graph</div>
		<br>
		<div>
			Current prices
			<div>
				<h5>Amazon</h5>
				<label id="product_details_amazon"></label>
				<button>Buy</button>
			</div>
			<div>
				<h5>Flipkart</h5>
				<label id="product_details_flipkart"></label>
				<button>Buy</button>
			</div>
		</div>
	</div>
</body>
</html>