package com.book.batch;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.service.AladinService;
import com.book.category.dto.CategoryDto;
import com.book.category.service.CategoryService;
import com.book.model.Category;
import com.book.model.Member;
import com.book.myaladin.repository.AladinBookRepository;
import com.book.recommend.dto.BookFilterDto;
import com.book.recommend.dto.RecommendParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.book.recommend.service.RecommendService;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookBatchScheduler {
    private final AladinService aladinService;
    private final RecommendService recommendService;
    private final CategoryService categoryService;
    private final AladinBookRepository aladinBookRepository;

    //@Scheduled(cron = "0 */1 * * * ?")
    //@Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    public void scheduleBestSellerBatch() {
        log.info("베스트셀러 배치 작업 시작");
        try {
            List<Category> categories = categoryService.findCategories();
            HashSet<Integer> cids = categories.stream().map(category -> category.getCid()).collect(Collectors.toCollection(HashSet::new));

            var recommendParam = RecommendParam.builder().cids(cids).build();
            recommendParam.setCategoryDto(new CategoryDto());
            recommendParam.setHistories(new ArrayList<>());
            recommendParam.setMember(new Member());
            BookFilterDto bookFilterDto = recommendService.createBookFilterDto(recommendParam);
            bookFilterDto.setShowBooksCount(10);
            List<AladinBook> customFilteredBooks = new ArrayList<>();
            int customFilteredBooksCount = 0;
            while (customFilteredBooksCount < 5) {
                recommendService.customFilteredListBatch(customFilteredBooks, bookFilterDto);
                if (customFilteredBooks.size() == 0) {
                    customFilteredBooksCount ++;
                    continue;
                }

                List<Integer> alreadyItemIds = new ArrayList<>();
                if (!ObjectUtils.isEmpty(customFilteredBooks)) {
                    var itemIds = customFilteredBooks.stream().map(AladinBook::getItemId).collect(Collectors.toList());
                    var alreadyItemList = aladinBookRepository.findAllByItemIdIn(itemIds);

                    if (!ObjectUtils.isEmpty(alreadyItemList)) {
                        alreadyItemIds =  alreadyItemList.stream().map(AladinBook::getItemId).collect(Collectors.toList());
                    }
                    List<Integer> finalAlreadyItemIds = alreadyItemIds;
                    var newItems = customFilteredBooks.stream().filter(newItem -> !finalAlreadyItemIds.contains(newItem.getItemId())).collect(Collectors.toList());
                    newItems.forEach( aladinBook -> {
                        aladinBook.setBookCommentList(recommendService.saveBookDetail(aladinBook));
                    });
                    aladinBookRepository.saveAll(newItems);

                    //초기화
                    customFilteredBooks = new ArrayList<>();
                }

                log.info("customFilteredBooks : {}", customFilteredBooks);
            }

        } catch (Exception e) {
            log.error("batch error : {}", e.getMessage(), e);
        }
    }
}
