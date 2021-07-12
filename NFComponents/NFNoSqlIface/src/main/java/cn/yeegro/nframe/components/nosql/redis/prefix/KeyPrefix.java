package cn.yeegro.nframe.components.nosql.redis.prefix;

public interface KeyPrefix {
		
	public int expireSeconds();
	
	public String getPrefix();
	
}
