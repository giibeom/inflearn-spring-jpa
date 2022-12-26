package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired EntityManager em;

    @Test
    @DisplayName("Dirty Checking Test")
    void it_dirty_checking_example() throws Exception {
        Book book = em.find(Book.class, 1L);

        // Transaction
        // em.find()를 통해 영속성 객체를 가져왔으므로 여기에서는 변경 감지가 일어남
        book.setName("아무거나");

        // 로직이 종료되면 트랜잭션이 commit 될 때 flush가 날라가면서 변경된 값의 쿼리가 날라감
    }
}
