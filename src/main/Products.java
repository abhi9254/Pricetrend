package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Products {

	private ArrayList<Integer> p_list;
	private ArrayList<String> p_names;
	private ArrayList<Float> p_best_prices;

	public Products() {

	}

	public Products(ArrayList<Integer> p_list) {
		this.p_list = p_list;
		SQL sql = new SQL();
		this.p_names = sql.getProductNames(p_list);
	//	this.p_best_prices = sql.getBestPrices(p_list);
	}

	public ArrayList<String> getP_names() {
		return p_names;
	}

	public ArrayList<Float> getP_best_prices() {
		return p_best_prices;
	}

	public ArrayList<Integer> getAllProducts() {
		SQL sql = new SQL();
		p_list = new ArrayList<Integer>(sql.getListOfProducts());
		return p_list;
	}

	public boolean checkProductExists(Product prd) {
		SQL sql = new SQL();
		return sql.checkUrlExists(prd.getHashed_p_url());
	}

	public int insertIntoProducts(Product prd) {
		SQL sql = new SQL();
		sql.insertIntoAllProducts(prd.getP_name(), prd.getP_desc(), prd.getP_section(), prd.getP_category(),
				prd.getP_sub_category());

		return sql.getMaxPid();
	}

}
