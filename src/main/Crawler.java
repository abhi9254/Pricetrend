package main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public Float getPriceFromURL(URL url) {
		// System.out.println(url.getHost());
		Float product_price = Float.MAX_VALUE;
		if ("www.amazon.in".equals(url.getHost())) {

			try {
				Document doc = Jsoup.connect(url.toString()).get();
				Elements details = doc
						.select("span#priceblock_ourprice,span#priceblock_saleprice,span.olp-padding-right > span");

				if (details.isEmpty()) {
					product_price = null;
				} else {
					for (Element detail : details) {
						if (product_price > Float.parseFloat(detail.ownText().replaceAll("[^0-9.]", "")))
							product_price = Float.parseFloat(detail.ownText().replaceAll("[^0-9.]", ""));
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("www.flipkart.com".equals(url.getHost())) {
			try {
				Document doc = Jsoup.connect(url.toString()).get();
				Elements details = doc.select("div._37U4_g");
				for (Element detail : details) {
					if (product_price > Float.parseFloat(detail.ownText().replaceAll("[^0-9.]", "")))
						product_price = Float.parseFloat(detail.ownText().replaceAll("[^0-9.]", ""));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("paytmmall.com".equals(url.getHost())) {
			try {
				Document doc = Jsoup.connect(url.toString()).get();
				Elements details = doc.select("span._1d5g");

				for (Element detail : details) {
					if (product_price > Float.parseFloat(detail.ownText().replaceAll("[^0-9.]", "")))
						product_price = Float.parseFloat(detail.ownText().replaceAll("[^0-9.]", ""));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return product_price;
	}

	public HashMap<String, String> getPrdDetailsFromURL(URL url) throws IOException {

		HashMap<String, String> prd_dtls = new HashMap<String, String>();
		Float product_price = Float.MAX_VALUE;
		if ("www.amazon.in".equals(url.getHost())) {

			Document doc = Jsoup.connect(url.toString()).get();
			Elements price_els = doc
					.select("span#priceblock_ourprice,span#priceblock_saleprice,span.olp-padding-right > span");
			if (price_els.isEmpty()) {
				product_price = null;
			} else {
				for (Element price_el : price_els) {
					if (product_price > Float.parseFloat(price_el.ownText().replaceAll("[^0-9.]", "")))
						product_price = Float.parseFloat(price_el.ownText().replaceAll("[^0-9.]", ""));
				}
			}

			Elements title = doc.select("span#productTitle");
			String product_title = title.first().ownText().replaceAll("'", "");

			Elements desc = doc.select("div#productDescription > p");
			String product_desc = title.first().ownText().replaceAll("'", "");

			Elements availability = doc.select("div#availability > span");
			String product_avl = availability.first().ownText();

			Element category = doc.select("div#wayfinding-breadcrumbs_feature_div > ul").first();
			Elements hierarchy = category.getElementsByTag("a");
			String[] product_hierarchy = new String[4];
			for (int i = 0; i < hierarchy.size() && i < product_hierarchy.length; i++) {
				product_hierarchy[i] = hierarchy.get(i).ownText();
			}

			Elements specs = doc.select("div.pdTab").first().getElementsByTag("td");
			for (int i = 0; i < specs.size(); i = i + 2) {
				// System.out.println(specs.get(i).ownText() + " " + specs.get(i
				// + 1).ownText());
				prd_dtls.put(specs.get(i).ownText(), specs.get(i + 1).ownText());
			}
			prd_dtls.put("product_title", product_title);
			prd_dtls.put("product_desc", product_desc);
			prd_dtls.put("product_hierarchy_0", product_hierarchy[0]);
			prd_dtls.put("product_hierarchy_1", product_hierarchy[1]);
			prd_dtls.put("product_hierarchy_2", product_hierarchy[2]);
			prd_dtls.put("product_hierarchy_3", product_hierarchy[3]);
			prd_dtls.put("product_avl", product_avl);
			prd_dtls.put("product_price", product_price.toString());

		}

		if ("www.flipkart.com".equals(url.getHost())) {

			Document doc = Jsoup.connect(url.toString()).get();
			Elements price_els = doc.select("div._37U4_g");

			if (price_els.isEmpty()) {
				product_price = null;
			} else {
				for (Element price_el : price_els) {
					if (product_price > Float.parseFloat(price_el.ownText().replaceAll("[^0-9.]", "")))
						product_price = Float.parseFloat(price_el.ownText().replaceAll("[^0-9.]", ""));
				}
			}
			Elements title = doc.select("h1._3eAQiD");
			String product_title = title.first().ownText().replaceAll("'", "");

			// Elements desc = doc.select("div#productDescription > p");
			// String product_desc = title.first().ownText();

			Element availability = doc.select("div._3xgqrA").first();
			String product_avl;
			if (availability != null)
				product_avl = availability.ownText();
			else
				product_avl = "Available";

			Elements hierarchy = doc.select("div._1HEvv0 > a");
			String[] product_hierarchy = new String[4];
			for (int i = 0; i < hierarchy.size() && i < product_hierarchy.length; i++) {
				product_hierarchy[i] = hierarchy.get(i).ownText();
			}

			Elements specs_box = doc.select("div._2Kp3n6 > ul");
			for (int i = 0; i < specs_box.size(); i++) {
				Elements specs = specs_box.get(i).select("div.vmXPri,ul._3dG3ix > li");
				for (int j = 0; j < specs.size(); j = j + 2) {
					//System.out.println(specs.get(j).ownText() + " : " + specs.get(j + 1).ownText());
					prd_dtls.put(specs.get(j).ownText(), specs.get(j + 1).ownText());
				}
			}
			prd_dtls.put("product_title", product_title);
			prd_dtls.put("product_desc", product_title);
			prd_dtls.put("product_hierarchy_0", product_hierarchy[0]);
			prd_dtls.put("product_hierarchy_1", product_hierarchy[1]);
			prd_dtls.put("product_hierarchy_2", product_hierarchy[2]);
			prd_dtls.put("product_hierarchy_3", product_hierarchy[3]);
			prd_dtls.put("product_avl", product_avl);
			prd_dtls.put("product_price", product_price.toString());

		}

		return prd_dtls;
	}

	public String getDetailsAmazon(String url) {
		StringBuilder product_details = new StringBuilder();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements details = doc.select("span#priceblock_ourprice,span#priceblock_saleprice");

			for (Element detail : details) {
				product_details.append(detail.ownText() + " ");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product_details.toString();
	}

	public String getDetailsFlipkart(String url) {
		StringBuilder product_details = new StringBuilder();
		try {
			Document doc = Jsoup.connect(url).get();

			Elements details = doc.select("h1._3eAQiD,div._37U4_g");
			for (Element detail : details) {
				product_details.append(detail.ownText() + " ");
			}

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product_details.toString();
	}

}
