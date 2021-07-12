package cn.yeegro.nframe.common.utils;

import java.io.InputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import lombok.extern.slf4j.Slf4j;

/**
 * XSS 工具类， 用于过滤特殊字符
 */
@Slf4j
public class XssUtils {
	private static final String ANTISAMY_SLASHDOT_XML = "antisamy-ebay.xml";
	// AntiSamy使用的策略文件
	private static Policy policy = null;

	static {
		log.trace(" start read XSS configfile [" + ANTISAMY_SLASHDOT_XML + "]");
		InputStream inputStream = XssUtils.class.getClassLoader().getResourceAsStream(ANTISAMY_SLASHDOT_XML);

		try {
			policy = Policy.getInstance(inputStream);
			log.trace("read XSS configfile [" + ANTISAMY_SLASHDOT_XML + "] success");
		} catch (PolicyException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * @desc AntiSamy清洗数据
	 */
	public static String cleanXSS(String taintedHTML) {
		try {
			AntiSamy antiSamy = new AntiSamy();
			final CleanResults cr = antiSamy.scan(taintedHTML, policy);
			// AntiSamy会把“&nbsp;”转换成乱码，把双引号转换成"&quot;"
			// 先将&nbsp;的乱码替换为空，双引号的乱码替换为双引号
			String str = StringEscapeUtils.unescapeHtml3(cr.getCleanHTML())  ;
			str = str.replaceAll(antiSamy.scan("&nbsp;", policy).getCleanHTML(), "");
			str = str.replaceAll(antiSamy.scan("\"", policy).getCleanHTML(), "\"");
			return str;

		} catch (ScanException | PolicyException e) {
			log.error(e.getMessage());
		}
		return taintedHTML;
	}

	public static void main(String[] args) {
		String xsshtml = "hyf<script>alert(1)";
		System.out.println(XssUtils.cleanXSS(xsshtml));

	}
}
