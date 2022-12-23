package com.younglog.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.younglog.domain.Post;
import com.younglog.domain.QPost;
import com.younglog.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.younglog.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }
}
