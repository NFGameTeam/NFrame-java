package cn.yeegro.nframe.common.hystrix;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**


 */
@Configuration
public class HystrixConcurrencyStrategyConfig {
 
	@Bean
	public RequestAttributeHystrixConcurrencyStrategy requestAttributeHystrixConcurrencyStrategy() {
		return new RequestAttributeHystrixConcurrencyStrategy();
	}
	
}
