package cn.yeegro.nframe.uaa.server.json;

public  class Jackson2Mapper implements  JsonMapper {
	private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

	@Override
	public String write(Object input) throws Exception {
		return mapper.writeValueAsString(input);
	}

	@Override
	public <T> T read(String input, Class<T> type) throws Exception {
		return mapper.readValue(input, type);
	}
}
