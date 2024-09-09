package com.book.history.service;

import com.book.model.History;
import com.book.model.Member;
import com.book.history.repository.HistoryRepository;
import com.book.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveHistory(Member loginMember, long bookId) {
        List<History> historyList = historyRepository.findHistoriesByMemberIdAndItemId(loginMember.getId(), bookId);
        Member member = memberRepository.getById(loginMember.getId());
        if (historyList.isEmpty()) {
            if (member.getId() != 0) {
                History history = History.createHistory(bookId, member);
                historyRepository.save(history);
                //log.debug("save history sucess itemId: "+bookId+"loginId: "+loginMember.getId());
            }
        }
    }
}
