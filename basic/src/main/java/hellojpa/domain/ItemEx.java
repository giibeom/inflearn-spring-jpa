package hellojpa.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // dtype의 컬럼명을 바꿀수 있음 (default : DTYPE)
public class ItemEx {

    @Id @GeneratedValue
    @Column(name = "ITEM_EX_ID")
    private Long id;

    private String name;
    private int price;
}
