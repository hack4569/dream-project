package com.book.recommend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringEscapeUtils;

@Getter
@Setter
@Builder
public class RecommendCommentDto {
    private long commentId;
    private String type;
    private String content;//fulldescription, phrase
    private String originContent;
    public String getContent() {
        return StringEscapeUtils.unescapeHtml(content);
    }

    public String getOriginContent() {
        return StringEscapeUtils.unescapeHtml(originContent);
    }
}
