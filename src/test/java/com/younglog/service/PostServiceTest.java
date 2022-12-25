package com.younglog.service;

import com.younglog.domain.Post;
import com.younglog.exception.PostNotFound;
import com.younglog.repository.PostRepository;
import com.younglog.request.PostCreate;
import com.younglog.request.PostEdit;
import com.younglog.request.PostSearch;
import com.younglog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
@Transactional
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void deleteAll(){
        postRepository.deleteAll();
    }
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

        assertEquals(1L, postRepository.count());

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

    @Test
    @Transactional
    @DisplayName("게시글 제목 수정")
    void test5(){
        Post post = Post.builder()
                .title("영로그")
                .content("로또일등")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("영로그 수정")
                .content("로또일등")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id "+post.getId()));


        assertEquals("영로그 수정",post.getTitle());
        assertEquals("로또일등",post.getContent());

    }

    @Test
    @Transactional
    @DisplayName("게시글 내용 수정")
    void test6(){
        Post post = Post.builder()
                .title("영로그")
                .content("로또일등")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("영로그")
                .content("연금일등")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id "+post.getId()));


        assertEquals("영로그",post.getTitle());
        assertEquals("연금일등",post.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    @Transactional
    void test7(){
        Post post = Post.builder()
                .title("헤용시")
                .content("블로그 일등 줘라")
                .build();

        postRepository.save(post);

        postService.delete(post.getId());

        assertEquals(0, postRepository.count());
    }
    public Post createPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }


    @Transactional
    @Test
    @DisplayName("글 1개 조회 with Exception")
    public void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        //expected
//        Assertions.assertThrows(IllegalArgumentException.class, () ->
//                postService.get(post.getId() + 1L)
//        ,"존재하지 않는 게시글 입니다.");

//        assertThrows(NullPointerException.class, () ->
//                postService.get(post.getId() + 1L)
//                ,"예외처리가 잘못되었어요");
        assertThrows(PostNotFound.class,
                () -> postService.get(post.getId() + 1L));



    }
}