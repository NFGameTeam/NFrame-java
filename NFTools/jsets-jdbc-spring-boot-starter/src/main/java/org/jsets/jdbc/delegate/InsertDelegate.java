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

import java.util.LinkedList;
import org.jsets.jdbc.metadata.ElementResolver;
import org.jsets.jdbc.metadata.EntityElement;
import org.jsets.jdbc.metadata.FieldElement;
import org.jsets.jdbc.metadata.ValueElement;
import org.jsets.jdbc.transition.ValueSetter;
import org.jsets.jdbc.util.JdbcCommons;
import org.jsets.jdbc.util.SqlBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
/**
 * 
 * 新增执行器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class InsertDelegate extends AbstractDelegate<Integer> {

	private final Object persistent;
	private final SqlBuilder sqlBuilder = SqlBuilder.BUILD();
	private LinkedList<ValueElement> valueElements;
	
	public InsertDelegate(JdbcTemplate jdbcTemplate,Object persistent) {
		super(jdbcTemplate);
		this.persistent = persistent;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistent.getClass());
		EntityElement entityElement = ElementResolver.resolve(this.persistent.getClass());
		this.valueElements = Lists.newLinkedList();
		this.sqlBuilder.INSERT_INTO(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) continue;
			Object value = JdbcCommons.invokeMethod(this.persistent, fieldElement.getReadMethod()
							, "实体："+entityElement.getName()+" 字段："+fieldElement.getName()+" 获取值失败");
			if (fieldElement.isPrimaryKey()) {
				value = super.generatedId(this.persistent,fieldElement, value);
				Assert.notNull(value,"实体:" + entityElement.getName() + ", 主键不能为空");
			}
			this.sqlBuilder.VALUES(fieldElement.getColumn(), "?");
			this.valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
		}
	}

	@Override
	protected Integer doExecute() throws DataAccessException{
		String sql = this.sqlBuilder.toString().toUpperCase();
		return this.jdbcTemplate.update(sql,new ValueSetter(this.LOBHANDLER,this.valueElements));
	}
}