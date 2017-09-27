package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SQL {
	private DataSource dataSource;
	private Connection connection;
	private Statement statement;

	public SQL() {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("mysql_pool");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void insertIntoCrawledLinks(String domain, String anchor, Timestamp crawl_timestamp, String p_url,
			String non_p_url, String hashed_url) {
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.crawled_links VALUES(" + crawl_timestamp.getTime() + ",'" + domain
					+ "','" + anchor + "','" + crawl_timestamp + "','" + p_url + "','" + non_p_url + "','" + hashed_url
					+ "')";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public boolean insertIntoMyProducts(Integer p_id, String p_name, String p_desc, String p_url, String p_website,
			String p_price) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.my_products VALUES(" + p_id + ",'" + p_name + "','" + p_desc + "','"
					+ p_url + "','" + p_website + "','" + p_price + "')";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;
	}

	public boolean insertIntoCrawledProducts(String p_name, String p_desc, String p_section, String p_category,
			String p_sub_category, String p_type, String p_sub_type, String p_specs, Timestamp crawl_timestamp) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "INSERT INTO pricetrend.crawled_products VALUES(" + crawl_timestamp.getTime() + ",'" + p_name
					+ "','" + p_desc + "','" + p_section + "','" + p_category + "','" + p_sub_category + "','" + p_type
					+ "','" + p_sub_type + "',JSON_OBJECT(" + p_specs + "),'" + crawl_timestamp + "')";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;
	}

	public boolean insertIntoProductsLinks(long c_p_id, String p_website, Float p_price, String p_url,
			String p_hashed_url, Timestamp updated_timestamp) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.product_links (c_p_id,p_website,p_price,p_url,hashed_p_url,updated_timestamp) VALUES("
					+ c_p_id + ",'" + p_website + "'," + p_price + ",'" + p_url + "','" + p_hashed_url + "','"
					+ updated_timestamp + "')";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;
	}

	public boolean insertIntoProductSpecs(Integer p_id, String p_specs, Timestamp updated_timestamp) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.product_specs (p_id,p_specs,updated_timestamp) VALUES(" + p_id
					+ ",JSON_OBJECT(" + p_specs + "),'" + updated_timestamp + "')";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;
	}

	public ArrayList<Long> getListOfProducts() {
		ResultSet resultSet;
		ArrayList<Long> p_list = new ArrayList<Long>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select c_p_id from pricetrend.crawled_products_denorm";

			// String query = "select p_id from pricetrend.all_products";
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				p_list.add(resultSet.getLong(1));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p_list;
	}

	public int getMaxPid() {
		ResultSet resultSet;
		int p_id = 0;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select MAX(p_id) from pricetrend.all_products";
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				p_id = resultSet.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p_id;
	}

	public ArrayList<String> getURLsForProduct(long p_id) {
		ResultSet resultSet;
		ArrayList<String> p_url_list = new ArrayList<String>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select p_url from pricetrend.products_links where p_id=" + p_id;
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				p_url_list.add(resultSet.getString(1));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p_url_list;
	}

	public boolean checkUrlExists(String hashed_p_url) {
		ResultSet resultSet;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select c_p_id from pricetrend.product_links where hashed_p_url='" + hashed_p_url + "'";
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				connection.close();
				return true;
			}

			else {
				connection.close();
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String[]> getCrawledPrdUrls() {
		ResultSet resultSet;
		List<String[]> crawled_prd_url_list = new ArrayList<String[]>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select distinct hashed_url,p_url from pricetrend.crawled_links";
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String[] url = new String[2];
				url[0] = resultSet.getString(1);
				url[1] = resultSet.getString(2);
				crawled_prd_url_list.add(url);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return crawled_prd_url_list;

	}

	public ArrayList<String> getProductNames(ArrayList<Long> p_list) {
		ResultSet resultSet;
		ArrayList<String> prd_names = new ArrayList<String>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			for (long p_id : p_list) {
				String query = "select p_name from pricetrend.crawled_products_denorm where c_p_id=" + p_id;

				// String query = "select p_name from pricetrend.all_products
				// where p_id=" + p_id;
				// System.out.println(query);
				resultSet = statement.executeQuery(query);
				while (resultSet.next()) {
					String p_name = resultSet.getString(1);
					prd_names.add(p_name);
				}
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prd_names;

	}

	public String[] getProductDetails(long p_id) {
		ResultSet resultSet;
		String[] prd_dtls = new String[2];
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select p_name,CONCAT(p_spec_2,'/ ',p_spec_3) from pricetrend.crawled_products_denorm where c_p_id="
					+ p_id;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				prd_dtls[0] = resultSet.getString(1);
				prd_dtls[1] = resultSet.getString(2);
			}
			connection.close();
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return prd_dtls;

	}

	public Long[] getProductMatches(long p_id) {
		ResultSet resultSet;
		Long[] prd_matches = new Long[2];
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select match_c_p_id_1,match_c_p_id_2 from pricetrend.product_matches where c_p_id=" + p_id;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				prd_matches[0] = resultSet.getLong(1);
				prd_matches[1] = resultSet.getLong(2);
			}
			connection.close();
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return prd_matches;

	}

	public String[] getProductLink(long p_id) {
		ResultSet resultSet;
		String[] prd_link = new String[3];
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select p_website,p_price,p_url from pricetrend.product_links where c_p_id=" + p_id;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				prd_link[0] = resultSet.getString(1);
				prd_link[1] = resultSet.getString(2);
				prd_link[2] = resultSet.getString(3);
			}
			connection.close();
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return prd_link;

	}

	public ArrayList<Long> getUserProducts(int u_id) {
		ResultSet resultSet;
		ArrayList<Long> prd_list = new ArrayList<Long>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "select p_id from pricetrend.my_products where u_id=" + u_id;
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				long p_id = resultSet.getInt(1);
				prd_list.add(p_id);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prd_list;

	}

	public boolean denormalizeCrawledData() {
		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.crawled_products_denorm (c_p_id,p_name,p_desc,p_section,p_category,p_sub_category,p_type,p_sub_type,p_spec_1,p_spec_2,p_spec_3,p_spec_4,p_spec_5)"
					+ "SELECT c_p_id,p_name,p_desc,p_section,p_category,p_sub_category,p_type,p_sub_type,brand,model,color,NULL,NULL FROM "
					+ "(select c_p_id,p_name,p_desc,p_section,p_category,p_sub_category,p_type,p_sub_type,coalesce(p_specs->>'$.Brand',getBrandFrom(p_name)) as brand,coalesce(p_specs->>'$.Model',p_specs->>'$.\"Model Name\"',p_specs->>'$.\"Model Id\"') as model,coalesce(p_specs->>'$.Color',p_specs->>'$.Colour',getColorFrom(p_name)) as color from pricetrend.crawled_products "
					+ "where c_p_id IN(select DISTINCT c_p_id from pricetrend.product_links) AND c_p_id NOT IN(select DISTINCT c_p_id from pricetrend.crawled_products_denorm))d where (brand is not null and model is not null and color is not null)";

			System.out.println("Cleaning pricetrend.crawled_products_denorm");
			statement.executeUpdate("delete FROM pricetrend.crawled_products_denorm");
			statement.executeUpdate(query);
			System.out.println("Cleaning pricetrend.product_matches");
			statement.executeUpdate("delete FROM pricetrend.product_matches");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;
	}

	public LinkedHashMap<Long, Float> findMatchingProducts(Long c_p_id, String p_spec_1, String p_spec_2,
			String p_spec_3) {
		ResultSet resultSet;
		LinkedHashMap<Long, Float> match_prds = new LinkedHashMap<Long, Float>(10);
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "select c_p_id,match_difference FROM"
					+ "(select tab.c_p_id,(max_similarity - MATCH(p_spec_2,p_spec_3) AGAINST('" + p_spec_2 + " "
					+ p_spec_3 + "'))/max_similarity AS match_difference FROM"
					+ "(select MAX(MATCH(p_spec_2,p_spec_3) AGAINST('" + p_spec_2 + " " + p_spec_3
					+ "')) AS max_similarity " + "FROM pricetrend.crawled_products_denorm WHERE c_p_id!=" + c_p_id
					+ " and LOWER(p_spec_1)='" + p_spec_1.toLowerCase()
					+ "')tab2,pricetrend.crawled_products_denorm tab " + "WHERE c_p_id!=" + c_p_id
					+ " and LOWER(p_spec_1)='" + p_spec_1.toLowerCase()
					+ "' and max_similarity>=0.8)tab3 WHERE match_difference<=0.2 ORDER BY match_difference LIMIT 10";

			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				Long match_c_p_id = resultSet.getLong(1);
				Float match_diff = resultSet.getFloat(2);
				match_prds.put(match_c_p_id, match_diff);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return match_prds;

	}

	public HashMap<Long, String[]> getAllDenormalizedProducts() {
		ResultSet resultSet;
		HashMap<Long, String[]> all_products = new HashMap<Long, String[]>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "select c_p_id,p_section,p_category,p_type,p_sub_type,p_spec_1,p_spec_2,p_spec_3,p_spec_4,p_spec_5 from pricetrend.crawled_products_denorm";

			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				Long c_p_id = resultSet.getLong(1);
				String[] prd_dtls = new String[5];
				prd_dtls[0] = resultSet.getString(6);
				prd_dtls[1] = resultSet.getString(7);
				prd_dtls[2] = resultSet.getString(8);
				prd_dtls[3] = resultSet.getString(9);
				prd_dtls[4] = resultSet.getString(10);
				all_products.put(c_p_id, prd_dtls);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return all_products;
	}

	public boolean insertIntoMatchingProducts(Long c_p_id, Long match_c_p_id_1, Float match_diff_1, Long match_c_p_id_2,
			Float match_diff_2, Long match_c_p_id_3, Float match_diff_3, Long match_c_p_id_4, Float match_diff_4,
			Long match_c_p_id_5, Float match_diff_5, Long match_c_p_id_6, Float match_diff_6, Long match_c_p_id_7,
			Float match_diff_7, Long match_c_p_id_8, Float match_diff_8, Long match_c_p_id_9, Float match_diff_9,
			Long match_c_p_id_10, Float match_diff_10) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.product_matches VALUES(" + c_p_id + "," + match_c_p_id_1 + ","
					+ match_diff_1 + "," + match_c_p_id_2 + "," + match_diff_2 + "," + match_c_p_id_3 + ","
					+ match_diff_3 + "," + match_c_p_id_4 + "," + match_diff_4 + "," + match_c_p_id_5 + ","
					+ match_diff_5 + "," + match_c_p_id_6 + "," + match_diff_6 + "," + match_c_p_id_7 + ","
					+ match_diff_7 + "," + match_c_p_id_8 + "," + match_diff_8 + "," + match_c_p_id_9 + ","
					+ match_diff_9 + "," + match_c_p_id_10 + "," + match_diff_10 + ")";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;

	}
}
