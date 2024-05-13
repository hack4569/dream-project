package com.book.aladin.service;

import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.book.BookFilterDto;
import com.book.common.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AladinService {
    @Value("${aladin.host}")
    public String aladinHost;
    public static final String ITEM_LOOKUP = "/ttb/api/ItemLookUp.aspx";

    public static final String ITEM_LIST = "/ttb/api/ItemList.aspx";

    private WebClient aladinApi;

    public AladinService() {
        aladinApi = WebClient.create(aladinHost);
    }

    public List<AladinBook> bestSellerList(BookFilterDto bookFilterDto) {
        aladinApi = WebClient.create(aladinHost);
//        Map<String, Object> map = new HashMap<>();
//        map.put("querytype", "BestSeller");
//        map.put("start", bookFilterDto.getStartIdx());
//        map.put("maxResults", bookFilterDto.getMaxResults());
        ApiParam apiParam = ApiParam.builder()
                .querytype("BestSeller")
                .start(bookFilterDto.getStartIdx())
                .maxResults(bookFilterDto.getMaxResults()).build();

        ResponseEntity<AladinMaster> response = aladinApi
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(ITEM_LIST)
                        .queryParams(apiParam.getApiParamMap())
                        .build()
                )
                .retrieve()
                .toEntity(AladinMaster.class)
                .block();
        AladinMaster aladinMaster = response.getBody();
        return aladinMaster.getItem();
    }
}
