package org.jsets.jdbc.util;


/**
 * 分页查询工具
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class PaginationUtil {
	
	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	public static final String SQLSERVER = "sqlserver";

	public static String pagination(String dialectName,String original, int startRow, int size){
		if(MYSQL.equals(dialectName.toLowerCase())){
			return mysqlPagination( original,  startRow,  size) ;
		} else if(ORACLE.equals(dialectName.toLowerCase())){
			return oraclePagination( original,  startRow,  size) ;
		} else if(SQLSERVER.equals(dialectName.toLowerCase())){
			return sqlserverPagination( original,  startRow,  size) ;
		} else {
			throw new IllegalArgumentException("不支持数据库："+dialectName+"分页。");
		}
	}
	
	
	private static String mysqlPagination(String original, int startRow, int size) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(original);
		if (startRow == 0) {
			sqlBuilder.append(" LIMIT ");
			sqlBuilder.append(size);
		} else {
			sqlBuilder.append(" LIMIT ");
			sqlBuilder.append(startRow);
			sqlBuilder.append(",");
			sqlBuilder.append(size);
		}
		return sqlBuilder.toString();
	}
	
	private static String oraclePagination(String original, int startRow, int size) {
		StringBuilder sqlBuilder = new StringBuilder();
		int endRow = startRow+size;
		sqlBuilder.append("SELECT * FROM ( SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
		sqlBuilder.append(original);
		sqlBuilder.append(" ) TMP_PAGE WHERE ROWNUM <= " + endRow + " ) WHERE ROW_ID > " + startRow);
		return sqlBuilder.toString();
	}
	
	private static String sqlserverPagination(String original, int startRow, int size) {
		StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(original);
        sqlBuilder.append(" OFFSET ");
        sqlBuilder.append(startRow);
        sqlBuilder.append(" ROWS ");
        sqlBuilder.append(" FETCH NEXT ");
        sqlBuilder.append(size);
        sqlBuilder.append(" ROWS ONLY");
		return sqlBuilder.toString();
	}
}
