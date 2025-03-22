package org.pt.exception;

public class LoginException extends Exception{

    public LoginException() {
        super();
    }

    // 带错误信息的构造方法
    public LoginException(String message) {
        super(message);
    }

}
