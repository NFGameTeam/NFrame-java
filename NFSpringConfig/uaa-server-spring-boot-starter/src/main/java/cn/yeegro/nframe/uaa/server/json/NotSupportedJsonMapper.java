package cn.yeegro.nframe.uaa.server.json;

public   class NotSupportedJsonMapper implements  JsonMapper {
	@Override
	public String write(Object input) throws Exception {
		throw new UnsupportedOperationException(
				"Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
	}

	@Override
	public <T> T read(String input, Class<T> type) throws Exception {
		throw new UnsupportedOperationException(
				"Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
	}
}