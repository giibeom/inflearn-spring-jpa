package jpabook.jpashop.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateItemDto {
    private String name;
    private int price;
    private int stockQuantity;
}
