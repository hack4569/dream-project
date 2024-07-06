package com.book.recommend.bookfilter;

import com.book.aladin.domain.AladinBook;
import com.book.aladin.exception.AladinException;
import com.book.aladin.service.AladinService;
import com.book.category.service.CategoryService;
import com.book.history.repository.HistoryRepository;
import com.book.model.History;
import com.book.recommend.constants.RcmdConst;
import com.book.recommend.dto.BookFilterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Component
@RequiredArgsConstructor
public class BestSellerFilter implements BookFilter {
    private final HistoryRepository historyRepository;
    private final CategoryService categoryService;
    private final AladinService aladinService;

    @Override
    public void customFilteredList(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto )
    {
        try {
            String subCid = ObjectUtils.isEmpty(bookFilterDto.getCategory()) ? "" : bookFilterDto.getCategory().getSubCid();

            HashSet<Integer> cids = new HashSet<>();
            //사용자가 희망하는 카테고리가 있을 경우
            if (StringUtils.hasText(subCid)) {
//                List<Category> list = categoryMapper.getCategoryByParam(bookFilterDto.getCategory());
//                for (Category categoryMap : list) {
//                    String cid = Integer.toString(categoryMap.getCid());
//                    aladinAcceptCategoryList.add(cid);
//                }
            } else {
                cids = categoryService.findCategories();
            }

            List<AladinBook> aladinBestSellerBooks = aladinService.bestSellerList(bookFilterDto).orElseThrow(() -> new AladinException("베스트 상품 조회중 에러가 발생하였습니다."));

            //오늘 날짜
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
            String today = dateFormatter.format(cal.getTime());

            List<History> histories = historyRepository.findHistoryByMemberId(bookFilterDto.getMemberId());

            bookFilterDto.setFinalCids(Optional.ofNullable(cids));
            bookFilterDto.setAnchorDate(Optional.ofNullable(today));
            bookFilterDto.setHistories(Optional.ofNullable(histories));

            //필터1 : 허용 카테고리만
            aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(i -> bookFilterDto.getFinalCids().orElse(new HashSet<>()).contains(i.getCategoryId())).collect(Collectors.toList());

            //필터2 : 1년도 안된 책 out
            aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(i -> Integer.parseInt(bookFilterDto.getAnchorDate().orElse("0") ) > Integer.parseInt(this.getCustomDate(i.getPubDate()))).collect(Collectors.toList());

            //필터3 : history에 없는 데이터
            for (History history : bookFilterDto.getHistories().orElseGet(() -> new ArrayList<>())) {
                aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(bB ->
                {
                    return
                            (
                                    bB.getItemId() == history.getItemId()
                                            &&
                                            LocalDate.now().isEqual(history.getCreated().toLocalDate())
                            )
                                    || bB.getItemId() != history.getItemId();
                }).collect(Collectors.toList());
            }

            aladinBooks.addAll(aladinBestSellerBooks);

            if (aladinBooks.size() < RcmdConst.SHOW_BOOKS_COUNT) {
                bookFilterDto.setStartIdx(bookFilterDto.getStartIdx() + 1);
                this.customFilteredList(aladinBooks, bookFilterDto);
            }
        } catch (AladinException ae) {
            throw ae;
        } catch (Exception e) {
            log.error("customFilteredList Error: {}", e);
            throw new AladinException(e.getMessage(), e);
        }
    }

    private String getCustomDate(String yyyymmdd) {
        int year = Integer.parseInt(yyyymmdd.substring(0, 4));
        int month = Integer.parseInt(yyyymmdd.substring(5, 7));
        int date = Integer.parseInt(yyyymmdd.substring(8, 10));

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        return dateFormatter.format(cal.getTime());
    }
}
