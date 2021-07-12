package cn.yeegro.nframe.rabbitmq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.yeegro.nframe.rabbitmq.producer.FastBuildRabbitMqProducer;

/**
 * @author Coder编程
 * @version V1.0
 * @Title: RabbitMQAutoConfigure
 * @Package: cn.yeegro.nframe.rabbitmq.config
 * @Description: TODO
 * @date 2019/8/25  21:03
 **/

@Configuration
@ConditionalOnClass(FastBuildRabbitMqProducer.class)
@EnableConfigurationProperties(RabbitMQProperties.class)
public class RabbitMQAutoConfigure {


    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMQProperties.getAddresses());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMQProperties.getVirtualHost());
        connectionFactory.setPublisherConfirms(rabbitMQProperties.isPublisherConfirms());
        return connectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ocp.fast.rabbitmq", value = "enalbe", havingValue = "true")
    public FastBuildRabbitMqProducer fastRabbitMQProducer(ConnectionFactory connectionFactory){
        return new FastBuildRabbitMqProducer(connectionFactory);
    }
}
