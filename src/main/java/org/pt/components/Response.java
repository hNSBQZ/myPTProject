package org.pt.components;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Han
 */
@Data
public class Response<T> {
    private Integer status; // 编码：1成功，0和其它数字为失败
    private String msg; // 错误信息
    private T data; // 数据

    public static <T> Response<T> success(T object) {
        Response<T> r = new Response<T>();
        r.data = object;
        r.status = 1;
        r.msg="success";
        return r;
    }

    public static <T> Response<T> error(String msg,Integer status) {
        var r = new Response();
        r.msg = msg;
        r.status = status;
        return r;
    }

}
