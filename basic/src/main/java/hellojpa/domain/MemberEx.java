package hellojpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class MemberEx extends BaseEntityEx {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_EX_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    /*@Column(name = "TEAM_ID")
    private Long teamId;*/

    // 하나의 팀에 여러 멤버들이 소속
    @ManyToOne
    @JoinColumn(name = "TEAM_EX_ID")
    private TeamEx teamEx;

    @Override
    public String toString() {
        return "MemberEx{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teamEx=" + teamEx +
                '}';
    }
}
