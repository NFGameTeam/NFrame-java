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
import javax.persistence.UniqueConstraint;
import org.jsets.jdbc.metadata.Element;
import org.jsets.jdbc.metadata.EntityElement;
import org.springframework.util.Assert;
import com.google.common.base.Strings;

/**
 * Table注解解析器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class TableResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		EntityElement entityElement = (EntityElement) element;
		Class<?> persistentClass = entityElement.getPersistentClass();
		Assert.isNull(persistentClass.getAnnotation(javax.persistence.MappedSuperclass.class),
				"实体："+persistentClass.getName()+",注解错误。 MappedSuperclass、Table两个注解不能同时用在一个类上");
		javax.persistence.Table table = (javax.persistence.Table) annotation;
		if (!Strings.isNullOrEmpty(table.name())) entityElement.setTable(table.name());
		entityElement.setCatalog(table.catalog());
		entityElement.setSchema(table.schema());
		UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
		entityElement.setUniqueConstraints(uniqueConstraints);
	}

}