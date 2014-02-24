package com.pingan.jrkj.datacenter.dataload.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.csvreader.CsvReader;
import com.pingan.jrkj.datacenter.dataload.handle.CsvHandle;

public class CsvUtil {

	private static final String DEFAULT_ENCODING = "GBK";

	private static final char DEFAULT_DELIMITER = '\t';

	public static String[][] readCsv(String filePath) {
		return readCsv(filePath, DEFAULT_DELIMITER, DEFAULT_ENCODING);
	}

	public static String[][] readCsv(String filePath, char delimiter) {
		return readCsv(filePath, delimiter, DEFAULT_ENCODING);
	}

	public static String[][] readCsv(String filePath, String fileName, String encoding) {
		return readCsv(filePath, DEFAULT_DELIMITER, encoding);
	}

	public static String[][] readCsv(String filePath, char delimiter, String encoding) {
		ArrayList<String[]> csvList = null;
		String[][] strs = null;
		CsvReader reader = null;
		try {
			csvList = new ArrayList<String[]>();
			reader = new CsvReader(filePath, delimiter, Charset.forName(encoding));
			// reader.readHeaders(); // 跳过表头 如果需要表头的话，不要写这句。
			reader.setSkipEmptyRecords(true);
			// reader.setDelimiter('	');
			int intRow = 0;
			int intCol = 0;
			while (reader.readRecord()) { // 逐行读入除表头的数据
				intCol = reader.getColumnCount();
				csvList.add(reader.getValues());
				intRow++;
			}
			reader.close();
			strs = new String[intRow][intCol];
			for (int i = 0; i < csvList.size(); i++) {
				String[] str = csvList.get(i);
				for (int j = 0; j < intCol; j++) {
					// 如果是空字符串,则返回null
//					strs[i][j] = str[j].equals("") ? null : str[j];
					strs[i][j] = str[j];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return strs;
	}

	public static void main(String[] args) {
		String[] synchFields = {"#A#", "#B#", "#D#"};

		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

		for(String synchField : synchFields){
			map.put(synchField, new HashMap<String, String>());
		}

		String[][] strs = readCsv("D:/data/coreKPI/upload/dcds/success/24Money_VV.csv");
		for (int i = 0; i < strs.length; i++) {
			for (int j = 0; j < strs[i].length; j++) {
				System.out.print(strs[i][j] + "|");
				
				for(String synchField : synchFields){
					HashMap<String, String> m = map.get(synchField);
					//(CsvHandle.PlaceMap.get(synchField) - 1);
					m.put(strs[i][(CsvHandle.PlaceMap.get(synchField) - 1)], null);
				}
				
			}
			System.out.println();
		}

		System.out.println("-----------------------");
		System.out.println(map);
	}

}
