package cn.yeegro.nframe.rabbitmq.enumcode;


public enum ConsumerResultEnum {

    SEND(0,"收到消息,未确认"),
    SUCCESS(1,"收到消息，确认消费成功"),
    FAIL(2,"收到消息，确认消费失败");

    private Integer code;
    private String desc;

    ConsumerResultEnum(Integer code, String desc){

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
