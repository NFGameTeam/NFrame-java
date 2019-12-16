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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jsets.jdbc.metadata.resolver.ColumnResolver;
import org.jsets.jdbc.metadata.resolver.GeneratedValueResolver;
import org.jsets.jdbc.metadata.resolver.IdResolver;
import org.jsets.jdbc.metadata.resolver.LobResolver;
import org.jsets.jdbc.metadata.resolver.Resolver;
import org.jsets.jdbc.metadata.resolver.TableResolver;
import org.jsets.jdbc.metadata.resolver.TransientResolver;
import org.jsets.jdbc.util.JdbcCommons;
import org.springframework.util.Assert;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * 
 * 元数据解析
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 *
 */
public class ElementResolver {
	
	public static final Map<String, EntityElement> ENTITIES = new ConcurrentHashMap<String, EntityElement>();
	public static final Map<String, DynamicEntityElement> DYNAMIC_ENTITIES = new ConcurrentHashMap<String, DynamicEntityElement>();
	private static final Map<Class<?>, Resolver> RESOLVERS = new ConcurrentHashMap<Class<?>,Resolver>();

	static{
		RESOLVERS.put(javax.persistence.Table.class, new TableResolver());
		RESOLVERS.put(javax.persistence.Id.class, new IdResolver());
		RESOLVERS.put(javax.persistence.GeneratedValue.class, new GeneratedValueResolver());
		RESOLVERS.put(javax.persistence.Column.class, new ColumnResolver());
		RESOLVERS.put(javax.persistence.Lob.class, new LobResolver());
		RESOLVERS.put(javax.persistence.Transient.class, new TransientResolver());
		
	}
	
	/**
	 * 解析实体
	 */
	public static EntityElement resolve(Class<?> persistentClass){
		String persistentClassName = persistentClass.getName();
		if (ENTITIES.containsKey(persistentClassName)){
			return ENTITIES.get(persistentClassName);
		}
		EntityElement entityElement = new EntityElement();
		entityElement.setPersistentClass(persistentClass);
		entityElement.setName(persistentClassName);
		for(Annotation annotation:persistentClass.getAnnotations()){
			Resolver resolver = RESOLVERS.get(annotation.annotationType());
			if(null!=resolver)
				resolver.resolve(entityElement, annotation);
		}
		Set<Class<?>> mappedSuperclass  = getMappedSuperclass(persistentClass);
		Set<Field> fields =  JdbcCommons.getFields(persistentClass,mappedSuperclass);
		for(Field field:fields){
			if(Modifier.isFinal(field.getModifiers())) continue;
			String fieldName = field.getName();
			Method readMethod = JdbcCommons.getReadMethod(persistentClass,mappedSuperclass, fieldName);
			Method writeMethod = JdbcCommons.getWriteMethod(persistentClass,mappedSuperclass, fieldName, field.getType());
			FieldElement fieldElement = new FieldElement();
			fieldElement.setEntityElement(entityElement);
			fieldElement.setField(field);
			fieldElement.setType(field.getType());
			fieldElement.setName(fieldName);
			fieldElement.setReadMethod(readMethod);
			fieldElement.setWriteMethod(writeMethod);
			Annotation[] annotations = field.getAnnotations();
			if ((null == annotations || annotations.length == 0)&&null!=readMethod) {
				annotations = readMethod.getAnnotations();
			}
			for(Annotation annotation:field.getAnnotations()){
				Resolver resolver = RESOLVERS.get(annotation.annotationType());
				if(null!=resolver)
					resolver.resolve(fieldElement, annotation);
			}
			if(!fieldElement.isTransientField()){
				Assert.notNull(readMethod,"实体:" + persistentClassName + ", 字段：" + fieldName + " 没有get方法");
				Assert.notNull(writeMethod,"实体:" + persistentClassName + ", 字段：" + fieldName + " 没有set方法");
			}
			if(Strings.isNullOrEmpty(fieldElement.getColumn()))
				fieldElement.setColumn(JdbcCommons.camelToUnderline(fieldName));
			fieldElement.setEntityElement(null);
			fieldElement.setField(null);
			entityElement.addFieldElement(fieldElement.getColumn().toUpperCase(), fieldElement);
		}
		Assert.notNull(entityElement.getPrimaryKey(),"实体："+entityElement.getName()+",必须要注解主键。");
		entityElement.setPersistentClass(null);
		ENTITIES.put(persistentClassName, entityElement);
		return entityElement;
	}

	/**
	 * 获取给定类的所有注解MappedSuperclass的父类
	 */
	private static Set<Class<?>> getMappedSuperclass(final Class<?> cls) {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		Class<?> superclass = cls.getSuperclass();
		while (superclass != null) {
			if(null!=superclass.getAnnotation(javax.persistence.MappedSuperclass.class)){
				Assert.isNull(superclass.getAnnotation(javax.persistence.Table.class),
						"实体："+superclass.getName()+",注解错误。 MappedSuperclass、Table两个注解不能同时用在一个类上");
				classes.add(superclass);
			}
			superclass = superclass.getSuperclass();
		}
		return Collections.unmodifiableSet(classes);
	}
	
	/**
	 * 解析动态实体
	 */
	public static DynamicEntityElement resolveDynamic(Class<?> dynamicEntityClass,Map<String,String> dynamicMappings) {
		boolean dynamicMappinged = false;
		if(null!=dynamicMappings&&!dynamicMappings.isEmpty()) dynamicMappinged  = true;
		String dynamicEntityClassName = dynamicEntityClass.getName();
		if (!dynamicMappinged&&DYNAMIC_ENTITIES.containsKey(dynamicEntityClassName)){
			return DYNAMIC_ENTITIES.get(dynamicEntityClassName);
		}
		DynamicEntityElement dynamicEntityElement = new DynamicEntityElement();
		dynamicEntityElement.setName(dynamicEntityClassName);
		Set<Class<?>> superclass = Sets.newHashSet();
		Set<Field> fields = JdbcCommons.getFields(dynamicEntityClass,superclass);
		for(Field field:fields){
			if(Modifier.isFinal(field.getModifiers())) continue;
			String fieldName = field.getName();
			Method readMethod = JdbcCommons.getReadMethod(dynamicEntityClass,superclass, fieldName);
			Method writeMethod = JdbcCommons.getWriteMethod(dynamicEntityClass, superclass, fieldName, field.getType());
			Assert.notNull(readMethod,"实体:" + dynamicEntityClassName + ", 字段：" + fieldName + " 没有get方法");
			Assert.notNull(writeMethod,"实体:" + dynamicEntityClassName + ", 字段：" + fieldName + " 没有set方法");
			DynamicFieldElement dynamicFieldElement = new DynamicFieldElement();
			dynamicFieldElement.setName(fieldName);
			dynamicFieldElement.setType(field.getType());
			dynamicFieldElement.setReadMethod(readMethod);
			dynamicFieldElement.setWriteMethod(writeMethod);
			String columnName =  JdbcCommons.camelToUnderline(fieldName);
			if(dynamicMappinged&&!Strings.isNullOrEmpty(dynamicMappings.get(fieldName)))
				columnName = dynamicMappings.get(fieldName);
			dynamicFieldElement.setColumn(columnName);
			dynamicEntityElement.addDynamicFieldElements(columnName.toUpperCase(), dynamicFieldElement);
		}
		if(!dynamicMappinged)
			DYNAMIC_ENTITIES.put(dynamicEntityClassName, dynamicEntityElement);
		return dynamicEntityElement;
	}
	
}