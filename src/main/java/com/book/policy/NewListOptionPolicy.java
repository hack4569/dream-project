package com.book.policy;

public class NewListOptionPolicy implements ListOptionPolicy{
    @Override
    public int listCount() {
        return 10;
    }
}
