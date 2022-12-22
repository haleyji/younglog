package com.younglog.service;

import com.younglog.domain.Post;
import com.younglog.repository.PostRespository;
import com.younglog.request.PostCreate;
import com.younglog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRespository postRespository;

    public void write(PostCreate postCreate){
        //postCreate -> entity 형태로 변환 필요

//        이런식으로 오픈되어 있는것 비추
//        Post post = new Post();
//        post.title = postCreate.getTitle();
//        post.content = postCreate.getContent();
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRespository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRespository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postResponse;
    }
}
