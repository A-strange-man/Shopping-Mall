package com.example.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CaoJing
 * @date 2021/07/26 15:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonData {
    // 状态码
    private Integer code;

    // 业务数据
    private Object data;

    // 信息描述
    private String msg;

    public static JsonData buildSuccess() {
        return new JsonData(0, null, "success");
    }

    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, "success");
    }

    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    public static JsonData buildError(Integer code, String msg) {
        return new JsonData(code, null, msg);
    }
}
