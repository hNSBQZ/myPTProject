package org.pt.exception;

public class RegisterException extends Exception{

    public RegisterException() {
        super();
    }

    // 带错误信息的构造方法
    public RegisterException(String message) {
        super(message);
    }

}
