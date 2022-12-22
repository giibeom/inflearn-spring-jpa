package jpabook.jpashop.domain.delivery;

import jpabook.jpashop.domain.member.Address;
import jpabook.jpashop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    // default인 ORDINAL은 숫자로 enum 값을 넣는데, 절대 사용하면 안됨(중간에 새로운 enum 값 들어갈 경우 다 꼬임)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP
}
