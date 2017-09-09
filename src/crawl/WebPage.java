package crawl;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
	private int CRAWL_DEPTH = 1;

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

		int page = 1;

		SQL ob = new SQL();
		BlockingQueue<String> nextPages = new ArrayBlockingQueue<>(5);
		try {
			String web_url = anchor.getAnchorUrl();
			document = Jsoup.connect(web_url).get();
			Elements details = document.select("a");

			if ("amazon".equals(website)) {
				do {
					if (page != 1) {
						System.out.println("Page : " + page);
						details = Jsoup.connect(nextPages.take()).get().select("a");
					}
					for (Element detail : details) {
						String url = detail.attr("abs:href").toString();

						if (url.contains("/dp/")) {
							ob.insertIntoCrawledData(anchor.getDomain().getDomainUrl(), anchor.getAnchorUrl(),
									Common.getTimestamp(), url.substring(0, url.indexOf("/dp/") + 14), null,
									Hasher.toSHA256(url.substring(0, url.indexOf("/dp/") + 14)));

						} else if (detail.hasClass("pagnNext")) {
							if (!nextPages.contains(url)) {
								nextPages.offer(url);
								// System.out.println(url);
							}
						}
					}
					page++;
				} while (!nextPages.isEmpty() && page <= CRAWL_DEPTH);
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
							ob.insertIntoCrawledData(anchor.getDomain().getDomainUrl(), anchor.getAnchorUrl(),
									Common.getTimestamp(), url.substring(0, url.indexOf("/p/") + 19), null,
									Hasher.toSHA256(url.substring(0, url.indexOf("/p/") + 19)));

						} else if (detail.hasClass("_33m_Yg") && url.contains("?page=" + (page + 1) + "&")) {
							if (!nextPages.contains(url)) {
								nextPages.offer(url);
								// System.out.println(url);
							}
						}
					}

					page++;
				} while (!nextPages.isEmpty() && page <= CRAWL_DEPTH);
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
