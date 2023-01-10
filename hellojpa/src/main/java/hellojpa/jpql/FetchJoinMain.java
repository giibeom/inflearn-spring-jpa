package hellojpa.jpql;

import hellojpa.jpql.domain.Member;
import hellojpa.jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinMain {
    public static void main(String[] args) {
        // 웹서버가 올라오는 시점에 DB당 한개씩 생성하여 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlTest");

        // 고객의 요청이 올 때 마다 쓰고 버림 (쓰레드 간에 공유하면 절대 안됨!)
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 된다 (단순 조회는 트랜잭션 필요 없음)
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("ALEX1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("ALEX2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("ALEX3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m from Member m";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // ALEX1, 팀A -> SQL
                // ALEX2, 팀A -> 1차캐시
                // ALEX3, 팀B -> SQL

                // 회원 100명이 팀이 모두 다를경우 -> N + 1 문제 발생
            }

            System.out.println("========================================");

            // fetch join -> 멤버를 조회할 때 팀도 같이 긁어오라는 지시
            // 즉 지연 로딩으로 하지 않고 즉시 로딩(EAGER)으로 동작되도록 명령
            String fetchjoinQuery = "select m from Member m join fetch m.team";
            List<Member> fetchResult = em.createQuery(fetchjoinQuery, Member.class)
                    .getResultList();

            for (Member member : fetchResult) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // inner join으로 쿼리를 한방에 가져와서 영속성 컨텍스트에 등록
                // ALEX1, 팀A -> 1차캐시
                // ALEX2, 팀A -> 1차캐시
                // ALEX3, 팀B -> 1차캐시
            }

            System.out.println("========================================");

        /*
            Team -> Member (1:다 관계)
            fetch join에서 컬렉션 페치 조인(1:다 조인)인 경우 데이터 개수에 뻥튀기가 일어남
            따라서 distinct를 사용하면 JPA가 distinct로 가져온 데이터를 반환하기 전에,
            추가로 같은 식별자를 가진 값들에 대한 중복 제거를 실행 -> 애플리케이션에서 중복 제거
            (다:1인 경우에는 뻥튀기가 안되고 정상적으로 값만큼만 반환 됨) : Member -> Team
        */
            String distinctQuery = "select distinct t from Team t join fetch t.members";

            List<Team> distinctResult = em.createQuery(distinctQuery, Team.class)
                    .getResultList();

            // 결론: 패치 조인은 연관된 엔티티를 함께 조회(즉시 로딩)
            // -> 객체 그래프를 SQL 한번에 싹 조회하는 개념

            System.out.println("========================================");

            // 벌크 연산 : 영속성 컨텍스트를 무시하고 DB로 직접 쿼리가 나감 -> 따라서 실행 후 영속성 컨텍스트 초기화 해야 안전
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            // 영속성 컨텍스트를 초기화해야한다
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("findMember = " + findMember.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
