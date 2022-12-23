package com.younglog.service;

import com.younglog.domain.Post;
import com.younglog.repository.PostRepository;
import com.younglog.request.PostCreate;
import com.younglog.request.PostSearch;
import com.younglog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();


    }

    //글이 너무 많은 경우 비용이 많이 든다
    public List<PostResponse> getAll(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id")); 수동으로 만든거라서 application.yml 설정이 안먹힘

        return postRepository.getList(postSearch)
                .stream().map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
