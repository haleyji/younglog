package com.younglog.controller;

import com.younglog.domain.Post;
import com.younglog.exception.InvalidRequest;
import com.younglog.request.PostCreate;
import com.younglog.request.PostEdit;
import com.younglog.request.PostSearch;
import com.younglog.response.PostResponse;
import com.younglog.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor

public class PostController {
//SSR -> jsp, thymeleaf, mustache, freemarker
//SPA -> vue, nuxt(vue+SSR)
//    -> react, next

    private final PostService postService;

    @GetMapping("/posts")
    public String get() {
        return "hello world";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        //service -> repository
        if (request.getTitle().contains("바보")) {
            throw new InvalidRequest();
        }
            //case 1. 저장한 데이터 entity,
            //case 2. 저장한 데이터 primary_id -> client 에서는 수신한 id 를 가지고 글 조회 API 를 통해서 데이터를 수신받음
            //case 3. 응답 필요 없음
            //Bad case: 서버에서 응답은 이렇게 할거다 라고 fix 해버리는 경우 (서버에서는 유연하게 대응하는 것이 좋다)
            postService.write(request);

        //return Map.of("postId",postId)
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        PostResponse post = postService.get(id);
        return post;
    }

    @GetMapping(value = "/posts/all",produces = "application/json;charset=utf-8;")
    public List<PostResponse> getAll(PostSearch postSearch) {
        return postService.getAll(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
        return postService.get(postId);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }


}
