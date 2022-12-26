package jpabook.jpashop.service;

import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/* 테스트 요구사항
    - 회원가입을 성공해야 한다.
    - 회원가입 할 때 같은 이름이 있으면 예외가 발생해야 한다.
*/
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 회원_가입은 {

        @Nested
        @DisplayName("정상적인 회원 정보가 주어지면")
        class Context_with_valid_member {
            private Member member;

            @BeforeEach
            void setUp() {
                member = new Member();
                member.setName("Alex");
            }

            @Test
            @DisplayName("회원을 생성한다")
            void it_save_member() throws Exception {
                Long saveId = memberService.join(member);

                /*  @Transactional로 인해 테스트 종료 후 rollback이 진행되기 전에 flush
                    - flush를 하게 되면 실제 JPA 영속성 컨택스트에서 insert 쿼리가 실행됨 (로그에 보임)
                    - flush : 쓰기 지연 저장소에 쌓아놨던 DDL(INSERT, UPDATE, DELETE) SQL들이 DB에 날라감
                    - commit은 아니므로 테스트 종료되면 rollback 됨
                */
                em.flush(); // 매 테스트 실행마다 해당 작업을 수행하도록 하면 편하게 로그 체킹 가능할 듯 함

                assertThat(member).isEqualTo(memberRepository.findOne(saveId));
            }
        }

        @Nested
        @DisplayName("중복된 회원 정보가 주어지면")
        class Context_with_duplicate_member {
            private Member member1;
            private Member member2;

            @BeforeEach
            void setUp() {
                member1 = new Member();
                member1.setName("Alex");

                member2 = new Member();
                member2.setName("Alex");

                memberService.join(member1);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_returns_exception() throws Exception {
                assertThatThrownBy(
                        () -> memberService.join(member2)
                )
                        .as("IllegalStateException이 발생해야 한다")
                        .isInstanceOf(IllegalStateException.class);
            }
        }
    }
}
