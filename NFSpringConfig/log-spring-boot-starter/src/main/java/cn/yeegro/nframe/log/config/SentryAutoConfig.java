//package cn.yeegro.nframe.log.config;
//
//import javax.annotation.PreDestroy;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//
//import io.sentry.Sentry;
//import io.sentry.SentryClient;
//
//public class SentryAutoConfig {
//	
//	@Value("${spring.application.name:NA}")
//    private String appName;
//	
//	 
//	@Bean
//	public SentryClient sentryClient() {
//
//		SentryClient sentryClient = Sentry.init("https://de5f1886771945d7a40a308b2525c858@o413571.ingest.sentry.io/5300354");
//		sentryClient.setEnvironment("Default");
//		sentryClient.addTag("service", appName);
//		
//		return sentryClient;
//	}
//	
//	@PreDestroy
//    public void destroy() {
//        sentryClient().closeConnection();
//    }
//	
//	 
//	
//}
