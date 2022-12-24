package jpabook.jpashop.support;

import jpabook.jpashop.domain.member.Address;

public enum AddressFixture {
    주소_안양("안양시", "학의로", "77777"),
    ;

    private String city;
    private String street;
    private String zipcode;

    AddressFixture(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public Address 생성() {
        return new Address(this.city, this.street, this.zipcode);
    }
}
