package cn.yeegro.nframe.rabbitmq.comsumer;


import cn.yeegro.nframe.rabbitmq.common.DetailResponse;

/**
 * Created by littlersmall on 16/5/12.
 */
public interface MessageConsumer {
    DetailResponse consume();
}
