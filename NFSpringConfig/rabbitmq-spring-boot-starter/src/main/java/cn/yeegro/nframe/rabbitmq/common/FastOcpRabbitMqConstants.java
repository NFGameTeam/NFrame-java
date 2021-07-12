package cn.yeegro.nframe.rabbitmq.common;

public class FastOcpRabbitMqConstants {
    /**
     * 线程数
     */
    public final static int THREAD_COUNT = 5;

    /**
     * 处理间隔时间
     */
    public final static int INTERVAL_MILS = 0;

    /**
     * consumer失败后等待时间(mils)
     */
    public static final int ONE_SECOND = 1 * 1000;

    /**
     * 异常sleep时间(mils)
     */
    public static final int ONE_MINUTE = 1 * 60 * 1000;
    /**
     * MQ消息retry时间
     */
    public static final int RETRY_TIME_INTERVAL = ONE_MINUTE;
    /**
     * MQ消息有效时间
     */
    public static final int VALID_TIME = ONE_MINUTE;

    /**
     * 主题模式
     */
    public static final String TOPIC_TYPE = "topic";

    /**
     *
     */
    public static final String DIRECT_TYPE = "direct";


    /**
     * 发送消息服务名称
     */
    public final static String SERVER_NAME = "server_name";


    /**
     * 发送消息业务名称
     */
    public final static String MESSAGE_NAME = "message_name";


}