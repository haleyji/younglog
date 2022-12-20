package com.younglog.request;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {
    @NotNull
    @NotBlank(message = "타이틀을 입력해주세요")
    public String title;
    @NotNull
    @NotBlank(message = "내용을 입력해주세요")
    public String content;
}
