package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Ad Hoc Team")
public class AdHocTeam extends AuthoringEntity {

    @ManyToMany
    @JoinTable(
            name="AD_HOC_TEAMS_MEMBER",
            joinColumns=@JoinColumn(name="AD_HOC_TEAMS_EMAIL", referencedColumnName="EMAIL"),
            inverseJoinColumns=@JoinColumn(name="INDIVIDUAL_AUTHORS_EMAIL", referencedColumnName="EMAIL"))
    List<IndividualAuthor> individualAuthorList;

    public AdHocTeam() {}

    public AdHocTeam(String email, String name) {
        super(email, name);
    }

    public void insertAuthor(IndividualAuthor individualAuthor) {
        individualAuthorList.add(individualAuthor);
    }

    public boolean removeAuthor(IndividualAuthor individualAuthor) {
        return individualAuthorList.remove(individualAuthor);
    }

    public boolean containsAuthor(IndividualAuthor individualAuthor) {
        return individualAuthorList.contains(individualAuthor);
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
