package com.younglog.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {
    //수정할수있는 항목에 대해서만 필드생성
    private final String title;
    private final String content;

    @Builder

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
