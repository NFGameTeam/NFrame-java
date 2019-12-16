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
import java.util.List;
import org.jsets.jdbc.metadata.ElementResolver;
import org.jsets.jdbc.metadata.EntityElement;
import org.jsets.jdbc.metadata.FieldElement;
import org.jsets.jdbc.metadata.ValueElement;
import org.jsets.jdbc.transition.ValueBatchSetter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import org.jsets.jdbc.util.JdbcCommons;
import org.jsets.jdbc.util.SqlBuilder;

/**
 * 批量更新执行器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class UpdatesDelegate extends AbstractDelegate<int[]> {

	private final LinkedList persistents = Lists.newLinkedList();
	private final SqlBuilder sqlBuilder = SqlBuilder.BUILD();
	private LinkedList<LinkedList<ValueElement>> batchValueElements;
	
	public UpdatesDelegate(JdbcTemplate jdbcTemplate,List<?> persistents) {
		super(jdbcTemplate);
		this.persistents.addAll(persistents);
	}

	@Override
	public void prepare() {
		Class<?> persistentClass = this.persistents.get(0).getClass();
		this.checkEntity(persistentClass);
		EntityElement entityElement = ElementResolver.resolve(persistentClass);
		this.batchValueElements = Lists.newLinkedList();
		this.sqlBuilder.UPDATE(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) continue;
			if(fieldElement.isPrimaryKey()) continue;
			this.sqlBuilder.SET(fieldElement.getColumn() + " = ?");
		}
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn() + " = ?");
		for (Object persistent : persistents) {
			LinkedList<ValueElement> valueElements = Lists.newLinkedList();
			Object primaryKeyValue = null;
			for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
				if(fieldElement.isTransientField()) continue;
				if (fieldElement.isPrimaryKey()) {
					primaryKeyValue = JdbcCommons.invokeMethod(persistent, fieldElement.getReadMethod()
									, "实体："+entityElement.getName()+" 主键："+fieldElement.getName()+" 获取值失败");
					Assert.notNull(primaryKeyValue,"实体:" + entityElement.getName() + ", 主键不能为空");
					continue;
				}
				Object value = JdbcCommons.invokeMethod(persistent, fieldElement.getReadMethod()
							,"实体："+entityElement.getName()+" 字段："+fieldElement.getName()+" 获取值失败");
				valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
			}
			valueElements.add(new ValueElement(primaryKeyValue,Boolean.FALSE,Boolean.FALSE));
			this.batchValueElements.add(valueElements);
		}
	}

	@Override
	protected int[] doExecute() throws DataAccessException{
		String sql = this.sqlBuilder.toString().toUpperCase();
		return this.jdbcTemplate.batchUpdate(sql,new ValueBatchSetter(this.LOBHANDLER,this.batchValueElements));
	}
	
}