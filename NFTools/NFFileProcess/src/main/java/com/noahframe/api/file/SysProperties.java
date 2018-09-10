package com.noahframe.api.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SysProperties {

	
	private static java.util.Properties _pro_                  = new java.util.Properties();

	
	private Map<String, String> params=new HashMap<String, String>();
	
	public SysProperties(String name)
	{
		String properties=name+".properties";
		InputStream in = SysProperties.class.getResourceAsStream("/"+properties);
		try {
			_pro_.load(in);
			Enumeration<?> enu = _pro_.propertyNames();  
			 while (enu.hasMoreElements()) { 
				 String key = (String)enu.nextElement();
				 String value=_pro_.getProperty(key).trim();
				 params.put(key, value);
			 }
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getParams(String key){
		return params.get(key);
	}
	
}
