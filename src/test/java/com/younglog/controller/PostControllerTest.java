package com.younglog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.younglog.domain.Post;
import com.younglog.repository.PostRepository;
import com.younglog.request.PostCreate;
import com.younglog.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest//전반적인 test -> mockMvc autowired안됨
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("/posts 요청시 title 값은 필수다")
    void test2() throws Exception {
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("/posts 요청시 DB에 값이 저장된다")
    void test3() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .header("authorization","younglog"))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals(post.getTitle(), "제목입니다");
    }

    @Test
    @Transactional
    @DisplayName("글 1개 조회")
    public void test4() throws Exception {
        //given
        //title 글자수를 10자로 제한해달라는 요청
        Post post = Post.builder()
                .title("foofoofooofoofooofoofooofooofooofoo")
                .content("bar")
                .build();
        postRepository.save(post);
        //expect

        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foofoofooo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
        //then


    }

    @Test
    @Transactional
    @DisplayName("글 여러개 조회")
    public void test5() throws Exception {

        List<Post> requestPosts = IntStream.range(1, 31).mapToObj(i ->
                        Post.builder()
                                .title("younglog-" + i)
                                .content("younglog contents-" + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //expect

        mockMvc.perform(get("/posts/all?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
//                .andExpect(jsonPath("$.[0].id", is(30)))
//                .andExpect(jsonPath("$.[0].title").value("younglog-30"))
//                .andExpect(jsonPath("$.[0].content").value("younglog contents-30"))
                .andDo(print());
        //then


    }

    @Test
    @Transactional
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
    public void test6() throws Exception {

        List<Post> requestPosts = IntStream.range(1, 31).mapToObj(i ->
                        Post.builder()
                                .title("younglog-" + i)
                                .content("younglog contents-" + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //expect

        mockMvc.perform(get("/posts/all?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(10)))
//                .andExpect(jsonPath("$.[0].id", is(30)))
//                .andExpect(jsonPath("$.[0].title").value("younglog-30"))
//                .andExpect(jsonPath("$.[0].content").value("younglog contents-30"))
                .andDo(print());
        //then


    }

    @Test
    @Transactional
    @DisplayName("게시글 제목 수정")
    public void test7() throws Exception {

        Post post = Post.builder().title("영로그").content("로또일등").build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("영로그 수정").content("연금일등").build();
        //expect

        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("영로그 수정"))
                .andDo(print());


    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제")
    public void test8() throws Exception {

        Post post = Post.builder().title("영로그").content("로또일등").build();

        postRepository.save(post);


        //expect

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        mockMvc.perform(get("/posts/{postId}", 1L)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Post.builder()
                                        .title("수정해보라고")
                                        .content("내용인디")
                                        .build())))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("게시글 제목에 바보는 포함될 수 없다")
    void test11() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("바보바보")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }
}