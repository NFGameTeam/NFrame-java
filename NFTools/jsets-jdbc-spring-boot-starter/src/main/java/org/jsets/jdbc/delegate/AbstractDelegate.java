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

import java.util.UUID;
import org.jsets.jdbc.metadata.FieldElement;
import org.jsets.jdbc.metadata.IdGenerators;
import org.jsets.jdbc.util.JdbcCommons;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * 委托接口抽象类
 * @author wangjie 
 * https://gitee.com/wj596
 *
 */
import org.springframework.util.Assert;
/**
 * 抽象执行器
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 */
public abstract class AbstractDelegate<T> implements Delegate<T>{
	
	protected static final LobHandler LOBHANDLER = new DefaultLobHandler();
	protected final JdbcTemplate jdbcTemplate;

	public AbstractDelegate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public T execute() throws DataAccessException {
		prepare();
		return doExecute();
	}
	
	protected abstract void prepare();
	protected abstract T doExecute() throws DataAccessException;
	
	protected Object generatedId(Object persistent,FieldElement fieldElement,Object value){
		if((null == value||"".equals(value))
				&& fieldElement.isGeneratedValue()
				&& IdGenerators.UUID.equals(fieldElement.getGenerator())){
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String errorMsg = "实体："+persistent.getClass().getName()+" 主键："+fieldElement.getName()+" 设置值失败";
			JdbcCommons.invokeMethod(persistent, fieldElement.getWriteMethod(), errorMsg, uuid);
			return uuid;
		}
		return value;	
	}
	
	protected boolean isEntity(Class<?> persistentClass){
		return null != persistentClass.getAnnotation(javax.persistence.Entity.class);	
	}
	
	protected void checkEntity(Class<?> persistentClass){
		Assert.isTrue(isEntity(persistentClass),persistentClass+" 如果是实体类型请使用@Entity注解进行标识");	
	}
}