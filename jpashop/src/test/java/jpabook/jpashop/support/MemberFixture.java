package jpabook.jpashop.support;

import jpabook.jpashop.domain.member.Address;
import jpabook.jpashop.domain.member.Member;

import static jpabook.jpashop.support.AddressFixture.주소_안양;

public enum MemberFixture {
    회원_ALEX("Alex", 주소_안양.생성()),
    ;

    private String name;
    private Address address;

    MemberFixture(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Member 생성() {
        Member member = new Member();
        member.setName(this.name);
        member.setAddress(this.address);
        return member;
    }
}
