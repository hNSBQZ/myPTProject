package org.pt.exception;

import lombok.extern.slf4j.Slf4j;
import org.pt.components.MsgEnum;
import org.pt.components.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public Response<?> ErrorHandler(LoginException e) {
        return Response.error(e.getMessage(), MsgEnum.LOGIN_ERROR.getCode());
    }

    @ExceptionHandler(InvitationException.class)
    public Response<?> ErrorHandler(InvitationException e) {
        return Response.error(e.getMessage(), MsgEnum.INVITATION_ERROR.getCode());
    }

    @ExceptionHandler(RegisterException.class)
    public Response<?> ErrorHandler(RegisterException e) {
        return Response.error(e.getMessage(), MsgEnum.IREGISTER_ERROR.getCode());
    }

    @ExceptionHandler(UserException.class)
    public Response<?> ErrorHandler(UserException e) {
        return Response.error(e.getMessage(), MsgEnum.IREGISTER_ERROR.getCode());
    }

}
