package crawl;

import java.util.ArrayList;
import java.util.List;

public class CrawlDriver {

	private List<String> amazon_categories = new ArrayList<String>();
	private List<String> flipkart_categories = new ArrayList<String>();

	public CrawlDriver() {
		// Earphones category
		// amazon_categories.add("/b/ref=sv_sv_ce_0_1?node=1388921031");
		// Hard disk category
		// amazon_categories.add("/b/ref=s9_acss_bw_en_CEPCEN_d_1_4_w?node=1375395031");
		//flipkart_categories.add("/mobiles");
		flipkart_categories.add("/headphones-store");

	}

	public void startCrawl() throws Exception {
		Domain domain = new Domain("https://www.amazon.in");
		// Start from a category page
		Anchor anchor;
		for (String category : amazon_categories) {
			anchor = new Anchor(domain, category);
			// Try starting from a product page
			// Anchor anchor = new Anchor(domain,
			// "/Sennheiser-CX-180-Street-II/dp/B00D75AB6I");
			WebPage webPage = new WebPage(anchor);
			// System.out.println(webPage.getWebPageHash());
			webPage.loadDocumentFromWeb("amazon");
		}

		domain = new Domain("https://www.flipkart.com");
		for (String category : flipkart_categories) {
			anchor = new Anchor(domain, category);

			WebPage webPage = new WebPage(anchor);
			// System.out.println(webPage.getWebPageHash());
			webPage.loadDocumentFromWeb("flipkart");
		}
	}

}
