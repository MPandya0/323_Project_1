package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Ad Hoc Team")
public class AdHocTeam extends AuthoringEntity {
//    @Id
//    @ManyToMany
//    @JoinColumn(name="INDIVIDUAL_AUTHORS_EMAIL", referencedColumnName = "email", nullable = false)
//    private List<AuthoringEntity> authoringEntityList = new ArrayList<AuthoringEntity>();

//    @ManyToMany(mappedBy = "INDIVIDUAL_AUTHORS_EMAIL")
//    private List<AuthoringEntity> authoringEntityList = new ArrayList<>();


//    @JoinTable(
//            name="INDIVIDUAL_AUTHORS_EMAIL",
//            joinColumns = @JoinColumn(name = "INDIVIDUAL_AUTHORS_EMAIL", referencedColumnName = "email"),
//            inverseJoinColumns = @JoinColumn()
//    )



//    @Id
//    @ManyToMany
//    @JoinColumns()
//    private List<>

    public AdHocTeam() {}

    public AdHocTeam(String email, String name) {
        super(email, name);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
