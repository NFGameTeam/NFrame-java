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
package org.jsets.jdbc.delegate;

import java.util.Map;
import org.jsets.jdbc.metadata.DynamicEntityElement;
import org.jsets.jdbc.metadata.ElementResolver;
import org.jsets.jdbc.metadata.EntityElement;
import org.jsets.jdbc.transition.DynamicEntityRowMapper;
import org.jsets.jdbc.transition.EntityRowMapper;
import org.jsets.jdbc.util.PaginationUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 实体查询执行器  
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 */
public class FindDelegate<E> extends AbstractDelegate<E> {

	private final String dialectName;
	private final Class<?> persistentClass;
	private final String sql;
	private final Integer startRow;
	private final Integer limit;
	private final Map<String,String> dynamicMappings;
	private final Object[] parameters;
	private String querySql;
	private EntityElement entityElement;
	private DynamicEntityElement dynamicEntityElement;
	private boolean isDynamic;
	
	public FindDelegate(JdbcTemplate jdbcTemplate,String dialectName,Class<?> persistentClass,String sql) {
		this(jdbcTemplate, dialectName,persistentClass,sql,null,null,null,null);
	}
	
	public FindDelegate(JdbcTemplate jdbcTemplate,String dialectName
			,Class<?> persistentClass,String sql,Object[] parameters) {
		this(jdbcTemplate, dialectName,persistentClass,sql,parameters,null,null,null);
	}
	
	public FindDelegate(JdbcTemplate jdbcTemplate,String dialectName
				,Class<?> persistentClass,String sql,Object[] parameters
				,Map<String,String> dynamicMappings,Integer startRow,Integer limit) {
		super(jdbcTemplate);
		this.dialectName = dialectName;
		this.persistentClass = persistentClass;
		this.sql = sql;
		this.parameters = parameters;
		this.dynamicMappings = dynamicMappings;
		this.startRow = startRow;
		this.limit = limit;
	}

	@Override
	public void prepare() {
		if(this.isEntity(this.persistentClass)){
			this.isDynamic = false;
			this.entityElement = ElementResolver.resolve(this.persistentClass);
		} else {
			this.isDynamic = true;
			this.dynamicEntityElement = ElementResolver.resolveDynamic(this.persistentClass,this.dynamicMappings);
		}
		if(null!=this.limit&&this.limit>0){
			this.querySql = PaginationUtil.pagination(dialectName, this.sql, startRow, this.limit);
		} else {
			this.querySql  = this.sql;
		}
	}

	@Override
	@SuppressWarnings("all")
	protected E doExecute() throws DataAccessException{
		RowMapper rowMapper = null;
		if(this.isDynamic){
			rowMapper = new DynamicEntityRowMapper(this.LOBHANDLER,this.dynamicEntityElement,this.persistentClass);
		} else {
			rowMapper = new EntityRowMapper(this.LOBHANDLER,this.entityElement,this.persistentClass);
		}
		if(null==this.parameters||this.parameters.length==0){
			return (E) this.jdbcTemplate.query(this.querySql,rowMapper);
		} else {
			return (E) this.jdbcTemplate.query(this.querySql,this.parameters,rowMapper);
		}
	}
}