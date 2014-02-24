package com.pingan.jrkj.datacenter.dataload.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String getString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 根据str获得该reg里的值 如:"\\w*(\\d{4})-(\\d{2})-(\\d{2})\\w*",则是获得20121212格式的值
	 * 
	 * @param str
	 * @param reg
	 * @return
	 */
	public static String getRegexpValue(final String str, final String reg) {
		String s = "";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				s += m.group(i);
			}
		}
		return s;
	}

	/**
	 * 将该字符换中获得的新格式替换老格式的值
	 * 
	 * @param str
	 * @param oldReg
	 *            需要被替换的正则
	 * @param newReg
	 *            需要被替换成新的正则
	 * @param flag
	 *            是否替换成正则
	 * @return
	 */
	public static String replaceAll(final String str, final String oldReg, final String newReg) {
		if(str == null || oldReg == null || newReg == null){
			return str;
		}
		if (newReg.startsWith("^") && newReg.endsWith("$")) {
			return str.replaceAll(oldReg, getRegexpValue(str, newReg));
		}
		return str.replaceAll(oldReg, newReg);
	}

	public static void main(String[] args) {
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\w+(\\d{2})\\w+");
		Matcher m = p.matcher("qweqew34sdsa12sd");
		while (m.find()) {
			System.out.println(m.group(1));
		}
		System.out.println(java.util.regex.Pattern.compile("\\w+(\\d{2})\\w+").matcher("qweqew34sdsa12sd").find());
//		System.out.println("---------------------");
//		String str = "sdsaqsq2012-12-22ssasq";
//		String str1 = "2012-12-22";
//		String reg1 = "\\d{4}-\\d{2}-\\d{2}";
//		System.out.println(str.matches(reg1));
//		String rep2 = "\\w*(\\d{4})-(\\d{2})-(\\d{2})\\w*";
//		System.out.println(str.matches(rep2));
//		System.out.println(getRegexpValue(str, rep2));
//		System.out.println(str);
//		System.out.println(str1.replaceAll("\\d{4}-\\d{2}-\\d{2}", getRegexpValue(str, rep2)));
//		System.out.println(replaceAll("sdsaqsq2012-12-22ssasq", "\\d{4}-\\d{2}-\\d{2}", "^\\w*(\\d{4})-(\\d{2})-(\\d{2})\\w*$"));
	}

}
