package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
public class MovieEx extends ItemEx {
    private String director;
    private String actor;
}
