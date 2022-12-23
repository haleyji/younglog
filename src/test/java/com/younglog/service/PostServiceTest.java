package com.younglog.service;

import com.younglog.domain.Post;
import com.younglog.repository.PostRepository;
import com.younglog.request.PostCreate;
import com.younglog.request.PostSearch;
import com.younglog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

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

        Assertions.assertEquals(1L, postRepository.count());

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
        postRepository.save(post);

        PostResponse response = postService.get(post.getId());
        //when

        assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
        //then

    }


    @Test
    @Transactional
    @DisplayName("글 여러개 조회")
    public void test3(){
        List<Post> requestPosts = IntStream.range(0, 20).mapToObj(i ->
                Post.builder()
                    .title("younglog-"+i)
                    .content("younglog contents-"+i)
                    .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder().page(1).build();
        //sql -> select, limit, offset
        List<PostResponse> posts = postService.getAll(postSearch);

        assertEquals(10L, posts.size());
        assertEquals("younglog-19",posts.get(0).getTitle());
    }

    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}