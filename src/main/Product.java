package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import crawl.Hasher;

public class Product {

	private int p_id;
	private String p_name;
	private String p_desc;
	private String p_section;
	private String p_category;
	private String p_sub_category;
	private String p_url;
	private String hashed_p_url;
	private ArrayList<String> p_url_list;

	public Product(int p_id, String p_name, String p_desc) {
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_desc = p_desc;
	}

	public Product(String p_name, String p_desc, String p_section, String p_category, String p_sub_category) {
		this.p_name = p_name;
		this.p_desc = p_desc;
		this.p_section = p_section;
		this.p_category = p_category;
		this.p_sub_category = p_sub_category;
	}

	public Product(int p_id) {
		this.p_id = p_id;
	}

	public Product(String p_url) throws Exception {
		this.p_url = p_url;
		this.hashed_p_url = Hasher.toSHA256(p_url);
	}

	public String getP_desc() {
		return p_desc;
	}

	public int getP_id() {
		return p_id;
	}

	public ArrayList<String> getP_url_list() {
		return p_url_list;
	}

	public String getP_name() {
		return p_name;
	}

	public String getHashed_p_url() {
		return hashed_p_url;
	}

	public String getP_url() {
		return p_url;
	}

	public String getP_section() {
		return p_section;
	}

	public String getP_category() {
		return p_category;
	}

	public String getP_sub_category() {
		return p_sub_category;
	}

	public ArrayList<URL> getProductURLs() {
		SQL sql = new SQL();
		p_url_list = new ArrayList<String>(sql.getURLsForProduct(p_id));
		ArrayList<URL> p_urls = new ArrayList<URL>();
		for (String url : p_url_list)
			try {
				// Convert string to URL object
				p_urls.add(new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		return p_urls;
	}

	public void updateUrlForProduct(int p_id, String p_url) {

	}
}
