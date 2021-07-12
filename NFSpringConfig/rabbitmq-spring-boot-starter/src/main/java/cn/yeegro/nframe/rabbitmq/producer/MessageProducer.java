package cn.yeegro.nframe.rabbitmq.producer;

import cn.yeegro.nframe.rabbitmq.common.DetailResponse;

public interface MessageProducer {


    DetailResponse send(Object message);

}