package cz.hel.aos.util;

// TODO :)
public class HashProvider {

	public static boolean hashesMatch(String hashed, String plain) {
		if (hashed == null || plain == null) {
			return false;
		}
		return hashed.equals(plain);
	}

	public static String hash(String string) {
		return string;
	}
}
