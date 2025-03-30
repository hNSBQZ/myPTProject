package org.pt.components;

public enum MsgEnum {
    SUCCESS(0, "成功"),
    FAIL(-1, "失败"),

    TOKEN_ERROR(301,"TOKEN验证失败"),
    TOKEN_EXPIRE(302,"TOKEN过期"),

    INVITATION_ERROR(401,"邀请人存在"),

    AUTH_ERROR(502, "授权失败!"),
    LOGIN_ERROR(503, "登陆错误"),

    IREGISTER_ERROR(504, "注册错误"),

    DATABASE_OPERATION_FAILED(504, "数据库操作失败");
    private int code;
    private String message;

    MsgEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}

