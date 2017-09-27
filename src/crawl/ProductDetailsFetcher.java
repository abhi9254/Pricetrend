package crawl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import main.Product;
import main.Products;
import main.SQL;

public class ProductDetailsFetcher {

	public ProductDetailsFetcher() {
		System.out.println("Fetching product details from crawled links");
	}

	public void updateProductsData() throws MalformedURLException {
		List<String[]> crawled_prd_urls = new ArrayList<String[]>();
		SQL sql = new SQL();
		crawled_prd_urls = sql.getCrawledPrdUrls();
		Crawler ob = new Crawler();
		Product prd;
		int prd_inserts = 0;

		for (String[] prd_url : crawled_prd_urls) {
			if (!sql.checkUrlExists(prd_url[0])) {

				URL p_url = new URL(prd_url[1]);
				HashMap<String, String> prd_data;
				try {
					prd_data = ob.getPrdDetailsFromURL(p_url);
				} catch (Exception e) {
					continue;
				}
				prd = new Product(prd_data.get("product_title"), prd_data.get("product_desc"),
						prd_data.get("product_hierarchy_0"), prd_data.get("product_hierarchy_1"),
						prd_data.get("product_hierarchy_2"), prd_data.get("product_hierarchy_3"),
						prd_data.get("product_hierarchy_4"));

				prd.setCrawl_timestamp(Common.getTimestamp());

				sql.insertIntoProductsLinks(prd.getCrawl_timestamp().getTime(), p_url.getHost(),
						Float.parseFloat(prd_data.get("product_price")), p_url.toString(), prd_url[0],
						prd.getCrawl_timestamp());

				StringBuilder p_specs = new StringBuilder();
				prd_data.remove("product_title");
				prd_data.remove("product_desc");
				prd_data.remove("product_hierarchy_0");
				prd_data.remove("product_hierarchy_1");
				prd_data.remove("product_hierarchy_2");
				prd_data.remove("product_hierarchy_3");
				prd_data.remove("product_avl");
				prd_data.remove("product_price");
				prd_data.remove("\u00a0");

				int specs_cnt = prd_data.size();
				int added = 0;

				for (Entry<String, String> entry : prd_data.entrySet()) {
					p_specs.append("'" + entry.getKey().replaceAll("'", "") + "','"
							+ entry.getValue().replaceAll("'", "") + "'");
					if (++added != specs_cnt)
						p_specs.append(",");

				}
				// System.out.println(p_specs.toString());
				prd.setP_specs(p_specs.toString());
				insertIntoCrawledProducts(prd);
				prd_inserts++;
			}
		}
		if (prd_inserts == 0)
			System.out.println("No new URLs in crawled data");
		else
			System.out.println(prd_inserts + " new products fetched.");

	}

	public int insertIntoCrawledProducts(Product prd) {
		Products prds = new Products();
		return prds.insertIntoCrawledProducts(prd);
	}

	public void insertIntoProductsLinks() {

	}

}
