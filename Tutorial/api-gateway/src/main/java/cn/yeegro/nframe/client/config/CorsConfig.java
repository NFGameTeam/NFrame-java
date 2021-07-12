package cn.yeegro.nframe.client.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: [zhangzhiguang]
 * @Date: [2018-07-30 21:48]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Configuration
public class CorsConfig {


    /**
     * 跨域支持
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // #允许向该服务器提交请求的URI，*表示全部允许
        config.addAllowedOrigin(CorsConfiguration.ALL);
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", config);
        registration.setFilter(new CorsFilter(source));
        registration.addUrlPatterns("/*");
        registration.setName("corsFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
