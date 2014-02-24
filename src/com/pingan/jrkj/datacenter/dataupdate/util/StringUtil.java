package com.pingan.jrkj.datacenter.dataupdate.util;

public class StringUtil {

	public static String replaceAll(final String str, final String oldstr, final String newstr) {
		if (str.indexOf(oldstr) == -1)
			return str;
		return replaceAll(str.replace(oldstr, newstr), oldstr, newstr);
	}

	public static String getString(Object o) {
		if (o == null)
			return null;
		return o.toString();
	}

	public static void main(String[] args) {

		String str = "Update test set number=$B$,age=$B$ where 1=1  and id=1 ";
		System.out.println(replaceAll(str, "$B$", "111"));
		System.out.println("  asd asd sa das daqq qw das d1231 2ewq e1  ".trim());
	}

}
