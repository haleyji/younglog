package com.younglog.response;

import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
@Getter
public class SessionResponse {

    private final String accessToken;

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
