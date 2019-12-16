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
package org.jsets.jdbc.metadata.resolver;

import java.lang.annotation.Annotation;
import org.jsets.jdbc.metadata.Element;
import org.jsets.jdbc.metadata.FieldElement;
import com.google.common.base.Strings;
/**
 * Column注解解析器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class ColumnResolver implements Resolver{

	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement)element;
		javax.persistence.Column column = (javax.persistence.Column) annotation;
		if (!Strings.isNullOrEmpty(column.name())) fieldElement.setColumn(column.name());
		fieldElement.setNullable(column.nullable());
		fieldElement.setUnique(column.unique());
		fieldElement.setLength(column.length());
		fieldElement.setColumnDefinition(column.columnDefinition());
		fieldElement.setInsertable(column.insertable());
		fieldElement.setUpdatable(column.updatable());
		fieldElement.setTable(column.table());
	}

}