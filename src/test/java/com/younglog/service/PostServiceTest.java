package com.younglog.service;

import com.younglog.domain.Post;
import com.younglog.repository.PostRespository;
import com.younglog.request.PostCreate;
import com.younglog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRespository postRespository;

    @Transactional
    @Test
    @DisplayName("글작성")
    public void test1() throws Exception {
        //given

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니당")
                .content("내용입니당")
                .build();

        postService.write(postCreate);

        Assertions.assertEquals(1L, postRespository.count());

    }

    @Transactional
    @Test
    @DisplayName("글 1개 조회")
    public void test2() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRespository.save(post);

        PostResponse response = postService.get(post.getId());
        //when

        assertNotNull(response);
        assertEquals("foo",response.getTitle());
        assertEquals("foo",response.getContent());
        //then

    }

}