package cn.yeegro.nframe.uaa.server.constant;

public class SecurityConstant {
	/**
	 * 定义auth-server中src/main/view/static/login.html 登录页面
	 */
	public static final String LOGIN_PAGE = "/login.html";
	/**
	 * login.html中form 表单提交路径 action="/user/login"
	 * 绑定UsernamePasswordAuthenticationFilter过滤url
	 */
	public static final String LOGIN_PROCESSING_URL = "/user/login";
}
