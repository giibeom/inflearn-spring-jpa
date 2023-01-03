package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // 웹서버가 올라오는 시점에 DB당 한개씩 생성하여 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");

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
            /*
            List<MemberEx> findMember = em.createQuery("select m from MemberEx m", MemberEx.class)
                    // 페이징 방언 (Dialect)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();

            for (MemberEx member : findMember) {
                System.out.println("memberId : " + member.getId());
                System.out.println("memberName : " + member.getName());
            }
            */

            TeamEx team = new TeamEx();
            team.setName("TeamA");
            em.persist(team);

            MemberEx member = new MemberEx();
            member.setName("Alex");
            // teamId.... 음... 객체지향스럽지 못하고 테이블에 맞춘 모델링
//            member.setTeamId(team.getId());
            member.setTeamEx(team);
            em.persist(member);

            team.getMembers().add(member);

//            em.flush();
//            em.clear();

            MemberEx findMember = em.find(MemberEx.class, member.getId());
            // Team 정보를 가져오려면?
//            TeamEx findTeam = em.find(TeamEx.class, findMember.getTeamId());
            TeamEx findTeam = findMember.getTeamEx();
            System.out.println("findTeam.getName() = " + findTeam.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
