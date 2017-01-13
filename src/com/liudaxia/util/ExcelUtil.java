package com.liudaxia.util;



import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.thcic.datafile.excel.GenExcel;


public class ExcelUtil {

	public static final String DEFAULT_SHEETNAME = "sheet1";

	/**
	 * 使用DEFAULT_SHEETNAME，给定的结果集，输出流生成excel文件
	 * 
	 * @param rs
	 * @param out
	 * @throws Exception
	 */
	public static void writeExcel(ResultSet rs, OutputStream out)
			throws Exception {
		writeExcel(DEFAULT_SHEETNAME, rs, out);
	}

	/**
	 * 根据给定的sheetName，结果集和输出流生成excel文件
	 * 
	 * @param sheetName
	 * @param rs
	 * @param out
	 * @throws Exception
	 */
	public static void writeExcel(String sheetName, ResultSet rs,
			OutputStream out) throws Exception {
		if (!GenExcel.getExcel(sheetName, rs, out))
			throw new Exception("Failed in generating Excel.");
	}

	/**
	 * 使用DEFAULT_SHEETNAME，结果集，列名，输出流生成excel文件
	 * 
	 * @param rs
	 * @param columnLabels
	 * @param out
	 * @throws Exception
	 */
	public static void writeExcel(ResultSet rs, String[] columnLabels,
			OutputStream out) throws Exception {
		writeExcel(DEFAULT_SHEETNAME, rs, columnLabels, out);
	}

	/**
	 * 根据给定的sheetName，结果集，列名和输出流生成excel文件
	 * 
	 * @param sheetName
	 * @param rs
	 * @param columnLabels
	 * @param out
	 * @throws Exception
	 */
	public static void writeExcel(String sheetName, ResultSet rs,
			String[] columnLabels, OutputStream out) throws Exception {
		if (!GenExcel.getExcel(sheetName, rs, columnLabels, out))
			throw new Exception("Failed in generating Excel.");
	}

	/**
	 * 根据给定的ExcelObject和输出流生成excel文件
	 * 
	 * @param <T>
	 * 
	 * @param xls
	 * @param out
	 * @throws Exception
	 */
	public static <T> void writeExcel(ExcelObject<T> xls, OutputStream out)
			throws Exception {
		if (xls == null) {
			throw new Exception("Failed in generating Excel: null ExcelObject.");
		}

		List<T> list = xls.getDataList();

		if (list == null) {
			throw new Exception("Failed in generating Excel: null data.");
		}

		ArrayList<String[]> dataList = new ArrayList<String[]>(list.size());

		if (list.size() > 0) {
			Class<?> clazz = list.get(0).getClass();
			String[] fieldNames = xls.getFieldNames();
			int len = fieldNames.length;
			Field[] fields = new Field[len];
			for (int i = 0; i < len; i++) {
				Field field = clazz.getDeclaredField(fieldNames[i]);
				field.setAccessible(true);
				fields[i] = field;
			}
			for (int i = 0, count = list.size(); i < count; i++) {
				T t = list.get(i);
				String[] record = new String[fieldNames.length];
				for (int j = 0; j < record.length; j++) {
					Object value = fields[j].get(t);
					if (fields[j].getType() == Date.class) {
						String pattern = DateUtil.DEFAULT_DATETIME_FORMAT;
						if (fields[j].isAnnotationPresent(DateTimeFormat.class))
							pattern = fields[j].getAnnotation(
									DateTimeFormat.class).pattern();
						record[j] = value == null ? "" : DateUtil.getDateTime(
								(Date) value, pattern);
					} else
						record[j] = value == null ? "" : value.toString();
				}
				dataList.add(record);
			}
		}

		if (!GenExcel.getExcel(xls.getSheetName(), dataList,
				xls.getColumnLabels(), out))
			throw new Exception("Failed in generating Excel.");
	}

	/**
	 * 由给定的excel文件和相关参数生成ExcelObject对象
	 * 
	 * @param <T>
	 * 
	 * @param file
	 *            源文件
	 * @param fieldNames
	 *            数据对应的pojo的属性名数组，并且按照列名对应排序
	 * @param clazz
	 *            数据对应的pojo的Class
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(File file, String[] fieldNames,
			Class<T> clazz) throws Exception {
		return readExcel(Workbook.getWorkbook(file), fieldNames, clazz);
	}

	/**
	 * 由给定的输入流和相关参数生成ExcelObject对象
	 * 
	 * @param file
	 *            源文件
	 * @param target
	 *            目标bean
	 * @param ignore
	 *            忽略字段
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(File file, Class<T> target,
			String... ignore) throws Exception {
		Field[] fields = target.getDeclaredFields();
		List<String> fieldNameList = new ArrayList<String>();
		List<String> ignoreList = Arrays.asList(ignore);
		for (int i = 0; i < fields.length; i++) {
			if (!ignoreList.contains(fields[i].getName()))
				fieldNameList.add(fields[i].getName());
		}
		return readExcel(file, fieldNameList.toArray(new String[0]), target);
	}

	/**
	 * 由给定的输入流和相关参数生成ExcelObject对象
	 * 
	 * @param file
	 *            源文件
	 * @param target
	 *            目标bean
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(File file, Class<T> target)
			throws Exception {
		return readExcel(file, target, new String[0]);
	}

	/**
	 * 由给定的输入流和相关参数生成ExcelObject对象
	 * 
	 * @param <T>
	 * 
	 * @param is
	 *            输入流
	 * @param fieldNames
	 *            数据对应的pojo的属性名数组，并且按照列名对应排序
	 * @param clazz
	 *            数据对应的pojo的Class
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(InputStream is,
			String[] fieldNames, Class<T> clazz) throws Exception {
		return readExcel(Workbook.getWorkbook(is), fieldNames, clazz);
	}

	/**
	 * 由给定的输入流和相关参数生成ExcelObject对象
	 * 
	 * @param is
	 *            输入流
	 * @param target
	 *            目标bean
	 * @param ignore
	 *            忽略字段
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(InputStream is, Class<T> target,
			String... ignore) throws Exception {
		Field[] fields = target.getDeclaredFields();
		List<String> fieldNameList = new ArrayList<String>();
		List<String> ignoreList = Arrays.asList(ignore);
		for (int i = 0; i < fields.length; i++) {
			if (!ignoreList.contains(fields[i].getName()))
				fieldNameList.add(fields[i].getName());
		}
		return readExcel(is, fieldNameList.toArray(new String[0]), target);
	}

	/**
	 * 由给定的输入流和相关参数生成ExcelObject对象
	 * 
	 * @param is
	 *            输入流
	 * @param target
	 *            目标bean
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(InputStream is, Class<T> target)
			throws Exception {
		return readExcel(is, target, new String[0]);
	}

	/**
	 * 由给定的工作薄对象和相关参数生成ExcelObject对象
	 * 
	 * @param <T>
	 * 
	 * @param workbook
	 *            jxl包的工作簿类对象
	 * @param fieldNames
	 *            数据对应的pojo的属性名数组，并且按照列名对应排序
	 * @param clazz
	 *            数据对应的pojo的Class
	 * @return
	 * @throws Exception
	 */
	public static <T> ExcelObject<T> readExcel(Workbook workbook,
			String[] fieldNames, Class<T> clazz) throws Exception {
		Sheet sheet = workbook.getSheet(0);
		int len = sheet.getColumns();
		if (fieldNames.length != len) {
			throw new Exception("Wrong length of columns.");
		}

		// 获得列名
		String[] columnLabels = new String[len];
		for (int i = 0; i < len; i++) {
			columnLabels[i] = sheet.getCell(i, 0).getContents();
		}

		// 获得数据
		List<T> dataList = new ArrayList<T>();
		int count = sheet.getRows();
		if (count > 1) {
			Field[] fields = new Field[len];
			for (int i = 0; i < len; i++) {
				Field field = clazz.getDeclaredField(fieldNames[i]);
				field.setAccessible(true);
				fields[i] = field;
			}
			for (int i = 1; i < count; i++) {
				T t = clazz.newInstance();
				for (int j = 0; j < len; j++) {
					Cell cell = sheet.getCell(j, i);
					Class<?> fieldType = fields[j].getType();
					String valueStr = cell.getContents();
					if (StringUtils.hasText(valueStr)) {
						Object value = valueStr;
						if (fieldType == Date.class) {
							if (fields[j]
									.isAnnotationPresent(DateTimeFormat.class)) {
								String pattern = fields[j].getAnnotation(
										DateTimeFormat.class).pattern();
								value = DateUtil.parse(valueStr, pattern);
							} else {
								value = DateUtil.parseDate(valueStr);
							}
						}
						try {
							if (fieldType == Integer.class
									|| fieldType == Integer.TYPE) {
								value = Integer.parseInt(valueStr);
							} else if (fieldType == Double.class
									|| fieldType == Double.TYPE) {
								value = Double.parseDouble(valueStr);
							}
						} catch (Exception e) {
							value = 0;
						}
						fields[j].set(t, value);
					}
				}
				dataList.add(t);
			}
		}
		return new ExcelObject<T>(sheet.getName(), columnLabels, fieldNames,
				dataList);
	}

}
