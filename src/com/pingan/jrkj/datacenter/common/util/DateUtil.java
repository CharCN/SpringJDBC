package com.pingan.jrkj.datacenter.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static String subDay(String date, Integer number) throws ParseException {
		return sub(date, number, Calendar.DAY_OF_MONTH);
	}

	public static String subMonth(String date, Integer number) throws ParseException {
		return sub(date, number, Calendar.MONTH);
	}

	public static String sub(String date, Integer number, Integer field) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(date));
		c.add(field, -number);
		return sdf.format(c.getTime());
	}

	public static void main(String[] args) throws Exception {
		System.out.println(subMonth("2013-12-12", 12));
	}

}
