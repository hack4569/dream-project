package com.book.category.dto;

import com.book.model.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CategoryDto {
    private int cid;
    private String subCid;
    private String name;
    private String mall;
    private String depth1;

    @Builder
    public CategoryDto(int cid, String subCid, String name, String mall, String depth1) {
        this.cid = cid;
        this.subCid = subCid;
        this.name = name;
        this.mall = mall;
        this.depth1 = depth1;
    }

    public Category createCategory() {
        return Category.builder()
                .cid(this.getCid())
                .subCid(this.getSubCid())
                .depth1(this.getDepth1())
                .build();
    }
}
