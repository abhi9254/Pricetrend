package main;

import java.util.ArrayList;

public class User {
	private int u_id;
	private String u_name;
	private int pincode;
	private String email;
	private int mobile;
	private ArrayList<Integer> myproducts = new ArrayList<Integer>();

	public User(int u_id) {
		this.u_id = u_id;
		SQL sql =new SQL();
		this.myproducts=sql.getUserProducts(u_id);
	}

	public ArrayList<Integer> getMyproducts() {
		return myproducts;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getMobile() {
		return mobile;
	}

	public void setMobile(int mobile) {
		this.mobile = mobile;
	}

}
