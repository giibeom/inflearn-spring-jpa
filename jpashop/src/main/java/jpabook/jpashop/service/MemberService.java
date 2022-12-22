package jpabook.jpashop.service;

import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(final Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(final Long memberId) {
        return memberRepository.findOne(memberId);
    }


    private void validateDuplicateMember(final Member member) {
        List<Member> findmembers = memberRepository.findByName(member.getName());

        // 이런식으로 검증을 하더라도 동시성 이슈 발생 가능함 (동시에 접근할 경우 exception으로 못막는 현상)
        // 따라서 이런 경우 DB에서도 Unique로 지정해주어 2차 방어를 해주는 것이 좋음
        if (!findmembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
