package crawl;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.Linker;
import main.Product;
import main.Products;
import main.SQL;

public class WebPage {
	private Anchor anchor;
	private String webPageHash;
	private int anchorParseStatus;
	private int emailParseStatus;

	private Document document;

	public Anchor getAnchor() {
		return anchor;
	}

	public String getWebPageHash() {
		return webPageHash;
	}

	public int getAnchorParseStatus() {
		return anchorParseStatus;
	}

	public int getEmailParseStatus() {
		return emailParseStatus;
	}

	public WebPage(Anchor anchor) throws Exception {
		this.anchor = anchor;
		this.webPageHash = Hasher.toSHA256(anchor.getAnchorHash());
		this.anchorParseStatus = 0;
		this.emailParseStatus = 0;
	}

	public WebPage(Anchor anchor, String webPageHash, int anchorParseStatus, int emailParseStatus) {
		this.anchor = anchor;
		this.webPageHash = webPageHash;
		this.anchorParseStatus = anchorParseStatus;
		this.emailParseStatus = emailParseStatus;
	}

	public void loadDocumentFromWeb(String website) throws Exception {

		SQL ob = new SQL();

		try {
			document = Jsoup.connect(anchor.getAnchorUrl()).get();
			Elements details = document.select("a");
			if ("amazon".equals(website)) {
				for (Element detail : details) {
					// Print all urls
					// System.out.println(detail.attr("abs:href").toString());
					String url = detail.attr("abs:href").toString();

					if (url.contains("/dp/")) {
						// System.out.println(url.substring(0,
						// url.indexOf("/dp/")+14));

						ob.insertIntoCrawledData(anchor.getDomain().getDomainUrl(), anchor.getAnchorUrl(),
								Common.getTimestamp(), url.substring(0, url.indexOf("/dp/") + 14), null,
								Hasher.toSHA256(url.substring(0, url.indexOf("/dp/") + 14)));

						/*
						 * Products prds = new Products(); Product prd = new
						 * Product(url);
						 * 
						 * if (!prds.checkProductExists(prd)) { Linker lin = new
						 * Linker(prd.getP_url()); int p_id = lin.findLinkage();
						 * if (p_id != 0) prd.updateUrlForProduct(p_id,
						 * prd.getP_url()); else { prds.insertIntoProducts(prd);
						 * }
						 */ } // else {
					// System.out.println(url);
					// insert non-product url
					// }

				}
			}

			else if ("flipkart".equals(website)) {
				for (Element detail : details) {
					String url = detail.attr("abs:href").toString();
					if (url.contains("/p/")) {
						//System.out.println(url.substring(0, url.indexOf("/p/") + 19));

						ob.insertIntoCrawledData(anchor.getDomain().getDomainUrl(), anchor.getAnchorUrl(),
								Common.getTimestamp(), url.substring(0, url.indexOf("/p/") + 19), null,
								Hasher.toSHA256(url.substring(0, url.indexOf("/p/") + 19)));
					}
				}
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
