package com.pingan.jrkj.datacenter.dataload.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.pingan.jrkj.datacenter.dataload.handle.model.ALInputFileBean;
import com.pingan.jrkj.datacenter.dataload.handle.model.ALInputSqlFieldBean;
import com.pingan.jrkj.datacenter.dataload.util.CsvUtil;
import com.pingan.jrkj.datacenter.dataload.util.FileUtil;
import com.pingan.jrkj.datacenter.dataload.util.StringUtil;

public class CsvHandle {

	public static HashMap<Integer, String> LineMap = new HashMap<Integer, String>();
	public static HashMap<String, Integer> PlaceMap = new HashMap<String, Integer>();

	static {
		LineMap.put(1, "#A#");
		LineMap.put(2, "#B#");
		LineMap.put(3, "#C#");
		LineMap.put(4, "#D#");
		LineMap.put(5, "#E#");
		LineMap.put(6, "#F#");
		LineMap.put(7, "#G#");
		LineMap.put(8, "#H#");
		LineMap.put(9, "#I#");
		LineMap.put(10, "#J#");
		LineMap.put(11, "#K#");
		LineMap.put(12, "#L#");
		LineMap.put(13, "#M#");
		LineMap.put(14, "#N#");
		LineMap.put(15, "#O#");

		PlaceMap.put("#A#", 1);
		PlaceMap.put("#B#", 2);
		PlaceMap.put("#C#", 3);
		PlaceMap.put("#D#", 4);
		PlaceMap.put("#E#", 5);
		PlaceMap.put("#F#", 6);
		PlaceMap.put("#G#", 7);
		PlaceMap.put("#H#", 8);
		PlaceMap.put("#I#", 9);
		PlaceMap.put("#J#", 10);
		PlaceMap.put("#K#", 11);
		PlaceMap.put("#L#", 12);
		PlaceMap.put("#M#", 13);
		PlaceMap.put("#N#", 14);
		PlaceMap.put("#O#", 15);
	}

	private static List<ALInputFileBean> getInputFileModel() {
		List<ALInputFileBean> list = new ArrayList<ALInputFileBean>();
		ALInputFileBean model = new ALInputFileBean();
		model.setRecordId(1);
		model.setFileName("24money");
		model.setFilePath("D:\\data\\coreKPI\\upload\\dcds");
		model.setFileRegExp("24Money_\\w+.csv");
		model.setSplitSign('	');
		model.setInputSql("insert into test(name,lastperiod,age) values(#B#,#A#,#C#);");
		model.setEnable(1);
		model.setAlInputSqlFieldBeans(getInputSqlFieldModel());
		list.add(model);
		return list;
	}

	private static List<ALInputSqlFieldBean> getInputSqlFieldModel() {
		List<ALInputSqlFieldBean> list = new ArrayList<ALInputSqlFieldBean>();
		ALInputSqlFieldBean model = new ALInputSqlFieldBean();
		model.setRecordId(1);
		model.setFileId(1);
		model.setFieldPointName("#A#");
		model.setSynch(1);
		model.setDefaultValue(null);
		model.setLimitLength("1,10");
		model.setOldFormat("yyyyMMdd");
		model.setNewFormat("^yyyyg$");
		model.setSqlKey("URL");
		model.setSqlValue("RecordID");
		model.setTableName("DW_MetaData.TestUrl");
		list.add(model);
		ALInputSqlFieldBean model2 = new ALInputSqlFieldBean();
		model2.setRecordId(2);
		model2.setFileId(1);
		model2.setFieldPointName("#B#");
		model2.setSynch(1);
		model2.setDefaultValue(null);
		model2.setLimitLength("1,10");
		model2.setOldFormat(null);
		model2.setNewFormat(null);
		model2.setSqlKey("URL");
		model2.setSqlValue("RecordID");
		model2.setTableName("DW_MetaData.TestUrl");
		list.add(model2);
		return list;
	}

	public static String[][] convertField(final String[][] strs, final String sql, final List<ALInputSqlFieldBean> aisfBeans) {
		String replaceSql = sql;
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < strs.length; i++) {
			map.clear();
			for (int j = 0; j < strs[i].length; j++) {
				// System.out.print(strs[i][j]);
				map.put(LineMap.get((j + 1)), strs[i][j]);
			}
			for (ALInputSqlFieldBean aisfBean : aisfBeans) {
				String value = map.get(aisfBean.getFieldPointName());
				// 默认值
				value = getDefault(value, aisfBean.getDefaultValue());
				// 截取长度
				value = getLimitLength(value, aisfBean.getLimitLength());
				// 正则转换
				value = StringUtil.replaceAll(value, aisfBean.getOldFormat(), aisfBean.getNewFormat());
				map.put(aisfBean.getFieldPointName(), value);
			}
			for (String key : map.keySet()) {
				replaceSql = replaceSql.replaceAll(key, map.get(key));
			}
			// System.out.println();
			System.out.println(replaceSql);
		}
		return null;
	}

	/**
	 * 根据value获得默认值 当value不等于""(空字符串)的时候
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	private static String getDefault(final String value, final String defaultValue) {
		// 如果不是空字符串则返回该value
		if (!value.equals("")) {
			return value;
		}
		// 如果默认值为null
		if (defaultValue == null || defaultValue.equals("#NULL#")) {
			return "NULL";
		}
		// 如果默认值为空字符串
		if (defaultValue.equals("") || defaultValue.equals("#EMPTY#")) {
			return "''";
		}
		return "'" + defaultValue + "'";
	}

	/**
	 * 根据value截取字符串
	 * 
	 * @param value
	 * @param limitLength
	 *            格式:(5,10);(5,);(,10); 从第几个截取到第几个
	 * @return
	 */
	private static String getLimitLength(final String value, final String limitLength) {
		if (limitLength == null || limitLength.equals("") || limitLength.indexOf(',') == -1 || limitLength.equals(",")) {
			return value;
		}
		String[] limit = limitLength.split(",");
		int valueLength = value.length();
		int beginIndex = 0;
		int endIndex = 0;
		if (limitLength.matches("\\d+,\\d+")) {
			beginIndex = Integer.parseInt(limit[0]) - 1;
			endIndex = Integer.parseInt(limit[1]);
			endIndex = endIndex > valueLength ? valueLength : endIndex;
		} else if (limitLength.matches(",\\d+")) {
			beginIndex = valueLength - Integer.parseInt(limit[1]);
			beginIndex = beginIndex < 0 ? 0 : beginIndex;
			endIndex = valueLength;
		} else if (limitLength.matches("\\d+,")) {
			beginIndex = Integer.parseInt(limit[0]) - 1;
			beginIndex = beginIndex > valueLength ? valueLength : beginIndex;
			endIndex = valueLength;
		}
		// 容错处理(-111,-111)||(999999,999)
		beginIndex = beginIndex < 0 ? 0 : beginIndex;
		beginIndex = beginIndex > valueLength ? valueLength : beginIndex;
		endIndex = endIndex < 0 ? 0 : endIndex;
		endIndex = endIndex > valueLength ? valueLength : endIndex;
		return value.substring(beginIndex, endIndex);
	}

	public static void main(String[] args) throws Exception {
		// 读取需要处理的Csv文件

		String fileDirectory = "D:\\data\\";
		String regexp = "24Money_\\w*.csv";
		String[] names = FileUtil.findFileNameRegexp(fileDirectory, regexp);
		// 找到匹配文件
		if (names != null && names.length > 0) {
			for (String name : names) {
				// 读取CSV文件
				String[][] strs = CsvUtil.readCsv(fileDirectory + name);
				convertField(strs, "insert into test(name,lastperiod,age) values(#B#,#A#,#C#);", getInputSqlFieldModel());
				
				System.out.println(strs.length);
				// for (String[] strs1 : strs) {
				// for (String str : strs1) {
				// System.out.print(str);
				// }
				// System.out.println();
				// }
			}
		}

		// String value = "ab123456zx";
		// System.out.println(getLimitLength(value, "11231,1111"));
		// System.out.println(getLimitLength(value, "-11231,1111"));
		// System.out.println(getLimitLength(value, "7,9"));
		// System.out.println(getLimitLength(value, ",4"));
		// System.out.println(getLimitLength(value, "10,"));

	}

}
