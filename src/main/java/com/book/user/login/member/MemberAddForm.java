package com.book.user.login.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberAddForm {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;
}
