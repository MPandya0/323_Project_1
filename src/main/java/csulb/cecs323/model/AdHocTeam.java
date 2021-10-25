package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Ad Hoc Team")
public class AdHacTeam extends AuthoringEntity {
//    @Id
    @ManyToMany
    @JoinColumn(name="INDIVIDUAL_AUTHORS_EMAIL", referencedColumnName = "email", nullable = false)
    private List<AuthoringEntity> authoringEntityList = new ArrayList<AuthoringEntity>();

//    @Id
    @ManyToMany
    @JoinColumns()
    private List<>

}
