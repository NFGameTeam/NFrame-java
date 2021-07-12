package cn.yeegro.nframe.common.web;

public class StorageResult <T> {


    /**
     * code码，成功返回0
     */
    private int code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据对象
     */
    private T data;

    public StorageResult(int code) {
        super();
        this.code = code;
    }

    public StorageResult(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public StorageResult(int code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

}
