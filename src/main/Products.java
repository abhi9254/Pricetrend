package main;


import java.util.ArrayList;

public class Products {

	private ArrayList<Long> p_list;
	private ArrayList<String> p_names;
	private ArrayList<Float> p_best_prices;

	public Products() {

	}

	public Products(ArrayList<Long> p_list) {
		this.p_list = p_list;
		SQL sql = new SQL();
		this.p_names = sql.getProductNames(p_list);
		// this.p_best_prices = sql.getBestPrices(p_list);
	}

	public ArrayList<String> getP_names() {
		return p_names;
	}

	public ArrayList<Float> getP_best_prices() {
		return p_best_prices;
	}

	public ArrayList<Long> getAllProducts() {
		SQL sql = new SQL();
		p_list = new ArrayList<Long>(sql.getListOfProducts());
		return p_list;
	}

	public boolean checkProductExists(Product prd) {
		SQL sql = new SQL();
		return sql.checkUrlExists(prd.getHashed_p_url());
	}

	public int insertIntoCrawledProducts(Product prd) {
		SQL sql = new SQL();
		sql.insertIntoCrawledProducts(prd.getP_name(), prd.getP_desc(), prd.getP_section(), prd.getP_category(),
				prd.getP_sub_category(), prd.getP_type(), prd.getP_sub_type(),prd.getP_specs(), prd.getCrawl_timestamp());

		return sql.getMaxPid();
	}

}
