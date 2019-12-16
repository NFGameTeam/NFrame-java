/*
 * Copyright 2017-2018 the original author(https://github.com/wj596)
 * 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package org.jsets.jdbc;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jsets.jdbc.delegate.CountDelegate;
import org.jsets.jdbc.delegate.FindDelegate;
import org.jsets.jdbc.util.SqlBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * 选择器
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 */
public class JdbcSelector {

	private final JdbcTemplate jdbcTemplate;
	private final String dialectName;

	protected JdbcSelector(JdbcTemplate jdbcTemplate,String dialectName){
		this.jdbcTemplate = jdbcTemplate;
		this.dialectName = dialectName;
	}

	private JdbcSelector getSelf(){
		 return this;
	}
	
	private String sql;
	private int startRow;
	private int limit;
	private Class<?> entityClass;
	private final Map<String,String> mappings = Maps.newHashMap();
	private final LinkedList<Object> parameters = new LinkedList<Object>();

	/**
	 * 实体类型
	 * @param entityClass 实体类型
	 */
	public JdbcSelector entityClass(Class<?> entityClass){
		this.entityClass = entityClass;
		return getSelf();
	}
	
	/**
	 * 列<---->类字段 映射
	 * @param column 列名
	 * @param field 类字段名
	 */
	public JdbcSelector mapping(String column,String field){
		Assert.hasText(column, "映射的列不能为空");
		Assert.hasText(field, "映射的属性不能为空");
		this.mappings.put(field,column);
		return getSelf();
	}
	
	/**
	 * 查询SQL
	 * @param sql SQL语句
	 */
	public JdbcSelector sql(String sql){
		this.sql = sql;
		return getSelf();
	}
	
	/**
	 * 参数
	 * @param parameters 参数
	 */
	public JdbcSelector parameter(Object parameter) {
		Assert.notNull(parameters, "参数不能为空");
		this.parameters.add(parameter);
		return getSelf();
	}
	
	/**
	 * 参数
	 * @param parameters 参数
	 */
	public JdbcSelector parameters(Object... parameters) {
		Assert.notNull(parameters, "参数不能为空");
		for(Object parameter:parameters)
			this.parameters.add(parameter);
		return getSelf();
	}

	/**
	 * 起始行
	 * @param startRow
	 */
	public JdbcSelector startRow(int startRow){
		Assert.isTrue(startRow>=0, "startRow必须大于等于0");
		this.startRow = startRow;
		return getSelf();
	}

	/**
	 * 查询条数
	 * @param startRow
	 */
	public JdbcSelector limit(int limit){
		Assert.isTrue(limit>0, "limit必须要大于0");
		this.limit = limit;
		return getSelf();
	}

	/**
	 * 获取单个实体
	 */
	public <T> T get() throws DataAccessException{
		Assert.notNull(entityClass, "实体类型不能为空");
		if(Strings.isNullOrEmpty(this.sql))
			this.sql = this.sqlBuilder.toString();
		Assert.hasText(sql, "SQL语句不能为空");
		FindDelegate<List<T>> delegate =  new FindDelegate<List<T>>(jdbcTemplate,dialectName,entityClass,sql,parameters.toArray());
		List<T> results = delegate.execute();
		delegate = null;//hlep gc.
		int size = (results != null ? results.size() : 0);
		if(size>1){
			new IncorrectResultSizeDataAccessException(1, size);
		}
		if(size==1){
			return results.get(0);
		}
		return null;
	}
	/**
	 * 查询实体列表
	 */
	public <T> List<T>  list() throws DataAccessException{
		Assert.notNull(entityClass, "实体类型不能为空");
		if(Strings.isNullOrEmpty(this.sql))
			this.sql = this.sqlBuilder.toString();
		Assert.hasText(sql, "SQL语句不能为空");
		FindDelegate<List<T>> delegate =  new FindDelegate<List<T>>( 
						jdbcTemplate, dialectName,entityClass,sql
						,parameters.toArray(),this.mappings,startRow,limit);
		List<T> results = delegate.execute();
		delegate = null;//hlep gc.
		return results;
	}
	/**
	 * 查询实体数量
	 * @return 条目数
	 */
	public int  count() throws DataAccessException{
		if(Strings.isNullOrEmpty(this.sql))
			this.sql = this.sqlBuilder.toString();
		Assert.hasText(sql, "SQL语句不能为空");
		CountDelegate delegate =  new CountDelegate(jdbcTemplate,sql,parameters.toArray());
		int count = delegate.execute();
		delegate = null;//hlep gc.
		return count;
	}
	

	// ================= 构建SQL
	private final SqlBuilder sqlBuilder = SqlBuilder.BUILD();
	
	public JdbcSelector SELECT(String columns) {
		this.sqlBuilder.SELECT(columns);
		return getSelf();
	}

	public JdbcSelector SELECT_DISTINCT(String columns) {
		this.sqlBuilder.SELECT_DISTINCT(columns);
		return getSelf();
	}

	public JdbcSelector FROM(String table) {
		this.sqlBuilder.FROM(table);
		return getSelf();
	}

	public JdbcSelector JOIN(String join) {
		this.sqlBuilder.JOIN(join);
		return getSelf();
	}

	public JdbcSelector INNER_JOIN(String join) {
		this.sqlBuilder.INNER_JOIN(join);
		return getSelf();
	}

	public JdbcSelector LEFT_OUTER_JOIN(String join) {
		this.sqlBuilder.LEFT_OUTER_JOIN(join);
		return getSelf();
	}

	public JdbcSelector RIGHT_OUTER_JOIN(String join) {
		this.sqlBuilder.RIGHT_OUTER_JOIN(join);
		return getSelf();
	}

	public JdbcSelector OUTER_JOIN(String join) {
		this.sqlBuilder.OUTER_JOIN(join);
		return getSelf();
	}

	public JdbcSelector WHERE(String conditions) {
		this.sqlBuilder.WHERE(conditions);
		return getSelf();
	}

	public JdbcSelector OR() {
		this.sqlBuilder.OR();
		return getSelf();
	}

	public JdbcSelector AND() {
		this.sqlBuilder.AND();
		return getSelf();
	}

	public JdbcSelector GROUP_BY(String columns) {
		this.sqlBuilder.GROUP_BY(columns);
		return getSelf();
	}

	public JdbcSelector HAVING(String conditions) {
		this.sqlBuilder.HAVING(conditions);
		return getSelf();
	}

	public JdbcSelector ORDER_BY(String columns) {
		this.sqlBuilder.ORDER_BY(columns);
		return getSelf();
	}

}