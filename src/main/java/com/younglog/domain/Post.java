package com.younglog.domain;

import com.younglog.request.PostEdit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Lob//long text 형태로 db에 저장되도록
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle(){
        return this.title;
        // 이런식으로 서비스 정책에 관련해서는 만들지 말것
        // return this.title.substring(0, 10);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public void change(String title, String content) {
//          파라미터 순서가 바뀌거나, 파라미터 갯수가 늘어날 가능성 있음 -> 에러 발생
//        this.title = title;
//        this.content = content;
//    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder().title(title).content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
