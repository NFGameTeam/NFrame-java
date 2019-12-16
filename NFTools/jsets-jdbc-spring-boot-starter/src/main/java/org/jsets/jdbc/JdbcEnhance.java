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

import java.util.List;
import org.jsets.jdbc.delegate.CountDelegate;
import org.jsets.jdbc.delegate.DeleteDelegate;
import org.jsets.jdbc.delegate.FindDelegate;
import org.jsets.jdbc.delegate.GetDelegate;
import org.jsets.jdbc.delegate.InsertDelegate;
import org.jsets.jdbc.delegate.InsertsDelegate;
import org.jsets.jdbc.delegate.UpdateDelegate;
import org.jsets.jdbc.delegate.UpdatesDelegate;
import org.jsets.jdbc.util.SqlBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
/**
 * 
 * JdbcTemplate增强包装类，支持部分JPA规范
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 * 
 */
public class JdbcEnhance {

	private final JdbcTemplate jdbcTemplate;
	private final String dialectName;

	public JdbcEnhance(JdbcTemplate jdbcTemplate,String dialectName){
		this.jdbcTemplate = jdbcTemplate;
		this.dialectName = dialectName;
	}
	
	/**
	 * 获取jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	/**
	 * 插入
	 * @param persistent 持久化实体
	 * @return 受变更影响的行数
	 */
	public int insert(Object persistent) throws DataAccessException{
		Assert.notNull(persistent, "实体不能为空");
		InsertDelegate delegate = new InsertDelegate( jdbcTemplate, persistent);
		int rows = delegate.execute();
		delegate = null;//hlep gc.
		return rows;
	}

	/**
	 * 批量插入
	 * 如果数据量过大，建议分次插入，每次最好不超过一万条。
	 * @param entities 持久化实体列表
	 * @return 插入的行数
	 */
	public int inserts(List<?> persistents) throws DataAccessException{
		Assert.notEmpty(persistents, "实体列表不能为空");
		InsertsDelegate delegate = new InsertsDelegate(jdbcTemplate,persistents);
		int[] rows = delegate.execute();
		delegate = null;//hlep gc.
		return rows.length;
	}
	
	/**
	 * SQL插入
	 * @param insertSql SQL构造器
	 * @param parameters 参数
	 * @return 插入的行数
	 */
	public int insert(SqlBuilder insertSql,Object... parameters) throws DataAccessException{
		Assert.hasText(insertSql.toString(), "SQL构造器不能为空");
		int rows = this.jdbcTemplate.update(insertSql.toString(), parameters);
		return rows;
	}

	/**
	 * 删除
	 * @param persistentClass 实体类
	 * @param primaryKeyValue 主键值
	 * @return 受影响的行数
	 */
	public int delete(Class<?> persistentClass,Object primaryKeyValue) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		Assert.notNull(primaryKeyValue, "主键不能为空");
		Assert.hasText(primaryKeyValue.toString(), "主键不能为空");
		DeleteDelegate delegate = new DeleteDelegate(jdbcTemplate,persistentClass,primaryKeyValue);
		int rows = delegate.execute();
		delegate = null;//hlep gc.
		return rows;
	}
	
	/**
	 * SQL删除
	 * @param deleteSql SQL构造器
	 * @param parameters 参数
	 * @return 删除的行数
	 */
	public int delete(SqlBuilder deleteSql,Object... parameters) throws DataAccessException{
		Assert.hasText(deleteSql.toString(), "SQL构造器不能为空");
		int rows = this.jdbcTemplate.update(deleteSql.toString(), parameters);
		return rows;
	}

	/**
	 * 更新
	 * @param persistent 持久化实体
	 * @return 受影响的行数
	 */
	public int update(Object persistent) throws DataAccessException{
		Assert.notNull(persistent, "实体不能为空");
		UpdateDelegate delegate = new UpdateDelegate(jdbcTemplate,persistent,true);
		int rows = delegate.execute();
		delegate = null;//hlep gc.
		return rows;
	}
	/**
	 * 批量更新
	 * @param persistent 持久化实体列表
	 * @return 受影响的行数
	 */
	public int updates(List<?> persistents) throws DataAccessException{
		Assert.notEmpty(persistents, "实体列表不能为空");
		UpdatesDelegate delegate = new UpdatesDelegate(jdbcTemplate,persistents);
		int[] rows = delegate.execute();
		delegate = null;//hlep gc.
		return rows.length;
	}

	/**
	 * SQL修改
	 * @param updateSql SQL构造器
	 * @param parameters 参数
	 * @return 删除的行数
	 */
	public int update(SqlBuilder updateSql,Object... parameters) throws DataAccessException{
		Assert.notNull(updateSql, "SQL构造器不能为空");
		int rows = this.jdbcTemplate.update(updateSql.toString(), parameters);
		return rows;
	}
	
	/**
	 * 获取
	 * @param persistent 持久化实体类
	 * @param primaryKeyValue 主键值
	 * @return 实体对象
	 */
	public <T> T get(Class<?> persistentClass,Object primaryKeyValue) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		Assert.notNull(primaryKeyValue, "主键不能为空");
		GetDelegate<T> delegate = new GetDelegate<T>(jdbcTemplate,persistentClass,primaryKeyValue);
		try{
			T entity = delegate.execute();
			return entity;
		} catch(EmptyResultDataAccessException e){
			//当查询结果为空时，会抛出：EmptyResultDataAccessException，这里规避这个异常直接返回null
		}
		delegate = null;//hlep gc.
		return null;
		
	}
	
	/**
	 * @param sql sql语句
	 * @param persistentClass 实体类型
	 * @param parameters 参数
	 * @return
	 */
	public <T> T get(String sql,Class<?> persistentClass,Object... parameters) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		List<T> results = find( sql,persistentClass,parameters);
		int size = (results != null ? results.size() : 0);
		if(size>1){
			new IncorrectResultSizeDataAccessException(1, size);
		}
		return results.get(0);
	}
	
	/**
	 * 实体查询
	 * @param selectSql 查询SQL构造器
	 * @param persistentClass 持久化实体类
	 * @param parameters 查询参数
	 * @return 实体对象列表
	 */
	public <T> List<T> find(SqlBuilder selectSql,Class<?> persistentClass,Object... parameters) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		Assert.hasText(selectSql.toString(), "sql语句不能为空");
		FindDelegate<List<T>> delegate =  new FindDelegate<List<T>>(jdbcTemplate,dialectName,persistentClass,selectSql.toString(),parameters);
		List<T> list = delegate.execute();
		delegate = null;//hlep gc.
		return list;
	}
	
	/**
	 * 实体查询
	 * @param selectSql 查询SQL构造器
	 * @param startRow 起始行
	 * @param limit 条数
	 * @param persistentClass 持久化实体类
	 * @param parameters 查询参数
	 * @return 实体对象列表
	 */
	public <T> List<T> find(SqlBuilder selectSql,Class<?> persistentClass,int startRow,int limit,Object... parameters) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		Assert.hasText(selectSql.toString(), "sql语句不能为空");
		Assert.isTrue(startRow>=0, "startRow必须大于等于0");
		Assert.isTrue(limit>0, "limit必要大于0");
		FindDelegate<List<T>> delegate =  new FindDelegate<List<T>>(jdbcTemplate,dialectName,persistentClass,selectSql.toString(),parameters,null,startRow,limit);
		List<T> list = delegate.execute();
		delegate = null;//hlep gc.
		return list;
	}
	
	/**
	 * 实体查询
	 * @param sql 查询SQL
	 * @param persistentClass 持久化实体类
	 * @param parameters 查询参数
	 * @return 实体对象列表
	 */
	public <T> List<T> find(String sql,Class<?> persistentClass,Object... parameters) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		Assert.hasText(sql, "sql语句不能为空");
		FindDelegate<List<T>> delegate =  new FindDelegate<List<T>>(jdbcTemplate,dialectName,persistentClass,sql,parameters);
		List<T> list = delegate.execute();
		delegate = null;//hlep gc.
		return list;
	}
	
	/**
	 * 实体查询
	 * @param sql 查询SQL
	 * @param startRow 起始行
	 * @param limit 条数
	 * @param persistentClass 持久化实体类
	 * @param parameters 查询参数
	 * @return 实体对象列表
	 */
	public <T> List<T> find(String sql,Class<?> persistentClass,int startRow,int limit,Object... parameters) throws DataAccessException{
		Assert.notNull(persistentClass, "实体类型不能为空");
		Assert.hasText(sql, "sql语句不能为空");
		Assert.isTrue(startRow>=0, "startRow必须大于等于0");
		Assert.isTrue(limit>0, "limit必要大于0");
		FindDelegate<List<T>> delegate =  new FindDelegate<List<T>>(jdbcTemplate,dialectName,persistentClass,sql,parameters,null,startRow,limit);
		List<T> list = delegate.execute();
		delegate = null;//hlep gc.
		return list;
	}

	/**
	 * 条数统计
	 * @param sql 统计SQL
	 * @param args 统计参数
	 * @return 条数
	 */
	public int count(String sql,Object... parameters) throws DataAccessException{
		Assert.hasText(sql, "sql语句不能为空");
		CountDelegate delegate =  new CountDelegate(jdbcTemplate,sql,parameters);
		int count = delegate.execute();
		delegate = null;//hlep gc.
		return count;
	}

	/**
	 * 查询器
	 */
	public JdbcSelector selector(){
		return new JdbcSelector(jdbcTemplate,dialectName);
	}
}