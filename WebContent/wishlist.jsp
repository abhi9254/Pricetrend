<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Wishlist</title>

<script type="text/javascript">
	function refreshWishlist() {
		var active_user = '112';
		//Kumar
		document.getElementById("wishlist").innerHTML = "";
		var z = new XMLHttpRequest();
		z.open("GET", "ajax.jsp?wishlist=1&user=" + active_user, true);
		z.send(null);

		z.onreadystatechange = function() {
			if (z.readyState == 4) {
				document.getElementById("wishlist").innerHTML = z.responseText;
			}
		}
	}
</script>
</head>
<body onload="refreshWishlist()">
	<li><a href="index.jsp">Home</a> <a href="">Wishlist</a> <a
		href="profile.jsp">Account</a></li>
	<br>
	<label style="float: right">User : Kumar</label>
	<br>
	<br>
	<table>
		<thead>
			<th>Product name</th>
			<th>Best price</th>
			<th></th>
		</thead>
		<tbody id="wishlist"></tbody>
	</table>
</body>
</html>