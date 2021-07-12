package cn.yeegro.nframe.rabbitmq.producer;

import cn.yeegro.nframe.rabbitmq.common.DetailResponse;

public interface MessageSender {


    DetailResponse send(Object message);

    DetailResponse send(MessageWithTime messageWithTime);
}