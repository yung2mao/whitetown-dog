package cn.whitetown.dogbase.common.exception;

import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;

/**
 * 自定义通用异常类
 *
 * @author taixian
 */
public class WhResException extends RuntimeException{
    private ResponseStatusEnum statusEnum;
    private String message;
    public WhResException(ResponseStatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }
    public WhResException(ResponseStatusEnum statusEnum, String message) {
        this.statusEnum = statusEnum;
        this.message = message;
    }

    public WhResException(){};

    public WhResException(String message) {
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
