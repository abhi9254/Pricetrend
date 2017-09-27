package crawl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.SQL;

public class WebPage {
	private Anchor anchor;
	private String webPageHash;
	private int anchorParseStatus;
	private int emailParseStatus;
	private int CRAWL_NO_OF_PAGES = 1;
	private boolean ALT_SKU_CRAWL = true;

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

		int page = 1, prd_crawls = 0, alt_sku_crawls = 0;

		SQL ob = new SQL();
		BlockingQueue<String> nextPages = new ArrayBlockingQueue<>(5);
		try {
			String web_url = anchor.getAnchorUrl();
			document = Jsoup.connect(web_url).get();
			Elements details = document.select("a");
			List<String> alt_sku_details;

			if ("amazon".equals(website)) {
				do {
					if (page != 1) {
						System.out.println("Page : " + page);
						details = Jsoup.connect(nextPages.take()).get().select("a");
					}
					for (Element detail : details) {
						String url = detail.attr("abs:href").toString();

						if (url.contains("/dp/")) {
							prd_crawls++;
							ob.insertIntoCrawledLinks(anchor.getDomain().getDomainUrl(), anchor.getAnchorUrl(),
									Common.getTimestamp(), url.substring(0, url.indexOf("/dp/") + 14), null,
									Hasher.toSHA256(url.substring(0, url.indexOf("/dp/") + 14)));
							if (ALT_SKU_CRAWL) {
								Elements alt_skus = Jsoup.connect(url).get().select("li.swatchAvailable");
								if (!alt_skus.isEmpty()) {
									for (Element alt_sku : alt_skus) {
										url = "https://www.amazon.in" + alt_sku.attr("data-dp-url").toString();
										if (url.contains("/dp/")) {
											alt_sku_crawls++;
											ob.insertIntoCrawledLinks(anchor.getDomain().getDomainUrl(),
													anchor.getAnchorUrl(), Common.getTimestamp(),
													url.substring(0, url.indexOf("/dp/") + 14), null,
													Hasher.toSHA256(url.substring(0, url.indexOf("/dp/") + 14)));

										}
									}
								}

							}
						} else if (detail.hasClass("pagnNext")) {
							if (!nextPages.contains(url)) {
								nextPages.offer(url);
								// System.out.println(url);
							}
						}
					}
					page++;
				} while (!nextPages.isEmpty() && page <= CRAWL_NO_OF_PAGES);
				System.out.println(prd_crawls + " products having " + alt_sku_crawls + " alt skus crawled.");
			}

			else if ("flipkart".equals(website)) {
				do {
					if (page != 1) {
						System.out.println("Page : " + page);
						details = Jsoup.connect(nextPages.take()).get().select("a");
					}
					for (Element detail : details) {
						String url = detail.attr("abs:href").toString();
						if (url.contains("/p/")) {
							prd_crawls++;
							ob.insertIntoCrawledLinks(anchor.getDomain().getDomainUrl(), anchor.getAnchorUrl(),
									Common.getTimestamp(), url.substring(0, url.indexOf("/p/") + 19), null,
									Hasher.toSHA256(url.substring(0, url.indexOf("/p/") + 19)));

							if (ALT_SKU_CRAWL) {
								Elements alt_skus = Jsoup.connect(url).get().select("a._2aRfTu");
								if (!alt_skus.isEmpty()) {
									for (Element alt_sku : alt_skus) {
										url = alt_sku.attr("abs:href").toString();
										if (url.contains("/p/")) {
											alt_sku_crawls++;
											ob.insertIntoCrawledLinks(anchor.getDomain().getDomainUrl(),
													anchor.getAnchorUrl(), Common.getTimestamp(),
													url.substring(0, url.indexOf("/p/") + 19), null,
													Hasher.toSHA256(url.substring(0, url.indexOf("/p/") + 19)));
										}
									}
								}

							}
						} else if (detail.hasClass("_33m_Yg") && url.contains("?page=" + (page + 1) + "&")) {
							if (!nextPages.contains(url)) {
								nextPages.offer(url);
								// System.out.println(url);
							}
						}
					}

					page++;

					if (page > CRAWL_NO_OF_PAGES)
						System.out.println("Finished crawling on Flipkart");

				} while (!nextPages.isEmpty() && page <= CRAWL_NO_OF_PAGES);
				System.out.println(prd_crawls + " products having " + alt_sku_crawls + " alt skus crawled.");
			}

		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
