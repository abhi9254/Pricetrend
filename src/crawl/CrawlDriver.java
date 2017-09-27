package crawl;

import java.util.ArrayList;
import java.util.List;

public class CrawlDriver {

	private List<String> amazon_categories = new ArrayList<String>();
	private List<String> flipkart_categories = new ArrayList<String>();
	private List<String> paytmmall_categories = new ArrayList<String>();

	public CrawlDriver() {
		// Earphones category
		 amazon_categories.add("/b/ref=sv_sv_ce_0_1?node=1388921031");
		// flipkart_categories.add("/audio-video/headphones/pr?sid=0pm,fcn");
		//paytmmall_categories.add("/mobile-headphones-headsets-glpid-78654");

		// Hard disk category
		// amazon_categories.add("/b/ref=s9_acss_bw_en_CEPCEN_d_1_4_w?node=1375395031");
		// flipkart_categories.add("/computers/storage/external-hard-disks/pr?sid=6bo,jdy,nl6");
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

		// domain = new Domain("https://paytmmall.com");
		for (String category : paytmmall_categories) {
			// anchor = new Anchor(domain, category);

			DynamicPageCrawler dpc = new DynamicPageCrawler(category);
			dpc.scrapeProductLinks("paytmmall");
		}
		System.out.println("Finished scraping.");
	}

}
