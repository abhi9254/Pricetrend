package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Linker {

	public Linker(String p_url) {

	}

	public Linker() {
		System.out.println("Constructing linker");
	}

	public boolean denormalizeProductData() {
		SQL sql = new SQL();
		boolean status = sql.denormalizeCrawledData();
		if (status)
			System.out.println("Denormalize success");
		else
			System.out.println("Denormalize fail");
		return status;

	}

	public void linkMatchingProducts() {
		System.out.println("Linking in progress..");
		HashMap<Long, String[]> all_products = new HashMap<Long, String[]>();
		SQL sql = new SQL();
		all_products = sql.getAllDenormalizedProducts();

		Iterator<Entry<Long, String[]>> itr = all_products.entrySet().iterator();
		LinkedHashMap<Long, Float> matching_products;
		Iterator<Entry<Long, Float>> itr2;

		while (itr.hasNext()) {
			Map.Entry<Long, String[]> product = (Entry<Long, String[]>) itr.next();
			String[] prd_dtls = product.getValue();
			Long c_p_id = (Long) product.getKey();
			matching_products = sql.findMatchingProducts(c_p_id, prd_dtls[0], prd_dtls[1], prd_dtls[2]);

			itr2 = matching_products.entrySet().iterator();
			Long[] match_c_p_id = new Long[10];
			Float[] match_diff = new Float[10];
			int i = 0;
			while (itr2.hasNext()) {
				Map.Entry<Long, Float> matching_product = (Map.Entry<Long, Float>) itr2.next();
				match_c_p_id[i] = matching_product.getKey();
				match_diff[i++] = matching_product.getValue();
			}
			sql.insertIntoMatchingProducts(c_p_id, match_c_p_id[0], match_diff[0], match_c_p_id[1], match_diff[1],
					match_c_p_id[2], match_diff[2], match_c_p_id[3], match_diff[3], match_c_p_id[4], match_diff[4],
					match_c_p_id[5], match_diff[5], match_c_p_id[6], match_diff[6], match_c_p_id[7], match_diff[7],
					match_c_p_id[8], match_diff[8], match_c_p_id[9], match_diff[9]);
		}
		System.out.println("Linking finished");
	}

}
