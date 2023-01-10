package hellojpa.jpql;

import hellojpa.jpql.domain.Member;
import hellojpa.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JoinAndSubqueryMain {
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

            member.changeTeam(team);

            em.persist(member);
            em.flush();
            em.clear();

            // 1. 내부 조인 : inner 생략 가능
//            String joinQuery = "select m from Member m inner join m.team t";

            // 2. 외부 조인 :  outer 생략 가능
//            String joinQuery = "select m from Member m left outer join m.team t";

            // 3. 세타 조인
//            String joinQuery = "select m from Member m, Team t where m.username = t.name";

            // 4. on 절을 활용한 조인 : JPA 2.1부터 지원
            String joinQuery = "SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'";

            List<Member> joinqueryResult = em.createQuery(joinQuery, Member.class)
                    .getResultList();

            em.clear();

            // 서브쿼리 : WHERE, HAVING, SELECT(하이버네이트)는 모두 사용 가능
            // 하지만.... FROM 절에서는 서브쿼리가 불가능 -> 조인절이나 네이티브 쿼리로 풀어야됨
            // 영한님은 보통 얼추 추려서 가져온 데이터를 애플리케이션 단에서 추출하여 사용하거나, 쿼리 두번 날린다고 함
            String subquery = "select (select avg(m1.age) From Member m1) as avgAge from Member m " +
                    "join Team t on m.username = t.name";

            List<Member> subqueryResult = em.createQuery(subquery, Member.class)
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
