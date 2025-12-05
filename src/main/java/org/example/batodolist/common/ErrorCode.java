package org.example.batodolist.common;

public enum ErrorCode {
    INVALID_INPUT("400", "Invalid input data", 400),
    VALIDATION_ERROR("400", "Validation failed", 400),
    NOT_FOUND("001","ENTITY_NOT_FOUND",404),
    SUCCESS("002", "SUCCESS", 200),
    WRONG_USER_OR_PASSWORD("400", "WRONG_USER_OR_PASSWORD", 400),
    THIS_PERSON_DOES_NOT_BELONG_TO_THE_PROJECT("400","THIS_PERSON_DOES_NOT_BELONG_TO_THE_PROJECT",400),
    USER_IS_EXISTED("003", "USER_IS_EXISTED", 404);
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
