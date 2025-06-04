package com.loxpression.util;

public class StringUtils {

	public static String clean(String str) {
		return (str == null ? "" : str.trim());
	}

	public static String trim(String str) {
		return (str == null ? null : str.trim());
	}

	public static boolean isNotEmpty(String str) {
		return (str != null && str.length() > 0);
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	public static boolean isBlank(String str) {
		return (str == null || str.trim().isEmpty());
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean equals(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equalsIgnoreCase(str2));
	}

}
