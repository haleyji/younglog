package com.younglog.exception;
//status -> 404

public class PostNotFound extends YounglogException {
    private static final String MESSAGE = "존재하지 않는 글입니다";
    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
