package com.noahframe.nfcore.iface.exception;

/**
 * @Author:zoocee
 * @Date:2019/1/24 20:17
 */
public class NFDBException extends RuntimeException {

    private String msg;

    public NFDBException(String msg){
        super(msg);
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
