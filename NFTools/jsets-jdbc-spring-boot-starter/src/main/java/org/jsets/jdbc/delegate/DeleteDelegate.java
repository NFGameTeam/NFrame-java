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

import org.jsets.jdbc.metadata.ElementResolver;
import org.jsets.jdbc.metadata.EntityElement;
import org.jsets.jdbc.util.SqlBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * 删除执行器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class DeleteDelegate extends AbstractDelegate<Integer> {
	
	private final Class<?> persistentClass;
	private final Object primaryKeyValue;
	private final SqlBuilder sqlBuilder = SqlBuilder.BUILD();

	public DeleteDelegate(JdbcTemplate jdbcTemplate,Class<?> persistentClass,Object primaryKeyValue) {
		super(jdbcTemplate);
		this.persistentClass = persistentClass;
		this.primaryKeyValue = primaryKeyValue;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistentClass);
		EntityElement entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.DELETE_FROM(entityElement.getTable());
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn()+" = ?");
	}

	@Override
	protected Integer doExecute() throws DataAccessException{
		String sql = this.sqlBuilder.toString().toUpperCase();
		return this.jdbcTemplate.update(sql,this.primaryKeyValue);
	}
}