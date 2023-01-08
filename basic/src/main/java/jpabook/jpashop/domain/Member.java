package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;

    @Embedded
    private Address address;

    /*private String city;
    private String street;
    private String zipcode;*/

    /*
        Member는 그냥 회원에 대한 정보만 가지고 있는게 깔끔한 설계이긴하다.
        회원에 대한 주문 정보는 orders 테이블에 있는 memberId FK를 통해 사용하면된다.
        - 멤버를 찾아서 getOrders를 해서 주문 내역을 뿌리는 식의 설계는 관심사를 적절하게 끊어내지 못한 설계일 수 있다.

        주문이 필요할 땐 웬만하면 Order 객체로 시작하자 (Member는 order를 알 필요는 상황 상 거의 없다)
    */
    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<Order> orders = new ArrayList<>(); // 컬렉션은 바로 초기화 해주는 것이 하이버네이트 관례이다

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    /*
        중간 테이블은 매핑 정보만 넣을 수 있고 추가정보를 넣을 수 없음
        따라서 실무에서 @ManyToMany는 사용 안함
    */
    /*
    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT")
    private List<Product> products = new ArrayList<>();
    */
    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
