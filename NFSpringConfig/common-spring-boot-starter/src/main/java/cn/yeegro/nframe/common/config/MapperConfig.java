package cn.yeegro.nframe.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * dto pojo 互转


 */
@Configuration
public class MapperConfig {
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
