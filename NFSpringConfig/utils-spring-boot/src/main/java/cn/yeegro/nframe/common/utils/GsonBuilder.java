package cn.yeegro.nframe.common.utils;

import com.google.gson.Gson;

public class GsonBuilder {
 
	private final static Gson GSON = new Gson();
	
	public static Gson build(){
		return GSON ;
	}
	
}
