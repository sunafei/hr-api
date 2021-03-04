package com.eplugger.utils;

import java.security.MessageDigest;

/**
 * @author aujlure 加密
 */
public class PassMd5 {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	/** 
	 * 对字符串进行MD5加密
	 * @param originString 原字符串
	 * @return 加密后的字符串
	 */
	public static String encodeByMD5(String originString) {
		if (originString == null) {
			return null;
		}
		try {
			return byteArrayToHexString(MessageDigest.getInstance("MD5").digest(originString.getBytes())).toUpperCase();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 将一个字节数组转为16进制
	 * @param b 字节数组
	 * @return 转化后的十六进制字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			result.append(byteToHexString(b[i]));
		}
		return result.toString();
	}

	/** 
	 * 将一个字节转化成十六进制形式的字符串 
	 * @param b 一个字节
	 * @return 十六进制字符串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}