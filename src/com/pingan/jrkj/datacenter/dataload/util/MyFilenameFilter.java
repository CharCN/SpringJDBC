package com.pingan.jrkj.datacenter.dataload.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class MyFilenameFilter implements FilenameFilter {

	private Pattern p;

	public MyFilenameFilter(String regex) {
		p = Pattern.compile(regex);
	}

	public boolean accept(File file, String name) {
		return p.matcher(name).matches();
	}

	public static void main(String[] args) {
		File directory = new File("D:\\data");
		String[] names = directory.list(new MyFilenameFilter("24Money_\\w*.csv"));
		System.out.println(names[0] + names[1]);
	}

}
