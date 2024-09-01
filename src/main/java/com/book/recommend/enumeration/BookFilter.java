package com.book.recommend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum BookFilter {

    Default("default", "default");

    private String name;
    private String value;
}
