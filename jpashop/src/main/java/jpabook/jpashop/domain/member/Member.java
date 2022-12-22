package jpabook.jpashop.domain.member;

import jpabook.jpashop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 연관관계의 주인을 Order.member(FK)로 설정하고, 나는 주인에 의해 매핑된 거울일 뿐이다
    // 즉 해당 데이터는 읽기 전용이 된다
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
