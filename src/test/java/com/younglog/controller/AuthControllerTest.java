package com.younglog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.younglog.domain.Session;
import com.younglog.domain.User;
import com.younglog.repository.SessionRepository;
import com.younglog.repository.UserRepository;
import com.younglog.request.Login;
import com.younglog.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    @DisplayName("로그인 성공")
    @Transactional
    public void test() throws Exception {
        userRepository.save(User.builder().name("hyeyoung").email("test@co.kr").password("1111").build());
        //given
        Login login = Login.builder().email("test@co.kr").password("1111").build();
        String json = objectMapper.writeValueAsString(login);
        //when

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("회원가입 후 로그인, 로그인 후 세션 응답")
    public void test2() throws Exception {
        User user = userRepository.save(User.builder().name("hyeyoung").email("test@co.kr").password("1111").build());
        //given
        Login login = Login.builder().email("test@co.kr").password("1111").build();
        String json = objectMapper.writeValueAsString(login);
        //when

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());

        User loginUser = userRepository.findById(user.getId()).orElseThrow(RuntimeException::new);

        //then
        assertEquals(1L, loginUser.getSessions().size());

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속 /foo")
    public void test3() throws Exception {
//given
        User user = User.builder().name("hyeyoung").email("test@co.kr").password("1111").build();
        Session session = user.addSession();
        userRepository.save(user);



        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());

    }


    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다")
    public void test4() throws Exception {
//given
        User user = User.builder().name("younglog").email("test1234@co.kr").password("1111").build();
        Session session = user.addSession();
        userRepository.save(user);



        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken()+"sdlkfjsdhlkfjdslkf389")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());

    }


    @Test
    @DisplayName("회원가입 후 로그인, 로그인 후 쿠키가 발급")
    public void test5() throws Exception {
        User user = userRepository.save(User.builder().name("hyeyoung").email("test@co.kr").password("1111").build());
        //given
        Login login = Login.builder().email("test@co.kr").password("1111").build();
        String json = objectMapper.writeValueAsString(login);
        //when

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("set-cookie"))
                .andExpect(cookie().exists("SESSION"))
     //           .andExpect(cookie().value("SESSION","sdflkjasdflkjsdlkfjdslkf"))
                .andDo(print());

    }

    @Test
    @DisplayName("회원가입 후 로그인 후 쿠키 정보로 인증이 필요한 페이지 요청")
    public void test6() throws Exception {
        User user = userRepository.save(User.builder().name("hyeyoung").email("test@co.kr").password("1111").build());
        //given
        Login login = Login.builder().email("test@co.kr").password("1111").build();
        String accessToken = authService.login(login);
        //return new SessionResponse(accessToken); 이 토큰을 쿠키 헤더에 담아줘야함
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))//한시간 3600ms
                .sameSite("Strict")
                .build();


    }
}