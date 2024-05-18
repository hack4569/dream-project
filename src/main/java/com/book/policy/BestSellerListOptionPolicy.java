package com.book.policy;

import org.springframework.stereotype.Component;

@Component
public class BestSellerListOptionPolicy implements ListOptionPolicy{
    @Override
    public int listCount() {
        return 6;
    }
}
