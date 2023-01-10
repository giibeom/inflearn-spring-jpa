package hellojpa.jpql;


import hellojpa.jpql.domain.Member;
import hellojpa.jpql.domain.MemberDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // 웹서버가 올라오는 시점에 DB당 한개씩 생성하여 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlTest");

        // 고객의 요청이 올 때 마다 쓰고 버림 (쓰레드 간에 공유하면 절대 안됨!)
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 된다 (단순 조회는 트랜잭션 필요 없음)
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("ALEX");
            member.setAge(27);
            em.persist(member);

            // 타입 정의가 애매할 때 (String & int)
            Query query = em.createQuery("select m.username, m.age from Member m");

            // 타입이 명확할 때
            TypedQuery<Member> typedQuery = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "ALEX");

            Member findMember = typedQuery.getSingleResult();

            // 프로젝션 - 여러 값 조회 : new 명령어로 조회 방법
            // 패키지 명을 포함한 전체 클래스 명을 입력하고, 순서와 타입이 일치하는 생성자가 필요하다
            List<MemberDTO> members = em.createQuery("select new hellojpa.jpql.domain.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            em.flush();
            em.clear();

            for (int i = 0; i < 100; i++) {
                Member m = new Member();
                m.setUsername("member" + i);
                m.setAge(i);
                em.persist(m);
            }

            // 페이징 : JPA가 두개의 메서드로 추상화시킴 (DB 방언에 따라 페이징 쿼리를 알아서 적용해줌)
            String jpql = "select m from Member m order by m.age desc";
            List<Member> result = em.createQuery(jpql, Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size() = " + result.size());

            for (Member m : result) {
                System.out.println("result = " + m);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
