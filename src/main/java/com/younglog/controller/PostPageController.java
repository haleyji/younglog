package com.younglog.controller;

import com.younglog.domain.Post;
import com.younglog.repository.PostRepository;
import com.younglog.request.PostSearch;
import com.younglog.response.PostResponse;
import com.younglog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostPageController {

    private final PostService postService;


    @GetMapping("/post")
    public ModelAndView listPage(@RequestParam(required = false) PostSearch postSearch){
        ModelAndView model = new ModelAndView();
        model.setViewName("post");

        if (postSearch == null) {
            postSearch = PostSearch.builder().build();
        }
        List<PostResponse> posts = postService.getAll(postSearch);
        model.addObject("list", posts);



        return model;
    }

    @GetMapping("/write")
    public ModelAndView writePage(){
        ModelAndView model = new ModelAndView();
        model.setViewName("write");
        return model;
    }
}
