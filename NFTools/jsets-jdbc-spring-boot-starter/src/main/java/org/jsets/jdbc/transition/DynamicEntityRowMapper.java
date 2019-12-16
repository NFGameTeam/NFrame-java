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
import org.jsets.jdbc.metadata.DynamicEntityElement;
import org.jsets.jdbc.metadata.DynamicFieldElement;
import org.jsets.jdbc.util.JdbcCommons;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;
/**
 * 
 *  动态 RowMapper
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class DynamicEntityRowMapper<T> implements RowMapper<T>{
	
	private final LobHandler lobHandler;
	private final DynamicEntityElement dynamicEntityElement;
	private final Class<?> dynamicEntityClass;
	private final boolean isMap;

	public DynamicEntityRowMapper(LobHandler lobHandler
			,DynamicEntityElement dynamicEntityElement,Class<?> dynamicEntityClass) {
		this.lobHandler = lobHandler;
		this.dynamicEntityElement = dynamicEntityElement;
		this.dynamicEntityClass = dynamicEntityClass;
		this.isMap = true;
	}
	
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T instance = JdbcCommons.newInstance(this.dynamicEntityClass);
		ResultSetMetaData rsm =rs.getMetaData();
		int col = rsm.getColumnCount();   //获得列的个数
		for (int i = 1; i <= col; i++) { 
			String columnLabel = rsm.getColumnLabel(i).toUpperCase();
			int columnType = rsm.getColumnType(i);
			DynamicFieldElement dynamicFieldElement = this.dynamicEntityElement
								.getDynamicFieldElements().get(columnLabel.toUpperCase());
			if(null == dynamicFieldElement) continue;
			Object value = JdbcCommons.getResultValue(rs, i, columnType, dynamicFieldElement.getType());
			if(value==null) continue;
			String errorMsg = "实体："+this.dynamicEntityElement.getName()+" 字段："+dynamicFieldElement.getName()+" 设置值失败";
			JdbcCommons.invokeMethod(instance, dynamicFieldElement.getWriteMethod(), errorMsg, value);
		}
		return instance;
	}

}