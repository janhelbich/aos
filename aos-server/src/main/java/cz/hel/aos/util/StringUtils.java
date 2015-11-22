package cz.hel.aos.util;

public class StringUtils {

	public static boolean isEmpty(String s) {
		return s == null || "".equals(s); 
	}
	
	public static boolean compareStrings(String a, String b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null && b != null) {
			return a.equals(b);
		}
		
		return false;
	}
}
