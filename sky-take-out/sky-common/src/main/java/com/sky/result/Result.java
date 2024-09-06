package com.sky.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */

@ApiModel(description = "同一反回的结果类")
@Data
public class Result<T> implements Serializable {


    @ApiModelProperty("成功失败的代码")
    private Integer code; //编码：1成功，0和其它数字为失败
    @ApiModelProperty("message")
    private String msg; //错误信息
    @ApiModelProperty("放回的数据")
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
