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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 实体查询执行器  
 * 
 * @author wangjie (https://github.com/wj596)
 * @date 2016年6月24日 
 */
public class CountDelegate extends AbstractDelegate<Integer> {

	private final String sql;
	private final Object[] parameters;
	private String querySql;
	
	public CountDelegate(JdbcTemplate jdbcTemplate,String sql,Object[] parameters) {
		super(jdbcTemplate);
		this.sql = sql;
		this.parameters = parameters;
	}

	@Override
	public void prepare() {
		if (!this.sql.startsWith("SELECT COUNT")){
			String countRexp = "(?i)^select (?:(?!select|from)[\\s\\S])*(\\(select (?:(?!from)[\\s\\S])* from [^\\)]*\\)(?:(?!select|from)[^\\(])*)*from";
			String replacement = "SELECT COUNT(1) AS COUNT FROM";
			this.querySql = this.sql.replaceFirst(countRexp, replacement);
		} else {
			this.querySql = this.sql;
		}
	}

	@Override
	protected Integer doExecute() throws DataAccessException{
		if(null==this.parameters||this.parameters.length==0){
			return this.jdbcTemplate.queryForObject(this.querySql,Integer.class);
		} else {
			return this.jdbcTemplate.queryForObject(this.querySql, this.parameters,Integer.class);
		}
	}
}