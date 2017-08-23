package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Linker {

	private String p_url;

	public Linker(String p_url) {
		this.p_url = p_url;
	}

	public Linker() {
		System.out.println("constr linker");
	}

	public void updateProductsData() throws MalformedURLException {
		List<String[]> crawled_prd_urls = new ArrayList<String[]>();
		SQL sql = new SQL();
		crawled_prd_urls = sql.getCrawledPrdUrls();
		Crawler ob = new Crawler();
		Product prd;
		int prd_inserts = 0;
		int prd_link_adds = 0;
		for (String[] prd_url : crawled_prd_urls) {
			if (!sql.checkUrlExists(prd_url[0])) {

				URL p_url = new URL(prd_url[1]);
				String[] prd_data;
				try {
					prd_data = ob.getPrdDetailsFromURL(p_url);
				} catch (Exception e) {
					continue;
				}
				prd = new Product(prd_data[0], prd_data[1], prd_data[2], prd_data[3], prd_data[4]);

				int p_id = findLinkage(prd);
				// check product is new
				if (p_id == 0) {

					p_id = insertIntoProducts(prd);
					prd_inserts++;
				}
				sql.insertIntoProductsLinks(p_id, p_url.getHost(), Float.parseFloat(prd_data[6]), p_url.toString(),
						prd_url[0], Timestamp.valueOf((LocalDateTime.now())));
			}
		}
		if (prd_inserts == 0 && prd_link_adds == 0)
			System.out.println("No new URLs in crawled data");
		else
			System.out.println(prd_inserts + " new Products, " + prd_link_adds + " Product links added.");

	}

	public int findLinkage(Product prd) {

		Crawler crwl = new Crawler();

		if (1 == 2)
			return 1;
		else
			return 0;

	}

	public int insertIntoProducts(Product prd) {
		Products prds = new Products();
		return prds.insertIntoProducts(prd);
	}

	public void insertIntoProductsLinks() {

	}

}
