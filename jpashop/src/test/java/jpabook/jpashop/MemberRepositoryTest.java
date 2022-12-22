package jpabook.jpashop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원을 저장할 때")
    class Context_with_save_member {

        @Test
        @DisplayName("주어진 값을 저장하고 id를 리턴한다")
        @Transactional
        @Rollback(false)
        void it_() throws Exception {
            //given
            Member member = new Member();
            member.setUsername("Alex");

            //when
            Long memberId = memberRepository.save(member);
            Member findMember = memberRepository.find(memberId);

            //then
            assertThat(memberId).isNotNull()
                    .isEqualTo(findMember.getId());

            assertThat(member).isEqualTo(findMember);
            assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        }
    }
}
