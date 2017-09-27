package crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.SQL;

public class DynamicPageCrawler {

	// private Anchor anchor;
	// private String webPageHash;
	private String category;

	private int CRAWL_NO_OF_PAGES = 1;
	private boolean ALT_SKU_CRAWL = true;

	public DynamicPageCrawler(String category) throws Exception {
		// this.anchor = anchor;
		// this.webPageHash = Hasher.toSHA256(anchor.getAnchorHash());
		this.category = category;
	}

	public void scrapeProductLinks(String website) throws Exception {
		SQL ob = new SQL();
		int page = 1;

		if ("paytmmall".equals(website)) {
			ArrayList<String> prd_urls = new ArrayList<String>();
			String params = "?channel=web&child_site_id=6&site_id=2&version=2&items_per_page=40&resolution=960x720&quality=high&curated=1&_type=1&page_count";
			String baseUrl = "https://catalog.paytm.com/" + category + params;

			do {
				System.out.println("Page : " + page);
				JSONArray jsa = getProductsJSON(new URL(baseUrl + page)).getJSONArray("grid_layout");
				Iterator it = jsa.iterator();

				while (it.hasNext()) {
					JSONObject prd = (JSONObject) it.next();
					String prd_str = prd.get("seourl").toString();
					String prd_url = "https://paytmmall.com" + prd_str.substring(30, prd_str.indexOf('?')) + "-pdp";

					ob.insertIntoCrawledLinks("https://paytmmall.com", "https://paytmmall.com/" + category,
							Common.getTimestamp(), prd_url, null, Hasher.toSHA256(prd_url));
				}
				page++;
			} while (page <= CRAWL_NO_OF_PAGES);

		}
	}

	public JSONObject getProductsJSON(URL ajaxurl) throws IOException {

		HttpURLConnection con = (HttpURLConnection) ajaxurl.openConnection();
		con.setRequestMethod("GET");

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			content.append(inputLine);
		}
		br.close();

		JSONObject jso = new JSONObject(content.toString());
		// System.out.println(jso.length());
		return jso;
	}

}
