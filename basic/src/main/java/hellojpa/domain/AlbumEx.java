package hellojpa.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class AlbumEx extends ItemEx{

    private String artist;
}
