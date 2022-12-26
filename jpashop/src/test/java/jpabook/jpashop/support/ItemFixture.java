package jpabook.jpashop.support;

import jpabook.jpashop.domain.item.Book;

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

    public Book 책_생성() {
        Book book = new Book();
        book.setName(this.name);
        book.setPrice(this.price);
        book.setStockQuantity(this.stockQuantity);
        return book;
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
