package cn.yeegro.nframe.rabbitmq.producer;

import cn.yeegro.nframe.rabbitmq.common.Constants;
import cn.yeegro.nframe.rabbitmq.common.DetailResponse;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class RabbitMQBuildMessageProducer {


    @Autowired
    private ConnectionFactory connectionFactory;


    public MessageProducer buildMessageSender(final String exchange, final String routingKey, final String queue) throws IOException {
        return buildMessageSender(exchange, routingKey, queue, Constants.DIRECT_TYPE);
    }

    public MessageProducer buildTopicMessageSender(final String exchange, final String routingKey) throws IOException {
        return buildMessageSender(exchange, routingKey, null, Constants.TOPIC_TYPE);
    }

    public MessageProducer buildMessageSender(final String exchange, final String routingKey,
                                            final String queue, final String type) throws IOException {

        final Connection connection = connectionFactory.createConnection();
        if (type.equals(Constants.DIRECT_TYPE)) {
            buildQueue(exchange, routingKey, queue, connection, Constants.DIRECT_TYPE);
        } else if (type.equals(Constants.TOPIC_TYPE)) {
            buildTopic(exchange, connection);
        }

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);
        //设置message序列化方法
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        //回调函数: confirm确认 设置发送确认
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                //可以进行日志记录、异常处理、补偿处理等
                log.info("send message failed: " + cause + correlationData.toString());
            } else {
                //TODO 更新数据库，可靠性投递机制
            }
        });

        //回调函数: return返回
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
            log.info("send message failed: " + replyCode + " " + replyText);
            rabbitTemplate.send(message);
        });



        return new MessageProducer() {
            @Override
            public DetailResponse send(Object message) {
                return send(message);
            }
        };
    }


    private void buildQueue(String exchange, String routingKey,
                            final String queue, Connection connection, String type) throws IOException {
        Channel channel = connection.createChannel(false);

        if (type.equals(Constants.DIRECT_TYPE)) {
            channel.exchangeDeclare(exchange, Constants.DIRECT_TYPE, true, false, null);
        } else if (type.equals(Constants.TOPIC_TYPE)) {
            channel.exchangeDeclare(exchange, Constants.TOPIC_TYPE, true, false, null);
        }

        channel.queueDeclare(queue, true, false, false, null);
        channel.queueBind(queue, exchange, routingKey);

        try {
            channel.close();
        } catch (TimeoutException e) {
            log.info("close channel time out ", e);
        }
    }

    private void buildTopic(String exchange, Connection connection) throws IOException {
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare(exchange, Constants.TOPIC_TYPE, true, false, null);
    }
}
