package com.book.common;

public class IntHolder {

    public Integer value;

    public IntHolder(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
