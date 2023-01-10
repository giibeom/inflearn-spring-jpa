package hellojpa.jpql;


import hellojpa.jpql.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class CaseMain {
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
//            member.setUsername("ALEX");
            member.setUsername("관리자");
            member.setAge(27);
            em.persist(member);
            em.flush();
            em.clear();

            // 일반적인 case 식
            String query =
                    "select " +
                            "case when m.age <= 10 then '학생요금' " +
                            "     when m.age >= 60 then '경로요금' " +
                            "     else '일반요금'" +
                            "end " +
                            "from Member m";


            // COALESCE : 하나씩 조회해서 null이 아니면 반환
            String query1 = "select coalesce(m.username, '이름 없는 회원') from Member m";

            // NULLIF : 두 값이 같으면 null로 반환, 다르면 첫번째 값 반환
            String query2 = "select nullif(m.username, '관리자') from Member m";

            List<String> result = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
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
