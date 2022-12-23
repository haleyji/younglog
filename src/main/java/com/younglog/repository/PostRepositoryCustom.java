package com.younglog.repository;

import com.younglog.domain.Post;
import com.younglog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
