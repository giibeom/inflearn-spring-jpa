package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // 웹서버가 올라오는 시점에 DB당 한개씩 생성하여 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 고객의 요청이 올 때 마다 쓰고 버림 (쓰레드 간에 공유하면 절대 안됨!)
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 된다 (단순 조회는 트랜잭션 필요 없음)
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*
            Member member = new Member();
            member.setId(1L);
            member.setName("ALEX");

            em.persist(member);
            */
            /*
            Member member = em.find(Member.class, 1L);
            member.setName("ALEX UPDATE");
            */
            /*
            em.remove(member);
            */
            List<Member> findMember = em.createQuery("select m from Member m", Member.class)
                    // 페이징 방언 (Dialect)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();

            for (Member member : findMember) {
                System.out.println("memberId : " + member.getId());
                System.out.println("memberName : " + member.getName());
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
