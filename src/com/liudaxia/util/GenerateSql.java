package com.liudaxia.util;

import java.io.File;

import org.liudaxia.vo.InsertSql;

/*
 * 
 * 通过读取excel获取sql语句
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
		
		ExcelObject<InsertSql> readExcel = ExcelUtil.readExcel(file, InsertSql.class);
		//打印出中文版的insert语句
		for(InsertSql insertSql :readExcel.getDataList()){
			
			sb = new StringBuilder();
			//insert into SYS_DYYZYWJ values('jsp.cjddyb.v_zzfw_zzdy_yjsdysq_yjs.sqyybncgwszf','zh_CN','申请原因不能超过50个字符!','','','','研究生自助打印','','','','');
			sb.append("insert into SYS_DYYZYWJ values('");
			sb.append(insertSql.getId()).append("','zh_CN','")
			.append(insertSql.getZh()).append("','','','','").append(ywlb).append("','','','','');");
			
			System.out.println(sb.toString());
		}
		
		
		//打印出英文版的insert语句
				for(InsertSql insertSql :readExcel.getDataList()){
				
					//insert into SYS_DYYZYWJ values('jsp.cjddyb.v_zzfw_zzdy_yjsdysq_yjs.sqzt','en_US','申请状态','','是','','研究生自助打印','','','','');
					sb = new StringBuilder();
					//insert into SYS_DYYZYWJ values('jsp.cjddyb.v_zzfw_zzdy_yjsdysq_yjs.sqyybncgwszf','zh_CN','申请原因不能超过50个字符!','','','','研究生自助打印','','','','');
					sb.append("insert into SYS_DYYZYWJ values('");
					sb.append(insertSql.getId()).append("','en_US','")
					.append(insertSql.getZh()).append("','','是','','").append(ywlb).append("','','','','');");
					
					System.out.println(sb.toString());
				}
		
	}
	
	public static void main(String[] args) throws Exception {
		String ywlb = "研究生自助打印";
		getInsertSql("C:\\Users\\liudaxia\\Desktop\\研究生自助打印成绩单需要翻译的内容.xls",ywlb);
	}
}
