package cn.yeegro.nframe.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: [zhangzhiguang]
 * @Date: [2018-08-01 23:39]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {


    private static final long serialVersionUID = -4696008537295855861L;
    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

}
