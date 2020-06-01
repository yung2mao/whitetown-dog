package cn.whitetown.dogbase.exception;

import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;

/**
 * 自定义通用异常处理器
 **/
public class CustomException extends RuntimeException{
    private ResponseStatusEnum statusEnum;
    private String message;
    public CustomException(ResponseStatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }
    public CustomException(ResponseStatusEnum statusEnum, String message) {
        this.statusEnum = statusEnum;
        this.message = message;
    }

    public CustomException(){};

    public CustomException(String message) {
        super(message);
    }

    public ResponseStatusEnum getStatusEnum() {
        return statusEnum;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
