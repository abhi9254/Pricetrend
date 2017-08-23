<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page
	import="main.Crawler,main.SQL,main.PriceUpdater,main.User,main.Products,main.Linker,crawl.CrawlDriver"%>
<%
	PrintWriter outRes = response.getWriter();

	if (request.getParameter("url") != null && request.getParameter("price") == null) {
		String url = request.getParameter("url");
		Crawler ob = new Crawler();
		String details = ob.getDetailsFlipkart(url);
		outRes.print(details);
	}

	if (request.getParameter("url") != null && request.getParameter("price") != null) {
		String p_url = request.getParameter("url");
		Integer p_id = 0;
		String p_name = "";
		String p_desc = "";
		String p_website = "Amazon.in";
		String p_price = request.getParameter("price");
		SQL ob = new SQL();
		ob.insertIntoMyProducts(p_id, p_name, p_desc, p_url, p_website, p_price);
		outRes.print("Successfully added");
	}

	if ("start".equals(request.getParameter("updater"))) {

		PriceUpdater ob = new PriceUpdater();
		ob.updatePrices();
		outRes.print("Update scheduler started");
	}

	if ("start".equals(request.getParameter("crawler"))) {

		CrawlDriver ob = new CrawlDriver();
		ob.startCrawl();
		outRes.print("Scraping finished");
	}

	if ("start".equals(request.getParameter("linker"))) {

		Linker ob = new Linker();
		ob.updateProductsData();
		outRes.print("Linking finished");
	}

	if ("1".equals(request.getParameter("wishlist")) && request.getParameter("user") != null) {

		User ob = new User(Integer.parseInt(request.getParameter("user")));
		ArrayList<Integer> user_prods = ob.getMyproducts();
		Products prds = new Products(user_prods);
		ArrayList<String> user_p_names = prds.getP_names();
		StringBuilder s = new StringBuilder();
		for (String p_name : user_p_names) {
			s.append("<tr><td>" + p_name + "</td><td>500.0</td><td><a href=''>Buy</></td>");
		}

		outRes.print(s.toString());
	}
%>