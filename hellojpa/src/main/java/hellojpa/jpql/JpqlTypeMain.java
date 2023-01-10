package hellojpa.jpql;

import hellojpa.jpql.domain.Member;
import hellojpa.jpql.domain.MemberType;
import hellojpa.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlTypeMain {
    public static void main(String[] args) {
        // 웹서버가 올라오는 시점에 DB당 한개씩 생성하여 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlTest");

        // 고객의 요청이 올 때 마다 쓰고 버림 (쓰레드 간에 공유하면 절대 안됨!)
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 된다 (단순 조회는 트랜잭션 필요 없음)
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("ALEX");
            member.setAge(27);
            member.setType(MemberType.ADMIN);

            member.changeTeam(team);

            em.persist(member);
            em.flush();
            em.clear();

            // 문자, 숫자, booelean
            String query = "select m.username, 'HELLO', 27, true from Member m " +
                    // WHERE 절 안에 하드코딩 해야될 경우는 enum의 패키지 명까지 다 적어줘야함
//                    "where m.type = hellojpa.jpql.domain.MemberType.ADMIN";
                    "where m.type = :userType";

            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("username = " + objects[0]);
                System.out.println("문자 = " + objects[1]);
                System.out.println("숫자 = " + objects[2]);
                System.out.println("boolean = " + objects[3]);
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
