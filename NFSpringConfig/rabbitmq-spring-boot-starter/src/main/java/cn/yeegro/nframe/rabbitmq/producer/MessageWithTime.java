package cn.yeegro.nframe.rabbitmq.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageWithTime {

    private long id;
    private long time;
    private Object message;
}
