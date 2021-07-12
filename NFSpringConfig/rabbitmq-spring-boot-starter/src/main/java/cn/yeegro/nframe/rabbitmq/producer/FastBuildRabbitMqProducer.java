package cn.yeegro.nframe.rabbitmq.producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import cn.yeegro.nframe.rabbitmq.cache.RetryCache;
import cn.yeegro.nframe.rabbitmq.common.DetailResponse;
import cn.yeegro.nframe.rabbitmq.common.FastOcpRabbitMqConstants;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Coder编程
 * @version V1.0
 * @Title: RabbitMQProducer
 * @Package: cn.yeegro.nframe.rabbitmq.producer
 * @Description: 消息生产者
 * @date 2019/8/25  21:01
 **/

@Slf4j
public class FastBuildRabbitMqProducer {

    private ConnectionFactory connectionFactory;

    public FastBuildRabbitMqProducer(ConnectionFactory connectionFactory){
        this.connectionFactory = connectionFactory;
    }


    public MessageSender buildDirectMessageSender(final String exchange, final String routingKey, final String queue) throws IOException {
        return buildMessageSender(exchange, routingKey, queue, "direct");
    }

    public MessageSender buildTopicMessageSender(final String exchange, final String routingKey) throws IOException {
        return buildMessageSender(exchange, routingKey, null, "topic");
    }

    /**
     * 发送消息
     * @param exchange    消息交换机
     * @param routingKey  消息路由key
     * @param queue       消息队列
     * @param type        消息类型
     * return
     */
    private MessageSender buildMessageSender(final String exchange, final String routingKey, final String queue, final String type)throws IOException {
        Connection connection = connectionFactory.createConnection();
        //1
        if (type.equals("direct")) {
            buildQueue(exchange, routingKey, queue, connection, "direct");
        } else if (type.equals("topic")) {
            buildTopic(exchange, connection);
        }

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        RetryCache retryCache = new RetryCache();

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.info("send message failed: " + cause + correlationData.toString());
            } else {
                retryCache.del(Long.valueOf(correlationData.getId()));
            }
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
            try {
                Thread.sleep(FastOcpRabbitMqConstants.ONE_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("send message failed: " + replyCode + " " + replyText);
            rabbitTemplate.send(message);
        });

        return new MessageSender() {
            {retryCache.setSender(this);}

            @Override
            public DetailResponse send(Object message) {
                long id = retryCache.generateId();
                long time = System.currentTimeMillis();

                return send(new MessageWithTime(id, time, message));
            }

            @Override
            public DetailResponse send(MessageWithTime messageWithTime) {
                try {
                    retryCache.add(messageWithTime);
                    rabbitTemplate.correlationConvertAndSend(messageWithTime.getMessage(),
                            new CorrelationData(String.valueOf(messageWithTime.getId())));
                } catch (Exception e) {
                    return new DetailResponse(false, "","");
                }
                return new DetailResponse(true, "","");
            }
        };
    }




    private void buildQueue(String exchange, String routingKey,
                            final String queue, Connection connection, String type) throws IOException {
        Channel channel = connection.createChannel(false);

        if (type.equals("direct")) {
            channel.exchangeDeclare(exchange, "direct", true, false, null);
        } else if (type.equals("topic")) {
            channel.exchangeDeclare(exchange, "topic", true, false, null);
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
        channel.exchangeDeclare(exchange, "topic", true, false, null);
    }



}
