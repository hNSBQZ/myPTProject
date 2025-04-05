package org.pt.exception;


public class UserException extends Exception{

    public UserException() {
        super();
    }

    // 带错误信息的构造方法
    public UserException(String message) {
        super(message);
    }

}
