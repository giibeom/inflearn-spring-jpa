package hellojpa;

import hellojpa.domain.MemberEx;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPQLMain {

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

            // 아래에서 쿼리를 직접 실행하여 select를 해야 될 경우 적절하게 flush가 필요 (영속성 컨텍스트)
            em.flush();

            // 네이티브 쿼리 (이럴 경우에는 그냥 SpringJdbcTemplate나 MyBatis를 사용하는 것이 좀 더 편리할 듯
            em.createNativeQuery("select MEMBER_EX_ID, city, street, zipcode, USERNAME from MEMBEREX")
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
