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
import jpabook.jpashop.support.ItemFixture;
import jpabook.jpashop.support.MemberFixture;
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

        @Nested
        @DisplayName("정상적인 주문이 주어지면")
        class Context_with_valid_order_item {
            private final int 주문_수량 = 상품_JPA.재고_수량();
            private Member member;
            private Item item;

            @BeforeEach
            void setUp() {
                member = createMember(회원_ALEX);
                item = createBook(상품_JPA);
            }

            @Test
            @DisplayName("상품 주문이 성공한다")
            void it_success_order() throws Exception {
                Long 주문_id = orderService.order(member.getId(), item.getId(), 주문_수량);

                Order getOrder = orderRepository.findOne(주문_id);

                assertThat(getOrder.getStatus())
                        .as("상품 주문시 상태는 ORDER 이어야 한다")
                        .isEqualTo(OrderStatus.ORDER);
                assertThat(getOrder.getOrderItems())
                        .as("주문한 상품 종류 수가 정확해야 한다")
                        .hasSize(1);
                assertThat(getOrder.getTotalPrice())
                        .as("주문 가격은 가격 x 수량 이어야 한다")
                        .isEqualTo(상품_JPA.가격() * 주문_수량);
                assertThat(item.getStockQuantity())
                        .as("주문 수량만큼 재고가 줄어야 한다")
                        .isEqualTo(상품_JPA.재고_수량() - 주문_수량);
            }
        }

        @Nested
        @DisplayName("재고 수량이 초과되면")
        class Context_with_not_enough_stock {
            private final int 초과된_주문_수량 = 상품_JPA.재고_수량() + 1;
            private Member member;
            private Item item;

            @BeforeEach
            void setUp() {
                member = createMember(회원_ALEX);
                item = createBook(상품_JPA);
            }

            @Test
            @DisplayName("NotEnoughStockException 예외를 던진다")
            void it_retruns_exception() throws Exception {
                assertThatThrownBy(
                        () -> orderService.order(member.getId(), item.getId(), 초과된_주문_수량)
                )
                        .as("NotEnoughStockException 예외가 발생해야 한다")
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
            private final int orderCount = 10;
            private Item item;
            private Long orderId;

            @BeforeEach
            void setUp() {
                Member member = createMember(회원_ALEX);
                item = createBook(상품_JPA);
                orderId = orderService.order(member.getId(), item.getId(), orderCount);
            }


            @Test
            @DisplayName("주문이 취소되고 상품 수량이 올라간다")
            void it_cancel_order() throws Exception {
                int beforeStockQuantity = itemRepository.findOne(item.getId())
                        .getStockQuantity();

                orderService.cancleOrder(orderId);

                int afterStockQuantity = itemRepository.findOne(item.getId())
                        .getStockQuantity();

                assertThat(afterStockQuantity)
                        .as("주문이 취소된 상품은 그만큼 재고가 증가해야 한다")
                        .isEqualTo(beforeStockQuantity + orderCount);
                assertThat(orderRepository.findOne(orderId).getStatus())
                        .as("주문 취소 시 상태는 CANCLE 이어야 한다")
                        .isEqualTo(OrderStatus.CANCEL);
            }
        }

        @Nested
        @DisplayName("배송 완료된 주문 id가 주어지면")
        class Context_with_complete_delivery {
            private final int orderCount = 10;
            private Long orderId;

            @BeforeEach
            void setUp() {
                Member member = createMember(회원_ALEX);
                Item item = createBook(상품_JPA);
                orderId = orderService.order(member.getId(), item.getId(), orderCount);
                Order order = orderRepository.findOne(orderId);
                order.getDelivery().setStatus(DeliveryStatus.COMP);
            }

            @Test
            @DisplayName("IllegalStateException 예외를 던진다")
            void it_returns_exception() throws Exception {
                assertThatThrownBy(
                        () -> orderService.cancleOrder(orderId)
                )
                        .as("IllegalStateException이 발생해야 한다")
                        .isInstanceOf(IllegalStateException.class);
            }
        }
    }

    private Member createMember(MemberFixture memberFixture) {
        Member member = memberFixture.생성();
        memberRepository.save(member);
        return member;
    }

    private Book createBook(ItemFixture itemFixture) {
        Book book = itemFixture.책_생성();
        itemRepository.save(book);
        return book;
    }
}
