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
package org.jsets.jdbc.metadata;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.UniqueConstraint;

/**
 * 实体元素
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class EntityElement implements Element{

	private static final long serialVersionUID = -1010706706028472274L;
	
	private String name;// 实体名称
	private String table;//// 表名称
	private String catalog;// 指定数据库名称
	private String schema;// 指定数据库的用户名
	private FieldElement primaryKey;// 主键
	UniqueConstraint[] uniqueConstraints;//指定唯一性字段约束,如为personid 和name 字段指定唯一性约束 uniqueConstraints={ @UniqueConstraint(columnNames={"personid", "name"})}
	private Class<?> persistentClass;//持久化类
	private Map<String,FieldElement> fieldElements = new LinkedHashMap<String,FieldElement>();//属性

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public FieldElement getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(FieldElement primaryKey) {
		this.primaryKey = primaryKey;
	}
	public UniqueConstraint[] getUniqueConstraints() {
		return uniqueConstraints;
	}
	public void setUniqueConstraints(UniqueConstraint[] uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}
	public Class<?> getPersistentClass() {
		return persistentClass;
	}
	public void setPersistentClass(Class<?> persistentClass) {
		this.persistentClass = persistentClass;
	}
	public Map<String, FieldElement> getFieldElements() {
		return Collections.unmodifiableMap(this.fieldElements);
	}
	public void addFieldElement(String columnName, FieldElement fieldElement) {
		this.fieldElements.put(columnName, fieldElement);
	}
	
}