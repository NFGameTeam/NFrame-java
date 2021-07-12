package cn.yeegro.nframe.uaa.server.json;

public class JacksonMapper implements JsonMapper {
	private org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

	@Override
	public String write(Object input) throws Exception {
		return mapper.writeValueAsString(input);
	}

	@Override
	public <T> T read(String input, Class<T> type) throws Exception {
		return mapper.readValue(input, type);
	}
}
