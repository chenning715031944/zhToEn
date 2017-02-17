package com.liudaxia.util;

import java.io.File;

import org.liudaxia.vo.InsertSql;
import org.liudaxia.vo.UpdateSql;

/*
 * 
 * 通过读取excel获取sql语句
 * excel应该为xls兼容的不支持xlsx
 */
public class GenerateSql {
	
	/**
	 * 
	 * @param path excel文档的路径
	 * @param ywlb  生成的插入语句属于那一块的翻译内容比如 研究生自助打印
	 * @throws Exception
	 */
	public static void getInsertSql(String path,String ywlb) throws Exception{
		
		File file = new File(path);
		StringBuilder sb =null;
		StringBuilder errorInfo = new StringBuilder();
		
		ExcelObject<InsertSql> readExcel = ExcelUtil.readExcel(file, InsertSql.class);
		//打印出中文版的insert语句
		for(InsertSql insertSql :readExcel.getDataList()){
			
			if(insertSql.getId()==null||"".equals(insertSql.getId())||"null".equals(insertSql.getId())){
				errorInfo.append(insertSql.getZh()).append(",");
				continue;
			}
			
			sb = new StringBuilder();
			//insert into SYS_DYYZYWJ values('jsp.cjddyb.v_zzfw_zzdy_yjsdysq_yjs.sqyybncgwszf','zh_CN','申请原因不能超过50个字符!','','','','研究生自助打印','','','','');
			sb.append("insert into SYS_DYYZYWJ values('");
			sb.append(insertSql.getId()).append("','zh_CN','")
			.append(insertSql.getZh()).append("','','','','").append(ywlb).append("','','','','');");
			
			System.out.println(sb.toString());
			
		}
		
		
		//打印出英文版的insert语句
				for(InsertSql insertSql :readExcel.getDataList()){
					if(insertSql.getId()==null||"".equals(insertSql.getId())||"null".equals(insertSql.getId())){
						continue;
					}
				
					//insert into SYS_DYYZYWJ values('jsp.cjddyb.v_zzfw_zzdy_yjsdysq_yjs.sqzt','en_US','申请状态','','是','','研究生自助打印','','','','');
					sb = new StringBuilder();
					//insert into SYS_DYYZYWJ values('jsp.cjddyb.v_zzfw_zzdy_yjsdysq_yjs.sqyybncgwszf','zh_CN','申请原因不能超过50个字符!','','','','研究生自助打印','','','','');
					sb.append("insert into SYS_DYYZYWJ values('");
					sb.append(insertSql.getId()).append("','en_US','")
					.append(insertSql.getZh()).append("','','是','','").append(ywlb).append("','','','','');");
					
					System.out.println(sb.toString());
				}
				
				System.err.println(errorInfo.toString()+"没有Id");
		
	}
	
	/**
	 * 根据excel生成用于更新的sql语句
	 * @param excelPath
	 *
	 * @throws Exception 
	 */
	public static  void generateUpdateSql(String excelPath) throws Exception{
		File file = new File(excelPath);
		StringBuilder sb =null;
		
		ExcelObject<UpdateSql> readExcel = ExcelUtil.readExcel(file, UpdateSql.class);
		
		for(UpdateSql updateSql : readExcel.getDataList()){
			String en = updateSql.getEn();
			if(en==null||"".equals(en)){
				continue;
			}
			
			//update SYS_DYYZYWJ set XSZ='教学目的：' where id= 'jsp.kc.kckcb.show_center.jxmd' and YYLX='zh_CN'
			sb = new StringBuilder();
			sb.append("update SYS_DYYZYWJ set XSZ='").append(en).append("' ")
			.append("where id = '").append(updateSql.getId()).append("' ").append("and YYLX='en_US';");
			
			System.out.println(sb.toString());
			
		}
		
		
	}
	
	
	/**
	 * 研究生自助打印
	 * @param args
	 * @throws Exception
	 */
	
	public static void main1(String[] args) throws Exception {
		String path = "C:\\Users\\liudaxia\\Desktop\\研究生自助打印成绩单需要翻译的内容.xls";
		generateUpdateSql(path);
	}
	
	public static void main2(String[] args) throws Exception {
		String ywlb = "研究生自助打印";
		getInsertSql("C:\\Users\\liudaxia\\Desktop\\研究生自助打印成绩单需要翻译的内容.xls",ywlb);
	}
	
	
	/**
	 * 在读证明需要翻译的内容
	 */
	
	public static void main(String[] args) throws Exception {
		String ywlb = "研究生在读证明";
		getInsertSql("C:\\Users\\liudaxia\\Desktop\\研究生自助打印成绩单需要翻译的内容.xls",ywlb);
	}
	
}
