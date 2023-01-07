package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    // 관계형 DB를 객체에 맞춘 설계 -> 객체 그래프 탐색 불가능
//    @Column(name = "MEMBER_ID")
//    private Long memberId;

    // 객체 지향적으로 설계하려면 연관관계에 Member가 있어야 됨
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // OrderItem 데이터를 Order의 데이터 라이프사이클과 동일하게 맞추기 위한 용도(DDD - Aggregate Root 개념)
    @OneToMany(mappedBy = "order", fetch = LAZY, cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // Delivery 데이터를 Order의 데이터 라이프사이클과 동일하게 맞추기 위한 용도(DDD - Aggregate Root 개념)
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    // === 연관관계 편의 메서드 ===
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
