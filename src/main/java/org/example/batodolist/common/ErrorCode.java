package org.example.batodolist.common;

public enum ErrorCode {
    NOT_FOUND("001","ENTITY_NOT_FOUND",404),
    SUCCESS("002", "SUCCESS", 200),;
    public final String code;

    public final String message;

    public final Integer status;

    ErrorCode(String code, String message, Integer status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
