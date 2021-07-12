package cn.yeegro.nframe.rabbitmq.producer;



import cn.yeegro.nframe.rabbitmq.common.DetailResponse;


public interface MessageProcess<T> {
    DetailResponse process(T message);
}
