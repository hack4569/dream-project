package com.book.user.dto;

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
