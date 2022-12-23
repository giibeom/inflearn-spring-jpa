package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {
    public static final String MESSAGE = "재고 수량이 부족합니다.";

    public NotEnoughStockException() {
        super(MESSAGE);
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
