package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/* 테스트 요구사항
    - 상품 등록을 성공해야 한다.
*/
@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 상품_등록은 {

        @Nested
        @DisplayName("정상적인 상품 정보가 주어지면")
        class Context_with_valid_item {
            private Item item;

            @BeforeEach
            void setUp() {
                item = new Book();
                item.addStock(100);
                item.setName("JPA");
                item.setPrice(30000);
            }

            @Test
            @DisplayName("상품을 생성한다")
            void it_save_item() throws Exception {
                itemService.saveItem(item);

                // 1차 캐시에 쌓여있는 DDL을 모두 DB로 반영하고, 영속성 컨텍스트를 비운다
                em.flush();

                assertThat(item).isEqualTo(itemRepository.findOne(item.getId()));
            }
        }
    }
}
