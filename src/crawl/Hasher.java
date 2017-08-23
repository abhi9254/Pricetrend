package crawl;

import java.security.MessageDigest;

public class Hasher {
	public static String toSHA256(String inString) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(inString.toLowerCase().getBytes());
		byte[] hash = md.digest();

		StringBuilder sb = new StringBuilder();
		for (byte b : hash)
			sb.append(String.format("%02x", b));
		return sb.toString().toUpperCase();

	}
}
