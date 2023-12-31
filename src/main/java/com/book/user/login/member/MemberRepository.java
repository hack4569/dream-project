package com.book.user.login.member;


import com.book.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, QuerydslPredicateExecutor<Member> {
    Optional<Member> findMemberByLoginId(String loginId);
    Optional<Member> findMemberBySessionId(String sessionId);
//	private static Map<Long, Member> store = new HashMap<>();
//	private static long sequence = 0L;
//
//	public Member save(Member member) {
//		member.setId(++sequence);
//		log.info("save:member={)", member);
//		store.put(member.getId(), member);
//		return member;
//	}
//
//	public Optional<Member> findByLoginId(String loginId){
//		List<Member> all = findAll();
//		for(Member m : all) {
//			if(m.getLoginId().equals(loginId)) {
//				return Optional.of(m);
//			}
//		}
//		return Optional.empty();
//
//		//return findAll().stream().filter(m -> m.getLoginId().equals(loginId)).findFirst();
//	}
//
//	public Member findById(Long id) {
//		return store.get(id);
//	}
//
//	public List<Member> findAll(){
//		return new ArrayList<>(store.values());
//	}
}
