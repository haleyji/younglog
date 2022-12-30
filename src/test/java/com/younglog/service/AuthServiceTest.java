package com.younglog.service;

import com.younglog.domain.User;
import com.younglog.exception.InvalidSigninInformation;
import com.younglog.request.Login;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("로그인 시도")
    public void test1(){
        Login request = Login
                .builder()
                .email("cherish920810@gmail.com")
                .password("1234").build();

//        User user = authService.login(request);
//        Assertions.assertEquals(user.getId(),1L);
    }


    @Test
    @DisplayName("로그인 시도 실패  - 잘못된 비밀번호")
    public void test2(){
        Login request = Login
                .builder()
                .email("cherish920810@gmail.com")
                .password("123421321").build();

        Assertions.assertThrows(InvalidSigninInformation.class, ()->{
            authService.login(request);
        });
    }
}