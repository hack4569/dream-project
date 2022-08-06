package com.book.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendVO {
	private String itemId;
	private String approval;
	private String userId;
	private String brief;
	private String detail;
}
