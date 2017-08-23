package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
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

	public void insertIntoCrawledData(String domain, String anchor, Timestamp crawl_timestamp, String p_url,
			String non_p_url, String hashed_url) {
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO pricetrend.crawled_data VALUES('" + domain + "','" + anchor + "','"
					+ crawl_timestamp + "','" + p_url + "','" + non_p_url + "','" + hashed_url + "')";
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

	public boolean insertIntoAllProducts(String p_name, String p_desc, String p_section, String p_category,
			String p_sub_category) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "INSERT INTO pricetrend.all_products (p_name,p_desc,p_section,p_category,p_sub_category) VALUES('"
					+ p_name + "','" + p_desc + "','" + p_section + "','" + p_category + "','" + p_sub_category + "')";
			// System.out.println(query);
			statement.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			returnCode = false;
		}
		return returnCode;
	}

	public boolean insertIntoProductsLinks(Integer p_id, String p_website, Float p_price, String p_url,
			String p_hashed_url, Timestamp updated_timestamp) {

		boolean returnCode = true;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "INSERT INTO pricetrend.products_links (p_id,p_website,p_price,p_url,hashed_p_url,updated_timestamp) VALUES("
					+ p_id + ",'" + p_website + "'," + p_price + ",'" + p_url + "','" + p_hashed_url + "','"
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

	public ArrayList<Integer> getListOfProducts() {
		ResultSet resultSet;
		ArrayList<Integer> p_list = new ArrayList<Integer>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String query = "select p_id from pricetrend.all_products";
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				p_list.add(resultSet.getInt(1));
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

	public ArrayList<String> getURLsForProduct(int p_id) {
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
			String query = "select p_id from pricetrend.products_links where hashed_p_url='" + hashed_p_url + "'";
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
			String query = "select distinct hashed_url,p_url from pricetrend.crawled_data";
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

	public ArrayList<String> getProductNames(ArrayList<Integer> p_list) {
		ResultSet resultSet;
		ArrayList<String> prd_names = new ArrayList<String>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			for (int p_id : p_list) {
				String query = "select p_name from pricetrend.all_products where p_id=" + p_id;
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

	public ArrayList<Integer> getUserProducts(int u_id) {
		ResultSet resultSet;
		ArrayList<Integer> prd_list = new ArrayList<Integer>();
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			String query = "select p_id from pricetrend.my_products where u_id=" + u_id;
			// System.out.println(query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int p_id = resultSet.getInt(1);
				prd_list.add(p_id);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prd_list;

	}

}
