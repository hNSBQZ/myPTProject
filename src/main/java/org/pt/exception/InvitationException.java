package org.pt.exception;


public class InvitationException extends Exception{

    public InvitationException() {
        super();
    }

    // 带错误信息的构造方法
    public InvitationException(String message) {
        super(message);
    }

}
