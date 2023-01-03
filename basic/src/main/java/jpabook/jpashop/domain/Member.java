package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    /*
        Member는 그냥 회원에 대한 정보만 가지고 있는게 깔끔한 설계이긴하다.
        해당 회원에 대한 주문 정보는 orders 테이블에 있는 memberId FK를 통해 사용하면된다.
        - 멤버를 찾아서 getOrders를 해서 주문 내역을 뿌리는 식의 설계는 관심사를 적절하게 끊어내지 못한 설계일 수 있다.

        주문이 필요하면 웬만하면 Order 객체로 시작하자 (Member는 order를 알 필요는 상황 싱 거의 없다)
    */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); // 하이버네이트 관례로는 초기화를 바로 해주는 것이다.
}
