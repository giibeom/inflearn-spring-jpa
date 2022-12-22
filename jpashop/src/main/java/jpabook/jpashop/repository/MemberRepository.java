package jpabook.jpashop.repository;

import jpabook.jpashop.domain.member.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberRepository {
    // Spring Boot를 사용하면 @PersistenceContext 대신 @Autowired로도 주입이 가능 (Spring Data JPA에서 기능 지원)
    // 따라서 생성자 주입 또한 가능하게 된다 (코드 일관성 유지에 좋을 듯 함)
    private final EntityManager em;

    public MemberRepository(final EntityManager em) {
        this.em = em;
    }

    public void save(final Member meber) {
        em.persist(meber);
    }

    public Member findOne(final Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // from의 대상이 테이블이 아니라 엔티티 객체(Member.class)를 가르킴
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(final String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
