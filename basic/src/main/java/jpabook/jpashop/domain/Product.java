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

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Product {

    @Id @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    private String name;
    /*
    @ManyToMany(mappedBy = "products")
    private List<Product> products = new ArrayList<>();
    */

    @OneToMany(mappedBy = "product", fetch = LAZY)
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
