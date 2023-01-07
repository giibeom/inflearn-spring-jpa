package hellojpa;

import hellojpa.domain.MemberEx;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProxyJpaMain {

    public static void main(String[] args) {
        // 웹서버가 올라오는 시점에 DB당 한개씩 생성하여 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");

        // 고객의 요청이 올 때 마다 쓰고 버림 (쓰레드 간에 공유하면 절대 안됨!)
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 된다 (단순 조회는 트랜잭션 필요 없음)
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            MemberEx member = new MemberEx();
            member.setName("ALEX");

            em.persist(member);

            em.flush();
            em.clear();

//            MemberEx findMember = em.find(MemberEx.class, member.getId());
//            System.out.println("findMember = " + findMember);

            MemberEx findMember = em.getReference(MemberEx.class, member.getId());

            // id는 영속성 컨텍스트 1차캐시 되어있어서 select 쿼리 안나가네;;
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
