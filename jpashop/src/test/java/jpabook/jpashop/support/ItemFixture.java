package jpabook.jpashop.support;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;

public enum ItemFixture {
    상품_JPA("JPA 프로그래밍", 30000, 10000),
    ;

    private String name;
    private int price;
    private int stockQuantity;

    ItemFixture(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Item 책_생성() {
        Item item = new Book();
        item.setName(this.name);
        item.setPrice(this.price);
        item.setStockQuantity(this.stockQuantity);
        return item;
    }

    public String 이름() {
        return this.name;
    }

    public int 가격() {
        return this.price;
    }

    public int 재고_수량() {
        return this.stockQuantity;
    }
}
