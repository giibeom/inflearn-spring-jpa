package jpabook.jpashop.service;

import jpabook.jpashop.domain.delivery.DeliveryStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.member.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static jpabook.jpashop.support.ItemFixture.상품_JPA;
import static jpabook.jpashop.support.MemberFixture.회원_ALEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*  테스트 요구사항
    - 상품 주문이 성공해야 한다.
    - 상품을 주문할 때 재고 수량을 초과하면 안 된다.
    - 주문 취소가 성공해야 한다.
*/
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 상품_주문 {
        private Long memberId;
        private Long itemId;

        @BeforeEach
        void setUp() {
            Member member = 회원_ALEX.생성();
            memberRepository.save(member);
            memberId = member.getId();

            Item item = 상품_JPA.책_생성();
            itemRepository.save(item);
            itemId = item.getId();
        }

        @Nested
        @DisplayName("정상적인 주문이 주어지면")
        class Context_with_valid_order_item {

            @Test
            @DisplayName("상품 주문이 성공한다")
            void it_success_order() throws Exception {
                Long orderId = orderService.order(memberId, itemId, 상품_JPA.재고_수량());

                Order getOrder = orderRepository.findOne(orderId);
                assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
                assertThat(getOrder.getOrderItems().size()).isEqualTo(상품_JPA.재고_수량());
            }
        }

        @Nested
        @DisplayName("재고 수량이 초과되면")
        class Context_with_not_enough_stock {

            @Test
            @DisplayName("NotEnoughStockException 예외를 던진다")
            void it_retruns_exception() throws Exception {
                assertThatThrownBy(
                        () -> orderService.order(memberId, itemId, 상품_JPA.재고_수량() + 1)
                )
                        .isInstanceOf(NotEnoughStockException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 상품_주문_취소 {

        @Nested
        @DisplayName("찾을 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private Long memberId;
            private Long itemId;
            private Long orderId;
            private final int orderCount = 10;

            @BeforeEach
            void setUp() {
                Member member = 회원_ALEX.생성();
                memberRepository.save(member);
                memberId = member.getId();

                Item item = 상품_JPA.책_생성();
                itemRepository.save(item);
                itemId = item.getId();

                orderId = orderService.order(memberId, itemId, orderCount);
            }
    
            @Test
            @DisplayName("주문이 취소되고 상품 수량이 올라간다")
            void it_cancel_order() throws Exception {
                int beforeStockQuantity = itemRepository.findOne(itemId).getStockQuantity();

                orderService.cancleOrder(orderId);

                int afterStockQuantity = itemRepository.findOne(itemId).getStockQuantity();

                assertThat(afterStockQuantity - beforeStockQuantity).isEqualTo(orderCount);
                assertThat(orderRepository.findOne(orderId).getStatus()).isEqualTo(OrderStatus.CANCEL);
            }
        }

        @Nested
        @DisplayName("배송 완료된 주문 id가 주어지면")
        class Context_with_complete_delivery {
            private Long memberId;
            private Long itemId;
            private Long orderId;
            private final int orderCount = 10;

            @BeforeEach
            void setUp() {
                Member member = 회원_ALEX.생성();
                memberRepository.save(member);
                memberId = member.getId();

                Item item = 상품_JPA.책_생성();
                itemRepository.save(item);
                itemId = item.getId();

                orderId = orderService.order(memberId, itemId, orderCount);

                Order order = orderRepository.findOne(orderId);
                order.getDelivery().setStatus(DeliveryStatus.COMP);
            }

            @Test
            @DisplayName("IllegalStateException 예외를 던진다")
            void it_returns_exception() throws Exception {
                assertThatThrownBy(
                        () -> orderService.cancleOrder(orderId)
                )
                        .isInstanceOf(IllegalStateException.class);
            }
        }
    }
}
