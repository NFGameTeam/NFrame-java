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
package org.jsets.jdbc.transition;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.jsets.jdbc.metadata.EntityElement;
import org.jsets.jdbc.metadata.FieldElement;
import org.jsets.jdbc.util.JdbcCommons;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * 实体RowMapper
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class EntityRowMapper<T> implements RowMapper<T>{

	private final LobHandler lobHandler;
	private final EntityElement entityElement;
	private final Class<?> persistentClass;

	public EntityRowMapper(LobHandler lobHandler,EntityElement entityElement,Class<?> persistentClass) {
		this.persistentClass = persistentClass;
		this.entityElement = entityElement;
		this.lobHandler = lobHandler;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T instance = JdbcCommons.newInstance(this.persistentClass);
		ResultSetMetaData rsm =rs.getMetaData();
		int col = rsm.getColumnCount();
		for (int i = 1; i <= col; i++) {  
			String columnLabel = rsm.getColumnLabel(i).toUpperCase();
			int columnType = rsm.getColumnType(i);
			FieldElement fieldElement = this.entityElement.getFieldElements().get(columnLabel.toUpperCase());
			if(null == fieldElement) continue;
			Object value = null;
			if(fieldElement.isClob()){
				value = this.lobHandler.getClobAsString(rs, i);
			} else if(fieldElement.isBlob()){
				value = this.lobHandler.getBlobAsBytes(rs, i);
			} else {
				value = JdbcCommons.getResultValue(rs, i, columnType, fieldElement.getType());
			}
			if(value==null) continue;
			if(null == fieldElement.getWriteMethod()) {
				throw new RuntimeException("实体："+this.entityElement.getName()+" 字段："+fieldElement.getName()+" 没有set方法");
			}
			String errorMsg = "实体："+this.entityElement.getName()+" 字段："+fieldElement.getName()+" 设置值失败";
			JdbcCommons.invokeMethod(instance, fieldElement.getWriteMethod(), errorMsg, value);
		}
		return instance;
	}

}