<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="main.SQL,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Products</title>
<style>
table {
	font-size: 12px;
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
	font-family: arial, sans-serif;
	font-family: arial, sans-serif;
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

tr:nth-child(even) {
	background-color: #dddddd;
}
</style>
</head>
<body>
	<li><a href="index.jsp">Home</a> <a href="wishlist.jsp">Wishlist</a>
		<a href="#">Products</a> <a href="profile.jsp">Account</a></li>
	<br>
	<table>
		<thead>
			<th>Product_id</th>
			<th>Product name</th>
			<th>Model/Color</th>
			<th>Best price</th>
			<th>Link 1</th>
			<th>Link 2</th>
		</thead>

		<%
			SQL sql = new SQL();
			ArrayList<Long> prds = sql.getListOfProducts();
			//ArrayList<String> prd_names= sql.getProductNames(prds);
		%>

		<tbody>
			<%
				for (long prd_id : prds) {
					String[] prd_dtls = sql.getProductDetails(prd_id);
					String[] prd_link = sql.getProductLink(prd_id);
					Long[] prd_matches = sql.getProductMatches(prd_id);
					String[] match_1 = sql.getProductLink(prd_matches[0]);
					String[] match_1_dtls = sql.getProductDetails(prd_matches[0]);
					String[] match_2 = sql.getProductLink(prd_matches[1]);
					String[] match_2_dtls = sql.getProductDetails(prd_matches[1]);
			%>
			<tr>
				<td style="width: 10%"><%=prd_id%></td>
				<td style="width: 30%"><%=prd_dtls[0]%></td>
				<td style="width: 20%"><%=prd_dtls[1]%></td>
				<td style="width: 10%"><a href='<%=prd_link[2]%>'
					target='_blank'><%=prd_link[1]%></td>
				<td style="width: 15%"><a
					href='<%=(match_1[2] != null ? match_1[2] : "#")%>' target='_blank'><%=(match_1_dtls[1] != null ? match_1_dtls[1] : "")%></td>
				<td style="width: 15%"><a
					href='<%=(match_2[2] != null ? match_2[2] : "#")%>' target='_blank'><%=(match_2_dtls[1] != null ? match_2_dtls[1] : "")%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
</body>
</html>